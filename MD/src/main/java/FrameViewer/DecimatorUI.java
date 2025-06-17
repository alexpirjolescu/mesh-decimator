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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.net.URL;

import mesh.Mesh;
import simplify.QEMUtils;
/**
 *
 * @author alexp
 */
public class DecimatorUI extends JLayeredPane {
    //layer all the needed frames
        // input stream
        private InputStream inputStream;
        // file
        private File currentFile;
        // export file
        private File outFile;
        // decimate percentage
        private int percentage = 0;
        //frame 
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
        private OrbitViewer meshPanel;
        // vertex count label
        private JLabel vertexCountLabel;

        // mesh
        private Mesh mesh;

        private QEMUtils qem;

        private OBJMeshIO objMeshIO;



    public DecimatorUI(JFrame frame, File file, Integer percentageInt, File outFile){
        setLayout(null);
        setPreferredSize(new Dimension(800, 600));
        this.frame = frame;
        this.currentFile=file;
        this.outFile = outFile;
        this.percentage = percentageInt;

        if (this.currentFile == null) {
            try {
                // Get resource as URL
                URL resourceUrl = getClass().getResource("/HumanMesh.obj");
                if (resourceUrl == null) {
                    throw new RuntimeException("Default mesh resource not found");
                }
                
                // Handle both JAR and filesystem cases
                if (resourceUrl.getProtocol().equals("jar")) {
                    // When running from JAR
                    this.inputStream = getClass().getResourceAsStream("/HumanMesh.obj");
                    this.currentFile = inputStreamToTempFile(this.inputStream, "human", "obj");
                } else {
                    // When running in IDE
                    this.currentFile = new File(resourceUrl.toURI());
                }
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Failed to load default mesh");
                return;
            }
        }
        else {
            System.out.println("Loaded file: " + currentFile.getAbsolutePath());
        }

        
        // create OBJMeshIO object
        objMeshIO = new OBJMeshIO();
        
        // load mesh from currentFile to mesh and eqm
        loadMesh();

        
        
        // set up meshPanel with new orbit viewer   (can also use WireframeViewer)
        meshPanel = new OrbitViewer(qem.mesh);
        

        // set up load button
        loadButton = new JButton("Load");
        loadButton.addActionListener(e -> loadOBJ());
        
        // set up export button
        exportButton = new JButton("Export");
        exportButton.addActionListener(e -> exportOBJ());
        
        //set up decimate button
        decimateButton = new JButton("Decimate");
        decimateButton.addActionListener(e ->{ 
            int value = slider.getValue();
            System.out.println("Decimate button pressed!"); 
            decimateAsync(value);
            meshPanel.requestFocusInWindow();
        });
        
        //set up reload button
        reloadButton = new JButton("Reload");
        reloadButton.addActionListener(e -> {
            loadMesh();
            loadMeshPanel(qem.mesh);
        });
        
        //set up slider
        slider = new JSlider(0,100,50);
        
        //set up vertecies label
        vertexCountLabel = new JLabel("Vertices: " + qem.vertQuadricsSize());
        vertexCountLabel.setForeground(Color.WHITE);
        vertexCountLabel.setFont(new Font("Arial", Font.BOLD, 14));
        
        add(meshPanel, Integer.valueOf(0));
        add(loadButton, Integer.valueOf(1));
        add(exportButton, Integer.valueOf(1));
        add(decimateButton, Integer.valueOf(1));
        add(reloadButton, Integer.valueOf(1));
        add(slider,Integer.valueOf(1));
        add(vertexCountLabel, Integer.valueOf(1));


        if (percentage != 0) {
           SwingUtilities.invokeLater(() -> {
            decimateSilent(percentage);
            System.out.println("decimated: " + percentage);
        });
}

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
        if (outFile != null) {
            saveExportOBJ(qem.mesh, outFile);
        }
        else {
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Save OBJ File");
            if (chooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
                File saveFile = chooser.getSelectedFile();
                outFile = saveFile;
                saveExportOBJ(qem.mesh, saveFile);
            } 
        }
    }

    private void saveExportOBJ(Mesh mesh , File outFile) {
        try {
            objMeshIO.save(qem.mesh, outFile);
            JOptionPane.showMessageDialog(frame, "OBJ exported.");
            meshPanel.requestFocusInWindow();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Failed to export OBJ.");
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
        System.out.println("Silent decimation");
        int steps =(int)(qem.vertQuadricsSize() * value / 100) ;
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
                for (int i = 0; i < steps; i++) {
                    int before = qem.vertQuadricsSize();
                    qem.decimateOneStep();
                    System.out.println("Step done. Before: " + before + " After: " + qem.vertQuadricsSize());
                }
                return null;
            }

            @Override
            protected void done() {
                // Runs on EDT after doInBackground finishes
                System.out.println("done: " + qem.vertQuadricsSize());
                meshPanel.repaint();
                vertexCountLabel.setText("Vertices: " + qem.vertQuadricsSize());
            }
        };
        worker.execute();  // Starts the background thread
}

    public File inputStreamToTempFile(InputStream inputStream, String prefix, String suffix) throws IOException {
    // Create a temp file (auto-deleted on JVM exit)
        File tempFile = File.createTempFile(prefix, suffix);
        tempFile.deleteOnExit();

        try (FileOutputStream out = new FileOutputStream(tempFile)) {
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
        }

        return tempFile;
    }

}

