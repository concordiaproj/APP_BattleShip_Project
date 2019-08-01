package application.Models;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Stack;

import application.Controller.Main;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * @author sanja
 * @author arunipatel
 * @author dhvaniagola
 * @author SAHIL
 * @author Sahana
 * 
 * 
 * 
 *         This class Manage all actions done on battlefields of both the
 *         players.
 *         <ul>
 *         <li>Ship placing on both battlefields with validations</li>
 *         <li>Manage all hits done by both the players</li>
 *         <li>Displaying all informative messages</li>
 *         </ul>
 *
 */
public class BattleFieldsParent extends Parent {
	private VBox vboxVert_X = new VBox();
	private static int SCORE_ON_HIT = 50;
	private static Stack<Block> stkBlockLastHit = new Stack<>();
	public static Stack<Block> stkNeighbours = new Stack<>();
	int id;
	public int intScore;
	private int intTotalShips = 5;

	/**
	 * Creating grid for each players
	 * 
	 * @param id      This is the id for each player
	 * @param handler This is Mouse event handler
	 */
	public BattleFieldsParent(int id, int intScore, EventHandler<? super MouseEvent> handler) {
		// TODO Auto-generated constructor stub
//		this.enemy = enemy;
		this.id = id;
		this.intScore = intScore;
		for (int i = 0; i < 10; i++) {
			HBox hboxHor_Y = new HBox();
			for (int j = 0; j < 10; j++) {
				Block blkTemp = new Block(this, i, j, 'B');
				blkTemp.setOnMouseClicked(handler);
				hboxHor_Y.getChildren().add(blkTemp);
			}

			vboxVert_X.getChildren().add(hboxHor_Y);
		}

		getChildren().add(vboxVert_X);
	}

	public Point2D getXY() {
		Block blk = getBlock(0, 0);
		Point2D pd = blk.localToScreen(0, 0);
		return pd;
	}

