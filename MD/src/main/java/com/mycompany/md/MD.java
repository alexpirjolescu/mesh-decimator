/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.md;

import io.OBJMeshIO;
import java.io.File;
import java.net.URL;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import mesh.Mesh;
import simplify.QEMUtils;
import FrameViewer.*;
import cli.CLIHandle;
/**
 *
 * @author alexp
 */
public class MD {

    public static void main(String[] args) throws Exception {
        /*
            basic: 

            java -jar MD.jar <inputFile> <percentage> <outputFile>

            For GUI arguments:
            -i <inputFile>  #default (humanMesh)
            -p <percentage> #default (0) -> this will launch GUI with the decimated mesh 
            -o <outputFile>  #no default (if value is passed export dialog will not open)

            java -jar MD.jar --gui    (this will open default mesh)                 
            java -jar MD.jar --gui -p <percentage> (this will open default mesh decimated)
            java -jar MD.jar --gui -i <inputFile>  (this will open input mesh)
            java -jar MD.jar --gui -i <inputFile> -p <percentage> (this will open input mesh decimated)
            java -jar MD.jar --gui 
        
            */
        /*
         * am nevoie de Construcr pentru gui pentru toate posibilitatile de input:
         * LayeredFrame(frame, file)
         * LayeredFrame(frame, percentage)
         * LayeredFrame(frame, outputfile)
         * LayeredFrame(frame, file, percentage)
         * LayeredFrame(frame, file, outputfile)
         * LayeredFrame(frame, file)
         * 
         *  
         * 
         * 
         */
        if(args.length == 0) {
            JFrame frame = new JFrame("Mesh Viewer");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);
            DecimatorUI uiFrame = new DecimatorUI(frame, null, 0, null);
            frame.add(uiFrame);
            frame.setVisible(true);
        } else {
        CLIHandle cli = new CLIHandle(args);
        cli.runArgs();
        }
    }
}

