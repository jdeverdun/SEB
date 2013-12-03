package models;

import java.util.ArrayList;

public abstract class Tube {
	protected String name;
	protected Variable length;
	protected static String TUBE_LABEL = "T";
	protected static int ID = 0;
	protected int myID = -1;
	
	public Tube(){
		ID++;
		myID = ID;
		setName("Unknown");
		setLength(-1.0f);
	}
	
	public Tube(String name,float len){
		ID++;
		myID = ID;
		setName(name);
		setLength(len);
	}
	
	protected abstract Variable getLength();
	protected abstract void setLength(float length);
	public abstract ArrayList<Variable> getVariables();	
	protected String getName() {
		return name;
	}

	protected void setName(String name) {
		this.name = name;
	}
	public int getMyID(){
		return myID;
	}
	public abstract String getTubeNum();
	
	// ---------    EQUATIONS ---------------
	public abstract ArrayList<float[]> getEquations(ArrayList<Variable> variables) throws Exception;
	public abstract ArrayList<String[]> getSymbolicEquations(ArrayList<Variable> variables) throws Exception;
	// ----- Methodes ----
	public String toString(){
		return "Name = "+getName()+" - Length = "+getLength();
	}
	
	/**
	 * Recherche dans une arraylist la variable qui a le meme nom que le nom donne en argument
	 * @param name
	 * @param variables
	 * @return
	 * @throws Exception 
	 */
	public Variable findVariableWithName(String name,ArrayList<Variable> variables) throws Exception{
		for(Variable lv : variables){
			if(lv.getName().equals(name)){
				return lv;
			}
		}
		throw new Exception("Variable Area missing for momentum derivative in "+TUBE_LABEL+getTubeNum());
	}
	
	/**
	 * Renvoi les variables dans variables qui ont le
	 * code standard pour un tube décrit en argument ex : "R_T2_fo"
	 * @param hemisphere
	 * @param tube_num
	 * @param fLOWIN_LAparam_labelBEL
	 * @param variables
	 * @return
	 */
	public ArrayList<Variable> findVariableWithStruct(Hemisphere hemisphere,
			String tube_num, String param_label,
			ArrayList<Variable> variables) {
		ArrayList<Variable> results = new ArrayList<Variable>();
		String code = buildNameFromStruct(hemisphere, tube_num, param_label);
		for(Variable lv : variables){
			if(lv.getName().startsWith(code)){
				results.add(lv);
			}
		}
		return results;
	}
	public String buildNameFromStruct(Hemisphere hemisphere,
			String tube_num, String param_label) {
		String prefix = "";
		if(hemisphere == Hemisphere.LEFT)
			prefix = "L_";
		else
			if(hemisphere == Hemisphere.RIGHT)
				prefix = "R_";
		String code = prefix+TUBE_LABEL+tube_num+"_"+param_label;
		return code;
	}
}
