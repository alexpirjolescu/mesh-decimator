/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package FrameViewer;
import mesh.*;
import java.util.List;
import javax.swing.*;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

/**
 *
 * @author alexp
 */
public class OrbitViewer extends JPanel{
    private Mesh mesh;
    private int lastX = 0;
    private int lastY = 0;
    private double angleX = 0;
    private double angleY = 0;
    private double zoom = 1;
    private double centerX;
    private double centerY;
    private double centerZ;

    public OrbitViewer(Mesh mesh) {
        this.mesh = mesh;
        computeCenter(this.mesh.getVertices());
        setFocusable(true);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                lastX = e.getX();
                lastY = e.getY();
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
                
            @Override
            public void mouseDragged(MouseEvent e) {
                int dx = e.getX() - lastX;
                int dy = e.getY() - lastY;
                
                angleY -= dx * 0.01;
                angleX += dy * 0.01;
                
                lastX = e.getX();
                lastY = e.getY();

                repaint();
            }
        });

        addMouseWheelListener(e -> {
            int notches = e.getWheelRotation();
            zoom *= (notches > 0) ? 0.9 : 1.1;  // zoom in or out
            zoom = Math.max(0.1, Math.min(zoom, 10)); // clamp zoom range
            repaint();  
        });

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                double step = 1; // change as needed

                if (key == KeyEvent.VK_W) {
                    centerY -= step;
                } else if (key == KeyEvent.VK_S) {
                    centerY += step;
                }

                repaint();
            }
        });


    }    

    @Override 
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;


        
        int w= getWidth(), h=getHeight();
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0,0,w,h);

        // Pan and zoom 
        //g2d.translate(translateX, translateY);
        //g2d.scale(zoom, zoom);
        
        g2d.setColor(Color.WHITE);
        
        
        
        //For each face draw lines between the 3 vertecies that define it
        for (Face face : mesh.getFaces()) {
            if (face.isValid()){
                Vertex[] v ={
                    mesh.getVertices().get(face.getV1()),
                    mesh.getVertices().get(face.getV2()),
                    mesh.getVertices().get(face.getV3())
                };
                    
                int [][] points = new int[3][2];
                
                for (int i=0; i<3; i++){
                    double x = v[i].getX() - centerX;
                    double y = v[i].getY() - centerY;
                    double z = v[i].getZ() - centerZ;
                    
                    double xRot = x*  Math.cos(angleY) - z * Math.sin(angleY);
                    double zRot = x * Math.sin(angleY) + z * Math.cos(angleY);
                    double yRot = y * Math.cos(angleX) - zRot * Math.sin(angleX);
                    zRot = y * Math.sin(angleX) + zRot * Math.cos(angleX);
                    double scale = zoom * 24000 / (zRot+400 + 1e-5);        //Perspective projection
                    int screenX = (int) (w / 2 + xRot * scale);
                    int screenY = (int) (h /2 - yRot * scale);
                    points[i][0] = screenX;
                    points[i][1] = screenY;
                }
                for (int i=0; i<3; i++){
                    int j = (i+1)%3;
                    g2d.drawLine(points[i][0], points[i][1], points[j][0], points[j][1]);
                }
                
            }
        }
    }

    public void setMesh(Mesh mesh) {
        this.mesh = mesh;
        repaint();
    }


    private void computeCenter(List<Vertex> vertices) {
        double cx=0, cy=0, cz=0;
        for (Vertex v : vertices) {
            cx += v.getX();
            cy += v.getY();
            cz += v.getZ();
        }
        int count = vertices.size();
        centerX = cx / count;
        centerY = cy / count;
        centerZ = cz / count;
    }


}
