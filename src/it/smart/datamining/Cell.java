package it.smart.datamining;

import java.util.HashMap;

/**
 * Classe per gestire le celle.
 * 
 * @author Luca Pardini
 *
 */
public class Cell {
	private Point point;
	private HashMap<String, Integer> in;
	private HashMap<String, Integer> out;

	public static int check(HashMap<String, Integer> hashmap, String key) {
		return (hashmap.get(key) != null)?hashmap.get(key):0;
	}
	
	public Cell() {
		this(new Point());
	}
	
	public Cell(Point point) {
		this(point, new HashMap<String, Integer>(),new HashMap<String, Integer>());
	}
	
	public Cell(Point point, HashMap<String, Integer> in, HashMap<String, Integer> out) {
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

	public HashMap<String, Integer> getIn() {
		return in;
	}

	public void setIn(HashMap<String, Integer> in) {
		this.in = in;
	}

	public HashMap<String, Integer> getOut() {
		return out;
	}

	public void setOut(HashMap<String, Integer> out) {
		this.out = out;
	}
	
	public void incrIn(String key) {
		this.in.put(key, check(this.in, key) + 1);
	}
	
	public void incrOut(String key) {
		this.out.put(key, check(this.out, key) + 1);
	}
	
	@Override
	public String toString() {
		return "\n" + String.valueOf(point) + " IN: " + String.valueOf(this.in) + " OUT: " + String.valueOf(this.out);
	}
}
