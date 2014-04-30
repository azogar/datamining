package it.smart.datamining;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


/**
 * Classe principale del progetto.
 * 
 * @author Luca Pardini
 *
 */
public class DataMining {
	
	public static Grid createGrid(double height, double width, String filename_in, String filename_out) {
		double minLatitude = Double.MAX_VALUE;
		double maxLatitude = Double.MIN_VALUE;
		double minLongitude = Double.MAX_VALUE;
		double maxLongitude = Double.MIN_VALUE;
		
		try {	
			String [] filenames = {filename_in, filename_out};
			
			for (String filename : filenames) {			
				BufferedReader reader = new BufferedReader(new FileReader(filename));
				String line = reader.readLine();
				while ((line = reader.readLine()) != null) {
					String [] attributes = line.split(",");
					double latitude = Double.parseDouble(attributes[1]);
					double longitude = Double.parseDouble(attributes[2]);
					
					minLatitude = Math.min(minLatitude, latitude);
					maxLatitude = Math.max(maxLatitude, latitude);
					minLongitude = Math.min(minLongitude, longitude);
					maxLongitude = Math.max(maxLongitude, longitude);	
					
					System.out.println(latitude + " " + longitude);
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
			
		return grid;
	}
	
	public static Grid setInOut(Grid grid, String filename_in, String filename_out) {
		
		try {	
			String [] filenames = {filename_in, filename_out};
			
			for (String filename : filenames) {
				boolean in = (filename.compareTo(filename_in) == 0);
				BufferedReader reader = new BufferedReader(new FileReader(filename));
				String line = reader.readLine();
				while ((line = reader.readLine()) != null) {
					String [] attributes = line.split(",");
					double latitude = Double.parseDouble(attributes[1]);
					double longitude = Double.parseDouble(attributes[2]);
					
					if (in)
						grid.getCell(latitude, longitude).incrIn();
					else
						grid.getCell(latitude, longitude).incrOut();
					
				}
				reader.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	
		grid.removeEmptyCells();
		
		return grid;
	}
	
	
	public static void main(String [] args) {
		Grid grid = createGrid(0.01, 0.01, "data/pechino_starttime.csv", "data/pechino_endtime.csv");
		grid = setInOut(grid, "data/pechino_starttime.csv", "data/pechino_endtime.csv");
	//	System.out.println(String.valueOf(grid));
	
		try {
			grid.saveArffFile("data/weka.arff");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		System.out.println(String.valueOf(grid));
		System.out.println(grid.getCells().size());
	}
	
}
