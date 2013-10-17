package models;

import java.util.ArrayList;

public class Arteriole extends ElasticTube {
	public static final float DEFAULT_LENGTH = 1.75f;
	public static final float DEFAULT_AREA = 4.74f;
	public static final float DEFAULT_ELASTANCE = 2735000.0f;// en Pa
	public static final float DEFAULT_ALPHA = 3.076819468f * 1333.2240f;
	
	public Arteriole(String name) {
		super(name, DEFAULT_LENGTH,DEFAULT_AREA,DEFAULT_ALPHA,DEFAULT_ELASTANCE);
	}

	public Arteriole(String name, float len, float a) {
		super(name, len, a, DEFAULT_ALPHA, DEFAULT_ELASTANCE);
	}
	
	public Arteriole(String name, float len, float a, float alpha) {
		super(name, len, a, alpha, DEFAULT_ELASTANCE);
	}
	
	public Arteriole(String name, float len, float a, float alpha, float elast) {
		super(name, len, a, alpha, elast);
	}

	public Arteriole(String name, float len, float a, ArrayList<ElasticTube> par,
			ArrayList<ElasticTube> child) {
		super(name, len, a, DEFAULT_ALPHA, DEFAULT_ELASTANCE, par, child);
	}
	
	public String toString(){
		return "Arteriole : "+super.toString();
	}

}
