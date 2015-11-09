package models;

import java.util.ArrayList;

import params.ModelSpecification;
import params.WindowManager;

public abstract class ElasticTube extends Tube {
	public static String AREA_LABEL = "A";
	public static String INITIAL_AREA_LABEL = "A0";
	public static String PRESSURE_LABEL = "P";
	public static String FLOWIN_LABEL = "fi";
	public static String FLOWOUT_LABEL = "fo";
	public static String LENGTH_LABEL = "l0";
	public static String ALPHA_LABEL = "alfa";
	public static String ELASTANCE_LABEL = "E";
	public static String PRESSURE_INIT_LABEL = "INITP";
	public static String PRESSURE_OUT_LABEL = "OUTP";
	protected SimpleVariable flowin;
	protected SimpleVariable flowout;
	protected SimpleVariable pressure;
	protected SimpleVariable area;
	protected SimpleVariable alpha;
	protected SimpleVariable elastance;
	protected SimpleVariable initialArea;
	protected SimpleVariable P_INIT;
	protected Hemisphere hemisphere;
	protected ArrayList<ElasticTube> parents;
	protected ArrayList<ElasticTube> children;
	protected boolean connectivityAdded = false;
	protected boolean initialConnectivityAdded = false;

	protected boolean checkedForAlphaRecalculation = false;

	public ElasticTube(){
		super();
		hemisphere = Hemisphere.UNKNOWN;
		checkedForAlphaRecalculation = false;
		setFlowin(-1.0f);
		setFlowout(-1.0f);
		setPressure(-1.0f);
		setArea(-1.0f);
		setP_INIT(-1.0f);
		setInitialArea(-1.0f);
		//setP_OUT(2.0f);
		setP_INIT(50.0f);
		parents = new ArrayList<ElasticTube>();
		children = new ArrayList<ElasticTube>();
		String prefix = "";
		if(hemisphere == Hemisphere.LEFT)
			prefix = "L_";
		else
			if(hemisphere == Hemisphere.RIGHT)
				prefix = "R_";
		setName(name + " [" + prefix+TUBE_LABEL+getTubeNum()+"_"+getMyID()+"]");
		setAlpha(-1.0f);
	}

	public ElasticTube(String name, Hemisphere hemi, float len, float a, float alf, float elast, float flowin, float flowout, float pressure){
		super(name, len);
		setHemisphere(hemi);
		setInitialArea(a);
		setArea(a);
		//setP_OUT(2.0f);
		setP_INIT(-1.0f);
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
		setName(name + " [" + prefix+TUBE_LABEL+getTubeNum()+"_"+getMyID()+"]");
		setAlpha(alf);
	}	

	public ElasticTube(String name, Hemisphere hemi, float len, float a, float alf, float elast, float flowin, float flowout, float pressure, ArrayList<ElasticTube> par, ArrayList<ElasticTube> child){
		super(name,len);
		setHemisphere(hemi);
		setArea(a);
		//setP_OUT(2.0f);
		setP_INIT(-1.0f);
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
		setName(name + " [" + prefix+TUBE_LABEL+getTubeNum()+"_"+getMyID()+"]");
		setAlpha(alf);
	}

	public SimpleVariable getLength() {
		return length;
	}
	public void setLength(float length) {
		//this.length = new SimpleVariable(TUBE_LABEL+getTubeNum()+"_"+LENGTH_LABEL,length, (Tube)this);
		String prefix = "";
		/*if(hemisphere == Hemisphere.LEFT)
			prefix = "L_";
		else
			if(hemisphere == Hemisphere.RIGHT)
				prefix = "R_";*/
		SimpleVariable v = new SimpleVariable(prefix+TUBE_LABEL+getTubeNum()+"_"+LENGTH_LABEL+"_"+getMyID(),length, (Tube)this);
		this.length = v;
	}

	public ArrayList<ElasticTube> getParents() {
		return parents;
	}

	public void setParents(ArrayList<ElasticTube> parents) {
		this.parents = parents;
	}

	public ArrayList<ElasticTube> getChildren() {
		return children;
	}

	public void setChildren(ArrayList<ElasticTube> childrens) {
		this.children = childrens;
	}

	public SimpleVariable getElastance() {
		return elastance;
	}

