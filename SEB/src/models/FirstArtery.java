package models;

import java.util.ArrayList;

import params.ModelSpecification;

public class FirstArtery extends ElasticTube {
	public static final String TUBE_NUM = "I";
	public static final Hemisphere DEFAULT_HEMI = Hemisphere.NONE;
	public static final float DEFAULT_LENGTH = 0.5f;
	public static final float DEFAULT_AREA = 3.42f;
	public static final float DEFAULT_ELASTANCE = 1066579.2f/1333.2240f;// en Pa
	public static final float DEFAULT_ALPHA = 0.1618175f;
	public static final float DEFAULT_FLOWIN = 12.4f;
	public static final float DEFAULT_FLOWOUT = 12.4f;
	public static final float DEFAULT_PRESSURE = 133322.4f/1333.2240f;


	
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

	// --------------- UPDATE ALPHA ------------
	// UPDATE ALPHA en fonction du nombre de tube pour les modeles complexes
	public void updateAlpha(ArrayList<SimpleVariable> variables){
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
		res.add(getSymbolicInitialDistensibilityEquation(ar, pr, pbrain_left, pbrain_right,getChildren()));

		// momentum
		res.add(getSymbolicInitialMomentumEquation(pr,fi));
		if(!getChildren().isEmpty())
			res.add(getSymbolicInitialMomentumEquationOut(getChildren(),variables));
		// Connectivity
		if(!isInitialConnectivityAdded()){
			ArrayList<SimpleVariable> childFin = new ArrayList<SimpleVariable>();
			for(ElasticTube child:getChildren()){
				childFin.add(findVariableWithName(((Artery)child).getFlowin().getName(),variables));
			}
			//res.add(getSymbolicInitialConnectivityEquation(childFin, fo));
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
		res.add(getSymbolicContinuityEquation(fi, fo));
		// Distensibility
		SimpleVariable pr = findVariableWithName(getPressure().getName(),variables);
		SimpleVariable pbrain_left = findVariableWithName(ModelSpecification.architecture.getBrain().getLeftHemi().getPressure().getName(),variables);
		SimpleVariable pbrain_right = findVariableWithName(ModelSpecification.architecture.getBrain().getRightHemi().getPressure().getName(),variables);
		res.add(getSymbolicDistensibilityEquation(ar, pr, pbrain_left, pbrain_right,getChildren()));

		// momentum
		res.add(getSymbolicMomentumEquation(pr,fi));
		if(!getChildren().isEmpty())
			res.add(getSymbolicInitialMomentumEquationOut(getChildren(),variables));
		//res.add(getSymbolicMomentumEquation(pr,fi));

		// Connectivity
		if(!isConnectivityAdded()){
			ArrayList<SimpleVariable> childFin = new ArrayList<SimpleVariable>();
			for(ElasticTube child:getChildren()){
				childFin.add(findVariableWithName(((Artery)child).getFlowin().getName(),variables));
			}
			//res.add(getSymbolicConnectivityEquation(childFin, fo));
		}

		return res;
	}


	// symbolic equation (en chaine de caractere)
	private String getSymbolicContinuityEquation(SimpleVariable fi, SimpleVariable fo){
		// equ(73)
		return "" +fi.getName()+" - "+fo.getName();
	}

	private String getSymbolicDistensibilityEquation(SimpleVariable ar, SimpleVariable pr, SimpleVariable pbrain_left, SimpleVariable pbrain_right, ArrayList<ElasticTube> child){
		// equ(74)
		Hemisphere hemi = Hemisphere.NONE;
		for(ElasticTube el:child){
			if(hemi == Hemisphere.NONE)
				hemi = el.getHemisphere();
			else{
				if(!hemi.equals(el.getHemisphere())){
					hemi = Hemisphere.BOTH;
					break;
				}
			}
		}
		String brainterm = "";
		switch(hemi){
		case BOTH:
			brainterm = ""+0.5f+" * ("+pbrain_right.getName()+" + "+pbrain_left.getName()+")";
			break;
		case LEFT:
			brainterm = " ("+pbrain_left.getName()+")";
			break;
		case RIGHT:
			brainterm = " ("+pbrain_right.getName()+")";
			break;
		}
		return "("+pr.getName()+" - "+brainterm+") - "+getElastance().getName()+" * ("+ar.getName()+"/"+getInitialArea().getName()+"-"+1.0f+")";
	}

	private String getSymbolicMomentumEquation(SimpleVariable pr, SimpleVariable fi){
		// equ(75)
		return "("+ModelSpecification.P_INIT.getName()+"("+ModelSpecification.currentIter.getName()+")- "+pr.getName()+") - "+getAlpha().getName()+" * "+fi.getName();
	}

	

	//====== Connectivity ====

	private String getSymbolicConnectivityEquationStandard(ArrayList<SimpleVariable> childFin, SimpleVariable fo){
		// equ(76)
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

	// symbolic equation (en chaine de caractere)
	private String getSymbolicInitialContinuityEquation(SimpleVariable fi, SimpleVariable fo){
		// equ(73)
		return "" +fi.getName()+" - "+fo.getName();
	}

	private String getSymbolicInitialDistensibilityEquation(SimpleVariable ar, SimpleVariable pr, SimpleVariable pbrain_left, SimpleVariable pbrain_right, ArrayList<ElasticTube> child){
		// equ(74)
		Hemisphere hemi = Hemisphere.NONE;
		for(ElasticTube el:child){
			if(hemi == Hemisphere.NONE)
				hemi = el.getHemisphere();
			else{
				if(!hemi.equals(el.getHemisphere())){
					hemi = Hemisphere.BOTH;
					break;
				}
			}
		}
		String brainterm = "";
		switch(hemi){
		case BOTH:
			brainterm = ""+0.5f+" * ("+pbrain_right.getName()+" + "+pbrain_left.getName()+")";
			break;
		case LEFT:
			brainterm = " ("+pbrain_left.getName()+")";
			break;
		case RIGHT:
			brainterm = " ("+pbrain_right.getName()+")";
			break;
		}
		return "("+pr.getName()+" - "+brainterm+") - "+getElastance().getName()+" * ("+ar.getName()+"/"+getInitialArea().getName()+"-"+1.0f+")";
	}

	private String getSymbolicInitialMomentumEquation(SimpleVariable pr, SimpleVariable fi){
		// equ(75)
		return "("+ModelSpecification.P_INIT_INITIAL.getName()+" - "+pr.getName()+") - "+getAlpha().getName()+" * "+ModelSpecification.FI_INITIAL.getName();//fi.getName();
	}

	//====== Connectivity ====

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
}
