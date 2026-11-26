package mesh;

import java.util.Arrays;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author alexp
 */
public class Face {
    private int v1, v2, v3;
    private boolean valid = true;
    private int id;

    public Face(Face other) {
        this.v1 = other.getV1();
        this.v2 = other.getV2();
        this.v3 = other.getV3();
        this.id = other.getId();
    }
    
    public Face(int id, int v1, int v2, int v3)
    {
        this.id = id;
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
    }
    public int getId(){
        return id;
    }

    public int getV1()
    {
        return this.v1;
    }
    public int getV2()
    {
        return this.v2;
    }
    public int getV3()
    {
        return this.v3;
    }

    public int[] getVertices() {
        int[] vert = new int[] {v1, v2, v3};
        return vert;
    }
    public boolean isDegenerate(){
        return (v1 == v2 || v2==v3 || v1==v3);
    }

    public void replaceVertex(int oldV, int newV) { 
        if (v1 == oldV) v1 = newV;
        if (v2 == oldV) v2 = newV;
        if (v3 == oldV) v3 = newV;
    }

    public boolean isValid() {
        // a face that collapsed to a line or point is no longer valid
        if (this.isDegenerate()) {
            valid = false;
        }
        return valid;
    }

    public void setVertices(int v1, int v2, int v3) {
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
    }

    public void setValid(boolean status) {
        valid = status;
    }

}
    
