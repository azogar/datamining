package it.smart.datamining;

import java.util.Map;

import weka.clusterers.ClusterEvaluation;
import weka.clusterers.HierarchicalClusterer;
import weka.clusterers.SimpleKMeans;
import weka.core.CosineDistance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;

public class WekaCluster {
    
    private static String CONFIG_FILE_NAME = "config-cluster.txt";

    public static void main(String[] args) {
	try {
	    Map<String, String[]> options = ConfigUtil.getOptionValues(",", CONFIG_FILE_NAME);
	    final int numClusters = Integer.parseInt(options.get("numClusters")[0]);
	    final String inputFile = options.get("input")[0];
	    final String[] ignoredAttributes = options.get("ignoredAttributes");

	    // Read input dataset
	    Instances data = DataSource.read(inputFile);

	    // Filter to remove unwanted attributes from the dataset
	    Remove filter = new Remove();

	    // String representing the list of indexes of attributes to ignore
	    StringBuilder removedIdx = new StringBuilder();
	    for (int i = 0; i < ignoredAttributes.length; i++) {
		removedIdx.append(data.attribute(ignoredAttributes[i].trim()).index() + 1);
		removedIdx.append(",");
	    }
	    removedIdx.setLength(removedIdx.length() - 1); // Remove last comma

	    // Application of the Remove filter
	    filter.setAttributeIndices(removedIdx.toString());
	    filter.setInputFormat(data);
	    data = Filter.useFilter(data, filter);

	    // The distance function used is a Cosine Distance
	    CosineDistance distance = new CosineDistance();
	    
	    // --- First Clusterer used: Simple K Means ---
	    SimpleKMeans clustererSimpleKMeans = new SimpleKMeans();

	    // Setting clusterer's options
	    clustererSimpleKMeans.setNumClusters(numClusters);
	    clustererSimpleKMeans.setDistanceFunction(distance);

	    clustererSimpleKMeans.buildClusterer(data);

	    ClusterEvaluation evalSimpleKMeans = new ClusterEvaluation();
	    evalSimpleKMeans.setClusterer(clustererSimpleKMeans);
	    evalSimpleKMeans.evaluateClusterer(data);

	    System.out.println(evalSimpleKMeans.clusterResultsToString());

	    System.out.println("\n#################################\n");

	    // --- Second Clusterer used: Hierarchical Clusterer ---
	    HierarchicalClusterer clustererHierarchical = new HierarchicalClusterer();

	    // Setting clusterer's options
	    clustererHierarchical.setNumClusters(numClusters);
	    clustererHierarchical.setDistanceFunction(distance);

	    clustererHierarchical.buildClusterer(data);

	    ClusterEvaluation evalHierarchical = new ClusterEvaluation();
	    evalHierarchical.setClusterer(clustererHierarchical);
	    evalHierarchical.evaluateClusterer(data);

	    System.out.println(evalHierarchical.clusterResultsToString());

	} catch (Exception e) {
	    e.printStackTrace();
	}
    }
}
