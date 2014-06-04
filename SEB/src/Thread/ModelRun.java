package Thread;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import javax.swing.SwingUtilities;

import matlab.MatlabBuilder;
import matlab.MatlabModel;
import models.Architecture;
import models.Artery;
import models.Brain;
import models.BrainParenchyma;
import models.ElasticTube;
import models.FirstArtery;
import models.Hemisphere;
import models.SimpleVariable;
import models.Tube;
import models.Variable;
import models.VenousSinus;

import org.jscroll.JScrollDesktopPane;
import org.jscroll.widgets.JScrollInternalFrame;

import params.ModelSpecification;
import params.ModelSpecification.SimulationMode;
import params.SystemParams;
import params.WindowManager;

import com.display.LineLink;

import display.ModelRunMonitor;

public class ModelRun extends Thread {
	private Architecture architecture;
	private JScrollDesktopPane jsd;
	private ArrayList<Tube> tubes;
	private ArrayList<SimpleVariable> variables;
	private ArrayList<SimpleVariable> fixedvariables;
	private ArrayList<Variable> globalvariables;
	private ArrayList<Variable> iterationvariables;
	private ArrayList<String> equations;
	private ArrayList<String> initEquations;

	public ModelRun() {
		jsd = WindowManager.MAINWINDOW.getGraphicalModelPanel();
		tubes = new ArrayList<Tube>();
		variables = new ArrayList<SimpleVariable>();
		fixedvariables = new ArrayList<SimpleVariable>();
		globalvariables = new ArrayList<Variable>();
		iterationvariables = new ArrayList<Variable>();
		equations = new ArrayList<String>();
		initEquations = new ArrayList<String>();

	}
	public void run() {
		if(ModelSpecification.SIM_MODE == SimulationMode.DEBUG){
			for( JScrollInternalFrame jsf : jsd.getInternalFrames())
				if(!(jsf.getTubePanel().getTube() instanceof FirstArtery) && !(jsf.getTubePanel().getTube() instanceof Artery)){
					SystemParams.errordlg("Does not contains only arteries!!!!!");
					return;
				}
			BrainParenchyma left_brain = new BrainParenchyma("left_brain", Hemisphere.LEFT);
			BrainParenchyma right_brain = new BrainParenchyma("right_brain", Hemisphere.RIGHT);
			Brain brain = new Brain(left_brain, right_brain);	
			ArrayList<FirstArtery> firstArtery = new ArrayList<FirstArtery>();
			for(JScrollInternalFrame ljsf : jsd.getFirstArteryFrame())
				firstArtery.add((FirstArtery) ljsf.getTubePanel().getTube());
			architecture = new Architecture(firstArtery, null, brain);
			// on initialise le systeme (pression entree - sortie)
			ModelSpecification.init(architecture);
			tubes.add(left_brain);
			tubes.add(right_brain);
			addAllTubesToList();
			
			//////////////////////////////////////////////////////////
			///                                                    ///
			///             Recuperation des variables             ///
			///													   ///
			//////////////////////////////////////////////////////////

			for(Tube tube : tubes){
				variables.addAll(tube.getVariables());
				fixedvariables.addAll(tube.getFixedVariables());
			}
			

			//////////////////////////////////////////////////////////
			///                                                    ///
			///             Recuperation des variables             ///
			///					    Globales					   ///
			//////////////////////////////////////////////////////////

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
			try {
				ArrayList<SimpleVariable> fullvariablelist = new ArrayList<SimpleVariable>();
				fullvariablelist.addAll(variables);
				fullvariablelist.addAll(fixedvariables);
				for(Tube tube : tubes){
					initEquations.addAll(tube.getSymbolicInitialEquations(fullvariablelist));
					equations.addAll(tube.getSymbolicEquations(fullvariablelist));
				}
				for(String bloceq : equations){

					System.out.print(bloceq+"\n");
				}
			} catch (Exception e) {
				e.printStackTrace();
				SystemParams.errordlg("Error while retrieving equations ["+e.toString()+"]");
				return;
			}


			//////////////////////////////////////////////////////////
			///                                                    ///
			///                   Ecriture du modele               ///
			///													   ///
			//////////////////////////////////////////////////////////


			Path modelDir = Paths.get(SystemParams.MATLAB_MODEL_DIR);
			//Matrix m = EquationSolver.root(ModelSpecification.architecture, variables);
			final MatlabModel mmodel = MatlabBuilder.buildModel(modelDir,globalvariables, fixedvariables, variables, initEquations, equations);
			SwingUtilities.invokeLater(new Runnable() {

				@Override
				public void run() {
					ModelRunMonitor m = new ModelRunMonitor(mmodel.getMainScript().toFile(),variables);
				}
			});
		}else{
			if(jsd.getFirstArteryFrame()==null || jsd.getVenousSinousFrame() == null){
				SystemParams.errordlg("Missing first artery or venous sinous or both !");
				return;
			}
			ArrayList<FirstArtery> firstArtery = new ArrayList<FirstArtery>();
			for(JScrollInternalFrame ljsf : jsd.getFirstArteryFrame())
				firstArtery.add((FirstArtery) ljsf.getTubePanel().getTube());
			VenousSinus vsinous = (VenousSinus) jsd.getVenousSinousFrame().getTubePanel().getTube();
			if(firstArtery.isEmpty() || vsinous == null){
				SystemParams.errordlg("Missing first artery or venous senous");
				return;
			}

			// on instancie le cerveau brain
			BrainParenchyma left_brain = new BrainParenchyma("left_brain", Hemisphere.LEFT);
			BrainParenchyma right_brain = new BrainParenchyma("right_brain", Hemisphere.RIGHT);
			Brain brain = new Brain(left_brain, right_brain);

			architecture = new Architecture(firstArtery, vsinous, brain);
			// on initialise le systeme (pression entree - sortie)
			ModelSpecification.init(architecture);
			// on verifie la structure
			if(!checkStructure())
				return;

			// on stock tout les tubes dans une liste et 
			// ajout des blocs CSF
			tubes.add(left_brain);
			tubes.add(right_brain);
			addAllTubesToList();

			//////////////////////////////////////////////////////////
			///                                                    ///
			///             Recuperation des variables             ///
			///													   ///
			//////////////////////////////////////////////////////////

			for(Tube tube : tubes){
				variables.addAll(tube.getVariables());
				fixedvariables.addAll(tube.getFixedVariables());
			}
			/*for(int i = 0; i<variables.size();i++)
				System.out.println(variables.get(i));

			System.out.println("------------- FIXED ----------");

			for(int i = 0; i<fixedvariables.size();i++)
				System.out.println(fixedvariables.get(i));*/

			//////////////////////////////////////////////////////////
			///                                                    ///
			///             Recuperation des variables             ///
			///					    Globales					   ///
			//////////////////////////////////////////////////////////

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
			try {
				ArrayList<SimpleVariable> fullvariablelist = new ArrayList<SimpleVariable>();
				fullvariablelist.addAll(variables);
				fullvariablelist.addAll(fixedvariables);
				for(Tube tube : tubes){
					initEquations.addAll(tube.getSymbolicInitialEquations(fullvariablelist));
					equations.addAll(tube.getSymbolicEquations(fullvariablelist));
				}
				for(String bloceq : equations){

					System.out.print(bloceq+"\n");
				}
			} catch (Exception e) {
				e.printStackTrace();
				SystemParams.errordlg("Error while retrieving equations ["+e.toString()+"]");
				return;
			}


			//////////////////////////////////////////////////////////
			///                                                    ///
			///                   Ecriture du modele               ///
			///													   ///
			//////////////////////////////////////////////////////////


			Path modelDir = Paths.get(SystemParams.MATLAB_MODEL_DIR);
			//Matrix m = EquationSolver.root(ModelSpecification.architecture, variables);
			final MatlabModel mmodel = MatlabBuilder.buildModel(modelDir,globalvariables, fixedvariables, variables, initEquations, equations);
			SwingUtilities.invokeLater(new Runnable() {

				@Override
				public void run() {
					ModelRunMonitor m = new ModelRunMonitor(mmodel.getMainScript().toFile(),variables);
				}
			});

		}
	}


