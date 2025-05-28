package mesh;

import java.util.ArrayList;
import java.util.List;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author alexp
 */
public class Vertex {
    public int id;
    public float x,y,z;
    private List<Integer> connE = new ArrayList<>();
    private List<Face> partOfF = new ArrayList<>();
    
    public Vertex(int id, float x, float y, float z)
    {
        this.id = id;
        this.x = x;
        this.y = y;
        this.z = z;
    }
    public void addVconn(int v)
    {
        if(!(connE.contains(v)))
            connE.add(v);
    }
    public void addFconn(Face f)
    {
        if(!(partOfF.contains(f)))
            partOfF.add(f);
    }
    public List<Face> combineF(Vertex v1, Vertex v2){
        List<Face> fcomb = v1.partOfF;
        for(Face f : v2.partOfF){
            if(!(fcomb.contains(f)))
                fcomb.add(f);
        }
        return fcomb;
    }
    public List<Integer> combineE(Vertex v1,Vertex v2){
        List<Integer> ecomb = v1.connE;
        for(Integer i : v2.connE){
            if(!(ecomb.contains(i)))
                ecomb.add(i);
        }
        ecomb.remove(v1.id);
        ecomb.remove(v2.id);
        return ecomb;
    }
        
    
}
