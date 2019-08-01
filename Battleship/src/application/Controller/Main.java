package application.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import application.Models.Battlefield;
import javafx.application.Application;
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
 * @author SAHIL
 * 
 * 
 *
 */
public class Main extends Application {
//	static boolean running = false;// need to change var name
	public static List<List<Battlefield.Block>> lstAllShips_Player = new ArrayList<>();
	public static List<List<Battlefield.Block>> lstAllShips_Computer = new ArrayList<>();
	private boolean boolIsGameStart = false;
	private Battlefield bfComputer;
	private Battlefield bfPlayer;
	static List<Integer> lstShipSize = new ArrayList<>();
	static int INTTOTALSHIPS = 5;
	static int intIndex = 0;

	/**
	 * This method calls the starting of the game and shows the Stage
	 */
	@Override
	public void start(Stage stageInit) {
		try {
//			 Parent root = FXMLLoader.load(getClass().getResource("UI.fxml"));
			Scene scn = new Scene(prepareUI());
			stageInit.setTitle("Battleship");

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
		Random rand = new Random();
		BorderPane bpRoot = new BorderPane();
		bpRoot.setPrefSize(700, 800);
		Text txt1 = new Text(
				"Left Battlefield is yours\nRight Battlefield is opponents\nYellow Color : Ships on your battlefield\nRed Color : Successfully hit on the ship\nBlack Color : Missed the hit\n\nINSTRUCTION : \n => Do 'LEFT CLICK' to put a ship 'VERTICAL'\n => Do 'RIGHT CLICK' to put a ship 'HORIZONTAL'\n => You can put ships in this order of it's length : 2, 3, 3, 4, 5");
		txt1.setTextAlignment(TextAlignment.LEFT);
		bpRoot.setTop(txt1);
//		Label label = new Label("My Label");

//		Scene scene = new Scene(label, 200, 100);

		bfComputer = new Battlefield(0, event -> {
			if (!boolIsGameStart)
				return;
			else {

//				System.out.println("*******your turn*******");
				Battlefield.Block blk = (Battlefield.Block) event.getSource();
				if (!Battlefield.isHit(blk))
					return;
//				System.out.println("******Computer's turn******");

				while (true) {
					int x = rand.nextInt(10);
					int y = rand.nextInt(10);
					Battlefield.Block blkBlock = bfPlayer.getBlock(x, y);
					if (Battlefield.isHit(blkBlock))
						break;
				}
			}

		});
		bfPlayer = new Battlefield(1, event -> {
			if (boolIsGameStart)
				return;

			Battlefield.Block blk = (Battlefield.Block) event.getSource();

			if (bfPlayer.startPlaceShip(lstShipSize.get(intIndex), !(event.getButton() == MouseButton.PRIMARY), blk.y,
					blk.x, true)) {
//				System.out.println("indexini" + intIndex);

				intIndex++;
//				System.out.println("indexAfter" + intIndex);
				if (--INTTOTALSHIPS == 0) {
					deployeComputerShips();
				}
			}
		});
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
	}

	public static void main(String[] args) {
		launch(args);
	}
}
