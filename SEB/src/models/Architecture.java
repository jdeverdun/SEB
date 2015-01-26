package models;

import java.util.ArrayList;

import Jama.Matrix;

/**
 * Classe definissant l'architecture du modele (point de depart, fin) etc
 * @author DEVERDUN Jeremy
 *
 */
public class Architecture {

	private ArrayList<FirstArtery> startPoints;
	private VenousSinus endPoint;
	private Brain brain;
	private ArrayList<Tube> tubes;
	
	
	public Architecture(ArrayList<FirstArtery> firstArtery, VenousSinus ep, Brain br) {
		setStartPoints(firstArtery);
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
	public ArrayList<FirstArtery> getStartPoints() {
		return startPoints;
	}
	/**
	 * @param startPoint the startPoint to set
	 */
	public void setStartPoints(ArrayList<FirstArtery> startPoint) {
		this.startPoints = startPoint;
	}
	
	/**
	 * @return the tubes
	 */
	public ArrayList<Tube> getTubes() {
		return tubes;
	}
	/**
	 * @param tubes the tubes to set
	 */
	public void setTubes(ArrayList<Tube> tubes) {
		this.tubes = tubes;
	}
	/**
	 * Verifie si tous les champs ont ete rentre
	 * @return
	 */
	public boolean isFilled(){
		return (startPoints != null) && (endPoint != null) && (brain != null);
	}
	
	
	public boolean checkArchitectureValidity(ElasticTube startPoint) {
		if(startPoint instanceof VenousSinus)
			return true;
		if(!(startPoint instanceof FirstArtery) && !checkValidity(startPoint)){
			//System.out.println(startPoint);
			return false;
		}
		for(ElasticTube tube : startPoint.getChildren()){
		//	if(!(startPoint instanceof FirstArtery) && !(tube instanceof VenousSinus) && tube.getHemisphere() != startPoint.getHemisphere())
		//		return false;
			if(startPoint instanceof FirstArtery && !(tube instanceof Artery))
				return false;
			if(startPoint instanceof Artery && !(tube instanceof Arteriole || tube instanceof Artery))
				return false;
			if(startPoint instanceof Arteriole && !(tube instanceof Capillary))
				return false;
			if(startPoint instanceof Capillary && !(tube instanceof Veinule))
				return false;
			if(startPoint instanceof Veinule && !(tube instanceof Vein))
				return false;
			if(startPoint instanceof Vein && !(tube instanceof VenousSinus || tube instanceof Vein))
				return false;
			if(!checkArchitectureValidity(tube))
				return false;
		}
		return true;
	}
	private boolean checkValidity(ElasticTube t1) {
		if((t1.getParents().isEmpty() && t1.getChildren().isEmpty()))
			return false;
		for(ElasticTube parent : t1.getParents()){
			if(iterativeCheckIndirectParentLink(t1, parent,0)){
				//System.out.println("+++\n"+t1+"\n"+parent+"\n------");
				return false;
			}
		}
		for(ElasticTube child : t1.getChildren()){// A VOIR si il y a un soucis
			if(iterativeCheckIndirectChildLink(t1, child,0)){
				System.out.println("+++\n"+t1+"\n"+child+"\n------");
				return false;
			}
		}
		return true;
	}
	
	private boolean iterativeCheckIndirectParentLink(ElasticTube t1, ElasticTube parent,int level) {
		if(parent.equals(t1))
			return true;
		for(ElasticTube locparent : t1.getParents()){
			if(locparent.equals(parent) && level == 0)
				continue;
			if(!locparent.equals(parent)){
				if(iterativeCheckIndirectParentLink(locparent,parent,level+1))
					return true;
				continue;
			}else{
				return true;
			}
		}
		return false;
	}
	
	private boolean iterativeCheckIndirectChildLink(ElasticTube t1, ElasticTube child, int level){
		if(child.equals(t1))
			return true;
		
		for(ElasticTube locchild : t1.getParents()){
			if(locchild.equals(child) && level == 0)
				continue;
			if(!locchild.equals(child)){
				if(iterativeCheckIndirectChildLink(locchild,child,level+1))
					return true;
				continue;
			}else{
				return true;
			}
		}
		return false;
	}
}
