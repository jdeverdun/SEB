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
	protected SimpleVariable flowin;
	protected SimpleVariable flowout;
	protected SimpleVariable pressure;
	protected SimpleVariable area;
	protected SimpleVariable alpha;
	protected SimpleVariable elastance;
	protected SimpleVariable initialArea;
	protected Hemisphere hemisphere;
	protected ArrayList<ElasticTube> parents;
	protected ArrayList<ElasticTube> children;
	
	public ElasticTube(){
		super();
		hemisphere = Hemisphere.UNKNOWN;
		setFlowin(-1.0f);
		setFlowout(-1.0f);
		setPressure(-1.0f);
		setArea(-1.0f);
		setAlpha(-1.0f);
		setInitialArea(-1.0f);
		parents = new ArrayList<ElasticTube>();
		children = new ArrayList<ElasticTube>();
		String prefix = "";
		if(hemisphere == Hemisphere.LEFT)
			prefix = "L_";
		else
			if(hemisphere == Hemisphere.RIGHT)
				prefix = "R_";
		setName(name + " [" + prefix+TUBE_LABEL+getTubeNum()+"_"+ID+"]");
	}
	
	public ElasticTube(String name, Hemisphere hemi, float len, float a, float alf, float elast, float flowin, float flowout, float pressure){
		super(name, len);
		setHemisphere(hemi);
		setInitialArea(a);
		setArea(a);
		setAlpha(alf);
		setElastance(elast);
		setFlowin(flowin);
		setFlowout(flowout);
		setPressure(pressure);
		parents = new ArrayList<ElasticTube>();
		children = new ArrayList<ElasticTube>();
		String prefix = "";
		if(hemisphere == Hemisphere.LEFT)
			prefix = "L_";
		else
			if(hemisphere == Hemisphere.RIGHT)
				prefix = "R_";
		setName(name + " [" + prefix+TUBE_LABEL+getTubeNum()+"_"+ID+"]");
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
		setChildren(child);
		String prefix = "";
		if(hemisphere == Hemisphere.LEFT)
			prefix = "L_";
		else
			if(hemisphere == Hemisphere.RIGHT)
				prefix = "R_";
		setName(name + " [" + prefix+TUBE_LABEL+getTubeNum()+"_"+ID+"]");
	}

	protected SimpleVariable getLength() {
		return length;
	}
	protected void setLength(float length) {
		//this.length = new SimpleVariable(TUBE_LABEL+getTubeNum()+"_"+LENGTH_LABEL,length, (Tube)this);
		String prefix = "";
		if(hemisphere == Hemisphere.LEFT)
			prefix = "L_";
		else
			if(hemisphere == Hemisphere.RIGHT)
				prefix = "R_";
		SimpleVariable v = new SimpleVariable(prefix+TUBE_LABEL+getTubeNum()+"_"+LENGTH_LABEL+"_"+ID,length, (Tube)this);
		this.length = v;
	}
	
	protected ArrayList<ElasticTube> getParents() {
		return parents;
	}

	protected void setParents(ArrayList<ElasticTube> parents) {
		this.parents = parents;
	}

	protected ArrayList<ElasticTube> getChildren() {
		return children;
	}

	protected void setChildren(ArrayList<ElasticTube> childrens) {
		this.children = childrens;
	}

	protected SimpleVariable getElastance() {
		return elastance;
	}

	protected void setElastance(float elastance) {
		String prefix = "";
		if(hemisphere == Hemisphere.LEFT)
			prefix = "L_";
		else
			if(hemisphere == Hemisphere.RIGHT)
				prefix = "R_";
		SimpleVariable v = new SimpleVariable(prefix+TUBE_LABEL+getTubeNum()+"_"+ELASTANCE_LABEL+"_"+ID,elastance, (Tube)this);
		this.elastance = v;
		/*SimpleVariable v = new SimpleVariable("T"+getTubeNum()+"_"+ELASTANCE_LABEL+"_"+ID,elastance, (Tube)this);
		this.elastance = v;*/
	}	
	protected SimpleVariable getArea() {
		return area;
	}
	protected void setArea(float area){
		String prefix = "";
		if(hemisphere == Hemisphere.LEFT)
			prefix = "L_";
		else
			if(hemisphere == Hemisphere.RIGHT)
				prefix = "R_";
		SimpleVariable v = new SimpleVariable(prefix+TUBE_LABEL+getTubeNum()+"_"+AREA_LABEL+"_"+ID,area, (Tube)this);
		this.area = v;
	}

	protected SimpleVariable getAlpha() {
		return alpha;
	}

	protected void setAlpha(float alpha) {
		String prefix = "";
		if(hemisphere == Hemisphere.LEFT)
			prefix = "L_";
		else
			if(hemisphere == Hemisphere.RIGHT)
				prefix = "R_";
		SimpleVariable v = new SimpleVariable(prefix+TUBE_LABEL+getTubeNum()+"_"+ALPHA_LABEL+"_"+ID,alpha, (Tube)this);
		//SimpleVariable v = new SimpleVariable(TUBE_LABEL+getTubeNum()+"_"+ALPHA_LABEL+"_"+ID,alpha, (Tube)this);
		this.alpha = v;
	}
	public SimpleVariable getInitialArea() {
		return initialArea;
	}

	public void setInitialArea(float iArea) {
		String prefix = "";
		if(hemisphere == Hemisphere.LEFT)
			prefix = "L_";
		else
			if(hemisphere == Hemisphere.RIGHT)
				prefix = "R_";
		SimpleVariable v = new SimpleVariable(prefix+TUBE_LABEL+getTubeNum()+"_"+INITIAL_AREA_LABEL+"_"+ID,iArea, (Tube)this);
		//SimpleVariable v = new SimpleVariable(TUBE_LABEL+getTubeNum()+"_"+INITIAL_AREA_LABEL+"_"+ID,iArea, (Tube)this);
		this.initialArea = v;
	}

	public SimpleVariable getFlowin() {
		return flowin;
	}

	public void setFlowin(float flowin){
		String prefix = "";
		if(hemisphere == Hemisphere.LEFT)
			prefix = "L_";
		else
			if(hemisphere == Hemisphere.RIGHT)
				prefix = "R_";
		SimpleVariable v = new SimpleVariable(prefix+TUBE_LABEL+getTubeNum()+"_"+FLOWIN_LABEL+"_"+ID,flowin, (Tube)this);
		this.flowin = v;
	}

	public SimpleVariable getFlowout() {
		return flowout;
	}

	public void setFlowout(float flowout){
		String prefix = "";
		if(hemisphere == Hemisphere.LEFT)
			prefix = "L_";
		else
			if(hemisphere == Hemisphere.RIGHT)
				prefix = "R_";
		SimpleVariable v = new SimpleVariable(prefix+TUBE_LABEL+getTubeNum()+"_"+FLOWOUT_LABEL+"_"+ID,flowout, (Tube)this);
		this.flowout = v;
	}

	public SimpleVariable getPressure() {
		return pressure;
	}

	public void setPressure(float pressure){
		String prefix = "";
		if(hemisphere == Hemisphere.LEFT)
			prefix = "L_";
		else
			if(hemisphere == Hemisphere.RIGHT)
				prefix = "R_";
		SimpleVariable v = new SimpleVariable(prefix+TUBE_LABEL+getTubeNum()+"_"+PRESSURE_LABEL+"_"+ID,pressure, (Tube)this);
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
		if(!parent.getChildren().contains(this))
			parent.addChild(this);
		return parents.add(parent);
	}
	public boolean addChild(ElasticTube child){
		return children.add(child);
	}
	
	
	public ArrayList<SimpleVariable> getVariables(){
		ArrayList<SimpleVariable> variables = new ArrayList<SimpleVariable>();
		variables.add(getFlowin());
		variables.add(getFlowout());
		variables.add(getPressure());
		variables.add(getArea());
		return variables;
	}
	public ArrayList<SimpleVariable> getFixedVariables(){
		ArrayList<SimpleVariable> variables = new ArrayList<SimpleVariable>();
		variables.add(getInitialArea());
		variables.add(getLength());
		variables.add(getAlpha());
		variables.add(getElastance());
		return variables;
	}
	
	public ArrayList<SimpleVariable> getGlobalVariables(){
		ArrayList<SimpleVariable> variables = new ArrayList<SimpleVariable>();
		SimpleVariable temp = new SimpleVariable(getArea().getName()+LAST_ROUND_SUFFIX,getArea().getValue(),getArea().getSourceObj());
		variables.add(temp);
		temp = new SimpleVariable(getFlowin().getName()+LAST_ROUND_SUFFIX,getFlowin().getValue(),getFlowin().getSourceObj());
		variables.add(temp);
		temp = new SimpleVariable(getFlowout().getName()+LAST_ROUND_SUFFIX,getFlowout().getValue(),getFlowout().getSourceObj());
		variables.add(temp);
		return variables;
	}
	
	public ArrayList<SimpleVariable> getRecursiveVariables(){
		ArrayList<SimpleVariable> variables = new ArrayList<SimpleVariable>();
		for(SimpleVariable var:getVariables()){
			if(!variables.contains(var))
				variables.add(var);
		}
		if(getChildren().size() != 0){
			for(ElasticTube child : getChildren()){
				for(SimpleVariable var:child.getRecursiveVariables()){
					if(!variables.contains(var))
						variables.add(var);
				}
			}
		}
		return variables;
	}
	
	@Override
	public boolean equals(Object v) {
		if (v instanceof ElasticTube){
			return getMyID() == ((ElasticTube)v).getMyID();
		}
		return false;
	}
	
	
	
}
