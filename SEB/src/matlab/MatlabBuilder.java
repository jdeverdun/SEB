package matlab;

import java.nio.file.Path;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import params.ModelSpecification;
import params.SystemParams;
import params.WindowManager;

import models.ArrayVariable;
import models.SimpleVariable;
import models.Tube;
import models.Variable;

/**
 * Classe pour construire les fichiers MATLAB avec les equations
 * @author DEVERDUN Jeremy
 *
 */
public class MatlabBuilder {

	private static final String mainFunctionName = "SEB_model_main";//fichier principal
	private static final String equationsInitialFunctionName = "SEB_model_eqInit";//fichier equation initiales
	private static final String equationsTimeFunctionName = "SEB_model_eqTime";//fichier equation Time
	public static final String unknownLabel = "x";
	public static final String previousUnknownLabel = "x_old";
	public static final String equResultLabel = "equ";
	
	public static MatlabModel buildModel(Path folder, ArrayList<Variable> globalvariables,
			ArrayList<SimpleVariable> fixedvariables, ArrayList<SimpleVariable> variables,ArrayList<String[]> initEquations,ArrayList<String[]> equations) {
		if(!folder.toFile().canWrite()){
			SwingUtilities.invokeLater(new Runnable() {
				
				@Override
				public void run() {
					JDialog.setDefaultLookAndFeelDecorated(true);
					JOptionPane.showMessageDialog(WindowManager.MAINWINDOW,
						    "Can't write into specified directory",
						    "Permission denied",
						    JOptionPane.ERROR_MESSAGE);
				}
			});
			return null;
		}
		

		// ================ Main File =================
		String mainFileContent = "";
		mainFileContent += createModelHeader();
		mainFileContent +=  globalVarsDefinition(globalvariables);
		mainFileContent +=  mainGlobalVarsInitialization(globalvariables);
		mainFileContent +=  createDefaultUnknown(variables);
		mainFileContent += "%% Fixed variables \n";
		mainFileContent +=  createVariablesInitialization(fixedvariables);// on en aura peut etre besoin !
		mainFileContent += createFsolve(equationsInitialFunctionName);
		mainFileContent += createRetrieveGlobal(globalvariables,variables); // a partir du x
		mainFileContent += createTimeLoop(globalvariables,variables); // a partir du x
				
		// ============= Initial equations ============
		String initialEqFileContent = "";
		initialEqFileContent += createEquationInitialHeader();
		initialEqFileContent += globalVarsDefinition(globalvariables);
		initialEqFileContent += createEquations(fixedvariables, variables, initEquations);
		
		
		// ============== Time equations ==============
		String timeEqFileContent = "";
		timeEqFileContent += createEquationTimeHeader();
		timeEqFileContent += globalVarsDefinition(globalvariables);
		timeEqFileContent += createEquations(fixedvariables, variables, equations);
		
		
		System.out.println(initialEqFileContent);
		
		System.out.println(mainFileContent);
		return null;
	}
	
	/**
	 * La boucle sur le temps
	 * @param globalvariables
	 * @param variables
	 * @return
	 */
	private static String createTimeLoop(ArrayList<Variable> globalvariables,
			ArrayList<SimpleVariable> variables) {
		String content = "%% Time loop\n";
		// on declare et definit les P_INIT etc
		/*ArrayList<Variable> modelvars = ModelSpecification.getGlobalVariables();
		for(Variable var : globalvariables){
			if(modelvars.contains(var) && var.hasValue()){
				content += var.getName() + " = " + var.getValue() + ";\n";
			}
		}
		content += */
		return content;
	}

	/**
	 * On recupere le resultat du fsolve
	 * @return
	 */
	private static String createRetrieveGlobal(ArrayList<Variable> globalvariables, ArrayList<SimpleVariable> variables) {
		String content = "%% retrieving results of fsolve\n";
		ArrayList<Variable> modelvars = ModelSpecification.getGlobalVariables();
		for(Variable var : globalvariables){
			if(!modelvars.contains(var)){
				if(var instanceof SimpleVariable){
					String nname = var.getName().substring(0, var.getName().length() - Tube.LAST_ROUND_SUFFIX.length());
					int ind = variables.indexOf(new SimpleVariable(nname));
					if(ind == -1)
						System.err.println("Unable to find variable "+nname+" in variables list");
					content += var.getName() + " = " + unknownLabel + "(" + (ind+1) + ");\n";
				}
			}
		}
		return content;
	}

	/**
	 * Commande fsolve
	 * @param eqfilename 
	 * @return
	 */
	private static String createFsolve(String eqfilename) {
		String content = "%% fsolve resolution of the system\n";
		content += previousUnknownLabel + " = " + unknownLabel + ";\n";
		content += unknownLabel + " = fsolve(@" + eqfilename + "," + previousUnknownLabel + ");\n";
		return content;
	}

