package com.display;

import javax.swing.ImageIcon;
import javax.swing.JToolBar;
import javax.swing.JButton;
import javax.swing.JPanel;

import params.ModelSpecification;
import params.SystemParams;
import params.WindowManager;

import com.display.TubePanel.TubeClass;
import com.display.images.IconLibrary;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.FlowLayout;
import javax.swing.SwingConstants;

import models.Architecture;
import models.Arteriole;
import models.Artery;
import models.Brain;
import models.BrainParenchyma;
import models.Capillary;
import models.ElasticTube;
import models.FirstArtery;
import models.Hemisphere;
import models.Vein;
import models.Veinule;
import models.VenousSinus;

import org.jscroll.JScrollDesktopPane;
import org.jscroll.widgets.JScrollInternalFrame;

import java.awt.Color;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

public class MainToolbar extends JToolBar {

	private JButton btnNew;
	private JButton btnRun;
	private JPanel panel;
	
	public MainToolbar() {
		
		
		

		init();
	}

	public MainToolbar(int arg0) {
		super(arg0);
		init();
	}

	public MainToolbar(String arg0) {
		super(arg0);
		init();
	}

	public MainToolbar(String arg0, int arg1) {
		super(arg0, arg1);
		init();
	}

	private void init() {
		panel = new JPanel();
		add(panel);
		Image img = IconLibrary.NEWICON;
		img = img.getScaledInstance(25, 25,  java.awt.Image.SCALE_SMOOTH);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{43, 33, 33, 0};
		gbl_panel.rowHeights = new int[]{0, 0, 0};
		gbl_panel.columnWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		btnNew = new JButton();
		btnNew.setHorizontalAlignment(SwingConstants.LEFT);
		btnNew.setIcon(new ImageIcon(img));
		GridBagConstraints gbc_btnNew = new GridBagConstraints();
		gbc_btnNew.anchor = GridBagConstraints.NORTHWEST;
		gbc_btnNew.insets = new Insets(0, 0, 0, 5);
		gbc_btnNew.gridx = 0;
		gbc_btnNew.gridy = 1;
		panel.add(btnNew, gbc_btnNew);
		img = IconLibrary.RUNICON;
		img = img.getScaledInstance(25, 25,  java.awt.Image.SCALE_SMOOTH);
		btnRun = new JButton();
		btnRun.setHorizontalAlignment(SwingConstants.LEFT);
		btnRun.setIcon(new ImageIcon(img));
		GridBagConstraints gbc_btnRun = new GridBagConstraints();
		gbc_btnRun.insets = new Insets(0, 0, 0, 5);
		gbc_btnRun.anchor = GridBagConstraints.NORTHWEST;
		gbc_btnRun.gridx = 1;
		gbc_btnRun.gridy = 1;
		panel.add(btnRun, gbc_btnRun);
		// listeners
		btnNew.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(WindowManager.MAINWINDOW != null)
					WindowManager.MAINWINDOW.getGraphicalModelPanel().initNew();
			}
		});
		btnRun.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JScrollDesktopPane jsd = WindowManager.MAINWINDOW.getGraphicalModelPanel();
				FirstArtery firstArtery = (FirstArtery) jsd.getFirstArteryFrame().getTubePanel().getTube();
				VenousSinus vsinous = (VenousSinus) jsd.getVenousSinousFrame().getTubePanel().getTube();
				if(firstArtery == null || vsinous == null){
					SystemParams.errordlg("Missing first artery or venous senous");
					return;
				}
				
				// on instancie le cerveau brain
				BrainParenchyma left_brain = new BrainParenchyma("left_brain", Hemisphere.LEFT);
				BrainParenchyma right_brain = new BrainParenchyma("right_brain", Hemisphere.RIGHT);
				Brain brain = new Brain(left_brain, right_brain);
				
				Architecture architecture = new Architecture(firstArtery, vsinous, brain);
				// on initialise le systeme (pression entree - sortie)
				ModelSpecification.init(architecture);
				
				// ==========  Check de la structure ==================
				if(jsd.getFirstArteryFrame().getLineLinks().isEmpty() || jsd.getVenousSinousFrame().getLineLinks().isEmpty()){
					SystemParams.errordlg("First artery and/or vSinous are linked to nothing !");
					return;
				}
				//  on verifie que chaque bloc est au moins une fois un fils et un parent et qu'on a bien 2 hemi!
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
							SystemParams.errordlg("Error with architecture. Please ensure that everything is linked ["+jsf+"]");
							return;
						}
					}
				}
				if(!atLeastOneLeftHemi || !atLeastOneRightHemi){
					SystemParams.errordlg("Do you know that a brain has 2 hemispheres ? :)");
					return;
				}
				
				// check la validite des liens
				if(!checkArchitectureValidity(firstArtery)){
					SystemParams.errordlg("Incorrect architecture ...");
					return;
				}
				System.out.println("ok");
				// ================= Fin du check ==================
			}


		});
	}
	
	private boolean checkArchitectureValidity(ElasticTube startPoint) {
		if(startPoint instanceof VenousSinus)
			return true;
		if(!(startPoint instanceof FirstArtery) && !checkValidity(startPoint))
			return false;
		for(ElasticTube tube : startPoint.getChildren()){
			if(!(startPoint instanceof FirstArtery) && !(tube instanceof VenousSinus) && tube.getHemisphere() != startPoint.getHemisphere())
				return false;
			if(startPoint instanceof FirstArtery && !(tube instanceof Artery))
				return false;
			if(startPoint instanceof Artery && !(tube instanceof Arteriole || tube instanceof Artery))
				return false;
			if(startPoint instanceof Arteriole && !(tube instanceof Capillary))
				return false;
			if(startPoint instanceof Capillary && !(tube instanceof Veinule))
				return false;
			if(startPoint instanceof Veinule && !(tube instanceof Vein))
				return false;
			if(startPoint instanceof Vein && !(tube instanceof VenousSinus || tube instanceof Vein))
				return false;
			if(!checkArchitectureValidity(tube))
				return false;
		}
		return true;
	}
	private boolean checkValidity(ElasticTube t1) {
		if((t1.getParents().isEmpty() && t1.getChildren().isEmpty()))
			return false;
		for(ElasticTube parent : t1.getParents()){
			if(iterativeCheckIndirectParentLink(t1, parent,0))
				return false;
		}
		for(ElasticTube child : t1.getChildren()){ A VOIR
			if(iterativeCheckIndirectChildLink(t1, child,0))
				return false;
		}
		return true;
	}
	
	private boolean iterativeCheckIndirectParentLink(ElasticTube t1, ElasticTube parent,int level) {
		if(parent.equals(t1))
			return true;
		
		for(ElasticTube locparent : t1.getParents()){
			if(locparent.equals(parent) && level == 0)
				continue;
			if(!locparent.equals(parent)){
				if(iterativeCheckIndirectParentLink(locparent,parent,level+1))
					return true;
				continue;
			}else{
				return true;
			}
		}
		return false;
	}
	
	private boolean iterativeCheckIndirectChildLink(ElasticTube t1, ElasticTube child, int level){
		if(child.equals(t1))
			return true;
		
		for(ElasticTube locchild : t1.getParents()){
			if(locchild.equals(child) && level == 0)
				continue;
			if(!locchild.equals(child)){
				if(iterativeCheckIndirectChildLink(locchild,child,level+1))
					return true;
				continue;
			}else{
				return true;
			}
		}
		return false;
	}
}
