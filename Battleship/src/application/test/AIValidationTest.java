package application.test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Stack;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import application.Models.BattleFieldPlayer;
import application.Models.Block;
import application.Models.DataCoordinates;
import javafx.scene.layout.GridPane;
import junit.framework.Assert;

class AIValidationTest{
	private static GridPane gpPlayer;
	static BattleFieldPlayer bfPlayer;
	public ArrayList<ArrayList<Block>> gameBoard = new ArrayList<ArrayList<Block>>(10);
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		Stack<DataCoordinates> stkHitPoints = new Stack<>();
		bfPlayer = new BattleFieldPlayer(gpPlayer);
		DataCoordinates dc;
		//=new DataCoordinates(0, 0);
		//
		//stkHitPoints.push(dc);
		//
	}

	@Test
	void testGetFromAIAlgorithm() {
		DataCoordinates dc;//=new DataCoordinates(0, 0);
		Assert.assertEquals(0, bfPlayer.getFromAIAlgorithm(1).size());
		bfPlayer.stkHitPoints.clear();
	}
	@Test
	void testGetValidFromAIAlgorithmInitialOne() {
		DataCoordinates dc=new DataCoordinates(0, 0);
		bfPlayer.stkHitPoints.push(dc);
		Assert.assertEquals(1, bfPlayer.getFromAIAlgorithm(1).size());
		bfPlayer.stkHitPoints.clear();
	}
	
	@Test
	void testGetValidFromAIAlgorithmInitialSalva() {
		DataCoordinates dc=new DataCoordinates(0, 0);
		bfPlayer.stkHitPoints.push(dc);
		//System.out.println("size:"+bfPlayer.stkHitPoints.size());
	//	System.out.println(bfPlayer.stkHitPoints.lastElement().x+" "+bfPlayer.stkHitPoints.lastElement().y);
		int y=bfPlayer.getFromAIAlgorithm(5).size();
	    System.out.println(y);
		//Assert.assertEquals(2, bfPlayer.getFromAIAlgorithm(5).size());
		Assert.assertEquals(2, y);
		bfPlayer.stkHitPoints.clear();
	}
	
	
	@Test
	void testGetValidFromAIAlgorithmOne1() {
		
		DataCoordinates dc=new DataCoordinates(0, 5);
		bfPlayer.stkHitPoints.push(dc);
		int y=bfPlayer.getFromAIAlgorithm(1).size();
		//System.out.println(y);
		
		Assert.assertEquals(1, y);
		bfPlayer.stkHitPoints.clear();
		
	}
	
	
	@Test
	void testGetValidFromAIAlgorithmOneSal() {
		
		DataCoordinates dc=new DataCoordinates(0, 5);
		bfPlayer.stkHitPoints.push(dc);
		int y=bfPlayer.getFromAIAlgorithm(5).size();
		//System.out.println(y);
		
		Assert.assertEquals(3, y);
		bfPlayer.stkHitPoints.clear();
		
	}
	
	@Test
	void testGetValidFromAIAlgorithmTwo1() {
		
		DataCoordinates dc=new DataCoordinates(0, 5);
		bfPlayer.stkHitPoints.push(dc);
		 dc=new DataCoordinates(5, 5);
		bfPlayer.stkHitPoints.push(dc);
		int y=bfPlayer.getFromAIAlgorithm(1).size();
		//System.out.println(y);
		
		Assert.assertEquals(1, y);
		bfPlayer.stkHitPoints.clear();
		
	}
	
	@Test
	void testGetValidFromAIAlgorithmTwoSal() {
		
		DataCoordinates dc=new DataCoordinates(0, 5);
		bfPlayer.stkHitPoints.push(dc);
		 dc=new DataCoordinates(5, 5);
		bfPlayer.stkHitPoints.push(dc);
		int y=bfPlayer.getFromAIAlgorithm(5).size();
		//System.out.println(y);
		
		Assert.assertEquals(5, y);
		bfPlayer.stkHitPoints.clear();
		
	}
	
	
	

}
