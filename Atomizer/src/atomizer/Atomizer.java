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
import java.util.Vector;

/**
 *
 * @author radek
 */
public class Atomizer extends JPanel {
    
    public  Vector<Particle> particles = new Vector<Particle>();
    
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
        atomizer.particles.add(new Electron(40,30,new atomizer.Vector()));
        atomizer.particles.add(new Neutron(50,30,new atomizer.Vector()));
        float dt=1000/60;//TODO fixed time step but not laggy
        while(true){
            int size = atomizer.particles.size();
            for(int i=0;i<size;i++){
                Particle particle = atomizer.particles.get(i);
                for(int j=i+1;j<size;j++){
                    
                }
                particle.velocity.add(particle.force.divide(particle.mass));
                particle.position.add(particle.velocity);
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
