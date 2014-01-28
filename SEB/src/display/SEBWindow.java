package display;

import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import models.Hemisphere;
import net.miginfocom.swing.MigLayout;
import javax.swing.JToolBar;
import javax.swing.JTabbedPane;

import display.containers.InitialInputsPanel;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import params.WindowManager;

import BaseModel.StandardModel;
import javax.swing.JInternalFrame;
import javax.swing.SwingConstants;

import org.jscroll.JScrollDesktopPane;

import com.display.ArchitectureToolbar;
import com.display.GlobalArchitectureToolbar;
import com.display.MainToolbar;
import com.display.images.IconLibrary;

public class SEBWindow extends JFrame {

	// Menu
	private JMenuBar menuBar;
	private JMenu mnRun;
	private JMenu mnStandardModels;
	private JMenuItem mntmLinninger;
	// Panels
	private MainToolbar toolBar;
	private JPanel mainPanel;
	private JTabbedPane tabbedPane;
	private InitialInputsPanel initialInputPanel;
	private JMenuItem mntmLinningerCustom;
	private ArchitectureToolbar toolBarArchitectLeft;
	private ArchitectureToolbar toolBarArchitectRight;
	private JScrollDesktopPane graphicalModelPanel;
	private GlobalArchitectureToolbar toolBarGlobalArchitect;

	public SEBWindow() {
		IconLibrary.load();
		WindowManager.MAINWINDOW = this;
		mainPanel = new JPanel();
		getContentPane().add(mainPanel, BorderLayout.CENTER);
		mainPanel.setLayout(new MigLayout("", "[748.00,grow][]", "[][grow][][]"));
		
		toolBar = new MainToolbar();
		mainPanel.add(toolBar, "cell 0 0 2 1,grow");
		
		tabbedPane = new JTabbedPane(JTabbedPane.RIGHT);
		mainPanel.add(tabbedPane, "cell 0 1 2 1,grow");
		initialInputPanel = new InitialInputsPanel();
		tabbedPane.addTab("Initial values", initialInputPanel);
		JPanel pan = new JPanel();
		//pan.setDragMode(1);
		// TESTS --------
		graphicalModelPanel = new JScrollDesktopPane();

		pan.setLayout(new MigLayout("", "[grow,fill][fill][]", "[grow,fill][grow]"));
		
		toolBarArchitectLeft = new ArchitectureToolbar(Hemisphere.LEFT);
		toolBarArchitectRight = new ArchitectureToolbar(Hemisphere.RIGHT);
		toolBarGlobalArchitect = new GlobalArchitectureToolbar();
		pan.add(toolBarArchitectLeft, "cell 1 0,alignx right,aligny top");
		pan.add(toolBarArchitectRight, "cell 2 0,alignx right,aligny top");
		pan.add(toolBarGlobalArchitect, "cell 1 1 2 1,alignx right,aligny top");
		pan.add(graphicalModelPanel,"cell 0 0 1 2,alignx right,aligny top");
		tabbedPane.addTab("Architecture", pan);
		// FIN TESTS --------
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		mnRun = new JMenu("Run");
		menuBar.add(mnRun);
		
		mnStandardModels = new JMenu("Standard Models");
		mnRun.add(mnStandardModels);
		
		mntmLinninger = new JMenuItem("Linninger");
		mnStandardModels.add(mntmLinninger);
		
		mntmLinningerCustom = new JMenuItem("Linninger Custom");
		mnStandardModels.add(mntmLinningerCustom);
		
		
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
		mntmLinningerCustom.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Thread modelthread = new Thread(new Runnable() {
					
					@Override
					public void run() {
						StandardModel.run_linninger_with_added_tubes();
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
		this.setExtendedState(JFrame.MAXIMIZED_BOTH); 

	}
	public MainToolbar getToolBar() {
		return toolBar;
	}
	public void setToolBar(MainToolbar toolBar) {
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
	public JScrollDesktopPane getGraphicalModelPanel() {
		return graphicalModelPanel;
	}
}
