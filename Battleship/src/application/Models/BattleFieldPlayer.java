package application.Models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

import javafx.scene.Node;
import javafx.scene.layout.GridPane;

public class BattleFieldPlayer extends BattleFieldsParent {
	public Stack<DataCoordinates> stkHitPoints = new Stack<>();

	public BattleFieldPlayer(GridPane gp) {
		super(gp);
		// TODO Auto-generated constructor stub
		this.id = 1;
		for (int i = 0; i < 10; i++) {
			ArrayList<Block> arrLstBlock = new ArrayList<>();
			for (int j = 0; j < 10; j++) {
				Block blk = new Block(i, j, 'B');
				arrLstBlock.add(blk);
			}
			gameBoard.add(arrLstBlock);
		}
	}

	public List<DataCoordinates> getFromAIAlgorithm(int intNoOfTurns) {
		// TODO Auto-generated method stub
		List<DataCoordinates> lstReturn = new ArrayList<>();
		w1: while (stkHitPoints.size() > 0 && lstReturn.size() < intNoOfTurns) {
			DataCoordinates dc = stkHitPoints.lastElement();
			List<DataCoordinates> lstNeightbours = getNeightbours(dc);
			while (lstNeightbours.size() > 0) {
				DataCoordinates dcTemp = lstNeightbours.get(0);
				if (this.gameBoard.get(dcTemp.x).get(dcTemp.y).getCharOccupiedFor() == 'S'
						|| this.gameBoard.get(dcTemp.x).get(dcTemp.y).getCharOccupiedFor() == 'B') {
					if (lstReturn.size() < intNoOfTurns) {
						lstReturn.add(dcTemp);
						lstNeightbours.remove(0);
					} else {
						break w1;
					}
				} else
					lstNeightbours.remove(0);
			}
			if (lstNeightbours.size() == 0)
				stkHitPoints.pop();
		}
		return lstReturn;
	}

	private List<DataCoordinates> getNeightbours(DataCoordinates dc) {
		// TODO Auto-generated method stub
		List<DataCoordinates> lstReturn = new ArrayList<>();
		int x = dc.x;
		int y = dc.y;
		if ((x + 1) < 10)
			lstReturn.add(new DataCoordinates(x + 1, y));
		if ((x - 1) >= 0)
			lstReturn.add(new DataCoordinates(x - 1, y));
		if ((y + 1) < 10)
			lstReturn.add(new DataCoordinates(x, y + 1));
		if ((y - 1) >= 0)
			lstReturn.add(new DataCoordinates(x, y - 1));
		return lstReturn;
	}

	public boolean allValidPositions(HashMap<Node, ShipLatestLocation> mapShipLocation) {
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
		for (Node n : mapShipLocation.keySet()) {
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
//									break f1;
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
//									break f2;
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

}
