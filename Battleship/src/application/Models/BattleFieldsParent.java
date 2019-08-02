package application.Models;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.Node;
import javafx.scene.layout.GridPane;

public abstract class BattleFieldsParent {

	GridPane gridPane;
	int id;
	public List<Ship2> lstShip = new ArrayList<>();
	public ArrayList<ArrayList<Block>> gameBoard = new ArrayList<ArrayList<Block>>(10);
	public int intTotalAliveShips;
	public int intScore = 0;

	public BattleFieldsParent(GridPane gp) {
		this.gridPane = gp;
		this.intTotalAliveShips = 5;
	}

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

	public boolean isShipDestroyed(int intX, int intY) {
		// TODO Auto-generated method stub
		for (int i = 0; i < this.lstShip.size(); i++) {
			for (int j = 0; j < this.lstShip.get(i).lstShipCoord.size(); j++) {
				if (this.lstShip.get(i).lstShipCoord.get(j).x == intX
						&& this.lstShip.get(i).lstShipCoord.get(j).y == intY) {
					this.lstShip.get(i).lstShipCoord.remove(j);
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
}
