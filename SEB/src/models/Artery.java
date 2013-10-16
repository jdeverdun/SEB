package models;

import java.util.ArrayList;

public class Artery extends Vessel {
	public static final float DEFAULT_LENGTH = 4.15f;
	public static final float DEFAULT_AREA = 3.42f;
	public static final float DEFAULT_ELASTANCE = 1066579.2f;// en Pa
	public static final float DEFAULT_ALPHA = 0.7692f * 1333.2240f;
	
	public Artery(String name) {
		super(name, DEFAULT_LENGTH,DEFAULT_AREA,DEFAULT_ALPHA,DEFAULT_ELASTANCE);
	}

	public Artery(String name, float len, float a) {
		super(name, len, a, DEFAULT_ALPHA, DEFAULT_ELASTANCE);
	}
	
	public Artery(String name, float len, float a, float alpha) {
		super(name, len, a, alpha, DEFAULT_ELASTANCE);
	}
	
	public Artery(String name, float len, float a, float alpha, float elast) {
		super(name, len, a, alpha, elast);
	}

	public Artery(String name, float len, float a, ArrayList<Vessel> par,
			ArrayList<Vessel> child) {
		super(name, len, a, DEFAULT_ALPHA, DEFAULT_ELASTANCE, par, child);
	}
	
	public String toString(){
		return "Artery : "+super.toString();
	}

}
