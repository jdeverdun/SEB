package models;

import java.util.ArrayList;

import params.ModelSpecification;

public class VenousSinus extends ElasticTube {
	public static final String TUBE_NUM = "8";
	public static final Hemisphere DEFAULT_HEMI = Hemisphere.BOTH;
	public static final float DEFAULT_LENGTH = 15.0f;
	public static final float DEFAULT_AREA = 2.0f * 0.43f;
	public static final float DEFAULT_ELASTANCE = 120000.0f;// en Pa
	public static final float DEFAULT_ALPHA = 0.161896f * 1333.2240f;
	public static final float DEFAULT_FLOWIN = 13.0f;
	public static final float DEFAULT_FLOWOUT = 13.0f;
	public static final float DEFAULT_PRESSURE = 3999.67f;

	public VenousSinus(String name) {
		super(name, DEFAULT_HEMI, DEFAULT_LENGTH,DEFAULT_AREA,DEFAULT_ALPHA,DEFAULT_ELASTANCE, DEFAULT_FLOWIN, DEFAULT_FLOWOUT, DEFAULT_PRESSURE);
	}

	public VenousSinus(String name, float len, float a) {
		super(name, DEFAULT_HEMI, len, a, DEFAULT_ALPHA, DEFAULT_ELASTANCE, DEFAULT_FLOWIN, DEFAULT_FLOWOUT, DEFAULT_PRESSURE);
	}

	public VenousSinus(String name, float len, float a, float alpha, float elast, float fin, float fout, float press) {
		super(name, DEFAULT_HEMI, len, a, alpha, elast, fin, fout, press);
	}


	public VenousSinus(String name, float len, float a, float alpha, float elast, float fin, float fout, float press, ArrayList<ElasticTube> par,
			ArrayList<ElasticTube> child) {
		super(name, DEFAULT_HEMI, len, a, alpha, elast, fin, fout, press, par, child);
	}

