package models;

import java.util.ArrayList;

public class BrainParenchyma extends Tube {
	public static final String TUBE_NUM = "";
	public static final float DEFAULT_LENGTH = 7.0f;// par hemisphere
	public static final float DEFAULT_AREA_FLUID = 30.0f;
	public static final float DEFAULT_AREA_SOLID = 70.0f;
	public static final float DEFAULT_ALPHA1 = 8152.42f * 1333.2240f;
	public static final float DEFAULT_ALPHA2 = 500.0f * 1333.2240f;
	public static final float DEFAULT_FLOWIN1 = 0.002f;
	public static final float DEFAULT_FLOWOUT1 = 0.002f;
	public static final float DEFAULT_PRESSURE = 13332.24f;
	public static final float DEFAULT_FLOWIN2 = 0.0005f;
	public static final float DEFAULT_FLOWOUT2 = 0.0005f;
	protected static String BRAIN_LABEL = "brain";
	protected static String LENGTH_LABEL = "l";
	protected static String AREA_FLUID_LABEL = "Af";
	protected static String AREA_SOLID_LABEL = "As";
	protected static String ALPHA1_LABEL = "alpha1";
	protected static String ALPHA2_LABEL = "alpha2";
	protected static String FLOWIN1_LABEL = "fin1";
	protected static String FLOWIN2_LABEL = "fin2";
	protected static String FLOWOUT1_LABEL = "fout1";
	protected static String FLOWOUT2_LABEL = "fout2";
	protected static String PRESSURE_LABEL = "P";
	protected static String INITIAL_AREAFLUID_LABEL = "Af0"; 
	protected static String INITIAL_AREASOLID_LABEL = "As0"; 
	private Hemisphere hemi;
	private SimpleVariable areaFluid;
	private SimpleVariable areaSolid;
	private SimpleVariable initialAreaFluid;
	private SimpleVariable initialAreaSolid;
	private SimpleVariable alpha1; // connection to capillaries
	private SimpleVariable alpha2; // connection to ventricles
	private SimpleVariable flowin1;
	private SimpleVariable flowin2;
	private SimpleVariable flowout1;
	private SimpleVariable flowout2;
	private SimpleVariable pressure;


	public BrainParenchyma(String name, Hemisphere lhemi) {
		super(name, DEFAULT_LENGTH);
		setHemi(lhemi);
		setInitialAreaFluid(DEFAULT_AREA_FLUID);
		setInitialAreaSolid(DEFAULT_AREA_SOLID);
		setAreaFluid(DEFAULT_AREA_FLUID);
		setAreaSolid(DEFAULT_AREA_SOLID);
		setAlpha1(DEFAULT_ALPHA1);
		setAlpha2(DEFAULT_ALPHA2);
		setFlowin1(DEFAULT_FLOWIN1);
		setFlowin2(DEFAULT_FLOWIN2);
		setFlowout1(DEFAULT_FLOWOUT1);
		setFlowout2(DEFAULT_FLOWOUT2);
		setPressure(DEFAULT_PRESSURE);
	}

	public BrainParenchyma(String name, float len, Hemisphere lhemi) {
		super(name, len);
		setHemi(lhemi);
		setInitialAreaFluid(DEFAULT_AREA_FLUID);
		setInitialAreaSolid(DEFAULT_AREA_SOLID);
		setAreaFluid(DEFAULT_AREA_FLUID);
		setAreaSolid(DEFAULT_AREA_SOLID);
		setAlpha1(DEFAULT_ALPHA1);
		setAlpha2(DEFAULT_ALPHA2);
		setFlowin1(DEFAULT_FLOWIN1);
		setFlowin2(DEFAULT_FLOWIN2);
		setFlowout1(DEFAULT_FLOWOUT1);
		setFlowout2(DEFAULT_FLOWOUT2);
		setPressure(DEFAULT_PRESSURE);
	}

	public BrainParenchyma(String name, float len, Hemisphere lhemi, float aFluid, float aSolid, float alf1, float alf2, float fin1, float fin2, float fout1, float fout2, float press) {
		super(name, len);
		setHemi(lhemi);
		setInitialAreaFluid(aFluid);
		setInitialAreaSolid(aSolid);
		setAreaFluid(aFluid);
		setAreaSolid(aSolid);
		setAlpha1(alf1);
		setAlpha2(alf2);
		setFlowin1(fin1);
		setFlowin2(fin2);
		setFlowout1(fout1);
		setFlowout2(fout2);
		setPressure(press);
	}

	@Override
	public String getTubeNum() {
		return TUBE_NUM;
	}

	protected SimpleVariable getLength() {
		return length;
	}

	protected void setLength(float length) {
		this.length = new SimpleVariable(TUBE_LABEL+getTubeNum()+"_"+LENGTH_LABEL+BRAIN_LABEL,length, (Tube)this);
	}

	public SimpleVariable getAreaFluid() {
		return areaFluid;
	}

	public void setAreaFluid(float areaFluid) {
		String prefix = "";
		if(getHemi() == Hemisphere.LEFT)
			prefix = "L_";
		else
			if(getHemi() == Hemisphere.RIGHT)
				prefix = "R_";
		SimpleVariable v = new SimpleVariable(prefix+TUBE_LABEL+getTubeNum()+"_"+AREA_FLUID_LABEL+"_"+BRAIN_LABEL,areaFluid, (Tube)this);
		this.areaFluid = v;
	}

	public SimpleVariable getAreaSolid() {
		return areaSolid;
	}

	public void setAreaSolid(float areaSolid) {
		String prefix = "";
		if(getHemi() == Hemisphere.LEFT)
			prefix = "L_";
		else
			if(getHemi() == Hemisphere.RIGHT)
				prefix = "R_";
		SimpleVariable v = new SimpleVariable(prefix+TUBE_LABEL+getTubeNum()+"_"+AREA_SOLID_LABEL+"_"+BRAIN_LABEL,areaSolid, (Tube)this);
		this.areaSolid = v;
	}

	public SimpleVariable getAlpha1() {
		return alpha1;
	}

	public void setAlpha1(float alpha1) {
		SimpleVariable v = new SimpleVariable(TUBE_LABEL+getTubeNum()+"_"+ALPHA1_LABEL+"_"+BRAIN_LABEL,alpha1, (Tube)this);
		this.alpha1 = v;
	}

	public SimpleVariable getAlpha2() {
		return alpha2;
	}

	public void setAlpha2(float alpha2) {
		SimpleVariable v = new SimpleVariable(TUBE_LABEL+getTubeNum()+"_"+ALPHA2_LABEL+"_"+BRAIN_LABEL,alpha2, (Tube)this);
		this.alpha2 = v;
	}

	public SimpleVariable getFlowin1() {
		return flowin1;
	}

	public void setFlowin1(float flowin1) {
		String prefix = "";
		if(getHemi() == Hemisphere.LEFT)
			prefix = "L_";
		else
			if(getHemi() == Hemisphere.RIGHT)
				prefix = "R_";
		SimpleVariable v = new SimpleVariable(prefix+TUBE_LABEL+getTubeNum()+"_"+FLOWIN1_LABEL+"_"+BRAIN_LABEL,flowin1, (Tube)this);
		this.flowin1 = v;
	}

	public SimpleVariable getFlowin2() {
		return flowin2;
	}

	public void setFlowin2(float flowin2) {
		String prefix = "";
		if(getHemi() == Hemisphere.LEFT)
			prefix = "L_";
		else
			if(getHemi() == Hemisphere.RIGHT)
				prefix = "R_";
		SimpleVariable v = new SimpleVariable(prefix+TUBE_LABEL+getTubeNum()+"_"+FLOWIN2_LABEL+"_"+BRAIN_LABEL,flowin2, (Tube)this);
		this.flowin2 = v;
	}

	public SimpleVariable getFlowout1() {
		return flowout1;
	}

	public void setFlowout1(float flowout1) {
		String prefix = "";
		if(getHemi() == Hemisphere.LEFT)
			prefix = "L_";
		else
			if(getHemi() == Hemisphere.RIGHT)
				prefix = "R_";
		SimpleVariable v = new SimpleVariable(prefix+TUBE_LABEL+getTubeNum()+"_"+FLOWOUT1_LABEL+"_"+BRAIN_LABEL,flowout1, (Tube)this);
		this.flowout1 = v;
	}

	public SimpleVariable getFlowout2() {
		return flowout2;
	}

	public void setFlowout2(float flowout2) {
		String prefix = "";
		if(getHemi() == Hemisphere.LEFT)
			prefix = "L_";
		else
			if(getHemi() == Hemisphere.RIGHT)
				prefix = "R_";
		SimpleVariable v = new SimpleVariable(prefix+TUBE_LABEL+getTubeNum()+"_"+FLOWOUT2_LABEL+"_"+BRAIN_LABEL,flowout2, (Tube)this);
		this.flowout2 = v;
	}

	public SimpleVariable getPressure() {
		return pressure;
	}

	public void setPressure(float pressure) {
		String prefix = "";
		if(getHemi() == Hemisphere.LEFT)
			prefix = "L_";
		else
			if(getHemi() == Hemisphere.RIGHT)
				prefix = "R_";
		SimpleVariable v = new SimpleVariable(prefix+TUBE_LABEL+getTubeNum()+"_"+PRESSURE_LABEL+"_"+BRAIN_LABEL,pressure, (Tube)this);
		this.pressure = v;
	}

	/**
	 * @return the initialAreaFluid
	 */
	public SimpleVariable getInitialAreaFluid() {
		return initialAreaFluid;
	}

