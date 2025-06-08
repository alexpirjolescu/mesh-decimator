/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package FrameViewer;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

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
        // 
        private File currentFile;
        // frame
        private JFrame frame;
        // load button 
        private JButton loadButton;
        // export button
        private JButton exportButton;
        //decimate button 
        private JButton decimateButton;
        //reset button
        private JButton reloadButton;
        // slider
        private JSlider slider;
        // mesh frame
        private WireframeViewer meshPanel;
        // vertex count label
        private JLabel vertexCountLabel;

        // mesh
        private Mesh mesh;

        private QEMUtils qem;

        private OBJMeshIO objMeshIO;


    public LayeredFrame(JFrame frame, File file){
        setLayout(null);
        setPreferredSize(new Dimension(800, 600));
        this.frame = frame;
        this.currentFile=file;
        objMeshIO = new OBJMeshIO();

        loadMesh();
        meshPanel = new WireframeViewer(qem.mesh);
        add(meshPanel, Integer.valueOf(0));
    
        loadButton = new JButton("Load");
        loadButton.addActionListener(e -> loadOBJ());
        add(loadButton, Integer.valueOf(1));
    
        exportButton = new JButton("Export");
        exportButton.addActionListener(e -> exportOBJ());
        add(exportButton, Integer.valueOf(2));

        decimateButton = new JButton("Decimate");
        decimateButton.addActionListener(e ->{ 
            int value = slider.getValue();
            System.out.println("Decimate button pressed!"); 
            decimateAsync(value);
            meshPanel.requestFocusInWindow();
        });
        add(decimateButton, Integer.valueOf(1));

        reloadButton = new JButton("Reload");
        reloadButton.addActionListener(e -> {
            loadMesh();
            loadMeshPanel(qem.mesh);
        });
        add(reloadButton, Integer.valueOf(1));

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

            //Decimate button at 80% width 5% height
            decimateButton.setBounds((int)(width*0.80), (int)(height *0.05), 2*btnWidth, btnHeight);
            
            //Reload button 5% width 10% height
            reloadButton.setBounds((int)(width*0.05), (int)(height*0.10), btnWidth, btnHeight);
            
            //Slider at 5% width 90% height
           int sliderWidth = (int)(width * 0.25);
            int sliderHeight = 40;
            slider.setBounds((int)(width * 0.05), (int)(height * 0.90) - sliderHeight, sliderWidth, sliderHeight);
            int labelWidth = 200;
            int labelHeight = 40;
            vertexCountLabel.setBounds((int)(width*0.80), (int)(height*0.15) - labelHeight, labelWidth, labelHeight);
        }

        
    

    private void exportOBJ() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Save OBJ File");
        if (chooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
            File saveFile = chooser.getSelectedFile();
            try {
                OBJMeshIO objIO = new OBJMeshIO();
                objIO.save(qem.mesh, saveFile);
                JOptionPane.showMessageDialog(frame, "OBJ exported.");
                meshPanel.requestFocusInWindow();
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Failed to export OBJ.");
            }
        } 
    }

    private void loadOBJ() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Load OBJ File");
        chooser.setFileFilter(new FileNameExtensionFilter("OBJ Files", "obj"));
        if (chooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
            File loadFile = chooser.getSelectedFile();
            currentFile = loadFile;
            loadMesh();
            loadMeshPanel(qem.mesh);
        }
    }

    private void loadMesh() {
        try { 
            mesh = new Mesh(objMeshIO.load(currentFile));
            qem = new QEMUtils(mesh);
            System.out.println("Loaded mesh with " + mesh.getVertices().size() + " vertices.");
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Failed to load OBJ");
        }
        
    }

    private void loadMeshPanel(Mesh mesh) {
        meshPanel.setMesh(mesh);
        meshPanel.requestFocusInWindow();
        meshPanel.repaint();
        vertexCountLabel.setText("Vertices: " + qem.vertQuadricsSize());
    }

    private void decimateAsync(int value) {
        int steps =(int)(qem.vertQuadricsSize() * value / 100) ;
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
                for (int i = 0; i < steps; i++) {
                    qem.decimateOneStep();
                    publish();
                        
                    //Thread.sleep(1);
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


//          Silent Decimation (process not dislayed on UI)

        private void decimateSilent(int value) {
        int steps =(int)(qem.vertQuadricsSize() * value / 100) ;
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
                for (int i = 0; i < steps; i++) {
                    qem.decimateOneStep();
                }
                return null;
            }

            @Override
            protected void done() {
                // Runs on EDT after doInBackground finishes
                meshPanel.repaint();
                vertexCountLabel.setText("Vertices: " + qem.vertQuadricsSize());
            }
        };
        worker.execute();  // Starts the background thread
}
}

