package BaseModel;

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
import models.Vein;
import models.Veinule;
import models.VenousSinus;
import models.Ventricle;

public class StandardModel {

	public StandardModel(){
		
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
		
		
	}
}
