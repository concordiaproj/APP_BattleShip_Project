package application.test;

import java.util.ArrayList;

import org.junit.BeforeClass;
import org.junit.Test;

import application.Models.BattleFieldPlayer;
import application.Models.Block;
import application.Models.DataCoordinates;
import javafx.scene.layout.GridPane;
import junit.framework.Assert;

public class AIValidationTest {
	private static GridPane gpPlayer;
	static BattleFieldPlayer bfPlayer = new BattleFieldPlayer(gpPlayer);
	public ArrayList<ArrayList<Block>> gameBoard = new ArrayList<ArrayList<Block>>(10);

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		bfPlayer = new BattleFieldPlayer(gpPlayer);
	}

	@Test
	public void testGetFromAIAlgorithm() {
//		DataCoordinates dc;// =new DataCoordinates(0, 0);
		bfPlayer.stkHitPoints.clear();
		Assert.assertEquals(0, bfPlayer.getFromAIAlgorithm(1).size());
		bfPlayer.stkHitPoints.clear();
	}

	@Test
	public void testGetValidFromAIAlgorithmInitialOne() {
		DataCoordinates dc = new DataCoordinates(0, 0);
		bfPlayer.stkHitPoints.push(dc);
		Assert.assertEquals(1, bfPlayer.getFromAIAlgorithm(1).size());
		bfPlayer.stkHitPoints.clear();
	}

	@Test
	public void testGetValidFromAIAlgorithmInitialSalva() {
		DataCoordinates dc = new DataCoordinates(0, 0);
		bfPlayer.stkHitPoints.push(dc);
		int y = bfPlayer.getFromAIAlgorithm(5).size();
//		System.out.println(y);
		Assert.assertEquals(2, y);
		bfPlayer.stkHitPoints.clear();
	}

	@Test
	public void testGetValidFromAIAlgorithmOne1() {

		DataCoordinates dc = new DataCoordinates(0, 5);
		bfPlayer.stkHitPoints.push(dc);
		int y = bfPlayer.getFromAIAlgorithm(1).size();
		Assert.assertEquals(1, y);
		bfPlayer.stkHitPoints.clear();

	}

	@Test
	public void testGetValidFromAIAlgorithmOneSal() {

		DataCoordinates dc = new DataCoordinates(0, 5);
		bfPlayer.stkHitPoints.push(dc);
		int y = bfPlayer.getFromAIAlgorithm(5).size();
		Assert.assertEquals(3, y);
		bfPlayer.stkHitPoints.clear();

	}

	@Test
	public void testGetValidFromAIAlgorithmTwo1() {

		DataCoordinates dc = new DataCoordinates(0, 5);
		bfPlayer.stkHitPoints.push(dc);
		dc = new DataCoordinates(5, 5);
		bfPlayer.stkHitPoints.push(dc);
		int y = bfPlayer.getFromAIAlgorithm(1).size();
		// System.out.println(y);

		Assert.assertEquals(1, y);
		bfPlayer.stkHitPoints.clear();

	}

	@Test
	public void testGetValidFromAIAlgorithmTwoSal() {

		DataCoordinates dc = new DataCoordinates(0, 5);
		bfPlayer.stkHitPoints.push(dc);
		dc = new DataCoordinates(5, 5);
		bfPlayer.stkHitPoints.push(dc);
		int y = bfPlayer.getFromAIAlgorithm(5).size();
		// System.out.println(y);

		Assert.assertEquals(5, y);
		bfPlayer.stkHitPoints.clear();

	}

}