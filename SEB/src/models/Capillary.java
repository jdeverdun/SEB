package models;

import java.util.ArrayList;

import params.ModelSpecification;

public class Capillary extends ElasticTube {
	public static final String TUBE_NUM = "2";
	public static final float DEFAULT_LENGTH = 0.2618f;
	public static final float DEFAULT_AREA = 38.0f;
	public static final float DEFAULT_ELASTANCE = 8500f * 1333.2240f;// en Pa
	public static final float DEFAULT_ALPHA = 9.23508188f * 1333.2240f;
	public static final float DEFAULT_FLOWIN = 13.0f;
	public static final float DEFAULT_FLOWOUT = 13.0f;
	public static final float DEFAULT_PRESSURE = 20.0f * 1333.2240f;
	
	private boolean bilanConnectivityAdded = false;
	private boolean initialBilanConnectivityAdded = false;

	public Capillary(String name, Hemisphere hemi) {
		super(name, hemi, DEFAULT_LENGTH,DEFAULT_AREA,DEFAULT_ALPHA,DEFAULT_ELASTANCE, DEFAULT_FLOWIN, DEFAULT_FLOWOUT, DEFAULT_PRESSURE);
		bilanConnectivityAdded = false;
		initialBilanConnectivityAdded = false;
	}

	public Capillary(String name, Hemisphere hemi, float len, float a) {
		super(name, hemi, len, a, DEFAULT_ALPHA, DEFAULT_ELASTANCE, DEFAULT_FLOWIN, DEFAULT_FLOWOUT, DEFAULT_PRESSURE);
		bilanConnectivityAdded = false;
		initialBilanConnectivityAdded = false;
	}

	public Capillary(String name, Hemisphere hemi, float len, float a, float alpha, float elast, float fin, float fout, float press) {
		super(name, hemi, len, a, alpha, elast, fin, fout, press);
		bilanConnectivityAdded = false;
		initialBilanConnectivityAdded = false;
	}


	public Capillary(String name, Hemisphere hemi, float len, float a, float alpha, float elast, float fin, float fout, float press, ArrayList<ElasticTube> par,
			ArrayList<ElasticTube> child) {
		super(name, hemi, len, a, alpha, elast, fin, fout, press, par, child);
		bilanConnectivityAdded = false;
		initialBilanConnectivityAdded = false;
	}

