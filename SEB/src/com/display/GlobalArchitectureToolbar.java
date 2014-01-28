package com.display;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import models.Hemisphere;
import net.miginfocom.swing.MigLayout;
import javax.swing.JButton;

import org.jscroll.JScrollDesktopPane;
import org.jscroll.widgets.JScrollInternalFrame;

import params.WindowManager;

import com.display.images.IconLibrary;


public class GlobalArchitectureToolbar extends JToolBar{

	private JPanel panel;
	private JButton btnOrganize;
	
	public GlobalArchitectureToolbar() {
		setOrientation(JToolBar.VERTICAL);
		
		panel = new JPanel();
		add(panel);
		panel.setLayout(new MigLayout("", "[]", "[]"));
		
		btnOrganize = new JButton();
		Image img = IconLibrary.ORGANIZEICON;
		img = img.getScaledInstance(20, 20,  java.awt.Image.SCALE_SMOOTH); 
		btnOrganize.setIcon(new ImageIcon(img));
		panel.add(btnOrganize, "cell 0 0");
		
		
		// Events
		btnOrganize.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// Etape 1 : on verifie la presence d'un sinus veineux et de la premiere artere
				JScrollDesktopPane jsd = WindowManager.MAINWINDOW.getGraphicalModelPanel();
				if(jsd.getFirstArteryFrame() == null || jsd.getVenousSinousFrame() == null){
					errordlg("Please ensure that you have FirstArtery & vSinous");
					return;
				}
				if(jsd.getFirstArteryFrame().getLineLinks().isEmpty() || jsd.getVenousSinousFrame().getLineLinks().isEmpty()){
					errordlg("First arter and/or vSinous are linked to nothing !");
					return;
				}
				// Etape 2 on verifie que chaque bloc est au moins une fois un fils (sauf firstArtery)
				boolean isChild = false;
				boolean isParent = false;
				for(JScrollInternalFrame jsf : jsd.getInternalFrames()){
					if(jsf != jsd.getFirstArteryFrame() && jsf != jsd.getVenousSinousFrame()){
						isChild = false;
						isParent = false;
						for(LineLink line : jsf.getLineLinks()){
							if(line.getChild() == jsf){
								isChild = true;
							}
							if(line.getParent() == jsf){
								isParent = true;
							}
							if(isParent && isChild)
								break;
						}
						if(!isChild || !isParent){
							errordlg("Error with architecture. Please ensure that everything is linked");
							return;
						}
					}
				}
				// Si tout est bon on peut commencer à faire l'agencement
				// d'abord on calcul les largeurs max et profondeur max en nb de bloc
				HashMap<Integer,ArrayList<JScrollInternalFrame>> largeurMaxLevel = new HashMap<Integer, ArrayList<JScrollInternalFrame>>();
				int longestPath = calcLongestPathFrom(jsd.getFirstArteryFrame());
				for(int i = 1; i <=longestPath; i++) largeurMaxLevel.put(i, new ArrayList<JScrollInternalFrame>());
				
				System.out.println(longestPath);
				largeurMaxLevel = recursiveCountLevelWidth(jsd.getFirstArteryFrame(), 1, largeurMaxLevel);
				System.out.println("larg : "+largeurMaxLevel.get(2).size());
			}
		});
	}

	
	private int calcLongestPathFrom(
			JScrollInternalFrame startPoint) {
		int max = 0;
		for(LineLink line : startPoint.getLineLinks()){
			if(line.getChild() != startPoint){
				max = Math.max(calcLongestPathFrom(line.getChild()),max);
			}
		}
		return 1+max;
	}
	
	/**
	 * Recursively count width of a level in a tree
	 * @param startPoint
	 * @param level
	 * @param largeurMaxLevel
	 * @return
	 */
	private HashMap<Integer,ArrayList<JScrollInternalFrame>> recursiveCountLevelWidth(
			JScrollInternalFrame startPoint, int level,
			HashMap<Integer,ArrayList<JScrollInternalFrame>> largeurMaxLevel) {
		largeurMaxLevel.get(level).add(startPoint);
		for(LineLink line : startPoint.getLineLinks()){
			if(line.getChild() != startPoint){
				recursiveCountLevelWidth(line.getChild(), level+1, largeurMaxLevel);
			}
		}
		return largeurMaxLevel;
	}
	
	private void errordlg(String string) {
		final String msg = string;
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				JDialog.setDefaultLookAndFeelDecorated(true);
				JOptionPane.showMessageDialog(WindowManager.MAINWINDOW,
						msg,
					    "Error",
					    JOptionPane.ERROR_MESSAGE);
			}
		});
	}
}
