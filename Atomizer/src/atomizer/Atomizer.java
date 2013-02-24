/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package atomizer;

import atomizer.particles.Electron;
import atomizer.particles.Neutron;
import atomizer.particles.Particle;
import atomizer.particles.Proton;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author radek
 */
public class Atomizer extends JPanel {
    
    public  java.util.Vector<Particle> particles = new java.util.Vector<Particle>();
    
    public void paint(Graphics g) {

        Graphics2D g2d = (Graphics2D) g;
        for(int i=0;i<particles.size();i++){
            Particle p = particles.get(i);
            if(p instanceof Electron){
                g2d.setColor(Color.BLUE);
                atomizer.Vector pos = p.position;//TODO view transformation
                g2d.fillOval((int)pos.x-2,(int)pos.y-2 , 4, 4);
            }
            else if(p instanceof Proton){
                g2d.setColor(Color.RED);
                atomizer.Vector pos = p.position;//TODO view transformation
                g2d.fillOval((int)pos.x-4,(int)pos.y-4 , 8, 8);
            }
            else if(p instanceof Neutron){
                g2d.setColor(Color.GREEN);
                atomizer.Vector pos = p.position;//TODO view transformation
                g2d.fillOval((int)pos.x-4,(int)pos.y-4 , 8, 8);
            }
        }
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Atomizer atomizer = new Atomizer();
        JFrame frame = new JFrame("Atomizer");
        frame.add(atomizer);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        atomizer.particles.add(new Proton(30,30,new atomizer.Vector()));
        atomizer.particles.add(new Proton(30,60,new atomizer.Vector()));
        atomizer.particles.add(new Electron(40,30,new atomizer.Vector()));
        atomizer.particles.add(new Neutron(33,30,new atomizer.Vector()));
        float dt=100/60;//TODO fixed time step but not laggy
        while(true){
            int size = atomizer.particles.size();
            for(int i=0;i<size;i++){
                atomizer.particles.get(i).force = new atomizer.Vector();
            }
            for(int i=0;i<size;i++){
                Particle particle = atomizer.particles.get(i);
                for(int j=i+1;j<size;j++){
                    Particle other = atomizer.particles.get(j);
                    double distance = Vector.distance(particle.position,other.position);
                    double distance2=distance*distance;
                    if(particle.charge!=0 && other.charge!=0){
                        atomizer.Vector electro = particle.position.sub(other.position).multiply(particle.charge*other.charge*0.1).divide(distance2);
                        particle.force=particle.force.add(electro);
                        other.force=other.force.sub(electro);
                    }
                    if(distance<0.33){
                        Vector quantumCollision = particle.position.sub(other.position);
                        particle.force=particle.force.add(quantumCollision);
                        other.force=other.force.sub(quantumCollision);
                    }
                    if(distance<2.0 && (particle instanceof Proton || particle instanceof Neutron) && (other instanceof Proton || other instanceof Neutron)){
                        Vector strong = particle.position.sub(other.position).divide(distance2).multiply(100);
                        particle.force=particle.force.add(strong);
                        other.force=other.force.sub(strong);//TODO?
                    }
                }
                particle.velocity=particle.velocity.add(particle.force.divide(particle.mass).multiply(dt));
                particle.position=particle.position.add(particle.velocity.multiply(dt));
            }
            frame.repaint();
            try {
                Thread.sleep(1000/60);
            } catch (InterruptedException ex) {
                Logger.getLogger(Atomizer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
