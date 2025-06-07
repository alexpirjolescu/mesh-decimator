/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package simplify;
import mesh.*;
/**
 *
 * @author alexp
 */
public class EdgeCollapse {
    public Vertex v1;
    public Vertex v2;
    vec3d optimalPos;
    public double cost;
    public int v1Version;
    public int v2Version;
    
    public EdgeCollapse(Vertex v1, Vertex v2, double[] posArray, double cost) {
        this.v1 = v1;
        this.v2 = v2;
        if(posArray != null && posArray.length == 3) {
            this.optimalPos = new vec3d(posArray[0], posArray[1], posArray[2]);
        } else {
            this.optimalPos = vec3d.midpoint(v1, v2);  
        }
        this.cost = cost;

        this.v1Version = v1.getVersion();
        this.v2Version = v2.getVersion();

    }
    // this.v1Version ??
}