	/**
	 * 
	 * @param intShipLength    Length of a ship which going to be placed
	 * @param boolIsHorizontal Checking the ship is horizontal or vertical which is
	 *                         going to be placed
	 * @param x                x-coordinate where ship is going to be placed
	 * @param y                y-coordinate where ship is going to be placed
	 * @param boolIsPlayer     Checking is the player is a computer or a human
	 * @return Returns true if ship placed successfully else returns false.
	 */
	public boolean startPlaceShip(int intShipLength, boolean boolIsHorizontal, int x, int y, boolean boolIsPlayer) {
		List<Block> lstTemp = new ArrayList<>();

//		System.out.println("line37:" + this.id);

		if (isValidToPlaceShip(boolIsHorizontal, x, y, intShipLength)) {
			// check for is valid coordinates
			if (boolIsHorizontal == false) {
//				System.out.println("-----Vertical----");
				for (int i = y; i < y + intShipLength; i++) {

					Block blk = getBlock(x, i);
//                cell.ship = ship;
//				System.out.println("bf in startplaceship ver:" + blk.bf.id);
//					System.out.println("Block Occupied for before:" + blk.occupiedFor);
					blk.occupiedFor = 'S';
//					System.out.println("Block Occupied for after:" + blk.occupiedFor);
					lstTemp.add(blk);
					if (boolIsPlayer) {

						blk.setFill(Color.BURLYWOOD);
						blk.setStroke(Color.CHARTREUSE);
					}
				}
				if (boolIsPlayer)
					Main.lstAllShips_Player.add(lstTemp);
				else
					Main.lstAllShips_Computer.add(lstTemp);
			} else {
//				System.out.println("-----Horizontal----");
				for (int i = x; i < x + intShipLength; i++) {
					Block blk = getBlock(i, y);
//				System.out.println("bf in startplaceship hor:" + blk.bf.id);
					lstTemp.add(blk);
					blk.occupiedFor = 'S';
					if (boolIsPlayer) {
						blk.setFill(Color.BURLYWOOD);
						blk.setStroke(Color.CHARTREUSE);
					}
				}
				if (boolIsPlayer)
					Main.lstAllShips_Player.add(lstTemp);
				else
					Main.lstAllShips_Computer.add(lstTemp);
			}
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @param boolIsHorizontal Checking the ship is horizontal or vertical which is
	 *                         going to be placed
	 * @param x                x-coordinate where ship is going to be placed
	 * @param y                y-coordinate where ship is going to be placed
	 * @param intShipLength    Length of a ship which going to be placed
	 * @return Returns true if placing a ship at (x,y) is valid coordinate to place
	 *         a ship
	 * 
	 *         This Method is checking that the whole ship should be on blank place
	 *         and should be inside of the field.
	 */
	private boolean isValidToPlaceShip(boolean boolIsHorizontal, int x, int y, int intShipLength) {
		// TODO Auto-generated method stub
		if (boolIsHorizontal) {
//			System.out.println("hor");
			if (x + intShipLength - 1 <= 9) {
				for (int i = x; i < x + intShipLength; i++) {
					///
					if (!isValidPoint(i, y))
						return false;
					Block blk = getBlock(i, y);
//					System.out.println(blk.bf.id + ":" + blk.occupiedFor);
					if (blk.occupiedFor == 'S') {
//						System.out.println("hor : found 'S'");
						return false;
					}
					if (getNeighbors(i, y)) {
						return true;
					} else
						return false;

				}
			} else
				return false;
		} else {
			if (y + intShipLength - 1 <= 9) {
				for (int i = y; i < y + intShipLength; i++) {

					if (!isValidPoint(x, i))
						return false;
					Block blk = getBlock(x, i);
					if (blk.occupiedFor == 'S') {
						return false;
					}
//					for (Block neighbor : getNeighbors(i, y)) {
//	                    if (!isValidPoint(i, y))
//	                        return false;
//
//	                    if (neighbor.ship != null)
//	                        return false;
//	                }
					if (getNeighbors(x, i)) {
						return true;
					} else
						return false;
//					System.out.println("i:" + i);
				}
			} else
				return false;
		}
		return true;
	}

	/**
	 * 
	 * @param x x-coordinate of a block
	 * @param y y-coordinate of a block
	 * @return Returns Block object associated with (x,y) coordinates
	 * 
	 *         This method gets x and y coordinates and returns Block of that
	 *         coordinate
	 */
	public Block getBlock(int x, int y) {
//		System.out.println("bf:" + this.id);
		return (Block) ((HBox) vboxVert_X.getChildren().get(y)).getChildren().get(x);
	}

	private boolean getNeighbors(int x, int y) {
//        Point2D[] points = new Point2D[] {
//                new Point2D(x - 1, y),
//                new Point2D(x + 1, y),
//                new Point2D(x, y - 1),
//                new Point2D(x, y + 1)
//        };
//
		System.out.println("x:" + x + ",y:" + y);
		List<Block> neighbors = new ArrayList<Block>();
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				if (i != 0 || j != 0) {
					if ((x + i) >= 0 && (x + i) < 10 && (y + j) >= 0 && (y + j) < 10) {
						neighbors.add(getBlock(x + i, y + j));
						System.out.println((x + i) + "," + (y + j));
					}
				}
			}
		}
		for (int i = 0; i < neighbors.size(); i++) {
			if (neighbors.get(i).occupiedFor == 'S') {
				System.out.println("return : false");
				return false;
			}
		}
		System.out.println("return : true");
		return true;
	}

	private boolean isValidPoint(double x, double y) {
		return x >= 0 && x < 10 && y >= 0 && y < 10;
	}

	/**
	 * 
	 * This class create objects for each block in both the grids
	 * 
	 * @author dhvaniagola
	 *
	 */
	public class Block extends Rectangle {
		public Object ship;
		public int x;
		public int y;
		private BattleFieldsParent bf;
		public char occupiedFor;

