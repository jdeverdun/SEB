package models;

import java.util.ArrayList;

public class ThirdVentricle extends ElasticTube {

	public static final float DEFAULT_LENGTH = 1.0f;
	public static final float DEFAULT_AREA = 2.5f;
	public static final float DEFAULT_ALPHA = 1.0f * 1333.2240f;
	public static final float DEFAULT_ELASTANCE = 10.0f * 1333.2240f;
	public static final float DEFAULT_FLOWIN = 0.06f;
	public static final float DEFAULT_FLOWOUT = 0.06f;
	public static final float DEFAULT_PRESSURE = 13332.24f;
	
	public ThirdVentricle(String name) {
		super(name, DEFAULT_LENGTH,DEFAULT_AREA,DEFAULT_ALPHA,DEFAULT_ELASTANCE, DEFAULT_FLOWIN, DEFAULT_FLOWOUT, DEFAULT_PRESSURE);
	}

	public ThirdVentricle(String name, float len, float a) {
		super(name, len, a, DEFAULT_ALPHA, DEFAULT_ELASTANCE, DEFAULT_FLOWIN, DEFAULT_FLOWOUT, DEFAULT_PRESSURE);
	}
	
	public ThirdVentricle(String name, float len, float a, float alpha, float elast, float fin, float fout, float press) {
		super(name, len, a, alpha, elast, fin, fout, press);
	}
	

	public ThirdVentricle(String name, float len, float a, float alpha, float elast, float fin, float fout, float press, ArrayList<ElasticTube> par,
			ArrayList<ElasticTube> child) {
		super(name, len, a, alpha, elast, fin, fout, press, par, child);
	}
	
	public String toString(){
		return "ThirdVentricle : "+super.toString();
	}

}
