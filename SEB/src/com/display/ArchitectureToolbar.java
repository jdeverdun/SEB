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
					WindowManager.MAINWINDOW.getGraphicalModelPanel().addArtery(getHemi());
			
				}
			}
		});
		btnAddArteriole.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(WindowManager.MAINWINDOW != null){	
					WindowManager.MAINWINDOW.getGraphicalModelPanel().addArteriole(getHemi());
			
				}
			}
		});
		btnAddCapillary.addActionListener(new ActionListener() {
					
				@Override
				public void actionPerformed(ActionEvent e) {
					if(WindowManager.MAINWINDOW != null){	
						WindowManager.MAINWINDOW.getGraphicalModelPanel().addCapillary(getHemi());
					}
				}
			});
		btnAddVeinule.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(WindowManager.MAINWINDOW != null){
					WindowManager.MAINWINDOW.getGraphicalModelPanel().addVeinule(getHemi());
				}
			}
		});
		btnAddVein.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(WindowManager.MAINWINDOW != null){
					WindowManager.MAINWINDOW.getGraphicalModelPanel().addVein(getHemi());
			
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
	
	@Override
	public void setEnabled(boolean b){
		super.setEnabled(b);
		btnAddArtery.setEnabled(b);
		btnAddArteriole.setEnabled(b);
		btnAddCapillary.setEnabled(b);
		btnAddVeinule.setEnabled(b);
		btnAddVein.setEnabled(b);
	}
}
