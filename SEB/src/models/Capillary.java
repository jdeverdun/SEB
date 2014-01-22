package models;

import java.util.ArrayList;

import params.ModelSpecification;

public class Capillary extends ElasticTube {
	public static final String TUBE_NUM = "2";
	public static final float DEFAULT_LENGTH = 0.2618f;
	public static final float DEFAULT_AREA = 38.0f;
	public static final float DEFAULT_ELASTANCE = 8500f * 1333.2240f;// en Pa
	public static final float DEFAULT_ALPHA = 9.23508188f * 1333.2240f;
	public static final float DEFAULT_FLOWIN = 13.0f;
	public static final float DEFAULT_FLOWOUT = 13.0f;
	public static final float DEFAULT_PRESSURE = 20.0f * 1333.2240f;

	public Capillary(String name, Hemisphere hemi) {
		super(name, hemi, DEFAULT_LENGTH,DEFAULT_AREA,DEFAULT_ALPHA,DEFAULT_ELASTANCE, DEFAULT_FLOWIN, DEFAULT_FLOWOUT, DEFAULT_PRESSURE);
	}

	public Capillary(String name, Hemisphere hemi, float len, float a) {
		super(name, hemi, len, a, DEFAULT_ALPHA, DEFAULT_ELASTANCE, DEFAULT_FLOWIN, DEFAULT_FLOWOUT, DEFAULT_PRESSURE);
	}

	public Capillary(String name, Hemisphere hemi, float len, float a, float alpha, float elast, float fin, float fout, float press) {
		super(name, hemi, len, a, alpha, elast, fin, fout, press);
	}


	public Capillary(String name, Hemisphere hemi, float len, float a, float alpha, float elast, float fin, float fout, float press, ArrayList<ElasticTube> par,
			ArrayList<ElasticTube> child) {
		super(name, hemi, len, a, alpha, elast, fin, fout, press, par, child);
	}

