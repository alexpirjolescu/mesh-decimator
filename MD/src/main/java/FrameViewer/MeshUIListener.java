/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package FrameViewer;
import java.io.File;

/**
 *
 * @author alexp
 */
public interface MeshUIListener {
    void onExport(File exportedFile);
    void onReload(File currentFile);
    void onDecimationStarted(int percentage);
    void onClose();
}