	public String toString(){
		return "Capillary : "+super.toString();
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
		SimpleVariable Scp_br = findVariableWithName(getAssociatedBrainParenchyma().getScp_br().getName(),variables);
		SimpleVariable Sconst_cp_br = getAssociatedBrainParenchyma().getSconst_cp_br();
		ArrayList<SimpleVariable> fullcap_hemi = findVariableWithStruct(getHemisphere(), Capillary.TUBE_NUM, ElasticTube.PRESSURE_LABEL, variables);
		res.add(getSymbolicInitialContinuityEquation(fi, fo,Sconst_cp_br, Scp_br,fullcap_hemi.size()));
		// Distensibility
		SimpleVariable pr = findVariableWithName(getPressure().getName(),variables);
		SimpleVariable pbrain = findVariableWithName(getAssociatedBrainParenchyma().getPressure().getName(),variables);
		res.add(getSymbolicInitialDistensibilityEquation(ar, pr, pbrain));

		// momentum
		res.add(getSymbolicInitialMomentumEquation(getParents(),variables));
		if(!getChildren().isEmpty())
			res.add(getSymbolicInitialMomentumEquationOut(getChildren(),variables));
		/*for(ElasticTube parent:getParents()){
			SimpleVariable parentPressure = findVariableWithName(parent.getPressure().getName(),variables);
			SimpleVariable parentFlowout = findVariableWithName(parent.getFlowout().getName(),variables);
			if(getParents().size()>1)
				res.add(getSymbolicInitialMomentumEquationDoubleParent(parentFlowout,pr,parentPressure));
			else
				res.add(getSymbolicInitialMomentumEquation(fi, pr, parentPressure));
			
		}*/
		
		// bilan connectivity
		if(!isInitialBilanConnectivityAdded()){
			ArrayList<SimpleVariable> folist = findVariableWithStruct(getHemisphere(),Capillary.TUBE_NUM, FLOWOUT_LABEL,variables);
			ArrayList<SimpleVariable> ft4list = findVariableWithStruct(getHemisphere(),Ventricle.TUBE_NUM,FLOWIN_LABEL ,variables);
			ArrayList<SimpleVariable> ft3list = findVariableWithStruct(getHemisphere(),Veinule.TUBE_NUM,FLOWIN_LABEL ,variables);
			//ArrayList<SimpleVariable> ft3blist = findVariableWithStruct(getHemisphere(),Vein.TUBE_NUM,FLOWIN_LABEL ,variables);
			ArrayList<SimpleVariable> filist = new ArrayList<SimpleVariable>();
			filist.addAll(ft4list);
			//filist.addAll(ft3blist);
			filist.addAll(ft3list);
			SimpleVariable bfin1 = findVariableWithName(getAssociatedBrainParenchyma().getScp_br().getName(), variables);
			SimpleVariable bfin2 = getAssociatedBrainParenchyma().getSconst_cp_br();
			filist.add(bfin1);
			filist.add(bfin2);
			
			res.add(getSymbolicInitialBilanConnectivityEquation(folist, filist));
		}

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
		SimpleVariable Scp_br = findVariableWithName(getAssociatedBrainParenchyma().getScp_br().getName(),variables);
		SimpleVariable Sconst_cp_br = getAssociatedBrainParenchyma().getSconst_cp_br();
		ArrayList<SimpleVariable> fullcap_hemi = findVariableWithStruct(getHemisphere(), Capillary.TUBE_NUM, ElasticTube.PRESSURE_LABEL, variables);
		res.add(getSymbolicContinuityEquation(ar, fi, fo,Sconst_cp_br, Scp_br,fullcap_hemi.size()));
		// Distensibility
		SimpleVariable pr = findVariableWithName(getPressure().getName(),variables);
		SimpleVariable pbrain = findVariableWithName(getAssociatedBrainParenchyma().getPressure().getName(),variables);
		res.add(getSymbolicDistensibilityEquation(ar, pr, pbrain));

		// momentum
		res.add(getSymbolicInitialMomentumEquation(getParents(),variables));
		if(!getChildren().isEmpty())
			res.add(getSymbolicInitialMomentumEquationOut(getChildren(),variables));
		/*for(ElasticTube parent:getParents()){
			SimpleVariable parentPressure = findVariableWithName(parent.getPressure().getName(),variables);
			SimpleVariable parentFlowout = findVariableWithName(parent.getFlowout().getName(),variables);
			if(getParents().size()>1)
				res.add(getSymbolicMomentumEquationDoubleParent(parentFlowout,ar,pr,parentPressure));
			else
				res.add(getSymbolicMomentumEquation(fi, ar, pr, parentPressure));
		}*/
		
		// bilan connectivity
		if(!isBilanConnectivityAdded()){
			ArrayList<SimpleVariable> folist = findVariableWithStruct(getHemisphere(),Capillary.TUBE_NUM, FLOWOUT_LABEL,variables);
			ArrayList<SimpleVariable> ft4list = findVariableWithStruct(getHemisphere(),Ventricle.TUBE_NUM,FLOWIN_LABEL ,variables);
			ArrayList<SimpleVariable> ft3list = findVariableWithStruct(getHemisphere(),Veinule.TUBE_NUM,FLOWIN_LABEL ,variables);
			//ArrayList<SimpleVariable> ft3blist = findVariableWithStruct(getHemisphere(),Vein.TUBE_NUM,FLOWIN_LABEL ,variables);
			ArrayList<SimpleVariable> filist = new ArrayList<SimpleVariable>();
			filist.addAll(ft4list);
			//filist.addAll(ft3blist);
			filist.addAll(ft3list);
			SimpleVariable bfin1 = getAssociatedBrainParenchyma().getScp_br();
			SimpleVariable bfin2 = getAssociatedBrainParenchyma().getSconst_cp_br();
			filist.add(bfin1);
			filist.add(bfin2);
			res.add(getSymbolicBilanConnectivityEquation(folist, filist));
		}

		return res;
	}


	// symbolic equation (en chaine de caractere)
	private String getSymbolicContinuityEquation(SimpleVariable ar, SimpleVariable fi, SimpleVariable fo, SimpleVariable Sconst_cp_br, SimpleVariable Scp_br, int ncapillaries){
		// equ(3) et equ(8)
		return "" + "("+ar.getName()+" - "+getArea().getName()+LAST_ROUND_SUFFIX+")/"+ModelSpecification.dt.getName()+""+" + (- "+fi.getName()+"+"+ fo.getName()+" + "+Sconst_cp_br.getName()+"/"+ncapillaries+" + "+Scp_br.getName()+"/"+ncapillaries+")/"+getLength().getName();
	}

	private String getSymbolicDistensibilityEquation(SimpleVariable ar, SimpleVariable pr, SimpleVariable pbrain){
		// equ(18) et equ(23)
		return " -"+ModelSpecification.damp.getName()+" * ("+ar.getName()+" - "+getArea().getName()+LAST_ROUND_SUFFIX+" )/"+ModelSpecification.dt.getName()+" + ("+pr.getName()+"-"+pbrain.getName()+" )-"+getElastance().getName()+" * ("+ar.getName()+" / "+getInitialArea().getName()+" -1)";
	}

