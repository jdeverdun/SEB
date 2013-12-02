package models;

import java.util.ArrayList;

import params.ModelSpecification;

public class Vein extends ElasticTube {
	public static final String TUBE_NUM = "3b";
	public static final float DEFAULT_LENGTH = 4.074f;
	public static final float DEFAULT_AREA = 5.3388f;
	public static final float DEFAULT_ELASTANCE = 1008666.7f;// en Pa
	public static final float DEFAULT_ALPHA = 0.1457065f * 1333.2240f;
	public static final float DEFAULT_FLOWIN = 6.5f;
	public static final float DEFAULT_FLOWOUT = 6.5f;
	public static final float DEFAULT_PRESSURE = 5332.89f;

	public Vein(String name, Hemisphere hemi) {
		super(name, hemi, DEFAULT_LENGTH,DEFAULT_AREA,DEFAULT_ALPHA,DEFAULT_ELASTANCE, DEFAULT_FLOWIN, DEFAULT_FLOWOUT, DEFAULT_PRESSURE);
	}

	public Vein(String name, Hemisphere hemi, float len, float a) {
		super(name, hemi, len, a, DEFAULT_ALPHA, DEFAULT_ELASTANCE, DEFAULT_FLOWIN, DEFAULT_FLOWOUT, DEFAULT_PRESSURE);
	}

	public Vein(String name, Hemisphere hemi, float len, float a, float alpha, float elast, float fin, float fout, float press) {
		super(name, hemi, len, a, alpha, elast, fin, fout, press);
	}


	public Vein(String name, Hemisphere hemi, float len, float a, float alpha, float elast, float fin, float fout, float press, ArrayList<ElasticTube> par,
			ArrayList<ElasticTube> child) {
		super(name, hemi, len, a, alpha, elast, fin, fout, press, par, child);
	}

	public String toString(){
		return "Vein : "+super.toString();
	}
	@Override
	public String getTubeNum() {
		return TUBE_NUM;
	}

	// ------------------- EQUATIONS -------------
	@Override
	public ArrayList<float[]> getEquations(ArrayList<Variable> variables) throws Exception {
		ArrayList<float[]> res = new ArrayList<float[]>();

		// Continuity
		float[] continuity = new float[variables.size()+1];
		Variable ar = findVariableWithName(getArea().getName(),variables);
		Variable fi = findVariableWithName(getFlowin().getName(),variables);
		Variable fo = findVariableWithName(getFlowout().getName(),variables);
		continuity[0] = getContinuityEquation(fi, fo);
		for(int i = 0; i<variables.size();i++){
			continuity[i+1] = getContinuityDerivative(variables.get(i), variables);
		}
		res.add(continuity);
		// Distensibility
		float[] distensibility = new float[variables.size()+1];
		Variable pr = findVariableWithName(getPressure().getName(),variables);
		Variable pbrain = findVariableWithName(getAssociatedBrainParenchyma().getPressure().getName(),variables);
		distensibility[0] = getDistensibilityEquation(ar, pr, pbrain);
		for(int i = 0; i<variables.size();i++){
			distensibility[i+1] = getDistensibilityDerivative(variables.get(i), variables);
		}
		res.add(distensibility);

		// momentum
		for(ElasticTube parent:getParents()){
			Variable parentPressure = findVariableWithName(((Veinule)parent).getPressure().getName(),variables);
			float[] momentum = new float[variables.size()+1];
			momentum[0] = getMomentumEquation(fi, pr, parentPressure);
			for(int i = 0; i<variables.size();i++){
				momentum[i+1] = getMomentumDerivative(variables.get(i), variables);
			}
			res.add(momentum);
		}

		// connectivity
		float[] connectivity = new float[variables.size()+1];
		connectivity[0] = getConnectivityEquation(fi);
		for(int i = 0; i<variables.size();i++){
			connectivity[i+1] = getConnectivityDerivative(variables.get(i), variables);
		}
		res.add(connectivity);
		return res;
	}

