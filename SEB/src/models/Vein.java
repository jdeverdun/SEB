package models;

import java.util.ArrayList;

public class Vein extends ElasticTube {
	public static final float DEFAULT_LENGTH = 4.074f;
	public static final float DEFAULT_AREA = 5.3388f;
	public static final float DEFAULT_ELASTANCE = 1008666.7f;// en Pa
	public static final float DEFAULT_ALPHA = 0.1457065f * 1333.2240f;
	public static final float DEFAULT_FLOWIN = 6.5f;
	public static final float DEFAULT_FLOWOUT = 6.5f;
	public static final float DEFAULT_PRESSURE = 5332.89f;
	
	public Vein(String name, Hemisphere hemi) {
		super(name, hemi, DEFAULT_LENGTH,DEFAULT_AREA,DEFAULT_ALPHA,DEFAULT_ELASTANCE, DEFAULT_FLOWIN, DEFAULT_FLOWOUT, DEFAULT_PRESSURE);
	}

	public Vein(String name, Hemisphere hemi, float len, float a) {
		super(name, hemi, len, a, DEFAULT_ALPHA, DEFAULT_ELASTANCE, DEFAULT_FLOWIN, DEFAULT_FLOWOUT, DEFAULT_PRESSURE);
	}
	
	public Vein(String name, Hemisphere hemi, float len, float a, float alpha, float elast, float fin, float fout, float press) {
		super(name, hemi, len, a, alpha, elast, fin, fout, press);
	}
	

	public Vein(String name, Hemisphere hemi, float len, float a, float alpha, float elast, float fin, float fout, float press, ArrayList<ElasticTube> par,
			ArrayList<ElasticTube> child) {
		super(name, hemi, len, a, alpha, elast, fin, fout, press, par, child);
	}
	
	public String toString(){
		return "Vein : "+super.toString();
	}
	@Override
	public String getTubeNum() {
		return "3b";
	}

}
