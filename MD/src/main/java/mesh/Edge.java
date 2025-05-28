/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mesh;

import java.util.Objects;

/**
 *
 * @author alexp
 */
public class Edge {
    public int v1, v2;
    
    
    public Edge(int v1, int v2)
    {
        this.v1 = v1;
        this.v2 = v2;
    
    }

    //equals method
    @Override
    public boolean equals(Object o)
    {
        if(this ==o) return true;
        if(!(o instanceof Edge )) return false;
        Edge e = (Edge) o;
        // we assume values are ordered
        return v1 == e.v1 && v2 == e.v2;
     
    }
    
    @Override
    public int hashCode() {
        
        return Objects.hash(v1,v2);
    }
}
