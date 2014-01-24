package com.display;

import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.JButton;

import models.Arteriole;
import models.Artery;
import models.Hemisphere;

import com.display.TubePanel.TubeClass;

import params.WindowManager;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ArchitectureToolbar extends JToolBar {

	private JButton btnAddArtery;
	private JButton btnAddArteriole;
	
	public ArchitectureToolbar() {
		init();
	}

	public ArchitectureToolbar(int arg0) {
		super(arg0);
		init();
	}

	public ArchitectureToolbar(String arg0) {
		super(arg0);
		init();
	}

	public ArchitectureToolbar(String arg0, int arg1) {
		super(arg0, arg1);
		init();
	}
	
	/**
	 * Initialisation de la structure
	 */
	private void init(){
		this.setOrientation(SwingConstants.VERTICAL);
		
		btnAddArtery = new JButton("Artery");
		btnAddArtery.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		add(btnAddArtery);
		
		btnAddArteriole = new JButton("Arteriole");
		add(btnAddArteriole);
		
		
		btnAddArtery.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// ============================ ATTENTION LA C EST LEFT HEMI !!!!===========
				Artery art = new Artery("Artery", Hemisphere.LEFT);
				WindowManager.MAINWINDOW.getGraphicalModelPanel().add(art.getName(), new TubePanel(art, TubeClass.Artery), false);
			}
		});
		btnAddArteriole.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// ============================ ATTENTION LA C EST LEFT HEMI !!!!===========
				Arteriole art = new Arteriole("Arteriole", Hemisphere.LEFT);
				WindowManager.MAINWINDOW.getGraphicalModelPanel().add(art.getName(), new TubePanel(art, TubeClass.Arteriole), false);
			}
		});
	}

}
