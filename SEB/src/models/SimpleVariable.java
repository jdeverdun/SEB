package models;

public class SimpleVariable extends Variable {
	public static float DEFAULT_VALUE = 123456789.0f;
	private float value;
	
	public SimpleVariable(String name, float value, Tube obj) {
		super(name,obj);
		setValue(value);
	}		
	
	public SimpleVariable(String name, Tube obj) {
		super(name,obj);
		setValue(DEFAULT_VALUE);
	}
	
	public SimpleVariable(String name) {
		super(name,null);
		setValue(DEFAULT_VALUE);
	}

	public float getValue() {
		return value;
	}

	public void setValue(float value) {
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
}
