package it.smart.datamining;

import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

/**
 * Classe per gestire la griglia.
 * 
 * @author Luca Pardini
 *
 */
public class Grid {
	private double width;
	private double height;
	private Vector<Cell> cells;
		
	public Grid() {
		this(0, 0);
	}
	
	public Grid(double width, double height) {
		this(width, height, new Vector<Cell>());
	}

	public Grid(double width, double height, Vector<Cell> cells) {
		super();
		this.width = width;
		this.height = height;
		this.cells = cells;
	}
	
	public double getWidth() {
		return width;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	public Vector<Cell> getCells() {
		return cells;
	}

	public void setCells(Vector<Cell> cells) {
		this.cells = cells;
	}

	@Override
	public String toString() {
		return "Height: " + this.height + "\nWidth: " + this.width + "\nCells:\n" + String.valueOf(this.cells);
	}
	
	public Cell getCell(double latitude, double longitude) {
		for (Cell cell : this.cells) {
			if ((latitude >= cell.getPoint().getLatitude()) && (latitude < cell.getPoint().getLatitude() + this.width) && (longitude >= cell.getPoint().getLongitude()) && (longitude < cell.getPoint().getLongitude() + this.height))
				return cell;
		}
		return createCell(latitude, longitude);
	}
	
	private Cell createCell(double latitude, double longitude) {
		Cell cell = new Cell(new Point(Math.round((latitude / this.width) - 0.5) * this.width, Math.round((longitude / this.height) - 0.5) * this.height));
		this.cells.add(cell);
		return cell;
	}
	
	public void removeCellsLess(int threshold) {
		for (int i = this.cells.size() - 1; i >= 0; i--) {
			int sum = 0;
			for (String day : this.getDays()) {
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
	
	public Instances getInstances() {
		Set<String> days = this.getDays();
 		
		FastVector schema = new FastVector();
		schema.addElement(new Attribute("cell_id"));
		schema.addElement(new Attribute("p1_latitude"));
		schema.addElement(new Attribute("p1_longitude"));
		schema.addElement(new Attribute("p2_latitude"));
		schema.addElement(new Attribute("p2_longitude"));
		for (String day : days) {
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
	    	values[i++] = cell.getPoint().getLatitude() + this.width;
	    	values[i++] = cell.getPoint().getLongitude() + this.height;
			for (String day : days) {
				values[i++] = Cell.check(cell.getIn(), day);
				totin += values[i - 1];
				values[i++] = Cell.check(cell.getOut(), day);
				totout += values[i - 1];
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
	
	public Set<String> getDays() {
		Set<String> days = new HashSet<String>();
		for (Cell cell : this.cells) {
			days.addAll(cell.getIn().keySet());
			days.addAll(cell.getOut().keySet());
		}
		return days;
	}
}
