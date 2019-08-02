package application.Models;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * @author pg3f4
 */
public class Battleship extends Application {
	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("/application/View/battleship.fxml"));

		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.setTitle("Battleship");
		stage.setResizable(false);
		stage.show();
	}
}
