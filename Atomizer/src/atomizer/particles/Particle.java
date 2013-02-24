/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package atomizer.particles;

import atomizer.Vector;
import java.awt.Color;


/**
 *
 * @author radek
 */
public class Particle {//Everything's public because main class has optimizations of updating particles
    
    public double mass = 0;
    public double charge = 0;
    
    public Vector force = new Vector();
    public Vector velocity = new Vector();
    public Vector position = new Vector();
    
    
    public Particle(int x,int y,Vector speed){
        position=new Vector(x,y);
        velocity = speed;
    }
    
}
