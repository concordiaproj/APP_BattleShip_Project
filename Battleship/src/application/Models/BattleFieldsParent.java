package application.Models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import javafx.scene.shape.Rectangle;

/**
 * 
 * @author dhvaniagola
 * @author Sahana
 * @author sanja
 * 
 */
public abstract class BattleFieldsParent {

	// GridPane gridPane;
	public int id;
	public List<Ship2> lstShip = new ArrayList<>();
	public ArrayList<ArrayList<Block>> gameBoard = new ArrayList<ArrayList<Block>>(10);
	public int intTotalAliveShips;
	public int intScore = 0;
	public boolean isSalva = false;
	public boolean isReady = false;

	/**
	 * This Constructor will set the gridPane object and initial value of total
	 * alive ships
	 * 
	 * @param gp
	 *            Grid of the player
	 */
	// public BattleFieldsParent(GridPane gp) {
	public BattleFieldsParent() {
		// this.gridPane = gp;
		this.intTotalAliveShips = 5;
	}

	/**
	 * This method convert x and y coordinates to the Node object
	 * 
	 * @param x
	 *            x-coordinate of the node
	 * @param y
	 *            Y-coordinate of the node
	 * @return Returns the node with coordinates (x,y)
	 */
	// public Node getBlockNode(int x, int y) {
	// GridPane gpTemp = this.gridPane;
	//
	// for (Node n : gpTemp.getChildren()) {
	// if (n == null) {
	// return null;
	// }
	// if (GridPane.getRowIndex(n) == null || GridPane.getColumnIndex(n) == null) {
	// System.out.println("gpTemp Null");
	// }
	// if (GridPane.getRowIndex(n) == x && GridPane.getColumnIndex(n) == y) {
	// return n;
	// }
	// }
	// return null;
	// }

	/**
	 * This method will store a ship in the data structure.
	 * 
	 * @param ship
	 *            A ship which need to be store
	 * @param isRotated
	 *            Is ship is rotated or not
	 */
	public void addShip(int size, int startX, int startY, int endX, int endY, boolean isDestroyed, boolean isRotated) {

		// Do NOT add ship if it's not valid. (only applies to player game board)
		// if (id == 1) {
		// if (!this.isValidToPlace(ship.getSX(), ship.getSY(), ship.getSizeOfShip(),
		// isRotated)) {
		// return;
		// }
		// }
		Ship2 ship = new Ship2(size, startX, startY, endX, endY, isDestroyed);
		ship.setShipCoord();

		for (int x = ship.getSX(); x <= ship.getEX(); x++) {
			for (int y = ship.getSY(); y <= ship.getEY(); y++) {
				// Ship2 oldShip = this.gameBoard.get(x).get(y).getShip();
				// this.gameBoard.get(x).get(y).setShip(ship);
			}
		}
		if (ship.intStartX == ship.intEndX) {
			for (int i = Math.min(ship.intStartY, ship.intEndY); i <= Math.max(ship.intStartY, ship.intEndY); i++) {
				this.gameBoard.get(ship.intEndX).get(i).setShip(ship);
				this.gameBoard.get(ship.intEndX).get(i).setCharOccupiedFor('S');
			}
		} else {
			for (int i = Math.min(ship.intStartX, ship.intEndX); i <= Math.max(ship.intStartX, ship.intEndX); i++) {
				this.gameBoard.get(i).get(ship.intEndY).setShip(ship);
				this.gameBoard.get(i).get(ship.intEndY).setCharOccupiedFor('S');
			}
		}
		this.lstShip.add(ship);
	}

