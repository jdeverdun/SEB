package models;

import java.util.ArrayList;

public abstract class ElasticTube extends Tube {
	protected static String AREA_LABEL = "A";
	protected static String PRESSURE_LABEL = "P";
	protected static String FLOWIN_LABEL = "fi";
	protected static String FLOWOUT_LABEL = "fo";
	protected String TUBE_NUM;
	protected Variable flowin;
	protected Variable flowout;
	protected Variable pressure;
	protected Variable area;
	protected float alpha;
	protected float elastance;
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
		hemisphere = Hemisphere.UNKNOWN;
		parents = new ArrayList<ElasticTube>();
		children = new ArrayList<ElasticTube>();
	}
	
	public ElasticTube(String name, Hemisphere hemi, float len, float a, float alf, float elast, float flowin, float flowout, float pressure){
		super(name, len);
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

	protected float getElastance() {
		return elastance;
	}

	protected void setElastance(float elastance) {
		this.elastance = elastance;
	}	
	protected Variable getArea() {
		return area;
	}
	protected void setArea(float area){
		Variable v = new Variable("T"+TUBE_NUM+"_"+AREA_LABEL+"_"+ID,area);
		this.area = v;
	}

	protected float getAlpha() {
		return alpha;
	}

	protected void setAlpha(float alpha) {
		this.alpha = alpha;
	}
	public Variable getFlowin() {
		return flowin;
	}

	public void setFlowin(float flowin){
		Variable v = new Variable("T"+TUBE_NUM+"_"+FLOWIN_LABEL+"_"+ID,flowin);
		this.flowin = v;
	}

	public Variable getFlowout() {
		return flowout;
	}

	public void setFlowout(float flowout){
		Variable v = new Variable("T"+TUBE_NUM+"_"+FLOWOUT_LABEL+"_"+ID,flowout);
		this.flowout = v;
	}

	public Variable getPressure() {
		return pressure;
	}

	public void setPressure(float pressure){
		Variable v = new Variable("T"+TUBE_NUM+"_"+PRESSURE_LABEL+"_"+ID,pressure);
		this.pressure = v;
	}

	public Hemisphere getHemisphere() {
		return hemisphere;
	}

	public void setHemisphere(Hemisphere hemisphere) {
		this.hemisphere = hemisphere;
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
	
	
	// ---------    EQUATIONS ---------------
	public abstract ArrayList<float[]> getEquations(ArrayList<Variable> variables);
}
