package models;

import java.util.ArrayList;

public class SimpleVariable extends Variable {
	public static String DEFAULT_VALUE = ""+123456789.0f;
	private String value;
	private float[] valueInTime;
	
	public SimpleVariable(String name, String value, Tube obj) {
		super(name,obj);
		setValue(value);
	}
	public SimpleVariable(String name, float value, Tube obj) {
		super(name,obj);
		setValue(""+value);
	}	
	
	public SimpleVariable(String name, Tube obj) {
		super(name,obj);
		setValue(DEFAULT_VALUE);
	}
	
	public SimpleVariable(String name) {
		super(name,null);
		setValue(DEFAULT_VALUE);
	}

	public String getValue() {
		return value;
	}
	public float getFloatValue() {
		return Float.parseFloat(value);
	}

	public void setValue(float value) {
		this.value = ""+value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	@Override
	public String toString(){
		String src = "";
		if(sourceObj != null)
			src = sourceObj.getClass().getName();
		return name + " || " + getValue() + " || " + src;
	}
	
	@Override
	public boolean hasValue() {
		return getValue()!=DEFAULT_VALUE;
	}

	/**
	 * Remplit dans le tube le followup de la variable au cours du temps
	 * @param list
	 */
	public void setVariableInTime(ArrayList<Double> list) {
		valueInTime = new float[list.size()];
		int count = 0;
		for(double v:list)
			valueInTime[count++] = (float)v;
	}

	public float[] getValueInTime() {
		return valueInTime;
	}

	public void setValueInTime(float[] valueInTime) {
		this.valueInTime = valueInTime;
	}
}
