/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.md;

import io.OBJMeshIO;
import java.io.File;
import java.net.URL;
import javax.swing.JFrame;
import mesh.Mesh;
import FrameViewer.*;
/**
 *
 * @author alexp
 */
public class MD {

    public static void main(String[] args) throws Exception {
        URL resourceUrl = MD.class.getResource("/HumanMesh.obj");
        if (resourceUrl ==null)
            throw new RuntimeException("Mesh.obj not found in resources");
        
        File file = new File(resourceUrl.toURI());
        OBJMeshIO loader = new OBJMeshIO();
        Mesh mesh = loader.load(file);
        System.out.println("Loaded mesh with " + mesh.getVertices().size() + " vertices.");
        
        JFrame frame = new JFrame("Wireframe Viewer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800,600);
        frame.add(new LayeredFrame(mesh, frame));
        frame.setVisible(true);
    }
}
