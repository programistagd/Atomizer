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
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.event.MouseInputListener;

/**
 *
 * @author radek
 */
public class Atomizer extends JPanel implements MouseInputListener, KeyListener {
    
    public Atomizer(){
        addMouseListener(this);
        addMouseMotionListener(this);
        addKeyListener(this);
        setFocusable(true);
    }
    
    
    public  java.util.Vector<Particle> particles = new java.util.Vector<Particle>();

    @Override
    public void keyTyped(KeyEvent ke) {
        if(ke.getKeyCode()==KeyEvent.VK_N){
            selected=Part.neutron;
        }
        else if(ke.getKeyCode()==KeyEvent.VK_P){
            selected=Part.proton;
        }
        else if(ke.getKeyCode()==KeyEvent.VK_E){
            selected=Part.electron;
        }
    }

    @Override
    public void keyPressed(KeyEvent ke) {
        if(ke.getKeyCode()==KeyEvent.VK_N){
            selected=Part.neutron;
        }
        else if(ke.getKeyCode()==KeyEvent.VK_P){
            selected=Part.proton;
        }
        else if(ke.getKeyCode()==KeyEvent.VK_E){
            selected=Part.electron;
        }
    }

    @Override
    public void keyReleased(KeyEvent ke) {
        //nothing
    }
    
    public enum Part{
        neutron, proton, electron
    }
    
    public Part selected = Part.electron;
    
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
        frame.setFocusable(true);
        frame.addKeyListener(atomizer);
        /*atomizer.particles.add(new Proton(30,30,new atomizer.Vector()));
        atomizer.particles.add(new Proton(30,60,new atomizer.Vector()));
        atomizer.particles.add(new Electron(40,30,new atomizer.Vector()));
        atomizer.particles.add(new Neutron(31,30,new atomizer.Vector()));*/
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
                    if (distance>0.0001){
                        double distance2=distance*distance;
                        Vector gravity = particle.position.sub(other.position).multiply(particle.mass*other.mass).divide(distance2*20.0);
                        particle.force=particle.force.sub(gravity);
                        other.force=other.force.add(gravity);
                        if(particle.charge!=0 && other.charge!=0){
                            atomizer.Vector electro = particle.position.sub(other.position).multiply(particle.charge*other.charge).divide(distance2*10.0);
                            particle.force=particle.force.add(electro);
                            other.force=other.force.sub(electro);
                        }
                        //if(/*distance<10.0 && */(particle instanceof Proton || particle instanceof Neutron) && (other instanceof Proton || other instanceof Neutron)){
                        //   Vector strong = particle.position.sub(other.position).divide(distance2*distance2);
                        //    particle.force=particle.force.add(strong);
                        //    other.force=other.force.sub(strong);//no it's not strong interaction :/
                        //}
                        /*if(distance<0.9999){
                            //collsion
                            Vector d = new Vector(0.7, 0.7);
                            particle.position.add(d);
                            other.position.sub(d);
                        }*/
                        /*if(distance<3.0 && (particle instanceof Proton || particle instanceof Neutron) && (other instanceof Proton || other instanceof Neutron)){
                            //Vector strong = particle.position.sub(other.position).divide(distance2+0.1).multiply(100);
                            //particle.force=particle.force.sub(strong);
                            //other.force=other.force.add(strong);
                            //Normal strong force doesn't work because of relatively big delta time(should be nanoseconds)
                            //so i added some hacking
                            if(particle instanceof Proton){
                                other.force=particle.force;
                            }
                            else{
                                particle.force=other.force;
                            }
                        }*/
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

    @Override
    public void mouseClicked(MouseEvent me) {
        switch(selected){
            case neutron:
                particles.add(new Neutron(me.getX(), me.getY(), new Vector()));
            break;
            case electron:
                particles.add(new Electron(me.getX(), me.getY(), new Vector()));
            break;
            case proton:
                particles.add(new Proton(me.getX(), me.getY(), new Vector()));
            break;
        }
    }

    @Override
    public void mousePressed(MouseEvent me) {
        //nothing
    }

    @Override
    public void mouseReleased(MouseEvent me) {
        //nothing
    }

    @Override
    public void mouseEntered(MouseEvent me) {
        //nothing
    }

    @Override
    public void mouseExited(MouseEvent me) {
        //nothing
    }

    @Override
    public void mouseDragged(MouseEvent me) {
        //TODO add moving particle
    }

    @Override
    public void mouseMoved(MouseEvent me) {
        //nothing
    }
}
