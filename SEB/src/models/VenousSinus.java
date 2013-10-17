package models;

import java.util.ArrayList;

public class VenousSinus extends ElasticTube {
	public static final float DEFAULT_LENGTH = 15.0f;
	public static final float DEFAULT_AREA = 2.0f * 0.43f;
	public static final float DEFAULT_ELASTANCE = 120000.0f;// en Pa
	public static final float DEFAULT_ALPHA = 0.161896f * 1333.2240f;
	
	public VenousSinus(String name) {
		super(name, DEFAULT_LENGTH,DEFAULT_AREA,DEFAULT_ALPHA,DEFAULT_ELASTANCE);
	}

	public VenousSinus(String name, float len, float a) {
		super(name, len, a, DEFAULT_ALPHA, DEFAULT_ELASTANCE);
	}
	
	public VenousSinus(String name, float len, float a, float alpha) {
		super(name, len, a, alpha, DEFAULT_ELASTANCE);
	}
	
	public VenousSinus(String name, float len, float a, float alpha, float elast) {
		super(name, len, a, alpha, elast);
	}

	public VenousSinus(String name, float len, float a, ArrayList<ElasticTube> par,
			ArrayList<ElasticTube> child) {
		super(name, len, a, DEFAULT_ALPHA, DEFAULT_ELASTANCE, par, child);
	}
	
	public String toString(){
		return "VenousSinus : "+super.toString();
	}

}
