/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cli;

import java.io.File;
import java.io.InputStream;
import java.net.URL;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.mycompany.md.MD;

import FrameViewer.DecimatorUI;
import FrameViewer.MeshUIListener;
import io.OBJMeshIO;
import mesh.Mesh;
import simplify.*;

/**
 *
 * @author alexp
 */


/*
 *            basic: 

            java -jar MD.jar <inputFile> <percentage> <outputFile>

            For GUI arguments:
            -i <inputFile>  #default (humanMesh)
            -p <percentage> #default (0) -> this will launch GUI with the decimated mesh 
            -o <outputFile>  #no default (if value is passed export dialog will not open)

 */

public class CLIHandle {
    // command arguments
    private String[] args;

    // GUI boolean
    private boolean launchGui = false;

// -p <percentage>
    private int percentage = 0;
// -i <inputFile>    
    private String inArg;
// -o <outputFile> 
    private String outArg;
// -d <inputDirectory>    


    public CLIHandle(String[] args) {
        this.args = args;
        passArgs();

    }

    public void runArgs(){
        if (launchGui) {
            SwingUtilities.invokeLater(() -> {
                JFrame frame = new JFrame("Mesh Viewer");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(800,600); 
                /* 
                MeshUIListener listener = new MeshUIListener() {
                    @Override
                    public void onExport(File exportedFile) {
                        System.out.println("Exported to " + exportedFile.getAbsolutePath());
                    }

                    @Override
                    public void onReload(File currentFile) {
                        System.out.println("Reloading " + currentFile);
                    }

                    @Override
                    public void onDecimationStarted(int percent) {
                        System.out.println("Decimation started: " + percent + "%");
                    }

                    @Override
                    public void onClose() {
                        frame.dispose();
                    }
                };
                */
                DecimatorUI uiFrame = new DecimatorUI(
                    frame,
                    inArg != null ? new File(inArg) : null,
                    percentage,
                    outArg != null ? new File(outArg) : null);
                frame.add(uiFrame);
                frame.setVisible(launchGui);
            });
        } else {
            // need to pass directory; for each file load decimate and save
            //create a queue?
            //pass to DecimateDir (indir, p ,outdir) (run decimate there)
            //CLI requires: inputFile, percentage, outputFile
            File inputFile = inArg != null ? new File(inArg) : null;
            File outputFile = outArg != null ? new File(outArg) : null;

            if (inputFile == null || percentage < 1 || percentage > 99 ||     outputFile == null) {
                System.err.println("MD usage: --gui (open GUI) -i <inputFile> -p <percentage> -o <outputFile>");
                return;
            }

            Decimate decimate = new Decimate(inputFile, percentage, outputFile);
            decimate.batchProcess();
        }

    }



    private void passArgs() {
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "--gui":
                    launchGui = true;
                    break;
                case "-i":
                    if (i + 1 < args.length) {
                       inArg = args[++i];
                    } else {
                        System.err.println("Error: -i requires a filename");
                         return;
                    }
                    break;
                case "-p":
                    if (i + 1 < args.length) {
                        try {
                            percentage = Integer.parseInt(args[++i]);
                        } catch (NumberFormatException e) {
                            System.err.println("Error: -p requires an integer percentage");
                            return;
                        }
                    } else {
                        System.err.println("Error: -p requires a value");
                        return;
                    }
                    break;
                case "-o":
                    if (i + 1 < args.length) {
                        outArg =args[++i];
                    } else {
                        System.err.println("Error: -o requires a filename");
                        return;
                    }
                    break;
                default:
                    System.err.println("Unknown argument: " + args[i]);
                    return;
            }
        }
    }
}
