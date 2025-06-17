/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package simplify;
import java.io.File;

import io.OBJMeshIO;
import mesh.Mesh;
/**
 *
 * @author alexp
 */
public class Decimate {
    private File inFile;
    private File outFile;
    private OBJMeshIO objMeshIO;
    private QEMUtils qem;
    private Mesh mesh;
    private int percentage = 0;
    private File[] objFiles;

    public Decimate(File inFile, int percentage, File outFile) {
        this.inFile = inFile;
        this.outFile = outFile;
        this.percentage = percentage;
        this.objMeshIO = new OBJMeshIO();

    }

    public void batchProcess() {
        if (inFile.isDirectory()) {
            //directory case
            objFiles = inFile.listFiles((dir, name) -> name.toLowerCase().endsWith(".obj"));
            if (objFiles == null || objFiles.length == 0) {
                System.out.println("No .obj files found in directory: " + inFile.getAbsolutePath());
            } else {
                for (File file : objFiles) {
                    System.out.println("Processing: " + file.getName());
                    loadMesh(file);
                    decimate(percentage);
                    File outF = new File(outFile, file.getName().replace(".obj", "") + "_decimated.obj");
                    save(qem.mesh, outF);
                }
            }
        } else {
            //file case
            loadMesh(inFile);
            decimate(percentage);
            if(outFile.isDirectory()){
                File outF = new File(outFile, inFile.getName().replace(".obj", "") + "_decimated.obj");
                save(qem.mesh, outF);
            } else {
                save(qem.mesh, outFile);
            }
        }
    }


    private void loadMesh(File inputFile) {
        try {
            mesh = new Mesh(objMeshIO.load(inputFile));
            qem = new QEMUtils(mesh);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        qem = new QEMUtils(mesh);
    }   

    private void decimate(int value) { 
        int steps = (int)(qem.vertQuadricsSize() * value / 100) ;
        for (int i = 0; i < steps; i++) {
            qem.decimateOneStep();
        }
        System.out.println("Decimation complete. Remaining vertices: " + qem.vertQuadricsSize());
    }

    private void save(Mesh mesh, File outFile) {
        try {
            objMeshIO.save(mesh, outFile);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    //need a decimate method.
}
