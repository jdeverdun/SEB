package com.display;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
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

import com.display.TubePanel.TubeClass;
import com.display.images.IconLibrary;


public class GlobalArchitectureToolbar extends JToolBar{

	private JPanel panel;
	private JButton btnOrganize;
	
	// des variables pour le tri
	private Dimension iframeDim;
	private Dimension panelSize;
	private int xoffset = 40; //espace entre 2 blocs en x
	private int yoffset = 40; //espace entre 2 blocs en y
	private int defDistFromCenter; // espace au centre
	private int xcenter;
	
	
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
				// Etape 2 on verifie que chaque bloc est au moins une fois un fils et un parent et qu'on a bien 2 hemi!
				boolean isChild = false;
				boolean isParent = false;
				boolean atLeastOneLeftHemi = false;
				boolean atLeastOneRightHemi = false;
				for(JScrollInternalFrame jsf : jsd.getInternalFrames()){
					if(jsf != jsd.getFirstArteryFrame() && jsf != jsd.getVenousSinousFrame()){
						isChild = false;
						isParent = false;
						if(jsf.getTubePanel().getTube().getHemisphere() == Hemisphere.LEFT)
							atLeastOneLeftHemi = true;
						else
							atLeastOneRightHemi = true;
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
				if(!atLeastOneLeftHemi || !atLeastOneRightHemi){
					errordlg("Do you know that a brain has 2 hemispheres ? :)");
					return;
				}
				// Si tout est bon on peut commencer à faire l'agencement
				// d'abord on calcul les largeurs max et profondeur max en nb de bloc
				HashMap<Integer,ArrayList<JScrollInternalFrame>> largeurMaxLevel = new HashMap<Integer, ArrayList<JScrollInternalFrame>>();
				HashMap<TubeClass,Integer> longestPath = calcLongestPathFromFirstArtery(jsd.getFirstArteryFrame());
				for(int i = 1; i <=longestPath.get(TubeClass.VenousSinus); i++) largeurMaxLevel.put(i, new ArrayList<JScrollInternalFrame>());
				largeurMaxLevel = recursiveCountLevelWidth(jsd.getFirstArteryFrame(), 1, largeurMaxLevel);
				// maintenant qu'on sait tout on cherche le nombre de blocs max dans hemi gauche et droit
				int nMaxLeftHemi = 0;
				int nMaxRightHemi = 0;
				for(Integer level : largeurMaxLevel.keySet()){
					int nLeftHemi = 0;
					int nRightHemi = 0;
					for(JScrollInternalFrame jsf : largeurMaxLevel.get(level)){
						switch(jsf.getTubePanel().getTube().getHemisphere()){
						case LEFT:
							nLeftHemi++;break;
						case RIGHT:
							nRightHemi++;break;
						default:
							break;
						}
					}
					nMaxLeftHemi = Math.max(nMaxLeftHemi, nLeftHemi);
					nMaxRightHemi = Math.max(nMaxRightHemi, nRightHemi);
				}
				// on a toutes les dimensions ... maintenant on tri ! 
				// d'abord les tubes vSinous et firstArtery
				iframeDim = JScrollInternalFrame.DEFAULT_DIMENSION;
				panelSize = jsd.getSize();
				xoffset = 40; //espace entre 2 blocs en x
				yoffset = 40; //espace entre 2 blocs en y
				defDistFromCenter = (int) iframeDim.getWidth(); // espace au centre
				xcenter = (int) (panelSize.getWidth()/2);
				
				jsd.getFirstArteryFrame().setLocation((int) (xcenter - iframeDim.getWidth()/2), 10);
				jsd.getVenousSinousFrame().setLocation((int) (xcenter - iframeDim.getWidth()/2), (int) ((iframeDim.getHeight()+yoffset)*longestPath.get(TubeClass.VenousSinus)-iframeDim.getHeight()-25));
				// ensuite les frames 
				// le positionnement commence à partir du centre
				/*for(Integer level:largeurMaxLevel.keySet()){
					int xpointer_left = 0;//les decalages gauche / droite
					int xpointer_right = 0;
					for(JScrollInternalFrame jsf : largeurMaxLevel.get(level)){
						if(jsf.getTubePanel().getTube().getHemisphere() == Hemisphere.LEFT){
							jsf.setLocation((int) (xcenter-defDistFromCenter-(iframeDim.getWidth()-xoffset)*xpointer_left-iframeDim.getWidth()), (int) ((iframeDim.getHeight()+yoffset)*longestPath.get(jsf.getTubePanel().getTubeType())-iframeDim.getHeight()-25));
							xpointer_left++;
						}
					}
				}*/
				for(LineLink line : jsd.getFirstArteryFrame().getLineLinks()){
					if(line.getChild().getTubePanel().getTube().getHemisphere() == Hemisphere.LEFT)
						setInternalFrameLocation(line.getChild(), 0, 1);
				}
			}
		});
	}

	private int setInternalFrameLocation(JScrollInternalFrame jsf, int decalagex, int decalagey){
		int decalagefils = 0;
		for(LineLink line : jsf.getLineLinks()){
			if
			if(line.getChild().getTubePanel().getTubeType() == TubeClass.VenousSinus){
				jsf.setLocation((int) (xcenter-defDistFromCenter-(iframeDim.getWidth()-xoffset)*decalagex-iframeDim.getWidth()), (int) ((iframeDim.getHeight()+yoffset)*decalagey-iframeDim.getHeight()-25));
				
				break;
			}else{
				if(line.getParent() == jsf){
					jsf.setLocation((int) (xcenter-defDistFromCenter-(iframeDim.getWidth()-xoffset)*decalagex-iframeDim.getWidth()), (int) ((iframeDim.getHeight()+yoffset)*decalagey-iframeDim.getHeight()-25));
					decalagex = setInternalFrameLocation(line.getChild(), decalagex+decalagefils,decalagey++);
					decalagefils++;
				}
			}
		}
		return decalagex+decalagefils;
	}
	
	private int calcLongestPathFromTo(
			JScrollInternalFrame startPoint, TubeClass tclass) {
		int max = 0;
		if(startPoint.getTubePanel().getTubeType() == tclass)
			return 1+max;
		for(LineLink line : startPoint.getLineLinks()){
			if(line.getChild() != startPoint){
				max = Math.max(calcLongestPathFromTo(line.getChild(), tclass),max);
			}
		}
		return 1+max;
	}
	
	private HashMap<TubeClass,Integer> calcLongestPathFromFirstArtery(
			JScrollInternalFrame startPoint) {
		HashMap<TubeClass,Integer> res = new HashMap<TubeClass,Integer>();
		int max = 0;
		// artery
		for(LineLink line : startPoint.getLineLinks()){
			if(line.getChild() != startPoint){
				max = Math.max(calcLongestPathFromTo(line.getChild(),TubeClass.Artery),max);
			}
		}
		res.put(TubeClass.Artery, 1+max);
		// arteriole
		max = 0;
		for(LineLink line : startPoint.getLineLinks()){
			if(line.getChild() != startPoint){
				max = Math.max(calcLongestPathFromTo(line.getChild(),TubeClass.Arteriole),max);
			}
		}
		res.put(TubeClass.Arteriole, 1+max);
		// capillary
		max = 0;
		for(LineLink line : startPoint.getLineLinks()){
			if(line.getChild() != startPoint){
				max = Math.max(calcLongestPathFromTo(line.getChild(),TubeClass.Capillary),max);
			}
		}
		res.put(TubeClass.Capillary, 1+max);
		// veinule
		max = 0;
		for(LineLink line : startPoint.getLineLinks()){
			if(line.getChild() != startPoint){
				max = Math.max(calcLongestPathFromTo(line.getChild(),TubeClass.Veinule),max);
			}
		}
		res.put(TubeClass.Veinule, 1+max);
		// vein
		max = 0;
		for(LineLink line : startPoint.getLineLinks()){
			if(line.getChild() != startPoint){
				max = Math.max(calcLongestPathFromTo(line.getChild(),TubeClass.Vein),max);
			}
		}
		res.put(TubeClass.Vein, 1+max);
		// vSinous
		max = 0;
		for(LineLink line : startPoint.getLineLinks()){
			if(line.getChild() != startPoint){
				max = Math.max(calcLongestPathFromTo(line.getChild(),TubeClass.VenousSinus),max);
			}
		}
		res.put(TubeClass.VenousSinus, 1+max);
		return res;
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
