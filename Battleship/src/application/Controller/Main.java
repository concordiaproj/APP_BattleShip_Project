package application.Controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

import application.Models.BattleFieldComputer;
import application.Models.BattleFieldPlayer;
import application.Models.DataCoordinates;
import application.Models.Ship2;
import application.Models.ShipLatestLocation;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
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
	public static BattleFieldPlayer bfPlayer;
	public static BattleFieldComputer bfComputer;
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
	private HashMap<Node, ShipLatestLocation> mapShipLocation = new HashMap<>();

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

	/**
	 * This method id initialization point of the application.
	 * 
	 * @param url
	 * @param resourceBundle
	 */
	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		bfPlayer = new BattleFieldPlayer(gpPlayer);
		bfComputer = new BattleFieldComputer(gpComputer);
		taComputerScore.setText("00");
		taPlayerScore.setText("00");
		taShipCntComputer.setText("0");
		taShipCntPlayer.setText("0");
		setUpUI();
	}

	/**
	 * This Method will setup the UI
	 * 
	 */
	public void setUpUI() {
		startButton.disableProperty().set(true);

		currentShip = ship1;

		setPlayerGridResponding(bfPlayer);

		eventsForShips(ship1);
		eventsForShips(ship2);
		eventsForShips(ship3);
		eventsForShips(ship4);
		eventsForShips(ship5);
		for (Node node : gpComputer.getChildren()) {
			Rectangle rect = (Rectangle) node;
			rect.setDisable(true);
		}
	}

	/**
	 * This method sets up all mouse events on the ship which is passed as a
	 * parameter.
	 * 
	 * @param ndShip Ship on which all mouse event going to be set.
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
				Ship2 shipToRemove = null;
				for (Ship2 ship : bfPlayer.lstShip) {
					if (ship.getSX() == x && ship.getSY() == y) {
						shipToRemove = ship;
						for (int i = shipToRemove.getSX(); i <= shipToRemove.getEX(); i++) {
							for (int j = shipToRemove.getSY(); j <= shipToRemove.getEY(); j++) {
								bfPlayer.gameBoard.get(i).get(j).setShip(null);
							}
						}
						if (shipToRemove != null) {
							if (true) {// if (removeShip.getIsSet()) {
								if (!isRotated) {
									if (gpPlayer.contains(localX, localY)) {
										for (int i = 0; i < size && i <= 9 - x; i++) {
											Rectangle r = (Rectangle) bfPlayer.getBlockNode(x + i, y);
											r.setFill(Color.BLACK);
										}

									}
								}
								if (isRotated) {
									if (gpPlayer.contains(localX, localY)) {
										for (int i = 0; i < size && i <= 9 - y; i++) {
											Rectangle r = (Rectangle) bfPlayer.getBlockNode(x, y + i);
											r.setFill(Color.BLACK);
										}

									}
								}
							}
						}
					}
				}

				if (bfPlayer.lstShip.remove(shipToRemove)) {
					startButton.disableProperty().set(true);
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
	 * @param bfComputerParam Object of the computer grid.
	 */
	void setComputerGridResponding(BattleFieldComputer bfComputerParam) {
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
						long t = ((System.currentTimeMillis() - longTime) / 1000);
						if (t == 0)
							t = 1;
						bfPlayer.intScore += (int) (60 / t);
						taPlayerScore.clear();
						taPlayerScore.setText(bfPlayer.intScore + "");
						for (int i = 0; i < lstNodeToSelect.size(); i++) {
							GridPane g = (GridPane) lstNodeToSelect.get(i).getParent();

							int intY = GridPane.getColumnIndex(lstNodeToSelect.get(i));// y
							int intX = GridPane.getRowIndex(lstNodeToSelect.get(i));// x
							if (bfComputer.isHit(intX, intY, boolIsSalvaVariation, lstNodeToSelect.get(i), i)) {
								taShipCntComputer.clear();
								taShipCntComputer.setText(bfComputer.intTotalAliveShips + "");
								taPlayerScore.clear();
								taPlayerScore.setText(bfPlayer.intScore + "");
								return;
							}

							taShipCntComputer.clear();
							taShipCntComputer.setText(bfComputer.intTotalAliveShips + "");
							taPlayerScore.clear();
							taPlayerScore.setText(bfPlayer.intScore + "");
						}

						lstNodeToSelect.clear();
						boolIsComputerTurn = true;
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
	void setPlayerGridResponding(BattleFieldPlayer bfPlayerParam) {
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
						mapShipLocation.put(n, sll);
						if (bfPlayer.allValidPositions(mapShipLocation))
							startButton.disableProperty().set(false);
						else
							startButton.disableProperty().set(true);
						if (gpPlayer.contains(localX, localY)) {
							int x = (int) (localX / CELL_SIZE);
							int y = (int) (localY / CELL_SIZE);

							Rectangle r = (Rectangle) bfPlayer.getBlockNode(x, y);
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
	 * @param rectSlected A ship to which the border will be set.
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
	 * @param event Take the click event on 'StartGame' Button.
	 */
	@FXML
	private void onGameStart(ActionEvent event) {
//    	System.out.println("after start:"+ship1.getLayoutX());
		for (Node n : mapShipLocation.keySet()) {
			ShipLatestLocation sll = mapShipLocation.get(n);
			if (gpPlayer.contains(sll.intLocalX, sll.intLocalY)) {
				int x = (int) (sll.intLocalX / CELL_SIZE);
				int y = (int) (sll.intLocalY / CELL_SIZE);
				Rectangle r = (Rectangle) bfPlayer.getBlockNode(x, y);
				int endX = x, endY = y;
				if (!sll.boolIsRotated) {// ***
					endX = x;
					endY = y + sll.size - 1;
				} else {
					endX = x + sll.size - 1;
					endY = y;
				}
				System.out.println("x,y : endx,endy" + x + "," + y + " : " + endX + "," + endY);
				Ship2 ship = new Ship2(sll.size, x, y, endX, endY, false);
				ship.setShipCoord();
				bfPlayer.addShip(ship, sll.boolIsRotated);
				bfPlayer.lstShip.add(ship);
			}
		}
		taShipCntPlayer.clear();
		taShipCntPlayer.setText("5");
		taShipCntComputer.clear();
		taShipCntComputer.setText("5");
		System.out.println("Normal : " + rbNormal.isSelected());
		System.out.println("Salva : " + rbSalva.isSelected());
		if (rbSalva.isSelected()) {
			boolIsSalvaVariation = true;
			intNoOfTurns = 5;
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
		bfComputer.deployComputerShips();
		setComputerGridResponding(bfComputer);
		new Thread(() -> {
			try {
				makeReadyComputerToPlay();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}).start();

		startButton.disableProperty().set(true);

	}

	/**
	 * This method will make computer to play when the turn will come.
	 * 
	 * @throws InterruptedException
	 */
	private void makeReadyComputerToPlay() throws InterruptedException {
		// TODO Auto-generated method stub
		System.out.println("in ready computer" + boolIsComputerTurn);
		int x, y;
		Random rand = new Random();
		int q = 1;
		w1: while (true) {
			System.out.print("");

			if (boolIsComputerTurn) {
				Thread.sleep((rand.nextInt(2) + 1) * 1000);

				bfComputer.intScore += (int) (45 / ((System.currentTimeMillis() - longTime)) / 1000);
				taComputerScore.clear();
				taComputerScore.setText((bfComputer.intScore) + "");
				System.out.println("hi" + boolIsComputerTurn);
				List<DataCoordinates> lstBlocksToBeSelect = bfPlayer.getFromAIAlgorithm(intNoOfTurns);
				System.out.println("---" + lstBlocksToBeSelect.size());

				for (int i = 0; i < intNoOfTurns; i++) {
					lstNodeToSelect.clear();
					System.out.println("error4");
					if (lstBlocksToBeSelect.size() > 0) {
						x = lstBlocksToBeSelect.get(0).x;
						y = lstBlocksToBeSelect.get(0).y;
					} else {
						x = rand.nextInt(10);
						y = rand.nextInt(10);
					}
					System.out.println("error5:x,y" + x + "," + y);
					Rectangle r = (Rectangle) bfPlayer.getBlockNode(x, y);
					lstNodeToSelect.add(r);

					if (bfPlayer.isHit(x, y, boolIsSalvaVariation, r, 0)) {
						System.out.println("return true");
						taShipCntPlayer.clear();
						Thread.sleep(100);
						taShipCntPlayer.setText(bfPlayer.intTotalAliveShips + "");
						Thread.sleep(100);
						taComputerScore.clear();
						Thread.sleep(100);
						taComputerScore.setText(bfComputer.intScore + "");
						break;
					} else {
						if (!boolIsSalvaVariation) {
							boolIsComputerTurn = false;
						}
					}
					System.out.println("error3:size:" + lstBlocksToBeSelect.size());
					if (lstBlocksToBeSelect.size() > 0) {
						System.out.println("in if====");
						lstBlocksToBeSelect.remove(0);
					}
					System.out.println("error2");
					taShipCntPlayer.clear();
					System.out.println("line1");
					Thread.sleep(100);
					taShipCntPlayer.setText(bfPlayer.intTotalAliveShips + "");
					Thread.sleep(100);
					System.out.println("line2");
					taComputerScore.clear();
					Thread.sleep(100);
					System.out.println("line3");
					taComputerScore.setText(bfComputer.intScore + "");
					Thread.sleep(100);
					System.out.println("line4");
				}
				lstNodeToSelect.clear();
				if (boolIsSalvaVariation)
					boolIsComputerTurn = false;
				longTime = System.currentTimeMillis();
			}
		}
	}

	class Delta {
		double x;
		double y;

	}
}