	/**
	 * Renvoi les equations en format symbolic (en string)
	 * @param variables
	 * @return
	 * @throws Exception
	 */
	public ArrayList<String[]> getSymbolicEquations(ArrayList<Variable> variables) throws Exception {
		ArrayList<String[]> res = new ArrayList<String[]>();

		// Continuity
		String[] continuity = new String[variables.size()+1];
		Variable ar = findVariableWithName(getArea().getName(),variables);
		Variable fi = findVariableWithName(getFlowin().getName(),variables);
		Variable fo = findVariableWithName(getFlowout().getName(),variables);
		continuity[0] = getSymbolicContinuityEquation(fi, fo);
		for(int i = 0; i<variables.size();i++){
			continuity[i+1] = getSymbolicContinuityDerivative(variables.get(i), variables);
		}
		res.add(continuity);
		// Distensibility
		String[] distensibility = new String[variables.size()+1];
		Variable pr = findVariableWithName(getPressure().getName(),variables);
		Variable pbrain = findVariableWithName(getAssociatedBrainParenchyma().getPressure().getName(),variables);
		distensibility[0] = getSymbolicDistensibilityEquation(ar, pr, pbrain);
		for(int i = 0; i<variables.size();i++){
			distensibility[i+1] = getSymbolicDistensibilityDerivative(variables.get(i), variables);
		}
		res.add(distensibility);

		// momentum
		for(ElasticTube parent:getParents()){
			Variable parentPressure = findVariableWithName(((Veinule)parent).getPressure().getName(),variables);
			String[] momentum = new String[variables.size()+1];
			momentum[0] = getSymbolicMomentumEquation(fi, pr, parentPressure);
			for(int i = 0; i<variables.size();i++){
				momentum[i+1] = getSymbolicMomentumDerivative(variables.get(i), variables);
			}
			res.add(momentum);
		}

		// connectivity
		String[] connectivity = new String[variables.size()+1];
		connectivity[0] = getSymbolicConnectivityEquation(fi);
		for(int i = 0; i<variables.size();i++){
			connectivity[i+1] = getSymbolicConnectivityDerivative(variables.get(i), variables);
		}
		res.add(connectivity);

		return res;
	}


	private float getContinuityEquation(Variable fi, Variable fo){
		// equ(77) et equ(81)
		return fi.getValue()-fo.getValue();
	}

	private float getDistensibilityEquation(Variable ar, Variable pr, Variable pbrain){
		// equ(78) et equ(82)
		return (pr.getValue() - pbrain.getValue()) - getElastance().getValue() * (ar.getValue()/getInitialArea().getValue() - 1);
	}

	private float getMomentumEquation(Variable fi, Variable pr, Variable parentPressure){
		// equ(79) et equ(83)
		return (parentPressure.getValue() - pr.getValue()) - getAlpha().getValue() * fi.getValue();
	}

	// symbolic equation (en chaine de caractere)
	private String getSymbolicContinuityEquation(Variable fi, Variable fo){
		// equ(77) et equ(81)
		return ""+fi.getName()+"-"+fo.getName();
	}

	private String getSymbolicDistensibilityEquation(Variable ar, Variable pr, Variable pbrain){
		// equ(78) et equ(82)
		return "("+pr.getName()+" - "+pbrain.getName()+") - "+getElastance().getName()+" * ("+ar.getName()+"/"+getInitialArea().getName()+" - 1)";
	}

	private String getSymbolicMomentumEquation(Variable fi, Variable pr, Variable parentPressure){
		// equ(79) et equ(83)
		return "("+parentPressure.getName()+" - "+pr.getName()+") - "+getAlpha().getName()+" * "+fi.getName();
	}

	// ------- Derive -----------
	private float getContinuityDerivative(Variable v, ArrayList<Variable> variables){
		// equ(77) et equ(81)
		if(v.getName().equals(getFlowin().getName())){
			// derive selon flowin : 1;
			return 1.0f;
		}else{
			if(v.getName().equals(getFlowout().getName())){
				// derive selon flowout : -1
				return -1.0f;		
			}else{
				return 0.0f;
			}
		}
	}

	private float getDistensibilityDerivative(Variable v, ArrayList<Variable> variables){
		// equ(78) et equ(82)
		if(v.getName().equals(getArea().getName())){
			// derive selon area : - T3b_E * (1/T3b_A0);
			return - getElastance().getValue() * (1.0f/getInitialArea().getValue());
		}else{
			if(v.getName().equals(getPressure().getName())){
				// derive selon pression : 1.0f
				return 1.0f;
			}else{
				if(v.getName().equals(getAssociatedBrainParenchyma().getPressure().getName())){
					// derive selon pression brain : - 1 
					return -1.0f;		
				}else{
					return 0.0f;
				}
			}
		}
	}

	private float getMomentumDerivative(Variable v, ArrayList<Variable> variables) throws Exception{
		// equ(79) et equ(83)
		if(v.getName().equals(getFlowin().getName())){
			// derive selon flowin : -T3b_alfa ;
			return -getAlpha().getValue();
		}else{
			if(v.getName().equals(getPressure().getName())){
				// derive selon pression : - 1.0f
				return -1.0f;		
			}else{
				for(ElasticTube parent:getParents()){
					Variable pr = findVariableWithName(((Veinule)parent).getPressure().getName(),variables);
					if(v.getName().equals(pr.getName())){
						// derive selon pressionParent :  1.0f
						return 1.0f;		
					}
				}
				return 0.0f;
			}
		}
	}


