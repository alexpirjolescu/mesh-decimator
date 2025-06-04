/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template

/**
 *
 * @author 20244469
 */
package com.mycompany.md;

import FrameViewer.WireframeViewer;
import io.OBJMeshIO;
import mesh.Mesh;

import javax.swing.*;
import java.awt.*;
import java.io.*;

public class ExportButton {
    private final Mesh mesh;
    private final JFrame frame;

    public ExportButton(Mesh mesh) {
        this.mesh = mesh;
        this.frame = new JFrame("Wireframe Viewer");
        setupUI();
    }

    private void setupUI() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());

        // Viewer
        WireframeViewer viewer = new WireframeViewer(mesh);
        frame.add(viewer, BorderLayout.CENTER);

        // Buttons
        JButton exportOBJ = new JButton("Export .OBJ");

        exportOBJ.addActionListener(e -> exportOBJ());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(exportOBJ);
        frame.add(buttonPanel, BorderLayout.SOUTH);
    }

    public void show() {
        frame.setVisible(true);
    }

    private void exportOBJ() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Save OBJ File");
        if (chooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
            File saveFile = chooser.getSelectedFile();
            try {
                OBJMeshIO objIO = new OBJMeshIO();
                objIO.save(mesh, saveFile);
                JOptionPane.showMessageDialog(frame, "OBJ exported.");
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Failed to export OBJ.");
            }
        } 
    }
}
