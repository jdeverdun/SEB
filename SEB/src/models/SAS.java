package models;

import java.util.ArrayList;

import params.ModelSpecification;

public class SAS extends ElasticTube {
	public static final String TUBE_NUM = "7";
	public static final Hemisphere DEFAULT_HEMI = Hemisphere.BOTH;
	public static final float DEFAULT_LENGTH = 1.69f;
	public static final float DEFAULT_AREA = 17.7658f;
	public static final float DEFAULT_ALPHA = 1.0f * 1333.2240f;
	public static final float DEFAULT_ELASTANCE = 80.0f * 1333.2240f;
	public static final float DEFAULT_FLOWIN = 0.06f;
	public static final float DEFAULT_FLOWOUT = 0.06f;
	public static final float DEFAULT_PRESSURE = 13332.24f;

	public SAS(String name) {
		super(name, DEFAULT_HEMI, DEFAULT_LENGTH,DEFAULT_AREA,DEFAULT_ALPHA,DEFAULT_ELASTANCE, DEFAULT_FLOWIN, DEFAULT_FLOWOUT, DEFAULT_PRESSURE);
	}

	public SAS(String name, float len, float a) {
		super(name, DEFAULT_HEMI, len, a, DEFAULT_ALPHA, DEFAULT_ELASTANCE, DEFAULT_FLOWIN, DEFAULT_FLOWOUT, DEFAULT_PRESSURE);
	}

	public SAS(String name, float len, float a, float alpha, float elast, float fin, float fout, float press) {
		super(name, DEFAULT_HEMI, len, a, alpha, elast, fin, fout, press);
	}


	public SAS(String name, float len, float a, float alpha, float elast, float fin, float fout, float press, ArrayList<ElasticTube> par,
			ArrayList<ElasticTube> child) {
		super(name, DEFAULT_HEMI, len, a, alpha, elast, fin, fout, press, par, child);
	}

	public String toString(){
		return "SAS : "+super.toString();
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
		SimpleVariable sasPressure = findVariableWithName(getPressure().getName(),variables);
		ArrayList<SimpleVariable> vSinousPress = findVariableWithStruct(Hemisphere.BOTH, VenousSinus.TUBE_NUM, ElasticTube.PRESSURE_LABEL, variables);
		
		res.add(getSymbolicInitialContinuityEquation(fi, fo, sasPressure, vSinousPress));
		// Distensibility
		SimpleVariable pr = findVariableWithName(getPressure().getName(),variables);
		SimpleVariable pbrain_left = findVariableWithName(ModelSpecification.architecture.getBrain().getLeftHemi().getPressure().getName(),variables);
		SimpleVariable pbrain_right = findVariableWithName(ModelSpecification.architecture.getBrain().getRightHemi().getPressure().getName(),variables);
		res.add(getSymbolicInitialDistensibilityEquation(ar, pr, pbrain_left, pbrain_right));

		// momentum
		res.add(getSymbolicInitialMomentumEquation(getParents(),variables));
		if(!getChildren().isEmpty())
			res.add(getSymbolicInitialMomentumEquationOut(getChildren(),variables));
		/*for(ElasticTube parent:getParents()){
			SimpleVariable parentPressure = findVariableWithName(((FourthVentricle)parent).getPressure().getName(),variables);
			res.add(getSymbolicInitialMomentumEquation(fi, parentPressure, pr));
		}*/
		
		// connectivity
		ArrayList<SimpleVariable> parentFlowout = new ArrayList<SimpleVariable>();
		for(ElasticTube parent:getParents()){
			parentFlowout.add(findVariableWithName(((FourthVentricle)parent).getFlowout().getName(),variables));
		}
		//res.add(getSymbolicInitialConnectivityEquation(parentFlowout,fi));
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
		SimpleVariable sasPressure = findVariableWithName(getPressure().getName(),variables);
		ArrayList<SimpleVariable> vSinousPress = findVariableWithStruct(Hemisphere.BOTH, VenousSinus.TUBE_NUM, ElasticTube.PRESSURE_LABEL, variables);
		res.add(getSymbolicContinuityEquation(ar, fi, fo, sasPressure, vSinousPress));
		// Distensibility
		SimpleVariable pr = findVariableWithName(getPressure().getName(),variables);
		SimpleVariable pbrain_left = findVariableWithName(ModelSpecification.architecture.getBrain().getLeftHemi().getPressure().getName(),variables);
		SimpleVariable pbrain_right = findVariableWithName(ModelSpecification.architecture.getBrain().getRightHemi().getPressure().getName(),variables);
		res.add(getSymbolicDistensibilityEquation(ar, pr, pbrain_left, pbrain_right));

		// momentum
		res.add(getSymbolicInitialMomentumEquation(getParents(),variables));
		if(!getChildren().isEmpty())
			res.add(getSymbolicInitialMomentumEquationOut(getChildren(),variables));
		/*for(ElasticTube parent:getParents()){
			SimpleVariable parentPressure = findVariableWithName(((FourthVentricle)parent).getPressure().getName(),variables);
			res.add(getSymbolicMomentumEquation(fi, ar, pr, parentPressure));
		}*/
		
		// connectivity
		ArrayList<SimpleVariable> parentFlowout = new ArrayList<SimpleVariable>();
		for(ElasticTube parent:getParents()){
			parentFlowout.add(findVariableWithName(((FourthVentricle)parent).getFlowout().getName(),variables));
		}
		//res.add(getSymbolicConnectivityEquation(parentFlowout,fi));
		return res;
	}


