package it.smart.datamining;

import java.util.Vector;

/**
 * Classe per gestire la griglia.
 * 
 * @author Luca Pardini
 *
 */
public class Grid {
	private int height;
	private int weight;
	private Vector<Cell> cells;
	
	public static Grid create(long minLatitude, long maxLatitude, long minLongitude, long maxLongitude, int height, int weight) {
		Grid grid = new Grid(height, weight);
		
		for (long longitude = minLongitude; longitude < maxLongitude; longitude += height)
			for (long latitude = minLatitude; latitude < maxLatitude; latitude += weight)
				grid.getCells().add(new Cell(new Point(latitude, longitude)));

		return grid;
	}
	
	public Grid() {
		this(0, 0);
	}
	
	public Grid(int height, int weight) {
		this(height, weight, new Vector<Cell>());
	}
	
	public Grid(int height, int weight, Vector<Cell> cells) {
		super();
		this.height = height;
		this.weight = weight;
		this.cells = cells;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public Vector<Cell> getCells() {
		return cells;
	}

	public void setCells(Vector<Cell> cells) {
		this.cells = cells;
	}
	
	public Cell getCell(long latitude, long longitude) {
		for (Cell cell : this.cells) {
			if ((latitude >= cell.getPoint().getLatitude()) && (latitude < cell.getPoint().getLatitude() + this.weight) && (longitude >= cell.getPoint().getLongitude()) && (longitude < cell.getPoint().getLongitude() + this.height))
				return cell;
		}
		return null;
	}
}
