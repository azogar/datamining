package it.smart.datamining;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class DataMining {
	
	public static Grid createGrid(int height, int weight, String filename_in, String filename_out) {
		double minLatitude = Double.MAX_VALUE;
		double maxLatitude = Double.MIN_VALUE;
		double minLongitude = Double.MAX_VALUE;
		double maxLongitude = Double.MIN_VALUE;
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader(filename_in));
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
			
			reader = new BufferedReader(new FileReader(filename_out));
			line = reader.readLine();
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
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("MIN lat " + minLatitude);
		System.out.println("MIN lon " + minLongitude);
		System.out.println("MAX lat " + maxLatitude);
		System.out.println("MAX lon " + maxLongitude);
		
		Grid grid = Grid.create(minLatitude, maxLatitude, minLongitude, maxLongitude, height, weight);
			
		return grid;
	}
	
	public static void main(String [] args) {
		Grid grid = createGrid(1, 1, "data/pechino_starttime.csv", "data/pechino_endtime.csv");
	//	System.out.println(String.valueOf(grid));
	}
	
}
