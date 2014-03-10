package models;

import java.util.ArrayList;

public abstract class Tube {
	protected String name;
	protected SimpleVariable length;
	protected static String TUBE_LABEL = "T";
	public static int ID = 0;
	public static String ID_LABEL = "GLOBALTUBEID";
	public static final String LAST_ROUND_SUFFIX = "_PREV"; // suffixe a rajouter dans symbolic quand variable issue du tour precedent
	public int myID = -1;
	

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
	
	protected abstract SimpleVariable getLength();
	protected abstract void setLength(float length);
	public abstract ArrayList<SimpleVariable> getVariables();	
	public abstract ArrayList<SimpleVariable> getFixedVariables();	// variables type length ou A0
	public abstract ArrayList<SimpleVariable> getGlobalVariables();	// variables type length ou A0
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public int getMyID(){
		return myID;
	}
	public void setMyID(int myID) {
		this.myID = myID;
	}

	public abstract String getTubeNum();
	
	// ---------    EQUATIONS ---------------
	public abstract ArrayList<String> getSymbolicEquations(ArrayList<SimpleVariable> variables) throws Exception;
	public abstract ArrayList<String> getSymbolicInitialEquations(ArrayList<SimpleVariable> variables) throws Exception;
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
	public SimpleVariable findVariableWithName(String name,ArrayList<SimpleVariable> variables) throws Exception{
		for(SimpleVariable lv : variables){
			if(lv.getName().equals(name)){
				return lv;
			}
		}
		throw new Exception("Variable "+name+" missing for momentum derivative in "+TUBE_LABEL+getTubeNum());
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
	public ArrayList<SimpleVariable> findVariableWithStruct(Hemisphere hemisphere,
			String tube_num, String param_label,
			ArrayList<SimpleVariable> variables) {
		ArrayList<SimpleVariable> results = new ArrayList<SimpleVariable>();
		String code = buildNameFromStruct(hemisphere, tube_num, param_label);
		for(SimpleVariable lv : variables){
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
