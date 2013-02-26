/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package atomizer.particles;

import atomizer.Vector;

/**
 *
 * @author radek
 */
public class Electron extends Particle {
    public Electron(double x,double y,Vector speed){
        super(x,y,speed);
        mass = 1.0/50.0;//too little for scale
        charge = -1.0;
    }
}