	public String toString(){
		return "VenousSinus : "+super.toString();
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
		for(ElasticTube parent:getParents()){
			SimpleVariable parentPressure = findVariableWithName(((Vein)parent).getPressure().getName(),variables);
			SimpleVariable parentFlowout = findVariableWithName(((Vein)parent).getFlowout().getName(),variables);
			float[] momentum = new float[variables.size()+1];
			momentum[0] = getInitialMomentumEquation(parentFlowout, parentPressure, pr);
			for(int i = 0; i<variables.size();i++){
				momentum[i+1] = getInitialMomentumDerivative(variables.get(i), variables);
			}
			res.add(momentum);
		}

		// Connectivity
		ArrayList<SimpleVariable> psas = findVariableWithStruct(Hemisphere.BOTH, SAS.TUBE_NUM, PRESSURE_LABEL, variables);
		ArrayList<SimpleVariable> parentFlowout = new ArrayList<SimpleVariable>();
		for(ElasticTube parent:getParents()){
			parentFlowout.add(findVariableWithName(((Vein)parent).getFlowout().getName(),variables));
		}
		float[] connectivity = new float[variables.size()+1];
		connectivity[0] = getInitialConnectivityEquation(parentFlowout, psas.get(0), pr, fi);
		for(int i = 0; i<variables.size();i++){
			connectivity[i+1] = getInitialConnectivityDerivative(variables.get(i), variables);
		}
		res.add(connectivity);

		// additonal momentum
		float[] addmom = new float[variables.size()+1];
		addmom[0] = getInitialAddMomentumEquation(pr,fo);
		for(int i = 0; i<variables.size();i++){
			addmom[i+1] = getInitialAddMomentumDerivative(variables.get(i), variables);
		}
		res.add(addmom);


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
		SimpleVariable pbrain_left = findVariableWithName(ModelSpecification.architecture.getBrain().getLeftHemi().getPressure().getName(),variables);
		SimpleVariable pbrain_right = findVariableWithName(ModelSpecification.architecture.getBrain().getRightHemi().getPressure().getName(),variables);
		distensibility[0] = getDistensibilityEquation(ar, pr, pbrain_left, pbrain_right);
		for(int i = 0; i<variables.size();i++){
			distensibility[i+1] = getDistensibilityDerivative(variables.get(i), variables);
		}
		res.add(distensibility);

		// momentum
		for(ElasticTube parent:getParents()){
			SimpleVariable parentPressure = findVariableWithName(((Vein)parent).getPressure().getName(),variables);
			SimpleVariable parentFlowout = findVariableWithName(((Vein)parent).getFlowout().getName(),variables);
			SimpleVariable parentArea = findVariableWithName(((Vein)parent).getArea().getName(),variables);
			SimpleVariable parentFlowout_current = ((Vein)parent).getFlowout();
			SimpleVariable parentArea_current = ((Vein)parent).getArea();
			float[] momentum = new float[variables.size()+1];
			momentum[0] = getMomentumEquation(parentFlowout, parentArea, parentFlowout_current, parentArea_current, parentPressure, pr);
			for(int i = 0; i<variables.size();i++){
				momentum[i+1] = getMomentumDerivative(variables.get(i), variables);
			}
			res.add(momentum);
		}

		// Connectivity
		ArrayList<SimpleVariable> psas = findVariableWithStruct(Hemisphere.BOTH, SAS.TUBE_NUM, PRESSURE_LABEL, variables);
		ArrayList<SimpleVariable> parentFlowout = new ArrayList<SimpleVariable>();
		for(ElasticTube parent:getParents()){
			parentFlowout.add(findVariableWithName(((Vein)parent).getFlowout().getName(),variables));
		}
		float[] connectivity = new float[variables.size()+1];
		connectivity[0] = getConnectivityEquation(parentFlowout, psas.get(0), pr, fi);
		for(int i = 0; i<variables.size();i++){
			connectivity[i+1] = getConnectivityDerivative(variables.get(i), variables);
		}
		res.add(connectivity);

		// additonal momentum
		float[] addmom = new float[variables.size()+1];
		addmom[0] = getAddMomentumEquation(pr,fo);
		for(int i = 0; i<variables.size();i++){
			addmom[i+1] = getAddMomentumDerivative(variables.get(i), variables);
		}
		res.add(addmom);


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
		for(ElasticTube parent:getParents()){
			SimpleVariable parentPressure = findVariableWithName(((Vein)parent).getPressure().getName(),variables);
			SimpleVariable parentFlowout = findVariableWithName(((Vein)parent).getFlowout().getName(),variables);
			String[] momentum = new String[variables.size()+1];
			momentum[0] = getSymbolicInitialMomentumEquation(parentFlowout, parentPressure, pr);
			for(int i = 0; i<variables.size();i++){
				momentum[i+1] = getSymbolicInitialMomentumDerivative(variables.get(i), variables);
			}
			res.add(momentum);
		}

		// connectivity
		ArrayList<SimpleVariable> psas = findVariableWithStruct(Hemisphere.BOTH, SAS.TUBE_NUM, PRESSURE_LABEL, variables);
		ArrayList<SimpleVariable> parentFlowout = new ArrayList<SimpleVariable>();
		for(ElasticTube parent:getParents()){
			parentFlowout.add(findVariableWithName(((Vein)parent).getFlowout().getName(),variables));
		}
		String[] connectivity = new String[variables.size()+1];
		connectivity[0] = getSymbolicInitialConnectivityEquation(parentFlowout, psas.get(0), pr, fi);
		for(int i = 0; i<variables.size();i++){
			connectivity[i+1] = getSymbolicInitialConnectivityDerivative(variables.get(i), variables);
		}
		res.add(connectivity);

		// additonal momentum
		String[] addmom = new String[variables.size()+1];
		addmom[0] = getSymbolicInitialAddMomentumEquation(pr,fo);
		for(int i = 0; i<variables.size();i++){
			addmom[i+1] = getSymbolicInitialAddMomentumDerivative(variables.get(i), variables);
		}
		res.add(addmom);
				
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
		SimpleVariable pbrain_left = findVariableWithName(ModelSpecification.architecture.getBrain().getLeftHemi().getPressure().getName(),variables);
		SimpleVariable pbrain_right = findVariableWithName(ModelSpecification.architecture.getBrain().getRightHemi().getPressure().getName(),variables);
		distensibility[0] = getSymbolicDistensibilityEquation(ar, pr, pbrain_left, pbrain_right);
		for(int i = 0; i<variables.size();i++){
			distensibility[i+1] = getSymbolicDistensibilityDerivative(variables.get(i), variables);
		}
		res.add(distensibility);

		// momentum
		for(ElasticTube parent:getParents()){
			SimpleVariable parentPressure = findVariableWithName(((Vein)parent).getPressure().getName(),variables);
			SimpleVariable parentFlowout = findVariableWithName(((Vein)parent).getFlowout().getName(),variables);
			SimpleVariable parentArea = findVariableWithName(((Vein)parent).getArea().getName(),variables);
			SimpleVariable parentFlowout_current = ((Vein)parent).getFlowout();
			SimpleVariable parentArea_current = ((Vein)parent).getArea();
			String[] momentum = new String[variables.size()+1];
			momentum[0] = getSymbolicMomentumEquation(parentFlowout, parentArea, parentFlowout_current, parentArea_current, parentPressure, pr);
			for(int i = 0; i<variables.size();i++){
				momentum[i+1] = getSymbolicMomentumDerivative(variables.get(i), variables);
			}
			res.add(momentum);
		}

		// connectivity
		ArrayList<SimpleVariable> psas = findVariableWithStruct(Hemisphere.BOTH, SAS.TUBE_NUM, PRESSURE_LABEL, variables);
		ArrayList<SimpleVariable> parentFlowout = new ArrayList<SimpleVariable>();
		for(ElasticTube parent:getParents()){
			parentFlowout.add(findVariableWithName(((Vein)parent).getFlowout().getName(),variables));
		}
		String[] connectivity = new String[variables.size()+1];
		connectivity[0] = getSymbolicConnectivityEquation(parentFlowout, psas.get(0), pr, fi);
		for(int i = 0; i<variables.size();i++){
			connectivity[i+1] = getSymbolicConnectivityDerivative(variables.get(i), variables);
		}
		res.add(connectivity);

		// additonal momentum
		String[] addmom = new String[variables.size()+1];
		addmom[0] = getSymbolicAddMomentumEquation(pr,fo);
		for(int i = 0; i<variables.size();i++){
			addmom[i+1] = getSymbolicAddMomentumDerivative(variables.get(i), variables);
		}
		res.add(addmom);
				
		return res;
	}


