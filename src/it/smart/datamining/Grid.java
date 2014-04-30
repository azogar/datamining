package it.smart.datamining;

import java.io.File;
import java.io.IOException;
import java.util.Vector;

import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffSaver;

/**
 * Classe per gestire la griglia.
 * 
 * @author Luca Pardini
 *
 */
public class Grid {
	private double height;
	private double width;
	private Vector<Cell> cells;
	
	public static Grid create(double minLatitude, double maxLatitude, double minLongitude, double maxLongitude, double height, double width) {
		Grid grid = new Grid(height, width);
		
		for (double longitude = minLongitude; longitude < maxLongitude; longitude += height)
			for (double latitude = minLatitude; latitude < maxLatitude; latitude += width)
				grid.getCells().add(new Cell(new Point(latitude, longitude)));

		return grid;
	}
	
	public Grid() {
		this(0, 0);
	}
	
	public Grid(double height, double width) {
		this(height, width, new Vector<Cell>());
	}
	
	public Grid(double height, double width, Vector<Cell> cells) {
		super();
		this.height = height;
		this.width = width;
		this.cells = cells;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	public double getwidth() {
		return width;
	}

	public void setwidth(double width) {
		this.width = width;
	}

	public Vector<Cell> getCells() {
		return cells;
	}

	public void setCells(Vector<Cell> cells) {
		this.cells = cells;
	}
	
	@Override
	public String toString() {
		return "Height: " + this.height + "\nwidth: " + this.width + "\nCells:\n" + String.valueOf(this.cells);
	}
	
	public Cell getCell(double latitude, double longitude) {
		for (Cell cell : this.cells) {
			if ((latitude >= cell.getPoint().getLatitude()) && (latitude < cell.getPoint().getLatitude() + this.width) && (longitude >= cell.getPoint().getLongitude()) && (longitude < cell.getPoint().getLongitude() + this.height))
				return cell;
		}
		return null;
	}
	
	public void removeCellsLess(int threshold) {
		int i = 0;
		while (i < this.cells.size()) {
			if (this.cells.get(i).getIn() + this.cells.get(i).getOut() <= threshold) {
				this.cells.remove(i);
			} else {
				i++;
			}
		}
	}
	
	public void removeEmptyCells() {
		this.removeCellsLess(0);
	}
	
	public void saveArffFile(String filename) throws IOException {
		FastVector schema = new FastVector();
		schema.addElement(new Attribute("cell_id"));
		schema.addElement(new Attribute("latitude"));
		schema.addElement(new Attribute("longitude"));
		schema.addElement(new Attribute("in"));
		schema.addElement(new Attribute("out"));
		
	    Instances data = new Instances("DataSet", schema, 0);
	    for (Cell cell : this.cells) {
	    	double [] values = {this.cells.indexOf(cell), cell.getPoint().getLatitude(), cell.getPoint().getLongitude(), cell.getIn(), cell.getOut()};	    
	    	data.add(new Instance(1.0, values));
	    }
	    
	    ArffSaver saver = new ArffSaver();
	    saver.setInstances(data);
	    saver.setFile(new File(filename));
	    saver.writeBatch();
	}
	
}
