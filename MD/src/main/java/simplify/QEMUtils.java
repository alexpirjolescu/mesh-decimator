package simplify;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import mesh.*;
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**e
 *
 * @author alexp
 */

public class QEMUtils {
    
    //map vertex to quadric
    private Map<Integer, Quadric> vertexQuadrics = new HashMap<>();

    private Map<Integer, Quadric> faceQuadrics = new HashMap<>();

    PriorityQueue<EdgeCollapse> heap = new PriorityQueue<>(Comparator.comparingDouble(e -> e.cost));

    private Map<Integer, Set<Integer>> vertexToFaces = new HashMap<>();
   
    private Set<Integer> invalidFaces = new HashSet<>();

    public Mesh mesh;

    public QEMUtils(Mesh mesh){
        this.mesh = new Mesh(mesh);
        for (Face face : this.mesh.getFaces()){
            Vertex v1 = this.mesh.getV(face.getV1());
            Vertex v2 = this.mesh.getV(face.getV2());
            Vertex v3 = this.mesh.getV(face.getV3());
            //populate vertexToFaces
            vertexToFaces.computeIfAbsent(v1.getID(), k -> new HashSet<>()).add(face.getId());
            vertexToFaces.computeIfAbsent(v2.getID(), k -> new HashSet<>()).add(face.getId());
            vertexToFaces.computeIfAbsent(v3.getID(), k -> new HashSet<>()).add(face.getId());

            //compute plane 
            vec4d plane = computePlane(v1,v2,v3);
            //compute quadric from plane
            Quadric faceQ = Quadric.fromPlane(plane);

            //populate faceQuadrics
            faceQuadrics.put(face.getId(), faceQ);
            //add quadric of face to quadric of vertex
            addFaceQuadric(vertexQuadrics, v1, faceQ);
            addFaceQuadric(vertexQuadrics, v2, faceQ);
            addFaceQuadric(vertexQuadrics, v3, faceQ);
            
            
        }
        initHeap(this.mesh);
    
    }  


     private void initHeap(Mesh mesh){
        for (Edge edge : mesh.getEdges()) {
            EdgeCollapse e = toEdgeCollapse(mesh, edge);
            if (e != null){
                heap.add(e);
            }
        }
    }

    private EdgeCollapse toEdgeCollapse(Mesh mesh, Edge edge) {
        Vertex v1 = mesh.getV(edge.v1);
        Vertex v2 = mesh.getV(edge.v2);

        Quadric q1 = vertexQuadrics.get(v1.getID());
        Quadric q2 = vertexQuadrics.get(v2.getID());

        if (q1 == null || q2 == null) return null;

        Quadric Q = new Quadric(q1);
        Q.add(q2);
        vec3d fallback = vec3d.midpoint(v1, v2);
        double[] fallbackArr = new double[] { fallback.getX(), fallback.getY(), fallback.getZ() };
        double[] optimalPosArray = Q.solveOptimalPosition(fallbackArr); 
        vec3d vNew = new vec3d(optimalPosArray[0], optimalPosArray[1], optimalPosArray[2]);
        double cost = Q.evaluate(vNew);
        return new EdgeCollapse(v1, v2, optimalPosArray, cost);

    }
    
    
    
    public static vec4d computePlane(Vertex v1, Vertex v2, Vertex v3) {

        vec3d u = v2.minus(v1);
        vec3d v = v3.minus(v1);

        vec3d component = u.cross(v);
        component = component.getNormalized();

        double a = -component.dot(v1);

        return new vec4d(component, a);
    }
    
    
    private int[] sortThree(int a, int b, int c)
    {
        int temp;
        if (a>b) {temp =a; a=b; b=temp;}
        if (a>c) { temp =a; a=c; c=temp;}
        if (b>c) {temp=b; b=c; c=temp;}
        
        return new int[] {a,b,c};
    }

    private void addFaceQuadric(Map<Integer, Quadric> vertexQ, Vertex v, Quadric faceQ) {
    vertexQ.compute(v.getID(), (id, existingQ) -> {
        if (existingQ == null) return new Quadric(faceQ); // make a copy
        existingQ.add(faceQ); // add in-place
        return existingQ;
    });
    }

    public void decimateOneStep(){
        while (!heap.isEmpty()) {
            EdgeCollapse collapse = heap.poll();
            
            if(!isValid(collapse)) continue;

            collapseEdge(collapse);

            break;


        }
    }

    private void collapseEdge(EdgeCollapse collapse) { 
        Vertex v1 = collapse.v1;
        Vertex v2 = collapse.v2;
        vec3d vNewPos = collapse.optimalPos;

        Vertex vNew = new Vertex(mesh.getVListSize(), vNewPos.getX(), vNewPos.getY(), vNewPos.getZ());
        mesh.addVertex(vNew);
        // increment Version to ensure edge validity
        v1.incrementVersion();
        v2.incrementVersion();
        v1.setAlive(false);
        v2.setAlive(false);

        // create union face list of v1 v2;
        int[] unionFaces = getUnion(v1, v2);
        

        //subtract quadricOfFace from vertexQuadrics
        subtractCollapsedQuadrics(unionFaces);
        
        
        
        // replace vertex for each face
        replaceCollapsedFaces(unionFaces, v1.getID(), v2.getID(), vNew.getID());
        
        // check for degenerate faces.
        checkDegenarateFaces(unionFaces);
            // set invalid faces
        //remove quadrics of invalid faces from faceQuadrics
        Iterator<Integer> it = invalidFaces.iterator();
        while (it.hasNext()) {
            int face = it.next();
            faceQuadrics.remove(face);
            it.remove();
        }
        unionFaces = removeInvalidFaces(unionFaces, invalidFaces);


        // compute quadrics for new faces 
        computeNewQuadrics(unionFaces);
                // add quadrics to vertexQuadrics
        // update heap 
        int[] adjacentVertices = getAdjacentVertices(unionFaces,vNew.getID()); 

        // get list of V
        updateHeap(adjacentVertices, vNew.getID());
        

        // for face : union -> face.replacevertex(v, )
        //create new Vertex. 
        // for mesh
    }

