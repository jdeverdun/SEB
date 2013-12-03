package BaseModel;

import java.util.ArrayList;

import params.ModelSpecification;
import params.SystemParams;
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
import models.Variable;
import models.Vein;
import models.Veinule;
import models.VenousSinus;
import models.Ventricle;

public class StandardModel {

	public static boolean run(){
		
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
		
		System.out.println("pret");
		
		//////////////////////////////////////////////////////////
		///                                                    ///
		///             Recuperation des variables             ///
		///													   ///
		//////////////////////////////////////////////////////////
		
		// Sanguin
		ArrayList<Variable> variables = firstArtery.getRecursiveVariables();
		for(int i = 0; i<variables.size();i++)
			System.out.println(variables.get(i));
		
		return true;
	}
}
