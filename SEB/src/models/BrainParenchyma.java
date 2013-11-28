package models;

public class BrainParenchyma extends Tube {
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
	private Hemisphere hemi;
	private Variable areaFluid;
	private Variable areaSolid;
	private Variable alpha1; // connection to capillaries
	private Variable alpha2; // connection to ventricles
	private Variable flowin1;
	private Variable flowin2;
	private Variable flowout1;
	private Variable flowout2;
	private Variable pressure;
	
	
	public BrainParenchyma(String name, Hemisphere hemi) {
		super(name, DEFAULT_LENGTH);
		setHemi(hemi);
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

	public BrainParenchyma(String name, float len) {
		super(name, len);
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
	
	public BrainParenchyma(String name, float len, float aFluid, float aSolid, float alf1, float alf2, float fin1, float fin2, float fout1, float fout2, float press) {
		super(name, len);
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
		return "";
	}
	
	protected Variable getLength() {
		return length;
	}
	
	protected void setLength(float length) {
		this.length = new Variable(TUBE_LABEL+getTubeNum()+"_"+LENGTH_LABEL+BRAIN_LABEL,length);
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
		Variable v = new Variable(prefix+"_"+TUBE_LABEL+getTubeNum()+"_"+AREA_FLUID_LABEL+"_"+BRAIN_LABEL,areaFluid);
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
		Variable v = new Variable(prefix+"_"+TUBE_LABEL+getTubeNum()+"_"+AREA_SOLID_LABEL+"_"+BRAIN_LABEL,areaSolid);
		this.areaSolid = v;
	}

	public Variable getAlpha1() {
		return alpha1;
	}

	public void setAlpha1(float alpha1) {
		Variable v = new Variable(TUBE_LABEL+getTubeNum()+"_"+ALPHA1_LABEL+"_"+BRAIN_LABEL,alpha1);
		this.alpha1 = v;
	}

	public Variable getAlpha2() {
		return alpha2;
	}

	public void setAlpha2(float alpha2) {
		Variable v = new Variable(TUBE_LABEL+getTubeNum()+"_"+ALPHA2_LABEL+"_"+BRAIN_LABEL,alpha2);
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
		Variable v = new Variable(prefix+"_"+TUBE_LABEL+getTubeNum()+"_"+FLOWIN1_LABEL+"_"+BRAIN_LABEL,flowin1);
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
		Variable v = new Variable(prefix+"_"+TUBE_LABEL+getTubeNum()+"_"+FLOWIN2_LABEL+"_"+BRAIN_LABEL,flowin2);
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
		Variable v = new Variable(prefix+"_"+TUBE_LABEL+getTubeNum()+"_"+FLOWOUT1_LABEL+"_"+BRAIN_LABEL,flowout1);
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
		Variable v = new Variable(prefix+"_"+TUBE_LABEL+getTubeNum()+"_"+FLOWOUT2_LABEL+"_"+BRAIN_LABEL,flowout2);
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
		Variable v = new Variable(prefix+"_"+TUBE_LABEL+getTubeNum()+"_"+PRESSURE_LABEL+"_"+BRAIN_LABEL,pressure);
		this.pressure = v;
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

}
