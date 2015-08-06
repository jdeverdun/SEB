package com.display;

import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JToolBar;
import javax.swing.JButton;
import javax.swing.JPanel;

import params.ModelSpecification;
import params.SystemParams;
import params.WindowManager;

import Thread.ModelRun;

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
	private AbstractButton btnNewRaw;
	
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
		btnNewRaw = new JButton("RAW");
		btnNewRaw.setHorizontalAlignment(SwingConstants.LEFT);
		GridBagConstraints gbc_btnNew = new GridBagConstraints();
		gbc_btnNew.anchor = GridBagConstraints.NORTHWEST;
		gbc_btnNew.insets = new Insets(0, 0, 0, 5);
		gbc_btnNew.gridx = 0;
		gbc_btnNew.gridy = 1;
		panel.add(btnNew, gbc_btnNew);
		GridBagConstraints gbc_btnNewRaw = new GridBagConstraints();
		gbc_btnNewRaw.fill = GridBagConstraints.BOTH;
		gbc_btnNewRaw.insets = new Insets(0, 0, 0, 5);
		gbc_btnNewRaw.gridx = 1;
		gbc_btnNewRaw.gridy = 1;
		panel.add(btnNewRaw, gbc_btnNewRaw);
		img = IconLibrary.RUNICON;
		img = img.getScaledInstance(25, 25,  java.awt.Image.SCALE_SMOOTH);
		btnRun = new JButton();
		btnRun.setHorizontalAlignment(SwingConstants.LEFT);
		btnRun.setIcon(new ImageIcon(img));
		GridBagConstraints gbc_btnRun = new GridBagConstraints();
		gbc_btnRun.insets = new Insets(0, 0, 0, 5);
		gbc_btnRun.anchor = GridBagConstraints.NORTHWEST;
		gbc_btnRun.gridx = 2;
		gbc_btnRun.gridy = 1;
		panel.add(btnRun, gbc_btnRun);
		// listeners
		btnNew.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(WindowManager.MAINWINDOW != null)
					WindowManager.MAINWINDOW.getGraphicalModelPanel().initNew(true);
			}
		});
		btnNewRaw.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(WindowManager.MAINWINDOW != null)
					WindowManager.MAINWINDOW.getGraphicalModelPanel().removeAllFrame();
			}
		});
		btnRun.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				ModelRun modelrun = new ModelRun();
				modelrun.start();
			}


		});
	}
	
	
}
