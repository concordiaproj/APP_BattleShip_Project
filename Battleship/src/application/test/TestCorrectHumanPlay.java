package application.test;

import java.util.ArrayList;

import org.junit.BeforeClass;
import org.junit.Test;

import application.Models.BattleFieldComputer;
import application.Models.BattleFieldPlayer;
import application.Models.Block;
import javafx.scene.layout.GridPane;
import junit.framework.Assert;

public class TestCorrectHumanPlay {
	private static GridPane gpPlayer;
	static BattleFieldComputer bfComputer = new BattleFieldComputer("Player");;
	public ArrayList<ArrayList<Block>> gameBoard = new ArrayList<ArrayList<Block>>(10);
	static BattleFieldPlayer bfPlayer;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		bfPlayer = new BattleFieldPlayer("Player");
	}

	@Test
	public void testIsCorrectPlayer() {
		Assert.assertEquals(true, bfComputer.isValidToPlace(0, 9, 3, true));
	}

	@Test
	public void testIsnotCorrectPlayer() {
		Assert.assertEquals(false, bfComputer.isValidToPlace(6, 9, 5, true));
	}
}
