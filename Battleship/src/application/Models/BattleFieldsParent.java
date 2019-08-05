package application.Models;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import application.Controller.Main;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * 
 * @author dhvaniagola
 * @author Sahana
 * @author sanja
 * 
 */
public abstract class BattleFieldsParent {

	GridPane gridPane;
	public int id;
	public List<Ship2> lstShip = new ArrayList<>();
	public ArrayList<ArrayList<Block>> gameBoard = new ArrayList<ArrayList<Block>>(10);
	public int intTotalAliveShips;
	public int intScore = 0;

	/**
	 * This Constructor will set the gridPane object and initial value of total
	 * alive ships
	 * 
	 * @param gp Grid of the player
	 */
	public BattleFieldsParent(GridPane gp) {
		this.gridPane = gp;
		this.intTotalAliveShips = 5;
	}

	/**
	 * This method convert x and y coordinates to the Node object
	 * 
	 * @param x x-coordinate of the node
	 * @param y Y-coordinate of the node
	 * @return Returns the node with coordinates (x,y)
	 */
	public Node getBlockNode(int x, int y) {
		GridPane gpTemp = this.gridPane;

		for (Node n : gpTemp.getChildren()) {
			if (n == null) {
				return null;
			}
			if (GridPane.getRowIndex(n) == null || GridPane.getColumnIndex(n) == null) {
				System.out.println("gpTemp Null");
			}
			if (GridPane.getRowIndex(n) == x && GridPane.getColumnIndex(n) == y) {
				return n;
			}
		}
		return null;
	}

	/**
	 * This method will store a ship in the data structure.
	 * 
	 * @param ship      A ship which need to be store
	 * @param isRotated Is ship is rotated or not
	 */
	public void addShip(Ship2 ship, boolean isRotated) {

		// Do NOT add ship if it's not valid. (only applies to player game board)
//        if (id == 1) {
//            if (!this.isValidToPlace(ship.getSX(), ship.getSY(), ship.getSizeOfShip(), isRotated)) {
//                return;
//            }
//        }
		for (int x = ship.getSX(); x <= ship.getEX(); x++) {
			for (int y = ship.getSY(); y <= ship.getEY(); y++) {
//                Ship2 oldShip = this.gameBoard.get(x).get(y).getShip();
//                this.gameBoard.get(x).get(y).setShip(ship);
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
	}

	/**
	 * This method checks that the ship is valid to place or not
	 * 
	 * @param x             x-coordinate of first rectangle of the ship
	 * @param y             y-coordinate of first rectangle of the ship
	 * @param intSize       size of the ship
	 * @param boolIsRotated is ship rotated or not
	 * @return Returns true if ship is valid to place else returns false
	 */
	public boolean isValidToPlace(int x, int y, int intSize, boolean boolIsRotated) {
		// if boolIsRotated is true:vertical ship
		int xEnd = x;
		int yEnd = y;

		if (boolIsRotated) {
			xEnd = x + intSize - 1;
		} else
			yEnd = y + intSize - 1;
//		System.out.println("xEnd,yEnd,isRotate:"+xEnd+","+yEnd+","+boolIsRotated);
		if (xEnd > 9 || yEnd > 9)
			return false;
		if (boolIsRotated) {
			for (int i = x; i <= xEnd; i++) {
				for (int j = -1; j <= 1; j++) {
					for (int k = -1; k <= 1; k++) {
						if ((i + j) >= 0 && (i + j) < 10 && (y + k) >= 0 && (y + k) < 10) {

							if (this.gameBoard.get(i + j).get(y + k).getCharOccupiedFor() == 'S')
								return false;
						}
					}
				}
			}
		} else {
			for (int i = y; i <= yEnd; i++) {
				for (int j = -1; j <= 1; j++) {
					for (int k = -1; k <= 1; k++) {
						if ((x + j) >= 0 && (x + j) < 10 && (i + k) >= 0 && (i + k) < 10) {
//							System.out.println("neightbours : "+(x+j)+","+(i+k));
							if (this.gameBoard.get(x + j).get(i + k).getCharOccupiedFor() == 'S')
								return false;
						}
					}
				}
			}
		}
		return true;
	}

	/**
	 * This method checks that after hit on (x,y) the whole ship is destroyed or
	 * not.
	 * 
	 * @param intX x-coordinate of hit
	 * @param intY y-coordinate of hit
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
	 * @param x       x-coordinate of hit
	 * @param y       y-coordinate of hit
	 * @param isSalva the game is normal or salva variation
	 * @param r       Rectangle on which player hit
	 * @param i       Index of the list
	 * @return Returns true is player hit on ship or on already hit place else
	 *         returns false
	 */
	public boolean isHit(int x, int y, boolean isSalva, Rectangle r, int i) {
		if (this.gameBoard.get(x).get(y).getCharOccupiedFor() == 'M'
				|| this.gameBoard.get(x).get(y).getCharOccupiedFor() == 'H') {
			if (this.gameBoard.get(x).get(y).getCharOccupiedFor() == 'M') {
				Main.lstNodeToSelect.get(i).setFill(Color.GREY);
			} else {
				Main.lstNodeToSelect.get(i).setFill(Color.RED);
			}
			if (!isSalva) {
//				System.out.println("going to clear 1");
				Main.lstNodeToSelect.clear();
				return true;
			}
		} else if (this.gameBoard.get(x).get(y).getCharOccupiedFor() == 'S') {
			if (this.id == 1) {
				Main.bfComputer.intScore += 50;
			} else
				Main.bfPlayer.intScore += 50;
			this.gameBoard.get(x).get(y).setCharOccupiedFor('H');
			Main.lstNodeToSelect.get(i).setFill(Color.RED);
			if (this.id == 1)
				Main.bfPlayer.stkHitPoints.push(new DataCoordinates(x, y));
			if (this.isShipDestroyed(x, y)) {
				this.intTotalAliveShips--;
				if (this.id == 1) {
					Main.bfComputer.intScore += 20;
				} else
					Main.bfPlayer.intScore += 20;
				if (isWinner()) {
					String strWin = "";
					if (this.id == 1)
						strWin = "Computer Won !!";
					else
						strWin = "You Won !!";
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Information Dialog");
					alert.setHeaderText(null);
					alert.setContentText(strWin);
					Optional<ButtonType> result = alert.showAndWait();
					if (result.get() == ButtonType.OK) {
						System.exit(0);
					}

				}
			}
			if (!isSalva) {
				Main.lstNodeToSelect.clear();
				return true;
			}
		}

		else if (this.gameBoard.get(x).get(y).getCharOccupiedFor() == 'B') {
			this.gameBoard.get(x).get(y).setCharOccupiedFor('M');
			Main.lstNodeToSelect.get(i).setFill(Color.GREY);
		}
		return false;
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
}
