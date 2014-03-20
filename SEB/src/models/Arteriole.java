package models;

import java.util.ArrayList;

import params.ModelSpecification;

public class Arteriole extends ElasticTube {
	public static final String TUBE_NUM = "1";
	public static final float DEFAULT_LENGTH = 1.75f;
	public static final float DEFAULT_AREA = 4.74f;
	public static final float DEFAULT_ELASTANCE = 2735000.0f;// en Pa 
	public static final float DEFAULT_ALPHA = 3.076819468f * 1333.2240f;
	public static final float DEFAULT_FLOWIN = 13.0f;
	public static final float DEFAULT_FLOWOUT = 13.0f;
	public static final float DEFAULT_PRESSURE = 80.0f*1333.2240f;

	public Arteriole(String name, Hemisphere hemi) {
		super(name, hemi, DEFAULT_LENGTH,DEFAULT_AREA,DEFAULT_ALPHA,DEFAULT_ELASTANCE, DEFAULT_FLOWIN, DEFAULT_FLOWOUT, DEFAULT_PRESSURE);
	}

	public Arteriole(String name, Hemisphere hemi, float len, float a) {
		super(name, hemi, len, a, DEFAULT_ALPHA, DEFAULT_ELASTANCE, DEFAULT_FLOWIN, DEFAULT_FLOWOUT, DEFAULT_PRESSURE);
	}

	public Arteriole(String name, Hemisphere hemi, float len, float a, float alpha, float elast, float fin, float fout, float press) {
		super(name, hemi, len, a, alpha, elast, fin, fout, press);
	}


	public Arteriole(String name, Hemisphere hemi, float len, float a, float alpha, float elast, float fin, float fout, float press, ArrayList<ElasticTube> par,
			ArrayList<ElasticTube> child) {
		super(name, hemi, len, a, alpha, elast, fin, fout, press, par, child);
	}

	public String toString(){
		return "Arteriole : "+super.toString();
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
			SimpleVariable parentPressure = findVariableWithName(parent.getPressure().getName(),variables);
			res.add(getSymbolicMomentumEquation(fi, ar, pr, parentPressure));
		}
		
		// connectivity
		ArrayList<SimpleVariable> childFin = new ArrayList<SimpleVariable>();
		for(ElasticTube child:getChildren()){
			childFin.add(findVariableWithName(child.getFlowin().getName(),variables));
		}
		res.add(getSymbolicConnectivityEquation(childFin, fo));
				
