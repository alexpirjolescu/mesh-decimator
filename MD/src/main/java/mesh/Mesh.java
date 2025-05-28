package mesh;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import java.util.*;
/**
 *
 * @author alexp
 */
public class Mesh {
    private List<Vertex> vertices= new ArrayList<>();
    private List<Face> faces = new ArrayList<>();
    private List<Edge> edges = new ArrayList<>();

    private record edges(int v1, int v2){}
    private Map <edges, Integer> edgesMap = new HashMap<>();
    
    public List<Vertex> getVertices()
    {
        return vertices;
    }
    
    public Vertex getV(int i)
    {
        return vertices.get(i);
    }
                 
    
    public List<Face> getFaces()
    {
        return faces;
    }
    public List<Edge> getEdges()
    {
        return edges;
    }

    public void addVertex(Vertex v){
        vertices.add(v);
    }
    public int getVListSize()
    {
        return vertices.size();
    }
    public void addFace(Face f){
        faces.add(f);
        passVconn(f);
        passFconn(f);
    }
    public void passVconn(Face f){
        //for each vertex add the other 2 v that share an edge
        getV(f.getV1()).addVconn(f.getV2());
        getV(f.getV1()).addVconn(f.getV3());
        
        getV(f.getV2()).addVconn(f.getV1());
        getV(f.getV2()).addVconn(f.getV3());
        
        getV(f.getV3()).addVconn(f.getV1());
        getV(f.getV3()).addVconn(f.getV2());
        
        
    }
    
    public void passFconn(Face f){
        // for eahc vertex add the face that it is part of to list
        getV(f.getV1()).addFconn(f);
        getV(f.getV2()).addFconn(f);
        getV(f.getV3()).addFconn(f);
    }
    
}
