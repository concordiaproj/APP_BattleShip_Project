package application.test;

import java.util.ArrayList;

import org.junit.BeforeClass;
import org.junit.Test;

import application.Models.BattleFieldPlayer;
import application.Models.Block;
import javafx.scene.layout.GridPane;
import junit.framework.Assert;

public class WinnerTest {
	private static GridPane gpPlayer;
	static BattleFieldPlayer bfPlayer = new BattleFieldPlayer("Player");
	private static GridPane gpComputer;
	static BattleFieldPlayer bfComputer = new BattleFieldPlayer("Computer");
	public ArrayList<ArrayList<Block>> gameBoard = new ArrayList<ArrayList<Block>>(10);

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		bfPlayer = new BattleFieldPlayer("Player");
	}

	@Test
	public void testIsPlayerWinner() {
		bfPlayer.intTotalAliveShips = 0;
		Assert.assertEquals(true, bfPlayer.isWinner());
		bfPlayer.intTotalAliveShips = 5;
	}

	@Test
	public void testIsPlayerNotWinner() {
		bfPlayer.intTotalAliveShips = 3;
		Assert.assertEquals(false, bfPlayer.isWinner());
		bfPlayer.intTotalAliveShips = 5;
	}

	@Test
	public void testIsComputerWinner() {
		bfComputer.intTotalAliveShips = 0;
		Assert.assertEquals(true, bfComputer.isWinner());
		bfComputer.intTotalAliveShips = 5;
	}

	@Test
	public void testIsComputerNotWinner() {
		bfComputer.intTotalAliveShips = 2;
		Assert.assertEquals(false, bfComputer.isWinner());
		bfComputer.intTotalAliveShips = 5;
	}

	@Test
	public void testIsComputerNotWinner1() {
		bfComputer.intTotalAliveShips = 5;
		Assert.assertEquals(false, bfComputer.isWinner());
		bfComputer.intTotalAliveShips = 5;
	}

	@Test
	public void testIsComputerNotWinner2() {
		bfComputer.intTotalAliveShips = 1;
		Assert.assertEquals(false, bfComputer.isWinner());
		bfComputer.intTotalAliveShips = 5;
	}

}
