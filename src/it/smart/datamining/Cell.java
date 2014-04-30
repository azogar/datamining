package it.smart.datamining;

/**
 * Classe per gestire le celle.
 * 
 * @author Luca Pardini
 *
 */
public class Cell {
	private Point point;
	private int in; //TODO aggiungere gestione multipla del tempo
	private int out;
	
	public Cell() {
		this(new Point());
	}
	
	public Cell(Point point) {
		this(point, 0, 0);
	}
	
	public Cell(Point point, int in, int out) {
		super();
		this.point = point;
		this.in = in;
		this.out = out;
	}

	public Point getPoint() {
		return point;
	}

	public void setPoint(Point point) {
		this.point = point;
	}

	public int getIn() {
		return in;
	}

	public void setIn(int in) {
		this.in = in;
	}

	public int getOut() {
		return out;
	}

	public void setOut(int out) {
		this.out = out;
	}
	
	public void incrIn() {
		this.in++;
	}
	
	public void incrOut() {
		this.out++;
	}
	
	@Override
	public String toString() {
		return "\n" + String.valueOf(point) + " IN: " + this.in + " OUT: " + this.out;
	}
}
