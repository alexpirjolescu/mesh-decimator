/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package FrameViewer;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import mesh.*;

import javax.swing.*;
import java.util.*;
import java.util.List;
import javax.swing.Timer;

/**
 *
 * @author alexp
 */
public class WireframeViewer extends JPanel{
    List<Vertex> vertices = new ArrayList<>();
    List<Face> faces = new ArrayList<>();
    private double angle = 0;
    private double zoom =1.0;
    private double translateX = 0;
    private double translateY = 0;
    
    
    public WireframeViewer(Mesh mesh)
    {
        this.vertices = mesh.getVertices();
        this.faces = mesh.getFaces();
        
        // Rotate Mesh
       /* Timer timer = new Timer(30, e->{
            angle+=0.01;
            repaint();
        });
        timer.start();
        */
        // Zoom in/out
        addMouseWheelListener(e -> {
            double delta = 0.1f;
            if(e.getPreciseWheelRotation()<0){
                zoom += delta;
            }
                else{
                zoom = Math.max(0.1, zoom-delta);
            }
            repaint();
        });
        
        // Move camera position
        MouseAdapter ma = new MouseAdapter() {
            private int lastX, lastY;
            @Override 
            public void mousePressed(MouseEvent e){
                lastX = e.getX();
                lastY= e.getY();
            }
            @Override
            public void mouseDragged(MouseEvent e){
                translateX += (e.getX()- lastX);
                translateY += (e.getY()- lastY);
                lastX= e.getX();
                lastY = e.getY();
                repaint();
            }
        };
        addMouseListener(ma);
        addMouseMotionListener(ma);
        
        
        
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;


        
        
        int w= getWidth(), h=getHeight();
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0,0,w,h);
        
        // Pan and zoom 
        g2d.translate(translateX, translateY);
        g2d.scale(zoom, zoom);
        
        g2d.setColor(Color.WHITE);
        
        
        
        //For each face draw lines between the 3 vertecies that define it
        for (Face face : faces) {
            Vertex[] v ={
                vertices.get(face.getV1()),
                vertices.get(face.getV2()),
                vertices.get(face.getV3())
            };
            
            int [][] points = new int[3][2];
            
            for (int i=0; i<3; i++){
                double x = v[i].x;
                double y = v[i].y;
                double z = v[i].z;
                
                double xRot = x*  Math.cos(angle) - z *Math.sin(angle);
                double zRot = x* Math.sin(angle) - z*Math.cos(angle);
            
                double scale = 24000 / (zRot+400);        //Perspective projection
                int screenX = (int) (w / 2 + xRot * scale);
                int screenY = (int) (h /2 - y * scale);
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
