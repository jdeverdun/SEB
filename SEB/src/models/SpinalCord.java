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
		distensibility[0] = getInitialDistensibilityEquation(ar, pr);
		for(int i = 0; i<variables.size();i++){
			distensibility[i+1] = getInitialDistensibilityDerivative(variables.get(i), variables);
		}
		res.add(distensibility);

		// momentum
		for(ElasticTube parent:getParents()){
			SimpleVariable parentPressure = findVariableWithName(((SAS)parent).getPressure().getName(),variables);
			float[] momentum = new float[variables.size()+1];
			momentum[0] = getInitialMomentumEquation(fi, parentPressure, pr);
			for(int i = 0; i<variables.size();i++){
				momentum[i+1] = getInitialMomentumDerivative(variables.get(i), variables);
			}
			res.add(momentum);
		}

		// Connectivity
		SimpleVariable sasFlowout = ((SAS)getParents().get(0)).getFlowout();
		SimpleVariable sasPressure = ((SAS)getParents().get(0)).getPressure();
		ArrayList<SimpleVariable> psin = findVariableWithStruct(Hemisphere.BOTH, VenousSinus.TUBE_NUM, PRESSURE_LABEL, variables);
		float[] connectivity = new float[variables.size()+1];
		connectivity[0] = getInitialConnectivityEquation(sasFlowout, fi, sasPressure, psin.get(0));
		for(int i = 0; i<variables.size();i++){
			connectivity[i+1] = getInitialConnectivityDerivative(variables.get(i), variables);
		}
		res.add(connectivity);

		//bilan connectivity
		float[] bilanconnectivity = new float[variables.size()+1];
		bilanconnectivity[0] = getInitialBilanConnectivityEquation(fo);
		for(int i = 0; i<variables.size();i++){
			bilanconnectivity[i+1] = getInitialBilanConnectivityDerivative(variables.get(i), variables);
		}
		res.add(bilanconnectivity);
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
		distensibility[0] = getDistensibilityEquation(ar, pr);
		for(int i = 0; i<variables.size();i++){
			distensibility[i+1] = getDistensibilityDerivative(variables.get(i), variables);
		}
		res.add(distensibility);

		// momentum
		for(ElasticTube parent:getParents()){
			SimpleVariable parentPressure = findVariableWithName(((SAS)parent).getPressure().getName(),variables);
			float[] momentum = new float[variables.size()+1];
			momentum[0] = getMomentumEquation(fi, ar, pr, parentPressure);
			for(int i = 0; i<variables.size();i++){
				momentum[i+1] = getMomentumDerivative(variables.get(i), variables);
			}
			res.add(momentum);
		}

		// Connectivity
		SimpleVariable sasFlowout = ((SAS)getParents().get(0)).getFlowout();
		SimpleVariable sasPressure = ((SAS)getParents().get(0)).getPressure();
		ArrayList<SimpleVariable> psin = findVariableWithStruct(Hemisphere.BOTH, VenousSinus.TUBE_NUM, PRESSURE_LABEL, variables);
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
		distensibility[0] = getSymbolicInitialDistensibilityEquation(ar, pr);
		for(int i = 0; i<variables.size();i++){
			distensibility[i+1] = getSymbolicInitialDistensibilityDerivative(variables.get(i), variables);
		}
		res.add(distensibility);

		// momentum
		for(ElasticTube parent:getParents()){
			SimpleVariable parentPressure = findVariableWithName(((SAS)parent).getPressure().getName(),variables);
			String[] momentum = new String[variables.size()+1];
			momentum[0] = getSymbolicInitialMomentumEquation(fi, parentPressure, pr);
			for(int i = 0; i<variables.size();i++){
				momentum[i+1] = getSymbolicInitialMomentumDerivative(variables.get(i), variables);
			}
			res.add(momentum);
		}

		// Connectivity
		SimpleVariable sasFlowout = ((SAS)getParents().get(0)).getFlowout();
		SimpleVariable sasPressure = ((SAS)getParents().get(0)).getPressure();
		ArrayList<SimpleVariable> psin = findVariableWithStruct(Hemisphere.BOTH, VenousSinus.TUBE_NUM, PRESSURE_LABEL, variables);
		String[] connectivity = new String[variables.size()+1];
		connectivity[0] = getSymbolicInitialConnectivityEquation(sasFlowout, fi, sasPressure, psin.get(0));
		for(int i = 0; i<variables.size();i++){
			connectivity[i+1] = getSymbolicInitialConnectivityDerivative(variables.get(i), variables);
		}
		res.add(connectivity);

		//bilan connectivity
		String[] bilanconnectivity = new String[variables.size()+1];
		bilanconnectivity[0] = getSymbolicInitialBilanConnectivityEquation(fo);
		for(int i = 0; i<variables.size();i++){
			bilanconnectivity[i+1] = getSymbolicInitialBilanConnectivityDerivative(variables.get(i), variables);
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
		distensibility[0] = getSymbolicDistensibilityEquation(ar, pr);
		for(int i = 0; i<variables.size();i++){
			distensibility[i+1] = getSymbolicDistensibilityDerivative(variables.get(i), variables);
		}
		res.add(distensibility);

		// momentum
		for(ElasticTube parent:getParents()){
			SimpleVariable parentPressure = findVariableWithName(((SAS)parent).getPressure().getName(),variables);
			String[] momentum = new String[variables.size()+1];
			momentum[0] = getSymbolicMomentumEquation(fi, ar, pr, parentPressure);
			for(int i = 0; i<variables.size();i++){
				momentum[i+1] = getSymbolicMomentumDerivative(variables.get(i), variables);
			}
			res.add(momentum);
		}

		// Connectivity
		SimpleVariable sasFlowout = ((SAS)getParents().get(0)).getFlowout();
		SimpleVariable sasPressure = ((SAS)getParents().get(0)).getPressure();
		ArrayList<SimpleVariable> psin = findVariableWithStruct(Hemisphere.BOTH, VenousSinus.TUBE_NUM, PRESSURE_LABEL, variables);
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


	private float getContinuityEquation(SimpleVariable ar, SimpleVariable fi, SimpleVariable fo){
		// equ(15)
		return (ar.getValue() - getArea().getValue())/ModelSpecification.dt.getValue() + (- fi.getValue() + fo.getValue())/getLength().getValue();
	}

	private float getDistensibilityEquation(SimpleVariable ar, SimpleVariable pr){
		// equ(30)
		return -ModelSpecification.damp.getValue() * (ar.getValue() - getArea().getValue())/ModelSpecification.dt.getValue() + (pr.getValue()- ModelSpecification.Pstar.getValue())-getElastance().getValue()*(ar.getValue()/getInitialArea().getValue()-1);
	}

	private float getMomentumEquation(SimpleVariable fi, SimpleVariable ar, SimpleVariable pr, SimpleVariable parentPressure){
		// equ(45)
		return ModelSpecification.damp2.getValue() * ((fi.getValue()/ar.getValue()) - (getFlowin().getValue()/getArea().getValue()))/ModelSpecification.dt.getValue() + (parentPressure.getValue() - pr.getValue())-getAlpha().getValue()*fi.getValue();
	}

	// symbolic equation (en chaine de caractere)
	private String getSymbolicContinuityEquation(SimpleVariable ar, SimpleVariable fi, SimpleVariable fo){
		// equ(15)
		return "" + "("+ar.getName()+" - "+getArea().getName()+LAST_ROUND_SUFFIX+")/"+ModelSpecification.dt.getName()+""+" + (- "+fi.getName()+"+"+ fo.getName()+")/"+getLength().getName();
	}

	private String getSymbolicDistensibilityEquation(SimpleVariable ar, SimpleVariable pr){
		// equ(30)
		return "-"+ModelSpecification.damp.getName()+" * ("+ar.getName()+" - "+getArea().getName()+LAST_ROUND_SUFFIX+")/"+ModelSpecification.dt.getName()+" + ("+pr.getName()+"- "+ModelSpecification.Pstar.getName()+")-"+getElastance().getName()+"*("+ar.getName()+"/"+getInitialArea().getName()+"-1)";
	}

	private String getSymbolicMomentumEquation(SimpleVariable fi, SimpleVariable ar, SimpleVariable pr, SimpleVariable parentPressure){
		// equ(45)
		return " "+ModelSpecification.damp2.getName()+" * (("+fi.getName()+" / "+ar.getName()+" ) - ("+getFlowin().getName()+LAST_ROUND_SUFFIX+" / "+getArea().getName()+LAST_ROUND_SUFFIX+" ))/ dt + ("+parentPressure.getName()+"-"+pr.getName()+" )-"+getAlpha().getName()+" * "+fi.getName();
	}

	// ------- Derive -----------
	private float getContinuityDerivative(SimpleVariable v, ArrayList<SimpleVariable> variables){
		// equ(15)

		if(v.getName().equals(getArea().getName())){
			// derive selon area : 1/"+ModelSpecification.dt.getName()+" 
			return 1.0f/ModelSpecification.dt.getValue();
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

	private float getDistensibilityDerivative(SimpleVariable v, ArrayList<SimpleVariable> variables) throws Exception{
		// equ(30)
		if(v.getName().equals(getArea().getName())){  
			// derive selon area : - damp /"+ModelSpecification.dt.getName()+"  -T9_E * (1/T9_A0);
			return -ModelSpecification.damp.getValue()/ModelSpecification.dt.getValue()-getElastance().getValue()*(1.0f/getInitialArea().getValue());
		}else{
			if(v.getName().equals(getPressure().getName())){
				// derive selon pression : 1.0f
				return 1.0f;
			}else{
				return 0.0f;
			}
		}
	}

	private float getMomentumDerivative(SimpleVariable v, ArrayList<SimpleVariable> variables) throws Exception{
		// equ(45)

		if(v.getName().equals(getFlowin().getName())){
			// derive selon flowin : damp2 * ((1/T9_A))/"+ModelSpecification.dt.getName()+" -T9_alfa ;
			SimpleVariable ar = findVariableWithName(getArea().getName(),variables);
			return ModelSpecification.damp2.getValue()*(1/ar.getValue())/ModelSpecification.dt.getValue() - getAlpha().getValue();
		}else{
			if(v.getName().equals(getArea().getName())){
				// derive selon area : damp2 * (-T9_fi/T9_A²)/"+ModelSpecification.dt.getName()+"
				SimpleVariable fi = findVariableWithName(getFlowin().getName(),variables);
				return (float) (ModelSpecification.damp2.getValue() * (-fi.getValue()/Math.pow(v.getValue(),2))/ModelSpecification.dt.getValue());
			}else{
				if(v.getName().equals(getPressure().getName())){
					// derive selon pression : - 1.0f
					return -1.0f;		
				}else{
					for(ElasticTube parent:getParents()){
						SimpleVariable pr = findVariableWithName(((SAS)parent).getPressure().getName(),variables);
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


	private String getSymbolicContinuityDerivative(SimpleVariable v, ArrayList<SimpleVariable> variables){
		// equ(15)

		if(v.getName().equals(getArea().getName())){
			// derive selon area : 1/"+ModelSpecification.dt.getName()+" 
			return "" + 1.0f+"/"+ModelSpecification.dt.getName()+"";
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

	private String getSymbolicDistensibilityDerivative(SimpleVariable v, ArrayList<SimpleVariable> variables) throws Exception{
		// equ(30)

		if(v.getName().equals(getArea().getName())){
			// derive selon area : - damp /"+ModelSpecification.dt.getName()+"  -T9_E * (1/T9_A0);
			return "-"+ModelSpecification.damp.getName()+"/"+ModelSpecification.dt.getName()+"-"+getElastance().getName()+"*("+1.0f+"/"+getInitialArea().getName()+")";
		}else{
			if(v.getName().equals(getPressure().getName())){
				// derive selon pression : 1.0f
				return ""+1.0f;
			}else{
				return ""+0.0f;
			}
		}
	}

	private String getSymbolicMomentumDerivative(SimpleVariable v, ArrayList<SimpleVariable> variables) throws Exception{
		// equ(45)

		if(v.getName().equals(getFlowin().getName())){
			// derive selon flowin : damp2 * ((1/T7_A))/"+ModelSpecification.dt.getName()+" -T7_alfa ;
			SimpleVariable ar = findVariableWithName(getArea().getName(),variables);
			return ""+ModelSpecification.damp2.getName()+"*(1/"+ar.getName()+")/"+ModelSpecification.dt.getName()+" - "+getAlpha().getName();
		}else{
			if(v.getName().equals(getArea().getName())){
				// derive selon area : damp2 * (-T7_fi/T7_A²)/"+ModelSpecification.dt.getName()+"
				SimpleVariable fi = findVariableWithName(getFlowin().getName(),variables);
				return "("+ModelSpecification.damp2.getName()+" * (-"+fi.getName()+"/"+v.getName()+"^2)/"+ModelSpecification.dt.getName()+")";
			}else{
				if(v.getName().equals(getPressure().getName())){
					// derive selon pression : - 1.0f
					return ""+-1.0f;		
				}else{
					for(ElasticTube parent:getParents()){
						SimpleVariable pr = findVariableWithName(((SAS)parent).getPressure().getName(),variables);
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
	private float getConnectivityEquation(SimpleVariable sasFlowout, SimpleVariable fi, SimpleVariable sasPressure, SimpleVariable vsinuspr){
		// equ(58)
		float res = sasFlowout.getValue() - (fi.getValue() + ModelSpecification.k1.getValue() * (sasPressure.getValue() - vsinuspr.getValue()));
		return res;
	}

	private String getSymbolicConnectivityEquation(SimpleVariable sasFlowout, SimpleVariable fi, SimpleVariable sasPressure, SimpleVariable vsinuspr){
		// equ(58)
		return ""+sasFlowout.getName()+" - ("+fi.getName()+" + "+ModelSpecification.k1.getName()+" * ("+sasPressure.getName()+" - "+vsinuspr.getName()+"))";
	}

	private float getConnectivityDerivative(SimpleVariable v, ArrayList<SimpleVariable> variables) throws Exception{
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
					return -ModelSpecification.k1.getValue();
				}else{
					ArrayList<SimpleVariable> psin = findVariableWithStruct(Hemisphere.BOTH, VenousSinus.TUBE_NUM, PRESSURE_LABEL, variables);
					if(v.getName().equals(psin.get(0).getName())){
						// derive selon la pression vsinus : k1;
						return ModelSpecification.k1.getValue();
					}else{
						return 0.0f;
					}
				}
			}
		}
	}

	private String getSymbolicConnectivityDerivative(SimpleVariable v, ArrayList<SimpleVariable> variables) throws Exception{
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
					return "-"+ModelSpecification.k1.getName();
				}else{
					ArrayList<SimpleVariable> psin = findVariableWithStruct(Hemisphere.BOTH, VenousSinus.TUBE_NUM, PRESSURE_LABEL, variables);
					if(v.getName().equals(psin.get(0).getName())){
						// derive selon la pression vsinus : k1;
						return ModelSpecification.k1.getName();
					}else{
						return ""+0.0f;
					}
				}
			}
		}
	}

	//====== Blilan Connectivity ====

	private float getBilanConnectivityEquation(SimpleVariable fo){
		// equ(59)
		return fo.getValue() - 0.0f;
	}

	private String getSymbolicBilanConnectivityEquation(SimpleVariable fo){
		// equ(59)
		return ""+fo.getName()+" - "+0.0f;
	}

	private float getBilanConnectivityDerivative(SimpleVariable v, ArrayList<SimpleVariable> variables) throws Exception{
		// equ(59)
		if(v.getName().equals(getFlowout().getName())){
			// derive selon flowout : 1;
			return 1.0f;
		}else{
			return 0.0f;
		}
	}

	private String getSymbolicBilanConnectivityDerivative(SimpleVariable v, ArrayList<SimpleVariable> variables) throws Exception{
		// equ(59)
		if(v.getName().equals(getFlowout().getName())){
			// derive selon flowout : 1;
			return ""+1.0f;
		}else{
			return ""+0.0f;
		}
	}

	// ================= init ========================

	private float getInitialContinuityEquation(SimpleVariable fi, SimpleVariable fo){
		// eq(15)
		return fi.getValue() - fo.getValue();
	}
	private float getInitialContinuityDerivative(SimpleVariable v, ArrayList<SimpleVariable> variables){
		// eq(15)
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
		// eq(15)
		return fi.getName()+" - "+fo.getName();
	}
	private String getSymbolicInitialContinuityDerivative(SimpleVariable v, ArrayList<SimpleVariable> variables){
		// eq(15)
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
	private float getInitialDistensibilityEquation(SimpleVariable ar, SimpleVariable pr){
		// equ(30)
		return  (pr.getValue()- ModelSpecification.Pstar.getValue())-getElastance().getValue()*(ar.getValue()/getInitialArea().getValue()-1);
	}
	private float getInitialDistensibilityDerivative(SimpleVariable v, ArrayList<SimpleVariable> variables) throws Exception{
		// equ(30)

		if(v.getName().equals(getArea().getName())){
			// derive selon area : -T9_E * (1/T9_A0);
			return -getElastance().getValue()*(1.0f/getInitialArea().getValue());
		}else{
			if(v.getName().equals(getPressure().getName())){
				// derive selon pression : 1.0f
				return 1.0f;
			}
			return 0.0f;
		}
	}
	private String getSymbolicInitialDistensibilityEquation(SimpleVariable ar, SimpleVariable pr){
		// equ(30)
		return "("+pr.getName()+"- "+ModelSpecification.Pstar.getName()+")-"+getElastance().getName()+"*("+ar.getName()+"/"+getInitialArea().getName()+"-1)";
	}
	private String getSymbolicInitialDistensibilityDerivative(SimpleVariable v, ArrayList<SimpleVariable> variables) throws Exception{
		// equ(30)

		if(v.getName().equals(getArea().getName())){
			// derive selon area : -T9_E * (1/T9_A0);
			return "-"+getElastance().getName()+"*("+1.0f+"/"+getInitialArea().getName()+")";
		}else{
			if(v.getName().equals(getPressure().getName())){
				// derive selon pression : 1.0f
				return ""+1.0f;
			}
			return ""+0.0f;
		}
	}

	// momentum
	private float getInitialMomentumEquation(SimpleVariable fi, SimpleVariable parentPressure, SimpleVariable pr){
		// equ(45)
		return (parentPressure.getValue() - pr.getValue())-getAlpha().getValue()*fi.getValue();
	}
	private float getInitialMomentumDerivative(SimpleVariable v, ArrayList<SimpleVariable> variables) throws Exception{
		// equ(45)
		if(v.getName().equals(getPressure().getName())){
			// derive selon pression : - 1.0f
			return -1.0f;		
		}else{
			for(ElasticTube parent:getParents()){
				SimpleVariable parentPressure = findVariableWithName(((SAS)parent).getPressure().getName(),variables);
				if(v.getName().equals(parentPressure.getName())){
					// derive selon parent pressure : 1.0
					return 1.0f;
				}else{
					if(v.getName().equals(getFlowin().getName())){
						// derive selon fin : -T9_alfa;
						return  - getAlpha().getValue();		
					}
				}
			}
			return 0.0f;
		}
	}
	private String getSymbolicInitialMomentumEquation(SimpleVariable fi, SimpleVariable parentPressure, SimpleVariable pr){
		// equ(45)
		return "("+parentPressure.getName()+" - "+pr.getName()+")-"+getAlpha().getName()+"*"+fi.getName();
	}

	private String getSymbolicInitialMomentumDerivative(SimpleVariable v, ArrayList<SimpleVariable> variables) throws Exception{
		// equ(45)
		if(v.getName().equals(getPressure().getName())){
			// derive selon pression : - 1.0f
			return "-"+1.0f;		
		}else{
			for(ElasticTube parent:getParents()){
				SimpleVariable parentPressure = findVariableWithName(((SAS)parent).getPressure().getName(),variables);
				if(v.getName().equals(parentPressure.getName())){
					// derive selon parent pressure : 1.0
					return ""+1.0f;
				}else{
					if(v.getName().equals(getFlowin().getName())){
						// derive selon parent fin :  -T9_alfa;
						return "-"+getAlpha().getName();		
					}
				}
			}
			return ""+0.0f;
		}
	}
	/**
	 * Pour l'equation de connectivite du flux on fait la somme du flux en amont qui doit etre egale au flux in
	 * @param parentFlowout
	 * @param fi
	 * @return
	 */
	private float getInitialConnectivityEquation(SimpleVariable sasFlowout, SimpleVariable fi, SimpleVariable sasPressure, SimpleVariable vsinuspr){
		// equ(58)
		float res = sasFlowout.getValue() - (fi.getValue() + ModelSpecification.k1.getValue() * (sasPressure.getValue() - vsinuspr.getValue()));
		return res;
	}

	private String getSymbolicInitialConnectivityEquation(SimpleVariable sasFlowout, SimpleVariable fi, SimpleVariable sasPressure, SimpleVariable vsinuspr){
		// equ(58)
		return ""+sasFlowout.getName()+" - ("+fi.getName()+" + "+ModelSpecification.k1.getName()+" * ("+sasPressure.getName()+" - "+vsinuspr.getName()+"))";
	}

	private float getInitialConnectivityDerivative(SimpleVariable v, ArrayList<SimpleVariable> variables) throws Exception{
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
					return -ModelSpecification.k1.getValue();
				}else{
					ArrayList<SimpleVariable> psin = findVariableWithStruct(Hemisphere.BOTH, VenousSinus.TUBE_NUM, PRESSURE_LABEL, variables);
					if(v.getName().equals(psin.get(0).getName())){
						// derive selon la pression vsinus : k1;
						return ModelSpecification.k1.getValue();
					}else{
						return 0.0f;
					}
				}
			}
		}
	}

	private String getSymbolicInitialConnectivityDerivative(SimpleVariable v, ArrayList<SimpleVariable> variables) throws Exception{
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
					return "-"+ModelSpecification.k1.getName();
				}else{
					ArrayList<SimpleVariable> psin = findVariableWithStruct(Hemisphere.BOTH, VenousSinus.TUBE_NUM, PRESSURE_LABEL, variables);
					if(v.getName().equals(psin.get(0).getName())){
						// derive selon la pression vsinus : k1;
						return ModelSpecification.k1.getName();
					}else{
						return ""+0.0f;
					}
				}
			}
		}
	}

	//====== Blilan Connectivity ====

	private float getInitialBilanConnectivityEquation(SimpleVariable fo){
		// equ(59)
		return fo.getValue() - 0.0f;
	}

	private String getSymbolicInitialBilanConnectivityEquation(SimpleVariable fo){
		// equ(59)
		return ""+fo.getName()+" - "+0.0f;
	}

	private float getInitialBilanConnectivityDerivative(SimpleVariable v, ArrayList<SimpleVariable> variables) throws Exception{
		// equ(59)
		if(v.getName().equals(getFlowout().getName())){
			// derive selon flowout : 1;
			return 1.0f;
		}else{
			return 0.0f;
		}
	}

	private String getSymbolicInitialBilanConnectivityDerivative(SimpleVariable v, ArrayList<SimpleVariable> variables) throws Exception{
		// equ(59)
		if(v.getName().equals(getFlowout().getName())){
			// derive selon flowout : 1;
			return ""+1.0f;
		}else{
			return ""+0.0f;
		}
	}
}
