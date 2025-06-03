package simplify;
import java.util.HashMap;
import java.util.Map;
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
    private Map<Integer, Quadric> vertexQ = new HashMap<>();
    //private GFG minHEdgeCost = new GFG(1000);
    private record edges(int v1, int v2){}
    public Map<edges, Quadric> edgeQ = new HashMap<>();
    
    public QEMUtils(Mesh mesh){
        for (Face face : mesh.getFaces()){
            Vertex v1 = mesh.getV(face.getV1());
            Vertex v2 = mesh.getV(face.getV2());
            Vertex v3 = mesh.getV(face.getV3());
            
            vec4d plane = computePlane(v1,v2,v3);
            Quadric faceQ = Quadric.fromPlane(plane);
            
            vertexQ.computeIfAbsent(v1.getID(), id -> new Quadric()).add(faceQ);
            vertexQ.computeIfAbsent(v2.getID(), id -> new Quadric()).add(faceQ);
            vertexQ.computeIfAbsent(v3.getID(), id -> new Quadric()).add(faceQ);
        }
        
        for (Face face : mesh.getFaces()){
            //   verts[1] <= verts[2] <= verts[3]
            int[] verts = sortThree(face.getV1(), face.getV2(), face.getV3());
            
            edges e = new edges(verts[1], verts[2]);
            if(!(edgeQ.containsKey(e))){
                edgeQ.put(e, Quadric.from2(vertexQ.get(e.v1),vertexQ.get(e.v2)));
                //edgeQ.put(e, (vertexQ.get(e.v1)+vertexQ.get(e.v2)));
            }  
            e = new edges(verts[1], verts[3]);
            if(!(edgeQ.containsKey(e))){
                edgeQ.put(e, Quadric.from2(vertexQ.get(e.v1),vertexQ.get(e.v2)));
                //edgeQ.put(e, (vertexQ.get(e.v1)+vertexQ.get(e.v2)));
            } 
            e = new edges(verts[2], verts[3]);
            if(!(edgeQ.containsKey(e))){
                edgeQ.put(e, Quadric.from2(vertexQ.get(e.v1),vertexQ.get(e.v2)));
            } 
        }
    
       
    }  
    
    
    
    
    
    private vec4d computePlane(Vertex v1, Vertex v2, Vertex v3) {

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
    
}
