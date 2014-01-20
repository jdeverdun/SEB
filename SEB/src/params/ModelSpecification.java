package params;

import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.substance.api.skin.SubstanceGraphiteLookAndFeel;

import display.SEBWindow;
import models.Architecture;
import models.Variable;


/**
 * Parametres du modele
 * @author DEVERDUN Jeremy
 *
 */
public class ModelSpecification {

	public static Variable Pstar = new Variable("Pstar",(0.0f + 0.0f) * 1333.2240f,null);
	public static Variable k1 = new Variable("k1",(float) 8.0e-7,null);
	public static Variable TPout_alfa = new Variable("TPout_alfa",0.0809088f * 1333.2240f,null);
	public static Variable dt = new Variable("dt",0.025f,null);
	public static Variable damp = new Variable("damp",1.0f,null);
	public static Variable damp2 = new Variable("damp2",0.0f,null);
	public static Variable time_step = new Variable("time_step",120,null);
	public static Variable currentIter = new Variable("currentIter");
	public static Architecture architecture;
	public static String P_INIT_NAME = "P_INIT";
	public static String P_OUT_NAME = "P_OUT";
	public static String OUT_D_NAME = "OUT_D";
	public static float[] P_INIT = null;
	public static float[] P_OUT = null;
	public static float[] OUT_D = null;
	public static float[] fourier_funct = null;
	public static float[] time = null;

	
	
	public static ArrayList<Variable> getGlobalVariables(){
		ArrayList<Variable> vars = new ArrayList<Variable>();
		vars.add(Pstar);
		vars.add(k1);
		vars.add(TPout_alfa);
		vars.add(dt);
		vars.add(damp);
		vars.add(time_step);
		vars.add(currentIter);
		vars.add(new Variable(P_INIT_NAME));
		vars.add(new Variable(P_OUT_NAME));
		vars.add(new Variable(OUT_D_NAME));
		return vars;
	}
	
	/**
	 * On prepare la simulation en calculant les P en entrée à tous les points, temps etc
	 */
	public static void init(Architecture arch){
		architecture = arch;
		currentIter.setValue(0);
		time = new float[(int) time_step.getValue()];
		P_INIT = new float[(int) time_step.getValue()];
		P_OUT = new float[(int) time_step.getValue()];
		OUT_D = new float[(int) time_step.getValue()];
		fourier_funct = new float[(int) time_step.getValue()];
		for(int i = 0; i<(int) time_step.getValue();i++){
			time[i] = i * dt.getValue();
			fourier_funct[i] = (float) (  0.6 + 0.6*(0.70588472173953 - 0.05900572216651*Math.cos(2*Math.PI*time[i]) 
                     - 0.0872254163115668*Math.cos(4*Math.PI*time[i]) - 0.0456805350837203*Math.cos(6*Math.PI*time[i]) 
                     - 0.0190254766060677*Math.cos(8*Math.PI*time[i]) - 0.00227790528223953*Math.cos(10*Math.PI*time[i]) 
                     + 0.00863945490784959*Math.cos(12*Math.PI*time[i]) + 0.00476870523854349*Math.cos(14*Math.PI*time[i])
                     + 0.010425693139133*Math.cos(16*Math.PI*time[i]) + 0.172216755025139*Math.sin(2*Math.PI*time[i])
                     + 0.0485990604271839*Math.sin(4*Math.PI*time[i]) - 0.0273802235346523*Math.sin(6*Math.PI*time[i])
                     - 0.0120187782765389*Math.sin(8*Math.PI*time[i]) - 0.0298121524913813*Math.sin(10*Math.PI*time[i])
                     - 0.00705400623825701*Math.sin(12*Math.PI*time[i])- 0.00711904276150898*Math.sin(14*Math.PI*time[i])
                     + 0.000860786521170459*Math.sin(16*Math.PI*time[i]) ) );
			P_INIT[i] = 102.0f * 1333.2240f * fourier_funct[i];
			P_OUT[i] =  2.5f * 1333.2240f;
			OUT_D[i] = 0.0f;
		}
		// on update l'affichage
		WindowManager.MAINWINDOW.getInitialInputPanel().refreshPlot();
	}

}