	private float getContinuityEquation(SimpleVariable ar, SimpleVariable fi, SimpleVariable fo){
		// equ(14)
		return (ar.getValue() - getArea().getValue())/ModelSpecification.dt.getValue() + (- fi.getValue() + fo.getValue())/getLength().getValue();
	}

	private float getDistensibilityEquation(SimpleVariable ar, SimpleVariable pr, SimpleVariable pbrain_left, SimpleVariable pbrain_right){
		// equ(29)
		return -ModelSpecification.damp.getValue() * (ar.getValue() - getArea().getValue())/ModelSpecification.dt.getValue() + (pr.getValue()- 0.5f * (pbrain_left.getValue() + pbrain_right.getValue()))-getElastance().getValue()*(ar.getValue()/getInitialArea().getValue()-1);
	}

	private float getMomentumEquation(SimpleVariable parentFlowout, SimpleVariable parentArea, SimpleVariable parentFlowout_current, SimpleVariable parentArea_current, SimpleVariable parentPressure, SimpleVariable pr){
		// equ(46) et equ(47)
		return ModelSpecification.damp2.getValue() * ((parentFlowout.getValue()/parentArea.getValue()) - (parentFlowout_current.getValue()/parentArea_current.getValue()))/ModelSpecification.dt.getValue() + (parentPressure.getValue() - pr.getValue())-getAlpha().getValue()*parentFlowout.getValue();
	}

	// symbolic equation (en chaine de caractere)
	private String getSymbolicContinuityEquation(SimpleVariable ar, SimpleVariable fi, SimpleVariable fo){
		// equ(14)
		return "" + "("+ar.getName()+" - "+getArea().getName()+LAST_ROUND_SUFFIX+")/"+ModelSpecification.dt.getName()+""+" + (- "+fi.getName()+"+"+ fo.getName()+")/"+getLength().getName();
	}