	private boolean containsOnlyArteries() {
		boolean b = true;
		for(JScrollInternalFrame fi : jsd.getFirstArteryFrame()){
			for(ElasticTube et:fi.getTubePanel().getTube().getChildren()){
				if(et instanceof Artery){
					b = recursiveIsArteryOnly((Artery)et);
					if(!b)
						return false;
				}else{
					return false;
				}
			}
		}
		return true;
	}
	private boolean recursiveIsArteryOnly(Artery ar) {
		boolean b = true;
		for(ElasticTube et:ar.getChildren()){
			if(et instanceof Artery){
				b = recursiveIsArteryOnly((Artery)et);
				if(!b)
					return false;
			}else{
				return false;
			}
		}
		return true;
	}
	private void addAllTubesToList() {
		for(ElasticTube tube : architecture.getStartPoints())
			recursivelyAddTubeToList(tube);
		// le LCR
		if(ModelSpecification.SIM_MODE != SimulationMode.DEBUG){
			if(jsd.getVentricleleftFrame()!=null)
				recursivelyAddTubeToList(jsd.getVentricleleftFrame().getTubePanel().getTube());
			if(jsd.getVentriclerightFrame()!=null)
				recursivelyAddTubeToList(jsd.getVentriclerightFrame().getTubePanel().getTube());
		}
	}
	private void recursivelyAddTubeToList(ElasticTube tube) {
		if(!tubes.contains(tube))
			tubes.add(tube);
		for(ElasticTube child : tube.getChildren()){
			recursivelyAddTubeToList(child);
		}
	}
	private boolean checkStructure() {

		// ==========  Check de la structure ==================
		boolean farterror = false;
		for(JScrollInternalFrame jsf : jsd.getFirstArteryFrame())
			if(jsf.getLineLinks().isEmpty())
				farterror = true;
		if( farterror || jsd.getVenousSinousFrame().getLineLinks().isEmpty()){
			SystemParams.errordlg("First arteries and/or vSinous are linked to nothing !");
			return false;
		}
		//  on verifie que chaque bloc est au moins une fois un fils et un parent et qu'on a bien 2 hemi!
		boolean isChild = false;
		boolean isParent = false;
		boolean atLeastOneLeftHemi = false;
		boolean atLeastOneRightHemi = false;
		for(JScrollInternalFrame jsf : jsd.getInternalFrames()){
			boolean jsfequals = false;
			for(JScrollInternalFrame ljsf : jsd.getFirstArteryFrame())
				if(jsf == ljsf)
					jsfequals = true;
			if(!jsfequals && jsf != jsd.getVenousSinousFrame()){
				isChild = false;
				isParent = false;
				if(jsf.getTubePanel().getTube().getHemisphere() == Hemisphere.LEFT)
					atLeastOneLeftHemi = true;
				else
					atLeastOneRightHemi = true;
				for(LineLink line : jsf.getLineLinks()){
					if(line.getChild() == jsf){
						isChild = true;
					}
					if(line.getParent() == jsf){
						isParent = true;
					}
					if(isParent && isChild)
						break;
				}
				if(!isChild || !isParent){
					SystemParams.errordlg("Error with architecture. Please ensure that everything is linked ["+jsf+"]");
					return false;
				}
			}
		}
		if(!atLeastOneLeftHemi || !atLeastOneRightHemi){
			SystemParams.errordlg("Do you know that a brain has 2 hemispheres ? :)");
			return false;
		}

		// check la validite des liens
		boolean hasIntegrityError = false;
		for(ElasticTube eltube : architecture.getStartPoints())
			if(!architecture.checkArchitectureValidity(eltube)){
				hasIntegrityError = true;
			}
		if(hasIntegrityError){
			SystemParams.errordlg("Incorrect architecture ...");
			return false;
		}
		return true;
		// ================= Fin du check ==================
	}
}
