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

	public Capillary(String name, Hemisphere hemi) {
		super(name, hemi, DEFAULT_LENGTH,DEFAULT_AREA,DEFAULT_ALPHA,DEFAULT_ELASTANCE, DEFAULT_FLOWIN, DEFAULT_FLOWOUT, DEFAULT_PRESSURE);
	}

	public Capillary(String name, Hemisphere hemi, float len, float a) {
		super(name, hemi, len, a, DEFAULT_ALPHA, DEFAULT_ELASTANCE, DEFAULT_FLOWIN, DEFAULT_FLOWOUT, DEFAULT_PRESSURE);
	}

	public Capillary(String name, Hemisphere hemi, float len, float a, float alpha, float elast, float fin, float fout, float press) {
		super(name, hemi, len, a, alpha, elast, fin, fout, press);
	}


	public Capillary(String name, Hemisphere hemi, float len, float a, float alpha, float elast, float fin, float fout, float press, ArrayList<ElasticTube> par,
			ArrayList<ElasticTube> child) {
		super(name, hemi, len, a, alpha, elast, fin, fout, press, par, child);
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
		res.add(getSymbolicInitialContinuityEquation(fi, fo));
		// Distensibility
		SimpleVariable pr = findVariableWithName(getPressure().getName(),variables);
		SimpleVariable pbrain = findVariableWithName(getAssociatedBrainParenchyma().getPressure().getName(),variables);
		res.add(getSymbolicInitialDistensibilityEquation(ar, pr, pbrain));

		// momentum
		for(ElasticTube parent:getParents()){
			SimpleVariable parentPressure = findVariableWithName(((Arteriole)parent).getPressure().getName(),variables);
			res.add(getSymbolicInitialMomentumEquation(fi, pr, parentPressure));
		}

		// connectivity
		res.add(getSymbolicInitialConnectivityEquation(fi));
		
		// bilan connectivity
		ArrayList<SimpleVariable> folist = findVariableWithStruct(getHemisphere(),Capillary.TUBE_NUM, FLOWOUT_LABEL,variables);
		ArrayList<SimpleVariable> ft4list = findVariableWithStruct(getHemisphere(),Ventricle.TUBE_NUM,FLOWIN_LABEL ,variables);
		ArrayList<SimpleVariable> ft3list = findVariableWithStruct(getHemisphere(),Veinule.TUBE_NUM,FLOWIN_LABEL ,variables);
		//ArrayList<SimpleVariable> ft3blist = findVariableWithStruct(getHemisphere(),Vein.TUBE_NUM,FLOWIN_LABEL ,variables);
		ArrayList<SimpleVariable> filist = new ArrayList<SimpleVariable>();
		filist.addAll(ft4list);
		//filist.addAll(ft3blist);
		filist.addAll(ft3list);
		SimpleVariable bfin1 = findVariableWithName(getAssociatedBrainParenchyma().getFlowin1().getName(), variables);
		SimpleVariable bfin2 = findVariableWithName(getAssociatedBrainParenchyma().getFlowin2().getName(), variables);
		filist.add(bfin1);
		filist.add(bfin2);
		res.add(getSymbolicInitialBilanConnectivityEquation(folist, filist));

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
		SimpleVariable pbrain = findVariableWithName(getAssociatedBrainParenchyma().getPressure().getName(),variables);
		res.add(getSymbolicDistensibilityEquation(ar, pr, pbrain));

		// momentum
		for(ElasticTube parent:getParents()){
			SimpleVariable parentPressure = findVariableWithName(((Arteriole)parent).getPressure().getName(),variables);
			res.add(getSymbolicMomentumEquation(fi, ar, pr, parentPressure));
		}

		// connectivity
		res.add(getSymbolicConnectivityEquation(fi));
		
		// bilan connectivity
		ArrayList<SimpleVariable> folist = findVariableWithStruct(getHemisphere(),Capillary.TUBE_NUM, FLOWOUT_LABEL,variables);
		ArrayList<SimpleVariable> ft4list = findVariableWithStruct(getHemisphere(),Ventricle.TUBE_NUM,FLOWIN_LABEL ,variables);
		ArrayList<SimpleVariable> ft3list = findVariableWithStruct(getHemisphere(),Veinule.TUBE_NUM,FLOWIN_LABEL ,variables);
		//ArrayList<SimpleVariable> ft3blist = findVariableWithStruct(getHemisphere(),Vein.TUBE_NUM,FLOWIN_LABEL ,variables);
		ArrayList<SimpleVariable> filist = new ArrayList<SimpleVariable>();
		filist.addAll(ft4list);
		//filist.addAll(ft3blist);
		filist.addAll(ft3list);
		SimpleVariable bfin1 = findVariableWithName(getAssociatedBrainParenchyma().getFlowin1().getName(), variables);
		SimpleVariable bfin2 = findVariableWithName(getAssociatedBrainParenchyma().getFlowin2().getName(), variables);
		filist.add(bfin1);
		filist.add(bfin2);
		res.add(getSymbolicBilanConnectivityEquation(folist, filist));

		return res;
	}


	// symbolic equation (en chaine de caractere)
	private String getSymbolicContinuityEquation(SimpleVariable ar, SimpleVariable fi, SimpleVariable fo){
		// equ(3) et equ(8)
		return "" + "("+ar.getName()+" - "+getArea().getName()+LAST_ROUND_SUFFIX+")/"+ModelSpecification.dt.getName()+""+" + (- "+fi.getName()+"+"+ fo.getName()+")/"+getLength().getName();
	}

	private String getSymbolicDistensibilityEquation(SimpleVariable ar, SimpleVariable pr, SimpleVariable pbrain){
		// equ(18) et equ(23)
		return " -"+ModelSpecification.damp.getName()+" * ("+ar.getName()+" - "+getArea().getName()+LAST_ROUND_SUFFIX+" )/"+ModelSpecification.dt.getName()+" + ("+pr.getName()+"-"+pbrain.getName()+" )-"+getElastance().getName()+" * ("+ar.getName()+" / "+getInitialArea().getName()+" -1)";
	}

	private String getSymbolicMomentumEquation(SimpleVariable fi, SimpleVariable ar, SimpleVariable pr, SimpleVariable parentPressure){
		// equ(33) et equ(38)
		return " "+ModelSpecification.damp2.getName()+" * (("+fi.getName()+" / "+ar.getName()+" ) - ("+getFlowin().getName()+LAST_ROUND_SUFFIX+" / "+getArea().getName()+LAST_ROUND_SUFFIX+" ))/ dt + ("+parentPressure.getName()+"-"+pr.getName()+" )-"+getAlpha().getName()+" * "+fi.getName();
	}

	private String getSymbolicConnectivityEquation(SimpleVariable fi){
		// equ(49) et equ(52)
		String res = "(";
		for(ElasticTube parent : getParents()){//for(Variable pf : parentFlowout){
			if(!res.equals("("))
				res += "+";
			Arteriole par = ((Arteriole)parent);
			SimpleVariable pf = par.getFlowout();
			float fact = par.getChildren().size();
			res += "("+pf.getName()+"/"+fact+")";
		}
		res += ")";
		return "("+res+" - "+fi.getName()+")";
	}
	
	private String getSymbolicBilanConnectivityEquation(ArrayList<SimpleVariable> flowout, ArrayList<SimpleVariable> flowin){
		// equ(50) et equ(53)
		String res1 = "(";
		String res2 = "(";
		for(SimpleVariable pfo : flowout){
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
		return res1+" - "+res2;
	}
	
	
	// ================= init ========================

	private String getSymbolicInitialContinuityEquation(SimpleVariable fi, SimpleVariable fo){
		// eq (3)  (8)
		return fi.getName()+" - "+fo.getName();
	}
	private String getSymbolicInitialDistensibilityEquation(SimpleVariable ar, SimpleVariable pr, SimpleVariable pbrain){
		// eq (18)  (23)
		return "("+pr.getName()+" - "+pbrain.getName()+") - "+getElastance().getName()+" * ("+ar.getName()+"/"+getInitialArea().getName()+" - 1)";
	}
	private String getSymbolicInitialMomentumEquation(SimpleVariable fi, SimpleVariable pr, SimpleVariable parentPressure){
		// eq (33)  (38)
		return "("+parentPressure.getName()+" - "+pr.getName()+")-"+getAlpha().getName()+"*"+fi.getName();
	}
	private String getSymbolicInitialConnectivityEquation(SimpleVariable fi){
		// equ(49) et equ(52)
		String res = "(";
		for(ElasticTube parent : getParents()){//for(Variable pf : parentFlowout){
			if(!res.equals("("))
				res += "+";
			Arteriole par = ((Arteriole)parent);
			SimpleVariable pf = par.getFlowout();
			float fact = par.getChildren().size();
			res += "("+pf.getName()+"/"+fact+")";
		}
		res += ")";
		return "("+res+" - "+fi.getName()+")";
	}

	private String getSymbolicInitialBilanConnectivityEquation(ArrayList<SimpleVariable> flowout, ArrayList<SimpleVariable> flowin){
		// equ(50) et equ(53)
		String res1 = "(";
		String res2 = "(";
		for(SimpleVariable pfo : flowout){
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
		return res1+" - "+res2;
	}
}
