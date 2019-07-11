package application;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class DeployComputerShips {
	LinkedList<Integer> availableCoordinates[] = new LinkedList[10];
	List<List<ShipCoordinates>> lstAllShips_Computer = new ArrayList<>();
	char[][] seaComputer = new char[10][10];
	List<Integer> shipSize = new ArrayList<>();

	public DeployComputerShips(LinkedList<Integer> availableCoordinates[],
			List<List<ShipCoordinates>> lstAllShips_Computer, char[][] seaComputer, List<Integer> shipSize) {
		// TODO Auto-generated constructor stub
		this.availableCoordinates = availableCoordinates;
		this.lstAllShips_Computer = lstAllShips_Computer;
		this.seaComputer = seaComputer;
		this.shipSize = shipSize;
	}

	public void startDeploying() {
		Random rand = new Random();
		List<ShipCoordinates> lstTempCoord = new ArrayList<>();
		for (int i = 0; i < shipSize.size(); i++) {
			lstTempCoord.clear();
			int currentShipSize = shipSize.get(i);
			while (true) {
				int X1 = rand.nextInt(10);
				int Y1 = availableCoordinates[X1].get(rand.nextInt(availableCoordinates[X1].size()));
				if (X1 - currentShipSize >= 0 && isValid(X1, Y1, X1 - currentShipSize + 1, Y1)) {
					ShipCoordinates sc = new ShipCoordinates(X1 - currentShipSize + 1, Y1);
					lstTempCoord.add(sc);
				}
				if (X1 + currentShipSize <= 9 && isValid(X1, Y1, X1 + currentShipSize - 1, Y1)) {
					ShipCoordinates sc = new ShipCoordinates(X1 + currentShipSize - 1, Y1);
					lstTempCoord.add(sc);
				}
				if (Y1 - currentShipSize >= 0 && isValid(X1, Y1, X1, Y1 - currentShipSize + 1)) {
					ShipCoordinates sc = new ShipCoordinates(X1, Y1 - currentShipSize + 1);
					lstTempCoord.add(sc);
				}
				if (Y1 + currentShipSize <= 9 && isValid(X1, Y1, X1, Y1 + currentShipSize - 1)) {
					ShipCoordinates sc = new ShipCoordinates(X1, Y1 + currentShipSize - 1);
					lstTempCoord.add(sc);
				}
				if (lstTempCoord.size() > 0) {
					ShipCoordinates sc = lstTempCoord.get(rand.nextInt(lstTempCoord.size()));
					removeCoordinates(X1, Y1, sc.Xcoord, sc.Ycoord);
					lstAllShips_Computer.add(putShips(X1, Y1, sc.Xcoord, sc.Ycoord));
					break;
				}
			}
		}

	}

	private List<ShipCoordinates> putShips(int x1, int y1, int x2, int y2) {
		// TODO Auto-generated method stub
		List<ShipCoordinates> lstTempShip = new ArrayList<>();
		if (x1 == x2) {
			for (int i = Math.min(y1, y2); i <= Math.max(y1, y2); i++) {
				seaComputer[x1][i] = 'S';
				ShipCoordinates sc = new ShipCoordinates(x1, i);
				lstTempShip.add(sc);
			}
		}
		if (y1 == y2) {
			for (int i = Math.min(x1, x2); i <= Math.max(x1, x2); i++) {
				seaComputer[i][y1] = 'S';
				ShipCoordinates sc = new ShipCoordinates(i, y1);
				lstTempShip.add(sc);
			}
		}
		return lstTempShip;
	}

	private void removeCoordinates(int x1, int y1, int x2, int y2) {
		// TODO Auto-generated method stub
		if (x1 == x2) {
			for (int i = Math.min(y1, y2); i <= Math.max(y1, y2); i++)
				availableCoordinates[x1].remove(new Integer(i));
		}
		if (y1 == y2) {
			for (int i = Math.min(x1, x2); i <= Math.max(x1, x2); i++)
				availableCoordinates[i].remove(new Integer(y1));
		}
	}

	private boolean isValid(int x1, int y1, int x2, int y2) {
		// TODO Auto-generated method stub
		if (x1 == x2) {
			for (int i = Math.min(y1, y2); i <= Math.max(y1, y2); i++) {
				if (i >= 0 && i <= 9) {
					if (seaComputer[x1][i] == 'S')
						return false;
				} else
					return false;
			}
		}
		if (y1 == y2) {
			for (int i = Math.min(x1, x2); i <= Math.max(x1, x2); i++) {
				if (i >= 0 && i <= 9) {
					if (seaComputer[i][y1] == 'S')
						return false;
				} else
					return false;
			}
		}
		return true;
	}

}