	// symbolic equation (en chaine de caractere)
	private String getSymbolicContinuityEquation(SimpleVariable ar, SimpleVariable fi, SimpleVariable fo, SimpleVariable sasPressure, ArrayList<SimpleVariable> vsinousPress){
		// equ(13)
		String out = "" + "("+ar.getName()+" - "+getArea().getName()+LAST_ROUND_SUFFIX+")/"+ModelSpecification.dt.getName()+""+" + (- "+fi.getName()+"+"+ fo.getName();
		for(SimpleVariable prs:vsinousPress)
			out += " - ("+ModelSpecification.k1.getName()+"/"+vsinousPress.size()+") * ("+sasPressure.getName()+" - (("+prs.getName()+"*"+ModelSpecification.TPout_alfa.getName()+"+"+ModelSpecification.P_OUT.getName()+"("+ModelSpecification.currentIter.getName()+")*"+((VenousSinus)prs.getSourceObj()).getAlpha().getName()+")/("+ModelSpecification.TPout_alfa.getName()+"+"+((VenousSinus)prs.getSourceObj()).getAlpha().getName()+")))";
		
		out += ")/"+getLength().getName();
		return out;
	}

	private String getSymbolicDistensibilityEquation(SimpleVariable ar, SimpleVariable pr, SimpleVariable pbrain_left, SimpleVariable pbrain_right){
		// equ(28)
		return "-"+ModelSpecification.damp.getName()+" * ("+ar.getName()+" - "+getArea().getName()+LAST_ROUND_SUFFIX+")/"+ModelSpecification.dt.getName()+" + ("+pr.getName()+"- "+0.5f+" * ("+pbrain_left.getName()+" + "+pbrain_right.getName()+"))-"+getElastance().getName()+"*("+ar.getName()+"/"+getInitialArea().getName()+"-1)";
	}

	private String getSymbolicMomentumEquation(SimpleVariable fi, SimpleVariable ar, SimpleVariable pr, SimpleVariable parentPressure){
		// equ(44)
		return " "+ModelSpecification.damp2.getName()+" * (("+fi.getName()+" / "+ar.getName()+" ) - ("+getFlowin().getName()+LAST_ROUND_SUFFIX+" / "+getArea().getName()+LAST_ROUND_SUFFIX+" ))/ dt + ("+parentPressure.getName()+"-"+pr.getName()+" )-"+getAlpha().getName()+" * "+fi.getName();
	}

	private String getSymbolicConnectivityEquation(ArrayList<SimpleVariable> parentFlowout, SimpleVariable fi){
		// equ(57)
		String res = "(";
		for(SimpleVariable pf : parentFlowout){
			if(!res.equals("("))
				res += "+";
			res += pf.getName();
		}
		res += ")";
		return "("+res+" - "+fi.getName()+")";
	}

	
	
	// ================= init ========================

	private String getSymbolicInitialContinuityEquation(SimpleVariable fi, SimpleVariable fo, SimpleVariable sasPressure, ArrayList<SimpleVariable> vsinousPress){
		// eq(13)
		String out = fi.getName()+" - "+fo.getName();
		for(SimpleVariable prs:vsinousPress)
			out += " - ("+ModelSpecification.k1.getName()+")/"+vsinousPress.size()+" * ("+sasPressure.getName()+" - (("+prs.getName()+"*"+ModelSpecification.TPout_alfa.getName()+"+"+ModelSpecification.P_OUT_INITIAL.getName()+"*"+((VenousSinus)prs.getSourceObj()).getAlpha().getName()+")/("+ModelSpecification.TPout_alfa.getName()+"+"+((VenousSinus)prs.getSourceObj()).getAlpha().getName()+")))";
		return out;
	}
	private String getSymbolicInitialDistensibilityEquation(SimpleVariable ar, SimpleVariable pr, SimpleVariable pbrain_left, SimpleVariable pbrain_right){
		// equ(28)
		return "("+pr.getName()+"- "+0.5f+" * ("+pbrain_left.getName()+" + "+pbrain_right.getName()+"))-"+getElastance().getName()+"*("+ar.getName()+"/"+getInitialArea().getName()+"-1)";
	}
	private String getSymbolicInitialMomentumEquation(SimpleVariable fi, SimpleVariable parentPressure, SimpleVariable pr){
		// equ(44)
		return "("+parentPressure.getName()+" - "+pr.getName()+")-"+getAlpha().getName()+"*"+fi.getName();
	}
	private String getSymbolicInitialConnectivityEquation(ArrayList<SimpleVariable> parentFlowout, SimpleVariable fi){
		// equ(57)
		String res = "(";
		for(SimpleVariable pf : parentFlowout){
			if(!res.equals("("))
				res += "+";
			res += pf.getName();
		}
		res += ")";
		return "("+res+" - "+fi.getName()+")";
	}
}
