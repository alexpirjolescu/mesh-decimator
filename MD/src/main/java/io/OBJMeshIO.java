/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package io;
import java.io.*;
import mesh.*;
/**
 *
 * @author alexp
 */

 // General interface for 3D model loading & saving
interface MeshIO {  
        Mesh load(File file) throws IOException, FileNotFoundException; // Function for loading the file
        void save (Mesh mesh, File file) throws IOException, FileNotFoundException; // Function for saving a file
 }

public class OBJMeshIO implements MeshIO {

    @Override
    public Mesh load(File file) throws IOException, FileNotFoundException {

        Mesh mesh = new Mesh(); // Creates a new mesh object

        if(file.isFile()){
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) { // Creates reader for obj file
                String line;
                while ((line = reader.readLine()) != null) { // While the next line isnt null
                    line = line.trim(); // Strip the line
                    if (line.startsWith("v ")) { // If the line is identified as a vertex
                        String[] parts = line.split("\\s+"); // Split the string line into components
                        float x = Float.parseFloat(parts[1]); // Grab x
                        float y = Float.parseFloat(parts[2]); // Grab y
                        float z = Float.parseFloat(parts[3]); // Grab z
                        mesh.addVertex(new Vertex(mesh.getVListSize(), x, y, z)); // Add vertex with extracted x/y/z to the mesh object
                    } else if (line.startsWith("f ")) { // If the line is identified as a face
                        String[] parts = line.split("\\s+"); // Split the string line into components
                        int[] vals = new int[parts.length-1]; // allocate space for the vertex IDs
                        for(int i=0; i<vals.length; i++){ // parse all vertex IDs
                            vals[i] = parseIndex(parts[i+1]);
                        }
                        int anchor = vals[0]; // use first vertex as the anchor
                        for(int i=0; i<vals.length-2; i++){ // for all vertex pairs that dont include the anchor
                            mesh.addFace(new Face(mesh.sizeFList(), anchor, vals[i+1], vals[i+2])); //Add triangular face using anchor and the next pair
                        }
                    }
                }
            } catch(Exception e) { 
                throw new IOException("Failed to read from provided file."); // If something goes wrong with reading the file but the file exists throws IOException
            }
        } else {
            throw new FileNotFoundException("File does not exist or is a directory."); // If the provided file doesn't exist or just isnt a file throws FileNotFoundException
        }

        return mesh; // Returns the constructed mesh
    }

    private int parseIndex(String part) {
        // Handles cases like "f 1", "f 1/1", or "f 1/1/1"
        return Integer.parseInt(part.split("/")[0]) - 1;
    }

    @Override
    public void save(Mesh mesh, File file) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
            for (Vertex v : mesh.getVertices()) {
                if (v.getVersion() == 0){
                    writer.printf("v %.6f %.6f %.6f%n", v.getX(), v.getY(), v.getZ());
                }
            }
            for (Face f : mesh.getFaces()) {
                if (f.isValid()){
                    writer.printf("f %d %d %d%n", f.getV1() + 1, f.getV2() + 1, f.getV3() + 1);
                }
            }
        } catch(Exception e){
            throw new IOException("Failed to write to file.");
        }
    }
    
}
