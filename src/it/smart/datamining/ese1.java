package it.smart.datamining;

import java.io.File;

import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffSaver;


public class ese1 {
	
	private static int nAttribute = 3;
	private static int nIstances = 5;
	
	/**
	 * 
	 * http://weka.wikispaces.com/Creating+an+ARFF+file
	 * 
	 */
	public static void main(String [] args) throws Exception {

		
		FastVector att = new FastVector();
	    for (int i = 0; i < nAttribute; i++)
	    	att.addElement(new Attribute("attribute"+i));
	    Instances data = new Instances("DataSet", att, 0);
	     
	    for (int i = 0; i < nIstances; i++) {
	    	double [] values = new double[data.numAttributes()];
	    	for (int j = 0; j < data.numAttributes(); j++)
	    		values[j] = Math.random();
	    
	    	data.add(new Instance(1.0, values));
	    }
	    
	    System.out.println(data);
	    
	    ArffSaver saver = new ArffSaver();
	    saver.setInstances(data);
	    saver.setFile(new File("test.arff"));
	    saver.writeBatch();
	}
	
}
