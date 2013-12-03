package params;

import models.Architecture;


/**
 * Parametres du modele
 * @author DEVERDUN Jeremy
 *
 */
public class ModelSpecification {

	public static float Pstar = (0.0f + 0.0f) * 1333.2240f;
	public static float k1 = (float) 8.0e-7;
	public static float TPout_alfa = 0.0809088f * 1333.2240f;
	public static Architecture architecture;
	public static float[] P_INIT = null;
	public static float[] P_OUT = null;
	public static float[] OUT_D = null;
	public static float dt = 0.025f;
	public static float damp = 1.0f;
	public static float damp2 = 0.0f;
	public static int time_step = 120;
	public static float[] fourier_funct = null;
	public static float[] time = null;
	public static int currentIter = -1;
	
	/**
	 * On prepare la simulation en calculant les P en entrée à tous les points, temps etc
	 */
	public static void prepare(){
		time = new float[time_step];
		P_INIT = new float[time_step];
		P_OUT = new float[time_step];
		OUT_D = new float[time_step];
		fourier_funct = new float[time_step];
		for(int i = 0; i<time_step;i++){
			time[i] = i * dt;
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
	}
	public ModelSpecification() {
		// TODO Auto-generated constructor stub
	}

}
