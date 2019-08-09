package application.Controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import application.Models.ServerData2;
import application.Models.ShipLatestLocation2;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * 
 * @author dhvaniagola
 * @author sanja
 * @author arunipatel
 * @author SAHIL
 * @author Sahana
 *
 */
public class Main2 implements Initializable {
	private double CELL_SIZE = 30.0;
	private boolean boolIsClicked = false;
	private boolean boolIsRequiredToRotate = true;
	private boolean isRotated = false;
	private Rectangle currentShip;
	public boolean boolIsComputerTurn = false;
	public boolean boolIsSalvaVariation = false;
	public int intNoOfTurns = 1;
	private long longTime = System.currentTimeMillis();
	public static List<Rectangle> lstNodeToSelect = new ArrayList<>();
	private HashMap<String, ShipLatestLocation2> mapShipLocation = new HashMap<>();
	public static String strServerIp = "132.205.93.41";
	public static int intServerPort = 1235;

	@FXML
	private Rectangle ship1;
	@FXML
	private GridPane gpPlayer;
	@FXML
	private GridPane gpComputer;
	@FXML
	private AnchorPane anchorPane;
	@FXML
	private Rectangle ship2;
	@FXML
	private Rectangle ship3;
	@FXML
	private Rectangle ship4;
	@FXML
	private Rectangle ship5;
	@FXML
	private Button startButton;
	@FXML
	private Button aboutButton;
	@FXML
	private Button discardGame;
	@FXML
	private RadioButton rbNormal;
	@FXML
	private RadioButton rbSalva;
	@FXML
	private TextArea taComputerScore;
	@FXML
	private TextArea taPlayerScore;
	@FXML
	private TextArea taShipCntComputer;
	@FXML
	private TextArea taShipCntPlayer;
	@FXML
	private TextArea taServerMessage;
	@FXML
	private Button bReload;
	@FXML
	private Button bSave;
	@FXML
	private Button bRestart;

	// login module
	@FXML
	private TextField tfUname;
	@FXML
	private TextField tfPass;
	@FXML
	private Button bGo;

	/**
	 * This method id initialization point of the application.
	 * 
	 * @param url
	 * @param resourceBundle
	 */
	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {

		startButton.disableProperty().set(true);
		bReload.disableProperty().set(true);
		bSave.disableProperty().set(true);
		bRestart.disableProperty().set(true);
		// setUpUI();
	}

