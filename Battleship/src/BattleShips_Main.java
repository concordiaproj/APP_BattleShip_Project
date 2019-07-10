import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class BattleShips_Main {
	static LinkedList<Integer> availableCoordinates[] = new LinkedList[10];
	static List<List<shipCoordinates>> lstAllShips_Player = new ArrayList<>();
	static List<List<shipCoordinates>> lstAllShips_Computer = new ArrayList<>();
	static char[][] sea = new char[10][10];
	static char[][] seaComputerForPlayer = new char[10][10];
	static char[][] seaComputer = new char[10][10];
	static List<Integer> shipSize = new ArrayList<>();

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		for (int i = 0; i < 10; i++) {
			availableCoordinates[i] = new LinkedList<>();
			for (int j = 0; j < 10; j++) {
				availableCoordinates[i].add(j);
			}
		}
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				sea[i][j] = ' ';
				seaComputer[i][j] = ' ';
				seaComputerForPlayer[i][j] = ' ';
			}
		}
		shipSize.add(2);
		shipSize.add(3);
		shipSize.add(3);
		shipSize.add(4);
		shipSize.add(5);
		int arr[][] = { { 0, 6, 0, 7 }, { 3, 7, 3, 9 }, { 6, 7, 6, 9 }, { 2, 1, 2, 4 }, { 4, 5, 8, 5 } };

		for (int k = 0; k < shipSize.size(); k++) {
			lstAllShips_Player.add(selectCoordinates(shipSize.get(k), k, arr[k]));
		}
		System.out.println("Player sea");
		System.out.println("  0123456789");
		for (int i = 0; i < 10; i++) {
			System.out.print(i + "|");
			for (int j = 0; j < 10; j++)
				System.out.print(sea[i][j]);
			System.out.println();
		}
		System.out.println("\n\n");
		deployComputerShips();
		System.out.println("Computer Sea : ");
		System.out.println("  0123456789");
		for (int i = 0; i < 10; i++) {
			System.out.print(i + "|");
			for (int j = 0; j < 10; j++)
				System.out.print(seaComputer[i][j]);
			System.out.println();
		}
		System.out.println("\n\n");
		Game sg = new Game(availableCoordinates, lstAllShips_Player, lstAllShips_Computer, sea, seaComputerForPlayer,
				seaComputer);
		sg.startGame();
		// startGameLocal();
	}

	private static List<shipCoordinates> selectCoordinates(int currentShipSize, int shipNumber, int arr[]) {
		// TODO Auto-generated method stub
		List<shipCoordinates> lstShipFinal = new ArrayList<>();
		int X1 = arr[0];
		int Y1 = arr[1];
		int X2 = arr[2];
		int Y2 = arr[3];
		if (X1 == X2) {// hor
			for (int i = Math.min(Y1, Y2); i <= Math.max(Y1, Y2); i++) {
				shipCoordinates sc = new shipCoordinates(X2, i);
				sea[X2][i] = 'S';
				lstShipFinal.add(sc);
			}
		}
		if (Y1 == Y2) {// ver
			for (int i = Math.min(X1, X2); i <= Math.max(X1, X2); i++) {
				shipCoordinates sc = new shipCoordinates(i, Y1);
				sea[i][Y1] = 'S';
				lstShipFinal.add(sc);
			}
		}
		return lstShipFinal;
	}

	private static void deployComputerShips() {
		// TODO Auto-generated method stub
		Random rand = new Random();
		List<shipCoordinates> lstTempCoord = new ArrayList<>();
		for (int i = 0; i < shipSize.size(); i++) {
			lstTempCoord.clear();
			int currentShipSize = shipSize.get(i);
			while (true) {
				int X1 = rand.nextInt(10);
				int Y1 = availableCoordinates[X1].get(rand.nextInt(availableCoordinates[X1].size()));
				if (X1 - currentShipSize >= 0 && isValid(X1, Y1, X1 - currentShipSize + 1, Y1)) {
					shipCoordinates sc = new shipCoordinates(X1 - currentShipSize + 1, Y1);
					lstTempCoord.add(sc);
				}
				if (X1 + currentShipSize <= 9 && isValid(X1, Y1, X1 + currentShipSize - 1, Y1)) {
					shipCoordinates sc = new shipCoordinates(X1 + currentShipSize - 1, Y1);
					lstTempCoord.add(sc);
				}
				if (Y1 - currentShipSize >= 0 && isValid(X1, Y1, X1, Y1 - currentShipSize + 1)) {
					shipCoordinates sc = new shipCoordinates(X1, Y1 - currentShipSize + 1);
					lstTempCoord.add(sc);
				}
				if (Y1 + currentShipSize <= 9 && isValid(X1, Y1, X1, Y1 + currentShipSize - 1)) {
					shipCoordinates sc = new shipCoordinates(X1, Y1 + currentShipSize - 1);
					lstTempCoord.add(sc);
				}
				if (lstTempCoord.size() > 0) {
					shipCoordinates sc = lstTempCoord.get(rand.nextInt(lstTempCoord.size()));
					removeCoordinates(X1, Y1, sc.Xcoord, sc.Ycoord);
					lstAllShips_Computer.add(putShips(X1, Y1, sc.Xcoord, sc.Ycoord));
					break;
				}
			}
		}
	}

	private static List<shipCoordinates> putShips(int x1, int y1, int x2, int y2) {
		// TODO Auto-generated method stub
		List<shipCoordinates> lstTempShip = new ArrayList<>();
		if (x1 == x2) {
			for (int i = Math.min(y1, y2); i <= Math.max(y1, y2); i++) {
				seaComputer[x1][i] = 'S';
				shipCoordinates sc = new shipCoordinates(x1, i);
				lstTempShip.add(sc);
			}
		}
		if (y1 == y2) {
			for (int i = Math.min(x1, x2); i <= Math.max(x1, x2); i++) {
				seaComputer[i][y1] = 'S';
				shipCoordinates sc = new shipCoordinates(i, y1);
				lstTempShip.add(sc);
			}
		}
		return lstTempShip;
	}

	private static void removeCoordinates(int x1, int y1, int x2, int y2) {
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

	private static boolean isValid(int x1, int y1, int x2, int y2) {
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
