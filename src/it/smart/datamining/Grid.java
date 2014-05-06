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
	private float height;
	private float width;
	private Vector<Cell> cells;
	
	public static Grid create(float minLatitude, float maxLatitude, float minLongitude, float maxLongitude, float height, float width) {
		int size = (Math.round((maxLatitude - minLatitude) / width) + 1) * (Math.round((maxLongitude - minLongitude) / height) + 1);
		Grid grid = new Grid(height, width, new Vector<Cell>(size));
		
		int i = 0;
		for (float longitude = minLongitude; longitude < maxLongitude; longitude += height)
			for (float latitude = minLatitude; latitude < maxLatitude; latitude += width) {
				grid.getCells().add(new Cell(new Point(latitude, longitude)));
			}

		return grid;
	}
	
	public Grid() {
		this(0, 0);
	}
	
	public Grid(float height, float width) {
		this(height, width, new Vector<Cell>());
	}
	
	public Grid(float height, float width, Vector<Cell> cells) {
		super();
		this.height = height;
		this.width = width;
		this.cells = cells;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	public float getwidth() {
		return width;
	}

	public void setwidth(float width) {
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
	
	public Cell getCell(float latitude, float longitude) {
		for (Cell cell : this.cells) {
			if ((latitude >= cell.getPoint().getLatitude()) && (latitude < cell.getPoint().getLatitude() + this.width) && (longitude >= cell.getPoint().getLongitude()) && (longitude < cell.getPoint().getLongitude() + this.height))
				return cell;
		}
		return null;
	}
	
	public void removeCellsLess(int threshold) {
		for (int i = this.cells.size() - 1; i >= 0; i--) {
			int sum = 0;
			for (String day : Cell.days) {
				sum += Cell.check(this.cells.get(i).getIn(), day) + Cell.check(this.cells.get(i).getOut(), day);
			}
		
			if (sum <= threshold) {
				this.cells.remove(i);
			}
		}
	}
	
	public void removeEmptyCells() {
		this.removeCellsLess(0);
	}
	
	public void saveArffFile(String filename) throws IOException {
	    ArffSaver saver = new ArffSaver();
	    saver.setInstances(this.getInstances());
	    saver.setFile(new File(filename));
	    saver.writeBatch();
	}
	
	public Instances getInstances() {
		FastVector schema = new FastVector();
		schema.addElement(new Attribute("cell_id"));
		schema.addElement(new Attribute("latitude"));
		schema.addElement(new Attribute("longitude"));
		for (String day : Cell.days) {
			schema.addElement(new Attribute("in-" + day));
			schema.addElement(new Attribute("out-" + day));
		}
		schema.addElement(new Attribute("tot-in"));
		schema.addElement(new Attribute("tot-out"));
		schema.addElement(new Attribute("tot"));
		
	    Instances data = new Instances("DataSet", schema, 0);
	    for (Cell cell : this.cells) {
	    	int i = 0, totin = 0, totout = 0;
	    	double [] values = new double[schema.size()];
	    	values[i++] = this.cells.indexOf(cell);
	    	values[i++] = cell.getPoint().getLatitude();
	    	values[i++] = cell.getPoint().getLongitude();
			for (String day : Cell.days) {
				values[i++] = Cell.check(cell.getIn(), day);
				values[i++] = Cell.check(cell.getOut(), day);
				totin += totin += values[i - 2];
				totout += totin += values[i - 1];
			}
	    	values[i++] = totin;
	    	values[i++] = totout;
	    	values[i++] = totin + totout;
	    
	    	data.add(new Instance(1.0, values));
	    }		
	    
	    return data;
	}
	
	public void addGrid(Grid grid) {
		this.cells.addAll(grid.getCells());
	}
	
}
