package models;

import java.util.ArrayList;

public class FourthVentricle extends ElasticTube {

	public static final float DEFAULT_LENGTH = 1.0f;
	public static final float DEFAULT_AREA = 3.5f;
	public static final float DEFAULT_ALPHA = 1.0f * 1333.2240f;
	public static final float DEFAULT_ELASTANCE = 10.0f * 1333.2240f;
	
	public FourthVentricle(String name) {
		super(name, DEFAULT_LENGTH,DEFAULT_AREA,DEFAULT_ALPHA,DEFAULT_ELASTANCE);
	}

	public FourthVentricle(String name, float len, float a) {
		super(name, len, a, DEFAULT_ALPHA, DEFAULT_ELASTANCE);
	}
	
	public FourthVentricle(String name, float len, float a, float alpha) {
		super(name, len, a, alpha, DEFAULT_ELASTANCE);
	}
	
	public FourthVentricle(String name, float len, float a, float alpha, float elast) {
		super(name, len, a, alpha, elast);
	}

	public FourthVentricle(String name, float len, float a, ArrayList<ElasticTube> par,
			ArrayList<ElasticTube> child) {
		super(name, len, a, DEFAULT_ALPHA, DEFAULT_ELASTANCE, par, child);
	}
	
	public String toString(){
		return "FourthVentricle : "+super.toString();
	}

}
