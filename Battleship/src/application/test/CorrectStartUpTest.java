package application.test;

import java.util.ArrayList;

import org.junit.BeforeClass;
import org.junit.Test;

import application.Models.BattleFieldComputer;
import application.Models.BattleFieldPlayer;
import application.Models.Block;
import javafx.scene.layout.GridPane;
import junit.framework.Assert;

public class CorrectStartUpTest {
	private static GridPane gpPlayer;
	private static GridPane gpComp;
	static BattleFieldComputer bfComputer = new BattleFieldComputer(gpComp);
	static BattleFieldPlayer bfPlayer = new BattleFieldPlayer(gpPlayer);
	public ArrayList<ArrayList<Block>> gameBoard = new ArrayList<ArrayList<Block>>(10);

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		bfPlayer = new BattleFieldPlayer(gpPlayer);
	}

	@Test
	public void testIsAllShipsAlive() {
		Assert.assertEquals(5, bfComputer.intTotalAliveShips);
	}

	@Test
	public void testIsScoreZero() {
		Assert.assertEquals(0, bfPlayer.intScore);
	}

	@Test
	public void testIsIdAssigned() {
		Assert.assertEquals(0, bfComputer.id);
	}

	@Test
	public void testIsNoAnyHit() {
		Assert.assertEquals(0, bfPlayer.stkHitPoints.size());
	}
}
