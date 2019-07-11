package application;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Battlefield extends Parent {
	private VBox vert_X = new VBox();
	int id;
	private int totalShips = 5;

	public Battlefield(int id, EventHandler<? super MouseEvent> handler) {
		// TODO Auto-generated constructor stub
//		this.enemy = enemy;
		this.id = id;
		for (int i = 0; i < 10; i++) {
			HBox hor_Y = new HBox();
			for (int j = 0; j < 10; j++) {
				Block b = new Block(this, i, j, 'B');
				b.setOnMouseClicked(handler);
				hor_Y.getChildren().add(b);
			}

			vert_X.getChildren().add(hor_Y);
		}

		getChildren().add(vert_X);
	}

	public boolean startPlaceShip(int shipLength, boolean isHorizontal, int x, int y, boolean isPlayer) {
		List<Block> lstTemp = new ArrayList<>();

		System.out.println("line37:" + this.id);

		if (isValidToPlaceShip(isHorizontal, x, y, shipLength)) {
			// check for is valid coordinates
			if (isHorizontal == false) {
				System.out.println("-----Vertical----");
				for (int i = y; i < y + shipLength; i++) {

					Block blk = getBlock(x, i);
//                cell.ship = ship;
//				System.out.println("bf in startplaceship ver:" + blk.bf.id);
//					System.out.println("Block Occupied for before:" + blk.occupiedFor);
					blk.occupiedFor = 'S';
//					System.out.println("Block Occupied for after:" + blk.occupiedFor);
					lstTemp.add(blk);
					if (isPlayer) {

						blk.setFill(Color.BURLYWOOD);
						blk.setStroke(Color.CHARTREUSE);
					}
				}
				if (isPlayer)
					Main.lstAllShips_Player.add(lstTemp);
				else
					Main.lstAllShips_Computer.add(lstTemp);
			} else {
				System.out.println("-----Horizontal----");
				for (int i = x; i < x + shipLength; i++) {
					Block blk = getBlock(i, y);
//				System.out.println("bf in startplaceship hor:" + blk.bf.id);
					lstTemp.add(blk);
					blk.occupiedFor = 'S';
					if (isPlayer) {
						blk.setFill(Color.BURLYWOOD);
						blk.setStroke(Color.CHARTREUSE);
					}
				}
				if (isPlayer)
					Main.lstAllShips_Player.add(lstTemp);
				else
					Main.lstAllShips_Computer.add(lstTemp);
			}
			return true;
		}
		return false;
	}

	private boolean isValidToPlaceShip(boolean isHorizontal, int x, int y, int shipLength) {
		// TODO Auto-generated method stub
		if (isHorizontal) {
//			System.out.println("hor");
			if (x + shipLength - 1 <= 9) {
				for (int i = x; i < x + shipLength; i++) {
					Block blk = getBlock(i, y);
//					System.out.println(blk.bf.id + ":" + blk.occupiedFor);
					if (blk.occupiedFor == 'S') {
//						System.out.println("hor : found 'S'");
						return false;
					}
				}
			} else
				return false;
		} else {
			if (y + shipLength - 1 <= 9) {
				for (int i = y; i < y + shipLength; i++) {
					Block blk = getBlock(x, i);
//					System.out.println(blk.bf.id + ":" + blk.occupiedFor);
					if (blk.occupiedFor == 'S') {
//						System.out.println("ver");
						return false;
					}
//					System.out.println("i:" + i);
				}
			} else
				return false;
		}
		return true;
	}

	public Block getBlock(int x, int y) {
//		System.out.println("bf:" + this.id);
		return (Block) ((HBox) vert_X.getChildren().get(y)).getChildren().get(x);
	}

	public class Block extends Rectangle {
		public int x;
		public int y;
		private Battlefield bf;
		public char occupiedFor;

		public Block(Battlefield bf, int i, int j, char occupiedFor) {
			// TODO Auto-generated constructor stub
			super(30, 30);
			this.x = i;
			this.y = j;
			this.bf = bf;
			this.occupiedFor = occupiedFor;
			if (bf.id == 1)// player
				setFill(Color.LIGHTGRAY);
			else// computer
				setFill(Color.WHITE);
			setStroke(Color.BLACK);
		}

	}

	public static boolean isHit(Block blk) {
		// TODO Auto-generated method stub
		boolean needToShowAlert = false;
		if (blk.occupiedFor == 'S') {
			if (removeFromList(blk)) {
				needToShowAlert = true;

			}
			blk.setFill(Color.RED);
			blk.occupiedFor = 'H';
			if (allShipsSunk(blk)) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Information Dialog");
				alert.setHeaderText(null);
				if (blk.bf.id == 0)
					alert.setContentText("You won !!");
				else
					alert.setContentText("Computer won !!");

				Optional<ButtonType> result = alert.showAndWait();
				if (result.get() == ButtonType.OK) {
					System.exit(0);
				}
			}
			if (needToShowAlert) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Information Dialog");
				alert.setHeaderText(null);
				if (blk.bf.id == 0)
					alert.setContentText("you have sunk a ship : Computer's Ships Left : " + blk.bf.totalShips);
				else
					alert.setContentText("computer has sunk your ship : Your Ships Left : " + blk.bf.totalShips);
				Optional<ButtonType> result = alert.showAndWait();
				if (result.get() == ButtonType.OK) {

				}
			}
			return false;
		} else if (blk.occupiedFor == 'B') {
			blk.setFill(Color.BLACK);
			blk.occupiedFor = 'M';
		} else {
			if (blk.bf.id == 0) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Information Dialog");
				alert.setHeaderText(null);

				alert.setContentText("You already hit on the same place, Please try again on different place.");
				Optional<ButtonType> result = alert.showAndWait();
				if (result.get() == ButtonType.OK) {

				}
			}
			return false;
		}
		return true;
	}

	private static boolean allShipsSunk(Block blk) {
		// TODO Auto-generated method stub
		if (blk.bf.id == 1)// player
		{
			for (int i = 0; i < Main.lstAllShips_Player.size(); i++) {
				if (Main.lstAllShips_Player.get(i).size() > 0)
					return false;
			}
		} else// computer
		{
			for (int i = 0; i < Main.lstAllShips_Computer.size(); i++) {
				if (Main.lstAllShips_Computer.get(i).size() > 0)
					return false;
			}
		}
		return true;
	}

	private static boolean removeFromList(Block blk) {
		// TODO Auto-generated method stub
		if (blk.bf.id == 1)// Player
		{
			for (int i = 0; i < Main.lstAllShips_Player.size(); i++) {
				System.out.print("-" + Main.lstAllShips_Player.get(i).size());
			}
			System.out.println();
			f1: for (int i = 0; i < Main.lstAllShips_Player.size(); i++) {
				for (int j = 0; j < Main.lstAllShips_Player.get(i).size(); j++) {
					if (Main.lstAllShips_Player.get(i).get(j) == blk) {
						Main.lstAllShips_Player.get(i).remove(j);
						if (Main.lstAllShips_Player.get(i).size() == 0) {
							blk.bf.totalShips--;
							System.out.println("you have sunk a ship : Ships Left : " + blk.bf.totalShips);
							return true;
						}
						break f1;
					}
				}
			}
			for (int i = 0; i < Main.lstAllShips_Player.size(); i++) {
				System.out.print("-" + Main.lstAllShips_Player.get(i).size());
			}
			System.out.println();
		} else// computer
		{
			for (int i = 0; i < Main.lstAllShips_Computer.size(); i++) {
				System.out.print("-" + Main.lstAllShips_Computer.get(i).size());
			}
			System.out.println();
			f1: for (int i = 0; i < Main.lstAllShips_Computer.size(); i++) {
				for (int j = 0; j < Main.lstAllShips_Computer.get(i).size(); j++) {
					if (Main.lstAllShips_Computer.get(i).get(j) == blk) {
						Main.lstAllShips_Computer.get(i).remove(j);
						if (Main.lstAllShips_Computer.get(i).size() == 0) {
							blk.bf.totalShips--;
							System.out.println("you have sunk a ship : Ships Left : " + blk.bf.totalShips);

							return true;

						}
						break f1;
					}
				}
			}
			for (int i = 0; i < Main.lstAllShips_Computer.size(); i++) {
				System.out.print("-" + Main.lstAllShips_Computer.get(i).size());
			}
			System.out.println();
		}
		return false;

	}
}
