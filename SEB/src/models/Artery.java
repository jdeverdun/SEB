package models;

import java.io.ObjectInputStream.GetField;
import java.util.ArrayList;

import params.ModelSpecification;

public class Artery extends ElasticTube {
	public static final float DEFAULT_LENGTH = 4.15f;
	public static final float DEFAULT_AREA = 3.42f;
	public static final float DEFAULT_ELASTANCE = 1066579.2f;// en Pa
	public static final float DEFAULT_ALPHA = 0.7692f * 1333.2240f;
	public static final float DEFAULT_FLOWIN = 13.0f;
	public static final float DEFAULT_FLOWOUT = 13.0f;
	public static final float DEFAULT_PRESSURE = 100.0f * 1333.2240f;
	
	public Artery(String name, Hemisphere hemi) {
		super(name, hemi, DEFAULT_LENGTH,DEFAULT_AREA,DEFAULT_ALPHA,DEFAULT_ELASTANCE, DEFAULT_FLOWIN, DEFAULT_FLOWOUT, DEFAULT_PRESSURE);
	}

	public Artery(String name, Hemisphere hemi, float len, float a) {
		super(name, hemi, len, a, DEFAULT_ALPHA, DEFAULT_ELASTANCE, DEFAULT_FLOWIN, DEFAULT_FLOWOUT, DEFAULT_PRESSURE);
	}
	
	public Artery(String name, Hemisphere hemi, float len, float a, float alpha, float elast, float fin, float fout, float press) {
		super(name, hemi, len, a, alpha, elast, fin, fout, press);
	}
	

	public Artery(String name, Hemisphere hemi, float len, float a, float alpha, float elast, float fin, float fout, float press, ArrayList<ElasticTube> par,
			ArrayList<ElasticTube> child) {
		super(name, hemi, len, a, alpha, elast, fin, fout, press, par, child);
	}
	
	@Override
	public String getTubeNum() {
		return "0";
	}
	
	public String toString(){
		return "Artery : "+super.toString();
	}
	
	
	// ------------------- EQUATIONS -------------
	@Override
	public ArrayList<float[]> getEquations(ArrayList<Variable> variables) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
	private float getContinuityEquation(Variable ar, Variable fi, Variable fo){
		// equ(1) et equ(6)
		return (ar.getValue() - getArea().getValue())/ModelSpecification.dt + (- fi.getValue() + fo.getValue())/getLength().getValue();
	}
	
	private float getDistensibilityEquation(Variable ar, Variable pr, Variable pbrain){
		// equ(16) et equ(21)
		return -ModelSpecification.damp * (ar.getValue() - getArea().getValue())/ModelSpecification.dt + (pr.getValue()-pbrain.getValue())-getElastance().getValue()*(ar.getValue()/getInitialArea().getValue()-1);
	}
	
	private float getMomentumEquation(Variable fi, Variable ar, Variable pr){
		// equ(31) et equ(36)
		return ModelSpecification.damp2 * ((fi.getValue()/ar.getValue()) - (getFlowin().getValue()/getArea().getValue()))/ModelSpecification.dt + (ModelSpecification.P_INIT[ModelSpecification.currentIter] - pr.getValue())-getAlpha().getValue()*fi.getValue();
	}
	
	// symbolic equation (en chaine de caractere)
	private String getSymbolicContinuityEquation(Variable ar, Variable fi, Variable fo){
		// equ(1) et equ(6)
		return "" + "("+ar.getName()+" - "+getArea().getName()+")/dt"+" + (- "+fi.getName()+"+"+ fo.getName()+")/"+getLength().getName();
	}
	
	private String getSymbolicDistensibilityEquation(Variable ar, Variable pr, Variable pbrain){
		// equ(16) et equ(21)
		return " -damp * ("+ar.getName()+" - "+getArea().getName()+" )/dt + ("+pr.getName()+"-"+pbrain.getName()+" )-"+getElastance().getName()+" * ("+ar.getName()+" / "+getInitialArea().getName()+" -1)";
	}
	
	private String getSymbolicMomentumEquation(Variable fi, Variable ar, Variable pr){
		// equ(31) et equ(36)
		return " damp2 * (("+fi.getName()+" / "+ar.getName()+" ) - ("+getFlowin().getName()+" / "+getArea().getName()+" ))/ dt + (P_INIT[currentIter] - "+pr.getName()+" )-"+getAlpha().getName()+" * "+fi.getName();
	}
	
	// ------- Derive -----------
	private float getContinuityDerivative(Variable v){
		// equ(1) et equ(6)
		
		if(v.getName().equals(getArea().getName())){
			// derive selon area : 1/dt 
			return 1.0f/ModelSpecification.dt;
		}else{
			if(v.getName().equals(getFlowin().getName())){
				// derive selon flowin : - 1/T0_l0;
				return -1.0f/getLength().getValue();
			}else{
				if(v.getName().equals(getFlowout().getName())){
					// derive selon flowout : 1/T0_l0
					return 1.0f/getLength().getValue();		
				}else{
					return 0.0f;
				}
			}
		}
	}
	
	private float getDistensibilityDerivative(Variable v){
		// equ(16) et equ(21)
		
		if(v.getName().equals(getArea().getName())){
			// derive selon area : -damp/dt - T0_E * (1/T0_A0) 
			return -ModelSpecification.damp/ModelSpecification.dt-getElastance().getValue()*(1.0f/getInitialArea().getValue());
		}else{
			if(v.getName().equals(getPressure().getName())){
				// derive selon pression : 1.0f
				return 1.0f;
			}else{
				if(v.getName().equals(getAssociatedBrainParenchyma().getPressure().getName())){
					// derive selon pression brain : - 1 
					return -1.0f;		
				}else{
					return 0.0f;
				}
			}
		}
	}
	

}
