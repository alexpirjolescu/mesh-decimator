/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package FrameViewer;
import javax.swing.*;

import io.OBJMeshIO;

import java.awt.*;
import java.io.File;
import java.util.List;

import mesh.Mesh;
import simplify.QEMUtils;
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
        //decimate button 
        private JButton decimateButton;
        // slider
        private JSlider slider;
        // mesh frame
        private WireframeViewer meshPanel;
        // vertex count label
        private JLabel vertexCountLabel;

        // mesh
        private Mesh mesh;

        private QEMUtils qem;


    public LayeredFrame(Mesh mesh, JFrame frame){
        setLayout(null);
        setPreferredSize(new Dimension(800, 600));
        this.frame = frame;
        this.mesh = mesh;
        this.qem = new QEMUtils(this.mesh);
        meshPanel = new WireframeViewer(qem.mesh);
        add(meshPanel, Integer.valueOf(0));
    
        loadButton = new JButton("Load");
        add(loadButton, Integer.valueOf(1));
    
        exportButton = new JButton("Export");
        exportButton.addActionListener(e -> exportOBJ());
        add(exportButton, Integer.valueOf(2));

        decimateButton = new JButton("Decimate");
        decimateButton.addActionListener(e ->{ 
            int value = slider.getValue();
            System.out.println("Decimate button pressed!"); 
            decimateAsync(value);
        });
        add(decimateButton, Integer.valueOf(1));

        slider = new JSlider(0,100,50);
        add(slider,Integer.valueOf(1));

        vertexCountLabel = new JLabel("Vertices: " + qem.vertQuadricsSize());
        vertexCountLabel.setForeground(Color.WHITE);
        vertexCountLabel.setFont(new Font("Arial", Font.BOLD, 14));
        add(vertexCountLabel, Integer.valueOf(1));

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

            decimateButton.setBounds((int)(width*0.80), (int)(height *0.05), 2*btnWidth, btnHeight);
            //Slider at 5% width 90% height
            int sliderWidth = (int)(width * 0.25);
            int sliderHeight = 40;
            slider.setBounds((int)(width * 0.05), (int)(height * 0.90) - sliderHeight, sliderWidth, sliderHeight);
            int labelWidth = 200;
            int labelHeight = 40;
            vertexCountLabel.setBounds((int)(width*0.80), (int)(height*0.15) - labelHeight, labelWidth, labelHeight);
        }

    private void decimate100(){
        int i = 0;
        while(i < 100) {
            qem.decimateOneStep();
            i++;
            meshPanel.repaint();
        }
        
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

    private void decimateAsync(int value) {
        int steps =(int)(qem.vertQuadricsSize() * value / 100) ;
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
                for (int i = 0; i < steps; i++) {
                    qem.decimateOneStep();
                    publish();
                }
                return null;
            }
            @Override
            protected void process(List<Void> chunks) {
                meshPanel.repaint();
                vertexCountLabel.setText("Vertices: " + qem.vertQuadricsSize());
            }

            @Override
            protected void done() {
                // Runs on EDT after doInBackground finishes
                meshPanel.repaint();
            }
        };
        worker.execute();  // Starts the background thread
}
    
    private void decimate1010Async() {
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
                for (int i = 0; i < 20010; i++) {
                    qem.decimateOneStep();
                }
                return null;
            }

            @Override
            protected void done() {
                // Runs on EDT after doInBackground finishes
                meshPanel.repaint();
            }
        };
        worker.execute();  // Starts the background thread
}
}

