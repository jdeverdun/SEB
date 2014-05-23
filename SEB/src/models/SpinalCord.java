package models;

import java.util.ArrayList;

import params.ModelSpecification;

public class SpinalCord extends ElasticTube {
	public static final String TUBE_NUM = "9";
	public static final Hemisphere DEFAULT_HEMI = Hemisphere.NONE;
	public static final float DEFAULT_LENGTH = 43.0f;
	public static final float DEFAULT_AREA = 2.0f;
	public static final float DEFAULT_ALPHA = 0.1f * 1333.2240f;
	public static final float DEFAULT_ELASTANCE = 400.0f * 1333.2240f;
	public static final float DEFAULT_FLOWIN = 0.0f;
	public static final float DEFAULT_FLOWOUT = 0.0f;
	public static final float DEFAULT_PRESSURE = 13332.24f;

	public SpinalCord(String name) {
		super(name, DEFAULT_HEMI, DEFAULT_LENGTH,DEFAULT_AREA,DEFAULT_ALPHA,DEFAULT_ELASTANCE, DEFAULT_FLOWIN, DEFAULT_FLOWOUT, DEFAULT_PRESSURE);
	}

	public SpinalCord(String name, float len, float a) {
		super(name, DEFAULT_HEMI, len, a, DEFAULT_ALPHA, DEFAULT_ELASTANCE, DEFAULT_FLOWIN, DEFAULT_FLOWOUT, DEFAULT_PRESSURE);
	}

	public SpinalCord(String name, float len, float a, float alpha, float elast, float fin, float fout, float press) {
		super(name, DEFAULT_HEMI, len, a, alpha, elast, fin, fout, press);
	}


	public SpinalCord(String name, float len, float a, float alpha, float elast, float fin, float fout, float press, ArrayList<ElasticTube> par,
			ArrayList<ElasticTube> child) {
		super(name, DEFAULT_HEMI, len, a, alpha, elast, fin, fout, press, par, child);
	}

	public String toString(){
		return "SpinalCord : "+super.toString();
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
		res.add(getSymbolicInitialDistensibilityEquation(ar, pr));

		// momentum
		res.add(getSymbolicInitialMomentumEquation(getParents(),variables));
		if(!getChildren().isEmpty())
			res.add(getSymbolicInitialMomentumEquationOut(getChildren(),variables));
		/*for(ElasticTube parent:getParents()){
			SimpleVariable parentPressure = findVariableWithName(((SAS)parent).getPressure().getName(),variables);
			res.add(getSymbolicInitialMomentumEquation(fi, parentPressure, pr));
		}*/

		// Connectivity
		SimpleVariable sasFlowout = ((SAS)getParents().get(0)).getFlowout();
		SimpleVariable sasPressure = ((SAS)getParents().get(0)).getPressure();
		ArrayList<SimpleVariable> psin = findVariableWithStruct(Hemisphere.BOTH, VenousSinus.TUBE_NUM, PRESSURE_LABEL, variables);
		//res.add(getSymbolicInitialConnectivityEquation(sasFlowout, fi, sasPressure, psin.get(0)));

		//bilan connectivity
		res.add(getSymbolicInitialBilanConnectivityEquation(fo));

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
		res.add(getSymbolicDistensibilityEquation(ar, pr));

		// momentum
		res.add(getSymbolicInitialMomentumEquation(getParents(),variables));
		if(!getChildren().isEmpty())
			res.add(getSymbolicInitialMomentumEquationOut(getChildren(),variables));
		/*for(ElasticTube parent:getParents()){
			SimpleVariable parentPressure = findVariableWithName(((SAS)parent).getPressure().getName(),variables);
			res.add(getSymbolicMomentumEquation(fi, ar, pr, parentPressure));
		}*/

		// Connectivity
		SimpleVariable sasFlowout = ((SAS)getParents().get(0)).getFlowout();
		SimpleVariable sasPressure = ((SAS)getParents().get(0)).getPressure();
		ArrayList<SimpleVariable> psin = findVariableWithStruct(Hemisphere.BOTH, VenousSinus.TUBE_NUM, PRESSURE_LABEL, variables);
		//res.add(getSymbolicConnectivityEquation(sasFlowout, fi, sasPressure, psin.get(0)));

		//bilan connectivity
		res.add(getSymbolicBilanConnectivityEquation(fo));

		return res;
	}


