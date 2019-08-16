package application.test;

import java.util.ArrayList;

import org.junit.BeforeClass;
import org.junit.Test;

import application.Models.BattleFieldPlayer;
import application.Models.Block;
import application.Models.Ship2;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;
import junit.framework.Assert;

public class AttackTest {
	private static GridPane gpPlayer;
	static BattleFieldPlayer bfPlayer = new BattleFieldPlayer(gpPlayer);
	private static GridPane gpComputer;
	static BattleFieldPlayer bfComputer = new BattleFieldPlayer(gpComputer);
	public ArrayList<ArrayList<Block>> gameBoard = new ArrayList<ArrayList<Block>>(10);

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		bfPlayer = new BattleFieldPlayer(gpPlayer);
	}

	@Test
	public void testIsPlayerHitOnAlreadyHit() {
		int x = 1;
		int y = 3;
		Rectangle r = null;
		bfPlayer.gameBoard.get(x).get(y).setCharOccupiedFor('H');
		Assert.assertEquals(false, bfPlayer.ishit(x, y, true, r, 1));
	}

	@Test
	public void testIsPlayerHitOnAlreadyMiss() {
		int x = 1;
		int y = 7;
		Rectangle r = null;
		bfPlayer.gameBoard.get(x).get(y).setCharOccupiedFor('M');
		Assert.assertEquals(false, bfPlayer.ishit(x, y, true, r, 1));
	}

	@Test
	public void testIsPlayerHitSuccessfully() {
		int x = 5;
		int y = 5;
		Rectangle r = null;
		bfPlayer.gameBoard.get(x).get(y).setCharOccupiedFor('S');
		Assert.assertEquals(true, bfPlayer.ishit(x, y, true, r, 1));
	}

	@Test
	public void testIsPlayerMissTheHit() {
		int x = 7;
		int y = 7;
		Rectangle r = null;
		bfPlayer.gameBoard.get(x).get(y).setCharOccupiedFor('B');
		Assert.assertEquals(false, bfPlayer.ishit(x, y, true, r, 1));
	}

	@Test
	public void testIsShipNotDestroy() {
		Ship2 s = new Ship2(3, 8, 1, 8, 3, false);
//		Rectangle r=(Rectangle)bfPlayer.getBlockNode(x, y);
//		DataCoordinates dc = new DataCoordinates(8, 1);
		s.setShipCoord();
		bfPlayer.addShip(s, false);
		Assert.assertEquals(false, bfPlayer.isShipDestroyed(8, 2));

	}

//	@Test
//	public void testIsShipDestroy() {
//		Ship2 s = new Ship2(1, 9, 8, 9, 8, false);
////		DataCoordinates dc = new DataCoordinates(8, 1);
//		s.setShipCoord();
//		bfPlayer.lstShip.clear();
//		bfPlayer.addShip(s, false);
//		Assert.assertEquals(true, bfPlayer.isShipDestroyed(9, 8));
//
//	}
}