	/**
	 * This Method will setup the UI
	 * 
	 */
	public void setUpUI() {
		bReload.disableProperty().set(false);
		startButton.disableProperty().set(true);

		currentShip = ship1;

		setPlayerGridResponding();
		new Thread(() -> {
			try {
				try {
					UDPListening();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}).start();
		eventsForShips(ship1);
		eventsForShips(ship2);
		eventsForShips(ship3);
		eventsForShips(ship4);
		eventsForShips(ship5);
		for (Node node : gpPlayer.getChildren()) {
			Rectangle rect = (Rectangle) node;
			rect.setDisable(true);
		}
		taPlayerScore.clear();
		taPlayerScore.setText("00");
		taComputerScore.clear();
		taComputerScore.setText("00");
		taShipCntPlayer.clear();
		taShipCntPlayer.setText("0");
		taShipCntComputer.clear();
		taShipCntComputer.setText("0");
	}

	/**
	 * This method sets up all mouse events on the ship which is passed as a
	 * parameter.
	 * 
	 * @param ndShip
	 *            Ship on which all mouse event going to be set.
	 */
	private void eventsForShips(Node ndShip) {

		ndShip.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				boolIsClicked = true;
				boolIsRequiredToRotate = true;
				Rectangle rect = (Rectangle) ndShip;
				getSelectedRectangle(rect);
				if (ndShip.rotateProperty().getValue() == 0) {
					isRotated = false;
				} else {
					isRotated = true;
				}
				double localX = ndShip.getLayoutX() - 225;
				double localY = ndShip.getLayoutY() - 334;
				if (isRotated) {
					localX = localX + currentShip.getWidth() / 2;
					localY = localY - currentShip.getWidth() / 2 + CELL_SIZE;
				}
				int size = (int) ((int) currentShip.getWidth() / CELL_SIZE);
				int x = (int) (localX / CELL_SIZE);
				int y = (int) (localY / CELL_SIZE);
				try {
					ServerData2 sd = new ServerData2(0, 9, x, y);
					sendMessageToServer(sd);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				ndShip.setCursor(Cursor.MOVE);
			}
		});
		ndShip.setOnMouseReleased(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				boolIsClicked = false;
			}
		});
		ndShip.setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				boolIsRequiredToRotate = false;
				if (ndShip.rotateProperty().getValue() == 0) {
					isRotated = false;
				} else {
					isRotated = true;
				}

				if (isRotated) {
					ndShip.setLayoutX(mouseEvent.getSceneX() - (ndShip.getBoundsInLocal().getWidth() / 2));
					ndShip.setLayoutY(mouseEvent.getSceneY() + (ndShip.getBoundsInLocal().getWidth() / 3));
				} else {
					ndShip.setLayoutX(mouseEvent.getSceneX());
					ndShip.setLayoutY(mouseEvent.getSceneY());
				}
			}
		});

		ndShip.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent mouseEvent) {
				if (boolIsRequiredToRotate) {
					if (ndShip.rotateProperty().getValue() == 0) {
						ndShip.setRotate(90.0);
					} else
						ndShip.setRotate(0);
				}
			}

		});
	}

	/**
	 * This method sets all mouse event of the computer's grid.
	 * <ul>
	 * <li>Click event after game started</li>
	 * </ul>
	 * 
	 * @param bfComputerParam
	 *            Object of the computer grid.
	 */
	void setComputerGridResponding() {// PlayerGrid
		for (Node node : gpPlayer.getChildren()) {
			Rectangle rect = (Rectangle) node;
			rect.disableProperty().set(false);

			rect.setOnMouseClicked(new EventHandler<MouseEvent>() {
				public void handle(MouseEvent event) {
					if (!boolIsComputerTurn) {
						return;
					}
					System.out.println("Clicked on comp board");

					Rectangle r = (Rectangle) event.getSource();
					if (boolIsSalvaVariation) {
						r.setFill(Color.LIGHTPINK);
					}
					lstNodeToSelect.add(r);
					System.out.println("list size : " + lstNodeToSelect.size() + ", intNo of tuerns : " + intNoOfTurns);
					if (lstNodeToSelect.size() == intNoOfTurns) {
						while (lstNodeToSelect.size() > 0) {
							GridPane g = (GridPane) lstNodeToSelect.get(0).getParent();

							int intY = GridPane.getColumnIndex(lstNodeToSelect.get(0));// y
							int intX = GridPane.getRowIndex(lstNodeToSelect.get(0));// x
							try {
								ServerData2 sd = new ServerData2(0, 5, intX, intY, boolIsSalvaVariation, 0);
								sendMessageToServer(sd);
							} catch (IOException e) {
								e.printStackTrace();
							}
							lstNodeToSelect.remove(0);
						}

						lstNodeToSelect.clear();
						ServerData2 sd = new ServerData2(0, 6, false, false);
						try {
							sendMessageToServer(sd);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						// boolIsComputerTurn = false;
						longTime = System.currentTimeMillis();
					} else
						return;
				}
			});

		}
	}

	/**
	 * This method sets all mouse event of the Player's grid.
	 * <ul>
	 * <li>Drag and drop events for placing the ships before starting a game.</li>
	 * </ul>
	 * 
	 * @param bfPlayerParam
	 */
	void setPlayerGridResponding() {// computerGrid
		anchorPane.setOnMouseDragged(new EventHandler<MouseEvent>() {
			//
			@Override
			public void handle(MouseEvent event) {

				if (!boolIsClicked)
					return;

				anchorPane.setOnMouseReleased(new EventHandler<MouseEvent>() {
					public void handle(MouseEvent event2) {
						Node n = (Node) currentShip;
						String strShipName = n.getId();
						double localY = event2.getX() - 225.0;// ***
						double localX = event2.getY() - 334.0;// ***

						int size = (int) ((int) currentShip.getWidth() / CELL_SIZE);
						ShipLatestLocation2 sll = new ShipLatestLocation2(localX, localY, size, isRotated);
						mapShipLocation.put(n.getId(), sll);
						try {
							ServerData2 sd = new ServerData2(0, 2, mapShipLocation);
							sendMessageToServer(sd);
						} catch (IOException e) {
							e.printStackTrace();
						}

						if (gpComputer.contains(localX, localY)) {
							int x = (int) (localX / CELL_SIZE);
							int y = (int) (localY / CELL_SIZE);

							Rectangle r = null;
							for (Node nd : gpComputer.getChildren()) {
								if (nd != null) {
									if (GridPane.getRowIndex(nd) == x && GridPane.getColumnIndex(nd) == y)
										r = (Rectangle) nd;
								}
							}
							int endX, endY;
							if (isRotated) {
								System.out.println("is rotated");
								currentShip.setLayoutX(r.getLayoutX() + r.getParent().getTranslateX()
										- currentShip.getWidth() / 2 + CELL_SIZE / 2);
								currentShip.setLayoutY(r.getLayoutY() + r.getParent().getTranslateY() + size * CELL_SIZE
										- currentShip.getWidth() / 2 - CELL_SIZE / 2 + 5);
								endX = x;// **
								endY = y + size - 1;// **
							} else {
								currentShip.setLayoutX(r.getLayoutX() + r.getParent().getTranslateX());
								currentShip.setLayoutY(r.getLayoutY() + r.getParent().getTranslateY() + 5);
								endX = x + size - 1;// **
								endY = y;// **
							}

						}

						anchorPane.setOnMouseReleased(null);

					}
				});
			}

		});
	}

	/**
	 * This Method set the border on selected ship.
	 * 
	 * @param rectSlected
	 *            A ship to which the border will be set.
	 * @return Returns rectangle object of ship which is currently selected.
	 */
	private Rectangle getSelectedRectangle(Rectangle rectSlected) {
		currentShip.setStroke(Color.WHITE);
		currentShip.setStrokeWidth(1.0);
		if (rectSlected != null) {
			rectSlected.setStrokeWidth(2.5);
			rectSlected.setStroke(Color.WHITE);
		}
		currentShip = rectSlected;
		return currentShip;
	}

	/**
	 * This Method will call on click on 'StartGame' button<br>
	 * This method will store all placed ship on both the players' grids.
	 * 
	 * @param event
	 *            Take the click event on 'StartGame' Button.
	 * @throws IOException
	 */
	@FXML
	private void onGameStart(ActionEvent event) throws IOException {

		for (String n : mapShipLocation.keySet()) {
			ShipLatestLocation2 sll = mapShipLocation.get(n);
			if (gpComputer.contains(sll.intLocalX, sll.intLocalY)) {
				int x = (int) (sll.intLocalX / CELL_SIZE);
				int y = (int) (sll.intLocalY / CELL_SIZE);
				Rectangle r = null;
				for (Node nd : gpComputer.getChildren()) {
					if (nd != null) {
						if (GridPane.getRowIndex(nd) == x && GridPane.getColumnIndex(nd) == y)
							r = (Rectangle) nd;
					}
				}
				int endX = x, endY = y;
				if (!sll.boolIsRotated) {// ***
					endX = x;
					endY = y + sll.size - 1;
				} else {
					endX = x + sll.size - 1;
					endY = y;
				}
				System.out.println("x,y : endx,endy" + x + "," + y + " : " + endX + "," + endY);
				ServerData2 sd = new ServerData2(0, 4, sll.size, x, y, endX, endY, false, sll.boolIsRotated);
				sendMessageToServer(sd);
			}
		}
		System.out.println("Normal : " + rbNormal.isSelected());
		System.out.println("Salva : " + rbSalva.isSelected());
		if (rbSalva.isSelected()) {
			boolIsSalvaVariation = true;
			intNoOfTurns = 5;
		}

		ServerData2 sd = new ServerData2(0, 3, boolIsSalvaVariation);
		sendMessageToServer(sd);
		taShipCntPlayer.clear();
		taShipCntPlayer.setText("5");
		taShipCntComputer.clear();
		taShipCntComputer.setText("5");

	}

	/**
	 * This method will execute while any action event performed on 'Discard Game'
	 * button.
	 * 
	 * @param event
	 *            ActionEvent Object
	 * @throws IOException
	 */
	@FXML
	private void onDiscartGame(ActionEvent event) throws IOException {
		System.out.println("To discart game");
		ServerData2 sd = new ServerData2(0, 1, true);
		sendMessageToServer(sd);
	}

	/**
	 * This method will execute while any action event performed on 'Reload' button.
	 * 
	 * @param event
	 *            ActionEvent Object
	 */
	@FXML
	private void onReloadGame(ActionEvent event) {
		ServerData2 sd = new ServerData2(0, 11);
		try {
			sendMessageToServer(sd);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * This method will execute while any action event performed on 'Save Game'
	 * button.
	 * 
	 * @param event
	 *            ActionEvent Object
	 */
	@FXML
	private void onSaveGame(ActionEvent event) {
		ServerData2 sd = new ServerData2(0, 10);
		try {
			sendMessageToServer(sd);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (Node node : anchorPane.getChildren()) {
			node.setOnMouseClicked(null);
			node.setOnMouseDragged(null);
			node.setOnMouseReleased(null);
			node.setOnMousePressed(null);
			node.setOnMouseEntered(null);
		}
		anchorPane.setOnMouseReleased(null);
		anchorPane.setOnMouseDragged(null);
		gpPlayer.disableProperty().set(true);
	}

	/**
	 * This method will execute while any action event performed on 'Restart'
	 * button.
	 * 
	 * @param event
	 *            ActionEvent Object
	 */
	@FXML
	private void onRestart(ActionEvent event) {
		ServerData2 sd = new ServerData2(0, 13);
		try {
			sendMessageToServer(sd);
			setUpUI();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * This method will execute while any action event performed on 'Go' button.
	 * 
	 * @param event
	 *            ActionEvent Object
	 * @throws IOException
	 */
	@FXML
	private void onLogin(ActionEvent event) throws IOException {
		String Uname = tfUname.getText().trim();
		String pass = tfPass.getText().trim();
		ServerData2 sd = new ServerData2(0, 12, Uname, pass);
		sendMessageToServer(sd);
		setUpUI();
	}

	/**
	 * This method continuously receiving the response from the server.
	 * 
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws InterruptedException
	 */
	private void UDPListening() throws IOException, ClassNotFoundException, InterruptedException {
		// TODO Auto-generated method stub
		DatagramSocket ds = new DatagramSocket(1236);

		DatagramPacket request = null;
		while (true) {
			byte[] buffer = new byte[1000];
			request = new DatagramPacket(buffer, buffer.length);
			ds.receive(request);
			ServerData2 ServerData2 = (ServerData2) deserialize(request.getData());
			getResponse(ServerData2);
		}
	}

	/**
	 * This method will perform a action according to response received from server.
	 * 
	 * @param ServerData2
	 *            Object of ServerData2 class
	 * @throws InterruptedException
	 */

	private void getResponse(ServerData2 ServerData2) throws InterruptedException {
		// TODO Auto-generated method stub

		int intOperationId = ServerData2.intOperationId;
		switch (intOperationId) {
		case 1:
			if (ServerData2.intPlayerId == 0) {
				System.exit(0);
			} else {
				Platform.runLater(() -> {
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Information Dialog");
					alert.setHeaderText(null);
					alert.setContentText("Other Player Want To Discart A Game");
					Optional<ButtonType> result = alert.showAndWait();
					if (result.get() == ButtonType.OK) {
						System.exit(0);
					}
				});
			}
		case 2:
			if (ServerData2.boolReturn)
				startButton.disableProperty().set(false);
			else
				startButton.disableProperty().set(true);
			break;
		case 3:
			int reply = ServerData2.intReply;
			if (reply == 1 || (reply == 4 && ServerData2.intPlayerId == 0)) {
				for (Node node : anchorPane.getChildren()) {
					node.setOnMouseClicked(null);
					node.setOnMouseDragged(null);
					node.setOnMouseReleased(null);
					node.setOnMousePressed(null);
					node.setOnMouseEntered(null);
				}
				anchorPane.setOnMouseReleased(null);
				anchorPane.setOnMouseDragged(null);
				setComputerGridResponding();

				startButton.disableProperty().set(true);
			}
			if (reply == 1) {
				taServerMessage.clear();
				taServerMessage.setText("You can Start Game");
				bSave.disableProperty().set(false);
				bRestart.disableProperty().set(false);
				//
			} else if (reply == 2 && ServerData2.intPlayerId == 0) {

				taServerMessage.clear();
				taServerMessage.setText("Other Player Want to Play on Salva");
			} else if (reply == 3 && ServerData2.intPlayerId == 0) {
				taServerMessage.clear();
				taServerMessage.setText("Other Player want to play on Normal");
			} else if (reply == 4) {
				taServerMessage.clear();
				if (ServerData2.intPlayerId == 0) {

					taServerMessage.setText("Wait for other player to be ready");
				} else {
					taServerMessage.setText("Other Player ready to start game");
				}
			}
		case 5:
			if (ServerData2.intPlayerId == 0) {
				for (Node n : gpPlayer.getChildren()) {
					if (n != null) {
						if (GridPane.getRowIndex(n) == ServerData2.x && GridPane.getColumnIndex(n) == ServerData2.y) {
							Rectangle r = (Rectangle) n;
							if (ServerData2.c == 'G')
								r.setFill(Color.GREY);
							else if (ServerData2.c == 'R')
								r.setFill(Color.RED);
						}
					}
				}
			} else {
				for (Node n : gpComputer.getChildren()) {
					if (n != null) {
						if (GridPane.getRowIndex(n) == ServerData2.x && GridPane.getColumnIndex(n) == ServerData2.y) {
							Rectangle r = (Rectangle) n;
							if (ServerData2.c == 'G')
								r.setFill(Color.GREY);
							else if (ServerData2.c == 'R')
								r.setFill(Color.RED);
						}
					}
				}
			}
			System.out.println("line1");
			taPlayerScore.clear();
			Thread.sleep(100);
			taPlayerScore.setText(ServerData2.intCompScore + "");
			Thread.sleep(100);
			taComputerScore.clear();
			Thread.sleep(100);
			taComputerScore.setText(ServerData2.intPlayerScore + "");
			Thread.sleep(100);
			taShipCntPlayer.clear();
			Thread.sleep(100);
			taShipCntPlayer.setText(ServerData2.intCompAliveShip + "");
			Thread.sleep(100);
			taShipCntComputer.clear();
			Thread.sleep(100);
			taShipCntComputer.setText(ServerData2.intPlayerAliveShip + "");
			if (ServerData2.intPlayerId == 0 && ServerData2.isWinner) {
				Platform.runLater(() -> {
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Information Dialog");
					alert.setHeaderText(null);
					alert.setContentText("You Won !!");
					Optional<ButtonType> result = alert.showAndWait();
					if (result.get() == ButtonType.OK) {
						System.exit(0);
					}
				});
			}
			if (ServerData2.intPlayerId == 1 && ServerData2.isWinner) {
				Platform.runLater(() -> {
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Information Dialog");
					alert.setHeaderText(null);
					alert.setContentText("Other Player Won !!");
					Optional<ButtonType> result = alert.showAndWait();
					if (result.get() == ButtonType.OK) {
						System.exit(0);
					}
				});
			}
			break;
		case 6:
			boolIsComputerTurn = ServerData2.isComputerTurn;
			break;
		case 10:
			if (ServerData2.intPlayerId == 0) {
				taServerMessage.clear();
				taServerMessage.setText("Game Is Saved");
			} else {
				taServerMessage.clear();
				taServerMessage.setText("Other Player Want to Save a Game. You are no longer able to play this game");
			}
			break;
		case 11:
			if (ServerData2.flag) {
				bSave.disableProperty().set(false);
				bRestart.disableProperty().set(false);
				setShipsOnLocation(ServerData2.strSelf, ServerData2.strOther);
				for (Node node : anchorPane.getChildren()) {
					node.setOnMouseClicked(null);
					node.setOnMouseDragged(null);
					node.setOnMouseReleased(null);
					node.setOnMousePressed(null);
					node.setOnMouseEntered(null);
				}
				anchorPane.setOnMouseReleased(null);
				anchorPane.setOnMouseDragged(null);
				setComputerGridResponding();
				taPlayerScore.clear();
				Thread.sleep(100);
				taPlayerScore.setText(ServerData2.intPlayerScore + "");
				Thread.sleep(100);
				taComputerScore.clear();
				Thread.sleep(100);
				taComputerScore.setText(ServerData2.intCompScore + "");
				Thread.sleep(100);
				taShipCntPlayer.clear();
				Thread.sleep(100);
				taShipCntPlayer.setText(ServerData2.intPlayerAliveShip + "");
				Thread.sleep(100);
				taShipCntComputer.clear();
				Thread.sleep(100);
				taShipCntComputer.setText(ServerData2.intCompAliveShip + "");
			} else {
				if (ServerData2.intPlayerId == 1) {
					taServerMessage.clear();
					taServerMessage.setText("No game found");
					// print message : No game found
				}
			}
			break;
		case 12:
			taServerMessage.clear();
			Thread.sleep(100);
			taServerMessage.setText("Welcome " + ServerData2.strUname + "!");
			break;
		case 13:
			taPlayerScore.clear();
			Thread.sleep(100);
			taPlayerScore.setText("00");
			Thread.sleep(100);
			taComputerScore.clear();
			Thread.sleep(100);
			taComputerScore.setText("00");
			Thread.sleep(100);
			taShipCntPlayer.clear();
			Thread.sleep(100);
			taShipCntPlayer.setText("0");
			Thread.sleep(100);
			taShipCntComputer.clear();
			Thread.sleep(100);
			taShipCntComputer.setText("0");
			Thread.sleep(100);
			taServerMessage.clear();
			if (ServerData2.intPlayerId == 1) {

				Thread.sleep(100);
				taServerMessage.setText("Other User want to restart a game");
				Platform.runLater(() -> {
					setUpUI();
				});
			}
			for (Node nd : gpPlayer.getChildren()) {
				Rectangle r = (Rectangle) nd;
				r.setFill(Color.BLACK);
			}
			for (Node nd : gpComputer.getChildren()) {
				Rectangle r = (Rectangle) nd;
				r.setFill(Color.BLACK);
			}
			break;
		}

	}

	/**
	 * This method will change the color of each block according to the presence of
	 * the ships and hits on both gridpanes
	 * 
	 * @param strS
	 *            String which contains player's own ship details
	 * @param strO
	 *            String which contains other player's ship details
	 */
	private void setShipsOnLocation(String strS, String strO) {
		// TODO Auto-generated method stub
		char cahrArrS[][] = new char[10][10];
		char cahrArrO[][] = new char[10][10];
		int p = 0;
		Rectangle r = null;
		for (int i = 0; i < 100; i++) {
			if (i % 10 == 0) {
				p = 0;
			}
			cahrArrS[i / 10][p] = strS.charAt(i);
			cahrArrO[i / 10][p] = strO.charAt(i);
			p++;
		}
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				for (Node nd : gpComputer.getChildren()) {
					if (nd != null) {
						if (GridPane.getRowIndex(nd) == i && GridPane.getColumnIndex(nd) == j) {
							r = (Rectangle) nd;
							if (cahrArrS[i][j] == 'S')
								r.setFill(Color.DEEPSKYBLUE);
							else if (cahrArrS[i][j] == 'H')
								r.setFill(Color.RED);
							else if (cahrArrS[i][j] == 'M')
								r.setFill(Color.GREY);
							else
								r.setFill(Color.BLACK);
							break;
						}
					}
				}
				for (Node nd : gpPlayer.getChildren()) {
					if (nd != null) {
						if (GridPane.getRowIndex(nd) == i && GridPane.getColumnIndex(nd) == j) {
							r = (Rectangle) nd;
							if (cahrArrO[i][j] == 'H')
								r.setFill(Color.RED);
							else if (cahrArrO[i][j] == 'M')
								r.setFill(Color.GREY);
							else
								r.setFill(Color.BLACK);
							break;
						}
					}
				}
			}
		}
	}

	class Delta {
		double x;
		double y;

	}

	/**
	 * This method will send a UDP request to the server
	 * 
	 * @param sd
	 *            Object of ServerData2 class
	 * @throws IOException
	 */

	public static void sendMessageToServer(ServerData2 sd) throws IOException {
		DatagramSocket ds = new DatagramSocket();
		InetAddress ip = InetAddress.getByName(strServerIp);
		byte[] buf = null;
		buf = (serialize(sd));
		DatagramPacket DpSend = new DatagramPacket(buf, buf.length, ip, intServerPort);

		ds.send(DpSend);

	}

	/**
	 * This method will serialize the object
	 * 
	 * @param obj
	 *            Object which need to be serialized
	 * @return Returns serialized byte array
	 * @throws IOException
	 */
	public static byte[] serialize(Object obj) throws IOException {
		try (ByteArrayOutputStream b = new ByteArrayOutputStream()) {
			try (ObjectOutputStream o = new ObjectOutputStream(b)) {
				o.writeObject(obj);
			}
			return b.toByteArray();
		}
	}

	/**
	 * This method will deserialize the byte array
	 * 
	 * @param bytes
	 *            Byte array which need to be deserialized
	 * @return Returns deserialized object
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static Object deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
		try (ByteArrayInputStream b = new ByteArrayInputStream(bytes)) {
			try (ObjectInputStream o = new ObjectInputStream(b)) {
				return o.readObject();
			}
		}
	}
}
