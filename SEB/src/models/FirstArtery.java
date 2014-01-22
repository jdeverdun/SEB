package models;

import java.util.ArrayList;

import params.ModelSpecification;

public class FirstArtery extends ElasticTube {
	public static final String TUBE_NUM = "I";
	public static final Hemisphere DEFAULT_HEMI = Hemisphere.NONE;
	public static final float DEFAULT_LENGTH = 0.5f;
	public static final float DEFAULT_AREA = 3.42f;
	public static final float DEFAULT_ELASTANCE = 1066579.2f;// en Pa
	public static final float DEFAULT_ALPHA = 0.1618175f * 1333.2240f;
	public static final float DEFAULT_FLOWIN = 12.4f;
	public static final float DEFAULT_FLOWOUT = 12.4f;
	public static final float DEFAULT_PRESSURE = 133322.4f;

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
		SimpleVariable pbrain_left = findVariableWithName(ModelSpecification.architecture.getBrain().getLeftHemi().getPressure().getName(),variables);
		SimpleVariable pbrain_right = findVariableWithName(ModelSpecification.architecture.getBrain().getRightHemi().getPressure().getName(),variables);
		distensibility[0] = getInitialDistensibilityEquation(ar, pr, pbrain_left, pbrain_right);
		for(int i = 0; i<variables.size();i++){
			distensibility[i+1] = getInitialDistensibilityDerivative(variables.get(i), variables);
		}
		res.add(distensibility);

		// momentum
		float[] momentum = new float[variables.size()+1];
		momentum[0] = getInitialMomentumEquation(pr,fi);
		for(int i = 0; i<variables.size();i++){
			momentum[i+1] = getInitialMomentumDerivative(variables.get(i), variables);
		}
		res.add(momentum);

		// Connectivity
		ArrayList<SimpleVariable> childFin = new ArrayList<SimpleVariable>();
		for(ElasticTube child:getChildren()){
			childFin.add(findVariableWithName(((Artery)child).getFlowin().getName(),variables));
		}
		float[] connectivity = new float[variables.size()+1];
		connectivity[0] = getInitialConnectivityEquation(childFin, fo);
		for(int i = 0; i<variables.size();i++){
			connectivity[i+1] = getInitialConnectivityDerivative(variables.get(i), variables);
		}
		res.add(connectivity);


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
		continuity[0] = getContinuityEquation(fi, fo);
		for(int i = 0; i<variables.size();i++){
			continuity[i+1] = getContinuityDerivative(variables.get(i), variables);
		}
		res.add(continuity);
		// Distensibility
		float[] distensibility = new float[variables.size()+1];
		SimpleVariable pr = findVariableWithName(getPressure().getName(),variables);
		SimpleVariable pbrain_left = findVariableWithName(ModelSpecification.architecture.getBrain().getLeftHemi().getPressure().getName(),variables);
		SimpleVariable pbrain_right = findVariableWithName(ModelSpecification.architecture.getBrain().getRightHemi().getPressure().getName(),variables);
		distensibility[0] = getDistensibilityEquation(ar, pr, pbrain_left, pbrain_right);
		for(int i = 0; i<variables.size();i++){
			distensibility[i+1] = getDistensibilityDerivative(variables.get(i), variables);
		}
		res.add(distensibility);

		// momentum
		float[] momentum = new float[variables.size()+1];
		momentum[0] = getMomentumEquation(pr,fi);
		for(int i = 0; i<variables.size();i++){
			momentum[i+1] = getMomentumDerivative(variables.get(i), variables);
		}
		res.add(momentum);

