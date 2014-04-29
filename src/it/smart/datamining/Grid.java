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
}
