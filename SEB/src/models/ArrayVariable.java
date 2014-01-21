package models;

public class ArrayVariable extends Variable {
	public static float[] DEFAULT_VALUE = null;
	private float[] value;
	
	public ArrayVariable(String name, float[] value, Tube obj) {
		super(name,obj);
		setValue(value);
	}		
	
	public ArrayVariable(String name, Tube obj) {
		super(name,obj);
		setValue(DEFAULT_VALUE);
	}
	
	public float[] getValue() {
		return value;
	}

	public void setValue(float[] value) {
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
