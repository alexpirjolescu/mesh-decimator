package mesh;

public class vec3d {
    private double x,y,z;
    public vec3d(double x, double y, double z){
        this.x = x;
        this.y = y;
        this.z = z;
    }
    public vec3d minus(vec3d other) throws NullPointerException{
        if(other==null){
            throw new NullPointerException();
        }
        return new vec3d(this.x-other.getX(),this.y-other.getY(),this.z-other.getZ());
    }
    public vec3d minus(double value){
        return new vec3d(this.x-value, this.y-value, this.z-value);
    }

    public vec3d plus(vec3d other) throws NullPointerException{
        if(other==null){
            throw new NullPointerException();
        }
        return new vec3d(this.x+other.getX(),this.y+other.getY(),this.z+other.getZ());
    }
    public vec3d plus(double value){
        return new vec3d(this.x+value, this.y+value, this.z+value);
    }

    public vec3d mult(double value){
        return new vec3d(this.x*value, this.y*value, this.z*value);
    }

    public vec3d div(double value){
        return new vec3d(this.x/value, this.y/value, this.z/value);
    }

    public static vec3d midpoint(vec3d a, vec3d b) {
        return new vec3d((a.x + b.x) * 0.5, (a.y + b.y) * 0.5, (a.z + b.z) * 0.5);
    }

    public double dot(vec3d other) throws NullPointerException{
        if(other==null){
            throw new NullPointerException();
        }
        return this.x*other.getX() + this.y*other.getY() + this.z*other.getZ();
    }

    public vec3d cross(vec3d other) throws NullPointerException{
        if(other==null){
            throw new NullPointerException();
        }
        double a = this.y * other.getZ() - this.z * other.getY();
        double b = this.z * other.getX() - this.x * other.getZ();
        double c = this.x * other.getY() - this.y * other.getX();
        return new vec3d(a,b,c);
    }

    public double getSquareMagnitude(){
        return this.x*this.x+this.y*this.y+this.z*this.z;
    }

    public double getMagnitude(){
        return Math.sqrt(this.getSquareMagnitude());
    }

    public vec3d getNormalized(){
        double length = this.getMagnitude();
        if(length == 0){
            return new vec3d(0, 0, 0);
        }
        return this.div(length);
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
}
