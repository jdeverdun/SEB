package matlab;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.xml.crypto.dsig.keyinfo.RetrievalMethod;

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
	public final static Charset ENCODING = StandardCharsets.UTF_8;
	 
	private static final String mainFunctionName = "SEB_model_main";//fichier principal
	private static final String equationsInitialFunctionName = "SEB_model_eqInit";//fichier equation initiales
	private static final String equationsTimeFunctionName = "SEB_model_eqTime";//fichier equation Time
	public static final String unknownLabel = "x";
	public static final String previousUnknownLabel = "x_old";
	public static final String equResultLabel = "equ";
	public static final String NEWLINE_CHAR = "\n";
	public static final String MONITORINGVARS_SUFFIX = "_GRAPH";
	
	public static MatlabModel buildModel(Path folder, ArrayList<Variable> globalvariables,
			ArrayList<SimpleVariable> fixedvariables, ArrayList<SimpleVariable> variables,ArrayList<String> initEquations,ArrayList<String> equations) {
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
		mainFileContent += createSocketDefinition();
		//mainFileContent +=  globalVarsDefinition(globalvariables);
		mainFileContent +=  mainGlobalVarsInitialization(globalvariables);
		mainFileContent +=  createDefaultUnknown(variables);
		mainFileContent += "%% Fixed variables "+NEWLINE_CHAR+"";
		//mainFileContent +=  createVariablesInitialization(fixedvariables);// on en aura peut etre besoin !
		mainFileContent += createInitialFsolve(equationsInitialFunctionName, globalvariables);
		mainFileContent += createRetrieveGlobal(globalvariables,variables); // a partir du x
		mainFileContent += createTimeLoop(globalvariables,variables); // a partir du x
		mainFileContent += createFooter(variables);		
		
		// ============= Initial equations ============
		String initialEqFileContent = "";
		initialEqFileContent += createEquationInitialHeader(globalvariables);
		//initialEqFileContent += globalVarsDefinition(globalvariables);
		initialEqFileContent += createEquations(fixedvariables, variables, initEquations);
		
		
		// ============== Time equations ==============
		String timeEqFileContent = "";
		timeEqFileContent += createEquationTimeHeader(globalvariables);
		//timeEqFileContent += globalVarsDefinition(globalvariables);
		timeEqFileContent += createEquations(fixedvariables, variables, equations);
		
		
		
		// ============== Writing files ===============
		String mainfilepath = folder.toString() + File.separator + mainFunctionName + ".m";
		String eqInitfilepath = folder.toString() + File.separator + equationsInitialFunctionName + ".m";
		String eqTimefilepath = folder.toString() + File.separator + equationsTimeFunctionName + ".m";
		
		MatlabModel model = new MatlabModel(Paths.get(mainfilepath), Paths.get(eqInitfilepath), Paths.get(eqTimefilepath));
		
		List<String> mainContent = Arrays.asList(mainFileContent.split(NEWLINE_CHAR));  // convert string to array list
		List<String> eqInitContent = Arrays.asList(initialEqFileContent.split(NEWLINE_CHAR));  // convert string to array list
		List<String> eqTimeContent = Arrays.asList(timeEqFileContent.split(NEWLINE_CHAR));  // convert string to array list
		try {
			writeLargerTextFile(folder.toString() + File.separator + mainFunctionName + ".m",mainContent);
			writeLargerTextFile(folder.toString() + File.separator + equationsInitialFunctionName + ".m",eqInitContent);
			writeLargerTextFile(folder.toString() + File.separator + equationsTimeFunctionName + ".m",eqTimeContent);
		} catch (IOException e) {
			e.printStackTrace();
			SwingUtilities.invokeLater(new Runnable() {
				
				@Override
				public void run() {
					JDialog.setDefaultLookAndFeelDecorated(true);
					JOptionPane.showMessageDialog(WindowManager.MAINWINDOW,
						    "Can't write into specified file",
						    "Permission denied",
						    JOptionPane.ERROR_MESSAGE);
				}
			});
			return null;
		}
		System.out.println("Model files writed");
		return model;
	}
	
	/**
	 * On quitte matlab
	 * @return
	 */
	private static String createFooter(ArrayList<SimpleVariable> variables) {
		String content = "%% Sending results and leaving matlab"+NEWLINE_CHAR;
		content += createSendResults(variables);
		content += createSocketDestroy();
		content += "exit"+NEWLINE_CHAR;
		return content;
	}

	/**
	 * La boucle sur le temps
	 * @param globalvariables
	 * @param variables
	 * @return
	 */
	private static String createTimeLoop(ArrayList<Variable> globalvariables,
			ArrayList<SimpleVariable> variables) {
		String content = "%% Time loop"+NEWLINE_CHAR+"";
		// on verifie la presence des bonnes variables
		if(!globalvariables.contains(ModelSpecification.currentIter)){
			System.err.println("Error while creating time loop, unable to find "+ModelSpecification.currentIter.getName());
		}
		if(!globalvariables.contains(ModelSpecification.time_step)){
			System.err.println("Error while creating time loop, unable to find "+ModelSpecification.time_step.getName());
		}
		// debut for
		SimpleVariable currentIter = ((SimpleVariable)globalvariables.get(globalvariables.indexOf(ModelSpecification.currentIter)));
		SimpleVariable time_step = ((SimpleVariable)globalvariables.get(globalvariables.indexOf(ModelSpecification.time_step)));
		content += "for "+currentIter.getName()+" = 1 : "+ time_step.getName() + ""+NEWLINE_CHAR+"";
		
		// on rajoute les controles pour interaction via socket
		content += "\t% java interaction using socket"+NEWLINE_CHAR;
		content += "\ttry"+NEWLINE_CHAR;
		content += "\t\toutputstream.writeFloat("+currentIter.getName()+"/"+time_step.getName()+");"+NEWLINE_CHAR;
		content += "\t\toutputstream.flush();"+NEWLINE_CHAR;
		content += "\tcatch exception"+NEWLINE_CHAR;
		content += addPrefixToContent(createSocketDestroy(),"\t\t");
		content += "\t\texit;"+NEWLINE_CHAR;
		content += "\tend;"+NEWLINE_CHAR;

		
		
		content += addPrefixToContent(createTimeFsolve(equationsTimeFunctionName, globalvariables),"\t");
		content += addPrefixToContent(createRetrieveGlobal(globalvariables, variables),"\t");
		content += addPrefixToContent(createMonitoringVars(variables,currentIter.getName()),"\t");
		content += "end" + NEWLINE_CHAR;
		return content;
	}
	
	private static String createSocketDefinition(){
		String content = "% SOCKET definition to interact with java"+NEWLINE_CHAR;
		content += "socket = java.net.Socket('localhost',"+SystemParams.SOCKET_FLOAT_PORT+");"+NEWLINE_CHAR;
		content += "outputstream = java.io.DataOutputStream(socket.getOutputStream());"+NEWLINE_CHAR;
		content += "socketObject = java.net.Socket('localhost',"+SystemParams.SOCKET_OBJECT_PORT+");"+NEWLINE_CHAR;
		content += "outputstreamObject = java.io.ObjectOutputStream(socketObject.getOutputStream());"+NEWLINE_CHAR;
		return content;
	}

	private static String createSocketDestroy(){
		String content = "outputstream.flush();"+NEWLINE_CHAR;
		content += "outputstream.close();"+NEWLINE_CHAR;
		content += "outputstreamObject.close();"+NEWLINE_CHAR;
		content += "socket.close();"+NEWLINE_CHAR;
		content += "socketObject.close();"+NEWLINE_CHAR;
		return content;
	}
	/**
	 * Cree les lignes pour sauvegarder les valeurs des variables au cours du temps
	 * @param variables
	 * @param iterationVarName 
	 * @return
	 */
	private static String createMonitoringVars(
			ArrayList<SimpleVariable> variables, String iterationVarName) {
		String content = "%% Saving variables values accross time"+NEWLINE_CHAR;
		int count = 1;
		for(SimpleVariable var : variables){
			content += var.getName() + MONITORINGVARS_SUFFIX + "(" + iterationVarName + ") = " + unknownLabel + "(" + (count++) + ");" + NEWLINE_CHAR;
		}
		return content;
	}
	
	/**
	 * Cree la commande matlab pour renvoyer les resultats sous forme d'arraylist dans le bon ordre
	 * @param variables
	 * @return
	 */
	private static String createSendResults(
			ArrayList<SimpleVariable> variables) {
		String content = "% Format data as arrayList"+NEWLINE_CHAR;
		for(SimpleVariable var : variables){
			content += NEWLINE_CHAR+"% " + var.getName() + MONITORINGVARS_SUFFIX+NEWLINE_CHAR;
			content += var.getName() + MONITORINGVARS_SUFFIX+"_arr = java.util.ArrayList();"+NEWLINE_CHAR;
			String locvar = var.getName() + MONITORINGVARS_SUFFIX+"_arr";
			content += "for i = 1 : numel("+var.getName() + MONITORINGVARS_SUFFIX+")"+NEWLINE_CHAR;
			content += "\t"+locvar+".add("+var.getName() + MONITORINGVARS_SUFFIX+"(i));"+NEWLINE_CHAR;
			content += "end;"+NEWLINE_CHAR;
			content += "outputstreamObject.writeObject("+locvar+");"+NEWLINE_CHAR;
			content += "outputstreamObject.flush();"+NEWLINE_CHAR;
		}
		return content;
	}

	/**
	 * Ajoute un prefixe a toute les lignes de la chaine de caractere
	 * @param content
	 * @param prefix
	 * @return
	 */
	private static String addPrefixToContent(String content, String prefix) {
		String newcontent = "";
		String[] lines = content.split(NEWLINE_CHAR);
		for(String line : lines){
			newcontent += prefix + line + NEWLINE_CHAR;
		}
		return newcontent;
	}

	/**
	 * On recupere le resultat du fsolve
	 * @return
	 */
	private static String createRetrieveGlobal(ArrayList<Variable> globalvariables, ArrayList<SimpleVariable> variables) {
		String content = "%% retrieving results of fsolve"+NEWLINE_CHAR+"";
		ArrayList<Variable> modelvars = ModelSpecification.getGlobalVariables();
		for(Variable var : globalvariables){
			if(!modelvars.contains(var)){
				if(var instanceof SimpleVariable){
					String nname = var.getName().substring(0, var.getName().length() - Tube.LAST_ROUND_SUFFIX.length());
					int ind = variables.indexOf(new SimpleVariable(nname));
					if(ind == -1)
						System.err.println("Unable to find variable "+nname+" in variables list");
					content += var.getName() + " = " + unknownLabel + "(" + (ind+1) + ");"+NEWLINE_CHAR+"";
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
	private static String createTimeFsolve(String eqfilename, ArrayList<Variable> globalvariables ) {
		/*
		REF = [x,y,z];
		f = @(y) myfun(y,REF); % function of dummy variable y
		[out,fval]=fsolve(f,y0)
		*/
		String content = "%% fsolve resolution of the system"+NEWLINE_CHAR+"";
		content += previousUnknownLabel + " = " + unknownLabel + ";"+NEWLINE_CHAR+"";
		
		content += "func = @(" + unknownLabel + ") " + eqfilename + "(" + unknownLabel;
		for(Variable var : globalvariables){
			content += "," + var.getName(); 
		}
		content += ");" + NEWLINE_CHAR;
		content += unknownLabel + " = fsolve(func," + previousUnknownLabel + ");"+NEWLINE_CHAR+"";
		//content += unknownLabel + " = fsolve(@" + eqfilename + "," + previousUnknownLabel + ");"+NEWLINE_CHAR+"";
		return content;
	}
	
	/**
	 * Commande fsolve
	 * @param eqfilename 
	 * @return
	 */
	private static String createInitialFsolve(String eqfilename, ArrayList<Variable> globalvariables ) {
		/*
		REF = [x,y,z];
		f = @(y) myfun(y,REF); % function of dummy variable y
		[out,fval]=fsolve(f,y0)
		*/
		String content = "%% fsolve resolution of the system"+NEWLINE_CHAR+"";
		content += previousUnknownLabel + " = " + unknownLabel + ";"+NEWLINE_CHAR+"";
		
		content += "func = @(" + unknownLabel + ") " + eqfilename + "(" + unknownLabel;
		ArrayList<Variable> modelvars = ModelSpecification.getGlobalVariables();
		for(Variable var : globalvariables){
			if(modelvars.contains(var)){
				content += "," + var.getName(); 
			}
			
		}
		content += ");" + NEWLINE_CHAR;
		content += unknownLabel + " = fsolve(func," + previousUnknownLabel + ");"+NEWLINE_CHAR+"";
		//content += unknownLabel + " = fsolve(@" + eqfilename + "," + previousUnknownLabel + ");"+NEWLINE_CHAR+"";
		return content;
	}

	/**
	 * Init les damp, dt etc
	 * @param globalvariables
	 * @return
	 */
	private static String mainGlobalVarsInitialization(
			ArrayList<Variable> globalvariables) {
		String content = "%% Some global initialization"+NEWLINE_CHAR+"";
		ArrayList<Variable> modelvars = ModelSpecification.getGlobalVariables();
		for(Variable var : globalvariables){
			if(modelvars.contains(var)){
				if(var instanceof SimpleVariable){
					content += var.getName() + " = " + ((SimpleVariable)var).getValue() + ";"+NEWLINE_CHAR+"";
				}else{
					content += var.getName() + " = [";
					for(float fl : ((ArrayVariable)var).getValue()){
						content += fl + " ";
					}
					content += "];"+NEWLINE_CHAR+"";
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
		String content = "%% Initialize unknown"+NEWLINE_CHAR+"";
		content += unknownLabel + " = [];"+NEWLINE_CHAR+"";
		int count = 1;
		for(SimpleVariable var : variables){
			content += unknownLabel + "(" + (count++) + ") = " + var.getValue() + "; % "+var.getName()+""+NEWLINE_CHAR+"";
		}
		return content;
	}

	/**
	 * Cree le contenu du fichier pour les equation au round 1
	 * @param fixedvariables
	 * @param variables
	 * @param initEquations 
	 * @param initEquations 
	 * @return
	 */
	private static String createEquations(
			ArrayList<SimpleVariable> fixedvariables, ArrayList<SimpleVariable> variables, ArrayList<String> initEquations) {
		String content = "%% Fixed variables "+NEWLINE_CHAR+"";
		// on init les variables x()
		content += ""+NEWLINE_CHAR+""+NEWLINE_CHAR+"%%Variables initialization"+NEWLINE_CHAR+"";
		int count = 1;
		for(SimpleVariable var : variables){
			content += var.getName() + " = " + unknownLabel + "(" + (count++) + ");"+NEWLINE_CHAR+"";  
		}
		// on gere les variables fixes
		content += createVariablesInitialization(fixedvariables);
		
		
		// on init les variables x()
		content += ""+NEWLINE_CHAR+""+NEWLINE_CHAR+"%%Equations"+NEWLINE_CHAR+"";
		count = 1;
		for(String equ : initEquations){
			content += equResultLabel + "(" + (count++) + ") = " + equ + ";"+NEWLINE_CHAR+"";  
		}
		return content;
	}

	private static String createVariablesInitialization(ArrayList<SimpleVariable> variables){
		// les variables
		String content = "";
		for(SimpleVariable var : variables){
			content += var.getName() + " = " + var.getValue() + ";"+NEWLINE_CHAR+"";
		}
		return content;
	}
	/**
	 * Cree l'entete du fichier principal du modele (du script matlab)
	 * @return
	 */
	private static String createModelHeader() {
		String content = "%% Model script"+NEWLINE_CHAR+"%%"+NEWLINE_CHAR+"";
		content += "% This file is the main matlab script for the model"+NEWLINE_CHAR+"%%"+NEWLINE_CHAR+"";
		content += "% You can use it as a script"+NEWLINE_CHAR+"";
		content += "% File generated by "+SystemParams.PROGRAM_NAME+" Version : "+SystemParams.VERSION+""+NEWLINE_CHAR+"";
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		content += "% Date : "+dateFormat.format(date)+""+NEWLINE_CHAR+"";
		content += "function "+mainFunctionName+""+NEWLINE_CHAR+"";
		return content;
	}

	/**
	 * Cree l'entete du fichier qui contient les equation initiales du modele
	 * @return
	 */
	private static String createEquationInitialHeader(ArrayList<Variable> globalvariables) {
		String content = "%% Model function"+NEWLINE_CHAR+"%%"+NEWLINE_CHAR+"";
		content += "% This file contains equations for the round of the fsolve"+NEWLINE_CHAR+"%%"+NEWLINE_CHAR+"";
		content += "% You should only call this file through "+mainFunctionName+""+NEWLINE_CHAR+"";
		content += "% File generated by "+SystemParams.PROGRAM_NAME+" Version : "+SystemParams.VERSION+""+NEWLINE_CHAR+"";
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		content += "% Date : "+dateFormat.format(date)+""+NEWLINE_CHAR+"";
		content += "function "+equResultLabel+" = "+equationsInitialFunctionName+"("+unknownLabel;
		
		ArrayList<Variable> modelvars = ModelSpecification.getGlobalVariables();
		for(Variable var : globalvariables){
			if(modelvars.contains(var)){
				content += "," + var.getName(); 
			}
			
		}
		content += ")"+NEWLINE_CHAR+"";
		return content;
	}

	/**
	 * Cree l'entete du fichier qui contient les equation au cours du temps du modele
	 * @return
	 */
	private static String createEquationTimeHeader(ArrayList<Variable> globalvariables) {
		String content = "%% Model function"+NEWLINE_CHAR+"%%"+NEWLINE_CHAR+"";
		content += "% This file contains equations for the round of the fsolve"+NEWLINE_CHAR+"%%"+NEWLINE_CHAR+"";
		content += "% You should only call this file through "+mainFunctionName+""+NEWLINE_CHAR+"";
		content += "% File generated by "+SystemParams.PROGRAM_NAME+" Version : "+SystemParams.VERSION+""+NEWLINE_CHAR+"";
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		content += "% Date : "+dateFormat.format(date)+""+NEWLINE_CHAR+"";
		content += "function "+equResultLabel+" = "+equationsTimeFunctionName+"("+unknownLabel;
		
		for(Variable var : globalvariables){
			content += "," + var.getName(); 
		}
		content += ")"+NEWLINE_CHAR+"";
		return content;
	}

	/**
	 * Cree le contenu des fichiers m concernant les definitions de variables globales
	 * @param globalvariables
	 * @return
	 */
	public static String globalVarsDefinition(ArrayList<Variable> globalvariables){
		String content = "%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%"+NEWLINE_CHAR+"";
		content += "%%       Declaring global variables     %%"+NEWLINE_CHAR+"";
		content += "%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%"+NEWLINE_CHAR+"";
		for(Variable var:globalvariables){
			content += "global "+var.getName()+";"+NEWLINE_CHAR+"";
		}
		return content;
	}

	public static void writeLargerTextFile(String aFileName, List<String> aLines) throws IOException {
		Path path = Paths.get(aFileName);
		try (BufferedWriter writer = Files.newBufferedWriter(path, ENCODING)){
			for(String line : aLines){
				writer.write(line);
				writer.newLine();
			}
		}
	}
}
