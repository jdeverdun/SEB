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
		distensibility[0] = getDistensibilityEquation(ar, pr);
		for(int i = 0; i<variables.size();i++){
			distensibility[i+1] = getDistensibilityDerivative(variables.get(i), variables);
		}
		res.add(distensibility);

		// momentum
		for(ElasticTube parent:getParents()){
			Variable parentPressure = findVariableWithName(((SAS)parent).getPressure().getName(),variables);
			float[] momentum = new float[variables.size()+1];
			momentum[0] = getMomentumEquation(fi, ar, pr, parentPressure);
			for(int i = 0; i<variables.size();i++){
				momentum[i+1] = getMomentumDerivative(variables.get(i), variables);
			}
			res.add(momentum);
		}

		// Connectivity
		Variable sasFlowout = ((SAS)getParents().get(0)).getFlowout();
		Variable sasPressure = ((SAS)getParents().get(0)).getPressure();
		ArrayList<Variable> psin = findVariableWithStruct(Hemisphere.BOTH, VenousSinus.TUBE_NUM, PRESSURE_LABEL, variables);
		float[] connectivity = new float[variables.size()+1];
		connectivity[0] = getConnectivityEquation(sasFlowout, fi, sasPressure, psin.get(0));
		for(int i = 0; i<variables.size();i++){
			connectivity[i+1] = getConnectivityDerivative(variables.get(i), variables);
		}
		res.add(connectivity);

		//bilan connectivity
		float[] bilanconnectivity = new float[variables.size()+1];
		bilanconnectivity[0] = getBilanConnectivityEquation(fo);
		for(int i = 0; i<variables.size();i++){
			bilanconnectivity[i+1] = getBilanConnectivityDerivative(variables.get(i), variables);
		}
		res.add(bilanconnectivity);
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
		continuity[0] = getSymbolicContinuityEquation(ar, fi, fo);
		for(int i = 0; i<variables.size();i++){
			continuity[i+1] = getSymbolicContinuityDerivative(variables.get(i), variables);
		}
		res.add(continuity);
		// Distensibility
		String[] distensibility = new String[variables.size()+1];
		Variable pr = findVariableWithName(getPressure().getName(),variables);
		distensibility[0] = getSymbolicDistensibilityEquation(ar, pr);
		for(int i = 0; i<variables.size();i++){
			distensibility[i+1] = getSymbolicDistensibilityDerivative(variables.get(i), variables);
		}
		res.add(distensibility);

		// momentum
		for(ElasticTube parent:getParents()){
			Variable parentPressure = findVariableWithName(((SAS)parent).getPressure().getName(),variables);
			String[] momentum = new String[variables.size()+1];
			momentum[0] = getSymbolicMomentumEquation(fi, ar, pr, parentPressure);
			for(int i = 0; i<variables.size();i++){
				momentum[i+1] = getSymbolicMomentumDerivative(variables.get(i), variables);
			}
			res.add(momentum);
		}

		// Connectivity
		Variable sasFlowout = ((SAS)getParents().get(0)).getFlowout();
		Variable sasPressure = ((SAS)getParents().get(0)).getPressure();
		ArrayList<Variable> psin = findVariableWithStruct(Hemisphere.BOTH, VenousSinus.TUBE_NUM, PRESSURE_LABEL, variables);
		String[] connectivity = new String[variables.size()+1];
		connectivity[0] = getSymbolicConnectivityEquation(sasFlowout, fi, sasPressure, psin.get(0));
		for(int i = 0; i<variables.size();i++){
			connectivity[i+1] = getSymbolicConnectivityDerivative(variables.get(i), variables);
		}
		res.add(connectivity);

		//bilan connectivity
		String[] bilanconnectivity = new String[variables.size()+1];
		bilanconnectivity[0] = getSymbolicBilanConnectivityEquation(fo);
		for(int i = 0; i<variables.size();i++){
			bilanconnectivity[i+1] = getSymbolicBilanConnectivityDerivative(variables.get(i), variables);
		}
		res.add(bilanconnectivity);

		return res;
	}


	private float getContinuityEquation(Variable ar, Variable fi, Variable fo){
		// equ(15)
		return (ar.getValue() - getArea().getValue())/ModelSpecification.dt + (- fi.getValue() + fo.getValue())/getLength().getValue();
	}

	private float getDistensibilityEquation(Variable ar, Variable pr){
		// equ(30)
		return -ModelSpecification.damp * (ar.getValue() - getArea().getValue())/ModelSpecification.dt + (pr.getValue()- ModelSpecification.Pstar)-getElastance().getValue()*(ar.getValue()/getInitialArea().getValue()-1);
	}

	private float getMomentumEquation(Variable fi, Variable ar, Variable pr, Variable parentPressure){
		// equ(45)
		return ModelSpecification.damp2 * ((fi.getValue()/ar.getValue()) - (getFlowin().getValue()/getArea().getValue()))/ModelSpecification.dt + (parentPressure.getValue() - pr.getValue())-getAlpha().getValue()*fi.getValue();
	}

	// symbolic equation (en chaine de caractere)
	private String getSymbolicContinuityEquation(Variable ar, Variable fi, Variable fo){
		// equ(15)
		return "" + "("+ar.getName()+" - "+getArea().getName()+")/dt"+" + (- "+fi.getName()+"+"+ fo.getName()+")/"+getLength().getName();
	}

	private String getSymbolicDistensibilityEquation(Variable ar, Variable pr){
		// equ(30)
		return "-damp * ("+ar.getName()+" - "+getArea().getName()+")/dt + ("+pr.getName()+"- Pstar)-"+getElastance().getName()+"*("+ar.getName()+"/"+getInitialArea().getName()+"-1)";
	}

	private String getSymbolicMomentumEquation(Variable fi, Variable ar, Variable pr, Variable parentPressure){
		// equ(45)
		return " damp2 * (("+fi.getName()+" / "+ar.getName()+" ) - ("+getFlowin().getName()+" / "+getArea().getName()+" ))/ dt + ("+parentPressure.getName()+"-"+pr.getName()+" )-"+getAlpha().getName()+" * "+fi.getName();
	}

	// ------- Derive -----------
	private float getContinuityDerivative(Variable v, ArrayList<Variable> variables){
		// equ(15)

		if(v.getName().equals(getArea().getName())){
			// derive selon area : 1/dt 
			return 1.0f/ModelSpecification.dt;
		}else{
			if(v.getName().equals(getFlowin().getName())){
				// derive selon flowin : - 1/T9_l0;
				return -1.0f/getLength().getValue();
			}else{
				if(v.getName().equals(getFlowout().getName())){
					// derive selon flowout : 1/T9_l0
					return 1.0f/getLength().getValue();		
				}else{
					return 0.0f;
				}
			}
		}
	}

	private float getDistensibilityDerivative(Variable v, ArrayList<Variable> variables) throws Exception{
		// equ(30)
		if(v.getName().equals(getArea().getName())){  
			// derive selon area : - damp /dt  -T9_E * (1/T9_A0);
			return -ModelSpecification.damp/ModelSpecification.dt-getElastance().getValue()*(1.0f/getInitialArea().getValue());
		}else{
			if(v.getName().equals(getPressure().getName())){
				// derive selon pression : 1.0f
				return 1.0f;
			}else{
				return 0.0f;
			}
		}
	}

	private float getMomentumDerivative(Variable v, ArrayList<Variable> variables) throws Exception{
		// equ(45)

		if(v.getName().equals(getFlowin().getName())){
			// derive selon flowin : damp2 * ((1/T9_A))/dt -T9_alfa ;
			Variable ar = findVariableWithName(getArea().getName(),variables);
			return ModelSpecification.damp2*(1/ar.getValue())/ModelSpecification.dt - getAlpha().getValue();
		}else{
			if(v.getName().equals(getArea().getName())){
				// derive selon area : damp2 * (-T9_fi/T9_A²)/dt
				Variable fi = findVariableWithName(getFlowin().getName(),variables);
				return (float) (ModelSpecification.damp2 * (-fi.getValue()/Math.pow(v.getValue(),2))/ModelSpecification.dt);
			}else{
				if(v.getName().equals(getPressure().getName())){
					// derive selon pression : - 1.0f
					return -1.0f;		
				}else{
					for(ElasticTube parent:getParents()){
						Variable pr = findVariableWithName(((Capillary)parent).getPressure().getName(),variables);
						if(v.getName().equals(pr.getName())){
							// derive selon pressionParent :  1.0f
							return 1.0f;		
						}
					}
					return 0.0f;
				}
			}
		}
	}


	private String getSymbolicContinuityDerivative(Variable v, ArrayList<Variable> variables){
		// equ(15)

		if(v.getName().equals(getArea().getName())){
			// derive selon area : 1/dt 
			return "" + 1.0f+"/dt";
		}else{
			if(v.getName().equals(getFlowin().getName())){
				// derive selon flowin : - 1/T9_l0;
				return "-"+1.0f+"/"+getLength().getName();
			}else{
				if(v.getName().equals(getFlowout().getName())){
					// derive selon flowout : 1/T9_l0
					return ""+1.0f+"/"+getLength().getName();		
				}else{
					return ""+0.0f;
				}
			}
		}
	}

	private String getSymbolicDistensibilityDerivative(Variable v, ArrayList<Variable> variables) throws Exception{
		// equ(30)

		if(v.getName().equals(getArea().getName())){
			// derive selon area : - damp /dt  -T9_E * (1/T9_A0);
			return "-damp/dt-"+getElastance().getName()+"*("+1.0f+"/"+getInitialArea().getName()+")";
		}else{
			if(v.getName().equals(getPressure().getName())){
				// derive selon pression : 1.0f
				return ""+1.0f;
			}else{
				return ""+0.0f;
			}
		}
	}

	private String getSymbolicMomentumDerivative(Variable v, ArrayList<Variable> variables) throws Exception{
		// equ(45)

		if(v.getName().equals(getFlowin().getName())){
			// derive selon flowin : damp2 * ((1/T7_A))/dt -T7_alfa ;
			Variable ar = findVariableWithName(getArea().getName(),variables);
			return "damp2*(1/"+ar.getName()+")/dt - "+getAlpha().getName();
		}else{
			if(v.getName().equals(getArea().getName())){
				// derive selon area : damp2 * (-T7_fi/T7_A²)/dt
				Variable fi = findVariableWithName(getFlowin().getName(),variables);
				return "(damp2 * (-"+fi.getName()+"/"+v.getName()+"^2)/dt)";
			}else{
				if(v.getName().equals(getPressure().getName())){
					// derive selon pression : - 1.0f
					return ""+-1.0f;		
				}else{
					for(ElasticTube parent:getParents()){
						Variable pr = findVariableWithName(((Capillary)parent).getPressure().getName(),variables);
						if(v.getName().equals(pr.getName())){
							// derive selon pressionParent :  1.0f
							return ""+1.0f;		
						}
					}
					return ""+0.0f;
				}
			}
		}
	}


	//====== Connectivity ====
	/**
	 * Pour l'equation de connectivite du flux on fait la somme du flux en amont qui doit etre egale au flux in
	 * @param parentFlowout
	 * @param fi
	 * @return
	 */
	private float getConnectivityEquation(Variable sasFlowout, Variable fi, Variable sasPressure, Variable vsinuspr){
		// equ(58)
		float res = sasFlowout.getValue() - (fi.getValue() + ModelSpecification.k1 * (sasPressure.getValue() - vsinuspr.getValue()));
		return res;
	}

	private String getSymbolicConnectivityEquation(Variable sasFlowout, Variable fi, Variable sasPressure, Variable vsinuspr){
		// equ(58)
		return ""+sasFlowout.getName()+" - ("+fi.getName()+" + k1 * ("+sasPressure.getName()+" - "+vsinuspr.getName()+"))";
	}

	private float getConnectivityDerivative(Variable v, ArrayList<Variable> variables) throws Exception{
		// equ(58)
		if(v.getName().equals(getFlowin().getName())){
			// derive selon flowin : -1;
			return -1.0f;
		}else{
			if(v.getName().equals(((SAS)getParents().get(0)).getFlowout())){
				// derive selon la sas flowout : 1;
				return 1.0f;
			}else{
				if(v.getName().equals(((SAS)getParents().get(0)).getPressure())){
					// derive selon la sas sas pression : -k1;
					return -ModelSpecification.k1;
				}else{
					ArrayList<Variable> psin = findVariableWithStruct(Hemisphere.BOTH, VenousSinus.TUBE_NUM, PRESSURE_LABEL, variables);
					if(v.getName().equals(psin.get(0).getName())){
						// derive selon la pression vsinus : k1;
						return ModelSpecification.k1;
					}else{
						return 0.0f;
					}
				}
			}
		}
	}

	private String getSymbolicConnectivityDerivative(Variable v, ArrayList<Variable> variables) throws Exception{
		// equ(58)
		if(v.getName().equals(getFlowin().getName())){
			// derive selon flowin : -1;
			return "-"+1.0f;
		}else{
			if(v.getName().equals(((SAS)getParents().get(0)).getFlowout())){
				// derive selon la sas flowout : 1;
				return ""+1.0f;
			}else{
				if(v.getName().equals(((SAS)getParents().get(0)).getPressure())){
					// derive selon la sas sas pression : -k1;
					return "-k1";
				}else{
					ArrayList<Variable> psin = findVariableWithStruct(Hemisphere.BOTH, VenousSinus.TUBE_NUM, PRESSURE_LABEL, variables);
					if(v.getName().equals(psin.get(0).getName())){
						// derive selon la pression vsinus : k1;
						return "k1";
					}else{
						return ""+0.0f;
					}
				}
			}
		}
	}

	//====== Blilan Connectivity ====

	private float getBilanConnectivityEquation(Variable fo){
		// equ(59)
		return fo.getValue() - 0.0f;
	}

	private String getSymbolicBilanConnectivityEquation(Variable fo){
		// equ(59)
		return ""+fo.getName()+" - "+0.0f;
	}

	private float getBilanConnectivityDerivative(Variable v, ArrayList<Variable> variables) throws Exception{
		// equ(59)
		if(v.getName().equals(getFlowout().getName())){
			// derive selon flowout : 1;
			return 1.0f;
		}else{
			return 0.0f;
		}
	}

	private String getSymbolicBilanConnectivityDerivative(Variable v, ArrayList<Variable> variables) throws Exception{
		// equ(59)
		if(v.getName().equals(getFlowout().getName())){
			// derive selon flowout : 1;
			return ""+1.0f;
		}else{
			return ""+0.0f;
		}
	}

}