	// symbolic equation (en chaine de caractere)
	private String getSymbolicContinuityEquation(SimpleVariable ar, SimpleVariable fi, SimpleVariable fo){
		// equ(15)
		return "" + "("+ar.getName()+" - "+getArea().getName()+LAST_ROUND_SUFFIX+")/"+ModelSpecification.dt.getName()+""+" + (- "+fi.getName()+"+"+ fo.getName()+")/"+getLength().getName();
	}

	private String getSymbolicDistensibilityEquation(SimpleVariable ar, SimpleVariable pr){
		// equ(30)
		return "-"+ModelSpecification.damp.getName()+" * ("+ar.getName()+" - "+getArea().getName()+LAST_ROUND_SUFFIX+")/"+ModelSpecification.dt.getName()+" + ("+pr.getName()+"- "+ModelSpecification.Pstar.getName()+")-"+getElastance().getName()+"*("+ar.getName()+"/"+getInitialArea().getName()+"-1)";
	}

	private String getSymbolicMomentumEquation(SimpleVariable fi, SimpleVariable ar, SimpleVariable pr, SimpleVariable parentPressure){
		// equ(45)
		return " "+ModelSpecification.damp2.getName()+" * (("+fi.getName()+" / "+ar.getName()+" ) - ("+getFlowin().getName()+LAST_ROUND_SUFFIX+" / "+getArea().getName()+LAST_ROUND_SUFFIX+" ))/ dt + ("+parentPressure.getName()+"-"+pr.getName()+" )-"+getAlpha().getName()+" * "+fi.getName();
	}

	private String getSymbolicConnectivityEquation(SimpleVariable sasFlowout, SimpleVariable fi, SimpleVariable sasPressure, SimpleVariable vsinuspr){
		// equ(58)
		return ""+sasFlowout.getName()+" - ("+fi.getName()+" + "+ModelSpecification.k1.getName()+" * ("+sasPressure.getName()+" - "+vsinuspr.getName()+"))";
	}

	

	//====== Blilan Connectivity ====

	private String getSymbolicBilanConnectivityEquation(SimpleVariable fo){
		// equ(59)
		return ""+fo.getName()+" - "+0.0f;
	}

	

	// ================= init ========================

	private String getSymbolicInitialContinuityEquation(SimpleVariable fi, SimpleVariable fo){
		// eq(15)
		return fi.getName()+" - "+fo.getName();
	}
	private String getSymbolicInitialDistensibilityEquation(SimpleVariable ar, SimpleVariable pr){
		// equ(30)
		return "("+pr.getName()+"- "+ModelSpecification.Pstar.getName()+")-"+getElastance().getName()+"*("+ar.getName()+"/"+getInitialArea().getName()+"-1)";
	}
	private String getSymbolicInitialMomentumEquation(SimpleVariable fi, SimpleVariable parentPressure, SimpleVariable pr){
		// equ(45)
		return "("+parentPressure.getName()+" - "+pr.getName()+")-"+getAlpha().getName()+"*"+fi.getName();
	}

	private String getSymbolicInitialConnectivityEquation(SimpleVariable sasFlowout, SimpleVariable fi, SimpleVariable sasPressure, SimpleVariable vsinuspr){
		// equ(58)
		return ""+sasFlowout.getName()+" - ("+fi.getName()+" + "+ModelSpecification.k1.getName()+" * ("+sasPressure.getName()+" - "+vsinuspr.getName()+"))";
	}

	

	//====== Blilan Connectivity ====

	private String getSymbolicInitialBilanConnectivityEquation(SimpleVariable fo){
		// equ(59)
		return ""+fo.getName()+" - "+0.0f;
	}
}
