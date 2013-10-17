package models;

import java.util.ArrayList;

public class FirstArtery extends ElasticTube {
	public static final float DEFAULT_LENGTH = 0.5f;
	public static final float DEFAULT_AREA = 3.42f;
	public static final float DEFAULT_ELASTANCE = 1066579.2f;// en Pa
	public static final float DEFAULT_ALPHA = 0.1618175f * 1333.2240f;
	public static final float DEFAULT_FLOWIN = 12.4f;
	public static final float DEFAULT_FLOWOUT = 12.4f;
	public static final float DEFAULT_PRESSURE = 133322.4f;
	
	public FirstArtery(String name) {
		super(name, DEFAULT_LENGTH,DEFAULT_AREA,DEFAULT_ALPHA,DEFAULT_ELASTANCE, DEFAULT_FLOWIN, DEFAULT_FLOWOUT, DEFAULT_PRESSURE);
	}

	public FirstArtery(String name, float len, float a) {
		super(name, len, a, DEFAULT_ALPHA, DEFAULT_ELASTANCE, DEFAULT_FLOWIN, DEFAULT_FLOWOUT, DEFAULT_PRESSURE);
	}
	
	public FirstArtery(String name, float len, float a, float alpha, float elast, float fin, float fout, float press) {
		super(name, len, a, alpha, elast, fin, fout, press);
	}
	

	public FirstArtery(String name, float len, float a, float alpha, float elast, float fin, float fout, float press, ArrayList<ElasticTube> par,
			ArrayList<ElasticTube> child) {
		super(name, len, a, alpha, elast, fin, fout, press, par, child);
	}
	
	public String toString(){
		return "FirstArtery : "+super.toString();
	}

}
