package models;

import java.util.ArrayList;

public abstract class CSF extends Tube {
	protected ArrayList<CSF> parents;
	protected ArrayList<CSF> childrens;
	
	public CSF(){
		super();
		parents = new ArrayList<CSF>();
		childrens = new ArrayList<CSF>();
	}
	
	public CSF(String name, float len, float a, float alf, float elast){
		super(name, len,a,alf);
		parents = new ArrayList<CSF>();
		childrens = new ArrayList<CSF>();
	}	
	
	public CSF(String name, float len, float a, float alf, ArrayList<CSF> par, ArrayList<CSF> child){
		super(name,len,a,alf);
		setParents(par);
		setChildrens(child);
	}

	protected ArrayList<CSF> getParents() {
		return parents;
	}

	protected void setParents(ArrayList<CSF> parents) {
		this.parents = parents;
	}

	protected ArrayList<CSF> getChildrens() {
		return childrens;
	}

	protected void setChildrens(ArrayList<CSF> childrens) {
		this.childrens = childrens;
	}

	
	public String toString(){
		return super.toString();
	}
	
}
