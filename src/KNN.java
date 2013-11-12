import java.util.*;
import java.lang.Math;
/**
 * A kNN classification algorithm implementation.
 * 
 */

public class KNN {

	/**
	 * In this method, you should implement the kNN algorithm. You can add 
	 * other methods in this class, or create a new class to facilitate your
	 * work. If you create other classes, DO NOT FORGET to include those java
     * files when preparing your code for hand in.
     *
	 * Also, Please DO NOT MODIFY the parameters or return values of this method,
     * or any other provided code.  Again, create your own methods or classes as
     * you need them.
	 * 
	 * @param trainingData
	 * 		An Item array of training data
	 * @param testData
	 * 		An Item array of test data
	 * @param k
	 * 		The number of neighbors to use for classification
	 * @return
	 * 		The object KNNResult contains classification accuracy, 
	 * 		category assignment, etc.
	 */
	public KNNResult classify(Item[] trainingData, Item[] testData, int k) {
		KNNResult res = new KNNResult();
		String[] categoryAssignment =  new String[testData.length];
		String[][] nearestNeighbors = new String[testData.length][k];

		/*
		double[] basedis = testData[0].features;
		for (int j=0; j < trainingData.length; j++) {
			double d = distance(basedis, trainingData[j].features);
			System.out.println(trainingData[j].name + " " + d);
		}
		*/

	    // for each test item in testData
		for (int i=0; i < testData.length; i++) {
			ArrayList<Item> kNN = new ArrayList<Item>();
			ArrayList<Double> closest_distance = new ArrayList<Double>();

			// find kNN in trainingData
	    	for (int j=0; j < trainingData.length; j++) {
	      		Double distance = distance(testData[i].features, trainingData[j].features);
	      		if (closest_distance.size() == k) {
	      			double large = closest_distance.get(0);
	      			int large_ptr = 0;
	      			for (int d=1; d < closest_distance.size(); d++) {
	      				if (large < closest_distance.get(d)) {
	      					large = closest_distance.get(d);
	      					large_ptr = d;
	      				}
	      			}
      				if (distance < large) {
      					closest_distance.remove(large_ptr);
      					closest_distance.add(large_ptr, distance);
      					kNN.remove(large_ptr);
      					kNN.add(large_ptr, trainingData[j]);
      				}
	    		}
	    		else {
	    			closest_distance.add(distance);
	    			kNN.add(trainingData[j]);
	    		}
	    	}

	    	// get predicted category, save in KNNResult.categoryAssignment
	    	String category = getCategory(kNN);
	    	categoryAssignment[i] = category;
	    	res.categoryAssignment = categoryAssignment;

	    	// save kNN in KNNResult.nearestNeighbors
	    	for (int n=0; n < k; n++) {
	    		nearestNeighbors[i][n] = kNN.get(n).name;
	    	}
	    	res.nearestNeighbors = nearestNeighbors;

	    	//System.out.println(testData[i].name + " " + res.categoryAssignment[i]);
	    }
	    // calculate accuracy
	    int num_test = testData.length;
	    double correct = 0.0;
	    double accuracy = 0;
	    for (int x=0; x < num_test; x++) {
	    	if (testData[x].category.equals(res.categoryAssignment[x]))
	    		correct++;
	    }
	    accuracy = correct/num_test;
	    //System.out.println(accuracy);
	    res.accuracy = accuracy;

	    return res;
	}

	/* Finds the euclidian distance betwen two data points
	 * 
	 * @param a
	 *		The frist array of real valued features
	 * @param b
	 *		The second array of real valued features
	 * @return
	 *		The euclidian distance between the two data points
	*/
	public double distance(double[] a, double[] b) {
		double x = Math.pow(b[0] - a[0], 2);
		double y = Math.pow(b[1] - a[1], 2);
		double z = Math.pow(b[2] - a[2], 2);
		return Math.sqrt(x + y + z);
	}

	/* Finds the predicted catagory of an item using its k-nearest-neighbors
	 * 
	 * @param kNN
	 *		An array of k-nearest-neighbors
	 * @return
	 *		The predicted catagory of this item
	*/
	public String getCategory(ArrayList<Item> kNN) {
		// order of category_count: nation|machine|fruit
		int[] category_count = new int[3];
		int high = -1;
		int ptr = -1;
		for (Item item : kNN) {
			if (item.category.equals("nation")) category_count[0]++;
			else if (item.category.equals("machine")) category_count[1]++;
			else category_count[2]++;
		}
		for (int t=0; t < 3; t++) {
			if (category_count[t] > high) {
				high = category_count[t];
				ptr = t;
			}
		}
		switch (ptr) {
			case 0: return "nation";
			case 1: return "machine";
			case 2: return "fruit";
			default: return null;
		}
	}
}
