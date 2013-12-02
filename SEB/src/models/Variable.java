package models;


/**
 * Classe definissant une variable avec un nom et une valeur
 * @author DEVERDUN Jeremy
 *
 */
public class Variable {
	private String name;
	private float value;

	public Variable(String name, float value) {
		setName(name);
		setValue(value);
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
	@Override
	public boolean equals(Object v) {
		if (v instanceof Variable){
			return name.equals(((Variable)v).getName());
		}
		return false;
	}

}
