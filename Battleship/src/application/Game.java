package application;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Game {
	List<List<ShipCoordinates>> lstAllShips_Player = new ArrayList<>();
	List<List<ShipCoordinates>> lstAllShips_Computer = new ArrayList<>();
	char[][] sea = new char[10][10];
	char[][] seaComputerForPlayer = new char[10][10];
	char[][] seaComputer = new char[10][10];

	public Game(List<List<ShipCoordinates>> lstAllShips_Player, List<List<ShipCoordinates>> lstAllShips_Computer,
			char[][] sea, char[][] seaComputerForPlayer, char[][] seaComputer) {
		// TODO Auto-generated constructor stub
		this.lstAllShips_Player = lstAllShips_Player;
		this.lstAllShips_Computer = lstAllShips_Computer;
		this.sea = sea;
		this.seaComputerForPlayer = seaComputerForPlayer;
		this.seaComputer = seaComputer;
	}

	public void startGame() {

		// TODO Auto-generated method stub
		Scanner scn = new Scanner(System.in);
		Random rand = new Random();
		w1: while (true) {// break when game overs
			w2: while (true) // Player's turn
			{
				System.out.println("Select X and Y coordinates of opponent : ");
				int X = scn.nextInt();
				int Y = scn.nextInt();
				if (seaComputer[X][Y] == 'S') {// Hit
					seaComputer[X][Y] = 'H';
					seaComputerForPlayer[X][Y] = 'H';
					if (removeHitPoint(X, Y, "player"))
						System.out.println("Yeah! You have sunk the ship of opponent !!");
					if (allShipHit(lstAllShips_Computer)) {
						System.out.println("Congratulations! You are the winner !!");
						break w1;
					}
					System.out.println("Computer Sea - Player side : ");
					System.out.println("  0123456789");
					for (int i = 0; i < 10; i++) {
						System.out.print(i + "|");
						for (int j = 0; j < 10; j++)
							System.out.print(seaComputerForPlayer[i][j]);
						System.out.println();
					}
					System.out.println("\n\n");
				} else if (seaComputer[X][Y] == ' ') {// miss
					// remove missed point
					seaComputerForPlayer[X][Y] = 'M';
					System.out.println("Oh no! You missed the target !! Better luck next time !");
					System.out.println("Computer Sea - Player side: ");
					System.out.println("  0123456789");
					for (int i = 0; i < 10; i++) {
						System.out.print(i + "|");
						for (int j = 0; j < 10; j++)
							System.out.print(seaComputerForPlayer[i][j]);
						System.out.println();
					}
					System.out.println("\n\n");
					break w2;
				} else {
					System.out.println("You already hit on the same place, Please try again on different place.");
				}
			}
			w3: while (true) {// refactor -done
				while (true) {// not breaking -done
					int X = rand.nextInt(10);
					int Y = rand.nextInt(10);
					System.out.println("COMPUTER TURN : " + X + ", " + Y);
					if (sea[X][Y] == 'S') {// hit
						sea[X][Y] = 'H';
						System.out.println("Computer has hit on your ship");
						System.out.println("  0123456789");
						for (int i = 0; i < 10; i++) {
							System.out.print(i + "|");
							for (int j = 0; j < 10; j++)
								System.out.print(sea[i][j]);
							System.out.println();
						}
						if (removeHitPoint(X, Y, "computer"))
							System.out.println("Yeah! Computer have sunk your ship !!");
						if (allShipHit(lstAllShips_Player)) {
							System.out.println("Computer is the winner !!");
							break w1;
						}
						break;
					} else if (sea[X][Y] == ' ') {// miss
						sea[X][Y] = 'M';
						System.out.println("  0123456789");
						for (int i = 0; i < 10; i++) {
							System.out.print(i + "|");
							for (int j = 0; j < 10; j++)
								System.out.print(sea[i][j]);
							System.out.println();
						}
						System.out.println("Computer has missed the target!!");
						break w3;
					}

				}
			}
		}

	}

	private boolean removeHitPoint(int x, int y, String battleTurn) {
		if (battleTurn.equalsIgnoreCase("player")) {
			for (int i = 0; i < lstAllShips_Computer.size(); i++) {
				for (int j = 0; j < lstAllShips_Computer.get(i).size(); j++) {
					if (lstAllShips_Computer.get(i).get(j).Xcoord == x
							&& lstAllShips_Computer.get(i).get(j).Ycoord == y) {
						lstAllShips_Computer.get(i).remove(j);
						if (lstAllShips_Computer.get(i).size() == 0)
							return true;
						else
							return false;
					}
				}
			}
		}
		if (battleTurn.equalsIgnoreCase("computer")) {
			for (int i = 0; i < lstAllShips_Player.size(); i++) {
				for (int j = 0; j < lstAllShips_Player.get(i).size(); j++) {
					if (lstAllShips_Player.get(i).get(j).Xcoord == x && lstAllShips_Player.get(i).get(j).Ycoord == y) {
						lstAllShips_Player.get(i).remove(j);
						if (lstAllShips_Player.get(i).size() == 0)
							return true;
						else
							return false;
					}
				}
			}
		}
		return false;
	}

	private boolean allShipHit(List<List<ShipCoordinates>> lstAllShips_Player) {
		for (int i = 0; i < lstAllShips_Player.size(); i++) {
			if (lstAllShips_Player.get(i).size() > 0)
				return false;
		}
		return true;
	}
}
