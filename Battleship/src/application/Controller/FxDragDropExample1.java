package application.Controller;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class FxDragDropExample1 extends Application {
	// Create the TextFields
	TextField sourceFld = new TextField("This is the Source Text");
	TextField targetFld = new TextField("Drag and drop the source text here");

	// Create the LoggingArea
	TextArea loggingArea = new TextArea("");

	public static void main(String[] args) {
		Application.launch(args);
	}

	@Override

	public void start(Stage stage) throws IOException {
		// Set the Size of the TextFields
		Parent parent1 = FXMLLoader.load(getClass().getResource("Untitled.fxml"));
		// Create the VBox
		Scene scene = new Scene(parent1);
		stage.setScene(scene);
		stage.setTitle("Battleship");
		stage.setResizable(false);
		stage.show();
	}

}
