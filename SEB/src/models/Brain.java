package models;

public class Brain extends Tube {
	public static final float DEFAULT_LENGTH = 7.0f;// par hemisphere
	public static final float DEFAULT_AREA_FLUID = 30.0f;
	public static final float DEFAULT_AREA_SOLID = 70.0f;
	public static final float DEFAULT_ALPHA1 = 8152.42f * 1333.2240f;
	public static final float DEFAULT_ALPHA2 = 500.0f * 1333.2240f;
	
	private float areaFluid;
	private float areaSolid;
	private float alpha1; // connection to capillaries
	private float alpha2; // connection to ventricles
	
	public Brain(String name) {
		super(name, DEFAULT_LENGTH);
		setAreaFluid(DEFAULT_AREA_FLUID);
		setAreaSolid(DEFAULT_AREA_SOLID);
		setAlpha1(DEFAULT_ALPHA1);
		setAlpha2(DEFAULT_ALPHA2);
	}

	public Brain(String name, float len) {
		super(name, len);
		setAreaFluid(DEFAULT_AREA_FLUID);
		setAreaSolid(DEFAULT_AREA_SOLID);
		setAlpha1(DEFAULT_ALPHA1);
		setAlpha2(DEFAULT_ALPHA2);
	}
	
	public Brain(String name, float len, float aFluid, float aSolid, float alf1, float alf2) {
		super(name, len);
		setAreaFluid(aFluid);
		setAreaSolid(aSolid);
		setAlpha1(alf1);
		setAlpha2(alf2);
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

}