	/**
	 * This method checks that the ship is valid to place or not
	 * 
	 * @param x
	 *            x-coordinate of first rectangle of the ship
	 * @param y
	 *            y-coordinate of first rectangle of the ship
	 * @param intSize
	 *            size of the ship
	 * @param boolIsRotated
	 *            is ship rotated or not
	 * @return Returns true if ship is valid to place else returns false
	 */
	// public boolean isValidToPlace(int x, int y, int intSize, boolean
	// boolIsRotated) {
	// // if boolIsRotated is true:vertical ship
	// int xEnd = x;
	// int yEnd = y;
	//
	// if (boolIsRotated) {
	// xEnd = x + intSize - 1;
	// } else
	// yEnd = y + intSize - 1;
	//// System.out.println("xEnd,yEnd,isRotate:"+xEnd+","+yEnd+","+boolIsRotated);
	// if (xEnd > 9 || yEnd > 9)
	// return false;
	// if (boolIsRotated) {
	// for (int i = x; i <= xEnd; i++) {
	// for (int j = -1; j <= 1; j++) {
	// for (int k = -1; k <= 1; k++) {
	// if ((i + j) >= 0 && (i + j) < 10 && (y + k) >= 0 && (y + k) < 10) {
	//
	// if (this.gameBoard.get(i + j).get(y + k).getCharOccupiedFor() == 'S')
	// return false;
	// }
	// }
	// }
	// }
	// } else {
	// for (int i = y; i <= yEnd; i++) {
	// for (int j = -1; j <= 1; j++) {
	// for (int k = -1; k <= 1; k++) {
	// if ((x + j) >= 0 && (x + j) < 10 && (i + k) >= 0 && (i + k) < 10) {
	//// System.out.println("neightbours : "+(x+j)+","+(i+k));
	// if (this.gameBoard.get(x + j).get(i + k).getCharOccupiedFor() == 'S')
	// return false;
	// }
	// }
	// }
	// }
	// }
	// return true;
	// }

	/**
	 * This method checks that after hit on (x,y) the whole ship is destroyed or
	 * not.
	 * 
	 * @param intX
	 *            x-coordinate of hit
	 * @param intY
	 *            y-coordinate of hit
	 * @return Returns true if the ship is destroyed else returns false.
	 */
	public boolean isShipDestroyed(int intX, int intY) {
		// TODO Auto-generated method stub
		System.out.println("ships : " + this.lstShip.size());
		for (int i = 0; i < this.lstShip.size(); i++) {
			for (int j = 0; j < this.lstShip.get(i).lstShipCoord.size(); j++) {
				if (this.lstShip.get(i).lstShipCoord.get(j).x == intX
						&& this.lstShip.get(i).lstShipCoord.get(j).y == intY) {
					System.out.println("found ship soze:" + this.lstShip.get(i).lstShipCoord.size());
					this.lstShip.get(i).lstShipCoord.remove(j);
					System.out.println("list size after remove:" + this.lstShip.get(i).lstShipCoord.size());
					if (this.lstShip.get(i).lstShipCoord.size() == 0) {
						this.lstShip.get(i).setBoolIsHit(true);
						return true;
					} else
						return false;
				}
			}
		}
		return false;
	}

	/**
	 * This method checks that the hit is successful or missed and update the result
	 * in data structure and also update the UI.
	 * 
	 * @param x
	 *            x-coordinate of hit
	 * @param y
	 *            y-coordinate of hit
	 * @param isSalva
	 *            the game is normal or salva variation
	 * @param r
	 *            Rectangle on which player hit
	 * @param i
	 *            Index of the list
	 * @return Returns true is player hit on ship or on already hit place else
	 *         returns false
	 */
	public ServerData isHit(int x, int y, boolean isSalva, int i) {
		ServerData sd = new ServerData(1, 0, false, 0, 0, 0, 0, '\0', x, y, false);
		char c = '\0';
		Random rand = new Random();
		int point = rand.nextInt(5) + 1;
		point = (int) (15 / point);
		if (this.id == 1) {
			Server.bfComputer.intScore += point;
		} else
			Server.bfPlayer.intScore += point;
		if (this.gameBoard.get(x).get(y).getCharOccupiedFor() == 'M'
				|| this.gameBoard.get(x).get(y).getCharOccupiedFor() == 'H') {
			if (this.gameBoard.get(x).get(y).getCharOccupiedFor() == 'M') {
				// Main.lstNodeToSelect.get(i).setFill(Color.GREY);
				// c = 'G';
				sd.c = 'G';
			} else {
				// Main.lstNodeToSelect.get(i).setFill(Color.RED);
				// c = 'R';
				sd.c = 'R';
			}
			if (!isSalva) {
				// System.out.println("going to clear 1");
				// Main.lstNodeToSelect.clear();
				// return true;
				sd.boolReturn = true;
				return sd;
			}
		} else if (this.gameBoard.get(x).get(y).getCharOccupiedFor() == 'S') {
			if (this.id == 1) {
				Server.bfComputer.intScore += 50;
			} else
				Server.bfPlayer.intScore += 50;
			this.gameBoard.get(x).get(y).setCharOccupiedFor('H');
			// Main.lstNodeToSelect.get(i).setFill(Color.RED);
			// c = 'R';
			sd.c = 'R';
			if (this.id == 1)
				Server.bfPlayer.stkHitPoints.push(new DataCoordinates(x, y));
			if (this.isShipDestroyed(x, y)) {
				this.intTotalAliveShips--;
				if (this.id == 1) {
					Server.bfComputer.intScore += 20;
				} else
					Server.bfPlayer.intScore += 20;
				if (isWinner()) {
					sd.isWinner = true;
				}
			}
			if (!isSalva) {
				// Main.lstNodeToSelect.clear();
				// return true;
				sd.boolReturn = true;
				return sd;
			}
		}

		else if (this.gameBoard.get(x).get(y).getCharOccupiedFor() == 'B') {
			this.gameBoard.get(x).get(y).setCharOccupiedFor('M');
			// Main.lstNodeToSelect.get(i).setFill(Color.GREY);
			sd.c = 'G';
		}
		// return false;
		sd.boolReturn = false;
		return sd;
	}

