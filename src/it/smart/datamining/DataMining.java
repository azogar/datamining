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
	
	public static Grid createGrid(float width, float height, String filename_in, String filename_out, DateFormat format) {
		Grid grid = new Grid(width, height);
		
		try {	
			String [] filenames = {filename_in, filename_out};
			DateFormat originalformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
			
			
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
	//	DateFormat format = new SimpleDateFormat("EEE", Locale.ENGLISH);
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
		
		Grid grid = createGrid(0.001f, 0.001f, "data/pechino_starttime.csv", "data/pechino_endtime.csv", format);
			
		//grid.removeCellsLess(2);
		
		try {
			grid.saveArffFile("data/weka3.arff");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println(String.valueOf(grid));
		System.out.println(grid.getCells().size());
	}
	
}
