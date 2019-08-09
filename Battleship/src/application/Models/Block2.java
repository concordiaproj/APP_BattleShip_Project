package application.Models;

/**
 * 
 * 
 * @author dhvaniagola
 * @author arunipatel
 * @author SAHIL
 */
public class Block2 {
	private char charOccupiedFor;
	private int x;
	private int y;
	private Ship2 ship;

	/**
	 * This constructor set the information of each block of the grid
	 * 
	 * @param x
	 *            x-Coordinate of block
	 * @param y
	 *            y-Coordinate of block
	 * @param charOccupiedFor
	 *            Char which define the state of the block
	 */
	public Block2(int x, int y, char charOccupiedFor) {
		this.charOccupiedFor = charOccupiedFor;
		this.x = x;
		this.y = y;
	}

	public void setShip(Ship2 ship) {
		this.ship = ship;
	}

	public Ship2 getShip() {
		return this.ship;
	}

	public void setCharOccupiedFor(char c) {
		this.charOccupiedFor = c;
	}

	public char getCharOccupiedFor() {
		return this.charOccupiedFor;
	}
}
