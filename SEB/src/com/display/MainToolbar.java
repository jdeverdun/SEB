package com.display;

import javax.swing.ImageIcon;
import javax.swing.JToolBar;
import javax.swing.JButton;
import javax.swing.JPanel;

import params.WindowManager;

import com.display.images.IconLibrary;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainToolbar extends JToolBar {

	private JButton btnNew;
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
		panel.setLayout(new BorderLayout(0, 0));
		btnNew = new JButton();
		Image img = IconLibrary.NEWICON;
		img = img.getScaledInstance(25, 25,  java.awt.Image.SCALE_SMOOTH); 
		btnNew.setIcon(new ImageIcon(img));
		panel.add(btnNew, BorderLayout.WEST);
		
		// listeners
		btnNew.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				WindowManager.MAINWINDOW.getGraphicalModelPanel().initNew();
			}
		});
	}

}
