package mesh;

public class vec4d {

    private double x,y,z,a;

    public vec4d(double x, double y, double z, double a){
        this.x = x;
        this.y = y;
        this.z = z;
        this.a = a;
    }

    public vec4d(vec3d base, double extension){
        this.x = base.getX();
        this.y = base.getY();
        this.z = base.getZ();
        this.a = extension;
    }

    public double getX(){
        return x;
    }

    public double getY(){
        return y;
    }

    public double getZ(){
        return z;
    }

    public double getA(){
        return a;
    }
}
