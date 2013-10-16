package models;

import java.util.ArrayList;

public abstract class Tube {
	protected String name;
	protected float length;
	protected float area;
	protected float alpha;
	
	public Tube(){
		setName("Unknown");
		setLength(-1.0f);
		setArea(-1.0f);
		setAlpha(-1.0f);
	}
	
	public Tube(String name,float len, float a, float alf){
		setName(name);
		setLength(len);
		setArea(a);
		setAlpha(alf);
	}
	
	protected float getLength() {
		return length;
	}
	protected void setLength(float length) {
		this.length = length;
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
	
	protected String getName() {
		return name;
	}

	protected void setName(String name) {
		this.name = name;
	}

	// ----- Methodes ----
	public String toString(){
		return "Name = "+getName()+" - Length = "+getLength()+" - Area = "+getArea()+" - Alpha = "+getAlpha();
	}
	
}