		return res;
	}
	
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
			SimpleVariable parentPressure = findVariableWithName(parent.getPressure().getName(),variables);
			res.add(getSymbolicInitialMomentumEquation(fi, pr, parentPressure));
		}
		
		// connectivity
		ArrayList<SimpleVariable> childFin = new ArrayList<SimpleVariable>();
		for(ElasticTube child:getChildren()){
			childFin.add(findVariableWithName(child.getFlowin().getName(),variables));
		}
		res.add(getSymbolicInitialConnectivityEquation(childFin, fo));
				
		return res;
	}

	
	// symbolic equation (en chaine de caractere)

	
	
	private String getSymbolicContinuityEquation(SimpleVariable ar, SimpleVariable fi, SimpleVariable fo){
		// equ(2) et equ(7)
		return "" + "("+ar.getName()+" - "+getArea().getName()+LAST_ROUND_SUFFIX+")/"+ModelSpecification.dt.getName()+""+" + (- "+fi.getName()+"+"+ fo.getName()+")/"+getLength().getName();
	}

	private String getSymbolicDistensibilityEquation(SimpleVariable ar, SimpleVariable pr, SimpleVariable pbrain){
		// equ(17) et equ(22)
		return " -"+ModelSpecification.damp.getName()+" * ("+ar.getName()+" - "+getArea().getName()+LAST_ROUND_SUFFIX+" )/"+ModelSpecification.dt.getName()+" + ("+pr.getName()+"-"+pbrain.getName()+" )-"+getElastance().getName()+" * ("+ar.getName()+" / "+getInitialArea().getName()+" -1)";
	}

	private String getSymbolicMomentumEquation(SimpleVariable fi, SimpleVariable ar, SimpleVariable pr, SimpleVariable parentPressure){
		// equ(32) et equ(37)
		return " "+ModelSpecification.damp2.getName()+" * (("+fi.getName()+" / "+ar.getName()+" ) - ("+getFlowin().getName()+LAST_ROUND_SUFFIX+" / "+getArea().getName()+LAST_ROUND_SUFFIX+" ))/ dt + ("+parentPressure.getName()+"-"+pr.getName()+" )-"+getAlpha().getName()+" * "+fi.getName();
	}

	//====== Connectivity ====
	private String getSymbolicConnectivityEquationStandard(ArrayList<SimpleVariable> childFin, SimpleVariable fo){
		// equ(49) (52)
		String res = "";
		for(SimpleVariable pf : childFin){
			if(!res.equals(""))
				res += "+";
			res += pf.getName();
		}
		return ""+fo.getName()+" - ("+res+")";
	}
	private String getSymbolicConnectivityEquation(ArrayList<SimpleVariable> childFin, SimpleVariable fo){
		String res = "";
		
		// Cas simples
		if(childFin.size() == 1 && ((ElasticTube)childFin.get(0).getSourceObj()).getParents().size() == 1)
			return getSymbolicConnectivityEquationStandard(childFin, fo);
		boolean iseasy = true;
		for(ElasticTube ch:getChildren()){
			if(ch.getParents().size()>1)
				iseasy = false;
		}
		if(iseasy)
			return getSymbolicConnectivityEquationStandard(childFin, fo);
		
		// cas complique ....
		ArrayList<ElasticTube> parentAdded = new ArrayList<ElasticTube>();
		ArrayList<ElasticTube> childrenAdded = new ArrayList<ElasticTube>();
		String pref = "";
		parentAdded.add(this);
		for(ElasticTube ch:getChildren()){
			if(ch.getParents().size() == 1){
				res += pref + ch.getFlowin().getName();
				pref = "+";
				childrenAdded.add(ch);
			}else{
				childrenAdded.add(ch);
				res += pref + "(" + ch.getFlowin().getName() + " - " +  recursAdd(ch, parentAdded, childrenAdded) + ")";
				pref = "+";
			}
		}
		setConnectivityAdded(true);
		return fo.getName()+" - ("+res+")";
	}
	private String recursAdd(ElasticTube base,
			ArrayList<ElasticTube> parentAdded,
			ArrayList<ElasticTube> childrenAdded) {
		String res = "(";
		String pref = "";
		for(ElasticTube par:base.getParents()){
			if(parentAdded.contains(par))
				continue;
			if(par.getChildren().size() == 1){
				res += pref + par.getFlowout().getName();
				pref = "-";
				parentAdded.add(par);
				par.setConnectivityAdded(true);
			}else{
				parentAdded.add(par);
				par.setConnectivityAdded(true);
				res += pref  + par.getFlowout().getName() + " - " +  recursAdd2(par, parentAdded, childrenAdded);
				pref = "-";
			}
		}
		return res+")";
	}

	private String recursAdd2(ElasticTube base,
			ArrayList<ElasticTube> parentAdded,
			ArrayList<ElasticTube> childrenAdded) {
		String res = "(";
		String pref = "";
		
		
		for(ElasticTube ch:base.getChildren()){
			if(childrenAdded.contains(ch))
				continue;
			if(ch.getParents().size() == 1){
				res += pref + ch.getFlowin().getName();
				pref = "+";
				childrenAdded.add(ch);
			}else{
				childrenAdded.add(ch);
				res += pref + ch.getFlowin().getName() + " - " +  recursAdd(ch, parentAdded, childrenAdded);
				pref = "+";
			}
		}
		return res+")";
	}
	// ================= init ========================

	private String getSymbolicInitialContinuityEquation(SimpleVariable fi, SimpleVariable fo){
		// eq (2)  (7)
		return fi.getName()+" - "+fo.getName();
	}

	private String getSymbolicInitialDistensibilityEquation(SimpleVariable ar, SimpleVariable pr, SimpleVariable pbrain){
		// eq (17)  (22)
		return "("+pr.getName()+" - "+pbrain.getName()+") - "+getElastance().getName()+" * ("+ar.getName()+"/"+getInitialArea().getName()+" - 1)";
	}

	// momentum
	private String getSymbolicInitialMomentumEquation(SimpleVariable fi, SimpleVariable pr, SimpleVariable parentPressure){
		// eq (32)  (37)
		return "("+parentPressure.getName()+" - "+pr.getName()+")-"+getAlpha().getName()+"*"+fi.getName();
	}
	

	private String getSymbolicInitialConnectivityEquation(ArrayList<SimpleVariable> childFin, SimpleVariable fo){
		// equ(49) (52)
		String res = "";
		
		// Cas simples
		if(childFin.size() == 1 && ((ElasticTube)childFin.get(0).getSourceObj()).getParents().size() == 1)
			return getSymbolicConnectivityEquationStandard(childFin, fo);
		boolean iseasy = true;
		for(ElasticTube ch:getChildren()){
			if(ch.getParents().size()>1)
				iseasy = false;
		}
		if(iseasy)
			return getSymbolicConnectivityEquationStandard(childFin, fo);
		
		// cas complique ....
		ArrayList<ElasticTube> parentAdded = new ArrayList<ElasticTube>();
		ArrayList<ElasticTube> childrenAdded = new ArrayList<ElasticTube>();
		String pref = "";
		parentAdded.add(this);
		for(ElasticTube ch:getChildren()){
			if(ch.getParents().size() == 1){
				res += pref + ch.getFlowin().getName();
				pref = "+";
				childrenAdded.add(ch);
			}else{
				childrenAdded.add(ch);
				res += pref + "(" + ch.getFlowin().getName() + " - " +  recursInitAdd(ch, parentAdded, childrenAdded) + ")";
				pref = "+";
			}
		}
		setInitialConnectivityAdded(true);
		return fo.getName()+" - ("+res+")";
	}
	private String recursInitAdd(ElasticTube base,
			ArrayList<ElasticTube> parentAdded,
			ArrayList<ElasticTube> childrenAdded) {
		String res = "(";
		String pref = "";
		for(ElasticTube par:base.getParents()){
			if(parentAdded.contains(par))
				continue;
			if(par.getChildren().size() == 1){
				res += pref + par.getFlowout().getName();
				pref = "-";
				parentAdded.add(par);
				par.setInitialConnectivityAdded(true);
			}else{
				parentAdded.add(par);
				par.setInitialConnectivityAdded(true);
				res += pref  + par.getFlowout().getName() + " - " +  recursInitAdd2(par, parentAdded, childrenAdded);
				pref = "-";
			}
		}
		return res+")";
	}

	private String recursInitAdd2(ElasticTube base,
			ArrayList<ElasticTube> parentAdded,
			ArrayList<ElasticTube> childrenAdded) {
		return recursAdd2(base, parentAdded, childrenAdded);
	}

}
