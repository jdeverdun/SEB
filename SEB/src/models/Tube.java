package models;

import java.util.ArrayList;

public abstract class Tube {
	protected String name;
	protected Variable length;
	protected static String TUBE_LABEL = "T";
	protected static int ID = 0;
	
	public Tube(){
		ID++;
		setName("Unknown");
		setLength(-1.0f);
	}
	
	public Tube(String name,float len){
		setName(name);
		setLength(len);
	}
	
	protected abstract Variable getLength();
	protected abstract void setLength(float length);
		
	protected String getName() {
		return name;
	}

	protected void setName(String name) {
		this.name = name;
	}

	public abstract String getTubeNum();
	
	// ----- Methodes ----
	public String toString(){
		return "Name = "+getName()+" - Length = "+getLength();
	}
	
}
