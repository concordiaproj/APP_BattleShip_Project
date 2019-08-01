package application.Controller;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import application.Models.BattleFieldsParent;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

/**
 *
 * @author dhvaniagola
 * @author Sahana
 * @author SAHIL Branch2.1
 * 
 *
 */
public class Main extends Application {
//	static boolean running = false;// need to change var name
	public static List<List<BattleFieldsParent.Block>> lstAllShips_Player = new ArrayList<>();
	public static List<List<BattleFieldsParent.Block>> lstAllShips_Computer = new ArrayList<>();
	public static int intPlayerStartTime = 0;
	public static int intPlayerEndTime = 0;
	private boolean boolIsGameStart = false;
	private boolean boolIsComputerTurn = false;
	private BattleFieldsParent bfComputer;
	private BattleFieldsParent bfPlayer;
	static List<Integer> lstShipSize = new ArrayList<>();
	static int INTTOTALSHIPS = 5;
	static int intIndex = 0;

	/**
	 * This method calls the starting of the game and shows the Stage
	 */
	@Override
	public void start(Stage stageInit) {
		try {
			Scene scn = new Scene(prepareUI());
			stageInit.setTitle("Battleship");
///////
			lstShipSize.add(2);
			lstShipSize.add(3);
			lstShipSize.add(3);
			lstShipSize.add(4);
			lstShipSize.add(5);

			stageInit.setScene(scn);
			stageInit.setResizable(false);
			stageInit.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method colntrolls turns of both the players and displays the grids on UI
	 * 
	 * 
	 * @return This method return a Parent object of scene class which show a view.
	 * 
	 */
	private Parent prepareUI() {
		int intNoOfTurns = 5;// set 5 for Salva variation
		List<BattleFieldsParent.Block> lstSelectedBlocks = new ArrayList<>();
		Random rand = new Random();
//		long start,end;
		BorderPane bpRoot = new BorderPane();
		bpRoot.setPrefSize(700, 800);
		Text txt1 = new Text(
				"Left Battlefield is yours\nRight Battlefield is opponents\nYellow Color : Ships on your battlefield\nRed Color : Successfully hit on the ship\nBlack Color : Missed the hit\n\nINSTRUCTION : \n => Do 'LEFT CLICK' to put a ship 'VERTICAL'\n => Do 'RIGHT CLICK' to put a ship 'HORIZONTAL'\n => You can put ships in this order of it's length : 2, 3, 3, 4, 5");
		txt1.setTextAlignment(TextAlignment.LEFT);
		bpRoot.setTop(txt1);
//		Label label = new Label("My Label");

//		Scene scene = new Scene(label, 200, 100);

		bfComputer = new BattleFieldsParent(0, 0, event -> {
//			System.out.println("bfcomputer thread start");
			if (!boolIsGameStart)
				return;
			else if (!boolIsComputerTurn) {

				System.out.println("*******your turn*******");

				BattleFieldsParent.Block blk = (BattleFieldsParent.Block) event.getSource();
				lstSelectedBlocks.add(blk);
				if (lstSelectedBlocks.size() != intNoOfTurns)
					return;
//				System.out.println("selected...");
				intPlayerEndTime = (int) System.currentTimeMillis();
				int temp = 60 / ((intPlayerEndTime - intPlayerStartTime) / 1000);
				bfPlayer.intScore += 60 / ((intPlayerEndTime - intPlayerStartTime) / 1000);
//				System.out.println("bfComputer thread : " + temp + ":your score:" + bfPlayer.intScore + "--comp score:"
//						+ bfComputer.intScore);
				for (int i = 0; i < lstSelectedBlocks.size(); i++) {
					BattleFieldsParent.isHit(lstSelectedBlocks.get(i), bfPlayer, bfComputer);

				}
				lstSelectedBlocks.clear();
				boolIsComputerTurn = true;
//				System.out.println("waiting... going to clear list");

				Point2D pd = bfPlayer.getXY();
//				System.out.println("coord:" + (int) pd.getX() + ":" + (int) pd.getY());

				try {

					Robot robot = new java.awt.Robot();

					robot.mouseMove((int) pd.getX(), (int) pd.getY());
					robot.mousePress(InputEvent.BUTTON1_MASK);
					robot.mouseRelease(InputEvent.BUTTON1_MASK);
//					robot.mouseMove((int) originalLocation.getX(), (int)originalLocation.getY());
				} catch (AWTException e) {
					e.printStackTrace();
				}

			} else
				return;
//			System.out.println("waiting...");
			long start = System.currentTimeMillis();
//			Random rand=new Random();
//			while (System.currentTimeMillis() - start <= (10 * 1000)) {
//			}
		});
		bfPlayer = new BattleFieldsParent(1, 0, event -> {
			if (boolIsComputerTurn) {
				System.out.println("******Computer's turn******");
				long start = System.currentTimeMillis();
				int intRandDelay = rand.nextInt(5) + 1;
				while (System.currentTimeMillis() - start <= (0 * 1000)) {
				}
				bfComputer.intScore += 60 / intRandDelay;
				while (lstSelectedBlocks.size() < intNoOfTurns) {
					int x, y;
//					List<Battlefield.Block> blkListToHitByAI = bfPlayer.algorithmAIChooseBlock(bfPlayer);
					if (BattleFieldsParent.stkNeighbours.size() > 0) {
						System.out.println("AI**");
						BattleFieldsParent.Block blkListToHitByAI = BattleFieldsParent.stkNeighbours.lastElement();
						lstSelectedBlocks.add(blkListToHitByAI);
						BattleFieldsParent.stkNeighbours.pop();
					} else {
						System.out.println("random**");
						x = rand.nextInt(10);
						y = rand.nextInt(10);
						BattleFieldsParent.Block blkTemp = bfPlayer.getBlock(x, y);
						if (blkTemp.occupiedFor == 'B' || blkTemp.occupiedFor == 'S') {
							lstSelectedBlocks.add(blkTemp);
						}
					}

				}
				System.out.println("size of list : " + lstSelectedBlocks.size());

				for (int i = 0; i < lstSelectedBlocks.size(); i++) {
					System.out.println("hitResult of (" + lstSelectedBlocks.get(i).x + "," + lstSelectedBlocks.get(i).y
							+ ") :" + BattleFieldsParent.isHit(lstSelectedBlocks.get(i), bfPlayer, bfComputer));
//						if (Battlefield.isHit(lstSelectedBlocks.get(i)))
//							break;
					if (lstSelectedBlocks.get(i).occupiedFor == 'H') {
						bfPlayer.addNeightboursInStack(lstSelectedBlocks.get(i));
					}
				}

				lstSelectedBlocks.clear();
				boolIsComputerTurn = false;
				intPlayerStartTime = (int) System.currentTimeMillis();
			}
			if (boolIsGameStart) {
				return;
			}

			BattleFieldsParent.Block blk = (BattleFieldsParent.Block) event.getSource();

			if (bfPlayer.startPlaceShip(lstShipSize.get(intIndex), !(event.getButton() == MouseButton.PRIMARY), blk.y,
					blk.x, true)) {
//				System.out.println("indexini" + intIndex);

				intIndex++;
//				System.out.println("indexAfter" + intIndex);
				if (--INTTOTALSHIPS == 0) {
					deployeComputerShips();
				}
			}
//			System.out.println("Thread finish");
		});
//		System.out.println("147");
		VBox vbox = new VBox(10, bfComputer);
		vbox.setAlignment(Pos.CENTER_LEFT);
		bpRoot.setRight(vbox);
		VBox vbox1 = new VBox(10, bfPlayer);
		vbox1.setAlignment(Pos.CENTER_RIGHT);
		bpRoot.setLeft(vbox1);

		return bpRoot;
	}

	/**
	 * this method deploying the ships on computer's battlefield using random
	 * selection
	 * 
	 */
	private void deployeComputerShips() {
		// TODO Auto-generated method stub
		Random rand = new Random();
		int x, y;
		int intFlag;
		for (int i = 0; i < lstShipSize.size(); i++) {
			while (true) {
				x = rand.nextInt(10);
				y = rand.nextInt(10);
				intFlag = rand.nextInt(2);

				if (bfComputer.startPlaceShip(lstShipSize.get(i), (intFlag == 0 ? false : true), x, y, false))
					break;
			}
		}
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Information Dialog");
		alert.setHeaderText(null);
		alert.setContentText("Now start a game !! Good Luck !!");
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {

		}
		boolIsGameStart = true;
		intPlayerStartTime = (int) System.currentTimeMillis();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
