package models;

import java.util.ArrayList;

import Jama.Matrix;

/**
 * Classe definissant l'architecture du modele (point de depart, fin) etc
 * @author DEVERDUN Jeremy
 *
 */
public class Architecture {

	private FirstArtery startPoint;
	private VenousSinus endPoint;
	private Brain brain;
	private ArrayList<Tube> tubes;
	
	
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
		return (startPoint != null) && (endPoint != null) && (brain != null);
	}
	
	public ArrayList<Matrix> eval(ArrayList<SimpleVariable> variables){
		ArrayList<Matrix> mats = new ArrayList<Matrix>(2);
		try {
			ArrayList<float[]> equations = new ArrayList<float[]>();
			//ArrayList<String[]> ss = new ArrayList<String[]>();
			for(Tube tube : tubes){
				equations.addAll(tube.getInitialEquations(variables));
				//ss.addAll(tube.getSymbolicEquations(variables));
			}
			// on convertie en objet matrix
			Matrix fx = new Matrix(equations.size(),1);
			Matrix jacobian = new Matrix(equations.size(),equations.get(0).length-1);
			Matrix x = new Matrix(variables.size(),1);
	    	for(int i = 0 ; i < equations.size(); i++){
	    		fx.set(i, 0, equations.get(i)[0]);
	    		for(int j = 0 ; j < variables.size(); j++){
	    			jacobian.set(i, j, equations.get(i)[j+1]);
	    			if(i==0)
	    				x.set(j, 0, variables.get(j).getValue());
	    		}
	    	}
	    	/*System.out.println("\n\n\n");
	    	for(int i = 0; i<jacobian.getRowDimension();i++){
				
				for(int j = 0; j<jacobian.getColumnDimension();j++){
					System.out.print(jacobian.get(i, j)+"\t");
				}
				System.out.print("\n");
			}*/
			mats.add(fx);
			mats.add(jacobian);
			mats.add(x);
			return mats;
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
			return null;
		}
	}

}