		// Connectivity
		ArrayList<SimpleVariable> childFin = new ArrayList<SimpleVariable>();
		for(ElasticTube child:getChildren()){
			childFin.add(findVariableWithName(((Artery)child).getFlowin().getName(),variables));
		}
		float[] connectivity = new float[variables.size()+1];
		connectivity[0] = getConnectivityEquation(childFin, fo);
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
		SimpleVariable pbrain_left = findVariableWithName(ModelSpecification.architecture.getBrain().getLeftHemi().getPressure().getName(),variables);
		SimpleVariable pbrain_right = findVariableWithName(ModelSpecification.architecture.getBrain().getRightHemi().getPressure().getName(),variables);
		distensibility[0] = getSymbolicInitialDistensibilityEquation(ar, pr, pbrain_left, pbrain_right);
		for(int i = 0; i<variables.size();i++){
			distensibility[i+1] = getSymbolicInitialDistensibilityDerivative(variables.get(i), variables);
		}
		res.add(distensibility);

		// momentum
		String[] momentum = new String[variables.size()+1];
		momentum[0] = getSymbolicInitialMomentumEquation(pr,fi);
		for(int i = 0; i<variables.size();i++){
			momentum[i+1] = getSymbolicInitialMomentumDerivative(variables.get(i), variables);
		}
		res.add(momentum);

