package models;

import java.io.ObjectInputStream.GetField;
import java.util.ArrayList;

import params.ModelSpecification;
import params.ModelSpecification.SimulationMode;

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
		res.add(getSymbolicInitialMomentumEquation(getParents(),variables));
		if(!getChildren().isEmpty())
			res.add(getSymbolicInitialMomentumEquationOut(getChildren(),variables));
		/*for(ElasticTube el : getParents()){
			SimpleVariable parentPressure = findVariableWithName(el.getPressure().getName(),variables);
			SimpleVariable parentFlowout = findVariableWithName(el.getFlowout().getName(),variables);
			if(getParents().size()>1)
				res.add(getSymbolicInitialMomentumEquationDoubleParent(parentFlowout,pr,parentPressure));
			else
				res.add(getSymbolicInitialMomentumEquation(fi, pr, parentPressure));
		}*/
		
		if(ModelSpecification.SIM_MODE == SimulationMode.DEBUG && getChildren().isEmpty()){
			res.add(getSymbolicInitialAddMomentumEquation(pr,fo));
		}else{
			// connectivity if needed
			if(!isInitialConnectivityAdded()){
				ArrayList<SimpleVariable> childFin = new ArrayList<SimpleVariable>();
				for(ElasticTube child:getChildren()){
					childFin.add(findVariableWithName(child.getFlowin().getName(),variables));
				}
				//res.add(getSymbolicInitialConnectivityEquation(childFin, fo));
			}
		}
		
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
		for(ElasticTube el : getParents()){
			SimpleVariable parentPressure = findVariableWithName(el.getPressure().getName(),variables);
			SimpleVariable parentFlowout = findVariableWithName(el.getFlowout().getName(),variables);
			if(getParents().size()>1)
				res.add(getSymbolicMomentumEquationDoubleParent(parentFlowout,ar,pr,parentPressure));
			else
				res.add(getSymbolicMomentumEquation(fi, ar, pr, parentPressure));
		}
		
		// connectivity if needed
		if(ModelSpecification.SIM_MODE == SimulationMode.DEBUG && getChildren().isEmpty()){
			res.add(getSymbolicAddMomentumEquation(pr,fo));
		}else{
			if(!isConnectivityAdded()){
				ArrayList<SimpleVariable> childFin = new ArrayList<SimpleVariable>();
				for(ElasticTube child:getChildren()){
					childFin.add(findVariableWithName(child.getFlowin().getName(),variables));
				}
				res.add(getSymbolicConnectivityEquation(childFin, fo));
			}
		}
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
	
	private String getSymbolicMomentumEquation(SimpleVariable fi, SimpleVariable ar, SimpleVariable pr, SimpleVariable parentPressure){
		// equ(31) et equ(36)
		//return " "+ModelSpecification.damp2.getName()+" * (("+fi.getName()+" / "+ar.getName()+" ) - ("+getFlowin().getName()+LAST_ROUND_SUFFIX+" / "+getArea().getName()+LAST_ROUND_SUFFIX+" ))/ "+ModelSpecification.dt.getName()+" + ("+ModelSpecification.P_INIT.getName()+"("+ModelSpecification.currentIter.getName()+") - "+pr.getName()+" )-"+getAlpha().getName()+" * "+fi.getName();
		return " "+ModelSpecification.damp2.getName()+" * (("+fi.getName()+" / "+ar.getName()+" ) - ("+getFlowin().getName()+LAST_ROUND_SUFFIX+" / "+getArea().getName()+LAST_ROUND_SUFFIX+" ))/ "+ModelSpecification.dt.getName()+" + ("+parentPressure.getName()+" - "+pr.getName()+" )-"+getAlpha().getName()+" * "+fi.getName();
	}
	private String getSymbolicMomentumEquationDoubleParent(SimpleVariable parentFlowout, SimpleVariable ar, SimpleVariable pr, SimpleVariable parentPressure){
		// equ(31) et equ(36)
		//return " "+ModelSpecification.damp2.getName()+" * (("+fi.getName()+" / "+ar.getName()+" ) - ("+getFlowin().getName()+LAST_ROUND_SUFFIX+" / "+getArea().getName()+LAST_ROUND_SUFFIX+" ))/ "+ModelSpecification.dt.getName()+" + ("+ModelSpecification.P_INIT.getName()+"("+ModelSpecification.currentIter.getName()+") - "+pr.getName()+" )-"+getAlpha().getName()+" * "+fi.getName();
		return " "+ModelSpecification.damp2.getName()+" * (("+parentFlowout.getName()+" / "+ar.getName()+" ) - ("+getFlowin().getName()+LAST_ROUND_SUFFIX+" / "+getArea().getName()+LAST_ROUND_SUFFIX+" ))/ "+ModelSpecification.dt.getName()+" + ("+parentPressure.getName()+" - "+pr.getName()+" )-"+getAlpha().getName()+" * "+parentFlowout.getName();
	}
	

	//====== Connectivity quand parent = artere only ====

	private String getSymbolicConnectivityEquationStandard(ArrayList<SimpleVariable> childFin, SimpleVariable fo){
		// equ(48) (51)
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
		// eq (1)  (6)
		return fi.getName()+" - "+fo.getName();
	}
	
	// distensibility
	private String getSymbolicInitialDistensibilityEquation(SimpleVariable ar, SimpleVariable pr, SimpleVariable pbrain){
		// eq (16)  (21)
		return "("+pr.getName()+" - "+pbrain.getName()+") - "+getElastance().getName()+" * ("+ar.getName()+"/"+getInitialArea().getName()+" - 1)";
	}

	// momentum
	/*private String getSymbolicInitialMomentumEquation(SimpleVariable fi, SimpleVariable pr, SimpleVariable parentPressure){
		// eq (31)  (36)
		//return "("+ModelSpecification.P_INIT_INITIAL.getName()+" - "+pr.getName()+")-"+getAlpha().getName()+"*"+fi.getName();
		return "("+parentPressure.getName()+" - "+pr.getName()+")-"+getAlpha().getName()+"*"+fi.getName();
	}*/
	
	private String getSymbolicInitialMomentumEquationDoubleParent(SimpleVariable parentFlowout, SimpleVariable pr, SimpleVariable parentPressure){
		// eq (31)  (36)
		//return "("+ModelSpecification.P_INIT_INITIAL.getName()+" - "+pr.getName()+")-"+getAlpha().getName()+"*"+fi.getName();
		return "("+parentPressure.getName()+" - "+pr.getName()+")-"+getAlpha().getName()+"*"+parentFlowout.getName();
	}
	// --- init connectivity
	private String getSymbolicInitialConnectivityEquation(ArrayList<SimpleVariable> childFin, SimpleVariable fo){
		// equ(48) (51)
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
	
	// si bloc terminal
	private String getSymbolicAddMomentumEquation(SimpleVariable pr, SimpleVariable fo){
		// equ(72)
		return "("+pr.getName()+" - "+ModelSpecification.P_OUT.getName()+"("+ModelSpecification.currentIter.getName()+")) - "+ModelSpecification.TPout_alfa.getName()+" * "+fo.getName();
	}
	
	private String getSymbolicInitialAddMomentumEquation(SimpleVariable pr, SimpleVariable fo){
		// equ(72)
		return "("+pr.getName()+" - "+ModelSpecification.P_OUT_INITIAL.getName()+") - "+ModelSpecification.TPout_alfa.getName()+" * "+fo.getName();
	}

}
