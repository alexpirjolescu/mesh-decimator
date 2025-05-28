package mesh;

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
    
    public Face(int v1, int v2, int v3)
    {
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
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
    public boolean isDegenerate(Face f){
        if(f.getV1() == f.getV2() || f.getV2() == f.getV3() ||f.getV1() == f.getV3())
            return true;
        else
            return false;
    }
    
}
