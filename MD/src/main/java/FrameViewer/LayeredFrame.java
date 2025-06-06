/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package FrameViewer;
import javax.swing.*;

import io.OBJMeshIO;

import java.awt.*;
import java.io.File;

import mesh.Mesh;
/**
 *
 * @author alexp
 */
public class LayeredFrame extends JLayeredPane {
    //layer all the needed frames 
        // frame
        private JFrame frame;
        // load button 
        private JButton loadButton;
        // export button
        private JButton exportButton;
        // slider
        private JSlider slider;
        // mesh frame
        private WireframeViewer meshPanel;
        // mesh
        private Mesh mesh;


    public LayeredFrame(Mesh mesh, JFrame frame){
        setLayout(null);
        setPreferredSize(new Dimension(800, 600));
        this.frame = frame;
        this.mesh = mesh;
        meshPanel = new WireframeViewer(mesh);
        add(meshPanel, Integer.valueOf(0));
    
        loadButton = new JButton("Load");
        add(loadButton, Integer.valueOf(1));
    
        exportButton = new JButton("Export");
        exportButton.addActionListener(e -> exportOBJ());
        add(exportButton, Integer.valueOf(2));

        slider = new JSlider(0,100,50);
        add(slider,Integer.valueOf(1));

    }
        @Override
        public void doLayout(){
            int width = getWidth();
            int height = getHeight();

            //Mesh
            meshPanel.setBounds(0,0,width,height);

            int btnWidth = (int)(80);
            int btnHeight = (int)(30);
            // Load button 5% width 5% height
            loadButton.setBounds((int)(width*0.05), (int)(height*0.05), btnWidth, btnHeight);

            //Export button 15% width 5% height
            exportButton.setBounds((int)(width*0.15), (int)(height*0.05), btnWidth, btnHeight);

            //Slider at 5% width 90% height
            int sliderWidth = (int)(width * 0.25);
            int sliderHeight = 40;
            slider.setBounds((int)(width * 0.05), (int)(height * 0.90) - sliderHeight, sliderWidth, sliderHeight);

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

