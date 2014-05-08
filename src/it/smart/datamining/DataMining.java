package it.smart.datamining;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


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
				String[] attributeNames = line.split(",");
				
				int latitudeIdx = -1, longitudeIdx = -1, gpstime = -1;
				for (int i = 0; i < attributeNames.length; i++) {
				    if(attributeNames[i].matches("lat|latitude")) latitudeIdx = i;
				    else if(attributeNames[i].matches("long|longitude")) longitudeIdx = i;
				    else if(attributeNames[i].matches("gpsdate")) gpstime = i;
				}
				
				if(latitudeIdx == -1 || longitudeIdx == -1 || gpstime == -1) {
				    System.err.println("Latitude, Longitude or GPS time not found!");
				    System.exit(1);
				}

				while ((line = reader.readLine()) != null) {
					String [] attributes = line.split(",");
					float latitude = Float.parseFloat(attributes[latitudeIdx]);
					float longitude = Float.parseFloat(attributes[longitudeIdx]);
				
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
			grid.saveArffFile("data/weka.arff");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println(String.valueOf(grid));
		System.out.println(grid.getCells().size());
	}
	
}
