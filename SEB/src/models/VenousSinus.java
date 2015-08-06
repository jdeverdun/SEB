package models;

import java.util.ArrayList;

import params.ModelSpecification;

public class VenousSinus extends ElasticTube {
	public static final String TUBE_NUM = "8";
	public static final Hemisphere DEFAULT_HEMI = Hemisphere.BOTH;
	public static final float DEFAULT_LENGTH = 15.0f;
	public static final float DEFAULT_AREA = 2.0f * 0.43f;
	public static final float DEFAULT_ELASTANCE = 120000.0f/1333.2240f;// en Pa
	public static final float DEFAULT_ALPHA = 0.161896f;
	public static final float DEFAULT_FLOWIN = 13.0f;
	public static final float DEFAULT_FLOWOUT = 13.0f;
	public static final float DEFAULT_PRESSURE = 3999.67f/1333.2240f;

	public VenousSinus(String name) {
		super(name, DEFAULT_HEMI, DEFAULT_LENGTH,DEFAULT_AREA,DEFAULT_ALPHA,DEFAULT_ELASTANCE, DEFAULT_FLOWIN, DEFAULT_FLOWOUT, DEFAULT_PRESSURE);
	}

	public VenousSinus(String name, float len, float a) {
		super(name, DEFAULT_HEMI, len, a, DEFAULT_ALPHA, DEFAULT_ELASTANCE, DEFAULT_FLOWIN, DEFAULT_FLOWOUT, DEFAULT_PRESSURE);
	}

	public VenousSinus(String name, float len, float a, float alpha, float elast, float fin, float fout, float press) {
		super(name, DEFAULT_HEMI, len, a, alpha, elast, fin, fout, press);
	}


	public VenousSinus(String name, float len, float a, float alpha, float elast, float fin, float fout, float press, ArrayList<ElasticTube> par,
			ArrayList<ElasticTube> child) {
		super(name, DEFAULT_HEMI, len, a, alpha, elast, fin, fout, press, par, child);
	}

	public String toString(){
		return "VenousSinus : "+super.toString();
	}

	@Override
	public String getTubeNum() {
		return TUBE_NUM;
	}
	
	/*@Override
	public ArrayList<SimpleVariable> getVariables(){
		ArrayList<SimpleVariable> variables = new ArrayList<SimpleVariable>();
		
		
		variables.add(getPressure());
		variables.add(getArea());
		variables.add(getFlowin());
		if(getChildren().size()<=0)
			variables.add(getP_OUT());
		else
			variables.add(getFlowout());
		return variables;
	}*/
	// --------------- UPDATE ALPHA ------------
	// UPDATE ALPHA en fonction du nombre de tube pour les modeles complexes
	public void updateAlpha(ArrayList<SimpleVariable> variables){
	}

