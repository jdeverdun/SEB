package Thread;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import matlab.MatlabBuilder;
import matlab.MatlabModel;
import models.Architecture;
import models.Brain;
import models.BrainParenchyma;
import models.ElasticTube;
import models.FirstArtery;
import models.FourthVentricle;
import models.Hemisphere;
import models.SAS;
import models.SimpleVariable;
import models.SpinalCord;
import models.ThirdVentricle;
import models.Tube;
import models.Variable;
import models.VenousSinus;
import models.Ventricle;

import org.jscroll.JScrollDesktopPane;
import org.jscroll.widgets.JScrollInternalFrame;

import params.ModelSpecification;
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
	private ArrayList<String> equations;
	private ArrayList<String> initEquations;
	
	public ModelRun() {
		jsd = WindowManager.MAINWINDOW.getGraphicalModelPanel();
		tubes = new ArrayList<Tube>();
		variables = new ArrayList<SimpleVariable>();
		fixedvariables = new ArrayList<SimpleVariable>();
		globalvariables = new ArrayList<Variable>();
		equations = new ArrayList<String>();
		initEquations = new ArrayList<String>();
		
	}
	public void run() {
		if(jsd.getFirstArteryFrame()==null || jsd.getVenousSinousFrame() == null){
			SystemParams.errordlg("Missing first artery or venous sinous or both !");
			return;
		}
		FirstArtery firstArtery = (FirstArtery) jsd.getFirstArteryFrame().getTubePanel().getTube();
		VenousSinus vsinous = (VenousSinus) jsd.getVenousSinousFrame().getTubePanel().getTube();
		if(firstArtery == null || vsinous == null){
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
		checkStructure();
		
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
			for(Tube tube : tubes){
				initEquations.addAll(tube.getSymbolicInitialEquations(variables));
				equations.addAll(tube.getSymbolicEquations(variables));
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
	
	
	private void addAllTubesToList() {
		recursivelyAddTubeToList(architecture.getStartPoint());
		// le LCR
		recursivelyAddTubeToList(jsd.getVentricleleftFrame().getTubePanel().getTube());
		recursivelyAddTubeToList(jsd.getVentriclerightFrame().getTubePanel().getTube());
		/*// ------------ Definition des blocs LCR -----------
		
		// ventricle
		Ventricle ventricleleft = new Ventricle("", Hemisphere.LEFT);
		Ventricle ventricleright = new Ventricle("", Hemisphere.RIGHT);
		// 3V
		ThirdVentricle thirdVent = new ThirdVentricle("");
		// 4V
		FourthVentricle fourthVent = new FourthVentricle("");
		// SAS
		SAS sas = new SAS("");
		// sp. cord
		SpinalCord spinal = new SpinalCord("");
		
		// ---------------- Liaisons des blocs CSF -------
		// 3V
		thirdVent.addParent(ventricleleft);
		thirdVent.addParent(ventricleright);
		// 4V
		fourthVent.addParent(thirdVent);
		// SAS
		sas.addParent(fourthVent);
		// sp. cord
		spinal.addParent(sas);
		
		// on rajoute les tubes 
		tubes.add(ventricleleft);
		tubes.add(ventricleright);
		tubes.add(thirdVent);
		tubes.add(fourthVent);
		tubes.add(sas);
		tubes.add(spinal);*/
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
		if(jsd.getFirstArteryFrame().getLineLinks().isEmpty() || jsd.getVenousSinousFrame().getLineLinks().isEmpty()){
			SystemParams.errordlg("First artery and/or vSinous are linked to nothing !");
			return false;
		}
		//  on verifie que chaque bloc est au moins une fois un fils et un parent et qu'on a bien 2 hemi!
		boolean isChild = false;
		boolean isParent = false;
		boolean atLeastOneLeftHemi = false;
		boolean atLeastOneRightHemi = false;
		for(JScrollInternalFrame jsf : jsd.getInternalFrames()){
			if(jsf != jsd.getFirstArteryFrame() && jsf != jsd.getVenousSinousFrame()){
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
		if(!architecture.checkArchitectureValidity(architecture.getStartPoint())){
			SystemParams.errordlg("Incorrect architecture ...");
			return false;
		}
		return true;
		// ================= Fin du check ==================
	}
}
