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
	@Override
	public ArrayList<float[]> getInitialEquations(ArrayList<Variable> variables) throws Exception {
		ArrayList<float[]> res = new ArrayList<float[]>();
		
		// Continuity
		float[] continuity = new float[variables.size()+1];
		Variable ar = findVariableWithName(getArea().getName(),variables);
		Variable fi = findVariableWithName(getFlowin().getName(),variables);
		Variable fo = findVariableWithName(getFlowout().getName(),variables);
		continuity[0] = getInitialContinuityEquation(fi, fo);
		for(int i = 0; i<variables.size();i++){
			continuity[i+1] = getInitialContinuityDerivative(variables.get(i), variables);
		}
		res.add(continuity);
		// Distensibility
		float[] distensibility = new float[variables.size()+1];
		Variable pr = findVariableWithName(getPressure().getName(),variables);
		Variable pbrain = findVariableWithName(getAssociatedBrainParenchyma().getPressure().getName(),variables);
		distensibility[0] = getInitialDistensibilityEquation(ar, pr, pbrain);
		for(int i = 0; i<variables.size();i++){
			distensibility[i+1] = getInitialDistensibilityDerivative(variables.get(i), variables);
		}
		res.add(distensibility);
		
		// momentum
		float[] momentum = new float[variables.size()+1];
		momentum[0] = getInitialMomentumEquation(fi, pr);
		for(int i = 0; i<variables.size();i++){
			momentum[i+1] = getInitialMomentumDerivative(variables.get(i), variables);
		}
		res.add(momentum);
		return res;
	}
	
	@Override
	public ArrayList<float[]> getEquations(ArrayList<Variable> variables) throws Exception {
		ArrayList<float[]> res = new ArrayList<float[]>();
		
		// Continuity
		float[] continuity = new float[variables.size()+1];
		Variable ar = findVariableWithName(getArea().getName(),variables);
		Variable fi = findVariableWithName(getFlowin().getName(),variables);
		Variable fo = findVariableWithName(getFlowout().getName(),variables);
		continuity[0] = getContinuityEquation(ar, fi, fo);
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
		float[] momentum = new float[variables.size()+1];
		momentum[0] = getMomentumEquation(fi, ar, pr);
		for(int i = 0; i<variables.size();i++){
			momentum[i+1] = getMomentumDerivative(variables.get(i), variables);
		}
		res.add(momentum);
		return res;
	}
	
	/**
	 * Renvoi les equations en format symbolic (en string)
	 * @param variables
	 * @return
	 * @throws Exception
	 */
	public ArrayList<String[]> getSymbolicInitialEquations(ArrayList<Variable> variables) throws Exception {
		ArrayList<String[]> res = new ArrayList<String[]>();
		
		// Continuity
		String[] continuity = new String[variables.size()+1];
		Variable ar = findVariableWithName(getArea().getName(),variables);
		Variable fi = findVariableWithName(getFlowin().getName(),variables);
		Variable fo = findVariableWithName(getFlowout().getName(),variables);
		continuity[0] = getSymbolicInitialContinuityEquation(fi, fo);
		for(int i = 0; i<variables.size();i++){
			continuity[i+1] = getSymbolicInitialContinuityDerivative(variables.get(i), variables);
		}
		res.add(continuity);
		// Distensibility
		String[] distensibility = new String[variables.size()+1];
		Variable pr = findVariableWithName(getPressure().getName(),variables);
		Variable pbrain = findVariableWithName(getAssociatedBrainParenchyma().getPressure().getName(),variables);
		distensibility[0] = getSymbolicInitialDistensibilityEquation(ar, pr, pbrain);
		for(int i = 0; i<variables.size();i++){
			distensibility[i+1] = getSymbolicInitialDistensibilityDerivative(variables.get(i), variables);
		}
		res.add(distensibility);
		
		// momentum
		String[] momentum = new String[variables.size()+1];
		momentum[0] = getSymbolicInitialMomentumEquation(fi, pr);
		for(int i = 0; i<variables.size();i++){
			momentum[i+1] = getSymbolicInitialMomentumDerivative(variables.get(i), variables);
		}
		res.add(momentum);
		return res;
	}
	
	/**
	 * Renvoi les equations initiales en format symbolic (en string)
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
		continuity[0] = getSymbolicContinuityEquation(ar, fi, fo);
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
		String[] momentum = new String[variables.size()+1];
		momentum[0] = getSymbolicMomentumEquation(fi, ar, pr);
		for(int i = 0; i<variables.size();i++){
			momentum[i+1] = getSymbolicMomentumDerivative(variables.get(i), variables);
		}
		res.add(momentum);
		return res;
	}
	
	private float getContinuityEquation(Variable ar, Variable fi, Variable fo){
		// equ(1) et equ(6)
		return (ar.getValue() - getArea().getValue())/ModelSpecification.dt.getValue() + (- fi.getValue() + fo.getValue())/getLength().getValue();
	}
	
	private float getDistensibilityEquation(Variable ar, Variable pr, Variable pbrain){
		// equ(16) et equ(21)
		return -ModelSpecification.damp.getValue() * (ar.getValue() - getArea().getValue())/ModelSpecification.dt.getValue() + (pr.getValue()-pbrain.getValue())-getElastance().getValue()*(ar.getValue()/getInitialArea().getValue()-1);
	}
	
	private float getMomentumEquation(Variable fi, Variable ar, Variable pr){
		// equ(31) et equ(36)
		return ModelSpecification.damp2.getValue() * ((fi.getValue()/ar.getValue()) - (getFlowin().getValue()/getArea().getValue()))/ModelSpecification.dt.getValue() + (ModelSpecification.P_INIT[(int) ModelSpecification.currentIter.getValue()] - pr.getValue())-getAlpha().getValue()*fi.getValue();
	}
	
	// symbolic equation (en chaine de caractere)
	private String getSymbolicContinuityEquation(Variable ar, Variable fi, Variable fo){
		// equ(1) et equ(6)
		return "" + "("+ar.getName()+" - "+getArea().getName()+LAST_ROUND_SUFFIX+")/"+ModelSpecification.dt.getName()+""+" + (- "+fi.getName()+"+"+ fo.getName()+")/"+getLength().getName();
	}
	
	private String getSymbolicDistensibilityEquation(Variable ar, Variable pr, Variable pbrain){
		// equ(16) et equ(21)
		return " -"+ModelSpecification.damp.getName()+" * ("+ar.getName()+" - "+getArea().getName()+LAST_ROUND_SUFFIX+" )/"+ModelSpecification.dt.getName()+" + ("+pr.getName()+"-"+pbrain.getName()+" )-"+getElastance().getName()+" * ("+ar.getName()+" / "+getInitialArea().getName()+" -1)";
	}
	
	private String getSymbolicMomentumEquation(Variable fi, Variable ar, Variable pr){
		// equ(31) et equ(36)
		return " "+ModelSpecification.damp2.getName()+" * (("+fi.getName()+" / "+ar.getName()+" ) - ("+getFlowin().getName()+LAST_ROUND_SUFFIX+" / "+getArea().getName()+LAST_ROUND_SUFFIX+" ))/ dt + ("+ModelSpecification.P_INIT_NAME+"("+ModelSpecification.currentIter.getName()+") - "+pr.getName()+" )-"+getAlpha().getName()+" * "+fi.getName();
	}
	
	// ------- Derive -----------
	private float getContinuityDerivative(Variable v, ArrayList<Variable> variables){
		// equ(1) et equ(6)
		
		if(v.getName().equals(getArea().getName())){
			// derive selon area : 1/dt 
			return 1.0f/ModelSpecification.dt.getValue();
		}else{
			if(v.getName().equals(getFlowin().getName())){
				// derive selon flowin : - 1/T0_l0;
				return -1.0f/getLength().getValue();
			}else{
				if(v.getName().equals(getFlowout().getName())){
					// derive selon flowout : 1/T0_l0
					return 1.0f/getLength().getValue();		
				}else{
					return 0.0f;
				}
			}
		}
	}
	
	private float getDistensibilityDerivative(Variable v, ArrayList<Variable> variables){
		// equ(16) et equ(21)
		
		if(v.getName().equals(getArea().getName())){
			// derive selon area : -damp/dt - T0_E * (1/T0_A0) 
			return -ModelSpecification.damp.getValue()/ModelSpecification.dt.getValue()-getElastance().getValue()*(1.0f/getInitialArea().getValue());
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
		// equ(31) et equ(36)
		
		if(v.getName().equals(getFlowin().getName())){
			// derive selon flowin : damp2 * ((1/R_T0_A))/dt -T0_alfa ;
			Variable ar = findVariableWithName(getArea().getName(),variables);
			return ModelSpecification.damp2.getValue()*(1/ar.getValue())/ModelSpecification.dt.getValue() - getAlpha().getValue();
		}else{
			if(v.getName().equals(getArea().getName())){
				// derive selon area : damp2 * (-R_T0_fi/R_T0_A²)/dt
				Variable fi = findVariableWithName(getFlowin().getName(),variables);
				return (float) (ModelSpecification.damp2.getValue() * (-fi.getValue()/Math.pow(v.getValue(),2))/ModelSpecification.dt.getValue());
			}else{
				if(v.getName().equals(getPressure().getName())){
					// derive selon pression : - 1.0f
					return -1.0f;		
				}else{
					return 0.0f;
				}
			}
		}
	}
	
	
	private String getSymbolicContinuityDerivative(Variable v, ArrayList<Variable> variables){
		// equ(1) et equ(6)
		
		if(v.getName().equals(getArea().getName())){
			// derive selon area : 1/dt 
			return "" + 1.0f+"/"+ModelSpecification.dt.getName();
		}else{
			if(v.getName().equals(getFlowin().getName())){
				// derive selon flowin : - 1/T0_l0;
				return "-"+1.0f+"/"+getLength().getName();
			}else{
				if(v.getName().equals(getFlowout().getName())){
					// derive selon flowout : 1/T0_l0
					return ""+1.0f+"/"+getLength().getName();		
				}else{
					return ""+0.0f;
				}
			}
		}
	}
	
	private String getSymbolicDistensibilityDerivative(Variable v, ArrayList<Variable> variables){
		// equ(16) et equ(21)
		
		if(v.getName().equals(getArea().getName())){
			// derive selon area : -damp/dt - T0_E * (1/T0_A0) 
			return "-"+ModelSpecification.damp.getName()+"/"+ModelSpecification.dt.getName()+"-"+getElastance().getName()+"*("+1.0f+"/"+getInitialArea().getName()+")";
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
		// equ(31) et equ(36)
		
		if(v.getName().equals(getFlowin().getName())){
			// derive selon flowin : damp2 * ((1/R_T0_A))/dt -T0_alfa ;
			Variable ar = findVariableWithName(getArea().getName(),variables);
			return ""+ModelSpecification.damp2.getName()+"*(1/"+ar.getName()+")/"+ModelSpecification.dt.getName()+" - "+getAlpha().getName();
		}else{
			if(v.getName().equals(getArea().getName())){
				// derive selon area : damp2 * (-R_T0_fi/R_T0_A²)/dt
				Variable fi = findVariableWithName(getFlowin().getName(),variables);
				return "("+ModelSpecification.damp2.getName()+" * (-"+fi.getName()+"/"+v.getName()+"^2)/"+ModelSpecification.dt.getName()+")";
			}else{
				if(v.getName().equals(getPressure().getName())){
					// derive selon pression : - 1.0f
					return ""+-1.0f;		
				}else{
					return ""+0.0f;
				}
			}
		}
	}
	
	// ================= init ========================
	
	private float getInitialContinuityEquation(Variable fi, Variable fo){
		// eq (1)  (6)
		return fi.getValue() - fo.getValue();
	}
	private float getInitialContinuityDerivative(Variable v, ArrayList<Variable> variables){
		// eq (1)  (6)
		if(v.getName().equals(getFlowin().getName())){
			// derive selon fin : 1
			return 1.0f;
		}else{
			if(v.getName().equals(getFlowout().getName())){
				// derive selon fin : -1
				return -1.0f;
			}else{
				return 0.0f;
			}
		}
	}
	private String getSymbolicInitialContinuityEquation(Variable fi, Variable fo){
		// eq (1)  (6)
		return fi.getName()+" - "+fo.getName();
	}
	private String getSymbolicInitialContinuityDerivative(Variable v, ArrayList<Variable> variables){
		// eq (1)  (6)
		if(v.getName().equals(getFlowin().getName())){
			// derive selon fin : 1
			return ""+1.0f;
		}else{
			if(v.getName().equals(getFlowout().getName())){
				// derive selon fin : -1
				return "-"+1.0f;
			}else{
				return ""+0.0f;
			}
		}
	}
	
	// distensibility
	private float getInitialDistensibilityEquation(Variable ar, Variable pr, Variable pbrain){
		// eq (16)  (21)
		return (pr.getValue() - pbrain.getValue()) - getElastance().getValue() * (ar.getValue()/getInitialArea().getValue() - 1);
	}
	private float getInitialDistensibilityDerivative(Variable v, ArrayList<Variable> variables){
		// eq (16)  (21)
		if(v.getName().equals(getArea().getName())){
			// derive selon area : - T0_E * (1/T0_A0) 
			return -getElastance().getValue()*(1.0f/getInitialArea().getValue());
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
	private String getSymbolicInitialDistensibilityEquation(Variable ar, Variable pr, Variable pbrain){
		// eq (16)  (21)
		return "("+pr.getName()+" - "+pbrain.getName()+") - "+getElastance().getName()+" * ("+ar.getName()+"/"+getInitialArea().getName()+" - 1)";
	}
	private String getSymbolicInitialDistensibilityDerivative(Variable v, ArrayList<Variable> variables){
		// eq (16)  (21)
		if(v.getName().equals(getArea().getName())){
			// derive selon area : - T0_E * (1/T0_A0) 
			return "-"+getElastance().getName()+"*("+1.0f+"/"+getInitialArea().getName()+")";
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

	// momentum
	private float getInitialMomentumEquation(Variable fi, Variable pr){
		// eq (31)  (36)
		return (ModelSpecification.P_INIT[(int) ModelSpecification.currentIter.getValue()] - pr.getValue())-getAlpha().getValue()*fi.getValue();
	}
	private float getInitialMomentumDerivative(Variable v, ArrayList<Variable> variables){
		// eq (31)  (36)
		if(v.getName().equals(getFlowin().getName())){
			// derive selon flowin : -T0_alfa ;
			return -getAlpha().getValue();
		}else{
			if(v.getName().equals(getPressure().getName())){
				// derive selon pression : - 1.0f
				return -1.0f;		
			}else{
				return 0.0f;
			}
		}
	}
	private String getSymbolicInitialMomentumEquation(Variable fi, Variable pr){
		// eq (31)  (36)
		return "("+ModelSpecification.P_INIT_NAME+"("+ModelSpecification.currentIter.getName()+") - "+pr.getName()+")-"+getAlpha().getName()+"*"+fi.getName();
	}
	private String getSymbolicInitialMomentumDerivative(Variable v, ArrayList<Variable> variables){
		// eq (31)  (36)
		if(v.getName().equals(getFlowin().getName())){
			// derive selon flowin : -T0_alfa ;
			return "-"+getAlpha().getValue();
		}else{
			if(v.getName().equals(getPressure().getName())){
				// derive selon pression : - 1.0f
				return "-"+1.0f;		
			}else{
				return ""+0.0f;
			}
		}
	}
}