	public boolean isWinner() {
		if (this.intTotalAliveShips == 0)
			return true;
		else
			return false;
	}

	public boolean ishit(int x, int y, boolean isSalva, Rectangle r, int i) {
		if (this.gameBoard.get(x).get(y).getCharOccupiedFor() == 'M'
				|| this.gameBoard.get(x).get(y).getCharOccupiedFor() == 'H'
				|| this.gameBoard.get(x).get(y).getCharOccupiedFor() == 'B') {
			return false;
		}
		if (this.gameBoard.get(x).get(y).getCharOccupiedFor() == 'S') {
			return true;
		}
		return false;
	}

	/**
	 * This method checks that all ships are having correct positions or not.
	 * 
	 * @param mapShipLocation
	 *            A map in which all ships' position is stored
	 * @return Returns true if all ships have correct position else returns false.
	 */
	public boolean allValidPositions(HashMap<String, ShipLatestLocation> mapShipLocation) {
		// TODO Auto-generated method stub
		if (mapShipLocation.size() != 5)
			return false;
		boolean arrBool[][] = new boolean[10][10];
		int x, y, size, xEnd, yEnd;
		boolean isRotated;
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++)
				arrBool[i][j] = false;
		}
		for (String n : mapShipLocation.keySet()) {
			x = (int) (mapShipLocation.get(n).intLocalX / 30.0);
			y = (int) (mapShipLocation.get(n).intLocalY / 30.0);
			size = mapShipLocation.get(n).size;
			isRotated = mapShipLocation.get(n).boolIsRotated;
			xEnd = x;
			yEnd = y;
			if (isRotated)
				xEnd = x + size - 1;
			else
				yEnd = y + size - 1;

			if (xEnd > 9 || yEnd > 9)
				return false;

			if (isRotated) {
				f1: for (int i = x; i <= xEnd; i++) {
					for (int j = -1; j <= 1; j++) {
						for (int k = -1; k <= 1; k++) {
							if ((i + j) >= 0 && (i + j) < 10 && (y + k) >= 0 && (y + k) < 10) {
								if (arrBool[i + j][y + k] == true) {
									return false;
									// break f1;
								}
							}
						}
					}
				}
			} else {
				f2: for (int i = y; i <= yEnd; i++) {
					for (int j = -1; j <= 1; j++) {
						for (int k = -1; k <= 1; k++) {
							if ((x + j) >= 0 && (x + j) < 10 && (i + k) >= 0 && (i + k) < 10) {
								if (arrBool[x + j][i + k] == true) {
									return false;
									// break f2;
								}
							}
						}
					}
				}
			}

			if (isRotated) {
				for (int i = x; i <= xEnd; i++) {
					arrBool[i][y] = true;
				}
			} else {
				for (int i = y; i <= yEnd; i++) {
					arrBool[x][i] = true;
				}
			}

		}
		return true;
	}

	public boolean setShip_method(int x, int y) {
		Ship2 shipToRemove = null;
		for (Ship2 ship : this.lstShip) {
			if (ship.getSX() == x && ship.getSY() == y) {
				shipToRemove = ship;
				for (int i = shipToRemove.getSX(); i <= shipToRemove.getEX(); i++) {
					for (int j = shipToRemove.getSY(); j <= shipToRemove.getEY(); j++) {
						this.gameBoard.get(i).get(j).setShip(null);
					}
				}
			}
		}
		if (this.lstShip.remove(shipToRemove))
			return true;
		else
			return false;
	}

}
