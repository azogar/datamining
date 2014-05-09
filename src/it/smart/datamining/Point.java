package it.smart.datamining;

/**
 * Classe per gestire i punti.
 * 
 * @author Luca Pardini
 *
 */
public class Point {
	private double latitude;
	private double longitude;
	
	public Point() {
		this(0, 0);
	}
	
	public Point(double latitude, double longitude) {
		super();
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	
	@Override
	public String toString() {
		return "Lat: " + this.latitude + " Lon: " + this.longitude;
	}
}
