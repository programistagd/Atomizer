/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package atomizer;
import java.lang.Math;

/**
 *
 * @author radek
 */
public class Vector {
    public double x=0;
    public double y=0;
    
    public Vector(){
        
    }
    
    public Vector(Vector copy){
        x=copy.x;
        y=copy.y;
    }
    
    public Vector(double x,double y){
        this.x=x;
        this.y=y;
    }
    
    public double getLength(){
        return Math.sqrt(x*x+y*y);
    }
    public Vector add(Vector b){
        Vector r = new Vector();
        r.x=x+b.x;
        r.y=y+b.y;
        return r;
    }
    
    public Vector negate(){
        Vector n = new Vector();
        n.x=-x;
        n.y=-y;
        return n;
    }
    
    public Vector sub(Vector b){
        Vector r = new Vector();
        r.x=x-b.x;
        r.y=y-b.y;
        return r;
    }
    
    public Vector multiply(double f){
        Vector r= new Vector();
        r.x=x*f;
        r.y=y*f;
        return r;
    }
    
    public Vector divide(double f){
        Vector r= new Vector();
        r.x=x/f;
        r.y=y/f;
        return r;
    }
    
    public static Vector getFromAngular(double direction, double length){
        Vector r = new Vector();
        r.x=Math.cos(direction)*length;
        r.y=Math.sin(direction)*length;
        return r;
    }
    
    public static double distance(Vector a,Vector b){
        double dx=a.x-b.x;
        double dy=a.y-b.y;
        return Math.sqrt(dx*dx+dy*dy);
    }
}