	/**
	 * Init les damp, dt etc
	 * @param globalvariables
	 * @return
	 */
	private static String mainGlobalVarsInitialization(
			ArrayList<Variable> globalvariables) {
		String content = "%% Some global initialization\n";
		ArrayList<Variable> modelvars = ModelSpecification.getGlobalVariables();
		for(Variable var : globalvariables){
			if(modelvars.contains(var)){
				
				if(var instanceof SimpleVariable){
					content += var.getName() + " = " + ((SimpleVariable)var).getValue() + ";\n";
				}else{
					content += var.getName() + " = [";
					for(float fl : ((ArrayVariable)var).getValue()){
						content += fl + " ";
					}
					content += "];\n";
				}
			}
		}
		return content;
	}

	/**
	 * Initialise les inconnus (les variables x pour le fsolve)
	 * @param variables
	 * @return
	 */
	private static String createDefaultUnknown(ArrayList<SimpleVariable> variables) {
		String content = "%% Initialize unknown\n";
		content += unknownLabel + " = [];\n";
		int count = 1;
		for(SimpleVariable var : variables){
			content += unknownLabel + "(" + (count++) + ") = " + var.getValue() + "; % "+var.getName()+"\n";
		}
		return content;
	}

	/**
	 * Cree le contenu du fichier pour les equation au round 1
	 * @param fixedvariables
	 * @param variables
	 * @param equations 
	 * @param initEquations 
	 * @return
	 */
	private static String createEquations(
			ArrayList<SimpleVariable> fixedvariables, ArrayList<SimpleVariable> variables, ArrayList<String[]> equations) {
		String content = "%% Fixed variables \n";
		// on commence par les variables fixes
		content += createVariablesInitialization(fixedvariables);
		// on init les variables x()
		content += "\n\n%%Variables initialization\n";
		int count = 1;
		for(SimpleVariable var : variables){
			content += var.getName() + " = " + unknownLabel + "(" + (count++) + ");\n";  
		}
		
		// on init les variables x()
		content += "\n\n%%Equations\n";
		count = 1;
		for(String[] equ : equations){
			content += equResultLabel + "(" + (count++) + ") = " + equ[0] + ";\n";  
		}
		return content;
	}

	private static String createVariablesInitialization(ArrayList<SimpleVariable> variables){
		// les variables
		String content = "";
		for(SimpleVariable var : variables){
			content += var.getName() + " = " + var.getValue() + ";\n";
		}
		return content;
	}
	/**
	 * Cree l'entete du fichier principal du modele (du script matlab)
	 * @return
	 */
	private static String createModelHeader() {
		String content = "%% Model script\n%%\n";
		content += "% This file is the main matlab script for the model\n%%\n";
		content += "% You can use it as a script\n";
		content += "% File generated by "+SystemParams.PROGRAM_NAME+" Version : "+SystemParams.VERSION+"\n";
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		content += "% Date : "+dateFormat.format(date)+"\n";
		content += "function "+mainFunctionName+"\n";
		return content;
	}

	/**
	 * Cree l'entete du fichier qui contient les equation initiales du modele
	 * @return
	 */
	private static String createEquationInitialHeader() {
		String content = "%% Model function\n%%\n";
		content += "% This file contains equations for the round of the fsolve\n%%\n";
		content += "% You should only call this file through "+mainFunctionName+"\n";
		content += "% File generated by "+SystemParams.PROGRAM_NAME+" Version : "+SystemParams.VERSION+"\n";
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		content += "% Date : "+dateFormat.format(date)+"\n";
		content += "function "+equResultLabel+" = "+equationsInitialFunctionName+"("+unknownLabel+")\n";
		return content;
	}

	/**
	 * Cree l'entete du fichier qui contient les equation au cours du temps du modele
	 * @return
	 */
	private static String createEquationTimeHeader() {
		String content = "%% Model function\n%%\n";
		content += "% This file contains equations for the round of the fsolve\n%%\n";
		content += "% You should only call this file through "+mainFunctionName+"\n";
		content += "% File generated by "+SystemParams.PROGRAM_NAME+" Version : "+SystemParams.VERSION+"\n";
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		content += "% Date : "+dateFormat.format(date)+"\n";
		content += "function "+equResultLabel+" = "+equationsTimeFunctionName+"("+unknownLabel+")\n";
		return content;
	}

	/**
	 * Cree le contenu des fichiers m concernant les definitions de variables globales
	 * @param globalvariables
	 * @return
	 */
	public static String globalVarsDefinition(ArrayList<Variable> globalvariables){
		String content = "%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n";
		content += "%%       Declaring global variables     %%\n";
		content += "%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n";
		for(Variable var:globalvariables){
			content += "global "+var.getName()+";\n";
		}
		return content;
	}

}
