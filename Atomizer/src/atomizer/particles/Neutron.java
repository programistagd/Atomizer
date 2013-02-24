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
public class Neutron extends Particle{
    public Neutron(int x,int y,Vector speed){
        super(x,y,speed);
        mass = 1;
        charge = 0;
    }
}