	private String getSymbolicMomentumEquation(SimpleVariable fi, SimpleVariable ar, SimpleVariable pr, SimpleVariable parentPressure){
		// equ(33) et equ(38)
		return " "+ModelSpecification.damp2.getName()+" * (("+fi.getName()+" / "+ar.getName()+" ) - ("+getFlowin().getName()+LAST_ROUND_SUFFIX+" / "+getArea().getName()+LAST_ROUND_SUFFIX+" ))/ dt + ("+parentPressure.getName()+"-"+pr.getName()+" )-"+getAlpha().getName()+" * "+fi.getName();
	}
	private String getSymbolicMomentumEquationDoubleParent(SimpleVariable parentFlowout, SimpleVariable ar, SimpleVariable pr, SimpleVariable parentPressure){
		// equ(33) et equ(38)
		return " "+ModelSpecification.damp2.getName()+" * (("+parentFlowout.getName()+" / "+ar.getName()+" ) - ("+getFlowin().getName()+LAST_ROUND_SUFFIX+" / "+getArea().getName()+LAST_ROUND_SUFFIX+" ))/ dt + ("+parentPressure.getName()+"-"+pr.getName()+" )-"+getAlpha().getName()+" * "+parentFlowout.getName();
	}


	private String getSymbolicBilanConnectivityEquation(ArrayList<SimpleVariable> flowout, ArrayList<SimpleVariable> flowin){
		// equ(50) et equ(53)
		String res1 = "(";
		String res2 = "(";
		for(SimpleVariable pfo : flowout){
			((Capillary)pfo.getSourceObj()).setBilanConnectivityAdded(true);
			if(!res1.equals("("))
				res1 += "+";
			res1 += pfo.getName();
		}
		setBilanConnectivityAdded(true);
		res1 += ")";
		for(SimpleVariable pfi : flowin){
			if(!res2.equals("("))
				res2 += "+";
			res2 += pfi.getName();
		}
		res2 += ")";
		return res1+" - "+res2;
	}
	
	
	
	// ================= init ========================

	private String getSymbolicInitialContinuityEquation(SimpleVariable fi, SimpleVariable fo, SimpleVariable SconstCp_Br, SimpleVariable SCp_Br, int ncapillaries){
		// eq (3)  (8)
		return fi.getName()+" - "+fo.getName()+" - ("+SconstCp_Br.getName()+"/"+ncapillaries+") - ("+SCp_Br.getName()+"/"+ncapillaries+")";
	}
	private String getSymbolicInitialDistensibilityEquation(SimpleVariable ar, SimpleVariable pr, SimpleVariable pbrain){
		// eq (18)  (23)
		return "("+pr.getName()+" - "+pbrain.getName()+") - "+getElastance().getName()+" * ("+ar.getName()+"/"+getInitialArea().getName()+" - 1)";
	}
	private String getSymbolicInitialMomentumEquation(SimpleVariable fi, SimpleVariable pr, SimpleVariable parentPressure){
		// eq (33)  (38)
		return "("+parentPressure.getName()+" - "+pr.getName()+")-"+getAlpha().getName()+"*"+fi.getName();
	}
	private String getSymbolicInitialMomentumEquationDoubleParent(SimpleVariable parentFlowout, SimpleVariable pr, SimpleVariable parentPressure){
		// eq (33)  (38)
		return "("+parentPressure.getName()+" - "+pr.getName()+")-"+getAlpha().getName()+"*"+parentFlowout.getName();
	}
	private String getSymbolicInitialBilanConnectivityEquation(ArrayList<SimpleVariable> flowout, ArrayList<SimpleVariable> flowin){
		// equ(50) et equ(53)
		String res1 = "(";
		String res2 = "(";
		for(SimpleVariable pfo : flowout){
			((Capillary)pfo.getSourceObj()).setInitialBilanConnectivityAdded(true);
			if(!res1.equals("("))
				res1 += "+";
			res1 += pfo.getName();
		}
		res1 += ")";
		for(SimpleVariable pfi : flowin){
			if(!res2.equals("("))
				res2 += "+";
			res2 += pfi.getName();
		}
		res2 += ")";
		setInitialBilanConnectivityAdded(true);
		return res1+" - "+res2;
	}

	/**
	 * @return the initialBilanConnectivityAdded
	 */
	public boolean isInitialBilanConnectivityAdded() {
		return initialBilanConnectivityAdded;
	}

	/**
	 * @param initialBilanConnectivityAdded the initialBilanConnectivityAdded to set
	 */
	public void setInitialBilanConnectivityAdded(
			boolean initialBilanConnectivityAdded) {
		this.initialBilanConnectivityAdded = initialBilanConnectivityAdded;
	}

	/**
	 * @return the bilanConnectivityAdded
	 */
	public boolean isBilanConnectivityAdded() {
		return bilanConnectivityAdded;
	}

	/**
	 * @param bilanConnectivityAdded the bilanConnectivityAdded to set
	 */
	public void setBilanConnectivityAdded(boolean bilanConnectivityAdded) {
		this.bilanConnectivityAdded = bilanConnectivityAdded;
	}
}
