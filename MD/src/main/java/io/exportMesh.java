/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package io;
import java.util.HashMap;
import java.util.Map;
import mesh.*;

/**
 *
 * @author alexp
 */


public class exportMesh {

    public Mesh mesh;
    // mao from old id to new id 
    private Map<Integer, Integer> idMap= new HashMap<>();

    public exportMesh(Mesh mesh) {
        this.mesh = mesh;
        prepareMesh();
    }


    private void prepareMesh() {
        // put valid vertecies and valid faces in new lists.
        for(Vertex v : mesh.getVertices()) {
            if(v.getVersion() == 0){

                if (!idMap.containsKey(v.getID())) {
                    idMap.put(v.getID(), idMap.size());
                }
          }
        }
        for (Face face : mesh.getFaces()) {
            if(face.isValid()) {
                remapFace(face);
            }
        }
        // 
    }
    private void remapFace(Face face) {
        int[] verts = face.getVertices();
        for (int v : verts) {
            if(idMap.containsKey(v)) {
                face.replaceVertex(v, idMap.get(v));
            }
            else {
                System.out.println("Error: exportMesh.remapFace: v not in idMap:" + v);
            }
        }
    }
}
