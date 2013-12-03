package models;

import java.util.ArrayList;

import params.ModelSpecification;

public abstract class ElasticTube extends Tube {
	protected static String AREA_LABEL = "A";
	protected static String INITIAL_AREA_LABEL = "A0";
	protected static String PRESSURE_LABEL = "P";
	protected static String FLOWIN_LABEL = "fi";
	protected static String FLOWOUT_LABEL = "fo";
	protected static String LENGTH_LABEL = "l0";
	protected static String ALPHA_LABEL = "alfa";
	protected static String ELASTANCE_LABEL = "E";
	protected Variable flowin;
	protected Variable flowout;
	protected Variable pressure;
	protected Variable area;
	protected Variable alpha;
	protected Variable elastance;
	protected Variable initialArea;
	protected Hemisphere hemisphere;
	protected ArrayList<ElasticTube> parents;
	protected ArrayList<ElasticTube> children;
	
	public ElasticTube(){
		super();
		setFlowin(-1.0f);
		setFlowout(-1.0f);
		setPressure(-1.0f);
		setArea(-1.0f);
		setAlpha(-1.0f);
		setInitialArea(-1.0f);
		hemisphere = Hemisphere.UNKNOWN;
		parents = new ArrayList<ElasticTube>();
		children = new ArrayList<ElasticTube>();
	}
	
	public ElasticTube(String name, Hemisphere hemi, float len, float a, float alf, float elast, float flowin, float flowout, float pressure){
		super(name, len);
		setInitialArea(a);
		setArea(a);
		setAlpha(alf);
		setElastance(elast);
		setFlowin(flowin);
		setFlowout(flowout);
		setPressure(pressure);
		setHemisphere(hemi);
		parents = new ArrayList<ElasticTube>();
		children = new ArrayList<ElasticTube>();
	}	
	
	public ElasticTube(String name, Hemisphere hemi, float len, float a, float alf, float elast, float flowin, float flowout, float pressure, ArrayList<ElasticTube> par, ArrayList<ElasticTube> child){
		super(name,len);
		setHemisphere(hemi);
		setArea(a);
		setAlpha(alf);
		setElastance(elast);
		setFlowin(flowin);
		setFlowout(flowout);
		setPressure(pressure);
		setParents(par);
		setChildrens(child);
	}

	protected Variable getLength() {
		return length;
	}
	protected void setLength(float length) {
		this.length = new Variable(TUBE_LABEL+getTubeNum()+"_"+LENGTH_LABEL,length, (Tube)this);
	}
	
	protected ArrayList<ElasticTube> getParents() {
		return parents;
	}

	protected void setParents(ArrayList<ElasticTube> parents) {
		this.parents = parents;
	}

	protected ArrayList<ElasticTube> getChildrens() {
		return children;
	}

	protected void setChildrens(ArrayList<ElasticTube> childrens) {
		this.children = childrens;
	}

	protected Variable getElastance() {
		return elastance;
	}

	protected void setElastance(float elastance) {
		Variable v = new Variable("T"+getTubeNum()+"_"+ELASTANCE_LABEL+"_"+ID,elastance, (Tube)this);
		this.elastance = v;
	}	
	protected Variable getArea() {
		return area;
	}
	protected void setArea(float area){
		String prefix = "";
		if(hemisphere == Hemisphere.LEFT)
			prefix = "L_";
		else
			if(hemisphere == Hemisphere.RIGHT)
				prefix = "R_";
		Variable v = new Variable(prefix+TUBE_LABEL+getTubeNum()+"_"+AREA_LABEL+"_"+ID,area, (Tube)this);
		this.area = v;
	}

	protected Variable getAlpha() {
		return alpha;
	}

	protected void setAlpha(float alpha) {
		Variable v = new Variable(TUBE_LABEL+getTubeNum()+"_"+ALPHA_LABEL+"_"+ID,alpha, (Tube)this);
		this.alpha = v;
	}
	public Variable getInitialArea() {
		return initialArea;
	}

	public void setInitialArea(float iArea) {
		Variable v = new Variable(TUBE_LABEL+getTubeNum()+"_"+INITIAL_AREA_LABEL+"_"+ID,iArea, (Tube)this);
		this.initialArea = v;
	}

	public Variable getFlowin() {
		return flowin;
	}

	public void setFlowin(float flowin){
		String prefix = "";
		if(hemisphere == Hemisphere.LEFT)
			prefix = "L_";
		else
			if(hemisphere == Hemisphere.RIGHT)
				prefix = "R_";
		Variable v = new Variable(prefix+TUBE_LABEL+getTubeNum()+"_"+FLOWIN_LABEL+"_"+ID,flowin, (Tube)this);
		this.flowin = v;
	}

	public Variable getFlowout() {
		return flowout;
	}

	public void setFlowout(float flowout){
		String prefix = "";
		if(hemisphere == Hemisphere.LEFT)
			prefix = "L_";
		else
			if(hemisphere == Hemisphere.RIGHT)
				prefix = "R_";
		Variable v = new Variable(prefix+TUBE_LABEL+getTubeNum()+"_"+FLOWOUT_LABEL+"_"+ID,flowout, (Tube)this);
		this.flowout = v;
	}

	public Variable getPressure() {
		return pressure;
	}

	public void setPressure(float pressure){
		String prefix = "";
		if(hemisphere == Hemisphere.LEFT)
			prefix = "L_";
		else
			if(hemisphere == Hemisphere.RIGHT)
				prefix = "R_";
		Variable v = new Variable(prefix+TUBE_LABEL+getTubeNum()+"_"+PRESSURE_LABEL+"_"+ID,pressure, (Tube)this);
		this.pressure = v;
	}

	public Hemisphere getHemisphere() {
		return hemisphere;
	}

	public void setHemisphere(Hemisphere hemisphere) {
		this.hemisphere = hemisphere;
	}
	
	/**
	 * Renvoi le parenchyme associe a l'hemisphere de l'objet, nul si non applicable
	 * @return
	 */
	protected BrainParenchyma getAssociatedBrainParenchyma(){
		if(getHemisphere() != Hemisphere.LEFT && getHemisphere() != Hemisphere.RIGHT)
			return null;
		if(getHemisphere() == Hemisphere.LEFT)
			return ModelSpecification.architecture.getBrain().getLeftHemi();
		else
			return ModelSpecification.architecture.getBrain().getRightHemi();
	}
	public String toString(){
		return super.toString()+" - Elastance = "+getElastance()+" - Area = "+getArea()+" - Alpha = "+getAlpha()+" - Hemisphere = "+getHemisphere();
	}
	
	public boolean addParent(ElasticTube parent){
		return parents.add(parent);
	}
	public boolean addChild(ElasticTube child){
		return children.add(child);
	}
	
	
	
}
