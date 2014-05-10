package it.smart.datamining;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.CSVLoader;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;
import weka.filters.unsupervised.instance.RemoveDuplicates;
import weka.filters.unsupervised.instance.SubsetByExpression;


/**
 * Classe principale del progetto.
 * 
 * @author Luca Pardini
 *
 */
public class DataMining {
    
    private static String CONFIG_FILE_NAME = "config-pre.txt";

    public static Instances[] createInstances(double[] cellSize, int[] indexes, String[] fileNames, String[] headerRegex, DateFormat format) {
	
	Instances[] instances = new Instances[2];
	int latIdx = -1, longIdx = -1, dateIdx = -1, numCSVAttributes = -1;
	
	try {
	    for (String filename : fileNames) {
		CSVLoader csvLoader = new CSVLoader();
		csvLoader.setFile(new File(filename));
		Instances inputStructure = csvLoader.getStructure();

		latIdx = -1;
		longIdx = -1;
		dateIdx = -1;
		numCSVAttributes = inputStructure.numAttributes();
		for (int i = 0; i < numCSVAttributes; i++) {
		    if (inputStructure.attribute(i).name().matches(headerRegex[0])) {
			latIdx = i;
		    	indexes[0] = i;
		    } else if (inputStructure.attribute(i).name().matches(headerRegex[1])) {
			longIdx = i;
		    	indexes[1] = i;
		    } else if (inputStructure.attribute(i).name().matches(headerRegex[2])) {
			dateIdx = i;
		    	indexes[2] = i;
		    }
		}

		if (latIdx == -1 || longIdx == -1 || dateIdx == -1) {
		    System.err.println("Latitude, Longitude or date not found!");
		    System.exit(1);
		}
		
		csvLoader.setDateFormat("yyyy-MM-dd");
		csvLoader.setDateAttributes(String.valueOf(dateIdx));
		
		Instances inputData = csvLoader.getDataSet();

		// Save instances
		if(filename.equals(fileNames[0]))
		    instances[0] = inputData;
		else
		    instances[1] = inputData;
	    }
	    
	} catch (Exception e) {
	    e.printStackTrace();
	}

	return instances;
	}
    
    public static void applyFilters(Instances[] instances, String[] headerRegex, int[] indexesToKeep, Point[] regionCorners) {
	// Duplicates need to be removed before removing not useful attributes
	removeDuplicates(instances);
	removeAttributes(instances, headerRegex, indexesToKeep);
	cropRegion(instances, indexesToKeep, regionCorners);
    }
    
    public static void removeDuplicates(Instances[] instances) {
	try {
	    RemoveDuplicates filter = null;
	    for (int i = 0; i < instances.length; i++) {
		filter = new RemoveDuplicates();
		filter.setInputFormat(instances[i]);
		instances[i] = Filter.useFilter(instances[i], filter);
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }
    
    public static void removeAttributes(Instances[] instances, String[] headerRegex, int[] indexesToKeep) {
	try {
	    Remove filter = null;
	    for (int i = 0; i < instances.length; i++) {
		// Remove unwanted attributes
		filter = new Remove();
		filter.setInvertSelection(true);
		filter.setAttributeIndicesArray(indexesToKeep);
		filter.setInputFormat(instances[i]);
		instances[i] = Filter.useFilter(instances[i], filter);
	    }
	    
	    // Get the new indexes [lat, long, date] after the removal. Assume two datasets with same attribute order
	    for (int j = 0; j < indexesToKeep.length; j++) {
		if (instances[0].attribute(j).name().matches(headerRegex[0])) indexesToKeep[0] = j;
		else if (instances[0].attribute(j).name().matches(headerRegex[1])) indexesToKeep[1] = j;
		else if (instances[0].attribute(j).name().matches(headerRegex[2])) indexesToKeep[2] = j;
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }
    
    public static void cropRegion(Instances[] instances, int[] indexes, Point[] corners) {
	try {
	    SubsetByExpression filter = null;
	    int latIdx = indexes[0] + 1;
	    int longIdx = indexes[1] + 1;
	    String filterExpression = new String("(ATT" + latIdx + " >= "
		    + corners[0].getLatitude() + ") and (ATT" + latIdx
		    + " <= " + corners[1].getLatitude() + ") and (ATT"
		    + longIdx + " >= " + corners[0].getLongitude()
		    + ") and (ATT" + longIdx + " <= " + corners[1].getLongitude() + ")");
	    
	    for (int i = 0; i < instances.length; i++) {
		filter = new SubsetByExpression();
		filter.setExpression(filterExpression);
		filter.setInputFormat(instances[i]);
		instances[i] = Filter.useFilter(instances[i], filter);
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }
	
    public static Grid createGrid(double[] cellSize, Instances[] data, int[] indexes, DateFormat dateFormat) {
	Grid grid = new Grid(cellSize[0], cellSize[1]);
	int latIdx = indexes[0], longIdx = indexes[1], dateIdx = indexes[2];
	DateFormat originalDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
	boolean getIn = true;
	
	for(Instances currentData : data) {
	    int numInstances = currentData.numInstances();
	    for (int i = 0; i < numInstances; i++) {
		Instance currentInstance = currentData.instance(i);
		double latitude = currentInstance.value(latIdx);
		double longitude = currentInstance.value(longIdx);

		try {
		    Date date = originalDateFormat.parse(currentInstance.stringValue(dateIdx) + " 00:00:00");

		    if (getIn)
			grid.getCell(latitude, longitude).incrIn(dateFormat.format(date));
		    else
			grid.getCell(latitude, longitude).incrOut(dateFormat.format(date));

		} catch (ParseException e) {
		    e.printStackTrace();
		}
	    }
	    getIn = false;
	}

	return grid;
    }
	
    public static void main(String[] args) {
	Map<String, String[]> options = ConfigUtil.getOptionValues(",", CONFIG_FILE_NAME);
	DateFormat format = new SimpleDateFormat(options.get("timeDiscret")[0], Locale.ENGLISH);
	double[] cellSize = {Double.parseDouble(options.get("cellWidth")[0]), Double.parseDouble(options.get("cellHeight")[0])};
	String[] headerRegex = new String[] {options.get("latitudeHeaderRegex")[0], options.get("longitudeHeaderRegex")[0], options.get("dateHeaderRegex")[0]};
	String[] corners = options.get("regionCorners");
	int[] indexes = new int[3];

	String[] inOutFiles = options.get("inOutFiles");
	if (inOutFiles.length != 2) {
	    System.err.println("Not enough input files!");
	    System.exit(1);
	}

	Instances[] inputData = createInstances(cellSize, indexes, inOutFiles, headerRegex, format);
	
	Point[] regionCorners = new Point[] {
		new Point(Double.parseDouble(corners[0]), Double.parseDouble(corners[1])),
		new Point(Double.parseDouble(corners[2]), Double.parseDouble(corners[3]))
		};
	
	applyFilters(inputData, headerRegex, indexes, regionCorners);
	
	Grid grid = createGrid(cellSize, inputData, indexes, format);
	
	// TODO: normalize

	// grid.removeCellsLess(2);

	try {
	    grid.saveArffFile(options.get("output")[0]);
	} catch (IOException e) {
	    e.printStackTrace();
	}

	System.out.println(String.valueOf(grid));
	System.out.println("Number of cells: " + grid.getCells().size());
    }
	
}
