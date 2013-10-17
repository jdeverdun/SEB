package models;

import java.util.ArrayList;

public class FirstArtery extends ElasticTube {
	public static final float DEFAULT_LENGTH = 0.5f;
	public static final float DEFAULT_AREA = 3.42f;
	public static final float DEFAULT_ELASTANCE = 1066579.2f;// en Pa
	public static final float DEFAULT_ALPHA = 0.1618175f * 1333.2240f;
	
	public FirstArtery(String name) {
		super(name, DEFAULT_LENGTH,DEFAULT_AREA,DEFAULT_ALPHA,DEFAULT_ELASTANCE);
	}

	public FirstArtery(String name, float len, float a) {
		super(name, len, a, DEFAULT_ALPHA, DEFAULT_ELASTANCE);
	}
	
	public FirstArtery(String name, float len, float a, float alpha) {
		super(name, len, a, alpha, DEFAULT_ELASTANCE);
	}
	
	public FirstArtery(String name, float len, float a, float alpha, float elast) {
		super(name, len, a, alpha, elast);
	}

	public FirstArtery(String name, float len, float a, ArrayList<ElasticTube> par,
			ArrayList<ElasticTube> child) {
		super(name, len, a, DEFAULT_ALPHA, DEFAULT_ELASTANCE, par, child);
	}
	
	public String toString(){
		return "FirstArtery : "+super.toString();
	}

}
