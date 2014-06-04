package models;

import java.util.ArrayList;

import params.ModelSpecification;

public class ThirdVentricle extends ElasticTube {
	public static final String TUBE_NUM = "5";
	public static final Hemisphere DEFAULT_HEMI = Hemisphere.BOTH;
	public static final float DEFAULT_LENGTH = 1.0f;
	public static final float DEFAULT_AREA = 2.5f;
	public static final float DEFAULT_ALPHA = 1.0f * 1333.2240f;
	public static final float DEFAULT_ELASTANCE = 10.0f * 1333.2240f;
	public static final float DEFAULT_FLOWIN = 0.06f;
	public static final float DEFAULT_FLOWOUT = 0.06f;
	public static final float DEFAULT_PRESSURE = 13332.24f;

	public ThirdVentricle(String name) {
		super(name, DEFAULT_HEMI, DEFAULT_LENGTH,DEFAULT_AREA,DEFAULT_ALPHA,DEFAULT_ELASTANCE, DEFAULT_FLOWIN, DEFAULT_FLOWOUT, DEFAULT_PRESSURE);
	}

	public ThirdVentricle(String name, float len, float a) {
		super(name, DEFAULT_HEMI, len, a, DEFAULT_ALPHA, DEFAULT_ELASTANCE, DEFAULT_FLOWIN, DEFAULT_FLOWOUT, DEFAULT_PRESSURE);
	}

	public ThirdVentricle(String name, float len, float a, float alpha, float elast, float fin, float fout, float press) {
		super(name, DEFAULT_HEMI, len, a, alpha, elast, fin, fout, press);
	}


	public ThirdVentricle(String name, float len, float a, float alpha, float elast, float fin, float fout, float press, ArrayList<ElasticTube> par,
			ArrayList<ElasticTube> child) {
		super(name, DEFAULT_HEMI, len, a, alpha, elast, fin, fout, press, par, child);
	}

	public String toString(){
		return "ThirdVentricle : "+super.toString();
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
		res.add(getSymbolicInitialMomentumEquation(getParents(),variables));
		if(!getChildren().isEmpty())
			res.add(getSymbolicInitialMomentumEquationOut(getChildren(),variables));
		/*for(ElasticTube parent:getParents()){
			SimpleVariable parentPressure = findVariableWithName(((Ventricle)parent).getPressure().getName(),variables);
			SimpleVariable parentFlowout = findVariableWithName(((Ventricle)parent).getFlowout().getName(),variables);
			res.add(getSymbolicInitialMomentumEquation(parentFlowout, parentPressure, pr));
		}*/
		
		// Connectivity
		SimpleVariable leftbrain_flowout1 = ModelSpecification.architecture.getBrain().getLeftHemi().getSbr_lv();
		SimpleVariable rightbrain_flowout1 = ModelSpecification.architecture.getBrain().getLeftHemi().getSconst_br_lv();
		SimpleVariable leftbrain_flowout2 = ModelSpecification.architecture.getBrain().getRightHemi().getSbr_lv();
		SimpleVariable rightbrain_flowout2 = ModelSpecification.architecture.getBrain().getRightHemi().getSconst_br_lv();
		ArrayList<SimpleVariable> parentFlowout = new ArrayList<SimpleVariable>();
		for(ElasticTube parent:getParents()){
			parentFlowout.add(findVariableWithName(((Ventricle)parent).getFlowout().getName(),variables));
		}
		//res.add(getSymbolicInitialConnectivityEquation(parentFlowout, leftbrain_flowout1, rightbrain_flowout1, leftbrain_flowout2, rightbrain_flowout2, fi));
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
		res.add(getSymbolicContinuityEquation(ar, fi, fo));
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
			SimpleVariable parentPressure = findVariableWithName(((Ventricle)parent).getPressure().getName(),variables);
			SimpleVariable parentFlowout = findVariableWithName(((Ventricle)parent).getFlowout().getName(),variables);
			SimpleVariable parentArea = findVariableWithName(((Ventricle)parent).getArea().getName(),variables);
			SimpleVariable parentFlowout_current = ((Ventricle)parent).getFlowout();
			SimpleVariable parentArea_current = ((Ventricle)parent).getArea();
			res.add(getSymbolicMomentumEquation(parentFlowout, parentArea, parentFlowout_current, parentArea_current, parentPressure, pr));
		}*/
		
