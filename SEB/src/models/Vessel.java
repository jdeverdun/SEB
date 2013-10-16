package models;

import java.util.ArrayList;

public abstract class Vessel extends Tube {
	protected float elastance;
	protected ArrayList<Vessel> parents;
	protected ArrayList<Vessel> childrens;
	
	public Vessel(){
		super();
		parents = new ArrayList<Vessel>();
		childrens = new ArrayList<Vessel>();
	}
	
	public Vessel(String name, float len, float a, float alf, float elast){
		super(name, len,a,alf);
		setElastance(elast);
		parents = new ArrayList<Vessel>();
		childrens = new ArrayList<Vessel>();
	}	
	
	public Vessel(String name, float len, float a, float alf, float elast, ArrayList<Vessel> par, ArrayList<Vessel> child){
		super(name,len,a,alf);
		setElastance(elast);
		setParents(par);
		setChildrens(child);
	}

	protected ArrayList<Vessel> getParents() {
		return parents;
	}

	protected void setParents(ArrayList<Vessel> parents) {
		this.parents = parents;
	}

	protected ArrayList<Vessel> getChildrens() {
		return childrens;
	}

	protected void setChildrens(ArrayList<Vessel> childrens) {
		this.childrens = childrens;
	}

	protected float getElastance() {
		return elastance;
	}

	protected void setElastance(float elastance) {
		this.elastance = elastance;
	}	
	
	public String toString(){
		return super.toString()+" - Elastance = "+getElastance();
	}
	
}