	// ------------------- EQUATIONS -------------
	/**
	 * Renvoi les equations en format symbolic (en string)
	 * @param variables
	 * @return
	 * @throws Exception
	 */
	public ArrayList<String> getSymbolicInitialEquations(ArrayList<SimpleVariable> variables) throws Exception {
		ArrayList<String> res = new ArrayList<String>();

		// Continuity
		SimpleVariable ar = findVariableWithName(getArea().getName(),variables);
		SimpleVariable fi = findVariableWithName(getFlowin().getName(),variables);
		SimpleVariable fo = null;
		if(getChildren().size()>0)
			fo = findVariableWithName(getFlowout().getName(),variables);
		
		ArrayList<SimpleVariable> psas = findVariableWithStruct(Hemisphere.BOTH, SAS.TUBE_NUM, PRESSURE_LABEL, variables);
		ArrayList<SimpleVariable> psinous = findVariableWithStruct(Hemisphere.BOTH, VenousSinus.TUBE_NUM, PRESSURE_LABEL, variables);
		
		SimpleVariable pr = findVariableWithName(getPressure().getName(),variables);
		res.add(getSymbolicInitialContinuityEquation(fi, fo,psas.get(0),pr,psinous.size()));
		// Distensibility
		
		SimpleVariable pbrain_left = findVariableWithName(ModelSpecification.architecture.getBrain().getLeftHemi().getPressure().getName(),variables);
		SimpleVariable pbrain_right = findVariableWithName(ModelSpecification.architecture.getBrain().getRightHemi().getPressure().getName(),variables);
		res.add(getSymbolicInitialDistensibilityEquation(ar, pr, pbrain_left, pbrain_right));

		// momentum
		res.add(getSymbolicInitialMomentumEquation(getParents(),variables));
		if(!getChildren().isEmpty())
			res.add(getSymbolicInitialMomentumEquationOut(getChildren(),variables));
		/*for(ElasticTube parent:getParents()){
			SimpleVariable parentPressure = findVariableWithName(((Vein)parent).getPressure().getName(),variables);
			SimpleVariable parentFlowout = findVariableWithName(((Vein)parent).getFlowout().getName(),variables);
			res.add(getSymbolicInitialMomentumEquation(parentFlowout, parentPressure, pr));
		}*/

		// connectivity
		
		ArrayList<SimpleVariable> parentFlowout = new ArrayList<SimpleVariable>();
		for(ElasticTube parent:getParents()){
			parentFlowout.add(findVariableWithName(parent.getFlowout().getName(),variables));
		}
		//res.add(getSymbolicInitialConnectivityEquation(parentFlowout, psas.get(0), pr, fi));

		// additonal momentum
		if(getChildren().isEmpty())
			res.add(getSymbolicInitialAddMomentumEquation(pr,fo));
				
		return res;
	}
	
	/**
	 * Renvoi les equations en format symbolic (en string)
	 * @param variables
	 * @return
	 * @throws Exception
	 */
	public ArrayList<String> getSymbolicEquations(ArrayList<SimpleVariable> variables) throws Exception {
		ArrayList<String> res = new ArrayList<String>();

		// Continuity
		SimpleVariable ar = findVariableWithName(getArea().getName(),variables);
		SimpleVariable fi = findVariableWithName(getFlowin().getName(),variables);
		SimpleVariable fo = null;
		if(getChildren().size()>0)
			fo = findVariableWithName(getFlowout().getName(),variables);
		ArrayList<SimpleVariable> psas = findVariableWithStruct(Hemisphere.BOTH, SAS.TUBE_NUM, PRESSURE_LABEL, variables);
		ArrayList<SimpleVariable> psinous = findVariableWithStruct(Hemisphere.BOTH, VenousSinus.TUBE_NUM, PRESSURE_LABEL, variables);
		SimpleVariable pr = findVariableWithName(getPressure().getName(),variables);
		res.add(getSymbolicContinuityEquation(ar, fi, fo, psas.get(0),pr,psinous.size()));
		// Distensibility
		
		SimpleVariable pbrain_left = findVariableWithName(ModelSpecification.architecture.getBrain().getLeftHemi().getPressure().getName(),variables);
		SimpleVariable pbrain_right = findVariableWithName(ModelSpecification.architecture.getBrain().getRightHemi().getPressure().getName(),variables);
		res.add(getSymbolicDistensibilityEquation(ar, pr, pbrain_left, pbrain_right));

		// momentum
		res.add(getSymbolicInitialMomentumEquation(getParents(),variables));
		if(!getChildren().isEmpty())
			res.add(getSymbolicInitialMomentumEquationOut(getChildren(),variables));
		/*for(ElasticTube parent:getParents()){
			SimpleVariable parentPressure = findVariableWithName(((Vein)parent).getPressure().getName(),variables);
			SimpleVariable parentFlowout = findVariableWithName(((Vein)parent).getFlowout().getName(),variables);
			SimpleVariable parentArea = findVariableWithName(((Vein)parent).getArea().getName(),variables);
			SimpleVariable parentFlowout_current = ((Vein)parent).getFlowout();
			SimpleVariable parentArea_current = ((Vein)parent).getArea();
			res.add(getSymbolicMomentumEquation(parentFlowout, parentArea, parentFlowout_current, parentArea_current, parentPressure, pr));
		}*/

		// connectivity
		
		ArrayList<SimpleVariable> parentFlowout = new ArrayList<SimpleVariable>();
		for(ElasticTube parent:getParents()){
			parentFlowout.add(findVariableWithName(parent.getFlowout().getName(),variables));
		}
		//res.add(getSymbolicConnectivityEquation(parentFlowout, psas.get(0), pr, fi));

		// additonal momentum que si fils est vide hein ? 
		if(getChildren().isEmpty())
			res.add(getSymbolicAddMomentumEquation(pr,fo));
				
		return res;
	}


