package application.Models;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.scene.layout.GridPane;

public class BattleFieldComputer extends BattleFieldsParent {

	public BattleFieldComputer(GridPane gp) {
		super(gp);
		// TODO Auto-generated constructor stub
		this.id = 0;
		for (int i = 0; i < 10; i++) {
			ArrayList<Block> arrLstBlock = new ArrayList<>();
			for (int j = 0; j < 10; j++) {
				Block blk = new Block(i, j, 'B');
				arrLstBlock.add(blk);
			}
			gameBoard.add(arrLstBlock);
		}
	}

	public void deployComputerShips() {
		List<Integer> lstShipSize = new ArrayList<>();
		Random rand = new Random();
		int x, y, xEnd, yEnd;
		lstShipSize.add(1);
		lstShipSize.add(3);
		lstShipSize.add(3);
		lstShipSize.add(5);
		lstShipSize.add(5);
		for (int i = 0; i < lstShipSize.size(); i++) {
			while (true) {
				x = rand.nextInt(10);
				y = rand.nextInt(10);
				xEnd = x;
				yEnd = y;
				boolean isRotate = (rand.nextInt(2) == 0 ? false : true);
				if (this.isValidToPlace(x, y, lstShipSize.get(i), isRotate)) {
					if (isRotate)
						xEnd = x + lstShipSize.get(i) - 1;
					else
						yEnd = y + lstShipSize.get(i) - 1;
					Ship2 ship = new Ship2(lstShipSize.get(i), x, y, xEnd, yEnd, false);
					this.addShip(ship, isRotate);
					ship.setShipCoord();
					this.lstShip.add(ship);
					break;
				}
			}
		}

	}

}
