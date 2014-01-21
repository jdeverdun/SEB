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
	@Override
	public ArrayList<float[]> getInitialEquations(ArrayList<SimpleVariable> variables) throws Exception {
		ArrayList<float[]> res = new ArrayList<float[]>();

		// Continuity
		float[] continuity = new float[variables.size()+1];
		SimpleVariable ar = findVariableWithName(getArea().getName(),variables);
		SimpleVariable fi = findVariableWithName(getFlowin().getName(),variables);
		SimpleVariable fo = findVariableWithName(getFlowout().getName(),variables);
		continuity[0] = getInitialContinuityEquation(fi, fo);
		for(int i = 0; i<variables.size();i++){
			continuity[i+1] = getInitialContinuityDerivative(variables.get(i), variables);
		}
		res.add(continuity);
		// Distensibility
		float[] distensibility = new float[variables.size()+1];
		SimpleVariable pr = findVariableWithName(getPressure().getName(),variables);
		SimpleVariable pbrain = findVariableWithName(getAssociatedBrainParenchyma().getPressure().getName(),variables);
		distensibility[0] = getInitialDistensibilityEquation(ar, pr, pbrain);
		for(int i = 0; i<variables.size();i++){
			distensibility[i+1] = getInitialDistensibilityDerivative(variables.get(i), variables);
		}
		res.add(distensibility);

		// momentum
		float[] momentum = new float[variables.size()+1];
		momentum[0] = getInitialMomentumEquation(fi);
		for(int i = 0; i<variables.size();i++){
			momentum[i+1] = getInitialMomentumDerivative(variables.get(i), variables);
		}
		res.add(momentum);
		return res;
	}
	
	@Override
	public ArrayList<float[]> getEquations(ArrayList<SimpleVariable> variables) throws Exception {
		ArrayList<float[]> res = new ArrayList<float[]>();

		// Continuity
		float[] continuity = new float[variables.size()+1];
		SimpleVariable ar = findVariableWithName(getArea().getName(),variables);
		SimpleVariable fi = findVariableWithName(getFlowin().getName(),variables);
		SimpleVariable fo = findVariableWithName(getFlowout().getName(),variables);
		continuity[0] = getContinuityEquation(ar, fi, fo);
		for(int i = 0; i<variables.size();i++){
			continuity[i+1] = getContinuityDerivative(variables.get(i), variables);
		}
		res.add(continuity);
		// Distensibility
		float[] distensibility = new float[variables.size()+1];
		SimpleVariable pr = findVariableWithName(getPressure().getName(),variables);
		SimpleVariable pbrain = findVariableWithName(getAssociatedBrainParenchyma().getPressure().getName(),variables);
		distensibility[0] = getDistensibilityEquation(ar, pr, pbrain);
		for(int i = 0; i<variables.size();i++){
			distensibility[i+1] = getDistensibilityDerivative(variables.get(i), variables);
		}
		res.add(distensibility);

		// momentum
		float[] momentum = new float[variables.size()+1];
		momentum[0] = getMomentumEquation(fi);
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
	public ArrayList<String[]> getSymbolicInitialEquations(ArrayList<SimpleVariable> variables) throws Exception {
		ArrayList<String[]> res = new ArrayList<String[]>();

		// Continuity
		String[] continuity = new String[variables.size()+1];
		SimpleVariable ar = findVariableWithName(getArea().getName(),variables);
		SimpleVariable fi = findVariableWithName(getFlowin().getName(),variables);
		SimpleVariable fo = findVariableWithName(getFlowout().getName(),variables);
		continuity[0] = getSymbolicInitialContinuityEquation(fi, fo);
		for(int i = 0; i<variables.size();i++){
			continuity[i+1] = getSymbolicInitialContinuityDerivative(variables.get(i), variables);
		}
		res.add(continuity);
		// Distensibility
		String[] distensibility = new String[variables.size()+1];
		SimpleVariable pr = findVariableWithName(getPressure().getName(),variables);
		SimpleVariable pbrain = findVariableWithName(getAssociatedBrainParenchyma().getPressure().getName(),variables);
		distensibility[0] = getSymbolicInitialDistensibilityEquation(ar, pr, pbrain);
		for(int i = 0; i<variables.size();i++){
			distensibility[i+1] = getSymbolicInitialDistensibilityDerivative(variables.get(i), variables);
		}
		res.add(distensibility);

		// momentum
		String[] momentum = new String[variables.size()+1];
		momentum[0] = getSymbolicInitialMomentumEquation(fi);
		for(int i = 0; i<variables.size();i++){
			momentum[i+1] = getSymbolicInitialMomentumDerivative(variables.get(i), variables);
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
	public ArrayList<String[]> getSymbolicEquations(ArrayList<SimpleVariable> variables) throws Exception {
		ArrayList<String[]> res = new ArrayList<String[]>();

		// Continuity
		String[] continuity = new String[variables.size()+1];
		SimpleVariable ar = findVariableWithName(getArea().getName(),variables);
		SimpleVariable fi = findVariableWithName(getFlowin().getName(),variables);
		SimpleVariable fo = findVariableWithName(getFlowout().getName(),variables);
		continuity[0] = getSymbolicContinuityEquation(ar, fi, fo);
		for(int i = 0; i<variables.size();i++){
			continuity[i+1] = getSymbolicContinuityDerivative(variables.get(i), variables);
		}
		res.add(continuity);
		// Distensibility
		String[] distensibility = new String[variables.size()+1];
		SimpleVariable pr = findVariableWithName(getPressure().getName(),variables);
		SimpleVariable pbrain = findVariableWithName(getAssociatedBrainParenchyma().getPressure().getName(),variables);
		distensibility[0] = getSymbolicDistensibilityEquation(ar, pr, pbrain);
		for(int i = 0; i<variables.size();i++){
			distensibility[i+1] = getSymbolicDistensibilityDerivative(variables.get(i), variables);
		}
		res.add(distensibility);

		// momentum
		String[] momentum = new String[variables.size()+1];
		momentum[0] = getSymbolicMomentumEquation(fi);
		for(int i = 0; i<variables.size();i++){
			momentum[i+1] = getSymbolicMomentumDerivative(variables.get(i), variables);
		}
		res.add(momentum);
		return res;
	}


	private float getContinuityEquation(SimpleVariable ar, SimpleVariable fi, SimpleVariable fo){
		// equ(5) et equ(10)
		return (ar.getValue() - getArea().getValue())/ModelSpecification.dt.getValue() + (- fi.getValue() + fo.getValue())/getLength().getValue();
	}

	private float getDistensibilityEquation(SimpleVariable ar, SimpleVariable pr, SimpleVariable pbrain){
		// equ(20) et equ(25)
		return -ModelSpecification.damp.getValue() * (ar.getValue() - getArea().getValue())/ModelSpecification.dt.getValue() + (pr.getValue()-pbrain.getValue())-getElastance().getValue()*(ar.getValue()/getInitialArea().getValue()-1);
	}

	private float getMomentumEquation(SimpleVariable fi){
		// equ(35) et equ(40)
		return (fi.getValue() - (0.003f + ModelSpecification.OUT_D.getValue()[(int) ModelSpecification.currentIter.getValue()]));
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

	private String getSymbolicMomentumEquation(SimpleVariable fi){
		// equ(35) et equ(40)
		return "("+fi.getName()+" - ("+0.003f+" + "+ModelSpecification.OUT_D.getName()+"("+ModelSpecification.currentIter.getName()+")))";
	}

	// ------- Derive -----------
	private float getContinuityDerivative(SimpleVariable v, ArrayList<SimpleVariable> variables){
		// equ(5) et equ(10)

		if(v.getName().equals(getArea().getName())){
			// derive selon area : 1/"+ModelSpecification.dt.getName()+" 
			return 1.0f/ModelSpecification.dt.getValue();
		}else{
			if(v.getName().equals(getFlowin().getName())){
				// derive selon flowin : - 1/T4_l0;
				return -1.0f/getLength().getValue();
			}else{
				if(v.getName().equals(getFlowout().getName())){
					// derive selon flowout : 1/T4_l0
					return 1.0f/getLength().getValue();		
				}else{
					return 0.0f;
				}
			}
		}
	}

	private float getDistensibilityDerivative(SimpleVariable v, ArrayList<SimpleVariable> variables){
		// equ(20) et equ(25)

		if(v.getName().equals(getArea().getName())){
			// derive selon area : -damp/"+ModelSpecification.dt.getName()+" - T4_E * (1/T4_A0) 
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

	private float getMomentumDerivative(SimpleVariable v, ArrayList<SimpleVariable> variables) throws Exception{
		// equ(35) et equ(40)
		if(v.getName().equals(getFlowin().getName())){
			// derive selon flowin : 1 ;
			return 1.0f;
		}else{		
			return 0.0f;
		}
	}


	private String getSymbolicContinuityDerivative(SimpleVariable v, ArrayList<SimpleVariable> variables){
		// equ(5) et equ(10)

		if(v.getName().equals(getArea().getName())){
			// derive selon area : 1/"+ModelSpecification.dt.getName()+" 
			return "" + 1.0f+"/"+ModelSpecification.dt.getName()+"";
		}else{
			if(v.getName().equals(getFlowin().getName())){
				// derive selon flowin : - 1/T4_l0;
				return "-"+1.0f+"/"+getLength().getName();
			}else{
				if(v.getName().equals(getFlowout().getName())){
					// derive selon flowout : 1/T4_l0
					return ""+1.0f+"/"+getLength().getName();		
				}else{
					return ""+0.0f;
				}
			}
		}
	}

	private String getSymbolicDistensibilityDerivative(SimpleVariable v, ArrayList<SimpleVariable> variables){
		// equ(20) et equ(25)

		if(v.getName().equals(getArea().getName())){
			// derive selon area : -damp/"+ModelSpecification.dt.getName()+" - T4_E * (1/T4_A0) 
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

	private String getSymbolicMomentumDerivative(SimpleVariable v, ArrayList<SimpleVariable> variables) throws Exception{
		// equ(35) et equ(40)

		if(v.getName().equals(getFlowin().getName())){
			// derive selon flowin : 1 ;
			return ""+1.0f;
		}else{
			return ""+0.0f;
		}
	}
	
	// ================= init ========================

	private float getInitialContinuityEquation(SimpleVariable fi, SimpleVariable fo){
		// eq (5)  (10)
		return fi.getValue() - fo.getValue();
	}
	private float getInitialContinuityDerivative(SimpleVariable v, ArrayList<SimpleVariable> variables){
		// eq (5)  (10)
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
	private String getSymbolicInitialContinuityEquation(SimpleVariable fi, SimpleVariable fo){
		// eq (5)  (10)
		return fi.getName()+" - "+fo.getName();
	}
	private String getSymbolicInitialContinuityDerivative(SimpleVariable v, ArrayList<SimpleVariable> variables){
		// eq (5)  (10)
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
	private float getInitialDistensibilityEquation(SimpleVariable ar, SimpleVariable pr, SimpleVariable pbrain){
		// eq (20)  (25)
		return (pr.getValue() - pbrain.getValue()) - getElastance().getValue() * (ar.getValue()/getInitialArea().getValue() - 1);
	}
	private float getInitialDistensibilityDerivative(SimpleVariable v, ArrayList<SimpleVariable> variables){
		// eq (20)  (25)
		if(v.getName().equals(getArea().getName())){
			// derive selon area : - T4_E * (1/T4_A0) 
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
	private String getSymbolicInitialDistensibilityEquation(SimpleVariable ar, SimpleVariable pr, SimpleVariable pbrain){
		// eq (20)  (25)
		return "("+pr.getName()+" - "+pbrain.getName()+") - "+getElastance().getName()+" * ("+ar.getName()+"/"+getInitialArea().getName()+" - 1)";
	}
	private String getSymbolicInitialDistensibilityDerivative(SimpleVariable v, ArrayList<SimpleVariable> variables){
		// eq (20)  (25)
		if(v.getName().equals(getArea().getName())){
			// derive selon area : - T4_E * (1/T4_A0) 
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
	private float getInitialMomentumEquation(SimpleVariable fi){
		// equ(35) et equ(40)
		return (fi.getValue() - (0.003f));
	}
	private float getInitialMomentumDerivative(SimpleVariable v, ArrayList<SimpleVariable> variables) throws Exception{
		// equ(35) et equ(40)
		if(v.getName().equals(getFlowin().getName())){
			// derive selon flowin : 1 ;
			return 1.0f;
		}else{		
			return 0.0f;
		}
	}
	private String getSymbolicInitialMomentumEquation(SimpleVariable fi){
		// equ(35) et equ(40)
		return "("+fi.getName()+" - ("+0.003f+"))";
	}
	private String getSymbolicInitialMomentumDerivative(SimpleVariable v, ArrayList<SimpleVariable> variables) throws Exception{
		// equ(35) et equ(40)

		if(v.getName().equals(getFlowin().getName())){
			// derive selon flowin : 1 ;
			return ""+1.0f;
		}else{
			return ""+0.0f;
		}
	}
}
