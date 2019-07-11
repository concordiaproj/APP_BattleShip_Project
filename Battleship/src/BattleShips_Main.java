import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class BattleShips_Main {
	static LinkedList<Integer> availableCoordinates[] = new LinkedList[10];
	static List<List<ShipCoordinates>> lstAllShips_Player = new ArrayList<>();
	static List<List<ShipCoordinates>> lstAllShips_Computer = new ArrayList<>();
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
		DeployComputerShips deployComputerShips = new DeployComputerShips(availableCoordinates, lstAllShips_Computer,
				seaComputer, shipSize);
		deployComputerShips.startDeploying();
		// deployComputerShips();
		System.out.println("Computer Sea : ");
		System.out.println("  0123456789");
		for (int i = 0; i < 10; i++) {
			System.out.print(i + "|");
			for (int j = 0; j < 10; j++)
				System.out.print(seaComputer[i][j]);
			System.out.println();
		}
		System.out.println("\n\n");
		Game sg = new Game(lstAllShips_Player, lstAllShips_Computer, sea, seaComputerForPlayer, seaComputer);
		sg.startGame();

		// startGameLocal();
	}

	private static List<ShipCoordinates> selectCoordinates(int currentShipSize, int shipNumber, int arr[]) {
		// TODO Auto-generated method stub
		List<ShipCoordinates> lstShipFinal = new ArrayList<>();
		int X1 = arr[0];
		int Y1 = arr[1];
		int X2 = arr[2];
		int Y2 = arr[3];
		if (X1 == X2) {// hor
			for (int i = Math.min(Y1, Y2); i <= Math.max(Y1, Y2); i++) {
				ShipCoordinates sc = new ShipCoordinates(X2, i);
				sea[X2][i] = 'S';
				lstShipFinal.add(sc);
			}
		}
		if (Y1 == Y2) {// ver
			for (int i = Math.min(X1, X2); i <= Math.max(X1, X2); i++) {
				ShipCoordinates sc = new ShipCoordinates(i, Y1);
				sea[i][Y1] = 'S';
				lstShipFinal.add(sc);
			}
		}
		return lstShipFinal;
	}
}
