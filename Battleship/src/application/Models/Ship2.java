package application.Models;

import java.util.ArrayList;
import java.util.List;

public class Ship2 {

	int intSize;
	int intStartX;
	int intStartY;
	int intEndX;
	int intEndY;
	boolean boolIsHit;
	List<DataCoordinates> lstShipCoord = new ArrayList<>();

//	String strShipId;
	/**
	 * This constructor set the information of a ship
	 * 
	 * @param intSize   size of the ship
	 * @param intStartX x-Coordinate of first block of the ship
	 * @param intStartY y-Coordinate of first block of the ship
	 * @param intEndX   x-Coordinate of last block of the ship
	 * @param intEndY   y-Coordinate of last block of the ship
	 * @param boolIsHit Is whole ship is hit or not
	 */
	public Ship2(int intSize, int intStartX, int intStartY, int intEndX, int intEndY, boolean boolIsHit) {
		this.intSize = intSize;
		this.intStartX = intStartX;
		this.intStartY = intStartY;
		this.intEndX = intEndX;
		this.intEndY = intEndY;
		this.boolIsHit = boolIsHit;
//		this.strShipId=strShipId;
	}

	public int getSX() {
		return intStartX;
	}

	public int getSY() {
		return intStartY;
	}

	public int getEX() {
		return intEndX;
	}

	public int getEY() {
		return intEndY;
	}

	public int getSizeOfShip() {
		return intSize;
	}

	public boolean getBoolIsHit() {
		return boolIsHit;
	}

	public void setBoolIsHit(boolean boolIsHit) {
		this.boolIsHit = boolIsHit;
	}

	/**
	 * This method will set the coordinates of all the blocks of a ship
	 */
	public void setShipCoord() {
		// TODO Auto-generated method stub
		int x = this.getSX();
		int y = this.getSY();
		int xEnd = this.getEX();
		int yEnd = this.getEY();
		if (x == xEnd) {
			for (int i = y; i <= yEnd; i++) {
				DataCoordinates dc = new DataCoordinates(x, i);
				this.lstShipCoord.add(dc);
			}
		} else {
			for (int j = x; j <= xEnd; j++) {
				DataCoordinates dc = new DataCoordinates(j, y);
				this.lstShipCoord.add(dc);
			}
		}
	}
}
