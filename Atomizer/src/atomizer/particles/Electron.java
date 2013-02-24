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
    public Electron(int x,int y,Vector speed){
        super(x,y,speed);
        mass = 1.0/1000.0;
        charge = -1.0;
    }
}