		/**
		 * 
		 * @param bf              BattleField object which define the player
		 * @param i               x-coordinate of a block
		 * @param j               y-coordinate of a block
		 * @param charOccupiedFor which shows that block is blank, hit or missed
		 * 
		 *                        It will set the color of the block initially
		 */
		public Block(BattleFieldsParent bf, int i, int j, char charOccupiedFor) {
			// TODO Auto-generated constructor stub
			super(30, 30);
			this.x = i;
			this.y = j;
			this.bf = bf;
			this.occupiedFor = charOccupiedFor;
			if (bf.id == 1)// player
				setFill(Color.LIGHTGRAY);
			else// computer
				setFill(Color.WHITE);
			setStroke(Color.BLACK);
		}

	}

	/**
	 * 
	 * @param blk Block object to which player hit during it's turn
	 * @return Returns true if player missed the hit and no need to take turn again,
	 *         and returns false if need to take a turn again
	 * 
	 *         This Method checks the hit is misses or not
	 */
	synchronized public static boolean isHit(Block blk, BattleFieldsParent bfPlayer, BattleFieldsParent bfComputer) {
		// TODO Auto-generated method stub
		boolean boolNeedToShowAlert = false;
		if (blk.occupiedFor == 'S') {
			System.out.println("hit...");
			if (removeFromList(blk)) {
				boolNeedToShowAlert = true;

			}
			blk.setFill(Color.RED);
			blk.occupiedFor = 'H';
			if (blk.bf.id == 0)
				bfPlayer.intScore += SCORE_ON_HIT;
			else
				bfComputer.intScore += SCORE_ON_HIT;
//			if (blk.bf.id == 1) {
//				stkBlockLastHit.push(blk);
////				addNeightboursInStack(blk);
//				System.out.println("added in stack : (" + blk.x + "," + blk.y + ")");
//			}
			System.out.println("in isHit : your score:" + bfPlayer.intScore + "--comp score:" + bfComputer.intScore);
			if (allShipsSunk(blk)) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Information Dialog");
				alert.setHeaderText(null);
				if (blk.bf.id == 0)
					alert.setContentText("You won !!\nYour Score : " + bfPlayer.intScore + "\nComputer's Score : "
							+ bfComputer.intScore);
				else
					alert.setContentText("Computer won !!\nYour Score : " + bfPlayer.intScore + "\nComputer's Score : "
							+ bfComputer.intScore);

				Optional<ButtonType> result = alert.showAndWait();
				if (result.get() == ButtonType.OK) {
					System.exit(0);
				}
			}
			if (boolNeedToShowAlert) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Information Dialog");
				alert.setHeaderText(null);
				if (blk.bf.id == 0)
					alert.setContentText("you have sunk a ship : Computer's Ships Left : " + blk.bf.intTotalShips);
				else
					alert.setContentText("computer has sunk your ship : Your Ships Left : " + blk.bf.intTotalShips);
				Optional<ButtonType> result = alert.showAndWait();
				if (result.get() == ButtonType.OK) {

				}
			}
			return false;
		} else if (blk.occupiedFor == 'B') {
			blk.setFill(Color.BLACK);
			blk.occupiedFor = 'M';
		} else {
			System.out.println("hit on hit");
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

	public void addNeightboursInStack(Block blk) {
		// TODO Auto-generated method stub
		int x = blk.x;
		int y = blk.y;
		if ((x - 1) >= 0 && (x - 1) < 10) {
			if (getBlock(x - 1, y).occupiedFor == 'B' || getBlock(x - 1, y).occupiedFor == 'S') {
				System.out.println("Added:" + (x - 1) + "," + y);
				stkNeighbours.add(getBlock(x - 1, y));
			}
		}
		if ((x + 1) >= 0 && (x + 1) < 10) {
			if (getBlock(x + 1, y).occupiedFor == 'B' || getBlock(x + 1, y).occupiedFor == 'S') {
				System.out.println("Added:" + (x + 1) + "," + y);
				stkNeighbours.add(getBlock(x + 1, y));
			}

		}
		if ((y - 1) >= 0 && (y - 1) < 10) {
			if (getBlock(x, y - 1).occupiedFor == 'B' || getBlock(x, y - 1).occupiedFor == 'S') {
				System.out.println("Added:" + x + "," + (y - 1));
				stkNeighbours.add(getBlock(x, y - 1));
			}
		}
		if ((y + 1) >= 0 && (y + 1) < 10) {
			if (getBlock(x, y + 1).occupiedFor == 'B' || getBlock(x, y + 1).occupiedFor == 'S') {
				System.out.println("Added:" + x + "," + (y + 1));
				stkNeighbours.add(getBlock(x, y + 1));
			}
		}

	}

	/**
	 * 
	 * @param blk Block object to which player hit during it's turn
	 * @return Returns true if all ships are sunk else returns false
	 * 
	 *         This method checks if all ships sunk or not
	 */
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

	/**
	 * 
	 * @param blk Block object to which player hit during it's turn
	 * @return Returns true if any ship sunk else returns false
	 * 
	 *         This method checks any ship sunk or not and keep counts of alive
	 *         ships
	 */
	private static boolean removeFromList(Block blk) {
		// TODO Auto-generated method stub
		if (blk.bf.id == 1)// Player
		{
			f1: for (int i = 0; i < Main.lstAllShips_Player.size(); i++) {
				for (int j = 0; j < Main.lstAllShips_Player.get(i).size(); j++) {
					if (Main.lstAllShips_Player.get(i).get(j) == blk) {
						Main.lstAllShips_Player.get(i).remove(j);
						if (Main.lstAllShips_Player.get(i).size() == 0) {
							blk.bf.intTotalShips--;
							return true;
						}
						break f1;
					}
				}
			}
		} else// computer
		{
//			
			f1: for (int i = 0; i < Main.lstAllShips_Computer.size(); i++) {
				for (int j = 0; j < Main.lstAllShips_Computer.get(i).size(); j++) {
					if (Main.lstAllShips_Computer.get(i).get(j) == blk) {
						Main.lstAllShips_Computer.get(i).remove(j);
						if (Main.lstAllShips_Computer.get(i).size() == 0) {
							blk.bf.intTotalShips--;
							return true;

						}
						break f1;
					}
				}
			}
		}
		return false;

	}

//	public List<Block> algorithmAIChooseBlock(Battlefield bfPlayer) {
//		// TODO Auto-generated method stub
//		Block blkSelected;
//		Random rand = new Random();
//		System.out.println("stack size : " + stkBlockLastHit.size());
//		if (stkBlockLastHit.size() > 0) {
//			Block blkLastHit = stkBlockLastHit.lastElement();
//
//			System.out.println("block in stack : " + blkLastHit.occupiedFor + ": " + blkLastHit.x + "," + blkLastHit.y);
//			int x = blkLastHit.x;
//			int y = blkLastHit.y;
//			List<Block> blkListTemp = new ArrayList<>();
//			if (getBlock(x - 1, y).occupiedFor == 'B' || getBlock(x - 1, y).occupiedFor == 'S') {
//				System.out.println("added:" + (x - 1) + "," + y);
//				Block blk = getBlock(x - 1, y);
//				blkListTemp.add(blk);
//			}
//			if (getBlock(x + 1, y).occupiedFor == 'B' || getBlock(x + 1, y).occupiedFor == 'S') {
//				System.out.println("added:" + (x + 1) + "," + y);
//				Block blk = getBlock(x + 1, y);
//				blkListTemp.add(blk);
//			}
//			if (getBlock(x, y - 1).occupiedFor == 'B' || getBlock(x, y - 1).occupiedFor == 'S') {
//				System.out.println("added:" + x + "," + (y - 1));
//				Block blk = getBlock(x, y - 1);
//				blkListTemp.add(blk);
//			}
//			if (getBlock(x, y + 1).occupiedFor == 'B' || getBlock(x, y + 1).occupiedFor == 'S') {
//				System.out.println("added:" + x + "," + (y - 1));
//				Block blk = getBlock(x, y + 1);
//				blkListTemp.add(blk);
//			}
//			if (blkListTemp.size() == 0) {
////				blkSelected = blkListTemp.get(rand.nextInt(blkListTemp.size()));
//				stkBlockLastHit.pop();
//
//			}
//			return blkListTemp;
//		}
//
//		return null;
//	}
}