	public void setElastance(float elastance) {
		String prefix = "";
		if(hemisphere == Hemisphere.LEFT)
			prefix = "L_";
		else
			if(hemisphere == Hemisphere.RIGHT)
				prefix = "R_";
		SimpleVariable v = new SimpleVariable(prefix+TUBE_LABEL+getTubeNum()+"_"+ELASTANCE_LABEL+"_"+getMyID(),elastance, (Tube)this);
		this.elastance = v;
		/*SimpleVariable v = new SimpleVariable("T"+getTubeNum()+"_"+ELASTANCE_LABEL+"_"+getMyID(),elastance, (Tube)this);
		this.elastance = v;*/
	}	
	public SimpleVariable getArea() {
		return area;
	}
	public void setArea(float area){
		String prefix = "";
		if(hemisphere == Hemisphere.LEFT)
			prefix = "L_";
		else
			if(hemisphere == Hemisphere.RIGHT)
				prefix = "R_";
		SimpleVariable v = new SimpleVariable(prefix+TUBE_LABEL+getTubeNum()+"_"+AREA_LABEL+"_"+getMyID(),area, (Tube)this);
		this.area = v;
	}

	public SimpleVariable getAlpha() {
		return alpha;
	}

	public void setAlpha(float alpha) {
		String prefix = "";
		if(hemisphere == Hemisphere.LEFT)
			prefix = "L_";
		else
			if(hemisphere == Hemisphere.RIGHT)
				prefix = "R_"; 
		SimpleVariable v = new SimpleVariable(prefix+TUBE_LABEL+getTubeNum()+"_"+ALPHA_LABEL+"_"+getMyID(),alpha, (Tube)this);
		// on recalcule le alpha ON NE RECALCULE PAS LE ALPHA ON SAIT LA VALEUR
		// il faut mettre en m !
		if(getTubeNum() != SpinalCord.TUBE_NUM && getTubeNum() != Arteriole.TUBE_NUM && getTubeNum() != Capillary.TUBE_NUM && getTubeNum() != Veinule.TUBE_NUM)
			v.setValue("8*pi*0.004*((1e-6)/133.32)*("+getLength().getName()+"/100)/(("+getArea().getName()+"_PREV/10000)^2)");
		//SimpleVariable v = new SimpleVariable(TUBE_LABEL+getTubeNum()+"_"+ALPHA_LABEL+"_"+getMyID(),alpha, (Tube)this);


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
		SimpleVariable v = new SimpleVariable(prefix+TUBE_LABEL+getTubeNum()+"_"+INITIAL_AREA_LABEL+"_"+getMyID(),iArea, (Tube)this);
		//SimpleVariable v = new SimpleVariable(TUBE_LABEL+getTubeNum()+"_"+INITIAL_AREA_LABEL+"_"+getMyID(),iArea, (Tube)this);
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
		SimpleVariable v = new SimpleVariable(prefix+TUBE_LABEL+getTubeNum()+"_"+FLOWIN_LABEL+"_"+getMyID(),flowin, (Tube)this);
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
		SimpleVariable v = new SimpleVariable(prefix+TUBE_LABEL+getTubeNum()+"_"+FLOWOUT_LABEL+"_"+getMyID(),flowout, (Tube)this);
		this.flowout = v;
	}


	public SimpleVariable getP_INIT() {
		return P_INIT;
	}

	public void setP_INIT(float p_INIT) {
		String prefix = "";
		if(hemisphere == Hemisphere.LEFT)
			prefix = "L_";
		else
			if(hemisphere == Hemisphere.RIGHT)
				prefix = "R_";
		if(getTubeNum().equals(FirstArtery.TUBE_NUM)){
			p_INIT = 120.0f;
		}
		SimpleVariable v = new SimpleVariable(prefix+TUBE_LABEL+getTubeNum()+"_"+PRESSURE_INIT_LABEL+"_"+getMyID(),p_INIT, (Tube)this);
		this.P_INIT = v;
	}

	/*public SimpleVariable getP_OUT() {
		return P_OUT;
	}*/

	/*public void setP_OUT(float p_OUT) {
		String prefix = "";
		if(hemisphere == Hemisphere.LEFT)
			prefix = "L_";
		else
			if(hemisphere == Hemisphere.RIGHT)
				prefix = "R_";
		SimpleVariable v = new SimpleVariable(prefix+TUBE_LABEL+getTubeNum()+"_"+PRESSURE_OUT_LABEL+"_"+getMyID(),p_OUT, (Tube)this);
		this.P_OUT = v;
	}*/

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
		SimpleVariable v = new SimpleVariable(prefix+TUBE_LABEL+getTubeNum()+"_"+PRESSURE_LABEL+"_"+getMyID(),pressure, (Tube)this);
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

