package application.test;

import java.util.ArrayList;

import org.junit.BeforeClass;
import org.junit.Test;

import application.Models.BattleFieldComputer;
import application.Models.BattleFieldPlayer;
import application.Models.Block;
import javafx.scene.layout.GridPane;
import junit.framework.Assert;

public class ShipPlacementTest {
	private static GridPane gpPlayer;
	static BattleFieldComputer bfComputer = new BattleFieldComputer("Player");;
	public ArrayList<ArrayList<Block>> gameBoard = new ArrayList<ArrayList<Block>>(10);
	static BattleFieldPlayer bfPlayer;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		bfPlayer = new BattleFieldPlayer("Player");
	}

	@Test
	public void testIsValidToPlace() {
		Assert.assertEquals(true, bfComputer.isValidToPlace(0, 1, 5, false));
	}

	@Test
	public void testIsnotValidToPlace() {

		Assert.assertEquals(false, bfComputer.isValidToPlace(9, 9, 5, false));
	}

	@Test
	public void testIsnotValidToPlace1() {
		Assert.assertEquals(false, bfComputer.isValidToPlace(11, 11, 5, false));
	}

	@Test
	public void testIsnotValidToPlace2() {
		Assert.assertEquals(false, bfComputer.isValidToPlace(0, 9, 5, false));
	}

	@Test
	public void testIsnotValidToPlace3() {
		Assert.assertEquals(true, bfComputer.isValidToPlace(0, 1, 3, false));
	}

	@Test
	public void testIsnotValidToPlace4() {
		Assert.assertEquals(true, bfComputer.isValidToPlace(0, 1, 3, true));
	}

	@Test
	public void testIsnotValidToPlace5() {
		Assert.assertEquals(false, bfComputer.isValidToPlace(9, 9, 3, true));
	}

	@Test
	public void testIsnotValidToPlace6() {
		Assert.assertEquals(false, bfComputer.isValidToPlace(11, 12, 3, true));
	}

}