	/**
	 * @param initialAreaFluid the initialAreaFluid to set
	 */
	public void setInitialAreaFluid(float initialAreaFluid) {
		String prefix = "";
		if(getHemi() == Hemisphere.LEFT)
			prefix = "L_";
		else
			if(getHemi() == Hemisphere.RIGHT)
				prefix = "R_";
		SimpleVariable v = new SimpleVariable(prefix+TUBE_LABEL+getTubeNum()+"_"+INITIAL_AREAFLUID_LABEL+"_"+BRAIN_LABEL,initialAreaFluid, (Tube)this);
		this.initialAreaFluid = v;
	}

	/**
	 * @return the initialAreaSolid
	 */
	public SimpleVariable getInitialAreaSolid() {
		return initialAreaSolid;
	}

	/**
	 * @param initialAreaSolid the initialAreaSolid to set
	 */
	public void setInitialAreaSolid(float initialAreaSolid) {
		String prefix = "";
		if(getHemi() == Hemisphere.LEFT)
			prefix = "L_";
		else
			if(getHemi() == Hemisphere.RIGHT)
				prefix = "R_";
		SimpleVariable v = new SimpleVariable(prefix+TUBE_LABEL+getTubeNum()+"_"+INITIAL_AREASOLID_LABEL+"_"+BRAIN_LABEL,initialAreaSolid, (Tube)this);
		this.initialAreaSolid = v;
	}

	/**
	 * @return the hemi
	 */
	public Hemisphere getHemi() {
		return hemi;
	}

	/**
	 * @param hemi the hemi to set
	 */
	public void setHemi(Hemisphere hemi) {
		this.hemi = hemi;
	}

	public ArrayList<SimpleVariable> getVariables(){
		ArrayList<SimpleVariable> variables = new ArrayList<SimpleVariable>();
		variables.add(getFlowin1());
		variables.add(getFlowin2());
		variables.add(getFlowout1());
		variables.add(getFlowout2());
		variables.add(getPressure());
		variables.add(getAreaFluid());
		return variables;
	}
	public ArrayList<SimpleVariable> getFixedVariables(){
		ArrayList<SimpleVariable> variables = new ArrayList<SimpleVariable>();
		variables.add(getAlpha1());
		variables.add(getAlpha2());
		variables.add(getInitialAreaFluid());
		variables.add(getInitialAreaSolid());
		return variables;
	}
	
	public ArrayList<SimpleVariable> getGlobalVariables(){
		ArrayList<SimpleVariable> variables = new ArrayList<SimpleVariable>();
		return variables;
	}
	
	// ------------------- EQUATIONS -------------
	@Override
	public ArrayList<float[]> getInitialEquations(ArrayList<SimpleVariable> variables) throws Exception {
		ArrayList<float[]> res = new ArrayList<float[]>();

		// Connectivity
		float[] connectivity = new float[variables.size()+1];
		SimpleVariable fi1 = findVariableWithName(getFlowin1().getName(),variables);
		SimpleVariable fi2 = findVariableWithName(getFlowin2().getName(),variables);
		SimpleVariable fo1 = findVariableWithName(getFlowout1().getName(),variables);
		SimpleVariable fo2 = findVariableWithName(getFlowout2().getName(),variables);
		connectivity[0] = getInitialConnectivityEquation(fo1, fo2, fi1, fi2);
		for(int i = 0; i<variables.size();i++){
			connectivity[i+1] = getInitialConnectivityDerivative(variables.get(i), variables);
		}
		res.add(connectivity);

		// Momentum ventricle
		SimpleVariable pr = findVariableWithName(getPressure().getName(),variables);
		ArrayList<SimpleVariable> pven = findVariableWithStruct(getHemi(), Ventricle.TUBE_NUM, ElasticTube.PRESSURE_LABEL, variables);
		float[] momentumvent = new float[variables.size()+1];
		momentumvent[0] = getInitialMomentumVentricleEquation(pven.get(0), getPressure(), getAlpha2(), fo1);
		for(int i = 0; i<variables.size();i++){
			momentumvent[i+1] = getInitialMomentumVentricleDerivative(variables.get(i), variables);
		}
		res.add(momentumvent);

		// Momentum cappillary

		ArrayList<SimpleVariable> pcap = findVariableWithStruct(getHemi(), Capillary.TUBE_NUM, ElasticTube.PRESSURE_LABEL, variables);
		for(SimpleVariable pc : pcap){
			float[] momentumcap = new float[variables.size()+1];
			momentumcap[0] = getInitialMomentumCappilaryEquation(pr, pc, getAlpha1(), fi1);
			for(int i = 0; i<variables.size();i++){
				momentumcap[i+1] = getInitialMomentumCappilaryDerivative(variables.get(i), variables);
			}
			res.add(momentumcap);
		}

		// Total volume
		ArrayList<SimpleVariable> arteryArea = findVariableWithStruct(getHemi(), Artery.TUBE_NUM, ElasticTube.AREA_LABEL, variables);
		ArrayList<SimpleVariable> arteriolArea = findVariableWithStruct(getHemi(), Arteriole.TUBE_NUM, ElasticTube.AREA_LABEL, variables);
		ArrayList<SimpleVariable> cappilaryArea = findVariableWithStruct(getHemi(), Capillary.TUBE_NUM, ElasticTube.AREA_LABEL, variables);
		ArrayList<SimpleVariable> veinuleArea = findVariableWithStruct(getHemi(), Veinule.TUBE_NUM, ElasticTube.AREA_LABEL, variables);
		ArrayList<SimpleVariable> veinArea = findVariableWithStruct(getHemi(), Vein.TUBE_NUM, ElasticTube.AREA_LABEL, variables);
		ArrayList<SimpleVariable> ventricleArea = findVariableWithStruct(getHemi(), Ventricle.TUBE_NUM, ElasticTube.AREA_LABEL, variables);
		ArrayList<SimpleVariable> vsinousArea = findVariableWithStruct(Hemisphere.BOTH, VenousSinus.TUBE_NUM, ElasticTube.AREA_LABEL, variables);
		ArrayList<SimpleVariable> thirdvArea = findVariableWithStruct(Hemisphere.BOTH, ThirdVentricle.TUBE_NUM, ElasticTube.AREA_LABEL, variables);
		ArrayList<SimpleVariable> fourthvArea = findVariableWithStruct(Hemisphere.BOTH, FourthVentricle.TUBE_NUM, ElasticTube.AREA_LABEL, variables);
		ArrayList<SimpleVariable> sasArea = findVariableWithStruct(Hemisphere.BOTH, SAS.TUBE_NUM, ElasticTube.AREA_LABEL, variables);
		SimpleVariable brainFluidArea = findVariableWithName(getAreaFluid().getName(), variables);
		float[] totalvol = new float[variables.size()+1];
		totalvol[0] = getInitialTotalVolumeEquation(arteryArea, arteriolArea, cappilaryArea, veinuleArea, veinArea, vsinousArea.get(0), ventricleArea.get(0), thirdvArea.get(0), fourthvArea.get(0), sasArea.get(0), brainFluidArea);
		for(int i = 0; i<variables.size();i++){
			totalvol[i+1] = getInitialTotalVolumeDerivative(variables.get(i), variables);
		}
		res.add(totalvol);

		// Additional equations
		float[] addfout = new float[variables.size()+1];
		addfout[0] =getInitialAdditionalFout2Equation(fo2);
		for(int i = 0; i<variables.size();i++){
			addfout[i+1] = getInitialAdditionalFout2Derivative(variables.get(i), variables);
		}
		res.add(addfout);

		float[] addfin = new float[variables.size()+1];
		addfin[0] =getInitialAdditionalFin2Equation(fi2);
		for(int i = 0; i<variables.size();i++){
			addfin[i+1] = getInitialAdditionalFin2Derivative(variables.get(i), variables);
		}
		res.add(addfin);

		return res;
	}
	
