package weka.core;

import weka.core.TechnicalInformation.Field;
import weka.core.TechnicalInformation.Type;
import weka.core.neighboursearch.PerformanceStats;

public class CosineDistance
  extends EuclideanDistance {

    /** for serialization. */
    private static final long serialVersionUID = 5861235595466795518L;
  

  /**
   * Constructs an Cosine Distance object, Instances must be still set.
   */
  public CosineDistance() {
    super();
  }

  /**
   * Constructs an Cosine Distance object and automatically initializes the
   * ranges.
   * 
   * @param data 	the instances the distance function should work on
   */
  public CosineDistance(Instances data) {
    super(data);
  }

  public String globalInfo() {
    return "";
  }

  /**
   * Returns an instance of a TechnicalInformation object, containing 
   * detailed information about the technical background of this class,
   * e.g., paper reference or book this class is based on.
   * 
   * @return 		the technical information about this class
   */
  public TechnicalInformation getTechnicalInformation() {
    TechnicalInformation 	result;
    
    result = new TechnicalInformation(Type.MISC);
    result.setValue(Field.AUTHOR, "Wolfram");
    result.setValue(Field.TITLE, "Cosine distance");
    result.setValue(Field.URL, "https://reference.wolfram.com/mathematica/ref/CosineDistance.html");

    return result;
  }
  
  /**
   * Calculates the distance between two instances.
   * 
   * @param first 	the first instance
   * @param second 	the second instance
   * @return 		the distance between the two given instances
   */
  public double distance(Instance first, Instance second) {
    return distance(first, second, Double.POSITIVE_INFINITY, null);
  }
  
  /**
   * Calculates the distance (or similarity) between two instances. Need to
   * pass this returned distance later on to postprocess method to set it on
   * correct scale. <br/>
   * P.S.: Please don't mix the use of this function with
   * distance(Instance first, Instance second), as that already does post
   * processing. Please consider passing Double.POSITIVE_INFINITY as the cutOffValue to
   * this function and then later on do the post processing on all the
   * distances.
   *
   * @param first 	the first instance
   * @param second 	the second instance
   * @param stats 	the structure for storing performance statistics.
   * @return 		the distance between the two given instances or 
   * 			Double.POSITIVE_INFINITY.
   */
  public double distance(Instance first, Instance second, PerformanceStats stats) {
    return distance(first, second, Double.POSITIVE_INFINITY, stats);
  }
  
  public double distance(Instance first, Instance second, double cutOffValue, PerformanceStats stats) {
	double distance = 0;
	int firstI, secondI;
	int firstNumValues = first.numValues();
	int secondNumValues = second.numValues();
	int numAttributes = m_Data.numAttributes();
	int classIndex = m_Data.classIndex();
	double normFirst = 0d, normSecond = 0d;

	validate();

	for (int p1 = 0, p2 = 0; p1 < firstNumValues || p2 < secondNumValues;) {
	    if (p1 >= firstNumValues)
		firstI = numAttributes;
	    else
		firstI = first.index(p1);

	    if (p2 >= secondNumValues)
		secondI = numAttributes;
	    else
		secondI = second.index(p2);

	    if (firstI == classIndex) {
		p1++;
		continue;
	    }
	    if ((firstI < numAttributes) && !m_ActiveIndices[firstI]) {
		p1++;
		continue;
	    }

	    if (secondI == classIndex) {
		p2++;
		continue;
	    }
	    if ((secondI < numAttributes) && !m_ActiveIndices[secondI]) {
		p2++;
		continue;
	    }

	    double diff;

	    if (firstI == secondI) {
		diff = difference(firstI, first.valueSparse(p1),
			second.valueSparse(p2)); // = first.valueSparse(p1) * second.valueSparse(p2)
		normFirst += Math.pow(first.valueSparse(p1), 2);
		normSecond += Math.pow(second.valueSparse(p2), 2);
		p1++;
		p2++;
	    } else if (firstI > secondI) {
		diff = difference(secondI, 0, second.valueSparse(p2)); // = 0
		normSecond += Math.pow(second.valueSparse(p2), 2);
		p2++;
	    } else {
		diff = difference(firstI, first.valueSparse(p1), 0); // = 0
		normFirst += Math.pow(first.valueSparse(p1), 2);
		p1++;
	    }
	    if (stats != null)
		stats.incrCoordCount();

	    distance = updateDistance(distance, diff); // sums distance with diff
	    if (distance > cutOffValue)
		return Double.POSITIVE_INFINITY;
	}

	distance = distance / ( Math.sqrt(normFirst)*Math.sqrt(normSecond) );
	distance = 1 - distance;
	if(distance < 0)
	    distance = 0;
	else if(distance > 1)
	    distance = 1;
	return distance;
  }
  
    public double difference(int index, double val1, double val2) {
	switch (m_Data.attribute(index).type()) {
	case Attribute.NOMINAL:
	    return Double.NaN;
	    // break;
	case Attribute.NUMERIC:
	    return val1 * val2;
	    // break;
	}
	return Double.NaN;
    }
  
  /**
   * Updates the current distance calculated so far with the new difference
   * between two attributes. The difference between the attributes was 
   * calculated with the difference(int,double,double) method.
   * 
   * @param currDist	the current distance calculated so far
   * @param diff	the difference between two new attributes
   * @return		the update distance
   * @see		#difference(int, double, double)
   */
  protected double updateDistance(double currDist, double diff) {
    double	result;
    
    result  = currDist;
    result += diff;
    
    return result;
  }
  
  /**
   * Does post processing of the distances (if necessary) returned by
   * distance(distance(Instance first, Instance second, double cutOffValue). It
   * is necessary to do so to get the correct distances if
   * distance(distance(Instance first, Instance second, double cutOffValue) is
   * used. This is because that function actually returns the squared distance
   * to avoid inaccuracies arising from floating point comparison.
   * 
   * @param distances	the distances to post-process
   */
  public void postProcessDistances(double distances[]) {
    for(int i = 0; i < distances.length; i++) {
      distances[i] = Math.sqrt(distances[i]);
    }
  }
  
  /**
   * Returns the squared difference of two values of an attribute.
   * 
   * @param index	the attribute index
   * @param val1	the first value
   * @param val2	the second value
   * @return		the squared difference
   */
  public double sqDifference(int index, double val1, double val2) {
    double val = difference(index, val1, val2);
    return val*val;
  }
  
  /**
   * Returns value in the middle of the two parameter values.
   * 
   * @param ranges 	the ranges to this dimension
   * @return 		the middle value
   */
  public double getMiddle(double[] ranges) {

    double middle = ranges[R_MIN] + ranges[R_WIDTH] * 0.5;
    return middle;
  }
  
  /**
   * Returns the index of the closest point to the current instance.
   * Index is index in Instances object that is the second parameter.
   *
   * @param instance 	the instance to assign a cluster to
   * @param allPoints 	all points
   * @param pointList 	the list of points
   * @return 		the index of the closest point
   * @throws Exception	if something goes wrong
   */
  public int closestPoint(Instance instance, Instances allPoints,
      			  int[] pointList) throws Exception {
    double minDist = Integer.MAX_VALUE;
    int bestPoint = 0;
    for (int i = 0; i < pointList.length; i++) {
      double dist = distance(instance, allPoints.instance(pointList[i]), Double.POSITIVE_INFINITY);
      if (dist < minDist) {
        minDist = dist;
        bestPoint = i;
      }
    }
    return pointList[bestPoint];
  }
  
  /**
   * Returns true if the value of the given dimension is smaller or equal the
   * value to be compared with.
   * 
   * @param instance 	the instance where the value should be taken of
   * @param dim 	the dimension of the value
   * @param value 	the value to compare with
   * @return 		true if value of instance is smaller or equal value
   */
  public boolean valueIsSmallerEqual(Instance instance, int dim,
      				     double value) {  //This stays
    return instance.value(dim) <= value;
  }
  
  /**
   * Returns the revision string.
   * 
   * @return		the revision
   */
  public String getRevision() {
    return RevisionUtils.extract("$Revision: 1.13 $");
  }
}
