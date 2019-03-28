import java.util.ArrayList;
import java.util.Collections;
import org.jfree.ui.RefineryUtilities;
import java.lang.Math;

public class Main {

	public static void main(String[] args) {
		
		double n = 13.0;

		//equidistance points
		 ArrayList<Double> xi = equidistancePoints(-5.0, 5.0, n);
		 
		//chebyshev points
		ArrayList<Double> chebyshevPoints = chebyshevPoints(-5.0, 5.0, n);
		
		//mappingFunction takes x values and finds corresponding y values using the function 1/(1+x^2).
		ArrayList<Double> yi = mappingFunction(xi);
		ArrayList<Double> chebyYi = mappingFunction(chebyshevPoints);
		
		//equidistance points used for divided difference equation
		ArrayList<Double> pxi = new ArrayList<Double>();
		ArrayList<Double> xInputsInterpolation = equidistancePoints(-5.0, 5.0, 1000);
		for(double x : xInputsInterpolation) {
			pxi.add(InterpDivDiff(xi,yi,x));
		}
		
		//graph f(x), p(x), chebyshev
		final XYSeriesDemo demo = new XYSeriesDemo("Graph", xi, yi,xInputsInterpolation, pxi, chebyshevPoints, chebyYi);
		demo.pack();
	    RefineryUtilities.positionFrameOnScreen(demo, 0.5, 0.5);
	    demo.setVisible(true);
	}
			
	//function take in two boundary points and returns n equidistant points between the boundaries.
	public static ArrayList<Double> equidistancePoints(double lowerBound, double upperBound, double n) {
		ArrayList<Double> xi = new ArrayList<Double>();
		for(double i = 0.0; i < n; i++) {
			double value = lowerBound + (i*((upperBound - lowerBound)/(n-1)));
			xi.add(value);
		}
		return xi;
	}
	
	//function take in two boundary points and returns n Chebyshev points between the boundaries.
	public static ArrayList<Double> chebyshevPoints(double a, double b, double n) {
		n--;
		
		ArrayList<Double> chebyshevPoints = new ArrayList<Double>();
		for(double i = 0.0; i < n + 1; i++) {
	    	double chebyNode = (0.5*(a+b) + 0.5*(b-a)*Math.cos(((2.0*i + 1.0)/(2.0*(n+1.0)) * Math.PI)));	
	    	chebyshevPoints.add(chebyNode);
	    }
	    
	    Collections.sort(chebyshevPoints);
	    return chebyshevPoints;
	}
	
	public static ArrayList<Double> mappingFunction(ArrayList<Double> xi) {
		ArrayList<Double> yi = new ArrayList<Double>();
		for(double x : xi) {
			double fx = 1 / (1 + Math.pow(x,2));
			yi.add(fx);
		}
		return yi;
	}
	

	public static double InterpDivDiff(ArrayList<Double> xi, ArrayList<Double> yi, double x) {
		int n = yi.size();
		ArrayList<ArrayList<Double>> table = createTable(n, yi);
		
		int rowCounter = 1, colsCounter = 0;
		recursiveHelper(rowCounter, colsCounter, n, table, xi);
		
		return createPolynomial(n, table, xi, x);
	}
	
	//helper function creates table and adds yi values to collumn zero
	public static ArrayList<ArrayList<Double>> createTable(int n, ArrayList<Double> yi){
		ArrayList<ArrayList<Double>> table = new ArrayList<ArrayList<Double>>();
		for(int i = 0; i < n; i++) {
			ArrayList<Double> row = new ArrayList<Double>();
			row.add(yi.get(i));
			table.add(row);
		}
		return table;
	}
	
	//fills in differences table
	public static void recursiveHelper(int rowCounter, int colsCounter, int n, ArrayList<ArrayList<Double>> table, ArrayList<Double> xi) {
		if(rowCounter != n) {
			for(int i = rowCounter; i < n; i++) {
				double value = (table.get(i).get(colsCounter) - table.get(i-1).get(colsCounter))/(xi.get(i) - xi.get(i-1-colsCounter));
				table.get(i).add(value);
			}

			rowCounter++;
			colsCounter++;
			//recursive call
			recursiveHelper(rowCounter, colsCounter, n, table, xi);
		}
	}
	
	
	public static double createPolynomial(int n, ArrayList<ArrayList<Double>>table, ArrayList<Double> xi, Double x) {
		Double px = 0.0;
		//helper function to get all factors with x
		ArrayList<Double> factorsWithX = xHelper(n, x, xi);
		for (int i = 0; i < n; i++) {
			px += table.get(i).get(i) * factorsWithX.get(i);
		}
		return px;
	}
	
	public static ArrayList<Double> xHelper(int n, Double x, ArrayList<Double> xi){
		ArrayList<Double> xFactors = new ArrayList<Double>();
		xFactors.add(1.0);
		for (int i = 1; i < n; i++) {
			xFactors.add(x - xi.get(i-1));
		}
		
		ArrayList<Double> finalFactors = new ArrayList<Double>();
		finalFactors.add(1.0);
		for(int i = 1; i < n; i++) {
			if(i == 1)
				finalFactors.add(xFactors.get(i));
			else {
				double value = xFactors.get(i);
				int z = i;
				while(z > 1) {
					z--;
					value *= xFactors.get(z);
					if (z == 1) 
						finalFactors.add(value);
				}
			}
		}
		return finalFactors;	
	}

}//end of class
