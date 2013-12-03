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
	private Variable areaFluid;
	private Variable areaSolid;
	private Variable initialAreaFluid;
	private Variable initialAreaSolid;
	private Variable alpha1; // connection to capillaries
	private Variable alpha2; // connection to ventricles
	private Variable flowin1;
	private Variable flowin2;
	private Variable flowout1;
	private Variable flowout2;
	private Variable pressure;


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

	protected Variable getLength() {
		return length;
	}

	protected void setLength(float length) {
		this.length = new Variable(TUBE_LABEL+getTubeNum()+"_"+LENGTH_LABEL+BRAIN_LABEL,length, (Tube)this);
	}

	public Variable getAreaFluid() {
		return areaFluid;
	}

	public void setAreaFluid(float areaFluid) {
		String prefix = "";
		if(getHemi() == Hemisphere.LEFT)
			prefix = "L_";
		else
			if(getHemi() == Hemisphere.RIGHT)
				prefix = "R_";
		Variable v = new Variable(prefix+"_"+TUBE_LABEL+getTubeNum()+"_"+AREA_FLUID_LABEL+"_"+BRAIN_LABEL,areaFluid, (Tube)this);
		this.areaFluid = v;
	}

	public Variable getAreaSolid() {
		return areaSolid;
	}

	public void setAreaSolid(float areaSolid) {
		String prefix = "";
		if(getHemi() == Hemisphere.LEFT)
			prefix = "L_";
		else
			if(getHemi() == Hemisphere.RIGHT)
				prefix = "R_";
		Variable v = new Variable(prefix+"_"+TUBE_LABEL+getTubeNum()+"_"+AREA_SOLID_LABEL+"_"+BRAIN_LABEL,areaSolid, (Tube)this);
		this.areaSolid = v;
	}

	public Variable getAlpha1() {
		return alpha1;
	}

	public void setAlpha1(float alpha1) {
		Variable v = new Variable(TUBE_LABEL+getTubeNum()+"_"+ALPHA1_LABEL+"_"+BRAIN_LABEL,alpha1, (Tube)this);
		this.alpha1 = v;
	}

	public Variable getAlpha2() {
		return alpha2;
	}

	public void setAlpha2(float alpha2) {
		Variable v = new Variable(TUBE_LABEL+getTubeNum()+"_"+ALPHA2_LABEL+"_"+BRAIN_LABEL,alpha2, (Tube)this);
		this.alpha2 = v;
	}

	public Variable getFlowin1() {
		return flowin1;
	}

	public void setFlowin1(float flowin1) {
		String prefix = "";
		if(getHemi() == Hemisphere.LEFT)
			prefix = "L_";
		else
			if(getHemi() == Hemisphere.RIGHT)
				prefix = "R_";
		Variable v = new Variable(prefix+"_"+TUBE_LABEL+getTubeNum()+"_"+FLOWIN1_LABEL+"_"+BRAIN_LABEL,flowin1, (Tube)this);
		this.flowin1 = v;
	}

	public Variable getFlowin2() {
		return flowin2;
	}

	public void setFlowin2(float flowin2) {
		String prefix = "";
		if(getHemi() == Hemisphere.LEFT)
			prefix = "L_";
		else
			if(getHemi() == Hemisphere.RIGHT)
				prefix = "R_";
		Variable v = new Variable(prefix+"_"+TUBE_LABEL+getTubeNum()+"_"+FLOWIN2_LABEL+"_"+BRAIN_LABEL,flowin2, (Tube)this);
		this.flowin2 = v;
	}

	public Variable getFlowout1() {
		return flowout1;
	}

	public void setFlowout1(float flowout1) {
		String prefix = "";
		if(getHemi() == Hemisphere.LEFT)
			prefix = "L_";
		else
			if(getHemi() == Hemisphere.RIGHT)
				prefix = "R_";
		Variable v = new Variable(prefix+"_"+TUBE_LABEL+getTubeNum()+"_"+FLOWOUT1_LABEL+"_"+BRAIN_LABEL,flowout1, (Tube)this);
		this.flowout1 = v;
	}

	public Variable getFlowout2() {
		return flowout2;
	}

	public void setFlowout2(float flowout2) {
		String prefix = "";
		if(getHemi() == Hemisphere.LEFT)
			prefix = "L_";
		else
			if(getHemi() == Hemisphere.RIGHT)
				prefix = "R_";
		Variable v = new Variable(prefix+"_"+TUBE_LABEL+getTubeNum()+"_"+FLOWOUT2_LABEL+"_"+BRAIN_LABEL,flowout2, (Tube)this);
		this.flowout2 = v;
	}

	public Variable getPressure() {
		return pressure;
	}

	public void setPressure(float pressure) {
		String prefix = "";
		if(getHemi() == Hemisphere.LEFT)
			prefix = "L_";
		else
			if(getHemi() == Hemisphere.RIGHT)
				prefix = "R_";
		Variable v = new Variable(prefix+"_"+TUBE_LABEL+getTubeNum()+"_"+PRESSURE_LABEL+"_"+BRAIN_LABEL,pressure, (Tube)this);
		this.pressure = v;
	}

	/**
	 * @return the initialAreaFluid
	 */
	public Variable getInitialAreaFluid() {
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
		Variable v = new Variable(prefix+"_"+TUBE_LABEL+getTubeNum()+"_"+INITIAL_AREAFLUID_LABEL+"_"+BRAIN_LABEL,initialAreaFluid, (Tube)this);
		this.initialAreaFluid = v;
	}

	/**
	 * @return the initialAreaSolid
	 */
	public Variable getInitialAreaSolid() {
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
		Variable v = new Variable(prefix+"_"+TUBE_LABEL+getTubeNum()+"_"+INITIAL_AREASOLID_LABEL+"_"+BRAIN_LABEL,initialAreaSolid, (Tube)this);
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

	public ArrayList<Variable> getVariables(){
		ArrayList<Variable> variables = new ArrayList<Variable>();
		variables.add(getFlowin1());
		variables.add(getFlowin2());
		variables.add(getFlowout1());
		variables.add(getFlowout2());
		variables.add(getPressure());
		variables.add(getAreaFluid());
		variables.add(getAreaSolid());
		return variables;
	}
	
	// ------------------- EQUATIONS -------------
	@Override
	public ArrayList<float[]> getEquations(ArrayList<Variable> variables) throws Exception {
		ArrayList<float[]> res = new ArrayList<float[]>();

		// Connectivity
		float[] connectivity = new float[variables.size()+1];
		Variable fi1 = findVariableWithName(getFlowin1().getName(),variables);
		Variable fi2 = findVariableWithName(getFlowin2().getName(),variables);
		Variable fo1 = findVariableWithName(getFlowout1().getName(),variables);
		Variable fo2 = findVariableWithName(getFlowout2().getName(),variables);
		connectivity[0] = getConnectivityEquation(fo1, fo2, fi1, fi2);
		for(int i = 0; i<variables.size();i++){
			connectivity[i+1] = getConnectivityDerivative(variables.get(i), variables);
		}
		res.add(connectivity);

		// Momentum ventricle
		Variable pr = findVariableWithName(getPressure().getName(),variables);
		Variable alf1 = findVariableWithName(getAlpha1().getName(),variables);
		Variable alf2 = findVariableWithName(getAlpha2().getName(),variables);
		ArrayList<Variable> pven = findVariableWithStruct(getHemi(), Ventricle.TUBE_NUM, ElasticTube.PRESSURE_LABEL, variables);
		float[] momentumvent = new float[variables.size()+1];
		momentumvent[0] = getMomentumVentricleEquation(pven.get(0), getPressure(), alf2, fo2);
		for(int i = 0; i<variables.size();i++){
			momentumvent[i+1] = getMomentumVentricleDerivative(variables.get(i), variables);
		}
		res.add(momentumvent);

		// Momentum cappillary

		ArrayList<Variable> pcap = findVariableWithStruct(getHemi(), Ventricle.TUBE_NUM, ElasticTube.PRESSURE_LABEL, variables);
		for(Variable pc : pcap){
			float[] momentumcap = new float[variables.size()+1];
			momentumcap[0] = getMomentumCappilaryEquation(pr, pc, alf1, fi1);
			for(int i = 0; i<variables.size();i++){
				momentumcap[i+1] = getMomentumCappilaryDerivative(variables.get(i), variables);
			}
			res.add(momentumcap);
		}

		// Total volume
		ArrayList<Variable> arteryArea = findVariableWithStruct(getHemi(), Artery.TUBE_NUM, ElasticTube.AREA_LABEL, variables);
		ArrayList<Variable> arteriolArea = findVariableWithStruct(getHemi(), Arteriole.TUBE_NUM, ElasticTube.AREA_LABEL, variables);
		ArrayList<Variable> cappilaryArea = findVariableWithStruct(getHemi(), Capillary.TUBE_NUM, ElasticTube.AREA_LABEL, variables);
		ArrayList<Variable> veinuleArea = findVariableWithStruct(getHemi(), Veinule.TUBE_NUM, ElasticTube.AREA_LABEL, variables);
		ArrayList<Variable> veinArea = findVariableWithStruct(getHemi(), Vein.TUBE_NUM, ElasticTube.AREA_LABEL, variables);
		ArrayList<Variable> ventricleArea = findVariableWithStruct(getHemi(), Ventricle.TUBE_NUM, ElasticTube.AREA_LABEL, variables);
		ArrayList<Variable> vsinousArea = findVariableWithStruct(Hemisphere.BOTH, VenousSinus.TUBE_NUM, ElasticTube.AREA_LABEL, variables);
		ArrayList<Variable> thirdvArea = findVariableWithStruct(Hemisphere.BOTH, ThirdVentricle.TUBE_NUM, ElasticTube.AREA_LABEL, variables);
		ArrayList<Variable> fourthvArea = findVariableWithStruct(Hemisphere.BOTH, FourthVentricle.TUBE_NUM, ElasticTube.AREA_LABEL, variables);
		ArrayList<Variable> sasArea = findVariableWithStruct(Hemisphere.BOTH, SAS.TUBE_NUM, ElasticTube.AREA_LABEL, variables);
		Variable brainFluidArea = findVariableWithName(getAreaFluid().getName(), variables);
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
		addfin[0] =getAdditionalFin1Equation(fi1);
		for(int i = 0; i<variables.size();i++){
			addfin[i+1] = getAdditionalFin1Derivative(variables.get(i), variables);
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
	public ArrayList<String[]> getSymbolicEquations(ArrayList<Variable> variables) throws Exception {
		ArrayList<String[]> res = new ArrayList<String[]>();

		// Connectivity
		String[] connectivity = new String[variables.size()+1];
		Variable fi1 = findVariableWithName(getFlowin1().getName(),variables);
		Variable fi2 = findVariableWithName(getFlowin2().getName(),variables);
		Variable fo1 = findVariableWithName(getFlowout1().getName(),variables);
		Variable fo2 = findVariableWithName(getFlowout2().getName(),variables);
		connectivity[0] = getSymbolicConnectivityEquation(fo1, fo2, fi1, fi2);
		for(int i = 0; i<variables.size();i++){
			connectivity[i+1] = getSymbolicConnectivityDerivative(variables.get(i), variables);
		}
		res.add(connectivity);

		// Momentum ventricle
		ArrayList<Variable> pven = findVariableWithStruct(getHemi(), Ventricle.TUBE_NUM, ElasticTube.PRESSURE_LABEL, variables);
		String[] momentumvent = new String[variables.size()+1];
		momentumvent[0] = getSymbolicMomentumVentricleEquation(pven.get(0), getPressure(), getAlpha2(), getFlowout2());
		for(int i = 0; i<variables.size();i++){
			momentumvent[i+1] = getSymbolicMomentumVentricleDerivative(variables.get(i), variables);
		}
		res.add(momentumvent);

		// Momentum cappillary
		ArrayList<Variable> pcap = findVariableWithStruct(getHemi(), Ventricle.TUBE_NUM, ElasticTube.PRESSURE_LABEL, variables);
		for(Variable pc : pcap){
			String[] momentumcap = new String[variables.size()+1];
			momentumcap[0] = getSymbolicMomentumCappilaryEquation(getPressure(), pc, getAlpha1(), getFlowin1());
			for(int i = 0; i<variables.size();i++){
				momentumcap[i+1] = getSymbolicMomentumCappilaryDerivative(variables.get(i), variables);
			}
			res.add(momentumcap);
		}

		// Total volume
		ArrayList<Variable> arteryArea = findVariableWithStruct(getHemi(), Artery.TUBE_NUM, ElasticTube.AREA_LABEL, variables);
		ArrayList<Variable> arteriolArea = findVariableWithStruct(getHemi(), Arteriole.TUBE_NUM, ElasticTube.AREA_LABEL, variables);
		ArrayList<Variable> cappilaryArea = findVariableWithStruct(getHemi(), Capillary.TUBE_NUM, ElasticTube.AREA_LABEL, variables);
		ArrayList<Variable> veinuleArea = findVariableWithStruct(getHemi(), Veinule.TUBE_NUM, ElasticTube.AREA_LABEL, variables);
		ArrayList<Variable> veinArea = findVariableWithStruct(getHemi(), Vein.TUBE_NUM, ElasticTube.AREA_LABEL, variables);
		ArrayList<Variable> ventricleArea = findVariableWithStruct(getHemi(), Ventricle.TUBE_NUM, ElasticTube.AREA_LABEL, variables);
		ArrayList<Variable> vsinousArea = findVariableWithStruct(Hemisphere.BOTH, VenousSinus.TUBE_NUM, ElasticTube.AREA_LABEL, variables);
		ArrayList<Variable> thirdvArea = findVariableWithStruct(Hemisphere.BOTH, ThirdVentricle.TUBE_NUM, ElasticTube.AREA_LABEL, variables);
		ArrayList<Variable> fourthvArea = findVariableWithStruct(Hemisphere.BOTH, FourthVentricle.TUBE_NUM, ElasticTube.AREA_LABEL, variables);
		ArrayList<Variable> sasArea = findVariableWithStruct(Hemisphere.BOTH, SAS.TUBE_NUM, ElasticTube.AREA_LABEL, variables);
		Variable brainFluidArea = findVariableWithName(getAreaFluid().getName(), variables);
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
		addfin[0] =getSymbolicAdditionalFin1Equation(fi1);
		for(int i = 0; i<variables.size();i++){
			addfin[i+1] = getSymbolicAdditionalFin1Derivative(variables.get(i), variables);
		}
		res.add(addfin);

		return res;
	}

	//====== Additional equation fout2 ====
	private float getAdditionalFout2Equation(Variable fout2){
		// equ(68) et equ(70)
		return fout2.getValue() - 0.0f;
	}

	private String getSymbolicAdditionalFout2Equation(Variable fout2){
		// equ(68) et equ(70)
		return ""+fout2.getName()+" - "+0.0f;
	}

	private float getAdditionalFout2Derivative(Variable v, ArrayList<Variable> variables) throws Exception{
		// equ(68) et equ(70)
		if(v.getName().equals(getFlowout2().getName())){
			// derive selon flow out1 : 1;
			return 1.0f;
		}else{
			return 0.0f;
		}
	}

	private String getSymbolicAdditionalFout2Derivative(Variable v, ArrayList<Variable> variables) throws Exception{
		// equ(68) et equ(70)
		if(v.getName().equals(getFlowout2().getName())){
			// derive selon flow out1 : 1;
			return ""+1.0f;
		}else{
			return ""+0.0f;
		}
	}

	//====== Additional equation fin1 ====
	private float getAdditionalFin1Equation(Variable fin1){
		// equ(69) et equ(70)
		return fin1.getValue() - 0.0f;
	}

	private String getSymbolicAdditionalFin1Equation(Variable fin1){
		// equ(69) et equ(70)
		return ""+fin1.getName()+" - "+0.0f;
	}

	private float getAdditionalFin1Derivative(Variable v, ArrayList<Variable> variables) throws Exception{
		// equ(69) et equ(70)
		if(v.getName().equals(getFlowin1().getName())){
			// derive selon flow in1 : 1;
			return 1.0f;
		}else{
			return 0.0f;
		}
	}

	private String getSymbolicAdditionalFin1Derivative(Variable v, ArrayList<Variable> variables) throws Exception{
		// equ(69) et equ(70)
		if(v.getName().equals(getFlowin1().getName())){
			// derive selon flow in1 : 1;
			return ""+1.0f;
		}else{
			return ""+0.0f;
		}
	}

	//====== Momentum ventricle ====
	private float getMomentumVentricleEquation(Variable ventriclePr, Variable pr, Variable alfa2, Variable fout1){
		// equ(62) et equ(66)
		return ventriclePr.getValue() - pr.getValue() + alfa2.getValue() * fout1.getValue();
	}

	private String getSymbolicMomentumVentricleEquation(Variable ventriclePr, Variable pr, Variable alfa2, Variable fout1){
		// equ(62) et equ(66)
		return ""+ventriclePr.getName()+" - "+pr.getName()+" + "+alfa2.getName()+" * "+fout1.getName();
	}

	private float getMomentumVentricleDerivative(Variable v, ArrayList<Variable> variables) throws Exception{
		// equ(62) et equ(66)
		if(v.getName().equals(getPressure().getName())){
			// derive selon pression : -1;
			return -1.0f;
		}else{
			if(v.getName().equals(getFlowout1().getName())){
				// derive selon flow out 1 : T_alpha2_brain ;
				return getAlpha2().getValue();
			}else{
				ArrayList<Variable> pven = findVariableWithStruct(getHemi(), Ventricle.TUBE_NUM, ElasticTube.PRESSURE_LABEL, variables);
				if(v.getName().equals(pven.get(0).getName())){
					// derive selon ventricule pression : 1;
					return 1.0f;
				}
				return 0.0f;
			}
		}
	}

	private String getSymbolicMomentumVentricleDerivative(Variable v, ArrayList<Variable> variables) throws Exception{
		// equ(62) et equ(66)
		if(v.getName().equals(getPressure().getName())){
			// derive selon pression : -1;
			return "-"+1.0f;
		}else{
			if(v.getName().equals(getFlowout1().getName())){
				// derive selon flow out 1 : T_alpha2_brain ;
				return ""+getAlpha2().getName();
			}else{
				ArrayList<Variable> pven = findVariableWithStruct(getHemi(), Ventricle.TUBE_NUM, ElasticTube.PRESSURE_LABEL, variables);
				if(v.getName().equals(pven.get(0).getName())){
					// derive selon ventricule pression : 1;
					return ""+1.0f;
				}
				return ""+0.0f;
			}
		}
	}

	//====== Momentum cappilaries ====
	private float getMomentumCappilaryEquation(Variable pr, Variable cappilaryPr, Variable alfa1, Variable fin1){
		// equ(61) et equ(65)
		return pr.getValue() - cappilaryPr.getValue() + getAlpha1().getValue() * fin1.getValue();
	}

	private String getSymbolicMomentumCappilaryEquation(Variable pr, Variable cappilaryPr, Variable alfa1, Variable fin1){
		// equ(61) et equ(65)
		return ""+pr.getName()+" - "+cappilaryPr.getName()+" + "+getAlpha1().getName()+" * "+fin1.getName();
	}

	private float getMomentumCappilaryDerivative(Variable v, ArrayList<Variable> variables) throws Exception{
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

	private String getSymbolicMomentumCappilaryDerivative(Variable v, ArrayList<Variable> variables) throws Exception{
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
	private float getConnectivityEquation(Variable fo1, Variable fo2, Variable fi1, Variable fi2){
		// equ(60) et equ(64)
		return fo1.getValue()+fo2.getValue()-fi1.getValue()-fi2.getValue();
	}

	private String getSymbolicConnectivityEquation(Variable fo1, Variable fo2, Variable fi1, Variable fi2){
		// equ(60) et equ(64)
		return ""+fo1.getName()+"+"+fo2.getName()+"-"+fi1.getName()+"-"+fi2.getName();
	}

	private float getConnectivityDerivative(Variable v, ArrayList<Variable> variables) throws Exception{
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

	private String getSymbolicConnectivityDerivative(Variable v, ArrayList<Variable> variables) throws Exception{
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
	private float getTotalVolumeEquation(ArrayList<Variable> arteryArea, ArrayList<Variable> arteriolArea, ArrayList<Variable> cappilaryArea, ArrayList<Variable> veinuleArea,
			ArrayList<Variable> veinArea,Variable vsinousArea, Variable ventricleArea,  Variable thirdvArea, Variable fourthvArea,
			Variable sasArea, Variable brainFluidArea){
		// equ(63) et equ(67)
		float res1 = 0;
		float res2 = 0;
		for(Variable arterya: arteryArea){
			res1 += (arterya.getValue() * ((Artery)arterya.getSourceObj()).getLength().getValue());
			res2 += (((Artery)arterya.getSourceObj()).getInitialArea().getValue() * ((Artery)arterya.getSourceObj()).getLength().getValue());  
		}
		for(Variable arteriola: arteriolArea){
			res1 += (arteriola.getValue() * ((Arteriole)arteriola.getSourceObj()).getLength().getValue());  
			res2 += (((Arteriole)arteriola.getSourceObj()).getInitialArea().getValue() * ((Arteriole)arteriola.getSourceObj()).getLength().getValue());  
		}
		for(Variable capa: cappilaryArea){
			res1 += (capa.getValue() * ((Capillary)capa.getSourceObj()).getLength().getValue());
			res2 += (((Capillary)capa.getSourceObj()).getInitialArea().getValue() * ((Capillary)capa.getSourceObj()).getLength().getValue());  
		}
		for(Variable vla: veinuleArea){
			res1 += (vla.getValue() * ((Veinule)vla.getSourceObj()).getLength().getValue());  
			res2 += (((Veinule)vla.getSourceObj()).getInitialArea().getValue() * ((Veinule)vla.getSourceObj()).getLength().getValue());  
		}
		for(Variable va: veinArea){
			res1 += (va.getValue() * ((Vein)va.getSourceObj()).getLength().getValue());
			res2 += (((Vein)va.getSourceObj()).getInitialArea().getValue() * ((Vein)va.getSourceObj()).getLength().getValue());  
		}
		res1 += (vsinousArea.getValue() * ((VenousSinus)vsinousArea.getSourceObj()).getLength().getValue());  
		res2 += (((VenousSinus)vsinousArea.getSourceObj()).getInitialArea().getValue() * ((VenousSinus)vsinousArea.getSourceObj()).getLength().getValue());  
		res1 += (ventricleArea.getValue() * ((Ventricle)ventricleArea.getSourceObj()).getLength().getValue()); 
		res2 += (((Ventricle)ventricleArea.getSourceObj()).getInitialArea().getValue() * ((Ventricle)ventricleArea.getSourceObj()).getLength().getValue());  
		res1 += (0.5 * (thirdvArea.getValue() * ((ThirdVentricle)thirdvArea.getSourceObj()).getLength().getValue())); 
		res2 += (((ThirdVentricle)thirdvArea.getSourceObj()).getInitialArea().getValue() * ((ThirdVentricle)thirdvArea.getSourceObj()).getLength().getValue());  
		res1 += (0.5 * (fourthvArea.getValue() * ((FourthVentricle)fourthvArea.getSourceObj()).getLength().getValue())); 
		res2 += (((FourthVentricle)fourthvArea.getSourceObj()).getInitialArea().getValue() * ((FourthVentricle)fourthvArea.getSourceObj()).getLength().getValue());  
		res1 += (0.5 * (sasArea.getValue() * ((SAS)sasArea.getSourceObj()).getLength().getValue())); 
		res2 += (((SAS)sasArea.getSourceObj()).getInitialArea().getValue() * ((SAS)sasArea.getSourceObj()).getLength().getValue());  
		res1 += ((brainFluidArea.getValue() + getInitialAreaSolid().getValue()) * getLength().getValue());
		res2 += ((getInitialAreaFluid().getValue() + getInitialAreaSolid().getValue()) * getLength().getValue());

		return res1 - res2;
	}

	private String getSymbolicTotalVolumeEquation(ArrayList<Variable> arteryArea, ArrayList<Variable> arteriolArea, ArrayList<Variable> cappilaryArea, ArrayList<Variable> veinuleArea,
			ArrayList<Variable> veinArea,Variable vsinousArea, Variable ventricleArea,  Variable thirdvArea, Variable fourthvArea,
			Variable sasArea, Variable brainFluidArea){
		// equ(63) et equ(67)
		String res1 = "(";
		String res2 = "(";
		for(Variable arterya: arteryArea){
			res1 += "("+arterya.getName()+" * "+((Artery)arterya.getSourceObj()).getLength().getName()+") + ";
			res2 += "("+((Artery)arterya.getSourceObj()).getInitialArea().getName()+" * "+((Artery)arterya.getSourceObj()).getLength().getName()+") + ";  
		}
		for(Variable arteriola: arteriolArea){
			res1 += "("+arteriola.getName() +" * "+ ((Arteriole)arteriola.getSourceObj()).getLength().getName()+") + ";  
			res2 += "("+((Arteriole)arteriola.getSourceObj()).getInitialArea().getName() +" * "+ ((Arteriole)arteriola.getSourceObj()).getLength().getName()+") + ";  
		}
		for(Variable capa: cappilaryArea){
			res1 += "("+capa.getName() +" * "+ ((Capillary)capa.getSourceObj()).getLength().getName()+") + ";
			res2 += "("+((Capillary)capa.getSourceObj()).getInitialArea().getName() +" * "+ ((Capillary)capa.getSourceObj()).getLength().getName()+") + ";  
		}
		for(Variable vla: veinuleArea){
			res1 += "("+vla.getName() +" * "+ ((Veinule)vla.getSourceObj()).getLength().getName()+") + ";  
			res2 += "("+((Veinule)vla.getSourceObj()).getInitialArea().getName() +" * "+ ((Veinule)vla.getSourceObj()).getLength().getName()+") + ";  
		}
		for(Variable va: veinArea){
			res1 += "("+va.getName() +" * "+ ((Vein)va.getSourceObj()).getLength().getName()+") + ";
			res2 += "("+((Vein)va.getSourceObj()).getInitialArea().getName() +" * "+ ((Vein)va.getSourceObj()).getLength().getName()+") + ";  
		}
		res1 += "("+vsinousArea.getName() +" * "+ ((VenousSinus)vsinousArea.getSourceObj()).getLength().getName()+") + ";  
		res2 += "("+((VenousSinus)vsinousArea.getSourceObj()).getInitialArea().getName() +" * "+ ((VenousSinus)vsinousArea.getSourceObj()).getLength().getName()+") + ";  
		res1 += "("+ventricleArea.getName() +" * "+ ((Ventricle)ventricleArea.getSourceObj()).getLength().getName()+") + "; 
		res2 += "("+((Ventricle)ventricleArea.getSourceObj()).getInitialArea().getName() +" * "+ ((Ventricle)ventricleArea.getSourceObj()).getLength().getName()+") + ";  
		res1 += "("+0.5 +" * "+ (thirdvArea.getName() +" * "+ ((ThirdVentricle)thirdvArea.getSourceObj()).getLength().getName())+") + "; 
		res2 += "("+((ThirdVentricle)thirdvArea.getSourceObj()).getInitialArea().getName() +" * "+ ((ThirdVentricle)thirdvArea.getSourceObj()).getLength().getName()+") + ";  
		res1 += "("+0.5 +" * "+ (fourthvArea.getName()+" * "+ ((FourthVentricle)fourthvArea.getSourceObj()).getLength().getName())+") + "; 
		res2 += "("+((FourthVentricle)fourthvArea.getSourceObj()).getInitialArea().getName() +" * "+ ((FourthVentricle)fourthvArea.getSourceObj()).getLength().getName()+") + ";  
		res1 += "("+0.5 +" * "+ (sasArea.getName() +" * "+ ((SAS)sasArea.getSourceObj()).getLength().getName())+") + "; 
		res2 += "("+((SAS)sasArea.getSourceObj()).getInitialArea().getName() +" * "+ ((SAS)sasArea.getSourceObj()).getLength().getName()+") + ";  
		res1 += "(("+brainFluidArea.getName()+" + "+getInitialAreaSolid().getName() +") * "+ getLength().getName()+")";
		res2 += "(("+getInitialAreaFluid().getName()+" + "+getInitialAreaSolid().getName() +") * "+ getLength().getName()+")";

		return res1+" - "+res2;
	}

	private float getTotalVolumeDerivative(Variable v, ArrayList<Variable> variables) throws Exception{
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

	private String getSymbolicTotalVolumeDerivative(Variable v, ArrayList<Variable> variables) throws Exception{
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