    private void updateHeap(int[] adjacentVertices, int vnew) {
        // create egde collapse for each vnew - adjvert pair
        //for each edge add edge collapse
        //get all new edges from 
        for (int vAdj : adjacentVertices) {
            int v1 = Math.min(vAdj, vnew);
            int v2 = Math.max(vAdj, vnew);
            Edge edge = new Edge(mesh.getEdges().size(), v1, v2);
            mesh.addEdge(edge);
            EdgeCollapse e= toEdgeCollapse(mesh, edge);
            if (e != null) {
                heap.add(e);
            }
        }
        

    }

    private int[] getAdjacentVertices(int[] unionFaces, int vnew) {
        Set<Integer> adjacentVerts = new HashSet<>();
        for (int faceIndex : unionFaces) { 
            Face face = mesh.getF(faceIndex);
            int[] faceVerts = face.getVertices();

            // Only process if vnew is in this face
            if (Arrays.stream(faceVerts).anyMatch(v -> v == vnew)) {
                for (int v : faceVerts) {
                    if (v != vnew) {
                        adjacentVerts.add(v);
                    }
                }
            }
        }
        return adjacentVerts.stream().mapToInt(Integer::intValue).toArray();
    }


    private void computeNewQuadrics(int[] unionFaces) {
        for (int faceIndex : unionFaces) {  
            Face face = mesh.getF(faceIndex);
            Vertex v1 = mesh.getV(face.getV1());
            Vertex v2 = mesh.getV(face.getV2());
            Vertex v3 = mesh.getV(face.getV3());
            //populate vertexToFaces
            vertexToFaces.computeIfAbsent(v1.getID(), k -> new HashSet<>()).add(face.getId());
            vertexToFaces.computeIfAbsent(v2.getID(), k -> new HashSet<>()).add(face.getId());
            vertexToFaces.computeIfAbsent(v3.getID(), k -> new HashSet<>()).add(face.getId());

            //compute plane 
            vec4d plane = computePlane(v1,v2,v3);
            //compute quadric from plane
            Quadric faceQ = Quadric.fromPlane(plane);

            //populate faceQuadrics
            faceQuadrics.put(face.getId(), faceQ);
            //add quadric of face to quadric of vertex
            addFaceQuadric(vertexQuadrics, v1, faceQ);
            addFaceQuadric(vertexQuadrics, v2, faceQ);
            addFaceQuadric(vertexQuadrics, v3, faceQ);
            

    }
    }

    private static int[] removeInvalidFaces(int[] unionFaces, Set<Integer> invalidFaces) {
        return Arrays.stream(unionFaces)
                    .filter(face -> !invalidFaces.contains(face))
                    .toArray();
    }

    private void checkDegenarateFaces(int[] unionFaces) {
        for (int faceIndex : unionFaces) {
            Face face = mesh.getF(faceIndex);
            if (face.isDegenerate()) {
                face.setValid(false);
                invalidFaces.add(faceIndex);
            }
            
        }
    }

    private void replaceCollapsedFaces(int[] unionFaces, int v1, int v2, int vnew) {
        //shortcut 
        for (int faceIndex : unionFaces) {
            mesh.getF(faceIndex).replaceVertex(v1, vnew);
            mesh.getF(faceIndex).replaceVertex(v2, vnew);
        }
    }

    

    private void subtractCollapsedQuadrics(int [] unionFaces) {
        //for each face in unionFaces subtract quadric of face from quadric of    
        for (int faceIndex : unionFaces) {
            Quadric faceQuadric = faceQuadrics.get(faceIndex);
            if (faceQuadric == null) continue;

            List<Integer> faceVertices = getVerticesOfFace(faceIndex);
            for (int vertex : faceVertices) {
                Quadric vertexQuadric = vertexQuadrics.get(vertex);
                if (vertexQuadric != null) {
                    vertexQuadric.subtract(faceQuadric);
                }
            }
        }

    }

    private List<Integer> getVerticesOfFace(int faceIndex) {
        List<Integer> verts = new ArrayList<>();
        verts.add(mesh.getF(faceIndex).getV1());
        verts.add(mesh.getF(faceIndex).getV2());
        verts.add(mesh.getF(faceIndex).getV3());
        return verts;
    }


    private boolean isValid(EdgeCollapse ec) {
        return ec.v1.isAlive() && ec.v2.isAlive()
            && ec.v1.getVersion() == ec.v1Version
            && ec.v2.getVersion() == ec.v2Version;
    }

    private int[] getUnion(Vertex v1, Vertex v2) {
        Set<Integer> unionSet = new HashSet<>();
        if(vertexToFaces.containsKey(v1.getID())) {
            unionSet.addAll(vertexToFaces.get(v1.getID()));
        }
        if(vertexToFaces.containsKey(v2.getID())) {
            unionSet.addAll(vertexToFaces.get(v2.getID()));
        }
        return unionSet.stream().mapToInt(Integer::intValue).toArray();
    }
}
    

