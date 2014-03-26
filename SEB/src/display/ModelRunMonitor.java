package display;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import models.SimpleVariable;
import net.miginfocom.swing.MigLayout;
import javax.swing.JTextArea;
import javax.swing.JButton;

import utils.SEButils;
import javax.swing.JProgressBar;

import params.SystemParams;
import params.WindowManager;

public class ModelRunMonitor extends JFrame {

	private static final int logMaxLines = 1000;
	private static final String matlabLogFile = "seb_matlab_session.log";
	
	
	private JTextArea textAreaLog;
	private JScrollPane msgScroller;
	
	// informations sur le process
	private Runtime runtime = Runtime.getRuntime();
	private Process process;
	private ServerSocket serverFloat;
	private ServerSocket serverObject;
	
	// autres
	private ArrayList<SimpleVariable> variables;
	
	
	// threads de montioring process
	private Thread stdThread;
	private Thread statusThread; // communication socket
	private Thread statusThreadObject;
	private Thread runningThread;
	private boolean isRunning = false;
	private JProgressBar progressBar;
	private JButton btnCancel;
	
	public ModelRunMonitor(File basescript, ArrayList<SimpleVariable> variables) {
		getContentPane().setLayout(new MigLayout("", "[grow][]", "[][][grow]"));
		this.variables = variables;
		textAreaLog = new JTextArea();
		textAreaLog.setEditable(false);
		msgScroller = new JScrollPane();        
		msgScroller.setBorder(
		    BorderFactory.createTitledBorder("Logs"));
		msgScroller.setViewportView(textAreaLog);
		
		btnCancel = new JButton("Cancel");
		getContentPane().add(btnCancel, "cell 0 0 2 1,grow");
		
		progressBar = new JProgressBar(0,100);
		getContentPane().add(progressBar, "cell 0 1 2 1,grow");
		getContentPane().add(msgScroller, "cell 0 2 2 1,grow");
		
		// display options
		setSize(400, 400);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setVisible(true);
		// commande
		final String[] command = new String[]{"C:\\Program Files\\MATLAB\\R2012a\\bin\\matlab.exe","-nodesktop","-minimize","-nosplash","-nodisplay", 
				"-logfile", matlabLogFile,"-wait","-r","run('"+basescript.toString()+"')"};
		
		// on lance le process
		runningThread = new Thread(){
			public void run() {
				try {
					isRunning = true;
					process = new ProcessBuilder(command).start();
					// create thread
					createMonitoringThreads();
					process.waitFor();
					if(!isRunning)
						return;
					dispose();
				} catch (IOException e1) {
					isRunning = false;
					e1.printStackTrace();
				} catch (InterruptedException e) {
					isRunning = false;
					e.printStackTrace();
				}
			}
		};
		runningThread.start();
		
		
		
		// Event
		btnCancel.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(isRunning){
					System.out.println("extinction");
					isRunning = false;
					process.destroy();
					dispose();
				}
			}
		});
	}
	
	
	private void createMonitoringThreads() {
		// Consommation de la sortie standard de l'application externe dans un Thread separe
		stdThread = new Thread() {
			public void run() {
				while(isRunning){
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					BufferedReader br;
					try {
						br = new BufferedReader(new FileReader(matlabLogFile));
					    try {
					        StringBuilder sb = new StringBuilder();
					        String line = br.readLine();
	
					        while (line != null) {
					            sb.append(line);
					            sb.append(System.lineSeparator());
					            line = br.readLine();
					        }
					        String everything = sb.toString();
					        textAreaLog.setText(everything);
					        SwingUtilities.invokeLater(new Runnable() {
								
								@Override
								public void run() {
									msgScroller.getVerticalScrollBar().setValue(msgScroller.getVerticalScrollBar().getMaximum()); 
								}
							});
					         
					    } catch (IOException e) {
							e.printStackTrace();
						} finally {
					        try {
								br.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
					    }
					} catch (FileNotFoundException e1) {
						e1.printStackTrace();
					}
					
				}
			}
		};
		
		stdThread.start();
		
		statusThread = new Thread(){
			public void run(){
				DataInputStream in = null;
				Socket s = null;
				try {
					if(serverFloat!=null)
						serverFloat.close();
					serverFloat = new ServerSocket(SystemParams.SOCKET_FLOAT_PORT);
					s = serverFloat.accept();
					in = new DataInputStream(s.getInputStream());
					while(isRunning){
						if(in.available()>0){
							final float progress = in.readFloat();
							SwingUtilities.invokeLater(new Runnable() {
								
								@Override
								public void run() {
									progressBar.setValue((int) (progress*100));
								}
							});
						}else{
							Thread.sleep(200);
						}
					}
					
				} catch (Exception e) {
					isRunning = false;
					e.printStackTrace();
				}finally {
					try {
						in.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					try {
						s.close();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					try {
						serverFloat.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		};
		statusThread.start();
		
		statusThreadObject = new Thread(){
			public void run(){
				ObjectInputStream in = null;
				Socket s = null;
				try {
					if(serverObject != null)
						serverObject.close();
					serverObject = new ServerSocket(SystemParams.SOCKET_OBJECT_PORT);
					s = serverObject.accept();
					in = new ObjectInputStream(s.getInputStream());
					for(SimpleVariable var:variables){
						ArrayList<Double> list = (ArrayList<Double>) in.readObject();
						var.setVariableInTime(list);
					}
					WindowManager.MAINWINDOW.getGraphicalModelPanel().updateInternalFrame();
						/*}else{
							Thread.sleep(200);
						}*/
					//}
				} catch (Exception e) {
					isRunning = false;
					e.printStackTrace();
				}finally {
					try {
						in.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					try {
						s.close();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					try {
						serverObject.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		};
		statusThreadObject.start();
	}
	
	public static void main(String[] args){
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				/*ModelRunMonitor m = new ModelRunMonitor(new File(""));
				m.setVisible(true);*/
			}
		});
	}

}