	@Override
	public ArrayList<float[]> getEquations(ArrayList<SimpleVariable> variables) throws Exception {
		ArrayList<float[]> res = new ArrayList<float[]>();

		// Connectivity
		float[] connectivity = new float[variables.size()+1];
		SimpleVariable fi1 = findVariableWithName(getFlowin1().getName(),variables);
		SimpleVariable fi2 = findVariableWithName(getFlowin2().getName(),variables);
		SimpleVariable fo1 = findVariableWithName(getFlowout1().getName(),variables);
		SimpleVariable fo2 = findVariableWithName(getFlowout2().getName(),variables);
		connectivity[0] = getConnectivityEquation(fo1, fo2, fi1, fi2);
		for(int i = 0; i<variables.size();i++){
			connectivity[i+1] = getConnectivityDerivative(variables.get(i), variables);
		}
		res.add(connectivity);

		// Momentum ventricle
		SimpleVariable pr = findVariableWithName(getPressure().getName(),variables);
		ArrayList<SimpleVariable> pven = findVariableWithStruct(getHemi(), Ventricle.TUBE_NUM, ElasticTube.PRESSURE_LABEL, variables);
		float[] momentumvent = new float[variables.size()+1];
		momentumvent[0] = getMomentumVentricleEquation(pven.get(0), getPressure(), getAlpha2(), fo1);
		for(int i = 0; i<variables.size();i++){
			momentumvent[i+1] = getMomentumVentricleDerivative(variables.get(i), variables);
		}
		res.add(momentumvent);

		// Momentum cappillary

		ArrayList<SimpleVariable> pcap = findVariableWithStruct(getHemi(), Capillary.TUBE_NUM, ElasticTube.PRESSURE_LABEL, variables);
		for(SimpleVariable pc : pcap){
			float[] momentumcap = new float[variables.size()+1];
			momentumcap[0] = getMomentumCappilaryEquation(pr, pc, getAlpha1(), fi1);
			for(int i = 0; i<variables.size();i++){
				momentumcap[i+1] = getMomentumCappilaryDerivative(variables.get(i), variables);
			}
			res.add(momentumcap);
		}

		// Total volume
		ArrayList<SimpleVariable> arteryArea = findVariableWithStruct(getHemi(), Artery.TUBE_NUM, ElasticTube.AREA_LABEL, variables);
		ArrayList<SimpleVariable> arteriolArea = findVariableWithStruct(getHemi(), Arteriole.TUBE_NUM, ElasticTube.AREA_LABEL, variables);
		ArrayList<SimpleVariable> cappilaryArea = findVariableWithStruct(getHemi(), Capillary.TUBE_NUM, ElasticTube.AREA_LABEL, variables);
		ArrayList<SimpleVariable> veinuleArea = findVariableWithStruct(getHemi(), Veinule.TUBE_NUM, ElasticTube.AREA_LABEL, variables);
		ArrayList<SimpleVariable> veinArea = findVariableWithStruct(getHemi(), Vein.TUBE_NUM, ElasticTube.AREA_LABEL, variables);
		ArrayList<SimpleVariable> ventricleArea = findVariableWithStruct(getHemi(), Ventricle.TUBE_NUM, ElasticTube.AREA_LABEL, variables);
		ArrayList<SimpleVariable> vsinousArea = findVariableWithStruct(Hemisphere.BOTH, VenousSinus.TUBE_NUM, ElasticTube.AREA_LABEL, variables);
		ArrayList<SimpleVariable> thirdvArea = findVariableWithStruct(Hemisphere.BOTH, ThirdVentricle.TUBE_NUM, ElasticTube.AREA_LABEL, variables);
		ArrayList<SimpleVariable> fourthvArea = findVariableWithStruct(Hemisphere.BOTH, FourthVentricle.TUBE_NUM, ElasticTube.AREA_LABEL, variables);
		ArrayList<SimpleVariable> sasArea = findVariableWithStruct(Hemisphere.BOTH, SAS.TUBE_NUM, ElasticTube.AREA_LABEL, variables);
		SimpleVariable brainFluidArea = findVariableWithName(getAreaFluid().getName(), variables);
		float[] totalvol = new float[variables.size()+1];
		totalvol[0] = getTotalVolumeEquation(arteryArea, arteriolArea, cappilaryArea, veinuleArea, veinArea, vsinousArea.get(0), ventricleArea.get(0), thirdvArea.get(0), fourthvArea.get(0), sasArea.get(0), brainFluidArea);
		for(int i = 0; i<variables.size();i++){
			totalvol[i+1] = getTotalVolumeDerivative(variables.get(i), variables);
		}
		res.add(totalvol);

		// Additional equations
		float[] addfout = new float[variables.size()+1];
		addfout[0] =getAdditionalFout2Equation(fo2);
		for(int i = 0; i<variables.size();i++){
			addfout[i+1] = getAdditionalFout2Derivative(variables.get(i), variables);
		}
		res.add(addfout);

		float[] addfin = new float[variables.size()+1];
		addfin[0] =getAdditionalFin2Equation(fi2);
		for(int i = 0; i<variables.size();i++){
			addfin[i+1] = getAdditionalFin2Derivative(variables.get(i), variables);
		}
		res.add(addfin);

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

		// Connectivity
		String[] connectivity = new String[variables.size()+1];
		SimpleVariable fi1 = findVariableWithName(getFlowin1().getName(),variables);
		SimpleVariable fi2 = findVariableWithName(getFlowin2().getName(),variables);
		SimpleVariable fo1 = findVariableWithName(getFlowout1().getName(),variables);
		SimpleVariable fo2 = findVariableWithName(getFlowout2().getName(),variables);
		connectivity[0] = getSymbolicInitialConnectivityEquation(fo1, fo2, fi1, fi2);
		for(int i = 0; i<variables.size();i++){
			connectivity[i+1] = getSymbolicInitialConnectivityDerivative(variables.get(i), variables);
		}
		res.add(connectivity);

		// Momentum ventricle
		ArrayList<SimpleVariable> pven = findVariableWithStruct(getHemi(), Ventricle.TUBE_NUM, ElasticTube.PRESSURE_LABEL, variables);
		String[] momentumvent = new String[variables.size()+1];
		momentumvent[0] = getSymbolicInitialMomentumVentricleEquation(pven.get(0), getPressure(), getAlpha2(), fo1);
		for(int i = 0; i<variables.size();i++){
			momentumvent[i+1] = getSymbolicInitialMomentumVentricleDerivative(variables.get(i), variables);
		}
		res.add(momentumvent);

		// Momentum cappillary
		ArrayList<SimpleVariable> pcap = findVariableWithStruct(getHemi(), Capillary.TUBE_NUM, ElasticTube.PRESSURE_LABEL, variables);
		for(SimpleVariable pc : pcap){
			String[] momentumcap = new String[variables.size()+1];
			momentumcap[0] = getSymbolicInitialMomentumCappilaryEquation(getPressure(), pc, getAlpha1(), fi1);
			for(int i = 0; i<variables.size();i++){
				momentumcap[i+1] = getSymbolicInitialMomentumCappilaryDerivative(variables.get(i), variables);
			}
			res.add(momentumcap);
		}

		// Total volume
		ArrayList<SimpleVariable> arteryArea = findVariableWithStruct(getHemi(), Artery.TUBE_NUM, ElasticTube.AREA_LABEL, variables);
		ArrayList<SimpleVariable> arteriolArea = findVariableWithStruct(getHemi(), Arteriole.TUBE_NUM, ElasticTube.AREA_LABEL, variables);
		ArrayList<SimpleVariable> cappilaryArea = findVariableWithStruct(getHemi(), Capillary.TUBE_NUM, ElasticTube.AREA_LABEL, variables);
		ArrayList<SimpleVariable> veinuleArea = findVariableWithStruct(getHemi(), Veinule.TUBE_NUM, ElasticTube.AREA_LABEL, variables);
		ArrayList<SimpleVariable> veinArea = findVariableWithStruct(getHemi(), Vein.TUBE_NUM, ElasticTube.AREA_LABEL, variables);
		ArrayList<SimpleVariable> ventricleArea = findVariableWithStruct(getHemi(), Ventricle.TUBE_NUM, ElasticTube.AREA_LABEL, variables);
		ArrayList<SimpleVariable> vsinousArea = findVariableWithStruct(Hemisphere.BOTH, VenousSinus.TUBE_NUM, ElasticTube.AREA_LABEL, variables);
		ArrayList<SimpleVariable> thirdvArea = findVariableWithStruct(Hemisphere.BOTH, ThirdVentricle.TUBE_NUM, ElasticTube.AREA_LABEL, variables);
		ArrayList<SimpleVariable> fourthvArea = findVariableWithStruct(Hemisphere.BOTH, FourthVentricle.TUBE_NUM, ElasticTube.AREA_LABEL, variables);
		ArrayList<SimpleVariable> sasArea = findVariableWithStruct(Hemisphere.BOTH, SAS.TUBE_NUM, ElasticTube.AREA_LABEL, variables);
		SimpleVariable brainFluidArea = findVariableWithName(getAreaFluid().getName(), variables);
		String[] totalvol = new String[variables.size()+1];
		totalvol[0] = getSymbolicInitialTotalVolumeEquation(arteryArea, arteriolArea, cappilaryArea, veinuleArea, veinArea, vsinousArea.get(0), ventricleArea.get(0), thirdvArea.get(0), fourthvArea.get(0), sasArea.get(0), brainFluidArea);
		for(int i = 0; i<variables.size();i++){
			totalvol[i+1] = getSymbolicInitialTotalVolumeDerivative(variables.get(i), variables);
		}
		res.add(totalvol);

		// Additional equations
		String[] addfout = new String[variables.size()+1];
		addfout[0] =getSymbolicInitialAdditionalFout2Equation(fo2);
		for(int i = 0; i<variables.size();i++){
			addfout[i+1] = getSymbolicInitialAdditionalFout2Derivative(variables.get(i), variables);
		}
		res.add(addfout);

		String[] addfin = new String[variables.size()+1];
		addfin[0] =getSymbolicInitialAdditionalFin2Equation(fi2);
		for(int i = 0; i<variables.size();i++){
			addfin[i+1] = getSymbolicInitialAdditionalFin2Derivative(variables.get(i), variables);
		}
		res.add(addfin);

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

		// Connectivity
		String[] connectivity = new String[variables.size()+1];
		SimpleVariable fi1 = findVariableWithName(getFlowin1().getName(),variables);
		SimpleVariable fi2 = findVariableWithName(getFlowin2().getName(),variables);
		SimpleVariable fo1 = findVariableWithName(getFlowout1().getName(),variables);
		SimpleVariable fo2 = findVariableWithName(getFlowout2().getName(),variables);
		connectivity[0] = getSymbolicConnectivityEquation(fo1, fo2, fi1, fi2);
		for(int i = 0; i<variables.size();i++){
			connectivity[i+1] = getSymbolicConnectivityDerivative(variables.get(i), variables);
		}
		res.add(connectivity);

		// Momentum ventricle
		ArrayList<SimpleVariable> pven = findVariableWithStruct(getHemi(), Ventricle.TUBE_NUM, ElasticTube.PRESSURE_LABEL, variables);
		String[] momentumvent = new String[variables.size()+1];
		momentumvent[0] = getSymbolicMomentumVentricleEquation(pven.get(0), getPressure(), getAlpha2(), fo1);
		for(int i = 0; i<variables.size();i++){
			momentumvent[i+1] = getSymbolicMomentumVentricleDerivative(variables.get(i), variables);
		}
		res.add(momentumvent);

		// Momentum cappillary
		ArrayList<SimpleVariable> pcap = findVariableWithStruct(getHemi(), Capillary.TUBE_NUM, ElasticTube.PRESSURE_LABEL, variables);
		for(SimpleVariable pc : pcap){
			String[] momentumcap = new String[variables.size()+1];
			momentumcap[0] = getSymbolicMomentumCappilaryEquation(getPressure(), pc, getAlpha1(), fi1);
			for(int i = 0; i<variables.size();i++){
				momentumcap[i+1] = getSymbolicMomentumCappilaryDerivative(variables.get(i), variables);
			}
			res.add(momentumcap);
		}

		// Total volume
		ArrayList<SimpleVariable> arteryArea = findVariableWithStruct(getHemi(), Artery.TUBE_NUM, ElasticTube.AREA_LABEL, variables);
		ArrayList<SimpleVariable> arteriolArea = findVariableWithStruct(getHemi(), Arteriole.TUBE_NUM, ElasticTube.AREA_LABEL, variables);
		ArrayList<SimpleVariable> cappilaryArea = findVariableWithStruct(getHemi(), Capillary.TUBE_NUM, ElasticTube.AREA_LABEL, variables);
		ArrayList<SimpleVariable> veinuleArea = findVariableWithStruct(getHemi(), Veinule.TUBE_NUM, ElasticTube.AREA_LABEL, variables);
		ArrayList<SimpleVariable> veinArea = findVariableWithStruct(getHemi(), Vein.TUBE_NUM, ElasticTube.AREA_LABEL, variables);
		ArrayList<SimpleVariable> ventricleArea = findVariableWithStruct(getHemi(), Ventricle.TUBE_NUM, ElasticTube.AREA_LABEL, variables);
		ArrayList<SimpleVariable> vsinousArea = findVariableWithStruct(Hemisphere.BOTH, VenousSinus.TUBE_NUM, ElasticTube.AREA_LABEL, variables);
		ArrayList<SimpleVariable> thirdvArea = findVariableWithStruct(Hemisphere.BOTH, ThirdVentricle.TUBE_NUM, ElasticTube.AREA_LABEL, variables);
		ArrayList<SimpleVariable> fourthvArea = findVariableWithStruct(Hemisphere.BOTH, FourthVentricle.TUBE_NUM, ElasticTube.AREA_LABEL, variables);
		ArrayList<SimpleVariable> sasArea = findVariableWithStruct(Hemisphere.BOTH, SAS.TUBE_NUM, ElasticTube.AREA_LABEL, variables);
		SimpleVariable brainFluidArea = findVariableWithName(getAreaFluid().getName(), variables);
		String[] totalvol = new String[variables.size()+1];
		totalvol[0] = getSymbolicTotalVolumeEquation(arteryArea, arteriolArea, cappilaryArea, veinuleArea, veinArea, vsinousArea.get(0), ventricleArea.get(0), thirdvArea.get(0), fourthvArea.get(0), sasArea.get(0), brainFluidArea);
		for(int i = 0; i<variables.size();i++){
			totalvol[i+1] = getSymbolicTotalVolumeDerivative(variables.get(i), variables);
		}
		res.add(totalvol);

		// Additional equations
		String[] addfout = new String[variables.size()+1];
		addfout[0] =getSymbolicAdditionalFout2Equation(fo2);
		for(int i = 0; i<variables.size();i++){
			addfout[i+1] = getSymbolicAdditionalFout2Derivative(variables.get(i), variables);
		}
		res.add(addfout);

		String[] addfin = new String[variables.size()+1];
		addfin[0] =getSymbolicAdditionalFin2Equation(fi2);
		for(int i = 0; i<variables.size();i++){
			addfin[i+1] = getSymbolicAdditionalFin2Derivative(variables.get(i), variables);
		}
		res.add(addfin);

		return res;
	}

