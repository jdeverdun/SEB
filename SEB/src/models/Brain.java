package models;

/**
 * Permet de regrouper les hemisphere
 * @author DEVERDUN Jeremy
 *
 */
public class Brain {
	private BrainParenchyma leftHemi;
	private BrainParenchyma rightHemi;
	
	public Brain(BrainParenchyma leftHemi, BrainParenchyma rightHemi){
		setLeftHemi(leftHemi);
		setRightHemi(rightHemi);
	}
	
	public Brain(){
		setLeftHemi(new BrainParenchyma("Left hemisphere",Hemisphere.LEFT));
		setRightHemi(new BrainParenchyma("Right hemisphere", Hemisphere.RIGHT));
	}

	/**
	 * @return the leftHemi
	 */
	public BrainParenchyma getLeftHemi() {
		return leftHemi;
	}

	/**
	 * @param leftHemi the leftHemi to set
	 */
	public void setLeftHemi(BrainParenchyma leftHemi) {
		this.leftHemi = leftHemi;
	}

	/**
	 * @return the rightHemi
	 */
	public BrainParenchyma getRightHemi() {
		return rightHemi;
	}

	/**
	 * @param rightHemi the rightHemi to set
	 */
	public void setRightHemi(BrainParenchyma rightHemi) {
		this.rightHemi = rightHemi;
	}
	
}
