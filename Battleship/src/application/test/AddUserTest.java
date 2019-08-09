package application.test;

import java.util.ArrayList;

import org.junit.BeforeClass;
import org.junit.Test;

import application.Models.BattleFieldPlayer;
import application.Models.Block;
import application.Models.Server;
import application.Models.fileNotExistExc;
import junit.framework.Assert;

public class AddUserTest {
	static BattleFieldPlayer bfPlayer = new BattleFieldPlayer("");
	static BattleFieldPlayer bfComputer = new BattleFieldPlayer("");
	public ArrayList<ArrayList<Block>> gameBoard = new ArrayList<ArrayList<Block>>(10);

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		Server.makeNewUserInFile("pqr", "pqr");
	}

	@Test
	public void testSetPlayerName() throws fileNotExistExc {

		Assert.assertEquals(true, Server.checkForUserExist("pqr", "pqr"));
	}

	@Test
	public void testSetPlayerName2() throws fileNotExistExc {

		Assert.assertEquals(false, Server.checkForUserExist("pqrs", "pqrs"));
	}

	@Test
	public void testSetPlayerName3() throws fileNotExistExc {

		Assert.assertEquals(false, Server.checkForUserExist("pqrs", "pqr"));
	}

	@Test
	public void testSetPlayerName4() throws fileNotExistExc {

		Assert.assertEquals(false, Server.checkForUserExist("pqr", "pqrs"));
	}

	@Test
	public void testSetPlayerName5() throws fileNotExistExc {

		Assert.assertEquals(false, Server.checkForUserExist("pqr", ""));
	}

	@Test
	public void testSetPlayerName6() throws fileNotExistExc {

		Assert.assertEquals(false, Server.checkForUserExist("", ""));
	}

	@Test
	public void testSetPlayerName7() throws fileNotExistExc {

		Assert.assertEquals(false, Server.checkForUserExist("", "sds"));
	}

}
