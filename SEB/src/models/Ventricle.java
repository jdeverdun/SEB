package models;

import java.util.ArrayList;

import params.ModelSpecification;

public class Ventricle extends ElasticTube {
	public static final String TUBE_NUM = "4";
	public static final float DEFAULT_LENGTH = 0.75f;
	public static final float DEFAULT_AREA = 12.0f;
	public static final float DEFAULT_ALPHA = 1.0f * 1333.2240f;
	public static final float DEFAULT_ELASTANCE = 10.0f * 1333.2240f;
	public static final float DEFAULT_FLOWIN = 0.003f;
	public static final float DEFAULT_FLOWOUT = 0.003f;
	public static final float DEFAULT_PRESSURE = 13332.24f;

	public Ventricle(String name, Hemisphere hemi) {
		super(name, hemi, DEFAULT_LENGTH,DEFAULT_AREA,DEFAULT_ALPHA,DEFAULT_ELASTANCE, DEFAULT_FLOWIN, DEFAULT_FLOWOUT, DEFAULT_PRESSURE);
	}

	public Ventricle(String name, Hemisphere hemi, float len, float a) {
		super(name, hemi, len, a, DEFAULT_ALPHA, DEFAULT_ELASTANCE, DEFAULT_FLOWIN, DEFAULT_FLOWOUT, DEFAULT_PRESSURE);
	}

	public Ventricle(String name, Hemisphere hemi, float len, float a, float alpha, float elast, float fin, float fout, float press) {
		super(name, hemi, len, a, alpha, elast, fin, fout, press);
	}


	public Ventricle(String name, Hemisphere hemi, float len, float a, float alpha, float elast, float fin, float fout, float press, ArrayList<ElasticTube> par,
			ArrayList<ElasticTube> child) {
		super(name, hemi, len, a, alpha, elast, fin, fout, press, par, child);
	}

	public String toString(){
		return "Ventricle : "+super.toString();
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
		SimpleVariable Sbr_lv = findVariableWithName(getAssociatedBrainParenchyma().getSbr_lv().getName(),variables);
		SimpleVariable Sconst_br_lv = getAssociatedBrainParenchyma().getSconst_br_lv();
		ArrayList<SimpleVariable> Sal_lv = findVariableWithStruct(getHemisphere(), Arteriole.TUBE_NUM, Arteriole.SAl_V_LABEL, variables);
		res.add(getSymbolicInitialContinuityEquation(fi, fo));
		// Distensibility
		SimpleVariable pr = findVariableWithName(getPressure().getName(),variables);
		SimpleVariable pbrain = findVariableWithName(getAssociatedBrainParenchyma().getPressure().getName(),variables);
		res.add(getSymbolicInitialDistensibilityEquation(ar, pr, pbrain));

		// momentum
		res.add(getSymbolicInitialMomentumEquation(fi,Sbr_lv,Sconst_br_lv,Sal_lv));
		
		
		if(!getChildren().isEmpty())
			res.add(getSymbolicInitialMomentumEquationOut(getChildren(),variables));
		
		return res;
	}
	
	// --------------- UPDATE ALPHA ------------
	// UPDATE ALPHA en fonction du nombre de tube pour les modeles complexes
	public void updateAlpha(ArrayList<SimpleVariable> variables){
		// pas pour LCS
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
		SimpleVariable Sbr_lv = findVariableWithName(getAssociatedBrainParenchyma().getSbr_lv().getName(),variables);
		SimpleVariable Sconst_br_lv = getAssociatedBrainParenchyma().getSconst_br_lv();
		ArrayList<SimpleVariable> Sal_lv = findVariableWithStruct(getHemisphere(), Arteriole.TUBE_NUM, Arteriole.SAl_V_LABEL, variables);
		res.add(getSymbolicContinuityEquation(ar, fi, fo));
		// Distensibility
		SimpleVariable pr = findVariableWithName(getPressure().getName(),variables);
		SimpleVariable pbrain = findVariableWithName(getAssociatedBrainParenchyma().getPressure().getName(),variables);
		res.add(getSymbolicDistensibilityEquation(ar, pr, pbrain));

		// momentum
		res.add(getSymbolicMomentumEquation(fi,Sbr_lv,Sconst_br_lv,Sal_lv));
		
		
		if(!getChildren().isEmpty())
			res.add(getSymbolicInitialMomentumEquationOut(getChildren(),variables));
		
		return res;
	}


	// symbolic equation (en chaine de caractere)
	private String getSymbolicContinuityEquation(SimpleVariable ar, SimpleVariable fi, SimpleVariable fo){
		// equ(5) et equ(10)
		return "" + "("+ar.getName()+" - "+getArea().getName()+LAST_ROUND_SUFFIX+")/"+ModelSpecification.dt.getName()+""+" + (- "+fi.getName()+"+"+ fo.getName()+")/"+getLength().getName();
	}

	private String getSymbolicDistensibilityEquation(SimpleVariable ar, SimpleVariable pr, SimpleVariable pbrain){
		// equ(20) et equ(25)
		return " -"+ModelSpecification.damp.getName()+" * ("+ar.getName()+" - "+getArea().getName()+LAST_ROUND_SUFFIX+" )/"+ModelSpecification.dt.getName()+" + ("+pr.getName()+"-"+pbrain.getName()+" )-"+getElastance().getName()+" * ("+ar.getName()+" / "+getInitialArea().getName()+" -1)";
	}

	private String getSymbolicMomentumEquation(SimpleVariable fi, SimpleVariable sbr_lv, SimpleVariable sconst_br_lv, ArrayList<SimpleVariable> sal_lv){
		// equ(35) et equ(40)
		return getSymbolicInitialMomentumEquation(fi,sbr_lv,sconst_br_lv,sal_lv);
	}

	
	
	// ================= init ========================

	private String getSymbolicInitialContinuityEquation(SimpleVariable fi, SimpleVariable fo){
		// eq (5)  (10)
		return fi.getName()+" - "+fo.getName();//+" - "+Sal_lv.getName()+" - "+Sbr_lv.getName+" - "+;
	}
	private String getSymbolicInitialDistensibilityEquation(SimpleVariable ar, SimpleVariable pr, SimpleVariable pbrain){
		// eq (20)  (25)
		return "("+pr.getName()+" - "+pbrain.getName()+") - "+getElastance().getName()+" * ("+ar.getName()+"/"+getInitialArea().getName()+" - 1)";
	}
	private String getSymbolicInitialMomentumEquation(SimpleVariable fi, SimpleVariable sbr_lv, SimpleVariable sconst_br_lv, ArrayList<SimpleVariable> sal_lv){
		// equ(35) et equ(40)
		String arteriols = "";
		for(SimpleVariable si:sal_lv){
			arteriols += " - "+si.getName();
			break;//a voir
		}
		return fi.getName()+" - "+sbr_lv.getName()+" - "+sconst_br_lv.getName()+arteriols;
		//return "("+fi.getName()+" - ("+0.003f+"))";
	}
}
