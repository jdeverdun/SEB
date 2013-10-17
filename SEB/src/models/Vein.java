package models;

import java.util.ArrayList;

public class Vein extends ElasticTube {
	public static final float DEFAULT_LENGTH = 3.0906f;
	public static final float DEFAULT_AREA = 5.3388f;
	public static final float DEFAULT_ELASTANCE = 1008666.7f;// en Pa
	public static final float DEFAULT_ALPHA = 2.309223857f * 1333.2240f;
	public static final float DEFAULT_FLOWIN = 13.0f;
	public static final float DEFAULT_FLOWOUT = 13.0f;
	public static final float DEFAULT_PRESSURE = 5.0f * 1333.2240f;
	
	public Vein(String name) {
		super(name, DEFAULT_LENGTH,DEFAULT_AREA,DEFAULT_ALPHA,DEFAULT_ELASTANCE, DEFAULT_FLOWIN, DEFAULT_FLOWOUT, DEFAULT_PRESSURE);
	}

	public Vein(String name, float len, float a) {
		super(name, len, a, DEFAULT_ALPHA, DEFAULT_ELASTANCE, DEFAULT_FLOWIN, DEFAULT_FLOWOUT, DEFAULT_PRESSURE);
	}
	
	public Vein(String name, float len, float a, float alpha, float elast, float fin, float fout, float press) {
		super(name, len, a, alpha, elast, fin, fout, press);
	}
	

	public Vein(String name, float len, float a, float alpha, float elast, float fin, float fout, float press, ArrayList<ElasticTube> par,
			ArrayList<ElasticTube> child) {
		super(name, len, a, alpha, elast, fin, fout, press, par, child);
	}
	
	public String toString(){
		return "Vein : "+super.toString();
	}

}
