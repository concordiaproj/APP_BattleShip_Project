package application;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import application.Battlefield.Block;
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

public class Main extends Application {
//	static boolean running = false;// need to change var name
	static List<List<Battlefield.Block>> lstAllShips_Player = new ArrayList<>();
	static List<List<Battlefield.Block>> lstAllShips_Computer = new ArrayList<>();
	private boolean isGameStart = false;
	private Battlefield bfComputer;
	private Battlefield bfPlayer;
	static List<Integer> shipSize = new ArrayList<>();
	static int totalShips = 5;
	static int index = 0;

	@Override
	public void start(Stage primaryStage) {
		try {
			Scene scn = new Scene(prepareUI());
			primaryStage.setTitle("Battleship");

			shipSize.add(2);
			shipSize.add(3);
			shipSize.add(3);
			shipSize.add(4);
			shipSize.add(5);

			primaryStage.setScene(scn);
			primaryStage.setResizable(false);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Parent prepareUI() {
		Random rand = new Random();
		BorderPane root = new BorderPane();
		root.setPrefSize(700, 800);
		Text txt1 = new Text(
				"Left Battlefield is yours\nRight Battlefield is opponents\nYellow Color : Ships on your battlefield\nRed Color : Successfully hit on the ship\nBlack Color : Missed the hit\n\nINSTRUCTION : \n => Do 'LEFT CLICK' to put a ship 'VERTICAL'\n => Do 'RIGHT CLICK' to put a ship 'HORIZONTAL'\n => You can put ships in this order of it's length : 2, 3, 3, 4, 5");
		txt1.setTextAlignment(TextAlignment.LEFT);
		root.setTop(txt1);
//		Label label = new Label("My Label");

//		Scene scene = new Scene(label, 200, 100);

		bfComputer = new Battlefield(0, event -> {
			if (!isGameStart)
				return;
			else {

				System.out.println("*******your turn*******");
				Block blk = (Block) event.getSource();// this will get the coordinates of mouse click
//blk.
				if (!Battlefield.isHit(blk))
					return;
				System.out.println("******Computer's turn******");
				// check is that point is valid ? is there a ship or a blank or already hit
				// point
				// if its hit or already hit point then continue; (before continue check either
				// any one ship is sunk or all the ships sunk)
				// else set the opponent turn true

				// start enemy turn
				// same as above
				while (true) {
					int x = rand.nextInt(10);
					int y = rand.nextInt(10);
					Block block = bfPlayer.getBlock(x, y);
					if (Battlefield.isHit(block))
						break;
				}
			}

		});
		bfPlayer = new Battlefield(1, event -> {
			if (isGameStart)
				return;

			Block blk = (Block) event.getSource();

			if (bfPlayer.startPlaceShip(shipSize.get(index), !(event.getButton() == MouseButton.PRIMARY), blk.y, blk.x,
					true)) {
				System.out.println("indexini" + index);

				index++;
				System.out.println("indexAfter" + index);
				if (--totalShips == 0) {
					deployeComputerShips();
				}
			}
		});
		VBox vbox = new VBox(10, bfComputer); // playerBoard
		vbox.setAlignment(Pos.CENTER_LEFT);
		root.setRight(vbox);
		VBox vbox1 = new VBox(10, bfPlayer);
		vbox1.setAlignment(Pos.CENTER_RIGHT);
		root.setLeft(vbox1);

		return root;
	}

	private void deployeComputerShips() {
		// TODO Auto-generated method stub
		Random rand = new Random();
		int x, y;
		int intFlag;
		for (int i = 0; i < shipSize.size(); i++) {
			while (true) {
				x = rand.nextInt(10);
				y = rand.nextInt(10);
				intFlag = rand.nextInt(2);

				if (bfComputer.startPlaceShip(shipSize.get(i), (intFlag == 0 ? false : true), x, y, false))
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
		isGameStart = true;
	}

	public static void main(String[] args) {
		launch(args);
	}
}
