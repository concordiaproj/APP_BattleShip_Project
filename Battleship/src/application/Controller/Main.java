package application.Controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.RadioButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

/**
 * FXML Controller class
 *
 */
public class Main implements Initializable {
	BattleFieldPlayer bfPlayer;
	BattleFieldComputer bfComputer;
	private double cellSize = 30.0;
	private boolean clicked = false;
	private boolean needToRotate = true;
	private boolean isRotated = false;
	private Rectangle selectedShip;
	public boolean boolIsComputerTurn = false;
	public boolean boolIsSalvaVariation = false;
	public int intNoOfTurns = 1;
	private long time = System.currentTimeMillis();
	List<Rectangle> lstNodeToSelect = new ArrayList<>();
	private HashMap<Node, ShipLatestLocation> mapShipLocation = new HashMap<>();

	@FXML
	private Rectangle ship1;
	@FXML
	private GridPane gpComputer;
	@FXML
	private GridPane gpPlayer;
	@FXML
	private AnchorPane anchor;
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

	/**
	 * Initializes the controller class.
	 *
	
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {

		bfPlayer = new BattleFieldPlayer(gpPlayer);
		bfComputer = new BattleFieldComputer(gpComputer);
		reset();
	}

	/**
	 * Puts game into Set-up state
	 */
	public void reset() {
		startButton.disableProperty().set(true);

		selectedShip = ship1;

		installPlayerBoardListeners(bfPlayer);

		// Set-up boats, place them in the right spot
		installBoatListeners(ship1);
		installBoatListeners(ship2);
		installBoatListeners(ship3);
		installBoatListeners(ship4);
		installBoatListeners(ship5);
		for (Node node : gpComputer.getChildren()) {
			Rectangle rect = (Rectangle) node;
			rect.setDisable(true);
		}

//		ai.reset();
	}