	//====== Additional equation fout2 ====
	private float getAdditionalFout2Equation(SimpleVariable fout2){
		// equ(68) et equ(70)
		return fout2.getValue() - 0.0f;
	}

	private String getSymbolicAdditionalFout2Equation(SimpleVariable fout2){
		// equ(68) et equ(70)
		return ""+fout2.getName()+" - "+0.0f;
	}

	private float getAdditionalFout2Derivative(SimpleVariable v, ArrayList<SimpleVariable> variables) throws Exception{
		// equ(68) et equ(70)
		if(v.getName().equals(getFlowout2().getName())){
			// derive selon flow out1 : 1;
			return 1.0f;
		}else{
			return 0.0f;
		}
	}

	private String getSymbolicAdditionalFout2Derivative(SimpleVariable v, ArrayList<SimpleVariable> variables) throws Exception{
		// equ(68) et equ(70)
		if(v.getName().equals(getFlowout2().getName())){
			// derive selon flow out1 : 1;
			return ""+1.0f;
		}else{
			return ""+0.0f;
		}
	}

	//====== Additional equation fin1 ====
	private float getAdditionalFin2Equation(SimpleVariable fin1){
		// equ(69) et equ(70)
		return fin1.getValue() - 0.0f;
	}

	private String getSymbolicAdditionalFin2Equation(SimpleVariable fin1){
		// equ(69) et equ(70)
		return ""+fin1.getName()+" - "+0.0f;
	}

	private float getAdditionalFin2Derivative(SimpleVariable v, ArrayList<SimpleVariable> variables) throws Exception{
		// equ(69) et equ(70)
		if(v.getName().equals(getFlowin1().getName())){
			// derive selon flow in1 : 1;
			return 1.0f;
		}else{
			return 0.0f;
		}
	}

	private String getSymbolicAdditionalFin2Derivative(SimpleVariable v, ArrayList<SimpleVariable> variables) throws Exception{
		// equ(69) et equ(70)
		if(v.getName().equals(getFlowin1().getName())){
			// derive selon flow in1 : 1;
			return ""+1.0f;
		}else{
			return ""+0.0f;
		}
	}

	//====== Momentum ventricle ====
	private float getMomentumVentricleEquation(SimpleVariable ventriclePr, SimpleVariable pr, SimpleVariable alfa2, SimpleVariable fout1){
		// equ(62) et equ(66)
		return ventriclePr.getValue() - pr.getValue() + alfa2.getValue() * fout1.getValue();
	}

	private String getSymbolicMomentumVentricleEquation(SimpleVariable ventriclePr, SimpleVariable pr, SimpleVariable alfa2, SimpleVariable fout1){
		// equ(62) et equ(66)
		return ""+ventriclePr.getName()+" - "+pr.getName()+" + "+alfa2.getName()+" * "+fout1.getName();
	}

	private float getMomentumVentricleDerivative(SimpleVariable v, ArrayList<SimpleVariable> variables) throws Exception{
		// equ(62) et equ(66)
		if(v.getName().equals(getPressure().getName())){
			// derive selon pression : -1;
			return -1.0f;
		}else{
			if(v.getName().equals(getFlowout1().getName())){
				// derive selon flow out 1 : T_alpha2_brain ;
				return getAlpha2().getValue();
			}else{
				ArrayList<SimpleVariable> pven = findVariableWithStruct(getHemi(), Ventricle.TUBE_NUM, ElasticTube.PRESSURE_LABEL, variables);
				if(v.getName().equals(pven.get(0).getName())){
					// derive selon ventricule pression : 1;
					return 1.0f;
				}
				return 0.0f;
			}
		}
	}

	private String getSymbolicMomentumVentricleDerivative(SimpleVariable v, ArrayList<SimpleVariable> variables) throws Exception{
		// equ(62) et equ(66)
		if(v.getName().equals(getPressure().getName())){
			// derive selon pression : -1;
			return "-"+1.0f;
		}else{
			if(v.getName().equals(getFlowout1().getName())){
				// derive selon flow out 1 : T_alpha2_brain ;
				return ""+getAlpha2().getName();
			}else{
				ArrayList<SimpleVariable> pven = findVariableWithStruct(getHemi(), Ventricle.TUBE_NUM, ElasticTube.PRESSURE_LABEL, variables);
				if(v.getName().equals(pven.get(0).getName())){
					// derive selon ventricule pression : 1;
					return ""+1.0f;
				}
				return ""+0.0f;
			}
		}
	}

	//====== Momentum cappilaries ====
	private float getMomentumCappilaryEquation(SimpleVariable pr, SimpleVariable cappilaryPr, SimpleVariable alfa1, SimpleVariable fin1){
		// equ(61) et equ(65)
		return pr.getValue() - cappilaryPr.getValue() + getAlpha1().getValue() * fin1.getValue();
	}

	private String getSymbolicMomentumCappilaryEquation(SimpleVariable pr, SimpleVariable cappilaryPr, SimpleVariable alfa1, SimpleVariable fin1){
		// equ(61) et equ(65)
		return ""+pr.getName()+" - "+cappilaryPr.getName()+" + "+getAlpha1().getName()+" * "+fin1.getName();
	}

	private float getMomentumCappilaryDerivative(SimpleVariable v, ArrayList<SimpleVariable> variables) throws Exception{
		// equ(61) et equ(65)
		if(v.getName().equals(getPressure().getName())){
			// derive selon pression : 1;
			return 1.0f;
		}else{
			if(v.getName().equals(getFlowin1().getName())){
				// derive selon flow out 1 : T_alpha2_brain ;
				return getAlpha1().getValue();
			}else{
				String namecappi = buildNameFromStruct(getHemi(), Capillary.TUBE_NUM, ElasticTube.PRESSURE_LABEL);
				if(v.getName().startsWith(namecappi)){
					// derive selon cappilary pression : -1;
					return -1.0f;
				}
				return 0.0f;
			}
		}
	}

