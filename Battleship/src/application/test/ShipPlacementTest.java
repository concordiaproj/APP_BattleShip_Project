package application.test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import application.Models.BattleFieldPlayer;
import application.Models.Block;
import javafx.scene.layout.GridPane;
import junit.framework.Assert;

class ShipPlacementTest {
	private static GridPane gpPlayer;
	static BattleFieldPlayer bfPlayer;
	public ArrayList<ArrayList<Block>> gameBoard = new ArrayList<ArrayList<Block>>(10);
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		bfPlayer = new BattleFieldPlayer(gpPlayer);
	}

	@Test
	void testIsValidToPlace() {
		Assert.assertEquals(true, bfPlayer.isValidToPlace(0, 1, 5, false));
	}
	@Test
	void testIsnotValidToPlace() {
		Assert.assertEquals(false, bfPlayer.isValidToPlace(9, 9, 5, false));
	}
	
	@Test
	void testIsnotValidToPlace1() {
		Assert.assertEquals(false, bfPlayer.isValidToPlace(11, 11, 5, false));
	}
}
