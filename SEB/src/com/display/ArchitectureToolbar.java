package com.display;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.JButton;

import models.Arteriole;
import models.Artery;
import models.Capillary;
import models.Hemisphere;
import models.Vein;
import models.Veinule;

import com.display.TubePanel.TubeClass;
import com.display.images.IconLibrary;

import params.WindowManager;

import java.awt.Image;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.IOException;

public class ArchitectureToolbar extends JToolBar {

	private Hemisphere hemi; // hemisphere associe
	private JButton btnAddArtery;
	private JButton btnAddArteriole;
	private JButton btnAddCapillary;
	private JButton btnAddVeinule;
	private JButton btnAddVein;
	
	public ArchitectureToolbar(Hemisphere hemi) {
		setHemi(hemi);
		init();
	}

	public ArchitectureToolbar(int arg0,Hemisphere hemi) {
		super(arg0);
		setHemi(hemi);
		init();
	}

	public ArchitectureToolbar(String arg0,Hemisphere hemi) {
		super(arg0);
		setHemi(hemi);
		init();
	}

	public ArchitectureToolbar(String arg0, int arg1,Hemisphere hemi) {
		super(arg0, arg1);
		setHemi(hemi);
		init();
	}
	
	/**
	 * Initialisation de la structure
	 */
	private void init(){
		this.setOrientation(SwingConstants.VERTICAL);
		
		btnAddArtery = new JButton();
		Image img = IconLibrary.ARTERY;
		img = img.getScaledInstance(25, 25,  java.awt.Image.SCALE_SMOOTH); 
		btnAddArtery.setIcon(new ImageIcon(img));

		add(btnAddArtery);
		
		btnAddArteriole = new JButton();
		add(btnAddArteriole);
		img = IconLibrary.ARTERIOLE;
		img = img.getScaledInstance(25, 25,  java.awt.Image.SCALE_SMOOTH); 
		btnAddArteriole.setIcon(new ImageIcon(img));
		
		btnAddCapillary = new JButton();
		add(btnAddCapillary);
		img = IconLibrary.CAPILLARY;
		img = img.getScaledInstance(25, 25,  java.awt.Image.SCALE_SMOOTH); 
		btnAddCapillary.setIcon(new ImageIcon(img));
		
		btnAddVeinule = new JButton();
		add(btnAddVeinule);
		img = IconLibrary.VEINULE;
		img = img.getScaledInstance(25, 25,  java.awt.Image.SCALE_SMOOTH); 
		btnAddVeinule.setIcon(new ImageIcon(img));
		
		btnAddVein = new JButton();
		add(btnAddVein);
		img = IconLibrary.VEIN;
		img = img.getScaledInstance(25, 25,  java.awt.Image.SCALE_SMOOTH); 
		btnAddVein.setIcon(new ImageIcon(img));
		
		btnAddArtery.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(WindowManager.MAINWINDOW != null){					
					Artery art = new Artery("", getHemi());
					WindowManager.MAINWINDOW.getGraphicalModelPanel().add(art.getName(), new ImageIcon(IconLibrary.ARTERY.getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH)), new TubePanel(art, TubeClass.Artery), false);
			
				}
			}
		});
		btnAddArteriole.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(WindowManager.MAINWINDOW != null){	
					Arteriole art = new Arteriole("", getHemi());
					WindowManager.MAINWINDOW.getGraphicalModelPanel().add(art.getName(),new ImageIcon(IconLibrary.ARTERIOLE.getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH)), new TubePanel(art, TubeClass.Arteriole), false);
			
				}
			}
		});
		btnAddCapillary.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						if(WindowManager.MAINWINDOW != null){	
							Capillary cap = new Capillary("", getHemi());
							WindowManager.MAINWINDOW.getGraphicalModelPanel().add(cap.getName(),new ImageIcon(IconLibrary.CAPILLARY.getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH)), new TubePanel(cap, TubeClass.Capillary), false);
						}
					}
				});
		btnAddVeinule.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(WindowManager.MAINWINDOW != null){
					Veinule vl = new Veinule("", getHemi());
					WindowManager.MAINWINDOW.getGraphicalModelPanel().add(vl.getName(),new ImageIcon(IconLibrary.VEINULE.getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH)), new TubePanel(vl, TubeClass.Veinule), false);
				}
			}
		});
		btnAddVein.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(WindowManager.MAINWINDOW != null){
					Vein v = new Vein("", getHemi());
					WindowManager.MAINWINDOW.getGraphicalModelPanel().add(v.getName(),new ImageIcon(IconLibrary.VEIN.getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH)), new TubePanel(v, TubeClass.Vein), false);
			
				}
			}
		});
	}

	public Hemisphere getHemi() {
		return hemi;
	}

	public void setHemi(Hemisphere hemi) {
		this.hemi = hemi;
	}

}
