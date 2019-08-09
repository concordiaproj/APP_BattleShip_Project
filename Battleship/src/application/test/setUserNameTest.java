package application.test;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;

import application.Models.BattleFieldPlayer;
import application.Models.Block;
import application.Models.ServerData;

public class setUserNameTest {
	static BattleFieldPlayer bfPlayer = new BattleFieldPlayer("");
	static BattleFieldPlayer bfComputer = new BattleFieldPlayer("");
	public ArrayList<ArrayList<Block>> gameBoard = new ArrayList<ArrayList<Block>>(10);

	// @Test
	// public void testSetPlayerName() {
	// ServerData sd = new ServerData(1, 14, "Player", "Player");
	// System.out.println(Server.bfPlayer.strUserName + "--");
	// Server.setUserName(sd);
	// System.out.println(Server.bfPlayer.strUserName + "---");
	// Assert.assertEquals("Player", Server.bfPlayer.strUserName);
	// }
	//
	@Test
	public void testSetComputerName() {
		ServerData sd = new ServerData(1, 14, "Computer", "Computer");
		Assert.assertEquals(false, bfPlayer.isWinner());
		bfPlayer.intTotalAliveShips = 5;
	}
}
