package models;

/**
 * Classe definissant l'architecture du modele (point de depart, fin) etc
 * @author DEVERDUN Jeremy
 *
 */
public class Architecture {

	private FirstArtery startPoint;
	private VenousSinus endPoint;
	private Brain brain;

	public Architecture(FirstArtery sp, VenousSinus ep, Brain br) {
		setStartPoint(sp);
		setEndPoint(ep);
		setBrain(br);
	}
	/**
	 * @return the brain
	 */
	public Brain getBrain() {
		return brain;
	}
	/**
	 * @param brain the brain to set
	 */
	public void setBrain(Brain brain) {
		this.brain = brain;
	}
	/**
	 * @return the endPoint
	 */
	public VenousSinus getEndPoint() {
		return endPoint;
	}
	/**
	 * @param endPoint the endPoint to set
	 */
	public void setEndPoint(VenousSinus endPoint) {
		this.endPoint = endPoint;
	}
	/**
	 * @return the startPoint
	 */
	public FirstArtery getStartPoint() {
		return startPoint;
	}
	/**
	 * @param startPoint the startPoint to set
	 */
	public void setStartPoint(FirstArtery startPoint) {
		this.startPoint = startPoint;
	}
	
	/**
	 * Verifie si tous les champs ont ete rentre
	 * @return
	 */
	public boolean isFilled(){
		return (startPoint != null) && (endPoint != null) && (brain != null);
	}

}
