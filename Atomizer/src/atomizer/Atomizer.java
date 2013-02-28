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
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.util.concurrent.CopyOnWriteArrayList;
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
    
    BufferedImage circle;
    
    public Atomizer(){
        addMouseListener(this);
        addMouseMotionListener(this);
        addKeyListener(this);
        setFocusable(true);
        setSize(800, 600);
        setMinimumSize(new Dimension(800,600));
    }
    
    
    public  java.util.concurrent.CopyOnWriteArrayList<Particle> particles = new java.util.concurrent.CopyOnWriteArrayList<Particle>();

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
     public boolean walls = false,sphere=false;
     
     //public BufferedImage blur = null;
     //public boolean isBlur = false;
     
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
        else if(ke.getKeyCode()==KeyEvent.VK_INSERT){
            walls=!walls;
        }
        else if(ke.getKeyCode()==KeyEvent.VK_HOME){
            sphere=!sphere;
        }
        else if(ke.getKeyCode()==KeyEvent.VK_DELETE){
            particles = new CopyOnWriteArrayList<Particle>();
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
        g2d.setColor(new Color(170,170,170));
        g2d.fillRect(0, 0, 800, 600);
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
                g2d.fillOval((int)(pos.x-p.mass*2),(int)(pos.y-p.mass*2) , (int)p.mass*4, (int)p.mass*4);
            }
            else if(p instanceof Electron){
                atomizer.Vector pos = p.position;//TODO view transformation
                g2d.setColor(new Color(0.0f, 0.3f,1.0f,0.3f));
                g2d.fillOval((int)pos.x-2,(int)pos.y-2 , 4, 4);
                g2d.setColor(Color.BLUE);
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
        if(walls){
            g2d.setColor(Color.BLACK);
            g2d.fillRect(0, 0, 800, 5);
            g2d.fillRect(0, 0, 5, 600);
            g2d.fillRect(0, 595, 800, 5);
            g2d.fillRect(795, 0, 5, 600);
        }
        if(sphere){
            g2d.drawImage(circle, 0, 0, null);
        }
        
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Atomizer atomizer = new Atomizer();
        JFrame frame = new JFrame("Atomizer");
        frame.add(atomizer);//frame.getContentPane().add(atomizer);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.pack();
        frame.setSize(800, 600+frame.getInsets().top);
        frame.setMinimumSize(new Dimension(800,600+frame.getInsets().top));//TODO works sometimes
        frame.setVisible(true);
        frame.setFocusable(true);
        frame.addKeyListener(atomizer);
        atomizer.circle = new BufferedImage(800, 600, BufferedImage.TYPE_4BYTE_ABGR);
        for(int x=0;x<800;x++){
            for(int y=0;y<600;y++){
                double dx=400.0-x;double dy=y-300.0;
                if(Math.sqrt(dx*dx+dy*dy)<=302.0){//if(dx*dx+dy*dy<=300.0*300.0){
                    atomizer.circle.setRGB(x, y, (new Color(0.0f,0.0f,0.0f,0.0f)).getRGB()); 
                }
                else{
                    atomizer.circle.setRGB(x, y, (new Color(0.0f,0.0f,0.0f,0.6f)).getRGB()); 
                }
            }
        }
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
                }catch(IndexOutOfBoundsException e){
                    System.err.println(e.getMessage());
                }
                }
                
                if(particle instanceof Atom){
                    if(particle.charge<particle.mass-particle.charge+(Math.random()*3.0-1.1) && particle.mass>1.5 && Math.random()>0.98){
                        Vector decay = (new Vector(Math.random()*2.0-1.0, Math.random()*2.0-1.0));
                        decay = decay.divide(decay.getLength()).multiply((particle.mass+1));
                        if(Math.random()>0.4){
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
                if(atomizer.walls){
                    if(particle.position.x<5){
                        particle.position.x=5;
                    }
                    if(particle.position.x>795){
                        particle.position.x=795;
                    }
                    if(particle.position.y<5){
                        particle.position.y=5;
                    }
                    if(particle.position.y>595){
                        particle.position.y=595;
                    }
                }
                if(atomizer.sphere){
                    Vector p = new Vector(particle.position);
                    Vector c = new Vector(400,300);
                    p=p.sub(c);
                    double l =p.getLength();
                    if(l>=300.0){
                        p=p.divide(l).multiply(300);
                        p=p.add(c);
                        particle.position=new Vector(p);
                    }
                }
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
        Vector v;
        if(me.getButton()==me.BUTTON3){
            Vector mouse = new Vector(me.getX(),me.getY());
            Particle near=null;double d=Double.POSITIVE_INFINITY;
            for(java.util.Iterator<Particle> i = particles.iterator();i.hasNext();){
                Particle c = i.next();
                if(c instanceof Atom){
                    double dist = Vector.distance(c.position, mouse);
                    if(dist<d){
                        near = c;
                        d=dist;
                    }
                }
            }
            if(near != null){
                double orbitL = d;
                double m=0;
                switch(selected){
                    case neutron:
                    case proton:
                        m=1.0;
                    break;
                    case electron:
                        m=1.0/50.0;
                }
                Vector gravity = near.position.sub(mouse).multiply(near.mass*m).divide(orbitL*orbitL*100.0);
                if(selected==Part.electron){
                    gravity.add(near.position.sub(mouse).multiply(near.charge*-1.0).divide(orbitL*orbitL*10.0));
                }
                
                v = gravity.divide(gravity.getLength());//normalize
                v.x=v.x;
                v.y=-v.y;//perpendicular
                
                //F=m*v^2*l; v^2=l*f/m; v=sqrt(l*grav/mass)
                v.multiply(Math.sqrt(orbitL*gravity.getLength()/m));
           }
            else{
                v=new Vector((startX-me.getX())/50.0,(startY-me.getY())/50.0);
            }
        }
        else{
            v=new Vector((startX-me.getX())/50.0,(startY-me.getY())/50.0);
        }
        isPressed=false;
        switch(selected){
            case neutron:
                particles.add(new Neutron(startX, startY, v));
            break;
            case electron:
                particles.add(new Electron(startX, startY, v));
            break;
            case proton:
                particles.add(new Proton(startX, startY, v));
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