		// Connectivity
		ArrayList<SimpleVariable> childFin = new ArrayList<SimpleVariable>();
		for(ElasticTube child:getChildren()){
			childFin.add(findVariableWithName(((Artery)child).getFlowin().getName(),variables));
		}
		String[] connectivity = new String[variables.size()+1];
		connectivity[0] = getSymbolicInitialConnectivityEquation(childFin, fo);
		for(int i = 0; i<variables.size();i++){
			connectivity[i+1] = getSymbolicInitialConnectivityDerivative(variables.get(i), variables);
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
	public ArrayList<String[]> getSymbolicEquations(ArrayList<SimpleVariable> variables) throws Exception {
		ArrayList<String[]> res = new ArrayList<String[]>();

		// Continuity
		String[] continuity = new String[variables.size()+1];
		SimpleVariable ar = findVariableWithName(getArea().getName(),variables);
		SimpleVariable fi = findVariableWithName(getFlowin().getName(),variables);
		SimpleVariable fo = findVariableWithName(getFlowout().getName(),variables);
		continuity[0] = getSymbolicContinuityEquation(fi, fo);
		for(int i = 0; i<variables.size();i++){
			continuity[i+1] = getSymbolicContinuityDerivative(variables.get(i), variables);
		}
		res.add(continuity);
		// Distensibility
		String[] distensibility = new String[variables.size()+1];
		SimpleVariable pr = findVariableWithName(getPressure().getName(),variables);
		SimpleVariable pbrain_left = findVariableWithName(ModelSpecification.architecture.getBrain().getLeftHemi().getPressure().getName(),variables);
		SimpleVariable pbrain_right = findVariableWithName(ModelSpecification.architecture.getBrain().getRightHemi().getPressure().getName(),variables);
		distensibility[0] = getSymbolicDistensibilityEquation(ar, pr, pbrain_left, pbrain_right);
		for(int i = 0; i<variables.size();i++){
			distensibility[i+1] = getSymbolicDistensibilityDerivative(variables.get(i), variables);
		}
		res.add(distensibility);

		// momentum
		String[] momentum = new String[variables.size()+1];
		momentum[0] = getSymbolicMomentumEquation(pr,fi);
		for(int i = 0; i<variables.size();i++){
			momentum[i+1] = getSymbolicMomentumDerivative(variables.get(i), variables);
		}
		res.add(momentum);

		// Connectivity
		ArrayList<SimpleVariable> childFin = new ArrayList<SimpleVariable>();
		for(ElasticTube child:getChildren()){
			childFin.add(findVariableWithName(((Artery)child).getFlowin().getName(),variables));
		}
		String[] connectivity = new String[variables.size()+1];
		connectivity[0] = getSymbolicConnectivityEquation(childFin, fo);
		for(int i = 0; i<variables.size();i++){
			connectivity[i+1] = getSymbolicConnectivityDerivative(variables.get(i), variables);
		}
		res.add(connectivity);

		return res;
	}


	private float getContinuityEquation(SimpleVariable fi, SimpleVariable fo){
		// equ(73)
		return fi.getValue() - fo.getValue();
	}

	private float getDistensibilityEquation(SimpleVariable ar, SimpleVariable pr, SimpleVariable pbrain_left, SimpleVariable pbrain_right){
		// equ(74)
		return (pr.getValue() - 0.5f * (pbrain_right.getValue() + pbrain_left.getValue())) - getElastance().getValue() * (ar.getValue()/getInitialArea().getValue()-1.0f);
	}

	private float getMomentumEquation(SimpleVariable pr, SimpleVariable fi){
		// equ(75)
		return (ModelSpecification.P_INIT.getValue()[(int) ModelSpecification.currentIter.getValue()] - pr.getValue()) - getAlpha().getValue() * fi.getValue();
	}

	// symbolic equation (en chaine de caractere)
	private String getSymbolicContinuityEquation(SimpleVariable fi, SimpleVariable fo){
		// equ(73)
		return "" +fi.getName()+" - "+fo.getName();
	}

	private String getSymbolicDistensibilityEquation(SimpleVariable ar, SimpleVariable pr, SimpleVariable pbrain_left, SimpleVariable pbrain_right){
		// equ(74)
		return "("+pr.getName()+" - "+0.5f+" * ("+pbrain_right.getName()+" + "+pbrain_left.getName()+")) - "+getElastance().getName()+" * ("+ar.getName()+"/"+getInitialArea().getName()+"-"+1.0f+")";
	}

	private String getSymbolicMomentumEquation(SimpleVariable pr, SimpleVariable fi){
		// equ(75)
		return "("+ModelSpecification.P_INIT.getName()+"("+ModelSpecification.currentIter.getName()+")- "+pr.getName()+") - "+getAlpha().getName()+" * "+fi.getName();
	}

	// ------- Derive -----------
	private float getContinuityDerivative(SimpleVariable v, ArrayList<SimpleVariable> variables){
		// equ(73)
		if(v.getName().equals(getFlowin().getName())){
			// derive selon fin : 1 
			return 1.0f;
		}else{
			if(v.getName().equals(getFlowout().getName())){
				// derive selon flowout : - 1;
				return -1.0f;
			}else{
				return 0.0f;
			}
		}
	}

	private float getDistensibilityDerivative(SimpleVariable v, ArrayList<SimpleVariable> variables) throws Exception{
		// equ(74)

		if(v.getName().equals(getArea().getName())){
			// derive selon area : - TI_E * (1/TI_A0) ;
			return -getElastance().getValue()*(1/getInitialArea().getValue());
		}else{
			if(v.getName().equals(getPressure().getName())){
				// derive selon pression : 1.0f
				return 1.0f;
			}else{
				SimpleVariable pbrain_left = findVariableWithName(ModelSpecification.architecture.getBrain().getLeftHemi().getPressure().getName(),variables);
				if(v.getName().equals(pbrain_left.getName())){
					// derive selon pression brain left : - 0.5
					return -0.5f;		
				}else{
					SimpleVariable pbrain_right = findVariableWithName(ModelSpecification.architecture.getBrain().getRightHemi().getPressure().getName(),variables);
					if(v.getName().equals(pbrain_right.getName())){
						// derive selon pression brain right : - 0.5
						return -0.5f;
					}else{
						return 0.0f;
					}

				}
			}
		}
	}

	private float getMomentumDerivative(SimpleVariable v, ArrayList<SimpleVariable> variables) throws Exception{
		// equ(75)
		if(v.getName().equals(getPressure().getName())){
			// derive selon pression : - 1.0f
			return -1.0f;		
		}else{
			if(v.getName().equals(getFlowin().getName())){
				// derive selon parent pressure : - TI_alfa 
				return -getAlpha().getValue();
			}else{
				return 0.0f;
			}
		}
	}


	private String getSymbolicContinuityDerivative(SimpleVariable v, ArrayList<SimpleVariable> variables){
		// equ(73)
		if(v.getName().equals(getFlowin().getName())){
			// derive selon fin : 1 
			return ""+1.0f;
		}else{
			if(v.getName().equals(getFlowout().getName())){
				// derive selon flowout : - 1;
				return "-"+1.0f;
			}else{
				return ""+0.0f;
			}
		}
	}

	private String getSymbolicDistensibilityDerivative(SimpleVariable v, ArrayList<SimpleVariable> variables) throws Exception{
		// equ(74)
		if(v.getName().equals(getArea().getName())){
			// derive selon area : - TI_E * (1/TI_A0) ;
			return "-"+getElastance().getName()+"*(1/"+getInitialArea().getName()+")";
		}else{
			if(v.getName().equals(getPressure().getName())){
				// derive selon pression : 1.0f
				return ""+1.0f;
			}else{
				SimpleVariable pbrain_left = findVariableWithName(ModelSpecification.architecture.getBrain().getLeftHemi().getPressure().getName(),variables);
				if(v.getName().equals(pbrain_left.getName())){
					// derive selon pression brain left : - 0.5
					return "-"+0.5f;		
				}else{
					SimpleVariable pbrain_right = findVariableWithName(ModelSpecification.architecture.getBrain().getRightHemi().getPressure().getName(),variables);
					if(v.getName().equals(pbrain_right.getName())){
						// derive selon pression brain right : - 0.5
						return "-"+0.5f;
					}else{
						return ""+0.0f;
					}

				}
			}
		}
	}

	private String getSymbolicMomentumDerivative(SimpleVariable v, ArrayList<SimpleVariable> variables) throws Exception{
		// equ(75)
		if(v.getName().equals(getPressure().getName())){
			// derive selon pression : - 1.0f
			return "-"+1.0f;		
		}else{
			if(v.getName().equals(getFlowin().getName())){
				// derive selon parent pressure : - TI_alfa 
				return "-"+getAlpha().getName();
			}else{
				return ""+0.0f;
			}
		}
	}

	//====== Connectivity ====

	private float getConnectivityEquation(ArrayList<SimpleVariable> childFin, SimpleVariable fo){
		// equ(76)
		float res = 0;
		for(SimpleVariable pf : childFin){
			res += pf.getValue();
		}
		return fo.getValue() - res;
	}

	private String getSymbolicConnectivityEquation(ArrayList<SimpleVariable> childFin, SimpleVariable fo){
		// equ(76)
		String res = "";
		for(SimpleVariable pf : childFin){
			if(!res.equals(""))
				res += "+";
			res += pf.getName();
		}
		return ""+fo.getName()+" - ("+res+")";
	}

	private float getConnectivityDerivative(SimpleVariable v, ArrayList<SimpleVariable> variables) throws Exception{
		// equ(76)
		if(v.getName().equals(getFlowout().getName())){
			// derive selon flowout : 1;
			return 1.0f;
		}else{
			for(ElasticTube child:getChildren()){
				SimpleVariable fi = findVariableWithName(((Artery)child).getFlowin().getName(),variables);
				if(v.getName().equals(fi.getName())){
					// derive selon flowin children :  -1.0f
					return -1.0f;		
				}
			}
			return 0.0f;
		}
	}

	private String getSymbolicConnectivityDerivative(SimpleVariable v, ArrayList<SimpleVariable> variables) throws Exception{
		// equ(76)
		if(v.getName().equals(getFlowout().getName())){
			// derive selon flowout : 1;
			return ""+1.0f;
		}else{
			for(ElasticTube child:getChildren()){
				SimpleVariable fi = findVariableWithName(((Artery)child).getFlowin().getName(),variables);
				if(v.getName().equals(fi.getName())){
					// derive selon flowin children :  -1.0f
					return "-"+1.0f;		
				}
			}
			return ""+0.0f;
		}
	}

	
	// ================ init ======================
	private float getInitialContinuityEquation(SimpleVariable fi, SimpleVariable fo){
		// equ(73)
		return fi.getValue() - fo.getValue();
	}

	private float getInitialDistensibilityEquation(SimpleVariable ar, SimpleVariable pr, SimpleVariable pbrain_left, SimpleVariable pbrain_right){
		// equ(74)
		return (pr.getValue() - 0.5f * (pbrain_right.getValue() + pbrain_left.getValue())) - getElastance().getValue() * (ar.getValue()/getInitialArea().getValue()-1.0f);
	}

	private float getInitialMomentumEquation(SimpleVariable pr, SimpleVariable fi){
		// equ(75)
		return (ModelSpecification.P_INIT_INITIAL.getValue() - pr.getValue()) - getAlpha().getValue() * fi.getValue();
	}

	// symbolic equation (en chaine de caractere)
	private String getSymbolicInitialContinuityEquation(SimpleVariable fi, SimpleVariable fo){
		// equ(73)
		return "" +fi.getName()+" - "+fo.getName();
	}

	private String getSymbolicInitialDistensibilityEquation(SimpleVariable ar, SimpleVariable pr, SimpleVariable pbrain_left, SimpleVariable pbrain_right){
		// equ(74)
		return "("+pr.getName()+" - "+0.5f+" * ("+pbrain_right.getName()+" + "+pbrain_left.getName()+")) - "+getElastance().getName()+" * ("+ar.getName()+"/"+getInitialArea().getName()+"-"+1.0f+")";
	}

	private String getSymbolicInitialMomentumEquation(SimpleVariable pr, SimpleVariable fi){
		// equ(75)
		return "("+ModelSpecification.P_INIT_INITIAL.getName()+" - "+pr.getName()+") - "+getAlpha().getName()+" * "+fi.getName();
	}

	// ------- Derive -----------
	private float getInitialContinuityDerivative(SimpleVariable v, ArrayList<SimpleVariable> variables){
		// equ(73)
		if(v.getName().equals(getFlowin().getName())){
			// derive selon fin : 1 
			return 1.0f;
		}else{
			if(v.getName().equals(getFlowout().getName())){
				// derive selon flowout : - 1;
				return -1.0f;
			}else{
				return 0.0f;
			}
		}
	}

	private float getInitialDistensibilityDerivative(SimpleVariable v, ArrayList<SimpleVariable> variables) throws Exception{
		// equ(74)

		if(v.getName().equals(getArea().getName())){
			// derive selon area : - TI_E * (1/TI_A0) ;
			return -getElastance().getValue()*(1/getInitialArea().getValue());
		}else{
			if(v.getName().equals(getPressure().getName())){
				// derive selon pression : 1.0f
				return 1.0f;
			}else{
				SimpleVariable pbrain_left = findVariableWithName(ModelSpecification.architecture.getBrain().getLeftHemi().getPressure().getName(),variables);
				if(v.getName().equals(pbrain_left.getName())){
					// derive selon pression brain left : - 0.5
					return -0.5f;		
				}else{
					SimpleVariable pbrain_right = findVariableWithName(ModelSpecification.architecture.getBrain().getRightHemi().getPressure().getName(),variables);
					if(v.getName().equals(pbrain_right.getName())){
						// derive selon pression brain right : - 0.5
						return -0.5f;
					}else{
						return 0.0f;
					}

				}
			}
		}
	}

	private float getInitialMomentumDerivative(SimpleVariable v, ArrayList<SimpleVariable> variables) throws Exception{
		// equ(75)
		if(v.getName().equals(getPressure().getName())){
			// derive selon pression : - 1.0f
			return -1.0f;		
		}else{
			if(v.getName().equals(getFlowin().getName())){
				// derive selon parent pressure : - TI_alfa 
				return -getAlpha().getValue();
			}else{
				return 0.0f;
			}
		}
	}


	private String getSymbolicInitialContinuityDerivative(SimpleVariable v, ArrayList<SimpleVariable> variables){
		// equ(73)
		if(v.getName().equals(getFlowin().getName())){
			// derive selon fin : 1 
			return ""+1.0f;
		}else{
			if(v.getName().equals(getFlowout().getName())){
				// derive selon flowout : - 1;
				return "-"+1.0f;
			}else{
				return ""+0.0f;
			}
		}
	}

	private String getSymbolicInitialDistensibilityDerivative(SimpleVariable v, ArrayList<SimpleVariable> variables) throws Exception{
		// equ(74)
		if(v.getName().equals(getArea().getName())){
			// derive selon area : - TI_E * (1/TI_A0) ;
			return "-"+getElastance().getName()+"*(1/"+getInitialArea().getName()+")";
		}else{
			if(v.getName().equals(getPressure().getName())){
				// derive selon pression : 1.0f
				return ""+1.0f;
			}else{
				SimpleVariable pbrain_left = findVariableWithName(ModelSpecification.architecture.getBrain().getLeftHemi().getPressure().getName(),variables);
				if(v.getName().equals(pbrain_left.getName())){
					// derive selon pression brain left : - 0.5
					return "-"+0.5f;		
				}else{
					SimpleVariable pbrain_right = findVariableWithName(ModelSpecification.architecture.getBrain().getRightHemi().getPressure().getName(),variables);
					if(v.getName().equals(pbrain_right.getName())){
						// derive selon pression brain right : - 0.5
						return "-"+0.5f;
					}else{
						return ""+0.0f;
					}

				}
			}
		}
	}

	private String getSymbolicInitialMomentumDerivative(SimpleVariable v, ArrayList<SimpleVariable> variables) throws Exception{
		// equ(75)
		if(v.getName().equals(getPressure().getName())){
			// derive selon pression : - 1.0f
			return "-"+1.0f;		
		}else{
			if(v.getName().equals(getFlowin().getName())){
				// derive selon parent pressure : - TI_alfa 
				return "-"+getAlpha().getName();
			}else{
				return ""+0.0f;
			}
		}
	}

	//====== Connectivity ====

	private float getInitialConnectivityEquation(ArrayList<SimpleVariable> childFin, SimpleVariable fo){
		// equ(76)
		float res = 0;
		for(SimpleVariable pf : childFin){
			res += pf.getValue();
		}
		return fo.getValue() - res;
	}

	private String getSymbolicInitialConnectivityEquation(ArrayList<SimpleVariable> childFin, SimpleVariable fo){
		// equ(76)
		String res = "";
		for(SimpleVariable pf : childFin){
			if(!res.equals(""))
				res += "+";
			res += pf.getName();
		}
		return ""+fo.getName()+" - ("+res+")";
	}

	private float getInitialConnectivityDerivative(SimpleVariable v, ArrayList<SimpleVariable> variables) throws Exception{
		// equ(76)
		if(v.getName().equals(getFlowout().getName())){
			// derive selon flowout : 1;
			return 1.0f;
		}else{
			for(ElasticTube child:getChildren()){
				SimpleVariable fi = findVariableWithName(((Artery)child).getFlowin().getName(),variables);
				if(v.getName().equals(fi.getName())){
					// derive selon flowin children :  -1.0f
					return -1.0f;		
				}
			}
			return 0.0f;
		}
	}

	private String getSymbolicInitialConnectivityDerivative(SimpleVariable v, ArrayList<SimpleVariable> variables) throws Exception{
		// equ(76)
		if(v.getName().equals(getFlowout().getName())){
			// derive selon flowout : 1;
			return ""+1.0f;
		}else{
			for(ElasticTube child:getChildren()){
				SimpleVariable fi = findVariableWithName(((Artery)child).getFlowin().getName(),variables);
				if(v.getName().equals(fi.getName())){
					// derive selon flowin children :  -1.0f
					return "-"+1.0f;		
				}
			}
			return ""+0.0f;
		}
	}
}