	private String getSymbolicDistensibilityEquation(SimpleVariable ar, SimpleVariable pr, SimpleVariable pbrain_left, SimpleVariable pbrain_right){
		// equ(29)
		return "-"+ModelSpecification.damp.getName()+" * ("+ar.getName()+" - "+getArea().getName()+LAST_ROUND_SUFFIX+")/"+ModelSpecification.dt.getName()+" + ("+pr.getName()+"- "+0.5f+" * ("+pbrain_left.getName()+" + "+pbrain_right.getName()+"))-"+getElastance().getName()+"*("+ar.getName()+"/"+getInitialArea().getName()+"-1)";
	}

	private String getSymbolicMomentumEquation(SimpleVariable parentFlowout, SimpleVariable parentArea, SimpleVariable parentFlowout_current, SimpleVariable parentArea_current, SimpleVariable parentPressure, SimpleVariable pr){
		// equ(46) et equ(47)
		return ""+ModelSpecification.damp2.getName()+" * (("+parentFlowout.getName()+"/"+parentArea.getName()+") - ("+parentFlowout_current.getName()+LAST_ROUND_SUFFIX+"/"+parentArea_current.getName()+LAST_ROUND_SUFFIX+"))/"+ModelSpecification.dt.getName()+" + ("+parentPressure.getName()+" - "+pr.getName()+")-"+getAlpha().getName()+"*"+parentFlowout.getName();
	}

	// ------- Derive -----------
	private float getContinuityDerivative(SimpleVariable v, ArrayList<SimpleVariable> variables){
		// equ(14)

		if(v.getName().equals(getArea().getName())){
			// derive selon area : 1/"+ModelSpecification.dt.getName()+" 
			return 1.0f/ModelSpecification.dt.getValue();
		}else{
			if(v.getName().equals(getFlowin().getName())){
				// derive selon flowin : - 1/T8_l0;
				return -1.0f/getLength().getValue();
			}else{
				if(v.getName().equals(getFlowout().getName())){
					// derive selon flowout : 1/T8_l0
					return 1.0f/getLength().getValue();		
				}else{
					return 0.0f;
				}
			}
		}
	}

