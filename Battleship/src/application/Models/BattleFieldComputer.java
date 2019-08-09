package application.Models;

import java.util.ArrayList;

/**
 * 
 * @author dhvaniagola
 * @author arunipatel
 * @author SAHIL
 *///
public class BattleFieldComputer extends BattleFieldsParent {
	/**
	 * This constructor will set the initial value of each block of the grid.
	 * 
	 * @param strUserName
	 *            User name of a player
	 */
	public BattleFieldComputer(String strUserName) {
		super(strUserName);
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

}