	private String getSymbolicContinuityDerivative(Variable v, ArrayList<Variable> variables){
		// equ(77) et equ(81)

		if(v.getName().equals(getFlowin().getName())){
			// derive selon flowin : 1;
			return ""+1.0f;
		}else{
			if(v.getName().equals(getFlowout().getName())){
				// derive selon flowout : -1
				return "-"+1.0f;		
			}else{
				return ""+0.0f;
			}
		}
	}

	private String getSymbolicDistensibilityDerivative(Variable v, ArrayList<Variable> variables){
		// equ(78) et equ(82)

		if(v.getName().equals(getArea().getName())){
			// derive selon area : - T3b_E * (1/T3b_A0);
			return "- "+getElastance().getName()+" * ("+1.0f+"/"+getInitialArea().getName()+")";
		}else{
			if(v.getName().equals(getPressure().getName())){
				// derive selon pression : 1.0f
				return ""+1.0f;
			}else{
				if(v.getName().equals(getAssociatedBrainParenchyma().getPressure().getName())){
					// derive selon pression brain : - 1 
					return "-"+1.0f;		
				}else{
					return ""+0.0f;
				}
			}
		}
	}

	private String getSymbolicMomentumDerivative(Variable v, ArrayList<Variable> variables) throws Exception{
		// equ(79) et equ(83)

		if(v.getName().equals(getFlowin().getName())){
			// derive selon flowin : -T3b_alfa ;
			return "-"+getAlpha().getName();
		}else{
			if(v.getName().equals(getPressure().getName())){
				// derive selon pression : - 1.0f
				return "-"+1.0f;		
			}else{
				for(ElasticTube parent:getParents()){
					Variable pr = findVariableWithName(((Veinule)parent).getPressure().getName(),variables);
					if(v.getName().equals(pr.getName())){
						// derive selon pressionParent :  1.0f
						return ""+1.0f;		
					}
				}
				return ""+0.0f;
			}
		}
	}

	
	/**
	 * Pour l'equation de connectivite du flux on fait la somme du flux en amont qui doit etre egale au flux in
	 * @param parentFlowout
	 * @param fi
	 * @return
	 */
	private float getConnectivityEquation(Variable fi){
		// equ(49) et equ(52)
		float res = 0;
		for(ElasticTube parent : getParents()){//for(Variable pf : parentFlowout){
			Vein par = ((Vein)parent);
			Variable pf = par.getFlowout();
			float fact = par.getChildrens().size();
			res += (pf.getValue()/fact);
		}
		return (res - fi.getValue());
	}
	
	private String getSymbolicConnectivityEquation(Variable fi){
		// equ(49) et equ(52)
		String res = "(";
		for(ElasticTube parent : getParents()){//for(Variable pf : parentFlowout){
			if(!res.equals("("))
				res += "+";
			Vein par = ((Vein)parent);
			Variable pf = par.getFlowout();
			float fact = par.getChildrens().size();
			res += "("+pf.getName()+"/"+fact+")";
		}
		res += ")";
		return "("+res+" - "+fi.getName()+")";
	}
	
	private float getConnectivityDerivative(Variable v, ArrayList<Variable> variables) throws Exception{
		// equ(49) et equ(52)
		if(v.getName().equals(getFlowin().getName())){
			// derive selon flowin : -1;
			return -1.0f;
		}else{
			for(ElasticTube parent:getParents()){
				Variable pr = findVariableWithName(((Vein)parent).getFlowout().getName(),variables);
				if(v.getName().equals(pr.getName())){
					// derive selon flowoutParent :  1.0f
					return 1.0f/(float)((Vein)parent).getChildrens().size();		
				}
			}
			return 0.0f;
		}
	}
	
	private String getSymbolicConnectivityDerivative(Variable v, ArrayList<Variable> variables) throws Exception{
		// equ(49) et equ(52)
		if(v.getName().equals(getFlowin().getName())){
			// derive selon flowin : -1;
			return "-"+1.0f;
		}else{
			for(ElasticTube parent:getParents()){
				Variable pr = findVariableWithName(((Vein)parent).getFlowout().getName(),variables);
				if(v.getName().equals(pr.getName())){
					// derive selon flowoutParent :  1.0f
					return ""+1.0f+"/"+(float)((Vein)parent).getChildrens().size();		
				}
			}
			return ""+0.0f;
		}
	}
}
