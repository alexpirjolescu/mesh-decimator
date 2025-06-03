/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package simplify;

import mesh.vec4d;

/**
 *
 * @author alexp
 */
public class Quadric{
    double[][] Q = new double[4][4];

    public void add(Quadric other) {
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++)
                this.Q[i][j] += other.Q[i][j];
    }

    public static Quadric fromPlane(vec4d plane) {
        double[] p = {plane.getX(), plane.getY(), plane.getZ(), plane.getA()};
        Quadric q = new Quadric();
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++)
                q.Q[i][j] = p[i] * p[j];
        return q;
    }
    public static Quadric from2(Quadric a, Quadric b)
    {
        Quadric q = new Quadric();
        for(int i=0; i<4; i++)
            for (int j=0; j<4; j++)
                q.Q[i][j]= a.Q[i][j]+b.Q[i][j];
        return q;
    }
    
    public double[] solveOptimalPosition(double[] fallbackPosition){
        double[][] A = new double[3][3];
        double[] b = new double [3];
        
        for(int i=0; i<3; i++){
            b[i] = -Q[i][3];
            for(int j=0; j<3; j++){
                A[i][j] = Q[i][j];
            }
        }
        double det = determinant3x3(A);
        if(Math.abs(det)<1e-10)
        {
            //Matrix is singular -> fallback
            return fallbackPosition;
        }
        
        return solveLinearSystem(A, b);
    }
    
    private double[] solveLinearSystem(double[][] A, double[] b)
    {
        int n =3;
        for(int i=0; i<n;i++){
            int max =i;
            for(int k= i+1; k<n; k++){
                if (Math.abs(A[k][i])> Math.abs(A[max][i]))
                    max =k;
            }
            double[] temp = A[i];
            A[i] = A[max];
            A[max] = temp;
            
            double t = b[i];
            b[i] = b[max];
            b[max]= t;
            
            for (int k=i+1; k<n ;k++){
                double factor = A[k][i]/A[i][i];
                b[k] -= factor*b[i];
                for (int j=i; j<n; j++){
                    A[k][j] -= factor* A[i][j];
                }
            }
        }
        double[] x = new double[n];
        for(int i =n-1; i>=0; i--){
            x[i] = b[i];
            for (int j=i+1; j<n; j++)
                x[i]-= A[i][j]*x[j];
            x[i]/= A[i][i];
        }
        
        return x;
        
    }
    private double determinant3x3(double[][] m)
    {
        return m[0][0] * (m[1][0] * m[2][2] - m[1][2]*m[2][1])
             - m[0][1] * (m[1][0] * m[2][2] - m[1][2]*m[2][0])
             + m[0][2] * (m[1][0] * m[2][1] - m[1][1]*m[2][0]);
    }
    
}
