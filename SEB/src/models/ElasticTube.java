package models;

import java.util.ArrayList;

public abstract class ElasticTube extends Tube {
	protected float flowin;
	protected float flowout;
	protected float pressure;
	protected float area;
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
	protected float getArea() {
		return area;
	}
	protected void setArea(float area) {
		this.area = area;
	}

	protected float getAlpha() {
		return alpha;
	}

	protected void setAlpha(float alpha) {
		this.alpha = alpha;
	}
	public float getFlowin() {
		return flowin;
	}

	public void setFlowin(float flowin) {
		this.flowin = flowin;
	}

	public float getFlowout() {
		return flowout;
	}

	public void setFlowout(float flowout) {
		this.flowout = flowout;
	}

	public float getPressure() {
		return pressure;
	}

	public void setPressure(float pressure) {
		this.pressure = pressure;
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
}
