package models;

import java.util.ArrayList;

public class Arteriole extends ElasticTube {
	public static final float DEFAULT_LENGTH = 1.75f;
	public static final float DEFAULT_AREA = 4.74f;
	public static final float DEFAULT_ELASTANCE = 2735000.0f;// en Pa
	public static final float DEFAULT_ALPHA = 3.076819468f * 1333.2240f;
	public static final float DEFAULT_FLOWIN = 13.0f;
	public static final float DEFAULT_FLOWOUT = 13.0f;
	public static final float DEFAULT_PRESSURE = 80.0f*1333.2240f;
	
	public Arteriole(String name, Hemisphere hemi) {
		super(name, hemi, DEFAULT_LENGTH,DEFAULT_AREA,DEFAULT_ALPHA,DEFAULT_ELASTANCE, DEFAULT_FLOWIN, DEFAULT_FLOWOUT, DEFAULT_PRESSURE);
	}

	public Arteriole(String name, Hemisphere hemi, float len, float a) {
		super(name, hemi, len, a, DEFAULT_ALPHA, DEFAULT_ELASTANCE, DEFAULT_FLOWIN, DEFAULT_FLOWOUT, DEFAULT_PRESSURE);
	}
	
	public Arteriole(String name, Hemisphere hemi, float len, float a, float alpha, float elast, float fin, float fout, float press) {
		super(name, hemi, len, a, alpha, elast, fin, fout, press);
	}
	

	public Arteriole(String name, Hemisphere hemi, float len, float a, float alpha, float elast, float fin, float fout, float press, ArrayList<ElasticTube> par,
			ArrayList<ElasticTube> child) {
		super(name, hemi, len, a, alpha, elast, fin, fout, press, par, child);
	}
	
	public String toString(){
		return "Arteriole : "+super.toString();
	}

	@Override
	public String getTubeNum() {
		return "1";
	}
	
	// --------------- EQUATIONS -------------
	@Override
	public ArrayList<float[]> getEquations(ArrayList<Variable> variables) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
	private float getContinuityEquation(Variable area, Variable flowin, Variable flowout){
		// equ(2)
		return (area.getValue() - getArea().getValue())/ModelSpecification.dt + (- flowin.getValue() + flowout.getValue())/getLength().getValue();
	}





}
