package it.smart.datamining;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class DataMining {
	
	public static Grid createGrid(int height, int weight, String filename_in, String filename_out) {
		long minLatitude = Long.MAX_VALUE;
		long maxLatitude = Long.MIN_VALUE;
		long minLongitude = Long.MAX_VALUE;
		long maxLongitude = Long.MIN_VALUE;
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader(filename_in));
			String line;
			while ((line = reader.readLine()) != null) {
				String [] attributes = line.split(",");
				long latitude = Long.parseLong(attributes[1]);
				long longitude = Long.parseLong(attributes[2]);
				
				minLatitude = Math.min(minLatitude, latitude);
				maxLatitude = Math.max(maxLatitude, latitude);
				minLongitude = Math.min(minLongitude, longitude);
				maxLongitude = Math.max(maxLongitude, longitude);		
			}
			reader.close();
			
			reader = new BufferedReader(new FileReader(filename_out));
			while ((line = reader.readLine()) != null) {
				String [] attributes = line.split(",");
				long latitude = Long.parseLong(attributes[1]);
				long longitude = Long.parseLong(attributes[2]);
				
				minLatitude = Math.min(minLatitude, latitude);
				maxLatitude = Math.max(maxLatitude, latitude);
				minLongitude = Math.min(minLongitude, longitude);
				maxLongitude = Math.max(maxLongitude, longitude);		
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Grid grid = Grid.create(minLatitude, maxLatitude, minLongitude, maxLongitude, height, weight);
			
		return grid;
	}
	
	public static void main(String [] args) {
		
	}
	
}
