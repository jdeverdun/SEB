package params;

import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.substance.api.skin.SubstanceGraphiteLookAndFeel;

import display.SEBWindow;
import models.Architecture;
import models.ArrayVariable;
import models.SimpleVariable;
import models.Variable;


/**
 * Parametres du modele
 * @author DEVERDUN Jeremy
 *
 */
public class ModelSpecification {

	public enum SimulationMode{DEBUG,STANDARD};
	
	public static SimpleVariable Pstar = new SimpleVariable("Pstar",(0.0f + 0.0f) * 1333.2240f,null);
	public static SimpleVariable k1 = new SimpleVariable("k1",(float) 8.0e-7,null);
	public static SimpleVariable TPout_alfa = new SimpleVariable("TPout_alfa",0.0809088f * 1333.2240f,null);
	public static SimpleVariable dt = new SimpleVariable("dt",0.025f,null);//defaut 0.025
	public static SimpleVariable damp = new SimpleVariable("damp",1.0f,null);// valeur initiale 1.0f
	public static SimpleVariable damp2 = new SimpleVariable("damp2",0.0f,null);
	public static SimpleVariable time_step = new SimpleVariable("time_step",20,null);// valeur initiale 120
	public static SimpleVariable currentIter = new SimpleVariable("currentIter");
	public static SimpleVariable P_INIT_INITIAL = new SimpleVariable("P_INIT_INITIAL",(0.0f + 102.0f) * 1333.2240f,null);
	public static SimpleVariable P_OUT_INITIAL = new SimpleVariable("P_OUT_INITIAL",(0.0f + 2.5f) * 1333.2240f,null);
	public static Architecture architecture;
	public static String P_INIT_NAME = "P_INIT";
	public static String P_OUT_NAME = "P_OUT";
	public static String OUT_D_NAME = "OUT_D";
	private static String TIME_NAME = "time";
	private static String FOURRIER_FUNCT_NAME = "fourrier_funct";
	public static ArrayVariable P_INIT = null;
	public static ArrayVariable P_OUT = null;
	public static ArrayVariable OUT_D = null;
	public static ArrayVariable fourrier_funct = null;
	public static ArrayVariable time = null;
	
	public static SimulationMode SIM_MODE = SimulationMode.STANDARD;

	
	
	public static ArrayList<Variable> getGlobalVariables(){
		ArrayList<Variable> vars = new ArrayList<Variable>();
		vars.add(Pstar);
		vars.add(k1);
		vars.add(TPout_alfa);
		vars.add(dt);
		vars.add(damp);
		vars.add(damp2);
		vars.add(time_step);
		vars.add(currentIter);
		vars.add(P_INIT_INITIAL);
		vars.add(P_OUT_INITIAL);
		vars.add(P_INIT);
		vars.add(P_OUT);
		vars.add(OUT_D);
		return vars;
	}
	
	/**
	 * On prepare la simulation en calculant les P en entrée à tous les points, temps etc
	 */
	public static void init(Architecture arch){
		architecture = arch;
		currentIter.setValue(0);
		time = new ArrayVariable(TIME_NAME ,new float[(int) time_step.getFloatValue()+1],null);
		P_INIT = new ArrayVariable(P_INIT_NAME, new float[(int) time_step.getFloatValue()], null);
		P_OUT = new ArrayVariable(P_OUT_NAME,new float[(int) time_step.getFloatValue()], null);
		OUT_D = new ArrayVariable(OUT_D_NAME,new float[(int) time_step.getFloatValue()], null);
		fourrier_funct = new ArrayVariable(FOURRIER_FUNCT_NAME,new float[(int) time_step.getFloatValue()], null);
		time.getValue()[0] = 0;
		for(int i = 0; i<(int) time_step.getFloatValue();i++){
			time.getValue()[i+1] = (i+1) * dt.getFloatValue();
			fourrier_funct.getValue()[i] = (float) (  0.6 + 0.6*(0.70588472173953 - 0.05900572216651*Math.cos(2*Math.PI*time.getValue()[i]) 
                     - 0.0872254163115668*Math.cos(4*Math.PI*time.getValue()[i]) - 0.0456805350837203*Math.cos(6*Math.PI*time.getValue()[i]) 
                     - 0.0190254766060677*Math.cos(8*Math.PI*time.getValue()[i]) - 0.00227790528223953*Math.cos(10*Math.PI*time.getValue()[i]) 
                     + 0.00863945490784959*Math.cos(12*Math.PI*time.getValue()[i]) + 0.00476870523854349*Math.cos(14*Math.PI*time.getValue()[i])
                     + 0.010425693139133*Math.cos(16*Math.PI*time.getValue()[i]) + 0.172216755025139*Math.sin(2*Math.PI*time.getValue()[i])
                     + 0.0485990604271839*Math.sin(4*Math.PI*time.getValue()[i]) - 0.0273802235346523*Math.sin(6*Math.PI*time.getValue()[i])
                     - 0.0120187782765389*Math.sin(8*Math.PI*time.getValue()[i]) - 0.0298121524913813*Math.sin(10*Math.PI*time.getValue()[i])
                     - 0.00705400623825701*Math.sin(12*Math.PI*time.getValue()[i])- 0.00711904276150898*Math.sin(14*Math.PI*time.getValue()[i])
                     + 0.000860786521170459*Math.sin(16*Math.PI*time.getValue()[i]) ) );
			P_INIT.getValue()[i] = 102.0f * 1333.2240f * fourrier_funct.getValue()[i];
			P_OUT.getValue()[i] =  2.5f * 1333.2240f;
			OUT_D.getValue()[i] = 0.0f;
		}
		// on update l'affichage
		WindowManager.MAINWINDOW.getInitialInputPanel().refreshPlot();
	}

}
