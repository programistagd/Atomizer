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
public class Proton extends Particle{
    public Proton(double x,double y,Vector speed){
        super(x,y,speed);
        mass = 1.0;
        charge = 1.0;
    }
}
