package models;

import java.util.ArrayList;

public class SpinalCord extends ElasticTube {

	public static final float DEFAULT_LENGTH = 43.0f;
	public static final float DEFAULT_AREA = 2.0f;
	public static final float DEFAULT_ALPHA = 0.1f * 1333.2240f;
	public static final float DEFAULT_ELASTANCE = 400.0f * 1333.2240f;
	public static final float DEFAULT_FLOWIN = 0.0f;
	public static final float DEFAULT_FLOWOUT = 0.0f;
	public static final float DEFAULT_PRESSURE = 13332.24f;
	
	public SpinalCord(String name) {
		super(name, DEFAULT_LENGTH,DEFAULT_AREA,DEFAULT_ALPHA,DEFAULT_ELASTANCE, DEFAULT_FLOWIN, DEFAULT_FLOWOUT, DEFAULT_PRESSURE);
	}

	public SpinalCord(String name, float len, float a) {
		super(name, len, a, DEFAULT_ALPHA, DEFAULT_ELASTANCE, DEFAULT_FLOWIN, DEFAULT_FLOWOUT, DEFAULT_PRESSURE);
	}
	
	public SpinalCord(String name, float len, float a, float alpha, float elast, float fin, float fout, float press) {
		super(name, len, a, alpha, elast, fin, fout, press);
	}
	

	public SpinalCord(String name, float len, float a, float alpha, float elast, float fin, float fout, float press, ArrayList<ElasticTube> par,
			ArrayList<ElasticTube> child) {
		super(name, len, a, alpha, elast, fin, fout, press, par, child);
	}
	
	public String toString(){
		return "SpinalCord : "+super.toString();
	}

}
