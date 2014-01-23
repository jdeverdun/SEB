package models;

import java.io.ObjectInputStream.GetField;
import java.util.ArrayList;

import params.ModelSpecification;

public class Artery extends ElasticTube {
	public static final String TUBE_NUM = "0";
	public static final float DEFAULT_LENGTH = 4.15f;
	public static final float DEFAULT_AREA = 3.42f;
	public static final float DEFAULT_ELASTANCE = 1066579.2f;// en Pa
	public static final float DEFAULT_ALPHA = 0.7692f * 1333.2240f;
	public static final float DEFAULT_FLOWIN = 13.0f;
	public static final float DEFAULT_FLOWOUT = 13.0f;
	public static final float DEFAULT_PRESSURE = 100.0f * 1333.2240f;
	
	public Artery(String name, Hemisphere hemi) {
		super(name, hemi, DEFAULT_LENGTH,DEFAULT_AREA,DEFAULT_ALPHA,DEFAULT_ELASTANCE, DEFAULT_FLOWIN, DEFAULT_FLOWOUT, DEFAULT_PRESSURE);
	}

	public Artery(String name, Hemisphere hemi, float len, float a) {
		super(name, hemi, len, a, DEFAULT_ALPHA, DEFAULT_ELASTANCE, DEFAULT_FLOWIN, DEFAULT_FLOWOUT, DEFAULT_PRESSURE);
	}
	
	public Artery(String name, Hemisphere hemi, float len, float a, float alpha, float elast, float fin, float fout, float press) {
		super(name, hemi, len, a, alpha, elast, fin, fout, press);
	}
	

	public Artery(String name, Hemisphere hemi, float len, float a, float alpha, float elast, float fin, float fout, float press, ArrayList<ElasticTube> par,
			ArrayList<ElasticTube> child) {
		super(name, hemi, len, a, alpha, elast, fin, fout, press, par, child);
	}
	
	@Override
	public String getTubeNum() {
		return TUBE_NUM;
	}
	
	public String toString(){
		return "Artery : "+super.toString();
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
		SimpleVariable pbrain = findVariableWithName(getAssociatedBrainParenchyma().getPressure().getName(),variables);
		res.add(getSymbolicInitialDistensibilityEquation(ar, pr, pbrain));
		
		// momentum
		res.add(getSymbolicInitialMomentumEquation(fi, pr));
		return res;
	}
	
	/**
	 * Renvoi les equations initiales en format symbolic (en string)
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
		SimpleVariable pbrain = findVariableWithName(getAssociatedBrainParenchyma().getPressure().getName(),variables);
		res.add(getSymbolicDistensibilityEquation(ar, pr, pbrain));
		
		// momentum
		res.add(getSymbolicMomentumEquation(fi, ar, pr));
		return res;
	}

	// symbolic equation (en chaine de caractere)
	private String getSymbolicContinuityEquation(SimpleVariable ar, SimpleVariable fi, SimpleVariable fo){
		// equ(1) et equ(6)
		return "" + "("+ar.getName()+" - "+getArea().getName()+LAST_ROUND_SUFFIX+")/"+ModelSpecification.dt.getName()+""+" + (- "+fi.getName()+"+"+ fo.getName()+")/"+getLength().getName();
	}
	
	private String getSymbolicDistensibilityEquation(SimpleVariable ar, SimpleVariable pr, SimpleVariable pbrain){
		// equ(16) et equ(21)
		return " -"+ModelSpecification.damp.getName()+" * ("+ar.getName()+" - "+getArea().getName()+LAST_ROUND_SUFFIX+" )/"+ModelSpecification.dt.getName()+" + ("+pr.getName()+"-"+pbrain.getName()+" )-"+getElastance().getName()+" * ("+ar.getName()+" / "+getInitialArea().getName()+" -1)";
	}
	
	private String getSymbolicMomentumEquation(SimpleVariable fi, SimpleVariable ar, SimpleVariable pr){
		// equ(31) et equ(36)
		return " "+ModelSpecification.damp2.getName()+" * (("+fi.getName()+" / "+ar.getName()+" ) - ("+getFlowin().getName()+LAST_ROUND_SUFFIX+" / "+getArea().getName()+LAST_ROUND_SUFFIX+" ))/ dt + ("+ModelSpecification.P_INIT.getName()+"("+ModelSpecification.currentIter.getName()+") - "+pr.getName()+" )-"+getAlpha().getName()+" * "+fi.getName();
	}
	

	//====== Connectivity quand parent = artere only ====
	/**
	 * Pour l'equation de connectivite du flux on fait la somme du flux en amont qui doit etre egale au flux in
	 * @param parentFlowout
	 * @param fi
	 * @return
	 */
	private String getSymbolicConnectivityEquation(SimpleVariable fi){
		// equ(48) et equ(51)
		String res = "(";
		for(ElasticTube parent : getParents()){//for(Variable pf : parentFlowout){
			if(parent instanceof Artery){
				if(!res.equals("("))
					res += "+";
				Artery par = ((Artery)parent);
				SimpleVariable pf = par.getFlowout();
				float fact = par.getChildren().size();
				res += "("+pf.getName()+"/"+fact+")";
			}
		}
		res += ")";
		return "("+res+" - "+fi.getName()+")";
	}

	// ================= init ========================
	private String getSymbolicInitialContinuityEquation(SimpleVariable fi, SimpleVariable fo){
		// eq (1)  (6)
		return fi.getName()+" - "+fo.getName();
	}
	
	// distensibility
	private String getSymbolicInitialDistensibilityEquation(SimpleVariable ar, SimpleVariable pr, SimpleVariable pbrain){
		// eq (16)  (21)
		return "("+pr.getName()+" - "+pbrain.getName()+") - "+getElastance().getName()+" * ("+ar.getName()+"/"+getInitialArea().getName()+" - 1)";
	}

	// momentum
	private String getSymbolicInitialMomentumEquation(SimpleVariable fi, SimpleVariable pr){
		// eq (31)  (36)
		return "("+ModelSpecification.P_INIT_INITIAL.getName()+" - "+pr.getName()+")-"+getAlpha().getName()+"*"+fi.getName();
	}

	/**
	 * Pour l'equation de connectivite du flux on fait la somme du flux en amont qui doit etre egale au flux in
	 * @param parentFlowout
	 * @param fi
	 * @return
	 */
	private String getSymbolicInitialConnectivityEquation(SimpleVariable fi){
		// equ(48) et equ(51)
		String res = "(";
		for(ElasticTube parent : getParents()){//for(Variable pf : parentFlowout){
			if(parent instanceof Artery){
				if(!res.equals("("))
					res += "+";
				Artery par = ((Artery)parent);
				SimpleVariable pf = par.getFlowout();
				float fact = par.getChildren().size();
				res += "("+pf.getName()+"/"+fact+")";
			}
		}
		res += ")";
		return "("+res+" - "+fi.getName()+")";
	}
}
