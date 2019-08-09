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

//import application.Models.Data;
import application.Models.ServerData;
import application.Models.ShipLatestLocation;
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
public class Main implements Initializable {
	// public static BattleFieldPlayer bfPlayer;
	// public static BattleFieldComputer bfComputer;
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
	private HashMap<String, ShipLatestLocation> mapShipLocation = new HashMap<>();
	public static String strServerIp = "132.205.93.29";
	public static int intServerPort = 1235;

	@FXML
	private Rectangle ship1;
	@FXML
	private GridPane gpComputer;
	@FXML
	private GridPane gpPlayer;
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

	/**
	 * This method id initialization point of the application.
	 * 
	 * @param url
	 * @param resourceBundle
	 */
	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {

		// // Data dt=new Data(1,1,gpPlayer);
		// // sendMessageToServerTemp(dt);

		setUpUI();
	}

	/**
	 * This Method will setup the UI
	 * 
	 */
	public void setUpUI() {
		// Alert alert = new Alert(AlertType.INFORMATION);
		// alert.setTitle("Information Dialog");
		// alert.setHeaderText(null);
		// alert.setContentText("Good Morning");
		// Optional<ButtonType> result = alert.showAndWait();
		// if (result.get() == ButtonType.OK) {
		// // System.exit(0);
		// }
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
		for (Node node : gpComputer.getChildren()) {
			Rectangle rect = (Rectangle) node;
			rect.setDisable(true);
		}
		taPlayerScore.clear();
		// Thread.sleep(50);
		taPlayerScore.setText("00");
		// Thread.sleep(50);
		taComputerScore.clear();
		// Thread.sleep(50);
		taComputerScore.setText("00");
		// Thread.sleep(50);
		taShipCntPlayer.clear();
		// Thread.sleep(50);
		taShipCntPlayer.setText("0");
		// Thread.sleep(50);
		taShipCntComputer.clear();
		// Thread.sleep(50);
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
					// DatagramSocket ds = new DatagramSocket();
					// InetAddress ip = InetAddress.getByName(strServerIp);
					ServerData sd = new ServerData(1, 9, x, y);
					sendMessageToServer(sd);
					// byte buf[] = null;
					// buf = (serialize(sd));
					// DatagramPacket DpSend = new DatagramPacket(buf, buf.length, ip, 1236);
					//
					// ds.send(DpSend);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// Ship2 shipToRemove = null;
				// for (Ship2 ship : bfPlayer.lstShip) {
				// if (ship.getSX() == x && ship.getSY() == y) {
				// shipToRemove = ship;
				// for (int i = shipToRemove.getSX(); i <= shipToRemove.getEX(); i++) {
				// for (int j = shipToRemove.getSY(); j <= shipToRemove.getEY(); j++) {
				// bfPlayer.gameBoard.get(i).get(j).setShip(null);
				// }
				// }
				// if (shipToRemove != null) {
				// if (true) {// if (removeShip.getIsSet()) {
				// if (!isRotated) {
				// if (gpPlayer.contains(localX, localY)) {
				// for (int i = 0; i < size && i <= 9 - x; i++) {
				// Rectangle r = (Rectangle) bfPlayer.getBlockNode(x + i, y);
				// r.setFill(Color.BLACK);
				// }
				//
				// }
				// }
				// if (isRotated) {
				// if (gpPlayer.contains(localX, localY)) {
				// for (int i = 0; i < size && i <= 9 - y; i++) {
				// Rectangle r = (Rectangle) bfPlayer.getBlockNode(x, y + i);
				// r.setFill(Color.BLACK);
				// }
				//
				// }
				// }
				// }
				// }
				// }
				// }
				//
				// if (bfPlayer.lstShip.remove(shipToRemove)) {
				// startButton.disableProperty().set(true);
				// }
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
					ndShip.setLayoutY(mouseEvent.getSceneY() + (ndShip.getBoundsInLocal().getWidth() / 3)); // Not sure
																											// how
					// much sense
					// the math
					// makes here,
					// but it works
					// right.
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
	void setComputerGridResponding() {
		for (Node node : gpComputer.getChildren()) {
			Rectangle rect = (Rectangle) node;
			rect.disableProperty().set(false);

			rect.setOnMouseClicked(new EventHandler<MouseEvent>() {
				public void handle(MouseEvent event) {
					if (boolIsComputerTurn) {
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
						// long t = ((System.currentTimeMillis() - longTime) / 1000);
						// if (t == 0)
						// t = 1;
						// bfPlayer.intScore += (int) (60 / t);
						// taPlayerScore.clear();
						// taPlayerScore.setText(bfPlayer.intScore + "");
						// for (int i = 0; i < lstNodeToSelect.size(); i++) {
						while (lstNodeToSelect.size() > 0) {
							GridPane g = (GridPane) lstNodeToSelect.get(0).getParent();

							int intY = GridPane.getColumnIndex(lstNodeToSelect.get(0));// y
							int intX = GridPane.getRowIndex(lstNodeToSelect.get(0));// x
							try {
								// DatagramSocket ds = new DatagramSocket();
								// InetAddress ip = InetAddress.getByName(strServerIp);
								ServerData sd = new ServerData(1, 5, intX, intY, boolIsSalvaVariation, 0);
								sendMessageToServer(sd);
								// byte buf[] = null;
								// buf = (serialize(sd));
								// DatagramPacket DpSend = new DatagramPacket(buf, buf.length, ip, 1236);
								//
								// ds.send(DpSend);
							} catch (IOException e) {
								e.printStackTrace();
							}
							// if (bfComputer.isHit(intX, intY, boolIsSalvaVariation,
							// lstNodeToSelect.get(i), i)) {
							// taShipCntComputer.clear();
							// taShipCntComputer.setText(bfComputer.intTotalAliveShips + "");
							// taPlayerScore.clear();
							// taPlayerScore.setText(bfPlayer.intScore + "");
							// return;
							// }

							// taShipCntComputer.clear();
							// taShipCntComputer.setText(bfComputer.intTotalAliveShips + "");
							// taPlayerScore.clear();
							// taPlayerScore.setText(bfPlayer.intScore + "");
							lstNodeToSelect.remove(0);
						}

						// lstNodeToSelect.clear();
						ServerData sd = new ServerData(1, 6, true, false);
						try {
							sendMessageToServer(sd);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						// boolIsComputerTurn = true;
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
	void setPlayerGridResponding() {
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
						ShipLatestLocation sll = new ShipLatestLocation(localX, localY, size, isRotated);
						mapShipLocation.put(n.getId(), sll);
						try {
							// DatagramSocket ds = new DatagramSocket();
							// InetAddress ip = InetAddress.getByName(strServerIp);
							ServerData sd = new ServerData(1, 2, mapShipLocation);
							sendMessageToServer(sd);
							// byte buf[] = null;
							// buf = (serialize(sd));
							// DatagramPacket DpSend = new DatagramPacket(buf, buf.length, ip, 1236);
							//
							// ds.send(DpSend);
						} catch (IOException e) {
							e.printStackTrace();
						}

						// if (bfPlayer.allValidPositions(mapShipLocation))
						// startButton.disableProperty().set(false);
						// else
						// startButton.disableProperty().set(true);
						if (gpPlayer.contains(localX, localY)) {
							int x = (int) (localX / CELL_SIZE);
							int y = (int) (localY / CELL_SIZE);

							Rectangle r = null;
							for (Node nd : gpPlayer.getChildren()) {
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
		// System.out.println("after start:"+ship1.getLayoutX());

		// test();

		for (String n : mapShipLocation.keySet()) {
			ShipLatestLocation sll = mapShipLocation.get(n);
			if (gpPlayer.contains(sll.intLocalX, sll.intLocalY)) {
				int x = (int) (sll.intLocalX / CELL_SIZE);
				int y = (int) (sll.intLocalY / CELL_SIZE);
				Rectangle r = null;
				for (Node nd : gpPlayer.getChildren()) {
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
				// Ship2 ship = new Ship2(sll.size, x, y, endX, endY, false);
				// ship.setShipCoord();
				// bfPlayer.addShip(ship, sll.boolIsRotated);
				// bfPlayer.lstShip.add(ship);
				// DatagramSocket ds = new DatagramSocket();
				// InetAddress ip = InetAddress.getByName(strServerIp);
				ServerData sd = new ServerData(1, 4, sll.size, x, y, endX, endY, false, sll.boolIsRotated);
				sendMessageToServer(sd);
				// byte buf[] = null;
				// buf = (serialize(sd));
				// DatagramPacket DpSend = new DatagramPacket(buf, buf.length, ip, 1236);
				//
				// ds.send(DpSend);
			}
		}
		// taShipCntPlayer.clear();
		// taShipCntPlayer.setText("5");
		// taShipCntComputer.clear();
		// taShipCntComputer.setText("5");
		System.out.println("Normal : " + rbNormal.isSelected());
		System.out.println("Salva : " + rbSalva.isSelected());
		if (rbSalva.isSelected()) {
			boolIsSalvaVariation = true;
			intNoOfTurns = 5;
		}

		// DatagramSocket ds = new DatagramSocket();
		// InetAddress ip = InetAddress.getByName(strServerIp);
		ServerData sd = new ServerData(1, 3, boolIsSalvaVariation);
		sendMessageToServer(sd);
		taShipCntPlayer.clear();
		taShipCntPlayer.setText("5");
		taShipCntComputer.clear();
		taShipCntComputer.setText("5");
		// byte buf[] = null;
		// buf = (serialize(sd));
		// DatagramPacket DpSend = new DatagramPacket(buf, buf.length, ip, 1236);
		//
		// ds.send(DpSend);

		// for (Node node : anchorPane.getChildren()) {
		// node.setOnMouseClicked(null);
		// node.setOnMouseDragged(null);
		// node.setOnMouseReleased(null);
		// node.setOnMousePressed(null);
		// node.setOnMouseEntered(null);
		// }
		// anchorPane.setOnMouseReleased(null);
		// anchorPane.setOnMouseDragged(null);
		//// bfComputer.deployComputerShips();
		// setComputerGridResponding();
		//// new Thread(() -> {
		//// try {
		//// makeReadyComputerToPlay();
		//// } catch (InterruptedException e) {
		//// // TODO Auto-generated catch block
		//// e.printStackTrace();
		//// }
		//// }).start();
		//
		// startButton.disableProperty().set(true);

	}

	@FXML
	private void onDiscartGame(ActionEvent event) throws IOException {
		System.out.println("To discart game");
		ServerData sd = new ServerData(1, 1, true);
		sendMessageToServer(sd);
	}

	@FXML
	private void onReloadGame(ActionEvent event) {
		ServerData sd = new ServerData(1, 11);
		try {
			sendMessageToServer(sd);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@FXML
	private void onSaveGame(ActionEvent event) {
		ServerData sd = new ServerData(1, 10);
		try {
			sendMessageToServer(sd);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void test() throws IOException {
		// TODO Auto-generated method stub

		// 172.30.111.12
		// System.out.println(InetAddress.getLocalHost());
		DatagramSocket ds = new DatagramSocket();
		InetAddress ip = InetAddress.getByName(strServerIp);
		ServerData sd = new ServerData(1, 1);
		byte buf[] = null;
		buf = (serialize(sd));
		DatagramPacket DpSend = new DatagramPacket(buf, buf.length, ip, 1236);

		ds.send(DpSend);
	}

	private void UDPListening() throws IOException, ClassNotFoundException, InterruptedException {
		// TODO Auto-generated method stub
		DatagramSocket ds = new DatagramSocket(1234);

		DatagramPacket request = null;
		while (true) {
			byte[] buffer = new byte[1000];
			request = new DatagramPacket(buffer, buffer.length);
			// System.out.println("while..41");
			ds.receive(request);
			ServerData serverData = (ServerData) deserialize(request.getData());
			// System.out.println(serverData.intOperationId);
			// System.out.println(new String(buffer));
			getResponse(serverData);
		}
	}

	private void getResponse(ServerData serverData) throws InterruptedException {
		// TODO Auto-generated method stub
		System.out.println("IN GETRESPONSE");

		int intOperationId = serverData.intOperationId;
		switch (intOperationId) {
		case 1:
			if (serverData.intPlayerId == 1)
				System.exit(0);
			else {
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
			if (serverData.boolReturn)
				startButton.disableProperty().set(false);
			else
				startButton.disableProperty().set(true);
			break;
		case 3:
			int reply = serverData.intReply;
			if (reply == 1 || (reply == 4 && serverData.intPlayerId == 1)) {
				for (Node node : anchorPane.getChildren()) {
					node.setOnMouseClicked(null);
					node.setOnMouseDragged(null);
					node.setOnMouseReleased(null);
					node.setOnMousePressed(null);
					node.setOnMouseEntered(null);
				}
				anchorPane.setOnMouseReleased(null);
				anchorPane.setOnMouseDragged(null);
				// bfComputer.deployComputerShips();
				setComputerGridResponding();
				// new Thread(() -> {
				// try {
				// makeReadyComputerToPlay();
				// } catch (InterruptedException e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// }
				// }).start();

				startButton.disableProperty().set(true);
			}
			if (reply == 1) {
				taServerMessage.clear();
				taServerMessage.setText("You can Start Game");
				//
			} else if (reply == 2 && serverData.intPlayerId == 1) {

				taServerMessage.clear();
				taServerMessage.setText("Other Player Want to Play on Salva");
			} else if (reply == 3 && serverData.intPlayerId == 1) {
				taServerMessage.clear();
				taServerMessage.setText("Other Player want to play on Normal");
			} else if (reply == 4) {
				taServerMessage.clear();
				if (serverData.intPlayerId == 1) {

					taServerMessage.setText("Wait for other player to be ready");
				} else {
					taServerMessage.setText("Other Player ready to start game");
				}
			}
		case 5:
			if (serverData.intPlayerId == 1) {
				for (Node n : gpComputer.getChildren()) {
					if (n != null) {
						if (GridPane.getRowIndex(n) == serverData.x && GridPane.getColumnIndex(n) == serverData.y) {
							Rectangle r = (Rectangle) n;
							if (serverData.c == 'G')
								r.setFill(Color.GREY);
							else if (serverData.c == 'R')
								r.setFill(Color.RED);
						}
					}
				}
			} else {
				System.out.println("Computer's turn");
				for (Node n : gpPlayer.getChildren()) {
					if (n != null) {
						if (GridPane.getRowIndex(n) == serverData.x && GridPane.getColumnIndex(n) == serverData.y) {
							Rectangle r = (Rectangle) n;
							if (serverData.c == 'G')
								r.setFill(Color.GREY);
							else if (serverData.c == 'R')
								r.setFill(Color.RED);
						}
					}
				}
			}
			System.out.println("line1-P");
			taPlayerScore.clear();
			Thread.sleep(100);
			taPlayerScore.setText(serverData.intPlayerScore + "");
			Thread.sleep(100);
			taComputerScore.clear();
			Thread.sleep(100);
			taComputerScore.setText(serverData.intCompScore + "");
			Thread.sleep(100);
			taShipCntPlayer.clear();
			Thread.sleep(100);
			taShipCntPlayer.setText(serverData.intPlayerAliveShip + "");
			Thread.sleep(100);
			taShipCntComputer.clear();
			Thread.sleep(100);
			taShipCntComputer.setText(serverData.intCompAliveShip + "");

			if (serverData.intPlayerId == 1 && serverData.isWinner) {
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
			if (serverData.intPlayerId == 0 && serverData.isWinner) {
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
			// gpComputer.rege
			// System.out.println( "Restarting app!" );
			// Battleship.stage.close();
			// Platform.runLater( () -> new Battleship.start( new Stage() ) );
			break;
		case 6:
			boolIsComputerTurn = serverData.isComputerTurn;
			break;
		case 10:
			System.out.println("case 10 Main");
			if (serverData.intPlayerId == 1) {
				taServerMessage.clear();
				taServerMessage.setText("Game Is Saved");
			} else {
				taServerMessage.clear();
				taServerMessage.setText("Other Player Want to Save a Game. You are no longer able to play this game");
			}
			break;
		case 11:
			if (serverData.flag) {
				setShipsOnLocation(serverData.strSelf, serverData.strOther);
				for (Node node : anchorPane.getChildren()) {
					node.setOnMouseClicked(null);
					node.setOnMouseDragged(null);
					node.setOnMouseReleased(null);
					node.setOnMousePressed(null);
					node.setOnMouseEntered(null);
				}
				anchorPane.setOnMouseReleased(null);
				anchorPane.setOnMouseDragged(null);
				// bfComputer.deployComputerShips();
				setComputerGridResponding();
				taPlayerScore.clear();
				Thread.sleep(100);
				taPlayerScore.setText(serverData.intPlayerScore + "");
				Thread.sleep(100);
				taComputerScore.clear();
				Thread.sleep(100);
				taComputerScore.setText(serverData.intCompScore + "");
				Thread.sleep(100);
				taShipCntPlayer.clear();
				Thread.sleep(100);
				taShipCntPlayer.setText(serverData.intPlayerAliveShip + "");
				Thread.sleep(100);
				taShipCntComputer.clear();
				Thread.sleep(100);
				taShipCntComputer.setText(serverData.intCompAliveShip + "");

			} else {
				if (serverData.intPlayerId == 1) {
					taServerMessage.clear();
					taServerMessage.setText("No game found");
					// print message : No game found
				}
			}
			break;
		}

	}

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
				for (Node nd : gpPlayer.getChildren()) {
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
				for (Node nd : gpComputer.getChildren()) {
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

	public static void sendMessageToServer(ServerData sd) throws IOException {
		DatagramSocket ds = new DatagramSocket();
		System.out.println("going to send to id : " + sd.intOperationId);
		InetAddress ip = InetAddress.getByName(strServerIp);
		byte buf[] = null;
		buf = (serialize(sd));
		DatagramPacket DpSend = new DatagramPacket(buf, buf.length, ip, intServerPort);

		ds.send(DpSend);

	}

	public static void sendMessageToServerTemp(ServerData sd) throws IOException {
		DatagramSocket ds = new DatagramSocket();
		System.out.println("going to send to id : ");
		InetAddress ip = InetAddress.getByName(strServerIp);
		byte buf[] = null;
		buf = (serialize(sd));
		DatagramPacket DpSend = new DatagramPacket(buf, buf.length, ip, intServerPort);

		ds.send(DpSend);

	}

	public static byte[] serialize(Object obj) throws IOException {
		try (ByteArrayOutputStream b = new ByteArrayOutputStream()) {
			try (ObjectOutputStream o = new ObjectOutputStream(b)) {
				o.writeObject(obj);
			}
			return b.toByteArray();
		}
	}

	public static Object deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
		try (ByteArrayInputStream b = new ByteArrayInputStream(bytes)) {
			try (ObjectInputStream o = new ObjectInputStream(b)) {
				return o.readObject();
			}
		}
	}
}
