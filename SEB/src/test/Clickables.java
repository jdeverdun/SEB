package test;

import java.awt.*;  
import java.awt.event.*;  
import java.awt.geom.Line2D;  
import javax.swing.*;  
   
public class Clickables extends JPanel {  
    Point selection = new Point(-1,-1);  
   
    protected void paintComponent(Graphics g) {  
        super.paintComponent(g);  
        Graphics2D g2 = (Graphics2D)g;  
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,  
                            RenderingHints.VALUE_ANTIALIAS_ON);  
        Component[] c = getComponents();  
        for(int j = 0; j < c.length; j++) {  
            Rectangle r = c[j].getBounds();  
            double x1 = r.getCenterX();  
            double y1 = r.getCenterY();  
            for(int k = j+1; k < c.length; k++) {  
                r = c[k].getBounds();  
                double x2 = r.getCenterX();  
                double y2 = r.getCenterY();  
                Color color = (j==selection.x && k==selection.y) ? Color.red  
                                                                 : Color.blue;  
                g2.setPaint(color);  
                g2.draw(new Line2D.Double(x1, y1, x2, y2));  
            }  
        }  
    }  
   
    public static void main(String[] args) {  
        Clickables test = new Clickables();  
        test.setLayout(new GridBagLayout());  
        GridBagConstraints gbc = new GridBagConstraints();  
        gbc.weightx = 1.0;  
        test.add(new JButton("Button 1"), gbc);  
        test.add(new JButton("Button 2"), gbc);  
        test.addMouseListener(test.ml);  
        JFrame f = new JFrame();  
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
        f.getContentPane().add(test);  
        f.setSize(400,200);  
        f.setLocation(200,200);  
        f.setVisible(true);  
    }  
   
    private MouseListener ml = new MouseAdapter() {  
        final int S = 6;  
        Rectangle sensor = new Rectangle(S,S);  
   
        public void mousePressed(MouseEvent e) {  
            Point p = e.getPoint();  
            sensor.setFrameFromCenter(p.x, p.y, p.x+S/2, p.y+S/2);  
            Container source = (Container)e.getComponent();  
            Component[] c = source.getComponents();  
            for(int j = 0; j < c.length; j++) {  
                Rectangle r = c[j].getBounds();  
                double x1 = r.getCenterX();  
                double y1 = r.getCenterY();  
                for(int k = j+1; k < c.length; k++) {  
                    r = c[k].getBounds();  
                    double x2 = r.getCenterX();  
                    double y2 = r.getCenterY();  
                    if(new Line2D.Double(x1,y1,x2,y2).intersects(sensor)) {  
                        if(selection.x == j && selection.y == k)  
                            selection.setLocation(-1,-1);  // toggle off  
                        else  
                            selection.setLocation(j, k);   // set values  
                        source.repaint();  
                        return;  
                    }  
                }  
            }  
        }  
    };  
}  