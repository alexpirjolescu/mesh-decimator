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

// Class defining a generic vector with 3 components. Can be a direction, a coordinate or anything else


public class Vertex extends vec3d{
    private int id;
    private List<Integer> connE = new ArrayList<>();
    private List<Face> partOfF = new ArrayList<>();
    
    public Vertex(int id, float x, float y, float z)
    {
        super(x,y,z);
        this.id = id;
    }

    public int getID(){
        return this.id;
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
