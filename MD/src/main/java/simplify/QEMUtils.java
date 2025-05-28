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
            
            double[] plane = computePlane(v1,v2,v3);
            Quadric faceQ = Quadric.fromPlane(plane[0], plane[1], plane[2], plane[3]);
            
            vertexQ.computeIfAbsent(v1.id, id -> new Quadric()).add(faceQ);
            vertexQ.computeIfAbsent(v2.id, id -> new Quadric()).add(faceQ);
            vertexQ.computeIfAbsent(v3.id, id -> new Quadric()).add(faceQ);
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
    
    
    
    
    
    private double[] computePlane(Vertex v1, Vertex v2, Vertex v3) {
        // Compute normal
        float[] u = {v2.x - v1.x, v2.y - v1.y, v2.z - v1.z};
        float[] v = {v3.x - v1.x, v3.y - v1.y, v3.z - v1.z};

        double a = u[1]*v[2] - u[2]*v[1];
        double b = u[2]*v[0] - u[0]*v[2];
        double c = u[0]*v[1] - u[1]*v[0];

        // Normalize
        double length = Math.sqrt(a*a + b*b + c*c);
        if (length == 0) return new double[] {0, 0, 0, 0};

        a /= length;
        b /= length;
        c /= length;

        // Compute d from point on plane
        double d = -(a*v1.x + b*v1.y + c*v1.z);
        return new double[] {a, b, c, d};
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
