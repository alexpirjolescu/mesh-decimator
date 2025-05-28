/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package io;
import java.io.*;
import java.util.*;
import mesh.*;
/**
 *
 * @author alexp
 */

interface MeshIO {
        Mesh load(File file) throws IOException;
        void save (Mesh mesh, File file) throws IOException;
 }

public class OBJMeshIO implements MeshIO {

    @Override
    public Mesh load(File file) throws IOException {
        Mesh mesh = new Mesh();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.startsWith("v ")) {
                    String[] parts = line.split("\\s+");
                    float x = Float.parseFloat(parts[1]);
                    float y = Float.parseFloat(parts[2]);
                    float z = Float.parseFloat(parts[3]);
                    mesh.addVertex(new Vertex(mesh.getVListSize(), x, y, z));
                } else if (line.startsWith("f ")) {
                    String[] parts = line.split("\\s+");
                    int v1 = parseIndex(parts[1]);
                    int v2 = parseIndex(parts[2]);
                    int v3 = parseIndex(parts[3]);
                    mesh.addFace(new Face(v1, v2, v3));
                }
            }
        }

        return mesh;
    }

    private int parseIndex(String part) {
        // Handles cases like "f 1", "f 1/1", or "f 1/1/1"
        return Integer.parseInt(part.split("/")[0]) - 1;
    }

    @Override
    public void save(Mesh mesh, File file) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
            for (Vertex v : mesh.getVertices()) {
                writer.printf("v %.6f %.6f %.6f%n", v.x, v.y, v.z);
            }
            for (Face f : mesh.getFaces()) {
                // Note: OBJ uses 1-based indexing
                writer.printf("f %d %d %d%n", f.getV1() + 1, f.getV2() + 1, f.getV3() + 1);
            }
        }
    }
    
}
