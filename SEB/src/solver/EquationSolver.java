package solver;

import java.util.ArrayList;

import models.Architecture;
import models.Tube;
import models.Variable;

import Jama.Matrix;

public class EquationSolver {
    public static final double EPSILON = 1e-4;

    // Newton's method to find x* such that f(x*) = 0, starting at x
    public static Matrix root(Architecture archi, ArrayList<Variable> variables) {
    	Matrix x = null;
    	int c=0;
        while (true) {
        	System.out.println(c++);
        	ArrayList<Matrix> mats = archi.eval(variables);
            Matrix f = mats.get(0);
            Matrix J = mats.get(1);//f.jacobian(x);
            x = mats.get(2);
           /* for (int i = 0; i < J.getRowDimension(); i++) {
                for (int j = 0; j < J.getColumnDimension(); j++) 
                    System.out.printf("%9.4f ", J.get(i,j));
                System.out.println();
            }*/
            Matrix delta = J.inverse().times(f);
            x = x.minus(delta);
            updateVariablesFromMatrix(variables,x);
            if (delta.norm1() < EPSILON) break;
        }
        
        return x;
    }

    /**
     * On met a jours l'arraylist des variables a partir d'une matrix
     * @param variables
     * @param x
     * @return
     */
	private static void updateVariablesFromMatrix(
			ArrayList<Variable> variables, Matrix x) {
		int m = x.getRowDimension();
        for(int i = 0; i < m; i++){
        	variables.get(i).setValue((float) x.get(i, 0));
        }
	}
}