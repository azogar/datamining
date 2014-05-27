package it.smart.datamining;

import java.util.Map;

import weka.clusterers.ClusterEvaluation;
import weka.clusterers.HierarchicalClusterer;
import weka.clusterers.SimpleKMeans;
import weka.core.Attribute;
import weka.core.CosineDistance;
import weka.core.EuclideanDistance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;
import weka.filters.unsupervised.attribute.RenameAttribute;

public class WekaCluster {
    
    private static String CONFIG_FILE_NAME = "config-cluster.txt";
    
    public static Instances renameAttribute(Instances instances, String attributeName, String newAttributeName) {
	RenameAttribute filter = null;
	try {
	    filter = new RenameAttribute();
	    filter.setFind(attributeName);
	    filter.setReplace(newAttributeName);
	    filter.setInputFormat(instances);
	    instances = Filter.useFilter(instances, filter);
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return instances;
    }

    public static void main(String[] args) {
	try {
	    Map<String, String[]> options = ConfigUtil.getOptionValues(",", CONFIG_FILE_NAME);
	    final int numClusters = Integer.parseInt(options.get("numClusters")[0]);
	    final String inputFile = options.get("input")[0];
	    final String[] ignoredAttributes = options.get("ignoredAttributes");
	    final String[] latLongAttributes = options.get("latLong");
	    
	    // Read input dataset
	    Instances originalData = DataSource.read(inputFile);
	    Instances data = new Instances(originalData);
	    int numInstances = data.numInstances();

	    // Filter to remove unwanted attributes from the dataset
	    Remove filter = new Remove();

	    // String representing the list of indexes of attributes to ignore
	    StringBuilder removedIdx = new StringBuilder();
	    for (int i = 0; i < ignoredAttributes.length; i++) {
		removedIdx.append(data.attribute(ignoredAttributes[i]).index() + 1);
		removedIdx.append(",");
	    }
	    removedIdx.setLength(removedIdx.length() - 1); // Remove last comma

	    // Application of the Remove filter
	    filter.setAttributeIndices(removedIdx.toString());
	    filter.setInputFormat(data);
	    data = Filter.useFilter(data, filter);

	    // The distance function used is a Cosine Distance
	    CosineDistance cosineDistance = new CosineDistance();
	    EuclideanDistance euclideanDistance = new EuclideanDistance();
	    
	    // --- First Clusterer used: Simple K Means with CosineDistance ---
	    SimpleKMeans cosineSimpleKMeans = new SimpleKMeans();

	    // Setting clusterer's options
	    cosineSimpleKMeans.setNumClusters(numClusters);
	    cosineSimpleKMeans.setDistanceFunction(cosineDistance);

	    cosineSimpleKMeans.buildClusterer(data);
	    
	    // --- Second Clusterer used: Simple K Means with EuclideanDistance ---
	    SimpleKMeans euclideanSimpleKMeans = new SimpleKMeans();

	    // Setting clusterer's options
	    euclideanSimpleKMeans.setNumClusters(numClusters);
	    euclideanSimpleKMeans.setDistanceFunction(euclideanDistance);

	    euclideanSimpleKMeans.buildClusterer(data);
	    
	    // Evaluate SimpleKMeans with the two different distances
	    ClusterEvaluation evalCosineSimpleKMeans = new ClusterEvaluation();
	    evalCosineSimpleKMeans.setClusterer(cosineSimpleKMeans);
	    evalCosineSimpleKMeans.evaluateClusterer(data);
	    System.out.println(evalCosineSimpleKMeans.clusterResultsToString());
	    
	    System.out.println("#################################\n");
	    
	    ClusterEvaluation evalEuclideanSimpleKMeans = new ClusterEvaluation();
	    evalEuclideanSimpleKMeans.setClusterer(euclideanSimpleKMeans);
	    evalEuclideanSimpleKMeans.evaluateClusterer(data);
	    System.out.println(evalEuclideanSimpleKMeans.clusterResultsToString());
	    
	    System.out.println("#################################\n");

	    // --- Third Clusterer used: Hierarchical Clusterer with CosineDistance---
	    HierarchicalClusterer cosineHierarchical = new HierarchicalClusterer();

	    // Setting clusterer's options
	    cosineHierarchical.setNumClusters(numClusters);
	    cosineHierarchical.setDistanceFunction(cosineDistance);

	    cosineHierarchical.buildClusterer(data);
	    
	    // --- Fourth Clusterer used: Hierarchical Clusterer with EuclideanDistance ---
	    HierarchicalClusterer euclideanHierarchical = new HierarchicalClusterer();

	    // Setting clusterer's options
	    euclideanHierarchical.setNumClusters(numClusters);
	    euclideanHierarchical.setDistanceFunction(euclideanDistance);

	    euclideanHierarchical.buildClusterer(data);
	    
	    // Evaluate Hierarchical clusterer with the two different instances
	    ClusterEvaluation evalCosineHierarchical = new ClusterEvaluation();
	    evalCosineHierarchical.setClusterer(cosineHierarchical);
	    evalCosineHierarchical.evaluateClusterer(data);
	    System.out.println(evalCosineHierarchical.clusterResultsToString());
	    
	    System.out.println("#################################\n");
	    
	    ClusterEvaluation evalEuclideanHierarchical = new ClusterEvaluation();
	    evalEuclideanHierarchical.setClusterer(euclideanHierarchical);
	    evalEuclideanHierarchical.evaluateClusterer(data);
	    System.out.println(evalEuclideanHierarchical.clusterResultsToString());
	    
	    System.out.println("#################################\n");
	    
	    // Assign clusters to the instances, for each clusterer
	    // Also, use only a pair (latitude, longitude) to identify the center of the cell
	    double[] cosineSimpleKMeansAssignments = evalCosineSimpleKMeans.getClusterAssignments();
	    double[] euclideanSimpleKMeansAssignments = evalEuclideanSimpleKMeans.getClusterAssignments();
	    double[] cosineHierarchicalAssignments = evalCosineHierarchical.getClusterAssignments();
	    double[] euclideanHierarchicalAssignments = evalEuclideanHierarchical.getClusterAssignments();
	    originalData.insertAttributeAt(new Attribute("cosineSimpleKMeans"), 0);
	    originalData.insertAttributeAt(new Attribute("euclideanSimpleKMeans"), 1);
	    originalData.insertAttributeAt(new Attribute("cosineHierarchical"), 2);
	    originalData.insertAttributeAt(new Attribute("euclideanHierarchical"), 3);
	    
	    Attribute latMin = originalData.attribute(latLongAttributes[0]);
	    Attribute longMin = originalData.attribute(latLongAttributes[1]);
	    Attribute latMax = originalData.attribute(latLongAttributes[2]);
	    Attribute longMax = originalData.attribute(latLongAttributes[3]);
	    
	    for(int i = 0; i < numInstances; i++) {
		// Classes...
		Instance currentInstance = originalData.instance(i);
		currentInstance.setValue(0, cosineSimpleKMeansAssignments[i]);
		currentInstance.setValue(1, euclideanSimpleKMeansAssignments[i]);
		currentInstance.setValue(2, cosineHierarchicalAssignments[i]);
		currentInstance.setValue(3, euclideanHierarchicalAssignments[i]);
		
		// ... coordinates
		currentInstance.setValue(latMin, (currentInstance.value(latMin) + currentInstance.value(latMax))/2);
		currentInstance.setValue(longMin, (currentInstance.value(longMin) + currentInstance.value(longMax))/2);
	    }
	    
	    originalData.renameAttribute(latMin, "latitude");
	    originalData.renameAttribute(longMin, "longitude");
	    originalData.deleteAttributeAt(latMax.index());
	    // Find the new index of the longMax attribute after this deletion
	    longMax = originalData.attribute(latLongAttributes[3]);
	    originalData.deleteAttributeAt(longMax.index());

	    ConfigUtil.writeArff(originalData, options.get("outputArff")[0]);
	    ConfigUtil.writeCSV(originalData, options.get("outputCSV")[0]);
	    
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }
}