	private float getDistensibilityDerivative(SimpleVariable v, ArrayList<SimpleVariable> variables) throws Exception{
		// equ(29)

		if(v.getName().equals(getArea().getName())){
			// derive selon area : - damp /"+ModelSpecification.dt.getName()+"  -T8_E * (1/T8_A0);
			return -ModelSpecification.damp.getValue()/ModelSpecification.dt.getValue()-getElastance().getValue()*(1.0f/getInitialArea().getValue());
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
		// equ(46) et equ(47)
		if(v.getName().equals(getPressure().getName())){
			// derive selon pression : - 1.0f
			return -1.0f;		
		}else{
			for(ElasticTube parent:getParents()){
				SimpleVariable parentPressure = findVariableWithName(((Vein)parent).getPressure().getName(),variables);
				if(v.getName().equals(parentPressure.getName())){
					// derive selon parent pressure : 1.0
					return 1.0f;
				}else{
					SimpleVariable parentArea = findVariableWithName(((Vein)parent).getArea().getName(),variables);
					SimpleVariable parentFlowout = findVariableWithName(((Vein)parent).getFlowout().getName(),variables);
					if(v.getName().equals(parentFlowout.getName())){
						// derive selon parent flow out : damp2 * ((1/R_T3b_A))/"+ModelSpecification.dt.getName()+"   -R_T8_alfa;
						return ModelSpecification.damp2.getValue() * (1/parentArea.getValue())/ModelSpecification.dt.getValue() - getAlpha().getValue();		
					}else{
						if(v.getName().equals(parentArea.getName())){
							// derive selon parent area : damp2 * ((-R_T3b_fo/R_T3b_A²))/"+ModelSpecification.dt.getName()+"
							return (float) (ModelSpecification.damp2.getValue() * (-parentFlowout.getValue()/Math.pow(parentArea.getValue(), 2))/ModelSpecification.dt.getValue());
						}
					}
				}
			}
			return 0.0f;
		}
	}


	private String getSymbolicContinuityDerivative(SimpleVariable v, ArrayList<SimpleVariable> variables){
		// equ(14)

		if(v.getName().equals(getArea().getName())){
			// derive selon area : 1/"+ModelSpecification.dt.getName()+" 
			return "" + 1.0f+"/"+ModelSpecification.dt.getName()+"";
		}else{
			if(v.getName().equals(getFlowin().getName())){
				// derive selon flowin : - 1/T8_l0;
				return "-"+1.0f+"/"+getLength().getName();
			}else{
				if(v.getName().equals(getFlowout().getName())){
					// derive selon flowout : 1/T8_l0
					return ""+1.0f+"/"+getLength().getName();		
				}else{
					return ""+0.0f;
				}
			}
		}
	}

	private String getSymbolicDistensibilityDerivative(SimpleVariable v, ArrayList<SimpleVariable> variables) throws Exception{
		// equ(29)

		if(v.getName().equals(getArea().getName())){
			// derive selon area : - damp /"+ModelSpecification.dt.getName()+"  -T8_E * (1/T8_A0);
			return "-"+ModelSpecification.damp.getName()+"/"+ModelSpecification.dt.getName()+"-"+getElastance().getName()+"*("+1.0f+"/"+getInitialArea().getName()+")";
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
		// equ(46) et equ(47)
		if(v.getName().equals(getPressure().getName())){
			// derive selon pression : - 1.0f
			return "-"+1.0f;		
		}else{
			for(ElasticTube parent:getParents()){
				SimpleVariable parentPressure = findVariableWithName(((Vein)parent).getPressure().getName(),variables);
				if(v.getName().equals(parentPressure.getName())){
					// derive selon parent pressure : 1.0
					return ""+1.0f;
				}else{
					SimpleVariable parentArea = findVariableWithName(((Vein)parent).getArea().getName(),variables);
					SimpleVariable parentFlowout = findVariableWithName(((Vein)parent).getFlowout().getName(),variables);
					if(v.getName().equals(parentFlowout.getName())){
						// derive selon parent flow out : damp2 * ((1/R_T3b_A))/"+ModelSpecification.dt.getName()+"   -R_T8_alfa;
						return ""+ModelSpecification.damp2.getName()+" * (1/"+parentArea.getName()+")/"+ModelSpecification.dt.getName()+" - "+getAlpha().getName();		
					}else{
						if(v.getName().equals(parentArea.getName())){
							// derive selon parent area : damp2 * ((-R_T3b_fo/R_T3b_A²))/"+ModelSpecification.dt.getName()+"
							return ""+ModelSpecification.damp2.getName()+" * (-"+parentFlowout.getName()+"/"+parentArea.getName()+"^2)/"+ModelSpecification.dt.getName()+"";
						}
					}
				}
			}
			return ""+0.0f;
		}
	}

	//====== Connectivity ====
	/**
	 * Pour l'equation de connectivite du flux on fait la somme du flux en amont qui doit etre egale au flux in
	 * @param parentFlowout
	 * @param fi
	 * @return
	 */
	private float getConnectivityEquation(ArrayList<SimpleVariable> parentFlowout, SimpleVariable sasPressure, SimpleVariable pr, SimpleVariable fi){
		// equ(54)
		float res = 0;
		for(SimpleVariable pf : parentFlowout){
			res += pf.getValue();
		}
		res = res + ModelSpecification.k1.getValue() * (sasPressure.getValue() - pr.getValue());
		return (res - fi.getValue());
	}

	private String getSymbolicConnectivityEquation(ArrayList<SimpleVariable> parentFlowout, SimpleVariable sasPressure, SimpleVariable pr, SimpleVariable fi){
		// equ(54)
		String res = "(";
		for(SimpleVariable pf : parentFlowout){
			if(!res.equals("("))
				res += "+";
			res += pf.getName();
		}
		res += " + "+ModelSpecification.k1.getName()+" * ("+sasPressure.getName()+" - "+pr.getName()+"))";
		return res+" - ("+fi.getName()+")";
	}

	private float getConnectivityDerivative(SimpleVariable v, ArrayList<SimpleVariable> variables) throws Exception{
		// equ(54)
		if(v.getName().equals(getFlowin().getName())){
			// derive selon flowin : -1;
			return -1.0f;
		}else{
			if(v.getName().equals(getPressure().getName())){
				// derive selon la pression : -k1;
				return -ModelSpecification.k1.getValue();
			}else{
				ArrayList<SimpleVariable> psas = findVariableWithStruct(Hemisphere.BOTH, SAS.TUBE_NUM, PRESSURE_LABEL, variables);
				if(v.getName().equals(psas.get(0).getName())){
					// derive selon la pression : k1;
					return ModelSpecification.k1.getValue();
				}else{
					for(ElasticTube parent:getParents()){
						SimpleVariable pr = findVariableWithName(((Vein)parent).getFlowout().getName(),variables);
						if(v.getName().equals(pr.getName())){
							// derive selon flowoutParent :  1.0f
							return 1.0f;		
						}
					}
					return 0.0f;
				}
			}
		}
	}

	private String getSymbolicConnectivityDerivative(SimpleVariable v, ArrayList<SimpleVariable> variables) throws Exception{
		// equ(54)
		if(v.getName().equals(getFlowin().getName())){
			// derive selon flowin : -1;
			return "-"+1.0f;
		}else{
			if(v.getName().equals(getPressure().getName())){
				// derive selon la pression : -k1;
				return "-"+ModelSpecification.k1.getName();
			}else{
				ArrayList<SimpleVariable> psas = findVariableWithStruct(Hemisphere.BOTH, SAS.TUBE_NUM, PRESSURE_LABEL, variables);
				if(v.getName().equals(psas.get(0).getName())){
					// derive selon la pression : k1;
					return ModelSpecification.k1.getName();
				}else{
					for(ElasticTube parent:getParents()){
						SimpleVariable pr = findVariableWithName(((Vein)parent).getFlowout().getName(),variables);
						if(v.getName().equals(pr.getName())){
							// derive selon flowoutParent :  1.0f
							return ""+1.0f;		
						}
					}
					return ""+0.0f;
				}
			}
		}
	}

	//====== additional momentum ====
	private float getAddMomentumEquation(SimpleVariable pr, SimpleVariable fo){
		// equ(72)
		return (pr.getValue() - ModelSpecification.P_OUT.getValue()[(int) ModelSpecification.currentIter.getValue()]) - ModelSpecification.TPout_alfa.getValue() * fo.getValue();
	}

	private String getSymbolicAddMomentumEquation(SimpleVariable pr, SimpleVariable fo){
		// equ(72)
		return "("+pr.getName()+" - "+ModelSpecification.P_OUT.getName()+"("+ModelSpecification.currentIter.getName()+")) - "+ModelSpecification.TPout_alfa.getName()+" * "+fo.getName();
	}

	private float getAddMomentumDerivative(SimpleVariable v, ArrayList<SimpleVariable> variables) throws Exception{
		// equ(72)
		if(v.getName().equals(getFlowout().getName())){
			// derive selon flowout : - TPout_alfa;
			return -ModelSpecification.TPout_alfa.getValue();
		}else{
			if(v.getName().equals(getPressure().getName())){
				// derive selon la pression : 1;
				return 1.0f;
			}else{
				return 0.0f;
			}
		}
	}

	private String getSymbolicAddMomentumDerivative(SimpleVariable v, ArrayList<SimpleVariable> variables) throws Exception{
		// equ(72)
		if(v.getName().equals(getFlowout().getName())){
			// derive selon flowout : - TPout_alfa;
			return "-"+ModelSpecification.TPout_alfa.getName();
		}else{
			if(v.getName().equals(getPressure().getName())){
				// derive selon la pression : 1;
				return ""+1.0f;
			}else{
				return ""+0.0f;
			}
		}
	}

	// ================= init ========================

	private float getInitialContinuityEquation(SimpleVariable fi, SimpleVariable fo){
		// eq(14)
		return fi.getValue() - fo.getValue();
	}
	private float getInitialContinuityDerivative(SimpleVariable v, ArrayList<SimpleVariable> variables){
		// eq(14)
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
		// eq(14)
		return fi.getName()+" - "+fo.getName();
	}
	private String getSymbolicInitialContinuityDerivative(SimpleVariable v, ArrayList<SimpleVariable> variables){
		// eq(14)
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
	private float getInitialDistensibilityEquation(SimpleVariable ar, SimpleVariable pr, SimpleVariable pbrain_left, SimpleVariable pbrain_right){
		// equ(29)
		return  (pr.getValue()- 0.5f * (pbrain_left.getValue() + pbrain_right.getValue()))-getElastance().getValue()*(ar.getValue()/getInitialArea().getValue()-1);
	}
	private float getInitialDistensibilityDerivative(SimpleVariable v, ArrayList<SimpleVariable> variables) throws Exception{
		// equ(29)

		if(v.getName().equals(getArea().getName())){
			// derive selon area : -T8_E * (1/T8_A0);
			return -getElastance().getValue()*(1.0f/getInitialArea().getValue());
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
	private String getSymbolicInitialDistensibilityEquation(SimpleVariable ar, SimpleVariable pr, SimpleVariable pbrain_left, SimpleVariable pbrain_right){
		// equ(29)
		return "("+pr.getName()+"- "+0.5f+" * ("+pbrain_left.getName()+" + "+pbrain_right.getName()+"))-"+getElastance().getName()+"*("+ar.getName()+"/"+getInitialArea().getName()+"-1)";
	}
	private String getSymbolicInitialDistensibilityDerivative(SimpleVariable v, ArrayList<SimpleVariable> variables) throws Exception{
		// equ(29)

		if(v.getName().equals(getArea().getName())){
			// derive selon area : -T8_E * (1/T8_A0);
			return "-"+getElastance().getName()+"*("+1.0f+"/"+getInitialArea().getName()+")";
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

	// momentum
	private float getInitialMomentumEquation(SimpleVariable parentFlowout, SimpleVariable parentPressure, SimpleVariable pr){
		// equ(46) et equ(47)
		return (parentPressure.getValue() - pr.getValue())-getAlpha().getValue()*parentFlowout.getValue();
	}
	private float getInitialMomentumDerivative(SimpleVariable v, ArrayList<SimpleVariable> variables) throws Exception{
		// equ(46) et equ(47)
		if(v.getName().equals(getPressure().getName())){
			// derive selon pression : - 1.0f
			return -1.0f;		
		}else{
			for(ElasticTube parent:getParents()){
				SimpleVariable parentPressure = findVariableWithName(((Vein)parent).getPressure().getName(),variables);
				if(v.getName().equals(parentPressure.getName())){
					// derive selon parent pressure : 1.0
					return 1.0f;
				}else{
					SimpleVariable parentFlowout = findVariableWithName(((Vein)parent).getFlowout().getName(),variables);
					if(v.getName().equals(parentFlowout.getName())){
						// derive selon parent flow out : -R_T8_alfa;
						return  - getAlpha().getValue();		
					}
				}
			}
			return 0.0f;
		}
	}
	private String getSymbolicInitialMomentumEquation(SimpleVariable parentFlowout, SimpleVariable parentPressure, SimpleVariable pr){
		// equ(46) et equ(47)
		return "("+parentPressure.getName()+" - "+pr.getName()+")-"+getAlpha().getName()+"*"+parentFlowout.getName();
	}

	private String getSymbolicInitialMomentumDerivative(SimpleVariable v, ArrayList<SimpleVariable> variables) throws Exception{
		// equ(46) et equ(47)
		if(v.getName().equals(getPressure().getName())){
			// derive selon pression : - 1.0f
			return "-"+1.0f;		
		}else{
			for(ElasticTube parent:getParents()){
				SimpleVariable parentPressure = findVariableWithName(((Vein)parent).getPressure().getName(),variables);
				if(v.getName().equals(parentPressure.getName())){
					// derive selon parent pressure : 1.0
					return ""+1.0f;
				}else{
					SimpleVariable parentFlowout = findVariableWithName(((Vein)parent).getFlowout().getName(),variables);
					if(v.getName().equals(parentFlowout.getName())){
						// derive selon parent flow out :  -R_T8_alfa;
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
	private float getInitialConnectivityEquation(ArrayList<SimpleVariable> parentFlowout, SimpleVariable sasPressure, SimpleVariable pr, SimpleVariable fi){
		// equ(54)
		float res = 0;
		for(SimpleVariable pf : parentFlowout){
			res += pf.getValue();
		}
		res = res + ModelSpecification.k1.getValue() * (sasPressure.getValue() - pr.getValue());
		return (res - fi.getValue());
	}

	private String getSymbolicInitialConnectivityEquation(ArrayList<SimpleVariable> parentFlowout, SimpleVariable sasPressure, SimpleVariable pr, SimpleVariable fi){
		// equ(54)
		String res = "(";
		for(SimpleVariable pf : parentFlowout){
			if(!res.equals("("))
				res += "+";
			res += pf.getName();
		}
		res += " + "+ModelSpecification.k1.getName()+" * ("+sasPressure.getName()+" - "+pr.getName()+"))";
		return res+" - ("+fi.getName()+")";
	}

	private float getInitialConnectivityDerivative(SimpleVariable v, ArrayList<SimpleVariable> variables) throws Exception{
		// equ(54)
		if(v.getName().equals(getFlowin().getName())){
			// derive selon flowin : -1;
			return -1.0f;
		}else{
			if(v.getName().equals(getPressure().getName())){
				// derive selon la pression : -k1;
				return -ModelSpecification.k1.getValue();
			}else{
				ArrayList<SimpleVariable> psas = findVariableWithStruct(Hemisphere.BOTH, SAS.TUBE_NUM, PRESSURE_LABEL, variables);
				if(v.getName().equals(psas.get(0).getName())){
					// derive selon la pression : k1;
					return ModelSpecification.k1.getValue();
				}else{
					for(ElasticTube parent:getParents()){
						SimpleVariable pr = findVariableWithName(((Vein)parent).getFlowout().getName(),variables);
						if(v.getName().equals(pr.getName())){
							// derive selon flowoutParent :  1.0f
							return 1.0f;		
						}
					}
					return 0.0f;
				}
			}
		}
	}

	private String getSymbolicInitialConnectivityDerivative(SimpleVariable v, ArrayList<SimpleVariable> variables) throws Exception{
		// equ(54)
		if(v.getName().equals(getFlowin().getName())){
			// derive selon flowin : -1;
			return "-"+1.0f;
		}else{
			if(v.getName().equals(getPressure().getName())){
				// derive selon la pression : -k1;
				return "-"+ModelSpecification.k1.getName();
			}else{
				ArrayList<SimpleVariable> psas = findVariableWithStruct(Hemisphere.BOTH, SAS.TUBE_NUM, PRESSURE_LABEL, variables);
				if(v.getName().equals(psas.get(0).getName())){
					// derive selon la pression : k1;
					return ModelSpecification.k1.getName();
				}else{
					for(ElasticTube parent:getParents()){
						SimpleVariable pr = findVariableWithName(((Vein)parent).getFlowout().getName(),variables);
						if(v.getName().equals(pr.getName())){
							// derive selon flowoutParent :  1.0f
							return ""+1.0f;		
						}
					}
					return ""+0.0f;
				}
			}
		}
	}
	//====== additional momentum ====
	private float getInitialAddMomentumEquation(SimpleVariable pr, SimpleVariable fo){
		// equ(72)
		return (pr.getValue() - ModelSpecification.P_OUT.getValue()[(int) ModelSpecification.currentIter.getValue()]) - ModelSpecification.TPout_alfa.getValue() * fo.getValue();
	}

	private String getSymbolicInitialAddMomentumEquation(SimpleVariable pr, SimpleVariable fo){
		// equ(72)
		return "("+pr.getName()+" - "+ModelSpecification.P_OUT.getName()+"("+ModelSpecification.currentIter.getName()+")) - "+ModelSpecification.TPout_alfa.getName()+" * "+fo.getName();
	}

	private float getInitialAddMomentumDerivative(SimpleVariable v, ArrayList<SimpleVariable> variables) throws Exception{
		// equ(72)
		if(v.getName().equals(getFlowout().getName())){
			// derive selon flowout : - TPout_alfa;
			return -ModelSpecification.TPout_alfa.getValue();
		}else{
			if(v.getName().equals(getPressure().getName())){
				// derive selon la pression : 1;
				return 1.0f;
			}else{
				return 0.0f;
			}
		}
	}

	private String getSymbolicInitialAddMomentumDerivative(SimpleVariable v, ArrayList<SimpleVariable> variables) throws Exception{
		// equ(72)
		if(v.getName().equals(getFlowout().getName())){
			// derive selon flowout : - TPout_alfa;
			return "-"+ModelSpecification.TPout_alfa.getName();
		}else{
			if(v.getName().equals(getPressure().getName())){
				// derive selon la pression : 1;
				return ""+1.0f;
			}else{
				return ""+0.0f;
			}
		}
	}
}
