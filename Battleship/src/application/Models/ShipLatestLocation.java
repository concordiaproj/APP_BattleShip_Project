package application.Models;

/**
 * @author Sahana
 * @author sanja
 * @author dhvaniagola
 *
 */
public class ShipLatestLocation {
	public double intLocalX;
	public double intLocalY;
	public int size;
	public boolean boolIsRotated;

	/**
	 * This constructor will set the latest location of the ship
	 * 
	 * @param intLocalX     x-coordinate of first block of the ship
	 * @param intLocalY     y-coordinate of first block of the ship
	 * @param size          size of the ship
	 * @param boolIsRotated Is ship rotated or not
	 */
	public ShipLatestLocation(double intLocalX, double intLocalY, int size, boolean boolIsRotated) {
		this.intLocalX = intLocalX;
		this.intLocalY = intLocalY;
		this.size = size;
		this.boolIsRotated = boolIsRotated;
	}
}