	private String getSymbolicMomentumCappilaryDerivative(SimpleVariable v, ArrayList<SimpleVariable> variables) throws Exception{
		// equ(61) et equ(65)
		if(v.getName().equals(getPressure().getName())){
			// derive selon pression : 1;
			return ""+1.0f;
		}else{
			if(v.getName().equals(getFlowin1().getName())){
				// derive selon flow out 1 : T_alpha2_brain ;
				return ""+getAlpha1().getName();
			}else{
				String namecappi = buildNameFromStruct(getHemi(), Capillary.TUBE_NUM, ElasticTube.PRESSURE_LABEL);
				if(v.getName().startsWith(namecappi)){
					// derive selon cappilary pression : -1;
					return "-"+1.0f;
				}
				return ""+0.0f;
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
	private float getConnectivityEquation(SimpleVariable fo1, SimpleVariable fo2, SimpleVariable fi1, SimpleVariable fi2){
		// equ(60) et equ(64)
		return fo1.getValue()+fo2.getValue()-fi1.getValue()-fi2.getValue();
	}

	private String getSymbolicConnectivityEquation(SimpleVariable fo1, SimpleVariable fo2, SimpleVariable fi1, SimpleVariable fi2){
		// equ(60) et equ(64)
		return ""+fo1.getName()+"+"+fo2.getName()+"-"+fi1.getName()+"-"+fi2.getName();
	}

	private float getConnectivityDerivative(SimpleVariable v, ArrayList<SimpleVariable> variables) throws Exception{
		// equ(60) et equ(64)
		if(v.getName().equals(getFlowout1().getName()) || v.getName().equals(getFlowout2().getName())){
			// derive selon flowout1 et 2 : 1;
			return 1.0f;
		}else{
			if(v.getName().equals(getFlowout1().getName()) || v.getName().equals(getFlowout2().getName())){
				// derive selon flowin1 et 2 : -1;
				return -1.0f;
			}
			return 0.0f;
		}
	}

	private String getSymbolicConnectivityDerivative(SimpleVariable v, ArrayList<SimpleVariable> variables) throws Exception{
		// equ(60) et equ(64)
		if(v.getName().equals(getFlowout1().getName()) || v.getName().equals(getFlowout2().getName())){
			// derive selon flowout1 et 2 : 1;
			return ""+1.0f;
		}else{
			if(v.getName().equals(getFlowout1().getName()) || v.getName().equals(getFlowout2().getName())){
				// derive selon flowin1 et 2 : -1;
				return "-"+1.0f;
			}
			return ""+0.0f;
		}
	}

	//====== Total volume ====
	private float getTotalVolumeEquation(ArrayList<SimpleVariable> arteryArea, ArrayList<SimpleVariable> arteriolArea, ArrayList<SimpleVariable> cappilaryArea, ArrayList<SimpleVariable> veinuleArea,
			ArrayList<SimpleVariable> veinArea,SimpleVariable vsinousArea, SimpleVariable ventricleArea,  SimpleVariable thirdvArea, SimpleVariable fourthvArea,
			SimpleVariable sasArea, SimpleVariable brainFluidArea){
		// equ(63) et equ(67)
		float res1 = 0;
		float res2 = 0;
		for(SimpleVariable arterya: arteryArea){
			res1 += (arterya.getValue() * ((Artery)arterya.getSourceObj()).getLength().getValue());
			res2 += (((Artery)arterya.getSourceObj()).getInitialArea().getValue() * ((Artery)arterya.getSourceObj()).getLength().getValue());  
		}
		for(SimpleVariable arteriola: arteriolArea){
			res1 += (arteriola.getValue() * ((Arteriole)arteriola.getSourceObj()).getLength().getValue());  
			res2 += (((Arteriole)arteriola.getSourceObj()).getInitialArea().getValue() * ((Arteriole)arteriola.getSourceObj()).getLength().getValue());  
		}
		for(SimpleVariable capa: cappilaryArea){
			res1 += (capa.getValue() * ((Capillary)capa.getSourceObj()).getLength().getValue());
			res2 += (((Capillary)capa.getSourceObj()).getInitialArea().getValue() * ((Capillary)capa.getSourceObj()).getLength().getValue());  
		}
		for(SimpleVariable vla: veinuleArea){
			res1 += (vla.getValue() * ((Veinule)vla.getSourceObj()).getLength().getValue());  
			res2 += (((Veinule)vla.getSourceObj()).getInitialArea().getValue() * ((Veinule)vla.getSourceObj()).getLength().getValue());  
		}
		for(SimpleVariable va: veinArea){
			res1 += (va.getValue() * ((Vein)va.getSourceObj()).getLength().getValue());
			res2 += (((Vein)va.getSourceObj()).getInitialArea().getValue() * ((Vein)va.getSourceObj()).getLength().getValue());  
		}
		res1 += (vsinousArea.getValue() * ((VenousSinus)vsinousArea.getSourceObj()).getLength().getValue());  
		res2 += (((VenousSinus)vsinousArea.getSourceObj()).getInitialArea().getValue() * ((VenousSinus)vsinousArea.getSourceObj()).getLength().getValue());  
		res1 += (ventricleArea.getValue() * ((Ventricle)ventricleArea.getSourceObj()).getLength().getValue()); 
		res2 += (((Ventricle)ventricleArea.getSourceObj()).getInitialArea().getValue() * ((Ventricle)ventricleArea.getSourceObj()).getLength().getValue());  
		res1 += (0.5f * (thirdvArea.getValue() * ((ThirdVentricle)thirdvArea.getSourceObj()).getLength().getValue())); 
		res2 += (0.5f * ((ThirdVentricle)thirdvArea.getSourceObj()).getInitialArea().getValue() * ((ThirdVentricle)thirdvArea.getSourceObj()).getLength().getValue());  
		res1 += (0.5f * (fourthvArea.getValue() * ((FourthVentricle)fourthvArea.getSourceObj()).getLength().getValue())); 
		res2 += (0.5f * ((FourthVentricle)fourthvArea.getSourceObj()).getInitialArea().getValue() * ((FourthVentricle)fourthvArea.getSourceObj()).getLength().getValue());  
		res1 += (0.5f * (sasArea.getValue() * ((SAS)sasArea.getSourceObj()).getLength().getValue())); 
		res2 += (0.5f * ((SAS)sasArea.getSourceObj()).getInitialArea().getValue() * ((SAS)sasArea.getSourceObj()).getLength().getValue());  
		res1 += ((brainFluidArea.getValue() + getInitialAreaSolid().getValue())* getLength().getValue());
		res2 += ((getInitialAreaFluid().getValue() + getInitialAreaSolid().getValue())* getLength().getValue());

		return res1 - res2;
	}

	private String getSymbolicTotalVolumeEquation(ArrayList<SimpleVariable> arteryArea, ArrayList<SimpleVariable> arteriolArea, ArrayList<SimpleVariable> cappilaryArea, ArrayList<SimpleVariable> veinuleArea,
			ArrayList<SimpleVariable> veinArea,SimpleVariable vsinousArea, SimpleVariable ventricleArea,  SimpleVariable thirdvArea, SimpleVariable fourthvArea,
			SimpleVariable sasArea, SimpleVariable brainFluidArea){
		// equ(63) et equ(67)
		String res1 = "(";
		String res2 = "(";
		for(SimpleVariable arterya: arteryArea){
			res1 += "("+arterya.getName()+" * "+((Artery)arterya.getSourceObj()).getLength().getName()+") + ";
			res2 += "("+((Artery)arterya.getSourceObj()).getInitialArea().getName()+" * "+((Artery)arterya.getSourceObj()).getLength().getName()+") + ";  
		}
		for(SimpleVariable arteriola: arteriolArea){
			res1 += "("+arteriola.getName() +" * "+ ((Arteriole)arteriola.getSourceObj()).getLength().getName()+") + ";  
			res2 += "("+((Arteriole)arteriola.getSourceObj()).getInitialArea().getName() +" * "+ ((Arteriole)arteriola.getSourceObj()).getLength().getName()+") + ";  
		}
		for(SimpleVariable capa: cappilaryArea){
			res1 += "("+capa.getName() +" * "+ ((Capillary)capa.getSourceObj()).getLength().getName()+") + ";
			res2 += "("+((Capillary)capa.getSourceObj()).getInitialArea().getName() +" * "+ ((Capillary)capa.getSourceObj()).getLength().getName()+") + ";  
		}
		for(SimpleVariable vla: veinuleArea){
			res1 += "("+vla.getName() +" * "+ ((Veinule)vla.getSourceObj()).getLength().getName()+") + ";  
			res2 += "("+((Veinule)vla.getSourceObj()).getInitialArea().getName() +" * "+ ((Veinule)vla.getSourceObj()).getLength().getName()+") + ";  
		}
		for(SimpleVariable va: veinArea){
			res1 += "("+va.getName() +" * "+ ((Vein)va.getSourceObj()).getLength().getName()+") + ";
			res2 += "("+((Vein)va.getSourceObj()).getInitialArea().getName() +" * "+ ((Vein)va.getSourceObj()).getLength().getName()+") + ";  
		}
		res1 += "("+vsinousArea.getName() +" * "+ ((VenousSinus)vsinousArea.getSourceObj()).getLength().getName()+") + ";  
		res2 += "("+((VenousSinus)vsinousArea.getSourceObj()).getInitialArea().getName() +" * "+ ((VenousSinus)vsinousArea.getSourceObj()).getLength().getName()+") + ";  
		res1 += "("+ventricleArea.getName() +" * "+ ((Ventricle)ventricleArea.getSourceObj()).getLength().getName()+") + "; 
		res2 += "("+((Ventricle)ventricleArea.getSourceObj()).getInitialArea().getName() +" * "+ ((Ventricle)ventricleArea.getSourceObj()).getLength().getName()+") + ";  
		res1 += "("+0.5f +" * "+ (thirdvArea.getName() +" * "+ ((ThirdVentricle)thirdvArea.getSourceObj()).getLength().getName())+") + "; 
		res2 += "("+0.5f +" * "+((ThirdVentricle)thirdvArea.getSourceObj()).getInitialArea().getName() +" * "+ ((ThirdVentricle)thirdvArea.getSourceObj()).getLength().getName()+") + ";  
		res1 += "("+0.5f +" * "+ (fourthvArea.getName()+" * "+ ((FourthVentricle)fourthvArea.getSourceObj()).getLength().getName())+") + "; 
		res2 += "("+0.5f +" * "+((FourthVentricle)fourthvArea.getSourceObj()).getInitialArea().getName() +" * "+ ((FourthVentricle)fourthvArea.getSourceObj()).getLength().getName()+") + ";  
		res1 += "("+0.5f +" * "+ (sasArea.getName() +" * "+ ((SAS)sasArea.getSourceObj()).getLength().getName())+") + "; 
		res2 += "("+0.5f +" * "+((SAS)sasArea.getSourceObj()).getInitialArea().getName() +" * "+ ((SAS)sasArea.getSourceObj()).getLength().getName()+") + ";  
		res1 += "(("+brainFluidArea.getName()+" + "+getInitialAreaSolid().getName() +")  * "+ getLength().getName()+")";
		res2 += "(("+getInitialAreaFluid().getName()+" + "+getInitialAreaSolid().getName() +")  * "+ getLength().getName()+")";

		return res1+")"+" - "+res2+")";
	}

	private float getTotalVolumeDerivative(SimpleVariable v, ArrayList<SimpleVariable> variables) throws Exception{
		// equ(63) et equ(67)
		if(v.getName().equals(getAreaFluid().getName())){
			// derive selon area brain : T0_lbrain ;
			return getLength().getValue();
		}else{
			String arteryname = buildNameFromStruct(getHemi(), Artery.TUBE_NUM, ElasticTube.AREA_LABEL);
			String arteriolname = buildNameFromStruct(getHemi(), Arteriole.TUBE_NUM, ElasticTube.AREA_LABEL);
			String capname = buildNameFromStruct(getHemi(), Capillary.TUBE_NUM, ElasticTube.AREA_LABEL);
			String vlname = buildNameFromStruct(getHemi(), Veinule.TUBE_NUM, ElasticTube.AREA_LABEL);
			String vname = buildNameFromStruct(getHemi(), Vein.TUBE_NUM, ElasticTube.AREA_LABEL);
			String ventname = buildNameFromStruct(getHemi(), Ventricle.TUBE_NUM, ElasticTube.AREA_LABEL);
			if(v.getName().startsWith(arteryname) || v.getName().startsWith(arteriolname) || v.getName().startsWith(capname)
					|| v.getName().startsWith(vlname) || v.getName().startsWith(vname) || v.getName().startsWith(ventname)){
				// derive selon area de tube localises dans un hemi : T1_l0  ;
				return v.getSourceObj().getLength().getValue();
			}else{
				String sinousname = buildNameFromStruct(Hemisphere.BOTH, VenousSinus.TUBE_NUM, ElasticTube.AREA_LABEL);
				String thirdname = buildNameFromStruct(Hemisphere.BOTH, ThirdVentricle.TUBE_NUM, ElasticTube.AREA_LABEL);
				String fourthname = buildNameFromStruct(Hemisphere.BOTH, FourthVentricle.TUBE_NUM, ElasticTube.AREA_LABEL);
				String sasname = buildNameFromStruct(Hemisphere.BOTH, SAS.TUBE_NUM, ElasticTube.AREA_LABEL);
				if(v.getName().startsWith(sinousname) || v.getName().startsWith(thirdname) || v.getName().startsWith(fourthname)
						|| v.getName().startsWith(sasname)){
					// derive selon area de tube localises dans un hemi : 0.5*T1_l0  ;
					return 0.5f * v.getSourceObj().getLength().getValue();
				}else{
					return 0.0f;
				}
			}
		}
	}

	private String getSymbolicTotalVolumeDerivative(SimpleVariable v, ArrayList<SimpleVariable> variables) throws Exception{
		// equ(63) et equ(67)
		if(v.getName().equals(getAreaFluid().getName())){
			// derive selon area brain : T0_lbrain ;
			return ""+getLength().getName();
		}else{
			String arteryname = buildNameFromStruct(getHemi(), Artery.TUBE_NUM, ElasticTube.AREA_LABEL);
			String arteriolname = buildNameFromStruct(getHemi(), Arteriole.TUBE_NUM, ElasticTube.AREA_LABEL);
			String capname = buildNameFromStruct(getHemi(), Capillary.TUBE_NUM, ElasticTube.AREA_LABEL);
			String vlname = buildNameFromStruct(getHemi(), Veinule.TUBE_NUM, ElasticTube.AREA_LABEL);
			String vname = buildNameFromStruct(getHemi(), Vein.TUBE_NUM, ElasticTube.AREA_LABEL);
			String ventname = buildNameFromStruct(getHemi(), Ventricle.TUBE_NUM, ElasticTube.AREA_LABEL);
			if(v.getName().startsWith(arteryname) || v.getName().startsWith(arteriolname) || v.getName().startsWith(capname)
					|| v.getName().startsWith(vlname) || v.getName().startsWith(vname) || v.getName().startsWith(ventname)){
				// derive selon area de tube localises dans un hemi : T1_l0  ;
				return ""+v.getSourceObj().getLength().getName();
			}else{
				String sinousname = buildNameFromStruct(Hemisphere.BOTH, VenousSinus.TUBE_NUM, ElasticTube.AREA_LABEL);
				String thirdname = buildNameFromStruct(Hemisphere.BOTH, ThirdVentricle.TUBE_NUM, ElasticTube.AREA_LABEL);
				String fourthname = buildNameFromStruct(Hemisphere.BOTH, FourthVentricle.TUBE_NUM, ElasticTube.AREA_LABEL);
				String sasname = buildNameFromStruct(Hemisphere.BOTH, SAS.TUBE_NUM, ElasticTube.AREA_LABEL);
				if(v.getName().startsWith(sinousname) || v.getName().startsWith(thirdname) || v.getName().startsWith(fourthname)
						|| v.getName().startsWith(sasname)){
					// derive selon area de tube localises dans un hemi : 0.5*T1_l0  ;
					return ""+0.5f+" * "+v.getSourceObj().getLength().getName();
				}else{
					return ""+0.0f;
				}
			}
		}
	}
	
	
	// ====================== init ============================
	//====== Additional equation fout2 ====
	private float getInitialAdditionalFout2Equation(SimpleVariable fout2){
		// equ(68) et equ(70)
		return fout2.getValue() - 0.0f;
	}

	private String getSymbolicInitialAdditionalFout2Equation(SimpleVariable fout2){
		// equ(68) et equ(70)
		return ""+fout2.getName()+" - "+0.0f;
	}

	private float getInitialAdditionalFout2Derivative(SimpleVariable v, ArrayList<SimpleVariable> variables) throws Exception{
		// equ(68) et equ(70)
		if(v.getName().equals(getFlowout2().getName())){
			// derive selon flow out1 : 1;
			return 1.0f;
		}else{
			return 0.0f;
		}
	}

	private String getSymbolicInitialAdditionalFout2Derivative(SimpleVariable v, ArrayList<SimpleVariable> variables) throws Exception{
		// equ(68) et equ(70)
		if(v.getName().equals(getFlowout2().getName())){
			// derive selon flow out1 : 1;
			return ""+1.0f;
		}else{
			return ""+0.0f;
		}
	}

	//====== Additional equation fin1 ====
	private float getInitialAdditionalFin2Equation(SimpleVariable fin1){
		// equ(69) et equ(70)
		return fin1.getValue() - 0.0f;
	}

	private String getSymbolicInitialAdditionalFin2Equation(SimpleVariable fin1){
		// equ(69) et equ(70)
		return ""+fin1.getName()+" - "+0.0f;
	}

	private float getInitialAdditionalFin2Derivative(SimpleVariable v, ArrayList<SimpleVariable> variables) throws Exception{
		// equ(69) et equ(70)
		if(v.getName().equals(getFlowin1().getName())){
			// derive selon flow in1 : 1;
			return 1.0f;
		}else{
			return 0.0f;
		}
	}

	private String getSymbolicInitialAdditionalFin2Derivative(SimpleVariable v, ArrayList<SimpleVariable> variables) throws Exception{
		// equ(69) et equ(70)
		if(v.getName().equals(getFlowin1().getName())){
			// derive selon flow in1 : 1;
			return ""+1.0f;
		}else{
			return ""+0.0f;
		}
	}

	//====== Momentum ventricle ====
	private float getInitialMomentumVentricleEquation(SimpleVariable ventriclePr, SimpleVariable pr, SimpleVariable alfa2, SimpleVariable fout1){
		// equ(62) et equ(66)
		return ventriclePr.getValue() - pr.getValue() + alfa2.getValue() * fout1.getValue();
	}

	private String getSymbolicInitialMomentumVentricleEquation(SimpleVariable ventriclePr, SimpleVariable pr, SimpleVariable alfa2, SimpleVariable fout1){
		// equ(62) et equ(66)
		return ""+ventriclePr.getName()+" - "+pr.getName()+" + "+alfa2.getName()+" * "+fout1.getName();
	}

	private float getInitialMomentumVentricleDerivative(SimpleVariable v, ArrayList<SimpleVariable> variables) throws Exception{
		// equ(62) et equ(66)
		if(v.getName().equals(getPressure().getName())){
			// derive selon pression : -1;
			return -1.0f;
		}else{
			if(v.getName().equals(getFlowout1().getName())){
				// derive selon flow out 1 : T_alpha2_brain ;
				return getAlpha2().getValue();
			}else{
				ArrayList<SimpleVariable> pven = findVariableWithStruct(getHemi(), Ventricle.TUBE_NUM, ElasticTube.PRESSURE_LABEL, variables);
				if(v.getName().equals(pven.get(0).getName())){
					// derive selon ventricule pression : 1;
					return 1.0f;
				}
				return 0.0f;
			}
		}
	}

	private String getSymbolicInitialMomentumVentricleDerivative(SimpleVariable v, ArrayList<SimpleVariable> variables) throws Exception{
		// equ(62) et equ(66)
		if(v.getName().equals(getPressure().getName())){
			// derive selon pression : -1;
			return "-"+1.0f;
		}else{
			if(v.getName().equals(getFlowout1().getName())){
				// derive selon flow out 1 : T_alpha2_brain ;
				return ""+getAlpha2().getName();
			}else{
				ArrayList<SimpleVariable> pven = findVariableWithStruct(getHemi(), Ventricle.TUBE_NUM, ElasticTube.PRESSURE_LABEL, variables);
				if(v.getName().equals(pven.get(0).getName())){
					// derive selon ventricule pression : 1;
					return ""+1.0f;
				}
				return ""+0.0f;
			}
		}
	}

	//====== Momentum cappilaries ====
	private float getInitialMomentumCappilaryEquation(SimpleVariable pr, SimpleVariable cappilaryPr, SimpleVariable alfa1, SimpleVariable fin1){
		// equ(61) et equ(65)
		return pr.getValue() - cappilaryPr.getValue() + getAlpha1().getValue() * fin1.getValue();
	}

	private String getSymbolicInitialMomentumCappilaryEquation(SimpleVariable pr, SimpleVariable cappilaryPr, SimpleVariable alfa1, SimpleVariable fin1){
		// equ(61) et equ(65)
		return ""+pr.getName()+" - "+cappilaryPr.getName()+" + "+getAlpha1().getName()+" * "+fin1.getName();
	}

	private float getInitialMomentumCappilaryDerivative(SimpleVariable v, ArrayList<SimpleVariable> variables) throws Exception{
		// equ(61) et equ(65)
		if(v.getName().equals(getPressure().getName())){
			// derive selon pression : 1;
			return 1.0f;
		}else{
			if(v.getName().equals(getFlowin1().getName())){
				// derive selon flow out 1 : T_alpha2_brain ;
				return getAlpha1().getValue();
			}else{
				String namecappi = buildNameFromStruct(getHemi(), Capillary.TUBE_NUM, ElasticTube.PRESSURE_LABEL);
				if(v.getName().startsWith(namecappi)){
					// derive selon cappilary pression : -1;
					return -1.0f;
				}
				return 0.0f;
			}
		}
	}

	private String getSymbolicInitialMomentumCappilaryDerivative(SimpleVariable v, ArrayList<SimpleVariable> variables) throws Exception{
		// equ(61) et equ(65)
		if(v.getName().equals(getPressure().getName())){
			// derive selon pression : 1;
			return ""+1.0f;
		}else{
			if(v.getName().equals(getFlowin1().getName())){
				// derive selon flow out 1 : T_alpha2_brain ;
				return ""+getAlpha1().getName();
			}else{
				String namecappi = buildNameFromStruct(getHemi(), Capillary.TUBE_NUM, ElasticTube.PRESSURE_LABEL);
				if(v.getName().startsWith(namecappi)){
					// derive selon cappilary pression : -1;
					return "-"+1.0f;
				}
				return ""+0.0f;
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
	private float getInitialConnectivityEquation(SimpleVariable fo1, SimpleVariable fo2, SimpleVariable fi1, SimpleVariable fi2){
		// equ(60) et equ(64)
		return fo1.getValue()+fo2.getValue()-fi1.getValue()-fi2.getValue();
	}

	private String getSymbolicInitialConnectivityEquation(SimpleVariable fo1, SimpleVariable fo2, SimpleVariable fi1, SimpleVariable fi2){
		// equ(60) et equ(64)
		return ""+fo1.getName()+"+"+fo2.getName()+"-"+fi1.getName()+"-"+fi2.getName();
	}

	private float getInitialConnectivityDerivative(SimpleVariable v, ArrayList<SimpleVariable> variables) throws Exception{
		// equ(60) et equ(64)
		if(v.getName().equals(getFlowout1().getName()) || v.getName().equals(getFlowout2().getName())){
			// derive selon flowout1 et 2 : 1;
			return 1.0f;
		}else{
			if(v.getName().equals(getFlowout1().getName()) || v.getName().equals(getFlowout2().getName())){
				// derive selon flowin1 et 2 : -1;
				return -1.0f;
			}
			return 0.0f;
		}
	}

	private String getSymbolicInitialConnectivityDerivative(SimpleVariable v, ArrayList<SimpleVariable> variables) throws Exception{
		// equ(60) et equ(64)
		if(v.getName().equals(getFlowout1().getName()) || v.getName().equals(getFlowout2().getName())){
			// derive selon flowout1 et 2 : 1;
			return ""+1.0f;
		}else{
			if(v.getName().equals(getFlowout1().getName()) || v.getName().equals(getFlowout2().getName())){
				// derive selon flowin1 et 2 : -1;
				return "-"+1.0f;
			}
			return ""+0.0f;
		}
	}

	//====== Total volume ====
	private float getInitialTotalVolumeEquation(ArrayList<SimpleVariable> arteryArea, ArrayList<SimpleVariable> arteriolArea, ArrayList<SimpleVariable> cappilaryArea, ArrayList<SimpleVariable> veinuleArea,
			ArrayList<SimpleVariable> veinArea,SimpleVariable vsinousArea, SimpleVariable ventricleArea,  SimpleVariable thirdvArea, SimpleVariable fourthvArea,
			SimpleVariable sasArea, SimpleVariable brainFluidArea){
		// equ(63) et equ(67)
		float res1 = 0;
		float res2 = 0;
		for(SimpleVariable arterya: arteryArea){
			res1 += (arterya.getValue() * ((Artery)arterya.getSourceObj()).getLength().getValue());
			res2 += (((Artery)arterya.getSourceObj()).getInitialArea().getValue() * ((Artery)arterya.getSourceObj()).getLength().getValue());  
		}
		for(SimpleVariable arteriola: arteriolArea){
			res1 += (arteriola.getValue() * ((Arteriole)arteriola.getSourceObj()).getLength().getValue());  
			res2 += (((Arteriole)arteriola.getSourceObj()).getInitialArea().getValue() * ((Arteriole)arteriola.getSourceObj()).getLength().getValue());  
		}
		for(SimpleVariable capa: cappilaryArea){
			res1 += (capa.getValue() * ((Capillary)capa.getSourceObj()).getLength().getValue());
			res2 += (((Capillary)capa.getSourceObj()).getInitialArea().getValue() * ((Capillary)capa.getSourceObj()).getLength().getValue());  
		}
		for(SimpleVariable vla: veinuleArea){
			res1 += (vla.getValue() * ((Veinule)vla.getSourceObj()).getLength().getValue());  
			res2 += (((Veinule)vla.getSourceObj()).getInitialArea().getValue() * ((Veinule)vla.getSourceObj()).getLength().getValue());  
		}
		for(SimpleVariable va: veinArea){
			res1 += (va.getValue() * ((Vein)va.getSourceObj()).getLength().getValue());
			res2 += (((Vein)va.getSourceObj()).getInitialArea().getValue() * ((Vein)va.getSourceObj()).getLength().getValue());  
		}
		res1 += (vsinousArea.getValue() * ((VenousSinus)vsinousArea.getSourceObj()).getLength().getValue());  
		res2 += (((VenousSinus)vsinousArea.getSourceObj()).getInitialArea().getValue() * ((VenousSinus)vsinousArea.getSourceObj()).getLength().getValue());  
		res1 += (ventricleArea.getValue() * ((Ventricle)ventricleArea.getSourceObj()).getLength().getValue()); 
		res2 += (((Ventricle)ventricleArea.getSourceObj()).getInitialArea().getValue() * ((Ventricle)ventricleArea.getSourceObj()).getLength().getValue());  
		res1 += (0.5f * (thirdvArea.getValue() * ((ThirdVentricle)thirdvArea.getSourceObj()).getLength().getValue())); 
		res2 += (0.5f * ((ThirdVentricle)thirdvArea.getSourceObj()).getInitialArea().getValue() * ((ThirdVentricle)thirdvArea.getSourceObj()).getLength().getValue());  
		res1 += (0.5f * (fourthvArea.getValue() * ((FourthVentricle)fourthvArea.getSourceObj()).getLength().getValue())); 
		res2 += (0.5f * ((FourthVentricle)fourthvArea.getSourceObj()).getInitialArea().getValue() * ((FourthVentricle)fourthvArea.getSourceObj()).getLength().getValue());  
		res1 += (0.5f * (sasArea.getValue() * ((SAS)sasArea.getSourceObj()).getLength().getValue())); 
		res2 += (0.5f * ((SAS)sasArea.getSourceObj()).getInitialArea().getValue() * ((SAS)sasArea.getSourceObj()).getLength().getValue());  
		res1 += ((brainFluidArea.getValue() + getInitialAreaSolid().getValue())* getLength().getValue());
		res2 += ((getInitialAreaFluid().getValue() + getInitialAreaSolid().getValue())* getLength().getValue());

		return res1 - res2;
	}

	private String getSymbolicInitialTotalVolumeEquation(ArrayList<SimpleVariable> arteryArea, ArrayList<SimpleVariable> arteriolArea, ArrayList<SimpleVariable> cappilaryArea, ArrayList<SimpleVariable> veinuleArea,
			ArrayList<SimpleVariable> veinArea,SimpleVariable vsinousArea, SimpleVariable ventricleArea,  SimpleVariable thirdvArea, SimpleVariable fourthvArea,
			SimpleVariable sasArea, SimpleVariable brainFluidArea){
		// equ(63) et equ(67)
		String res1 = "(";
		String res2 = "(";
		for(SimpleVariable arterya: arteryArea){
			res1 += "("+arterya.getName()+" * "+((Artery)arterya.getSourceObj()).getLength().getName()+") + ";
			res2 += "("+((Artery)arterya.getSourceObj()).getInitialArea().getName()+" * "+((Artery)arterya.getSourceObj()).getLength().getName()+") + ";  
		}
		for(SimpleVariable arteriola: arteriolArea){
			res1 += "("+arteriola.getName() +" * "+ ((Arteriole)arteriola.getSourceObj()).getLength().getName()+") + ";  
			res2 += "("+((Arteriole)arteriola.getSourceObj()).getInitialArea().getName() +" * "+ ((Arteriole)arteriola.getSourceObj()).getLength().getName()+") + ";  
		}
		for(SimpleVariable capa: cappilaryArea){
			res1 += "("+capa.getName() +" * "+ ((Capillary)capa.getSourceObj()).getLength().getName()+") + ";
			res2 += "("+((Capillary)capa.getSourceObj()).getInitialArea().getName() +" * "+ ((Capillary)capa.getSourceObj()).getLength().getName()+") + ";  
		}
		for(SimpleVariable vla: veinuleArea){
			res1 += "("+vla.getName() +" * "+ ((Veinule)vla.getSourceObj()).getLength().getName()+") + ";  
			res2 += "("+((Veinule)vla.getSourceObj()).getInitialArea().getName() +" * "+ ((Veinule)vla.getSourceObj()).getLength().getName()+") + ";  
		}
		for(SimpleVariable va: veinArea){
			res1 += "("+va.getName() +" * "+ ((Vein)va.getSourceObj()).getLength().getName()+") + ";
			res2 += "("+((Vein)va.getSourceObj()).getInitialArea().getName() +" * "+ ((Vein)va.getSourceObj()).getLength().getName()+") + ";  
		}
		res1 += "("+vsinousArea.getName() +" * "+ ((VenousSinus)vsinousArea.getSourceObj()).getLength().getName()+") + ";  
		res2 += "("+((VenousSinus)vsinousArea.getSourceObj()).getInitialArea().getName() +" * "+ ((VenousSinus)vsinousArea.getSourceObj()).getLength().getName()+") + ";  
		res1 += "("+ventricleArea.getName() +" * "+ ((Ventricle)ventricleArea.getSourceObj()).getLength().getName()+") + "; 
		res2 += "("+((Ventricle)ventricleArea.getSourceObj()).getInitialArea().getName() +" * "+ ((Ventricle)ventricleArea.getSourceObj()).getLength().getName()+") + ";  
		res1 += "("+0.5f +" * "+ (thirdvArea.getName() +" * "+ ((ThirdVentricle)thirdvArea.getSourceObj()).getLength().getName())+") + "; 
		res2 += "("+0.5f +" * "+((ThirdVentricle)thirdvArea.getSourceObj()).getInitialArea().getName() +" * "+ ((ThirdVentricle)thirdvArea.getSourceObj()).getLength().getName()+") + ";  
		res1 += "("+0.5f +" * "+ (fourthvArea.getName()+" * "+ ((FourthVentricle)fourthvArea.getSourceObj()).getLength().getName())+") + "; 
		res2 += "("+0.5f +" * "+((FourthVentricle)fourthvArea.getSourceObj()).getInitialArea().getName() +" * "+ ((FourthVentricle)fourthvArea.getSourceObj()).getLength().getName()+") + ";  
		res1 += "("+0.5f +" * "+ (sasArea.getName() +" * "+ ((SAS)sasArea.getSourceObj()).getLength().getName())+") + "; 
		res2 += "("+0.5f +" * "+((SAS)sasArea.getSourceObj()).getInitialArea().getName() +" * "+ ((SAS)sasArea.getSourceObj()).getLength().getName()+") + ";  
		res1 += "(("+brainFluidArea.getName()+" + "+getInitialAreaSolid().getName() +")  * "+ getLength().getName()+")";
		res2 += "(("+getInitialAreaFluid().getName()+" + "+getInitialAreaSolid().getName() +")  * "+ getLength().getName()+")";

		return res1+")"+" - "+res2+")";
	}

	private float getInitialTotalVolumeDerivative(SimpleVariable v, ArrayList<SimpleVariable> variables) throws Exception{
		// equ(63) et equ(67)
		if(v.getName().equals(getAreaFluid().getName())){
			// derive selon area brain : T0_lbrain ;
			return getLength().getValue();
		}else{
			String arteryname = buildNameFromStruct(getHemi(), Artery.TUBE_NUM, ElasticTube.AREA_LABEL);
			String arteriolname = buildNameFromStruct(getHemi(), Arteriole.TUBE_NUM, ElasticTube.AREA_LABEL);
			String capname = buildNameFromStruct(getHemi(), Capillary.TUBE_NUM, ElasticTube.AREA_LABEL);
			String vlname = buildNameFromStruct(getHemi(), Veinule.TUBE_NUM, ElasticTube.AREA_LABEL);
			String vname = buildNameFromStruct(getHemi(), Vein.TUBE_NUM, ElasticTube.AREA_LABEL);
			String ventname = buildNameFromStruct(getHemi(), Ventricle.TUBE_NUM, ElasticTube.AREA_LABEL);
			if(v.getName().startsWith(arteryname) || v.getName().startsWith(arteriolname) || v.getName().startsWith(capname)
					|| v.getName().startsWith(vlname) || v.getName().startsWith(vname) || v.getName().startsWith(ventname)){
				// derive selon area de tube localises dans un hemi : T1_l0  ;
				return v.getSourceObj().getLength().getValue();
			}else{
				String sinousname = buildNameFromStruct(Hemisphere.BOTH, VenousSinus.TUBE_NUM, ElasticTube.AREA_LABEL);
				String thirdname = buildNameFromStruct(Hemisphere.BOTH, ThirdVentricle.TUBE_NUM, ElasticTube.AREA_LABEL);
				String fourthname = buildNameFromStruct(Hemisphere.BOTH, FourthVentricle.TUBE_NUM, ElasticTube.AREA_LABEL);
				String sasname = buildNameFromStruct(Hemisphere.BOTH, SAS.TUBE_NUM, ElasticTube.AREA_LABEL);
				if(v.getName().startsWith(sinousname) || v.getName().startsWith(thirdname) || v.getName().startsWith(fourthname)
						|| v.getName().startsWith(sasname)){
					// derive selon area de tube localises dans un hemi : 0.5*T1_l0  ;
					return 0.5f * v.getSourceObj().getLength().getValue();
				}else{
					return 0.0f;
				}
			}
		}
	}

	private String getSymbolicInitialTotalVolumeDerivative(SimpleVariable v, ArrayList<SimpleVariable> variables) throws Exception{
		// equ(63) et equ(67)
		if(v.getName().equals(getAreaFluid().getName())){
			// derive selon area brain : T0_lbrain ;
			return ""+getLength().getName();
		}else{
			String arteryname = buildNameFromStruct(getHemi(), Artery.TUBE_NUM, ElasticTube.AREA_LABEL);
			String arteriolname = buildNameFromStruct(getHemi(), Arteriole.TUBE_NUM, ElasticTube.AREA_LABEL);
			String capname = buildNameFromStruct(getHemi(), Capillary.TUBE_NUM, ElasticTube.AREA_LABEL);
			String vlname = buildNameFromStruct(getHemi(), Veinule.TUBE_NUM, ElasticTube.AREA_LABEL);
			String vname = buildNameFromStruct(getHemi(), Vein.TUBE_NUM, ElasticTube.AREA_LABEL);
			String ventname = buildNameFromStruct(getHemi(), Ventricle.TUBE_NUM, ElasticTube.AREA_LABEL);
			if(v.getName().startsWith(arteryname) || v.getName().startsWith(arteriolname) || v.getName().startsWith(capname)
					|| v.getName().startsWith(vlname) || v.getName().startsWith(vname) || v.getName().startsWith(ventname)){
				// derive selon area de tube localises dans un hemi : T1_l0  ;
				return ""+v.getSourceObj().getLength().getName();
			}else{
				String sinousname = buildNameFromStruct(Hemisphere.BOTH, VenousSinus.TUBE_NUM, ElasticTube.AREA_LABEL);
				String thirdname = buildNameFromStruct(Hemisphere.BOTH, ThirdVentricle.TUBE_NUM, ElasticTube.AREA_LABEL);
				String fourthname = buildNameFromStruct(Hemisphere.BOTH, FourthVentricle.TUBE_NUM, ElasticTube.AREA_LABEL);
				String sasname = buildNameFromStruct(Hemisphere.BOTH, SAS.TUBE_NUM, ElasticTube.AREA_LABEL);
				if(v.getName().startsWith(sinousname) || v.getName().startsWith(thirdname) || v.getName().startsWith(fourthname)
						|| v.getName().startsWith(sasname)){
					// derive selon area de tube localises dans un hemi : 0.5*T1_l0  ;
					return ""+0.5f+" * "+v.getSourceObj().getLength().getName();
				}else{
					return ""+0.0f;
				}
			}
		}
	}
}
