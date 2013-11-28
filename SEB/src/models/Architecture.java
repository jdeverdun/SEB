package models;

/**
 * Classe definissant l'architecture du modele (point de depart, fin) etc
 * @author DEVERDUN Jeremy
 *
 */
public class Architecture {

	private Artery startPoint;
	private VenousSinus endPoint;
	private Brain brain;
 // ================= TODO ==================
	public Architecture() {
		// TODO Auto-generated constructor stub
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
	public Artery getStartPoint() {
		return startPoint;
	}
	/**
	 * @param startPoint the startPoint to set
	 */
	public void setStartPoint(Artery startPoint) {
		this.startPoint = startPoint;
	}
	

}
