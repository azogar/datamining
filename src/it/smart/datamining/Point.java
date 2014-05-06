package it.smart.datamining;

/**
 * Classe per gestire i punti.
 * 
 * @author Luca Pardini
 *
 */
public class Point {
	private float latitude;
	private float longitude;
	
	public Point() {
		this(0, 0);
	}
	
	public Point(float latitude, float longitude) {
		super();
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}

	public float getLongitude() {
		return longitude;
	}

	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}
	
	@Override
	public String toString() {
		return "Lat: " + this.latitude + " Lon: " + this.longitude;
	}
}
