package models;


/**
 * Classe definissant une variable avec un nom et une valeur
 * @author DEVERDUN Jeremy
 *
 */
public abstract class Variable {
	protected String name;
	protected Tube sourceObj;

	public Variable(String name, Tube obj) {
		setName(name);
		setSourceObj(obj);
	}

	public Variable(String name) {
		setName(name);
		setSourceObj(null);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	/**
	 * @return the sourceObj
	 */
	public Tube getSourceObj() {
		return sourceObj;
	}

	/**
	 * @param sourceObj the sourceObj to set
	 */
	public void setSourceObj(Tube sourceObj) {
		this.sourceObj = sourceObj;
	}
	
	public String toString(){
		return name + " || " + sourceObj.getClass().getName();
	}

	@Override
	public boolean equals(Object v) {
		if (v instanceof Variable){
			return name.equals(((Variable)v).getName());
		}
		return false;
	}
	
	public abstract boolean hasValue();

}
