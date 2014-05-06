package it.smart.datamining;

import weka.clusterers.ClusterEvaluation;
import weka.clusterers.SimpleKMeans;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;

public class WekaCluster {

    public static void main(String[] args) {
	try {
//		Instances trainingSet = DataSource.read("data/weka2.arff");
//		
//		Remove filter = new Remove();
//		filter.setAttributeIndices(""
//			+ (trainingSet.attribute("cell_id").index() + 1) + ","
//			+ (trainingSet.attribute("latitude").index() + 1) + ","
//			+ (trainingSet.attribute("longitude").index() + 1) + ","
//			+ (trainingSet.attribute("tot-in").index() + 1) + ","
//			+ (trainingSet.attribute("tot-out").index() + 1) + ","
//			+ (trainingSet.attribute("tot").index() + 1));
//		filter.setInputFormat(trainingSet);
//		Instances trainingSetClusterer = Filter.useFilter(trainingSet, filter);
//		
//		SimpleKMeans clusterer = new SimpleKMeans();
//		
//		clusterer.setNumClusters(3);
//		clusterer.setDistanceFunction(new CosineDistance());
//		
//		clusterer.buildClusterer(trainingSetClusterer);
//		
//		ClusterEvaluation eval = new ClusterEvaluation();
//		eval.setClusterer(clusterer);
//		eval.evaluateClusterer(trainingSet);
//		
//		System.out.println(eval.clusterResultsToString());
//		
//		System.out.println("\n#################################\n");
	    
	    	FastVector attInfo = new FastVector();
	    	attInfo.addElement(new Attribute("att1"));
	    	attInfo.addElement(new Attribute("att2"));
	    	attInfo.addElement(new Attribute("att3"));
	    	
	    	Instances instances = new Instances("test", attInfo, 2);
	    	
	    	Instance a = new Instance(3);
	    	a.setValue(0, 1);
	    	a.setValue(1, 2);
	    	a.setValue(2, 3);
	    	instances.add(a);
	    	
	    	Instance b = new Instance(3);
	    	b.setValue(0, 3);
	    	b.setValue(1, 2);
	    	b.setValue(2, 0);
	    	instances.add(b);
	    	
	    	SimpleKMeans clusterer = new SimpleKMeans();
		
		clusterer.setNumClusters(2);
		clusterer.setDistanceFunction(new CosineDistance());
		
		clusterer.buildClusterer(instances);
		
		ClusterEvaluation eval = new ClusterEvaluation();
		eval.setClusterer(clusterer);
		eval.evaluateClusterer(instances);
		
		System.out.println(eval.clusterResultsToString());
	    	
	} catch (Exception e) {
		e.printStackTrace();
	}
    }

}
