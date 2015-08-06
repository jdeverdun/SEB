package test;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.jscroll.JScrollDesktopPane;
import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.substance.api.skin.SubstanceGraphiteLookAndFeel;

public class test {

	/*
	  @param args
	 */
	public static void main(String[] args) {
		JFrame.setDefaultLookAndFeelDecorated(true);
		try {
	          UIManager.setLookAndFeel(new SubstanceGraphiteLookAndFeel());
        } catch (Exception e) {
          System.out.println("Substance Graphite failed to initialize");
        }
		UIManager.put(SubstanceLookAndFeel.WINDOW_ROUNDED_CORNERS, Boolean.FALSE);
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				JFrame f = new JFrame("Scrollable Desktop");
			     f.setSize(300,300);
			     // prepare the menuBar
			     JMenuBar menuBar = new JMenuBar();
			     f.setJMenuBar(menuBar);
			 
			     // create the scrollable desktop instance and add it to the JFrame
			     JScrollDesktopPane scrollableDesktop =
			           new JScrollDesktopPane(menuBar);
			     f.getContentPane().add(scrollableDesktop);
			     f.setVisible(true);
			 
			     // add a frame to the scrollable desktop
			     JPanel frameContents = new JPanel();
			     frameContents.add(
			           new JLabel("Hello and welcome to JScrollDesktopPane."));
			 
			 scrollableDesktop.add(frameContents);
			}
		});
		
	}

}
