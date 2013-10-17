package models;

public class Brain extends Tube {
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
	
	private float areaFluid;
	private float areaSolid;
	private float alpha1; // connection to capillaries
	private float alpha2; // connection to ventricles
	private float flowin1;
	private float flowin2;
	private float flowout1;
	private float flowout2;
	private float pressure;
	
	
	public Brain(String name) {
		super(name, DEFAULT_LENGTH);
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

	public Brain(String name, float len) {
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
	
	public Brain(String name, float len, float aFluid, float aSolid, float alf1, float alf2, float fin1, float fin2, float fout1, float fout2, float press) {
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

	public float getAreaFluid() {
		return areaFluid;
	}

	public void setAreaFluid(float areaFluid) {
		this.areaFluid = areaFluid;
	}

	public float getAreaSolid() {
		return areaSolid;
	}

	public void setAreaSolid(float areaSolid) {
		this.areaSolid = areaSolid;
	}

	public float getAlpha1() {
		return alpha1;
	}

	public void setAlpha1(float alpha1) {
		this.alpha1 = alpha1;
	}

	public float getAlpha2() {
		return alpha2;
	}

	public void setAlpha2(float alpha2) {
		this.alpha2 = alpha2;
	}

	public float getFlowin1() {
		return flowin1;
	}

	public void setFlowin1(float flowin1) {
		this.flowin1 = flowin1;
	}

	public float getFlowin2() {
		return flowin2;
	}

	public void setFlowin2(float flowin2) {
		this.flowin2 = flowin2;
	}

	public float getFlowout1() {
		return flowout1;
	}

	public void setFlowout1(float flowout1) {
		this.flowout1 = flowout1;
	}

	public float getFlowout2() {
		return flowout2;
	}

	public void setFlowout2(float flowout2) {
		this.flowout2 = flowout2;
	}

	public float getPressure() {
		return pressure;
	}

	public void setPressure(float pressure) {
		this.pressure = pressure;
	}

}
