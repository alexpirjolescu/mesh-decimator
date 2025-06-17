package mesh;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import java.util.*;
import java.util.stream.Collectors;
/**
 *
 * @author alexp
 */
public class Mesh {
    private List<Vertex> vertices;
    private List<Face> faces;
    private List<Edge> edges;
    private Set<Edge> edgeSet;

    //private record edges(int v1, int v2){}
    //private Map <edges, Integer> edgesMap = new HashMap<>();
    public Mesh() {
        this.vertices= new ArrayList<>();
        this.faces = new ArrayList<>();
        this.edges = new ArrayList<>();
        this.edgeSet = new HashSet<>();
    }

    public Mesh(Mesh mesh) {
        this.vertices = mesh.getVertices().stream()
            .map(Vertex::new)
            .collect(Collectors.toList());
            
        this.faces = mesh.getFaces().stream()
            .map(Face::new)
            .collect(Collectors.toList());
            
        this.edges = mesh.getEdges().stream()
            .map(Edge::new)
            .collect(Collectors.toList());
            
        this.edgeSet = new HashSet<>(this.edges);
    }
    
    
    public Vertex getV(int i)
    {
        return vertices.get(i);
    }

    public Face getF(int i) {
        return faces.get(i);
    }

    public List<Vertex> getVertices()
    {
        return vertices;
    }
                 
    public List<Face> getFaces()
    {
        return faces;
    }
    
    public List<Edge> getEdges()
    {
        return edges;
    }

    public Set<Edge> getEdgeSet() {
        return edgeSet;
    }

    public void addVertex(Vertex v){
        vertices.add(v);
    }
    
    public void addFace(Face f){
        faces.add(f);
        addEdges(f);
    }

    public void addEdge(Edge e) {
        if(!edgeSet.contains(e)) {
            edgeSet.add(e);
            edges.add(e);
        }
    }

    public int getVListSize()
    {
        return vertices.size();
    }

    public int sizeFList() {
        return faces.size();
    }



    private void addEdges(Face f) {
        int[] verts = f.getVertices();
        
        int[][] edgePairs = {
            {verts[0], verts[1]},
            {verts[0], verts[2]},
            {verts[1], verts[2]}
        };
        for (int[] pair : edgePairs) {
            int v1 = Math.min(pair[0], pair[1]);
            int v2 = Math.max(pair[0], pair[1]);

            Edge tempEdge = new Edge(-1, v1, v2);
            if(!edgeSet.contains(tempEdge)) {
                Edge newEdge = new Edge(edges.size(), v1, v2);
                edgeSet.add(newEdge);
                edges.add(newEdge);
            } 
        }
    }
    

    
}