		// Connectivity
		SimpleVariable leftbrain_flowout1 = ModelSpecification.architecture.getBrain().getLeftHemi().getSbr_lv();
		SimpleVariable rightbrain_flowout1 = ModelSpecification.architecture.getBrain().getLeftHemi().getSconst_br_lv();
		SimpleVariable leftbrain_flowout2 = ModelSpecification.architecture.getBrain().getRightHemi().getSbr_lv();
		SimpleVariable rightbrain_flowout2 = ModelSpecification.architecture.getBrain().getRightHemi().getSconst_br_lv();
		ArrayList<SimpleVariable> parentFlowout = new ArrayList<SimpleVariable>();
		for(ElasticTube parent:getParents()){
			parentFlowout.add(findVariableWithName(((Ventricle)parent).getFlowout().getName(),variables));
		}
		//res.add(getSymbolicConnectivityEquation(parentFlowout, leftbrain_flowout1, rightbrain_flowout1, leftbrain_flowout2, rightbrain_flowout2, fi));
		return res;
	}


	// symbolic equation (en chaine de caractere)
	private String getSymbolicContinuityEquation(SimpleVariable ar, SimpleVariable fi, SimpleVariable fo){
		// equ(11)
		return "" + "("+ar.getName()+" - "+getArea().getName()+LAST_ROUND_SUFFIX+")/"+ModelSpecification.dt.getName()+""+" + (- "+fi.getName()+"+"+ fo.getName()+")/"+getLength().getName();
	}

	private String getSymbolicDistensibilityEquation(SimpleVariable ar, SimpleVariable pr, SimpleVariable pbrain_left, SimpleVariable pbrain_right){
		// equ(26)
		return "-"+ModelSpecification.damp.getName()+" * ("+ar.getName()+" - "+getArea().getName()+LAST_ROUND_SUFFIX+")/"+ModelSpecification.dt.getName()+" + ("+pr.getName()+"- "+0.5f+" * ("+pbrain_left.getName()+" + "+pbrain_right.getName()+"))-"+getElastance().getName()+"*("+ar.getName()+"/"+getInitialArea().getName()+"-1)";
	}

	private String getSymbolicMomentumEquation(SimpleVariable parentFlowout, SimpleVariable parentArea, SimpleVariable parentFlowout_current, SimpleVariable parentArea_current, SimpleVariable parentPressure, SimpleVariable pr){
		// equ(41) et equ(42)
		return ""+ModelSpecification.damp2.getName()+" * (("+parentFlowout.getName()+"/"+parentArea.getName()+") - ("+parentFlowout_current.getName()+LAST_ROUND_SUFFIX+"/"+parentArea_current.getName()+LAST_ROUND_SUFFIX+"))/"+ModelSpecification.dt.getName()+" + ("+parentPressure.getName()+" - "+pr.getName()+")-"+getAlpha().getName()+"*"+parentFlowout.getName();
	}

	private String getSymbolicConnectivityEquation(ArrayList<SimpleVariable> parentFlowout, SimpleVariable leftbrain_flowout1, SimpleVariable rightbrain_flowout1, SimpleVariable leftbrain_flowout2, SimpleVariable rightbrain_flowout2, SimpleVariable fi){
		// equ(55)
		String res = "(";
		for(SimpleVariable pf : parentFlowout){
			if(!res.equals("("))
				res += "+";
			res += pf.getName();
		}
		res += "+"+rightbrain_flowout1.getName()+" + "+rightbrain_flowout2.getName()+" + "+leftbrain_flowout1.getName()+" + "+leftbrain_flowout2.getName()+")";
		return res+" - ("+fi.getName()+")";
	}

	
	
	// ================= init ========================

	private String getSymbolicInitialContinuityEquation(SimpleVariable fi, SimpleVariable fo){
		// eq(11)
		return fi.getName()+" - "+fo.getName();
	}
	private String getSymbolicInitialDistensibilityEquation(SimpleVariable ar, SimpleVariable pr, SimpleVariable pbrain_left, SimpleVariable pbrain_right){
		// equ(26)
		return "("+pr.getName()+"- "+0.5f+" * ("+pbrain_left.getName()+" + "+pbrain_right.getName()+"))-"+getElastance().getName()+"*("+ar.getName()+"/"+getInitialArea().getName()+"-1)";
	}
	private String getSymbolicInitialMomentumEquation(SimpleVariable parentFlowout, SimpleVariable parentPressure, SimpleVariable pr){
		// equ(41) et equ(42)
		return "("+parentPressure.getName()+" - "+pr.getName()+")-"+getAlpha().getName()+"*"+parentFlowout.getName();
	}
	private String getSymbolicInitialConnectivityEquation(ArrayList<SimpleVariable> parentFlowout, SimpleVariable leftbrain_flowout1, SimpleVariable rightbrain_flowout1, SimpleVariable leftbrain_flowout2, SimpleVariable rightbrain_flowout2, SimpleVariable fi){
		// equ(55)
		String res = "(";
		for(SimpleVariable pf : parentFlowout){
			if(!res.equals("("))
				res += "+";
			res += pf.getName();
		}
		res += "+"+rightbrain_flowout1.getName()+" + "+rightbrain_flowout2.getName()+" + "+leftbrain_flowout1.getName()+" + "+leftbrain_flowout2.getName()+")";
		return res+" - ("+fi.getName()+")";
	}
}