	public String toString(){
		return "Capillary : "+super.toString();
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
		for(ElasticTube parent:getParents()){
			SimpleVariable parentPressure = findVariableWithName(((Arteriole)parent).getPressure().getName(),variables);
			float[] momentum = new float[variables.size()+1];
			momentum[0] = getInitialMomentumEquation(fi, pr, parentPressure);
			for(int i = 0; i<variables.size();i++){
				momentum[i+1] = getInitialMomentumDerivative(variables.get(i), variables);
			}
			res.add(momentum);
		}

		// Connectivity
		float[] connectivity = new float[variables.size()+1];
		connectivity[0] = getInitialConnectivityEquation(fi);
		for(int i = 0; i<variables.size();i++){
			connectivity[i+1] = getInitialConnectivityDerivative(variables.get(i), variables);
		}
		res.add(connectivity);
		
		// bilan connectivity
		ArrayList<SimpleVariable> folist = findVariableWithStruct(getHemisphere(),Capillary.TUBE_NUM, FLOWOUT_LABEL,variables);
		ArrayList<SimpleVariable> ft4list = findVariableWithStruct(getHemisphere(),Ventricle.TUBE_NUM,FLOWIN_LABEL ,variables);
		ArrayList<SimpleVariable> ft3list = findVariableWithStruct(getHemisphere(),Veinule.TUBE_NUM,FLOWIN_LABEL ,variables);
		//ArrayList<SimpleVariable> ft3blist = findVariableWithStruct(getHemisphere(),Vein.TUBE_NUM,FLOWIN_LABEL ,variables);
		ArrayList<SimpleVariable> filist = new ArrayList<SimpleVariable>();
		filist.addAll(ft4list);
		//filist.addAll(ft3blist);
		filist.addAll(ft3list);
		SimpleVariable bfin1 = findVariableWithName(getAssociatedBrainParenchyma().getFlowin1().getName(), variables);
		SimpleVariable bfin2 = findVariableWithName(getAssociatedBrainParenchyma().getFlowin2().getName(), variables);
		filist.add(bfin1);
		filist.add(bfin2);
		float[] bilanconnectivity = new float[variables.size()+1];
		bilanconnectivity[0] = getInitialBilanConnectivityEquation(folist, filist);
		for(int i = 0; i<variables.size();i++){
			bilanconnectivity[i+1] = getInitialBilanConnectivityDerivative(variables.get(i), folist, filist);
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
		SimpleVariable pbrain = findVariableWithName(getAssociatedBrainParenchyma().getPressure().getName(),variables);
		distensibility[0] = getDistensibilityEquation(ar, pr, pbrain);
		for(int i = 0; i<variables.size();i++){
			distensibility[i+1] = getDistensibilityDerivative(variables.get(i), variables);
		}
		res.add(distensibility);

		// momentum
		for(ElasticTube parent:getParents()){
			SimpleVariable parentPressure = findVariableWithName(((Arteriole)parent).getPressure().getName(),variables);
			float[] momentum = new float[variables.size()+1];
			momentum[0] = getMomentumEquation(fi, ar, pr, parentPressure);
			for(int i = 0; i<variables.size();i++){
				momentum[i+1] = getMomentumDerivative(variables.get(i), variables);
			}
			res.add(momentum);
		}

		// Connectivity
		float[] connectivity = new float[variables.size()+1];
		connectivity[0] = getConnectivityEquation(fi);
		for(int i = 0; i<variables.size();i++){
			connectivity[i+1] = getConnectivityDerivative(variables.get(i), variables);
		}
		res.add(connectivity);
		
		// bilan connectivity
		ArrayList<SimpleVariable> folist = findVariableWithStruct(getHemisphere(),Capillary.TUBE_NUM, FLOWOUT_LABEL,variables);
		ArrayList<SimpleVariable> ft4list = findVariableWithStruct(getHemisphere(),Ventricle.TUBE_NUM,FLOWIN_LABEL ,variables);
		ArrayList<SimpleVariable> ft3list = findVariableWithStruct(getHemisphere(),Veinule.TUBE_NUM,FLOWIN_LABEL ,variables);
		//ArrayList<SimpleVariable> ft3blist = findVariableWithStruct(getHemisphere(),Vein.TUBE_NUM,FLOWIN_LABEL ,variables);
		ArrayList<SimpleVariable> filist = new ArrayList<SimpleVariable>();
		filist.addAll(ft4list);
		//filist.addAll(ft3blist);
		filist.addAll(ft3list);
		SimpleVariable bfin1 = findVariableWithName(getAssociatedBrainParenchyma().getFlowin1().getName(), variables);
		SimpleVariable bfin2 = findVariableWithName(getAssociatedBrainParenchyma().getFlowin2().getName(), variables);
		filist.add(bfin1);
		filist.add(bfin2);
		float[] bilanconnectivity = new float[variables.size()+1];
		bilanconnectivity[0] = getBilanConnectivityEquation(folist, filist);
		for(int i = 0; i<variables.size();i++){
			bilanconnectivity[i+1] = getBilanConnectivityDerivative(variables.get(i), folist, filist);
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
		SimpleVariable pbrain = findVariableWithName(getAssociatedBrainParenchyma().getPressure().getName(),variables);
		distensibility[0] = getSymbolicInitialDistensibilityEquation(ar, pr, pbrain);
		for(int i = 0; i<variables.size();i++){
			distensibility[i+1] = getSymbolicInitialDistensibilityDerivative(variables.get(i), variables);
		}
		res.add(distensibility);

		// momentum
		for(ElasticTube parent:getParents()){
			SimpleVariable parentPressure = findVariableWithName(((Arteriole)parent).getPressure().getName(),variables);
			String[] momentum = new String[variables.size()+1];
			momentum[0] = getSymbolicInitialMomentumEquation(fi, pr, parentPressure);
			for(int i = 0; i<variables.size();i++){
				momentum[i+1] = getSymbolicInitialMomentumDerivative(variables.get(i), variables);
			}
			res.add(momentum);
		}

		// connectivity
		String[] connectivity = new String[variables.size()+1];
		connectivity[0] = getSymbolicInitialConnectivityEquation(fi);
		for(int i = 0; i<variables.size();i++){
			connectivity[i+1] = getSymbolicInitialConnectivityDerivative(variables.get(i), variables);
		}
		res.add(connectivity);
		
		// bilan connectivity
		ArrayList<SimpleVariable> folist = findVariableWithStruct(getHemisphere(),Capillary.TUBE_NUM, FLOWOUT_LABEL,variables);
		ArrayList<SimpleVariable> ft4list = findVariableWithStruct(getHemisphere(),Ventricle.TUBE_NUM,FLOWIN_LABEL ,variables);
		ArrayList<SimpleVariable> ft3list = findVariableWithStruct(getHemisphere(),Veinule.TUBE_NUM,FLOWIN_LABEL ,variables);
		//ArrayList<SimpleVariable> ft3blist = findVariableWithStruct(getHemisphere(),Vein.TUBE_NUM,FLOWIN_LABEL ,variables);
		ArrayList<SimpleVariable> filist = new ArrayList<SimpleVariable>();
		filist.addAll(ft4list);
		//filist.addAll(ft3blist);
		filist.addAll(ft3list);
		SimpleVariable bfin1 = findVariableWithName(getAssociatedBrainParenchyma().getFlowin1().getName(), variables);
		SimpleVariable bfin2 = findVariableWithName(getAssociatedBrainParenchyma().getFlowin2().getName(), variables);
		filist.add(bfin1);
		filist.add(bfin2);
		String[] bilanconnectivity = new String[variables.size()+1];
		bilanconnectivity[0] = getSymbolicInitialBilanConnectivityEquation(folist, filist);
		for(int i = 0; i<variables.size();i++){
			bilanconnectivity[i+1] = getSymbolicInitialBilanConnectivityDerivative(variables.get(i), folist, filist);
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
		SimpleVariable pbrain = findVariableWithName(getAssociatedBrainParenchyma().getPressure().getName(),variables);
		distensibility[0] = getSymbolicDistensibilityEquation(ar, pr, pbrain);
		for(int i = 0; i<variables.size();i++){
			distensibility[i+1] = getSymbolicDistensibilityDerivative(variables.get(i), variables);
		}
		res.add(distensibility);

		// momentum
		for(ElasticTube parent:getParents()){
			SimpleVariable parentPressure = findVariableWithName(((Arteriole)parent).getPressure().getName(),variables);
			String[] momentum = new String[variables.size()+1];
			momentum[0] = getSymbolicMomentumEquation(fi, ar, pr, parentPressure);
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
		
		// bilan connectivity
		ArrayList<SimpleVariable> folist = findVariableWithStruct(getHemisphere(),Capillary.TUBE_NUM, FLOWOUT_LABEL,variables);
		ArrayList<SimpleVariable> ft4list = findVariableWithStruct(getHemisphere(),Ventricle.TUBE_NUM,FLOWIN_LABEL ,variables);
		ArrayList<SimpleVariable> ft3list = findVariableWithStruct(getHemisphere(),Veinule.TUBE_NUM,FLOWIN_LABEL ,variables);
		//ArrayList<SimpleVariable> ft3blist = findVariableWithStruct(getHemisphere(),Vein.TUBE_NUM,FLOWIN_LABEL ,variables);
		ArrayList<SimpleVariable> filist = new ArrayList<SimpleVariable>();
		filist.addAll(ft4list);
		//filist.addAll(ft3blist);
		filist.addAll(ft3list);
		SimpleVariable bfin1 = findVariableWithName(getAssociatedBrainParenchyma().getFlowin1().getName(), variables);
		SimpleVariable bfin2 = findVariableWithName(getAssociatedBrainParenchyma().getFlowin2().getName(), variables);
		filist.add(bfin1);
		filist.add(bfin2);
		String[] bilanconnectivity = new String[variables.size()+1];
		bilanconnectivity[0] = getSymbolicBilanConnectivityEquation(folist, filist);
		for(int i = 0; i<variables.size();i++){
			bilanconnectivity[i+1] = getSymbolicBilanConnectivityDerivative(variables.get(i), folist, filist);
		}
		res.add(bilanconnectivity);

		return res;
	}


	private float getContinuityEquation(SimpleVariable ar, SimpleVariable fi, SimpleVariable fo){
		// equ(3) et equ(8)
		return (ar.getValue() - getArea().getValue())/ModelSpecification.dt.getValue() + (- fi.getValue() + fo.getValue())/getLength().getValue();
	}

	private float getDistensibilityEquation(SimpleVariable ar, SimpleVariable pr, SimpleVariable pbrain){
		// equ(18) et equ(23)
		return -ModelSpecification.damp.getValue() * (ar.getValue() - getArea().getValue())/ModelSpecification.dt.getValue() + (pr.getValue()-pbrain.getValue())-getElastance().getValue()*(ar.getValue()/getInitialArea().getValue()-1);
	}

	private float getMomentumEquation(SimpleVariable fi, SimpleVariable ar, SimpleVariable pr, SimpleVariable parentPressure){
		// equ(33) et equ(38)
		return ModelSpecification.damp2.getValue() * ((fi.getValue()/ar.getValue()) - (getFlowin().getValue()/getArea().getValue()))/ModelSpecification.dt.getValue() + (parentPressure.getValue() - pr.getValue())-getAlpha().getValue()*fi.getValue();
	}

	// symbolic equation (en chaine de caractere)
	private String getSymbolicContinuityEquation(SimpleVariable ar, SimpleVariable fi, SimpleVariable fo){
		// equ(3) et equ(8)
		return "" + "("+ar.getName()+" - "+getArea().getName()+LAST_ROUND_SUFFIX+")/"+ModelSpecification.dt.getName()+""+" + (- "+fi.getName()+"+"+ fo.getName()+")/"+getLength().getName();
	}

	private String getSymbolicDistensibilityEquation(SimpleVariable ar, SimpleVariable pr, SimpleVariable pbrain){
		// equ(18) et equ(23)
		return " -"+ModelSpecification.damp.getName()+" * ("+ar.getName()+" - "+getArea().getName()+LAST_ROUND_SUFFIX+" )/"+ModelSpecification.dt.getName()+" + ("+pr.getName()+"-"+pbrain.getName()+" )-"+getElastance().getName()+" * ("+ar.getName()+" / "+getInitialArea().getName()+" -1)";
	}

	private String getSymbolicMomentumEquation(SimpleVariable fi, SimpleVariable ar, SimpleVariable pr, SimpleVariable parentPressure){
		// equ(33) et equ(38)
		return " "+ModelSpecification.damp2.getName()+" * (("+fi.getName()+" / "+ar.getName()+" ) - ("+getFlowin().getName()+LAST_ROUND_SUFFIX+" / "+getArea().getName()+LAST_ROUND_SUFFIX+" ))/ dt + ("+parentPressure.getName()+"-"+pr.getName()+" )-"+getAlpha().getName()+" * "+fi.getName();
	}

	// ------- Derive -----------
	private float getContinuityDerivative(SimpleVariable v, ArrayList<SimpleVariable> variables){
		// equ(3) et equ(8)

		if(v.getName().equals(getArea().getName())){
			// derive selon area : 1/"+ModelSpecification.dt.getName()+" 
			return 1.0f/ModelSpecification.dt.getValue();
		}else{
			if(v.getName().equals(getFlowin().getName())){
				// derive selon flowin : - 1/T2_l0;
				return -1.0f/getLength().getValue();
			}else{
				if(v.getName().equals(getFlowout().getName())){
					// derive selon flowout : 1/T2_l0
					return 1.0f/getLength().getValue();		
				}else{
					return 0.0f;
				}
			}
		}
	}

	private float getDistensibilityDerivative(SimpleVariable v, ArrayList<SimpleVariable> variables){
		// equ(18) et equ(23)

		if(v.getName().equals(getArea().getName())){
			// derive selon area : -damp/"+ModelSpecification.dt.getName()+" - T2_E * (1/T2_A0) 
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
		// equ(33) et equ(38)

		if(v.getName().equals(getFlowin().getName())){
			// derive selon flowin : damp2 * ((1/R_T2_A))/"+ModelSpecification.dt.getName()+" -T2_alfa ;
			SimpleVariable ar = findVariableWithName(getArea().getName(),variables);
			return ModelSpecification.damp2.getValue()*(1/ar.getValue())/ModelSpecification.dt.getValue() - getAlpha().getValue();
		}else{
			if(v.getName().equals(getArea().getName())){
				// derive selon area : damp2 * (-R_T2_fi/R_T2_A²)/"+ModelSpecification.dt.getName()+"
				SimpleVariable fi = findVariableWithName(getFlowin().getName(),variables);
				return (float) (ModelSpecification.damp2.getValue() * (-fi.getValue()/Math.pow(v.getValue(),2))/ModelSpecification.dt.getValue());
			}else{
				if(v.getName().equals(getPressure().getName())){
					// derive selon pression : - 1.0f
					return -1.0f;		
				}else{
					for(ElasticTube parent:getParents()){
						SimpleVariable pr = findVariableWithName(((Arteriole)parent).getPressure().getName(),variables);
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
		// equ(3) et equ(8)

		if(v.getName().equals(getArea().getName())){
			// derive selon area : 1/"+ModelSpecification.dt.getName()+" 
			return "" + 1.0f+"/"+ModelSpecification.dt.getName()+"";
		}else{
			if(v.getName().equals(getFlowin().getName())){
				// derive selon flowin : - 1/T2_l0;
				return "-"+1.0f+"/"+getLength().getName();
			}else{
				if(v.getName().equals(getFlowout().getName())){
					// derive selon flowout : 1/T2_l0
					return ""+1.0f+"/"+getLength().getName();		
				}else{
					return ""+0.0f;
				}
			}
		}
	}

	private String getSymbolicDistensibilityDerivative(SimpleVariable v, ArrayList<SimpleVariable> variables){
		// equ(18) et equ(23)

		if(v.getName().equals(getArea().getName())){
			// derive selon area : -damp/"+ModelSpecification.dt.getName()+" - T0_E * (1/T2_A0) 
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
		// equ(33) et equ(38)

		if(v.getName().equals(getFlowin().getName())){
			// derive selon flowin : damp2 * ((1/R_T2_A))/"+ModelSpecification.dt.getName()+" -T2_alfa ;
			SimpleVariable ar = findVariableWithName(getArea().getName(),variables);
			return ""+ModelSpecification.damp2.getName()+"*(1/"+ar.getName()+")/"+ModelSpecification.dt.getName()+" - "+getAlpha().getName();
		}else{
			if(v.getName().equals(getArea().getName())){
				// derive selon area : damp2 * (-R_T2_fi/R_T2_A²)/"+ModelSpecification.dt.getName()+"
				SimpleVariable fi = findVariableWithName(getFlowin().getName(),variables);
				return "("+ModelSpecification.damp2.getName()+" * (-"+fi.getName()+"/"+v.getName()+"^2)/"+ModelSpecification.dt.getName()+")";
			}else{
				if(v.getName().equals(getPressure().getName())){
					// derive selon pression : - 1.0f
					return ""+-1.0f;		
				}else{
					for(ElasticTube parent:getParents()){
						SimpleVariable pr = findVariableWithName(((Arteriole)parent).getPressure().getName(),variables);
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
	private float getConnectivityEquation(SimpleVariable fi){
		// equ(49) et equ(52)
		float res = 0;
		for(ElasticTube parent : getParents()){//for(Variable pf : parentFlowout){
			Arteriole par = ((Arteriole)parent);
			SimpleVariable pf = par.getFlowout();
			float fact = par.getChildren().size();
			res += (pf.getValue()/fact);
		}
		return (res - fi.getValue());
	}
	
	private String getSymbolicConnectivityEquation(SimpleVariable fi){
		// equ(49) et equ(52)
		String res = "(";
		for(ElasticTube parent : getParents()){//for(Variable pf : parentFlowout){
			if(!res.equals("("))
				res += "+";
			Arteriole par = ((Arteriole)parent);
			SimpleVariable pf = par.getFlowout();
			float fact = par.getChildren().size();
			res += "("+pf.getName()+"/"+fact+")";
		}
		res += ")";
		return "("+res+" - "+fi.getName()+")";
	}
	
	private float getConnectivityDerivative(SimpleVariable v, ArrayList<SimpleVariable> variables) throws Exception{
		// equ(49) et equ(52)
		if(v.getName().equals(getFlowin().getName())){
			// derive selon flowin : -1;
			return -1.0f;
		}else{
			for(ElasticTube parent:getParents()){
				SimpleVariable pr = findVariableWithName(((Arteriole)parent).getFlowout().getName(),variables);
				if(v.getName().equals(pr.getName())){
					// derive selon flowoutParent :  1.0f
					return 1.0f/(float)((Arteriole)parent).getChildren().size();		
				}
			}
			return 0.0f;
		}
	}
	
	private String getSymbolicConnectivityDerivative(SimpleVariable v, ArrayList<SimpleVariable> variables) throws Exception{
		// equ(49) et equ(52)
		if(v.getName().equals(getFlowin().getName())){
			// derive selon flowin : -1;
			return "-"+1.0f;
		}else{
			for(ElasticTube parent:getParents()){
				SimpleVariable pr = findVariableWithName(((Arteriole)parent).getFlowout().getName(),variables);
				if(v.getName().equals(pr.getName())){
					// derive selon flowoutParent :  1.0f
					return ""+1.0f+"/"+(float)((Arteriole)parent).getChildren().size();		
				}
			}
			return ""+0.0f;
		}
	}

	// ====== bilan connectivity =====
	private float getBilanConnectivityEquation(ArrayList<SimpleVariable> flowout, ArrayList<SimpleVariable> flowin){
		// equ(50) et equ(53)
		float resfo = 0;
		float resfi = 0;
		for(SimpleVariable pfo : flowout){
			resfo += pfo.getValue();
		}
		for(SimpleVariable pfi : flowin){
			resfi += pfi.getValue();
		}
		return (resfi - resfo);
	}
	
	private String getSymbolicBilanConnectivityEquation(ArrayList<SimpleVariable> flowout, ArrayList<SimpleVariable> flowin){
		// equ(50) et equ(53)
		String res = "";
		String res1 = "(";
		String res2 = "(";
		for(SimpleVariable pfo : flowout){
			if(!res1.equals("("))
				res1 += "+";
			res1 += pfo.getName();
		}
		res1 += ")";
		for(SimpleVariable pfi : flowin){
			if(!res2.equals("("))
				res2 += "+";
			res2 += pfi.getName();
		}
		res2 += ")";
		return res1+" - "+res2;
	}
	private float getBilanConnectivityDerivative(SimpleVariable v, ArrayList<SimpleVariable> flowout, ArrayList<SimpleVariable> flowin){
		// equ(50) et equ(53)
		if(flowout.contains(v)){
			// derive selon un flowout : 1
			return 1.0f;
		}else{
			if(flowin.contains(v)){
				// drive selon un flowin : -1
				return -1.0f;
			}
			return 0.0f;
		}
	}
	
	private String getSymbolicBilanConnectivityDerivative(SimpleVariable v, ArrayList<SimpleVariable> flowout, ArrayList<SimpleVariable> flowin){
		// equ(50) et equ(53)
		if(flowout.contains(v)){
			// derive selon un flowout : 1
			return ""+1.0f;
		}else{
			if(flowin.contains(v)){
				// drive selon un flowin : -1
				return "-"+1.0f;
			}
			return ""+0.0f;
		}
	}
	
	// ================= init ========================

	private float getInitialContinuityEquation(SimpleVariable fi, SimpleVariable fo){
		// eq (3)  (8)
		return fi.getValue() - fo.getValue();
	}
	private float getInitialContinuityDerivative(SimpleVariable v, ArrayList<SimpleVariable> variables){
		// eq (3)  (8)
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
		// eq (3)  (8)
		return fi.getName()+" - "+fo.getName();
	}
	private String getSymbolicInitialContinuityDerivative(SimpleVariable v, ArrayList<SimpleVariable> variables){
		// eq (3)  (8)
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
		// eq (18)  (23)
		return (pr.getValue() - pbrain.getValue()) - getElastance().getValue() * (ar.getValue()/getInitialArea().getValue() - 1);
	}
	private float getInitialDistensibilityDerivative(SimpleVariable v, ArrayList<SimpleVariable> variables){
		// eq (18)  (23)
		if(v.getName().equals(getArea().getName())){
			// derive selon area : - T2_E * (1/T2_A0) 
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
		// eq (18)  (23)
		return "("+pr.getName()+" - "+pbrain.getName()+") - "+getElastance().getName()+" * ("+ar.getName()+"/"+getInitialArea().getName()+" - 1)";
	}
	private String getSymbolicInitialDistensibilityDerivative(SimpleVariable v, ArrayList<SimpleVariable> variables){
		// eq (18)  (23)
		if(v.getName().equals(getArea().getName())){
			// derive selon area : - T2_E * (1/T2_A0) 
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
	private float getInitialMomentumEquation(SimpleVariable fi, SimpleVariable pr, SimpleVariable parentPressure){
		// eq (33)  (38)
		return (parentPressure.getValue() - pr.getValue())-getAlpha().getValue()*fi.getValue();
	}
	private float getInitialMomentumDerivative(SimpleVariable v, ArrayList<SimpleVariable> variables) throws Exception{
		// eq (33)  (38)
		if(v.getName().equals(getFlowin().getName())){
			// derive selon flowin : -T2_alfa ;
			return -getAlpha().getValue();
		}else{
			if(v.getName().equals(getPressure().getName())){
				// derive selon pression : - 1.0f
				return -1.0f;		
			}else{
				for(ElasticTube parent:getParents()){
					SimpleVariable pr = findVariableWithName(((Arteriole)parent).getPressure().getName(),variables);
					if(v.getName().equals(pr.getName())){
						// derive selon pressionParent :  1.0f
						return 1.0f;		
					}
				}
				return 0.0f;
			}
		}
	}
	private String getSymbolicInitialMomentumEquation(SimpleVariable fi, SimpleVariable pr, SimpleVariable parentPressure){
		// eq (33)  (38)
		return "("+parentPressure.getName()+" - "+pr.getName()+")-"+getAlpha().getName()+"*"+fi.getName();
	}
	private String getSymbolicInitialMomentumDerivative(SimpleVariable v, ArrayList<SimpleVariable> variables) throws Exception{
		// eq (33)  (38)
		if(v.getName().equals(getFlowin().getName())){
			// derive selon flowin : -T2_alfa ;
			return "-"+getAlpha().getValue();
		}else{
			if(v.getName().equals(getPressure().getName())){
				// derive selon pression : - 1.0f
				return "-"+1.0f;		
			}else{
				for(ElasticTube parent:getParents()){
					SimpleVariable pr = findVariableWithName(((Arteriole)parent).getPressure().getName(),variables);
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
	private float getInitialConnectivityEquation(SimpleVariable fi){
		// equ(49) et equ(52)
		float res = 0;
		for(ElasticTube parent : getParents()){//for(Variable pf : parentFlowout){
			Arteriole par = ((Arteriole)parent);
			SimpleVariable pf = par.getFlowout();
			float fact = par.getChildren().size();
			res += (pf.getValue()/fact);
		}
		return (res - fi.getValue());
	}

	private String getSymbolicInitialConnectivityEquation(SimpleVariable fi){
		// equ(49) et equ(52)
		String res = "(";
		for(ElasticTube parent : getParents()){//for(Variable pf : parentFlowout){
			if(!res.equals("("))
				res += "+";
			Arteriole par = ((Arteriole)parent);
			SimpleVariable pf = par.getFlowout();
			float fact = par.getChildren().size();
			res += "("+pf.getName()+"/"+fact+")";
		}
		res += ")";
		return "("+res+" - "+fi.getName()+")";
	}

	private float getInitialConnectivityDerivative(SimpleVariable v, ArrayList<SimpleVariable> variables) throws Exception{
		// equ(49) et equ(52)
		if(v.getName().equals(getFlowin().getName())){
			// derive selon flowin : -1;
			return -1.0f;
		}else{
			for(ElasticTube parent:getParents()){
				SimpleVariable pr = findVariableWithName(((Arteriole)parent).getFlowout().getName(),variables);
				if(v.getName().equals(pr.getName())){
					// derive selon flowoutParent :  1.0f
					return 1.0f/(float)((Arteriole)parent).getChildren().size();		
				}
			}
			return 0.0f;
		}
	}

	private String getSymbolicInitialConnectivityDerivative(SimpleVariable v, ArrayList<SimpleVariable> variables) throws Exception{
		// equ(49) et equ(52)
		if(v.getName().equals(getFlowin().getName())){
			// derive selon flowin : -1;
			return "-"+1.0f;
		}else{
			for(ElasticTube parent:getParents()){
				SimpleVariable pr = findVariableWithName(((Arteriole)parent).getFlowout().getName(),variables);
				if(v.getName().equals(pr.getName())){
					// derive selon flowoutParent :  1.0f
					return ""+1.0f+"/"+(float)((Arteriole)parent).getChildren().size();		
				}
			}
			return ""+0.0f;
		}
	}
	
	// ====== bilan connectivity =====
	private float getInitialBilanConnectivityEquation(ArrayList<SimpleVariable> flowout, ArrayList<SimpleVariable> flowin){
		// equ(50) et equ(53)
		float resfo = 0;
		float resfi = 0;
		for(SimpleVariable pfo : flowout){
			resfo += pfo.getValue();
		}
		for(SimpleVariable pfi : flowin){
			resfi += pfi.getValue();
		}
		return (resfi - resfo);
	}

	private String getSymbolicInitialBilanConnectivityEquation(ArrayList<SimpleVariable> flowout, ArrayList<SimpleVariable> flowin){
		// equ(50) et equ(53)
		String res = "";
		String res1 = "(";
		String res2 = "(";
		for(SimpleVariable pfo : flowout){
			if(!res1.equals("("))
				res1 += "+";
			res1 += pfo.getName();
		}
		res1 += ")";
		for(SimpleVariable pfi : flowin){
			if(!res2.equals("("))
				res2 += "+";
			res2 += pfi.getName();
		}
		res2 += ")";
		return res1+" - "+res2;
	}
	private float getInitialBilanConnectivityDerivative(SimpleVariable v, ArrayList<SimpleVariable> flowout, ArrayList<SimpleVariable> flowin){
		// equ(50) et equ(53)
		if(flowout.contains(v)){
			// derive selon un flowout : 1
			return 1.0f;
		}else{
			if(flowin.contains(v)){
				// drive selon un flowin : -1
				return -1.0f;
			}
			return 0.0f;
		}
	}

	private String getSymbolicInitialBilanConnectivityDerivative(SimpleVariable v, ArrayList<SimpleVariable> flowout, ArrayList<SimpleVariable> flowin){
		// equ(50) et equ(53)
		if(flowout.contains(v)){
			// derive selon un flowout : 1
			return ""+1.0f;
		}else{
			if(flowin.contains(v)){
				// drive selon un flowin : -1
				return "-"+1.0f;
			}
			return ""+0.0f;
		}
	}
}
