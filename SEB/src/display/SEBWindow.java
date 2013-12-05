package display;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import net.miginfocom.swing.MigLayout;
import javax.swing.JToolBar;
import javax.swing.JTabbedPane;

import display.containers.InitialInputsPanel;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import params.WindowManager;

import BaseModel.StandardModel;

public class SEBWindow extends JFrame {

	// Menu
	private JMenuBar menuBar;
	private JMenu mnRun;
	private JMenu mnStandardModels;
	private JMenuItem mntmLinninger;
	// Panels
	private JToolBar toolBar;
	private JPanel mainPanel;
	private JTabbedPane tabbedPane;
	private InitialInputsPanel initialInputPanel;

	public SEBWindow() {
		WindowManager.MAINWINDOW = this;
		mainPanel = new JPanel();
		getContentPane().add(mainPanel, BorderLayout.CENTER);
		mainPanel.setLayout(new MigLayout("", "[748.00,grow][]", "[][grow][][]"));
		
		toolBar = new JToolBar();
		mainPanel.add(toolBar, "cell 0 0 2 1,grow");
		
		tabbedPane = new JTabbedPane(JTabbedPane.RIGHT);
		mainPanel.add(tabbedPane, "cell 0 1 2 1,grow");
		initialInputPanel = new InitialInputsPanel();
		tabbedPane.addTab("Initial values", initialInputPanel);
		
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		mnRun = new JMenu("Run");
		menuBar.add(mnRun);
		
		mnStandardModels = new JMenu("Standard Models");
		mnRun.add(mnStandardModels);
		
		mntmLinninger = new JMenuItem("Linninger");
		mnStandardModels.add(mntmLinninger);
		
		
		// Menu listenner
		addMenuListeners();
	}
	private void addMenuListeners() {
		mntmLinninger.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Thread modelthread = new Thread(new Runnable() {
					
					@Override
					public void run() {
						StandardModel.run_linninger();
					}
				});
				modelthread.start();
			}
		});
	}
	public void createAndShowGUI() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(800, 700);
		this.setLocationRelativeTo(null);
		this.setVisible(true);

	}
	public JToolBar getToolBar() {
		return toolBar;
	}
	public void setToolBar(JToolBar toolBar) {
		this.toolBar = toolBar;
	}
	public JTabbedPane getTabbedPane() {
		return tabbedPane;
	}
	public void setTabbedPane(JTabbedPane tabbedPane) {
		this.tabbedPane = tabbedPane;
	}
	public InitialInputsPanel getInitialInputPanel() {
		return initialInputPanel;
	}
	public void setInitialInputPanel(InitialInputsPanel initialInputPanel) {
		this.initialInputPanel = initialInputPanel;
	}

}
