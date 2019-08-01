package application.Models;
/**
 * 
 * @author SAHIL
 * Getting ship live location
 */
public class ShipLatestLocation {
	public double intLocalX;
	public double intLocalY;
	public int size;
	public boolean boolIsRotated;

	public ShipLatestLocation(double intLocalX, double intLocalY, int size, boolean boolIsRotated) {
		this.intLocalX = intLocalX;
		this.intLocalY = intLocalY;
		this.size = size;
		this.boolIsRotated = boolIsRotated;
	}
}