	// symbolic equation (en chaine de caractere)
	private String getSymbolicContinuityEquation(SimpleVariable ar, SimpleVariable fi, SimpleVariable fo,SimpleVariable sasPressure,SimpleVariable pr, int nsinous){
		// equ(14)
		String pressureName = "";
		String alfaName = "";
		if(getChildren().size()>0){
			return "" + "("+ar.getName()+" - "+getArea().getName()+LAST_ROUND_SUFFIX+")/"+ModelSpecification.dt.getName()+""+" + (- "+fi.getName()+" + "+ fo.getName()+")/"+getLength().getName();
		}else{
			pressureName = getP_OUT().getName();
			alfaName = ModelSpecification.TPout_alfa.getName();
			return "" + "("+ar.getName()+" - "+getArea().getName()+LAST_ROUND_SUFFIX+")/"+ModelSpecification.dt.getName()+""+" + (- "+fi.getName()+" - ("+ModelSpecification.k1.getName()+")"+" * ("+sasPressure.getName()+" - "+pr.getName()+") + "+ ModelSpecification.F_OUT.getName()+"("+ModelSpecification.currentIter.getName()+")"+")/"+getLength().getName();
		}

		//return "" + "("+ar.getName()+" - "+getArea().getName()+LAST_ROUND_SUFFIX+")/"+ModelSpecification.dt.getName()+""+" + (- "+fi.getName()+" - ("+ModelSpecification.k1.getName()+")/"+nsinous+" * ("+sasPressure.getName()+" - (("+pr.getName()+"*"+alfaName+"+"+pressureName+"*"+((VenousSinus)pr.getSourceObj()).getAlpha().getName()+")/("+alfaName+"+"+((VenousSinus)pr.getSourceObj()).getAlpha().getName()+"))) + "+ fo.getName()+")/"+getLength().getName();
	}

	private String getSymbolicDistensibilityEquation(SimpleVariable ar, SimpleVariable pr, SimpleVariable pbrain_left, SimpleVariable pbrain_right){
		// equ(29)
		return "-"+ModelSpecification.damp.getName()+" * ("+ar.getName()+" - "+getArea().getName()+LAST_ROUND_SUFFIX+")/"+ModelSpecification.dt.getName()+" + ("+pr.getName()+"- "+0.5f+" * ("+pbrain_left.getName()+" + "+pbrain_right.getName()+"))-"+getElastance().getName()+"*("+ar.getName()+"/"+getInitialArea().getName()+"-1)";
	}

	private String getSymbolicMomentumEquation(SimpleVariable parentFlowout, SimpleVariable parentArea, SimpleVariable parentFlowout_current, SimpleVariable parentArea_current, SimpleVariable parentPressure, SimpleVariable pr){
		// equ(46) et equ(47)
		return ""+ModelSpecification.damp2.getName()+" * (("+parentFlowout.getName()+"/"+parentArea.getName()+") - ("+parentFlowout_current.getName()+LAST_ROUND_SUFFIX+"/"+parentArea_current.getName()+LAST_ROUND_SUFFIX+"))/"+ModelSpecification.dt.getName()+" + ("+parentPressure.getName()+" - "+pr.getName()+")-"+getAlpha().getName()+"*"+parentFlowout.getName();
	}
 
	/*private String getSymbolicConnectivityEquation(ArrayList<SimpleVariable> parentFlowout, SimpleVariable sasPressure, SimpleVariable pr, SimpleVariable fi){
		// equ(54)
		String res = "(";
		for(SimpleVariable pf : parentFlowout){
			if(!res.equals("("))
				res += "+";
			res += pf.getName();
		}
		res += " + "+ModelSpecification.k1.getName()+" * ("+sasPressure.getName()+" - (("+pr.getName()+"*"+ModelSpecification.TPout_alfa.getName()+"+"+ModelSpecification.P_OUT.getName()+"("+ModelSpecification.currentIter.getName()+")*"+((VenousSinus)pr.getSourceObj()).getAlpha().getName()+")/("+ModelSpecification.TPout_alfa.getName()+"+"+((VenousSinus)pr.getSourceObj()).getAlpha().getName()+")))";
		return res+" - ("+fi.getName()+")";
	}*/

