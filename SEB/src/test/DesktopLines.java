package test;
import java.awt.*;  
import java.awt.event.*;  
import java.awt.geom.*;  
import javax.swing.*;  
import javax.swing.event.*;  
   
public class DesktopLines  
{  
    public DesktopLines()  
    {  
        JDesktopPane desktop = new JDesktopPane()  
        {  
            protected void paintComponent(Graphics g)  
            {  
                super.paintComponent(g);  
                Graphics2D g2 = (Graphics2D)g;  
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,  
                                    RenderingHints.VALUE_ANTIALIAS_ON);  
                drawLines(g2, this);  
            }  
        };  
        desktop.setDesktopManager(new DTManager(desktop));  
        Point[] points = {  
           new Point(50, 50), new Point(300, 175), new Point(175, 300)   
        };  
        for(int j = 0; j < points.length; j++)  
            desktop.add(getIFrame((j + 1), points[j]));  
        JFrame f = new JFrame();  
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
        f.getContentPane().add(desktop);  
        f.setSize(500,500);  
        f.setLocation(100,200);  
        f.setVisible(true);  
    }  
    
    private JInternalFrame getIFrame(int count, Point p)  
    {  
        JInternalFrame iframe = new JInternalFrame("iframe " + count, true,  
                                                   true, true, true);  
        iframe.setSize(100, 100);  
        iframe.setLocation(p.x, p.y);  
        iframe.setVisible(true);  
        return iframe;  
    }  
   
    private void drawLines(Graphics2D g2, JDesktopPane desktop)  
    {  
        Component[] c = desktop.getComponents();  
        for(int j = 0; j < c.length; j++)  
        {  
            int x1 = c[j].getX() + c[j].getWidth()/2;  
            int y1 = c[j].getY() + c[j].getHeight()/2;  
            for(int k = j + 1; k < c.length; k++)  
            {  
                int x2 = c[k].getX() + c[k].getWidth()/2;  
                int y2 = c[k].getY() + c[k].getHeight()/2;  
                g2.draw(new Line2D.Double(x1, y1, x2, y2));  
            }  
        }  
    }  
   
    public static void main(String[] args)  
    {  
        new DesktopLines();  
    }  
}  
   
class DTManager extends DefaultDesktopManager  
{  
    JDesktopPane desktop;  
   
    public DTManager(JDesktopPane desktop)  
    {  
        super();  
        this.desktop = desktop;  
    }  
   
    public void dragFrame(JComponent c, int newX, int newY)  
    {  
        c.setLocation(newX, newY);  
        desktop.repaint();  
    }  
}  