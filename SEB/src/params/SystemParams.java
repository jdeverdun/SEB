package params;

import java.net.URI;
import java.util.logging.Logger;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 * Des variables statiques contenant type le logger etc
 * @author DEVERDUN Jeremy
 *
 */
public class SystemParams {
	public static final String PROGRAM_NAME = "SEB";
	public static final String VERSION = "0.1.2";
	public static Logger modelLogger = null;
	public static String MATLAB_MODEL_DIR = "C:\\Users\\Analyse\\Documents\\MATLAB\\Jeremy_these\\Modelisation\\linninger\\auto_generation_SEB";
	
	public static void errordlg(String string) {
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