		variables.add(getPressure());
		variables.add(getArea());

		if(getP_INIT().getFloatValue()==120.0f){
			variables.add(getP_INIT());
		}else{
			variables.add(getFlowin());
		}
		/*if(this instanceof VenousSinus && this.getChildren().size()<=0)
			variables.add(getP_OUT());
		else*/
			variables.add(getFlowout());
		return variables;
	}
	public ArrayList<SimpleVariable> getFixedVariables(){
		ArrayList<SimpleVariable> variables = new ArrayList<SimpleVariable>();
		variables.add(getInitialArea());
		variables.add(getLength());
		variables.add(getElastance());
		variables.add(getAlpha());
		return variables;
	}

	public ArrayList<SimpleVariable> getGlobalVariables(){
		ArrayList<SimpleVariable> variables = new ArrayList<SimpleVariable>();
		SimpleVariable temp = new SimpleVariable(getArea().getName()+LAST_ROUND_SUFFIX,getArea().getValue(),getArea().getSourceObj());
		variables.add(temp);



		if(getP_INIT().getFloatValue()==120.0f){
			temp = new SimpleVariable(getP_INIT().getName()+LAST_ROUND_SUFFIX,getP_INIT().getValue(),getP_INIT().getSourceObj());
			variables.add(temp);
		}else{
			temp = new SimpleVariable(getFlowin().getName()+LAST_ROUND_SUFFIX,getFlowin().getValue(),getFlowin().getSourceObj());
			variables.add(temp);
		}
		/*if(this instanceof VenousSinus && this.getChildren().size()<=0){
			temp = new SimpleVariable(getP_OUT().getName()+LAST_ROUND_SUFFIX,getP_OUT().getValue(),getP_OUT().getSourceObj());
			variables.add(temp);
		}else{*/
			temp = new SimpleVariable(getFlowout().getName()+LAST_ROUND_SUFFIX,getFlowout().getValue(),getFlowout().getSourceObj());
			variables.add(temp);
		//}


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

	// equations communes
	protected String getSymbolicInitialMomentumEquation(ArrayList<ElasticTube> parents, ArrayList<SimpleVariable> variables) throws Exception {
		// eq (31)  (36)
		if(parents.size()>1 || (parents.size()==1 && parents.get(0).getChildren().size()>1)){
			String left = "(";
			String usedvar = "@@";
			String prefix = "";
			SimpleVariable pressure = findVariableWithName(getPressure().getName(),variables);
			ArrayList<SimpleVariable> alphalist = new ArrayList<SimpleVariable>();
			ArrayList<ElasticTube> tubeslist = new ArrayList<ElasticTube>();
			for(ElasticTube el:parents){
				alphalist.add(el.getAlpha());
				tubeslist.add(el);
				for(ElasticTube el2 : el.getChildren())
					if(!el2.equals(this) && !tubeslist.contains(el2)){
						alphalist.add(el2.getAlpha());
						tubeslist.add(el2);
					}
			}
			for(ElasticTube el:tubeslist){
				String localpha = "";
				for(SimpleVariable alp:alphalist){
					if(!alp.getName().equals(el.getAlpha().getName()) && !usedvar.contains(alp.getName())){
						localpha = alp.getName();
						usedvar += localpha + "@@";
						break;
					}
				}
				if(!left.equals("("))
					prefix = " + ";
				left += prefix + "(" + localpha + "/2) * (" + pressure.getName() + " - " + el.getPressure().getName() + ")" ;
			}
			left += ")";
			// on gere la partie droite avec les alpha
			String right = "(";
			prefix = "";
			usedvar = "@@";
			alphalist.add(getAlpha());
			for(SimpleVariable a1 : alphalist){
				for(SimpleVariable a2 : alphalist){
					if(!right.equals("("))
						prefix = " + ";
					if(!a1.getName().equals(a2.getName()) && !usedvar.contains(a1.getName()+a2.getName())){
						right += prefix + "(" +a1.getName() + "/2) *" + "("+a2.getName()+"/2)";
						usedvar+=a1.getName()+a2.getName()+"@@"+a2.getName()+a1.getName()+"@@";
					}
				}
			}
			right += ") * " + getFlowin().getName();
			return left + " + " + right;
		}else{
			//return "(" + parents.get(0).getPressure().getName() + " - " + getPressure().getName() + ") + ("+getAlpha().getName()+ ") * "+getFlowin().getName();
			return "(" + getPressure().getName() + " - " + parents.get(0).getPressure().getName() + ") + (("+getAlpha().getName()+ "/2) + (" + parents.get(0).getAlpha().getName()+ "/2)) * "+getFlowin().getName();
		}
	}

	protected String getSymbolicInitialMomentumEquationOut(ArrayList<ElasticTube> children, ArrayList<SimpleVariable> variables) throws Exception {
		// eq (31)  (36)
		// PROBLEME QUAND NOMBRE IMPAIR
		if(children.size()>1 || (children.size()==1 && children.get(0).getParents().size()>1)){
			String left = "(";
			String usedvar = "@@";
			String prefix = "";
			SimpleVariable pressure = findVariableWithName(getPressure().getName(),variables);
			ArrayList<SimpleVariable> alphalist = new ArrayList<SimpleVariable>();
			ArrayList<ElasticTube> tubeslist = new ArrayList<ElasticTube>();
			for(ElasticTube el:children){
				alphalist.add(el.getAlpha());
				tubeslist.add(el);
				for(ElasticTube el2 : el.getParents())
					if(!el2.equals(this) && !tubeslist.contains(el2)){
						alphalist.add(el2.getAlpha());
						tubeslist.add(el2);
					}
			}
			for(ElasticTube el:tubeslist){
				String localpha = "";
				for(SimpleVariable alp:alphalist){
					if(!alp.getName().equals(el.getAlpha().getName()) && !usedvar.contains(alp.getName())){
						localpha = alp.getName();

						usedvar += localpha + "@@";
						break;
					}
				}
				// DEBUG
				/*if(localpha.equals("")){
					System.out.println(el);
					System.exit(0);
				}*/
				// fin DEBUG
				if(!left.equals("("))
					prefix = " + ";
				left += prefix + "(" + localpha + "/2) * (" + pressure.getName() + " - " + el.getPressure().getName() + ")" ;
			}
			left += ")";
			// on gere la partie droite avec les alpha
			String right = "(";
			prefix = "";
			usedvar = "@@";
			alphalist.add(getAlpha());
			for(SimpleVariable a1 : alphalist){
				for(SimpleVariable a2 : alphalist){
					if(!right.equals("("))
						prefix = " + ";
					if(!a1.getName().equals(a2.getName()) && !usedvar.contains(a1.getName()+a2.getName())){
						right += prefix + "("+ a1.getName() + "/2) * (" + a2.getName()+"/2)";
						usedvar+=a1.getName()+a2.getName()+"@@"+a2.getName()+a1.getName()+"@@";
					}
				}
			}
			right += ") * " + getFlowout().getName();
			return left + " - " + right;
		}else{
			//return "" + getFlowout().getName() + " - " + children.get(0).getFlowin().getName();
			return "(" + getPressure().getName() + " - " + children.get(0).getPressure().getName() + ") - (("+getAlpha().getName()+ "/2) + (" + children.get(0).getAlpha().getName()+ "/2)) * "+getFlowout().getName();
		}
	}



	@Override
	public boolean equals(Object v) {

		if (v instanceof ElasticTube){
			return ((ElasticTube)v).getName().equals(getName());//getMyID() == ((ElasticTube)v).getMyID() & 

		}
		return false;
	}

	public boolean removeParent(ElasticTube tube) {
		return getParents().remove(tube);
	}

	public boolean removeChild(ElasticTube tube) {
		return getChildren().remove(tube);
	}

	/**
	 * @return the connectivityAdded
	 */
	public boolean isConnectivityAdded() {
		return connectivityAdded;
	}

	/**
	 * @param connectivityAdded the connectivityAdded to set
	 */
	public void setConnectivityAdded(boolean connectivityAdded) {
		this.connectivityAdded = connectivityAdded;
	}

	public boolean isInitialConnectivityAdded() {
		return initialConnectivityAdded;
	}

	public void setInitialConnectivityAdded(boolean initialConnectivityAdded) {
		this.initialConnectivityAdded = initialConnectivityAdded;
	}

	public boolean isCheckedForAlphaRecalculation() {
		return checkedForAlphaRecalculation;
	}

	public void setCheckedForAlphaRecalculation(boolean checkedForAlphaRecalculation) {
		this.checkedForAlphaRecalculation = checkedForAlphaRecalculation;
	}




}
