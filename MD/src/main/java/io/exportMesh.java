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
        this.mesh = new Mesh(mesh);
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
        // map all three vertices at once, so a new id can't be
        // mistaken for an old id that still has to be remapped
        int[] verts = face.getVertices();
        int[] mapped = new int[3];
        for (int i = 0; i < 3; i++) {
            Integer newId = idMap.get(verts[i]);
            if (newId == null) {
                System.out.println("Error: exportMesh.remapFace: v not in idMap:" + verts[i]);
                face.setValid(false);
                return;
            }
            mapped[i] = newId;
        }
        face.setVertices(mapped[0], mapped[1], mapped[2]);
    }
}
