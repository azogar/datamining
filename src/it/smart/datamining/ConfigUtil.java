package it.smart.datamining;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVSaver;

public class ConfigUtil {
    public static void writeArff(Instances instances, String pathName) throws IOException {
	ArffSaver saver = new ArffSaver();
	saver.setInstances(instances);
	saver.setFile(new File(pathName));
	saver.writeBatch();
    }
    
    public static void writeCSV(Instances instances, String pathName) throws IOException {
	CSVSaver saver = new CSVSaver();
	saver.setInstances(instances);
	saver.setFile(new File(pathName));
	saver.writeBatch();
    }
    
    public static Map<String, String[]> getOptionValues(String separator, String pathName) {
	Map<String, String[]> values = new HashMap<String, String[]>();
	try {
	    BufferedReader reader = new BufferedReader(new FileReader(new File(pathName)));
	    String line = new String();
	    
	    Pattern pattern = Pattern.compile("(\\w*)\\s*=\\s*(.*)");
	    Matcher matcher;
	    
	    while((line = reader.readLine()) != null) {
		matcher = pattern.matcher(line);
		if(matcher.matches()) {
		    String[] rawValues = matcher.group(2).split(separator);
		    for (int i = 0; i < rawValues.length; i++) {
			rawValues[i] = rawValues[i].trim(); 
		    }
		    values.put(matcher.group(1), rawValues);
		}
	    }
	    reader.close();
	    
	} catch (Exception e) {
	    e.printStackTrace();
	}
	
	return values;
    }
}
