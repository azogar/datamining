package it.smart.datamining;

/**
 * Classe per gestire i punti.
 * 
 * @author Luca Pardini
 *
 */
public class Point {
	private long latitude;
	private long longitude;
	
	public Point() {
		this(0, 0);
	}
	
	public Point(long latitude, long longitude) {
		super();
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public long getLatitude() {
		return latitude;
	}

	public void setLatitude(long latitude) {
		this.latitude = latitude;
	}

	public long getLongitude() {
		return longitude;
	}

	public void setLongitude(long longitude) {
		this.longitude = longitude;
	}
}
