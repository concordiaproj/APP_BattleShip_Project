/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application.Models;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * 
 * @author dhvaniagola
 *
 */
public class Battleship extends Application {
	public static Stage stage;

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		launch(args);
	}

	/**
	 * @param stage
	 * @throws Exception
	 */
	@Override
	public void start(Stage stage) throws Exception {
		this.stage = stage;
		Parent root = FXMLLoader.load(getClass().getResource("/application/View/battleship.fxml"));

		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.setTitle("Battleship");
		stage.setResizable(false);
		stage.show();

	}

}