	/**
	 * Install listeners on rectangles to install drag/drop, interact with
	 * displayBoard, etc.
	 *
	 * @param boat the Shape that the boat listeners are being installed on
	 */
	private void installBoatListeners(Node boat) {

		// Handle dragging, using help from
		// http://stackoverflow.com/questions/22139615/dragging-buttons-in-javafx
		boat.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				clicked = true;
				needToRotate = true;
				Rectangle rect = (Rectangle) boat;
				select(rect);
				if (boat.rotateProperty().getValue() == 0) {
					isRotated = false;
				} else {
					isRotated = true;
				}
				double localX = boat.getLayoutX() - 225;
				double localY = boat.getLayoutY() - 334;
				if (isRotated) {
					localX = localX + selectedShip.getWidth() / 2;
					localY = localY - selectedShip.getWidth() / 2 + cellSize;
				}
				int size = (int) ((int) selectedShip.getWidth() / cellSize);
				int x = (int) (localX / cellSize);
				int y = (int) (localY / cellSize);
				Ship2 removeShip = null;
				for (Ship2 ship : bfPlayer.lstShip) {
					if (ship.getSX() == x && ship.getSY() == y) {
						removeShip = ship;
						for (int x2 = removeShip.getSX(); x2 <= removeShip.getEX(); x2++) {
							for (int y2 = removeShip.getSY(); y2 <= removeShip.getEY(); y2++) {
								bfPlayer.gameBoard.get(x2).get(y2).setShip(null);
							}
						}
						if (removeShip != null) {
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

				if (bfPlayer.lstShip.remove(removeShip)) {
					startButton.disableProperty().set(true);
				}
				boat.setCursor(Cursor.MOVE);
			}
		});
		boat.setOnMouseReleased(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				clicked = false;
//                boat.setCursor(Cursor.HAND);
			}
		});
		boat.setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				needToRotate = false;
				if (boat.rotateProperty().getValue() == 0) {
					isRotated = false;
				} else {
					isRotated = true;
				}

				if (isRotated) {
					boat.setLayoutX(mouseEvent.getSceneX() - (boat.getBoundsInLocal().getWidth() / 2));
					boat.setLayoutY(mouseEvent.getSceneY() + (boat.getBoundsInLocal().getWidth() / 3)); 
				} else {
					boat.setLayoutX(mouseEvent.getSceneX());
					boat.setLayoutY(mouseEvent.getSceneY());
				}
			}
		});
		boat.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {

			}
		});
		boat.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent mouseEvent) {

				int x, y;
				double localX, localY;
				boolean isRotated;
				if (needToRotate) {
					 
					if (boat.rotateProperty().getValue() == 0) {
						boat.setRotate(90.0);
					} else
						boat.setRotate(0);
				}
// 
			}

		});
	}

	/**
	 * Install listeners on pcBoard rectangles to allow for a way to interact with
	 * the PC game board through the PC display board
	 */
	void installPCBoardListeners(BattleFieldComputer bfComputerParam) {
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
						bfPlayer.intScore += (int) (60 / ((System.currentTimeMillis() - time) / 1000));
						boolean isNeedToBeDisplay = false;
						for (int i = 0; i < lstNodeToSelect.size(); i++) {
							GridPane g = (GridPane) lstNodeToSelect.get(i).getParent();

							int intY = GridPane.getColumnIndex(lstNodeToSelect.get(i));// y
							int intX = GridPane.getRowIndex(lstNodeToSelect.get(i));// x
							if (bfComputer.gameBoard.get(intX).get(intY).getCharOccupiedFor() == 'M'
									|| bfComputer.gameBoard.get(intX).get(intY).getCharOccupiedFor() == 'H') {
								if(bfComputer.gameBoard.get(intX).get(intY).getCharOccupiedFor() == 'M') {
									lstNodeToSelect.get(i).setFill(Color.GREY);
								}
								else {
									lstNodeToSelect.get(i).setFill(Color.RED);
								}
								if (!boolIsSalvaVariation) {
									System.out.println("going to clear 1");
									lstNodeToSelect.clear();
									return;
								}
							} else if (bfComputer.gameBoard.get(intX).get(intY).getCharOccupiedFor() == 'S') {
								bfPlayer.intScore += 50;
								bfComputer.gameBoard.get(intX).get(intY).setCharOccupiedFor('H');
								lstNodeToSelect.get(i).setFill(Color.RED);
								if (bfComputer.isShipDestroyed(intX, intY)) {
									bfComputer.intTotalAliveShips--;
									bfPlayer.intScore += 20;
									if (bfComputer.intTotalAliveShips == 0) {
										Alert alert = new Alert(AlertType.INFORMATION);
										alert.setTitle("Information Dialog");
										alert.setHeaderText(null);
										alert.setContentText("You Win!!\nYour Score : " + bfPlayer.intScore
												+ "\nComputer's Score : " + bfComputer.intScore);
										Optional<ButtonType> result = alert.showAndWait();
										if (result.get() == ButtonType.OK) {
											System.exit(0);
										}
										System.out.println("You Win!!\nYour Score : " + bfPlayer.intScore
												+ "\nComputer's Score : " + bfPlayer.intScore);
									} else {
										if (boolIsSalvaVariation)
											isNeedToBeDisplay = true;
										else {
											System.out.println("You have destroyed a ship of computer !! Ships Left : "
													+ bfComputer.intTotalAliveShips);
										}
									}
								}
								System.out.println("going to clear 2");
								if (!boolIsSalvaVariation) {
									lstNodeToSelect.clear();
									return;
								}
							}

							else if (bfComputer.gameBoard.get(intX).get(intY).getCharOccupiedFor() == 'B') {
								bfComputer.gameBoard.get(intX).get(intY).setCharOccupiedFor('M');
								lstNodeToSelect.get(i).setFill(Color.GREY);
							}
						}
						if (isNeedToBeDisplay)
							System.out.println("You have destroyed ship(s) of computer !! Ships Left : "
									+ bfComputer.intTotalAliveShips);
						System.out.println("going to clear 3");
						lstNodeToSelect.clear();
						boolIsComputerTurn = true;
						time = System.currentTimeMillis();
					} else
						return;
				}
			});

		}
	}

	/**
	 * Install listeners on playerBoard to create a bridge between player display
	 * and game boards
	 */
	void installPlayerBoardListeners(BattleFieldPlayer bfPlayerParam) {
		anchor.setOnMouseDragged(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
//            	System.out.println("Dragged");

				if (!clicked)
					return;

				anchor.setOnMouseReleased(new EventHandler<MouseEvent>() {
					public void handle(MouseEvent event2) {
						Node n = (Node) selectedShip;
						String strShipName = n.getId();
//                    	System.out.println("before:"+ship1.getLayoutX()+n.getId());
						// Adjust pointer to make up for Grid's offset
						double localY = event2.getX() - 225.0;// ***
						double localX = event2.getY() - 334.0;// ***

						int size = (int) ((int) selectedShip.getWidth() / cellSize);
						ShipLatestLocation sll = new ShipLatestLocation(localX, localY, size, isRotated);
						mapShipLocation.put(n, sll);
						if (bfPlayer.allValidPositions(mapShipLocation))
							startButton.disableProperty().set(false);
						else
							startButton.disableProperty().set(true);
						if (gpPlayer.contains(localX, localY)) {
							int x = (int) (localX / cellSize);
							int y = (int) (localY / cellSize);

							Rectangle r = (Rectangle) bfPlayer.getBlockNode(x, y);
							int endX, endY;
							if (isRotated) {
								System.out.println("is rotated");
								selectedShip.setLayoutX(r.getLayoutX() + r.getParent().getTranslateX()
										- selectedShip.getWidth() / 2 + cellSize / 2);
								selectedShip.setLayoutY(r.getLayoutY() + r.getParent().getTranslateY() + size * cellSize
										- selectedShip.getWidth() / 2 - cellSize / 2 + 5);
								endX = x;
								endY = y + size - 1;
							} else {
								selectedShip.setLayoutX(r.getLayoutX() + r.getParent().getTranslateX());
								selectedShip.setLayoutY(r.getLayoutY() + r.getParent().getTranslateY() + 5);
								endX = x + size - 1;
								endY = y;
							}

//                          

						}

						anchor.setOnMouseReleased(null);

					}
				});
			}

		});
	}



	/**
	 * Highlights a boat
	 *
	 * @param boat the boat to select
	 * @return
	 */
	private Rectangle select(Rectangle boat) {
		selectedShip.setStroke(Color.WHITE);
		selectedShip.setStrokeWidth(1.0);
		if (boat != null) {
			boat.setStrokeWidth(2.5);
			boat.setStroke(Color.WHITE);
		}
		selectedShip = boat;
		return selectedShip;
	}

	/**
	 * Changes application from set-up state to game state
	 *
	 * @param event
	 */
	@FXML
	private void startGame(ActionEvent event) {
//    	System.out.println("after start:"+ship1.getLayoutX());
		for (Node n : mapShipLocation.keySet()) {
			ShipLatestLocation sll = mapShipLocation.get(n);
			if (gpPlayer.contains(sll.intLocalX, sll.intLocalY)) {
				int x = (int) (sll.intLocalX / cellSize);
				int y = (int) (sll.intLocalY / cellSize);
				Rectangle r = (Rectangle) bfPlayer.getBlockNode(x, y);
				int endX = x, endY = y;
				if (!sll.boolIsRotated) {// ***
//                     selectedShip.setLayoutX(r.getLayoutX() + r.getParent().getTranslateX() - selectedShip.getWidth() / 2 + cellSize / 2);
//                     selectedShip.setLayoutY(r.getLayoutY() + r.getParent().getTranslateY() + sll.size * cellSize - selectedShip.getWidth() / 2 - cellSize / 2 + 5);
					endX = x;
					endY = y + sll.size - 1;
				} else {
//                     selectedShip.setLayoutX(r.getLayoutX() + r.getParent().getTranslateX());
//                     selectedShip.setLayoutY(r.getLayoutY() + r.getParent().getTranslateY() + 5);
					endX = x + sll.size - 1;
					endY = y;
				}
				Ship2 ship = new Ship2(sll.size, x, y, endX, endY, false);
				bfPlayer.addShip(ship, sll.boolIsRotated);
				Paint col;
//                 System.out.println("x,y:"+x+","+y);
				Node rect = bfPlayer.getBlockNode(x, y);
				System.out.println("rect x,y:" + isRotated);
				if (bfPlayer.isValidToPlace(x, y, sll.size, isRotated)) {
					col = Color.GREEN;
					ship.setShipCoord();
					bfPlayer.lstShip.add(ship);

//                 ship.setIsSet(true);
					if (bfPlayer.lstShip.size() == 5)
						startButton.disableProperty().set(false);

					for (int i = 0; i < sll.size && i < 9; i++) {
						if (!isRotated) {
							if (y + i <= 9)
								r = (Rectangle) bfPlayer.getBlockNode(x, y + i);

						} else {
							if (x + i <= 9)
								r = (Rectangle) bfPlayer.getBlockNode(x + i, y);
						}
						r.setFill(col);
					}
				}
			}
		}
		System.out.println("Normal : " + rbNormal.isSelected());
		System.out.println("Salva : " + rbSalva.isSelected());
		if (rbSalva.isSelected()) {
			boolIsSalvaVariation = true;
			intNoOfTurns = 5;
		}
		System.out.println("boolsalva : " + boolIsSalvaVariation);
     

		for (Node node : anchor.getChildren()) {
			node.setOnMouseClicked(null);
			node.setOnMouseDragged(null);
			node.setOnMouseReleased(null);
			node.setOnMousePressed(null);
			node.setOnMouseEntered(null);
		}
		anchor.setOnMouseReleased(null);
		anchor.setOnMouseDragged(null);
		System.out.println("====Final Ships====");
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				System.out.print(bfPlayer.gameBoard.get(i).get(j).getCharOccupiedFor());
			}
			System.out.println();
		}
		bfComputer.deployComputerShips();
		System.out.println("computer ships deployed");
		installPCBoardListeners(bfComputer);
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

	private void makeReadyComputerToPlay() throws InterruptedException {
		
		System.out.println("in ready computer" + boolIsComputerTurn);

		int x, y;
		Random rand = new Random();
		int q = 1;
		w1: while (true) {
			System.out.println("in w1");
			if (boolIsComputerTurn)
				System.out.println("computer tuen");
			else
				System.out.println("your turn");


			if (boolIsComputerTurn) {
				Thread.sleep((rand.nextInt(5) + 1) * 1000);
//				Thread.sleep(1000);
			
				bfComputer.intScore += (int) (60 / ((System.currentTimeMillis() - time) / 1000));
				System.out.println("hi" + boolIsComputerTurn);
				List<DataCoordinates> lstBlocksToBeSelect = bfPlayer.getFromAIAlgorithm(intNoOfTurns);
				boolean isNeedToBeDisplay = false;
				for (int i = 0; i < intNoOfTurns; i++) {
					if (lstBlocksToBeSelect.size() > 0) {
						x = lstBlocksToBeSelect.get(0).x;
						y = lstBlocksToBeSelect.get(0).y;
					} else {
						x = rand.nextInt(10);
						y = rand.nextInt(10);
					}
					if (bfPlayer.gameBoard.get(x).get(y).getCharOccupiedFor() == 'H'
							|| bfPlayer.gameBoard.get(x).get(y).getCharOccupiedFor() == 'M') {
						if (!boolIsSalvaVariation)
							break;
					} else if (bfPlayer.gameBoard.get(x).get(y).getCharOccupiedFor() == 'S') {
						bfComputer.intScore += 50;
						bfPlayer.gameBoard.get(x).get(y).setCharOccupiedFor('H');
						bfPlayer.stkHitPoints.push(new DataCoordinates(x, y));
						for (Node n : gpPlayer.getChildren()) {
							if (GridPane.getColumnIndex(n) == y && GridPane.getRowIndex(n) == x) {
								Rectangle r = (Rectangle) n;
								r.setFill(Color.RED);
							}
						}
						if (bfPlayer.isShipDestroyed(x, y)) {
							bfPlayer.intTotalAliveShips--;
							bfComputer.intScore += 20;
							if (bfPlayer.intTotalAliveShips == 0) {
								System.out.println("Computer Win!!\nYour Score : " + bfPlayer.intScore
										+ "\nComputer's Score : " + bfComputer.intScore);
								break w1;
							}
							if (boolIsSalvaVariation)
								isNeedToBeDisplay = true;
							else {
								System.out.println("Computer destroyed your ship !! Your Ships Left : "
										+ bfPlayer.intTotalAliveShips);
							}
						}
						if (!boolIsSalvaVariation)
							break;
					} else if (bfPlayer.gameBoard.get(x).get(y).getCharOccupiedFor() == 'B') {
						bfPlayer.gameBoard.get(x).get(y).setCharOccupiedFor('M');
						for (Node n : gpPlayer.getChildren()) {
							if (GridPane.getColumnIndex(n) == y && GridPane.getRowIndex(n) == x) {
								Rectangle r = (Rectangle) n;
								r.setFill(Color.GREY);
							}
						}
					}
					if (lstBlocksToBeSelect.size() > 0)
						lstBlocksToBeSelect.remove(0);
				}
				if (isNeedToBeDisplay) {
					System.out.println(
							"Computer destroyed your ship !! Your Ships Left : " + bfPlayer.intTotalAliveShips);
				}

				boolIsComputerTurn = false;
				time = System.currentTimeMillis();
			}
		}
	}

	class Delta {
		double x, y;
	}
}
