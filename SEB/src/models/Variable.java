package models;


/**
 * Classe definissant une variable avec un nom et une valeur
 * @author DEVERDUN Jeremy
 *
 */
public class Variable {
	public static float DEFAULT_VALUE = 123456789.0f;
	private String name;
	private float value;
	private Tube sourceObj;

	public Variable(String name, float value, Tube obj) {
		setName(name);
		setValue(value);
		setSourceObj(obj);
	}

	public Variable(String name) {
		setName(name);
		setValue(DEFAULT_VALUE);
		setSourceObj(null);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public float getValue() {
		return value;
	}

	public void setValue(float value) {
		this.value = value;
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
		return name + " || " + getValue() + " || " + sourceObj.getClass().getName();
	}

	@Override
	public boolean equals(Object v) {
		if (v instanceof Variable){
			return name.equals(((Variable)v).getName());
		}
		return false;
	}

	public boolean hasValue() {
		return getValue()!=DEFAULT_VALUE;
	}

}
