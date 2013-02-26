/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package atomizer;

import atomizer.particles.Atom;
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
    
    
    public  java.util.concurrent.CopyOnWriteArrayList<Particle> particles = new java.util.concurrent.CopyOnWriteArrayList<>();

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

    
     public boolean pause = false;
     
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
        else if(ke.getKeyCode()==KeyEvent.VK_SPACE){
            pause = !pause;
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
        g2d.setBackground(new Color(0,0,0));
        if(isPressed){
            g2d.setColor(Color.BLACK);
            g2d.drawLine(currentX, currentY, startX, startY);
        }
        for(int i=0;i<particles.size();i++){
            Particle p = particles.get(i);
            if(p instanceof Atom){
                double max;
                if(p.mass-p.charge>p.charge){
                    max=p.mass-p.charge;
                }
                else{
                    max=p.charge;
                }
                max=Math.abs(max);
                g2d.setColor((new Color((float)(Math.abs(p.charge)/max),(float)((p.mass-p.charge)/max),0.1f)));
                atomizer.Vector pos = p.position;//TODO view transformation
                g2d.fillOval((int)(pos.x-p.mass),(int)(pos.y-p.mass) , (int)p.mass*2, (int)p.mass*2);
            }
            else if(p instanceof Electron){
                g2d.setColor(Color.BLUE);
                atomizer.Vector pos = p.position;//TODO view transformation
                g2d.fillOval((int)pos.x-1,(int)pos.y-1 , 2, 2);
            }
            else if(p instanceof Proton){
                g2d.setColor(Color.RED);
                atomizer.Vector pos = p.position;//TODO view transformation
                g2d.fillOval((int)pos.x-2,(int)pos.y-2 , 4, 4);
            }
            else if(p instanceof Neutron){
                g2d.setColor(Color.GREEN);
                atomizer.Vector pos = p.position;//TODO view transformation
                g2d.fillOval((int)pos.x-2,(int)pos.y-2 , 4, 4);
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
            if(!atomizer.pause){
            for(java.util.Iterator<Particle> i = atomizer.particles.iterator();i.hasNext();){
                i.next().force = new atomizer.Vector();
            }
            for(java.util.ListIterator<Particle> i = atomizer.particles.listIterator();i.hasNext();){
                Particle particle = i.next();
                if(i.hasNext()){
                try{
                for(java.util.ListIterator<Particle> j = atomizer.particles.listIterator(i.nextIndex());j.hasNext();){
                    Particle other = j.next();
                    double distance = Vector.distance(particle.position,other.position);
                    if(distance<(particle.mass+other.mass)){
                        if(particle instanceof Atom && other instanceof Atom){
                            particle.force=particle.force.multiply(particle.mass).add(other.force.multiply(other.mass)).divide(particle.mass+other.mass);
                            particle.velocity=particle.velocity.multiply(particle.mass).add(other.velocity.multiply(other.mass)).divide(particle.mass+other.mass);
                            particle.mass+=other.mass;
                            particle.charge+=other.charge;
                            atomizer.particles.remove(other);
                        }
                        else if(particle instanceof Atom){
                            particle.force=particle.force.multiply(particle.mass).add(other.force.multiply(other.mass)).divide(particle.mass+other.mass);
                            particle.velocity=particle.velocity.multiply(particle.mass).add(other.velocity.multiply(other.mass)).divide(particle.mass+other.mass);
                            if(other.mass <= 1.0/40.0){
                                other.mass=0;
                            }
                            particle.mass+=other.mass;
                            particle.charge+=other.charge;
                            atomizer.particles.remove(other);
                        }
                        else if(other instanceof Atom){
                            other.force=other.force.multiply(other.mass).add(particle.force.multiply(particle.mass)).divide(other.mass+particle.mass);
                            other.velocity=other.velocity.multiply(other.mass).add(particle.velocity.multiply(particle.mass)).divide(other.mass+particle.mass);
                            if(particle.mass <= 1.0/40.0){
                                particle.mass=0;
                            }
                            other.mass+=particle.mass;
                            other.charge+=particle.charge;
                            atomizer.particles.remove(particle);
                        }
                        else {
                            if(particle.mass <= 1.0/40.0){
                                particle.mass=0;
                            }
                            if(other.mass <= 1.0/40.0){
                                other.mass=0;
                            }
                            Particle atom = new Atom(0,0,particle.velocity.multiply(particle.mass).add(other.velocity.multiply(other.mass)).divide(particle.mass+other.mass));
                            atom.position=particle.position.add(other.position).divide(2);
                            atom.force=particle.force.multiply(particle.mass).add(other.force.multiply(other.mass)).divide(particle.mass+other.mass);
                            atom.mass=other.mass+particle.mass;
                            atom.charge=other.charge+particle.charge;
                            atomizer.particles.remove(particle);
                            atomizer.particles.remove(other);
                            atomizer.particles.add(atom);
                        }
                    }
                    if (distance>0.0000001){
                        double distance2=distance*distance;
                        Vector gravity = particle.position.sub(other.position).multiply(particle.mass*other.mass).divide(distance2*100.0);
                        particle.force=particle.force.sub(gravity);
                        other.force=other.force.add(gravity);
                        if(particle.charge!=0 && other.charge!=0){
                            atomizer.Vector electro = particle.position.sub(other.position).multiply(particle.charge*other.charge).divide(distance2*5.0);
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
                }catch(IndexOutOfBoundsException e){
                    System.err.println(e.getMessage());
                }
                }
                
                if(particle instanceof Atom){
                    if(particle.charge<particle.mass-particle.charge+0.5 && particle.mass>1.5 && Math.random()>0.93){
                        Vector decay = (new Vector(Math.random()*2.0-1.0, Math.random()*2.0-1.0));
                        decay = decay.divide(decay.getLength()).multiply((particle.mass+1));
                        if(Math.random()>0.45){
                            //Neutron emission
                            particle.mass-=1;
                            particle.force.add(decay.divide(particle.mass).negate());
                            Particle p = new Neutron((particle.position.x+decay.x), (particle.position.y+decay.y), decay);
                            p.force=decay.divide(p.mass);
                            atomizer.particles.add(p);
                        }
                        else{
                            //Beta minus
                            particle.charge+=1;
                            Particle p = new Electron((particle.position.x+decay.x), (particle.position.y+decay.y), decay);
                            p.force=decay.divide(p.mass);
                            atomizer.particles.add(p);
                            //TOD energy?/anti-neutrino?
                        }
                        if(particle.mass<0){
                            particle.mass=0;
                            System.out.println("Hmmm...");
                        }
                    }
                }
                
                particle.velocity=particle.velocity.add(particle.force.divide(particle.mass).multiply(dt));
                particle.position=particle.position.add(particle.velocity.multiply(dt));
            }
            frame.setTitle("Atomizer");
            }
            else{
                frame.setTitle("Atomizer - Paused");
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
        /*switch(selected){
            case neutron:
                particles.add(new Neutron(me.getX(), me.getY(), new Vector()));
            break;
            case electron:
                particles.add(new Electron(me.getX(), me.getY(), new Vector()));
            break;
            case proton:
                particles.add(new Proton(me.getX(), me.getY(), new Vector()));
            break;
        }*/
    }
    
    int startX=0,startY=0;
    boolean isPressed = false;
    int currentX,currentY;
    
    @Override
    public void mousePressed(MouseEvent me) {
        startX=me.getX();
        startY=me.getY();
        isPressed=true;
    }

    @Override
    public void mouseReleased(MouseEvent me) {
        isPressed=false;
        switch(selected){
            case neutron:
                particles.add(new Neutron(me.getX(), me.getY(), new Vector((startX-me.getX())/50.0,(startY-me.getY())/50.0)));
            break;
            case electron:
                particles.add(new Electron(me.getX(), me.getY(), new Vector((startX-me.getX())/50.0,(startY-me.getY())/50.0)));
            break;
            case proton:
                particles.add(new Proton(me.getX(), me.getY(), new Vector((startX-me.getX())/50.0,(startY-me.getY())/50.0)));
            break;
        }
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
        currentX=me.getX();
        currentY=me.getY();
    }

    @Override
    public void mouseMoved(MouseEvent me) {
        currentX=me.getX();
        currentY=me.getY();
    }
}
