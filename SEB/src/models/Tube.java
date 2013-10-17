package models;

import java.util.ArrayList;

public abstract class Tube {
	protected String name;
	protected float length;
	
	public Tube(){
		setName("Unknown");
		setLength(-1.0f);
	}
	
	public Tube(String name,float len){
		setName(name);
		setLength(len);
	}
	
	protected float getLength() {
		return length;
	}
	protected void setLength(float length) {
		this.length = length;
	}
		
	protected String getName() {
		return name;
	}

	protected void setName(String name) {
		this.name = name;
	}

	// ----- Methodes ----
	public String toString(){
		return "Name = "+getName()+" - Length = "+getLength();
	}
	
}
