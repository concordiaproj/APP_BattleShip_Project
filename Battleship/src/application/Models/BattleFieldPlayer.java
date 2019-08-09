package application.Models;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * 
 * @author dhvaniagola
 * @author arunipatel
 * @author SAHIL
 */
public class BattleFieldPlayer extends BattleFieldsParent {
	public Stack<DataCoordinates> stkHitPoints = new Stack<>();

	/**
	 * This constructor will set the initial value of each block of the grid.
	 * 
	 * @param gp
	 *            Gridpane of Player
	 */
	// public BattleFieldPlayer(GridPane gp) {
	public BattleFieldPlayer(String strUserName) {
		super(strUserName);
		// TODO Auto-generated constructor stub
		this.id = 1;
		for (int i = 0; i < 10; i++) {
			ArrayList<Block> arrLstBlock = new ArrayList<>();
			for (int j = 0; j < 10; j++) {
				Block blk = new Block(i, j, 'B');
				arrLstBlock.add(blk);
			}
			gameBoard.add(arrLstBlock);
		}
	}

	/**
	 * This method will calculate the coordinates such that computer will play smart
	 * game.
	 * 
	 * @param intNoOfTurns
	 *            How many maximum coordinates required
	 * @return Returns list of the coordinates
	 */
	public List<DataCoordinates> getFromAIAlgorithm(int intNoOfTurns) {
		// TODO Auto-generated method stub
		List<DataCoordinates> lstReturn = new ArrayList<>();
		w1: while (stkHitPoints.size() > 0 && lstReturn.size() < intNoOfTurns) {
			DataCoordinates dc = stkHitPoints.lastElement();
			List<DataCoordinates> lstNeightbours = getNeightbours(dc);
			while (lstNeightbours.size() > 0) {
				DataCoordinates dcTemp = lstNeightbours.get(0);
				if (this.gameBoard.get(dcTemp.x).get(dcTemp.y).getCharOccupiedFor() == 'S'
						|| this.gameBoard.get(dcTemp.x).get(dcTemp.y).getCharOccupiedFor() == 'B') {
					if (lstReturn.size() < intNoOfTurns) {
						lstReturn.add(dcTemp);
						lstNeightbours.remove(0);
					} else {
						break w1;
					}
				} else
					lstNeightbours.remove(0);
			}
			if (lstNeightbours.size() == 0)
				stkHitPoints.pop();
		}
		return lstReturn;
	}

	/**
	 * This method will calculate neighbors of the passed coordinates.
	 * 
	 * @param dc
	 *            Object which contains x and y coordinates
	 * @return Returns List of the neighbors
	 */
	private List<DataCoordinates> getNeightbours(DataCoordinates dc) {
		// TODO Auto-generated method stub
		List<DataCoordinates> lstReturn = new ArrayList<>();
		int x = dc.x;
		int y = dc.y;
		if ((x + 1) < 10)
			lstReturn.add(new DataCoordinates(x + 1, y));
		if ((x - 1) >= 0)
			lstReturn.add(new DataCoordinates(x - 1, y));
		if ((y + 1) < 10)
			lstReturn.add(new DataCoordinates(x, y + 1));
		if ((y - 1) >= 0)
			lstReturn.add(new DataCoordinates(x, y - 1));
		return lstReturn;
	}

}
