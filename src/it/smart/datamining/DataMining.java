package it.smart.datamining;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import weka.clusterers.ClusterEvaluation;
import weka.clusterers.HierarchicalClusterer;
import weka.clusterers.SimpleKMeans;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;


/**
 * Classe principale del progetto.
 * 
 * @author Luca Pardini
 *
 */
public class DataMining {
	
	public static Grid createGrid(float height, float width, String filename_in, String filename_out) {
		float minLatitude = Float.MAX_VALUE;
		float maxLatitude = Float.MIN_VALUE;
		float minLongitude = Float.MAX_VALUE;
		float maxLongitude = Float.MIN_VALUE;
		
		try {	
			String [] filenames = {filename_in, filename_out};
			
			for (String filename : filenames) {			
				BufferedReader reader = new BufferedReader(new FileReader(filename));
				String line = reader.readLine();
				while ((line = reader.readLine()) != null) {
					String [] attributes = line.split(",");
					float latitude = Float.parseFloat(attributes[1]);
					float longitude = Float.parseFloat(attributes[2]);
					
					minLatitude = Math.min(minLatitude, latitude);
					maxLatitude = Math.max(maxLatitude, latitude);
					minLongitude = Math.min(minLongitude, longitude);
					maxLongitude = Math.max(maxLongitude, longitude);	
					
					// System.out.println(latitude + " " + longitude);
				}
				reader.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("MIN lat " + minLatitude);
		System.out.println("MIN lon " + minLongitude);
		System.out.println("MAX lat " + maxLatitude);
		System.out.println("MAX lon " + maxLongitude);
		
		Grid grid = Grid.create(minLatitude, maxLatitude, minLongitude, maxLongitude, height, width);

		grid = setInOut(grid, filename_in, filename_out);

		return grid;
	}
	
	public static Grid setInOut(Grid grid, String filename_in, String filename_out) {
		
		try {	
			String [] filenames = {filename_in, filename_out};
			DateFormat originalformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
			DateFormat format = new SimpleDateFormat("EEE", Locale.ENGLISH);
			
			
			for (String filename : filenames) {
				boolean in = (filename.compareTo(filename_in) == 0);
				BufferedReader reader = new BufferedReader(new FileReader(filename));
				String line = reader.readLine();

				while ((line = reader.readLine()) != null) {
					String [] attributes = line.split(",");
					float latitude = Float.parseFloat(attributes[1]);
					float longitude = Float.parseFloat(attributes[2]);
				
					try {
						Date date = originalformat.parse(attributes[4] + " 00:00:00");
		
						if (in)
							grid.getCell(latitude, longitude).incrIn(format.format(date));
						else
							grid.getCell(latitude, longitude).incrOut(format.format(date));
									
					} catch (ParseException e) {
						e.printStackTrace();
					}					
				}
				reader.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	
		System.out.println(grid.getCells().size());
		
		grid.removeEmptyCells();
		
		return grid;
	}
	
	
	public static void main(String [] args) {
		Grid grid = createGrid(0.001f, 0.001f, "data/pechino_starttime_filtered.csv", "data/pechino_endtime_filtered.csv");
		grid.addGrid(createGrid(0.001f, 0.001f, "data/pechino_starttime_filtered_2.csv", "data/pechino_endtime_filtered_2.csv"));
		grid.addGrid(createGrid(0.001f, 0.001f, "data/pechino_starttime_filtered_3.csv", "data/pechino_endtime_filtered_3.csv"));
		grid.addGrid(createGrid(0.001f, 0.001f, "data/pechino_starttime_filtered_4.csv", "data/pechino_endtime_filtered_4.csv"));
			
		grid.removeCellsLess(2);
		
		try {
			grid.saveArffFile("data/weka2.arff");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println(String.valueOf(grid));
		System.out.println(grid.getCells().size());
	}
	
}