	private String getSymbolicAddMomentumEquation(SimpleVariable pr, SimpleVariable fo){
		// equ(72)
		return "("+pr.getName()+" - "+P_OUT.getName()+") - "+ModelSpecification.TPout_alfa.getName()+" * "+ModelSpecification.F_OUT.getName()+"("+ModelSpecification.currentIter.getName()+")";//fo.getName();
	}

	

	// ================= init ========================

	private String getSymbolicInitialContinuityEquation(SimpleVariable fi, SimpleVariable fo,SimpleVariable sasPressure, SimpleVariable pr, int nsinous){
		// eq(14)
		String pressureName = "";
		String alfaName = "";

			
		if(getChildren().size()>0){
			return ""+"  (- "+fi.getName()+" + "+ fo.getName()+")/"+getLength().getName();
		}else{
			pressureName = P_OUT.getName();
			alfaName = ModelSpecification.TPout_alfa.getName();
			return "  (- "+fi.getName()+" - ("+ModelSpecification.k1.getName()+")"+" * ("+sasPressure.getName()+" - "+pr.getName()+") + "+ ModelSpecification.F_OUT_INITIAL.getName()+")";
		}
		
		//return fi.getName()+" + ("+ModelSpecification.k1.getName()+"/"+nsinous+") * ("+sasPressure.getName()+" - (("+pr.getName()+"*"+alfaName+"+"+pressureName+"*"+((VenousSinus)pr.getSourceObj()).getAlpha().getName()+")/("+alfaName+"+"+((VenousSinus)pr.getSourceObj()).getAlpha().getName()+"))) - "+fo.getName();
	}
	private String getSymbolicInitialDistensibilityEquation(SimpleVariable ar, SimpleVariable pr, SimpleVariable pbrain_left, SimpleVariable pbrain_right){
		// equ(29)
		return "("+pr.getName()+"- "+0.5f+" * ("+pbrain_left.getName()+" + "+pbrain_right.getName()+"))-"+getElastance().getName()+"*("+ar.getName()+"/"+getInitialArea().getName()+"-1)";
	}
	private String getSymbolicInitialMomentumEquation(SimpleVariable parentFlowout, SimpleVariable parentPressure, SimpleVariable pr){
		// equ(46) et equ(47)
		return "("+parentPressure.getName()+" - "+pr.getName()+")-"+getAlpha().getName()+"*"+parentFlowout.getName();
	}

	/*private String getSymbolicInitialConnectivityEquation(ArrayList<SimpleVariable> parentFlowout, SimpleVariable sasPressure, SimpleVariable pr, SimpleVariable fi){
		// equ(54)
		String res = "(";
		for(SimpleVariable pf : parentFlowout){
			if(!res.equals("("))
				res += "+";
			res += pf.getName();
		}
		res += " + "+ModelSpecification.k1.getName()+" * ("+sasPressure.getName()+" - (("+pr.getName()+"*"+ModelSpecification.TPout_alfa.getName()+"+"+ModelSpecification.P_OUT_INITIAL.getName()+"*"+((VenousSinus)pr.getSourceObj()).getAlpha().getName()+")/("+ModelSpecification.TPout_alfa.getName()+"+"+((VenousSinus)pr.getSourceObj()).getAlpha().getName()+")))";
		//res += " + "+ModelSpecification.k1.getName()+" * ("+sasPressure.getName()+" - "+pr.getName()+"))";
		return res+" - ("+fi.getName()+")";
	}
*/
	private String getSymbolicInitialAddMomentumEquation(SimpleVariable pr, SimpleVariable fo){
		// equ(72)
		return "("+pr.getName()+" - "+P_OUT.getName()+") - "+ModelSpecification.TPout_alfa.getName()+" * "+ModelSpecification.F_OUT_INITIAL.getName();
	}
}
