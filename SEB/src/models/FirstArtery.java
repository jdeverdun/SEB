package models;

import java.util.ArrayList;

import params.ModelSpecification;

public class FirstArtery extends ElasticTube {
	public static final String TUBE_NUM = "I";
	public static final Hemisphere DEFAULT_HEMI = Hemisphere.NONE;
	public static final float DEFAULT_LENGTH = 0.5f;
	public static final float DEFAULT_AREA = 3.42f;
	public static final float DEFAULT_ELASTANCE = 1066579.2f;// en Pa
	public static final float DEFAULT_ALPHA = 0.1618175f * 1333.2240f;
	public static final float DEFAULT_FLOWIN = 12.4f;
	public static final float DEFAULT_FLOWOUT = 12.4f;
	public static final float DEFAULT_PRESSURE = 133322.4f;

	public FirstArtery(String name) {
		super(name, DEFAULT_HEMI, DEFAULT_LENGTH,DEFAULT_AREA,DEFAULT_ALPHA,DEFAULT_ELASTANCE, DEFAULT_FLOWIN, DEFAULT_FLOWOUT, DEFAULT_PRESSURE);
	}

	public FirstArtery(String name, float len, float a) {
		super(name, DEFAULT_HEMI, len, a, DEFAULT_ALPHA, DEFAULT_ELASTANCE, DEFAULT_FLOWIN, DEFAULT_FLOWOUT, DEFAULT_PRESSURE);
	}

	public FirstArtery(String name, float len, float a, float alpha, float elast, float fin, float fout, float press) {
		super(name, DEFAULT_HEMI, len, a, alpha, elast, fin, fout, press);
	}


	public FirstArtery(String name, float len, float a, float alpha, float elast, float fin, float fout, float press, ArrayList<ElasticTube> par,
			ArrayList<ElasticTube> child) {
		super(name, DEFAULT_HEMI, len, a, alpha, elast, fin, fout, press, par, child);
	}

	public String toString(){
		return "FirstArtery : "+super.toString();
	}
	@Override
	public String getTubeNum() {
		return TUBE_NUM;
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
		SimpleVariable fo = findVariableWithName(getFlowout().getName(),variables);
		res.add(getSymbolicInitialContinuityEquation(fi, fo));
		// Distensibility
		SimpleVariable pr = findVariableWithName(getPressure().getName(),variables);
		SimpleVariable pbrain_left = findVariableWithName(ModelSpecification.architecture.getBrain().getLeftHemi().getPressure().getName(),variables);
		SimpleVariable pbrain_right = findVariableWithName(ModelSpecification.architecture.getBrain().getRightHemi().getPressure().getName(),variables);
		res.add(getSymbolicInitialDistensibilityEquation(ar, pr, pbrain_left, pbrain_right));

		// momentum
		res.add(getSymbolicInitialMomentumEquation(pr,fi));

		// Connectivity
		ArrayList<SimpleVariable> childFin = new ArrayList<SimpleVariable>();
		for(ElasticTube child:getChildren()){
			childFin.add(findVariableWithName(((Artery)child).getFlowin().getName(),variables));
		}
		res.add(getSymbolicInitialConnectivityEquation(childFin, fo));

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
		SimpleVariable fo = findVariableWithName(getFlowout().getName(),variables);
		res.add(getSymbolicContinuityEquation(fi, fo));
		// Distensibility
		SimpleVariable pr = findVariableWithName(getPressure().getName(),variables);
		SimpleVariable pbrain_left = findVariableWithName(ModelSpecification.architecture.getBrain().getLeftHemi().getPressure().getName(),variables);
		SimpleVariable pbrain_right = findVariableWithName(ModelSpecification.architecture.getBrain().getRightHemi().getPressure().getName(),variables);
		res.add(getSymbolicDistensibilityEquation(ar, pr, pbrain_left, pbrain_right));

		// momentum
		res.add(getSymbolicMomentumEquation(pr,fi));

		// Connectivity
		ArrayList<SimpleVariable> childFin = new ArrayList<SimpleVariable>();
		for(ElasticTube child:getChildren()){
			childFin.add(findVariableWithName(((Artery)child).getFlowin().getName(),variables));
		}
		res.add(getSymbolicConnectivityEquation(childFin, fo));

		return res;
	}


	// symbolic equation (en chaine de caractere)
	private String getSymbolicContinuityEquation(SimpleVariable fi, SimpleVariable fo){
		// equ(73)
		return "" +fi.getName()+" - "+fo.getName();
	}

	private String getSymbolicDistensibilityEquation(SimpleVariable ar, SimpleVariable pr, SimpleVariable pbrain_left, SimpleVariable pbrain_right){
		// equ(74)
		return "("+pr.getName()+" - "+0.5f+" * ("+pbrain_right.getName()+" + "+pbrain_left.getName()+")) - "+getElastance().getName()+" * ("+ar.getName()+"/"+getInitialArea().getName()+"-"+1.0f+")";
	}

	private String getSymbolicMomentumEquation(SimpleVariable pr, SimpleVariable fi){
		// equ(75)
		return "("+ModelSpecification.P_INIT.getName()+"("+ModelSpecification.currentIter.getName()+")- "+pr.getName()+") - "+getAlpha().getName()+" * "+fi.getName();
	}

	

	//====== Connectivity ====

	private String getSymbolicConnectivityEquation(ArrayList<SimpleVariable> childFin, SimpleVariable fo){
		// equ(76)
		String res = "";
		for(SimpleVariable pf : childFin){
			if(!res.equals(""))
				res += "+";
			res += pf.getName();
		}
		return ""+fo.getName()+" - ("+res+")";
	}

	// symbolic equation (en chaine de caractere)
	private String getSymbolicInitialContinuityEquation(SimpleVariable fi, SimpleVariable fo){
		// equ(73)
		return "" +fi.getName()+" - "+fo.getName();
	}

	private String getSymbolicInitialDistensibilityEquation(SimpleVariable ar, SimpleVariable pr, SimpleVariable pbrain_left, SimpleVariable pbrain_right){
		// equ(74)
		return "("+pr.getName()+" - "+0.5f+" * ("+pbrain_right.getName()+" + "+pbrain_left.getName()+")) - "+getElastance().getName()+" * ("+ar.getName()+"/"+getInitialArea().getName()+"-"+1.0f+")";
	}

	private String getSymbolicInitialMomentumEquation(SimpleVariable pr, SimpleVariable fi){
		// equ(75)
		return "("+ModelSpecification.P_INIT_INITIAL.getName()+" - "+pr.getName()+") - "+getAlpha().getName()+" * "+fi.getName();
	}

	

	//====== Connectivity ====

	private String getSymbolicInitialConnectivityEquation(ArrayList<SimpleVariable> childFin, SimpleVariable fo){
		// equ(76)
		String res = "";
		for(SimpleVariable pf : childFin){
			if(!res.equals(""))
				res += "+";
			res += pf.getName();
		}
		return ""+fo.getName()+" - ("+res+")";
	}
}
