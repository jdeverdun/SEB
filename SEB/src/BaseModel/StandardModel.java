package BaseModel;

import java.io.ObjectInputStream.GetField;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import Jama.Matrix;

import params.ModelSpecification;
import params.SystemParams;
import params.WindowManager;
import solver.EquationSolver;
import matlab.MatlabBuilder;
import matlab.MatlabModel;
import models.Architecture;
import models.Arteriole;
import models.Artery;
import models.Brain;
import models.BrainParenchyma;
import models.Capillary;
import models.FirstArtery;
import models.FourthVentricle;
import models.Hemisphere;
import models.SAS;
import models.SpinalCord;
import models.ThirdVentricle;
import models.Tube;
import models.Variable;
import models.Vein;
import models.Veinule;
import models.VenousSinus;
import models.Ventricle;

public class StandardModel {

	public static boolean run_linninger(){
		
		//////////////////////////////////////////////////////////
		///                                                    ///
		///             Definition des variables               ///
		///													   ///
		//////////////////////////////////////////////////////////
		
		// ------------ Definition du brain ---------
		BrainParenchyma left_brain = new BrainParenchyma("left_brain", Hemisphere.LEFT);
		BrainParenchyma right_brain = new BrainParenchyma("right_brain", Hemisphere.RIGHT);
		Brain brain = new Brain(left_brain, right_brain);
		
		// ------------ Definition de l'entree ------
		FirstArtery firstArtery = new FirstArtery("first_artery");
		
		// ------------ Definition de la sortie vsinus -----
		VenousSinus vsinous = new VenousSinus("venous_sinus");
		
		// ------------ Definition des blocs sanguins ------
		// artery
		Artery arteryleft = new Artery("left_artery", Hemisphere.LEFT);
		Artery arteryright = new Artery("right_artery", Hemisphere.RIGHT);
		// arteriole
		Arteriole arteriolleft = new Arteriole("left_arteriole", Hemisphere.LEFT);
		Arteriole arteriolright = new Arteriole("right_arteriole", Hemisphere.RIGHT);
		// capillary
		Capillary capleft = new Capillary("left_capillary", Hemisphere.LEFT);
		Capillary capright = new Capillary("right_capillary", Hemisphere.RIGHT);
		// veinule
		Veinule vlleft = new Veinule("left_veinule", Hemisphere.LEFT);
		Veinule vlright = new Veinule("right_veinule", Hemisphere.RIGHT);
		// vein
		Vein vleft = new Vein("left_vein", Hemisphere.LEFT);
		Vein vright = new Vein("right_vein", Hemisphere.RIGHT);
		
		// ------------ Definition des blocs LCR -----------
		// ventricle
		Ventricle ventricleleft = new Ventricle("left_ventricle", Hemisphere.LEFT);
		Ventricle ventricleright = new Ventricle("right_ventricle", Hemisphere.RIGHT);
		// 3V
		ThirdVentricle thirdVent = new ThirdVentricle("third_ventricule");
		// 4V
		FourthVentricle fourthVent = new FourthVentricle("fourth_ventricule");
		// SAS
		SAS sas = new SAS("sas");
		// sp. cord
		SpinalCord spinal = new SpinalCord("spinal_cord");
		
		
		//////////////////////////////////////////////////////////
		///                                                    ///
		///             Liaisons des variables                 ///
		///													   ///
		//////////////////////////////////////////////////////////
		
		// ------------ liaisons des blocs sanguins ------
		// artery
		arteryleft.addParent(firstArtery);
		arteryright.addParent(firstArtery);
		// arteriole
		arteriolleft.addParent(arteryleft);
		arteriolright.addParent(arteryright);
		// capillary
		capleft.addParent(arteriolleft);
		capright.addParent(arteriolright);
		// veinule
		vlleft.addParent(capleft);
		vlright.addParent(capright);
		// vein
		vleft.addParent(vlleft);
		vright.addParent(vlright);
		// vsinous
		vsinous.addParent(vleft);
		vsinous.addParent(vright);
		// ------------ Liaison des blocs LCR -------------
		// 3V
		thirdVent.addParent(ventricleleft);
		thirdVent.addParent(ventricleright);
		// 4V
		fourthVent.addParent(thirdVent);
		// SAS
		sas.addParent(fourthVent);
		// sp. cord
		spinal.addParent(sas);
		
		
		//////////////////////////////////////////////////////////
		///                                                    ///
		///             Definition de l'architecture           ///
		///													   ///
		//////////////////////////////////////////////////////////
		Architecture architecture = new Architecture(firstArtery, vsinous, brain);
		// on initialise le systeme (pression entree - sortie)
		ModelSpecification.init(architecture);
		// on stock la liste des tubes
		ArrayList<Tube> tubes = new ArrayList<Tube>();
		tubes.add(firstArtery);
		tubes.add(arteryleft);
		tubes.add(arteryright);
		tubes.add(arteriolleft);
		tubes.add(arteriolright);
		tubes.add(capleft);
		tubes.add(capright);
		tubes.add(vlleft);
		tubes.add(vlright);
		tubes.add(vleft);
		tubes.add(vright);
		tubes.add(vsinous);
		tubes.add(ventricleleft);
		tubes.add(ventricleright);
		tubes.add(thirdVent);
		tubes.add(fourthVent);
		tubes.add(sas);
		tubes.add(spinal);
		tubes.add(left_brain);
		tubes.add(right_brain);
		architecture.setTubes(tubes);
		System.out.println("pret");
		
		//////////////////////////////////////////////////////////
		///                                                    ///
		///             Recuperation des variables             ///
		///													   ///
		//////////////////////////////////////////////////////////
		
		ArrayList<Variable> variables = new ArrayList<Variable>();
		ArrayList<Variable> fixedvariables = new ArrayList<Variable>();
		for(Tube tube : tubes){
			variables.addAll(tube.getVariables());
			fixedvariables.addAll(tube.getFixedVariables());
		}
		for(int i = 0; i<variables.size();i++)
			System.out.println(variables.get(i));
		
		System.out.println("------------- FIXED ----------");
		
		for(int i = 0; i<fixedvariables.size();i++)
			System.out.println(fixedvariables.get(i));
		
		//////////////////////////////////////////////////////////
		///                                                    ///
		///             Recuperation des variables             ///
		///					    Globales					   ///
		//////////////////////////////////////////////////////////
		
		ArrayList<Variable> globalvariables = new ArrayList<Variable>();
		for(Tube tube : tubes){
			globalvariables.addAll(tube.getGlobalVariables());
		}
		globalvariables.addAll(ModelSpecification.getGlobalVariables());


		
		//////////////////////////////////////////////////////////
		///                                                    ///
		///             Recuperation des equations             ///
		///													   ///
		//////////////////////////////////////////////////////////
		
		System.out.println("============ Equations ===========");
		ArrayList<String[]> equations = new ArrayList<String[]>();
		ArrayList<String[]> initEquations = new ArrayList<String[]>();
		try {
			for(Tube tube : tubes){
				initEquations.addAll(tube.getSymbolicInitialEquations(variables));
				equations.addAll(tube.getSymbolicEquations(variables));
			}
			for(String[] bloceq : equations){
				
				for(String bloc1 : bloceq){
					System.out.print(bloc1+"\t");
				}
				System.out.print("\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
			SwingUtilities.invokeLater(new Runnable() {
				
				@Override
				public void run() {
					JDialog.setDefaultLookAndFeelDecorated(true);
					JOptionPane.showMessageDialog(WindowManager.MAINWINDOW,
						    "Error while retrieving equations",
						    "Initialization error",
						    JOptionPane.ERROR_MESSAGE);
				}
			});
			return false;
		}
		
		
		//////////////////////////////////////////////////////////
		///                                                    ///
		///             Recuperation des valeurs               ///
		///													   ///
		//////////////////////////////////////////////////////////
		
		/*System.out.println("============ Valeurs T0 ===========");
		try {
			ArrayList<float[]> equations = new ArrayList<float[]>();
			for(Tube tube : tubes){
				equations.addAll(tube.getEquations(variables));
			}
			
			for(float[] bloceq : equations){
				
				for(float bloc1 : bloceq){
					System.out.print(bloc1+"\t");
				}
				System.out.print("\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}*/

		
		
		
		//////////////////////////////////////////////////////////
		///                                                    ///
		///                   Resolution                       ///
		///													   ///
		//////////////////////////////////////////////////////////
		
		System.out.println("============ Resolution ===========");
		Path modelDir = Paths.get(".");
		//Matrix m = EquationSolver.root(ModelSpecification.architecture, variables);
		MatlabModel mmodel = MatlabBuilder.buildModel(modelDir,globalvariables, fixedvariables, variables, initEquations, equations);
		return true;
	}
}
