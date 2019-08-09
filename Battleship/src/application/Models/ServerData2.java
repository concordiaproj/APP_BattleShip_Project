package application.Models;

import java.io.Serializable;
import java.util.HashMap;

public class ServerData2 implements Serializable {
	public int intPlayerId;
	public int intOperationId;
	public HashMap<String, ShipLatestLocation2> mapShipLocation = new HashMap<>();
	public boolean boolReturn;
	public int x, y, i, j, size, endX, endY;
	public boolean isSalva = false, isRotated = false, isDestroyed = false;
	public int intPlayerScore;
	public int intCompScore;
	public int intPlayerAliveShip;
	public int intCompAliveShip;
	public char c = '\0';
	public int intReply = 0;
	public boolean isComputerTurn = false;
	public boolean isWinner = false;
	public String strSelf = "";
	public String strOther = "";
	public boolean flag = false;
	public String strUname = "";
	public String strPass = "";

	public ServerData2(int intPlayerId, int intOperationId, HashMap<String, ShipLatestLocation2> mapShipLocation) {
		this.intPlayerId = intPlayerId;
		this.intOperationId = intOperationId;
		this.mapShipLocation = mapShipLocation;
	}

	// to initialize objects
	public ServerData2(int intPlayerId, int intOperationId) {
		this.intPlayerId = intPlayerId;
		this.intOperationId = intOperationId;
	}

	// request and reply
	public ServerData2(int intPlayerId, int intOperationId, boolean isComputerTurn, boolean temp) {
		this.intPlayerId = intPlayerId;
		this.intOperationId = intOperationId;
		this.isComputerTurn = isComputerTurn;
	}

	// reply for reload game
	public ServerData2(int intPlayerId, int intOperationId, String strSelf, String strOther, int intPlayerScore,
			int intCompScore, int intPlayerAliveShip, int intCompAliveShip, boolean flag) {
		this.intPlayerId = intPlayerId;
		this.intOperationId = intOperationId;
		this.strSelf = strSelf;
		this.strOther = strOther;
		this.intPlayerScore = intPlayerScore;
		this.intCompScore = intCompScore;
		this.intPlayerAliveShip = intPlayerAliveShip;
		this.intCompAliveShip = intCompAliveShip;
		this.flag = flag;
	}

	// reply
	public ServerData2(int intPlayerId, int intOperationId, boolean boolReturn, int intPlayerScore, int intCompScore,
			int intPlayerAliveShip, int intCompAliveShip, char c, int x, int y, boolean isWinner) {
		this.intPlayerId = intPlayerId;
		this.intOperationId = intOperationId;
		this.boolReturn = boolReturn;
		this.intPlayerScore = intPlayerScore;
		this.intCompScore = intCompScore;
		this.intPlayerAliveShip = intPlayerAliveShip;
		this.intCompAliveShip = intCompAliveShip;
		this.c = c;
		this.x = x;
		this.y = y;
		this.isWinner = isWinner;
	}

	// reply2
	public ServerData2(int intPlayerId, int intOperationId, int intReply) {
		this.intPlayerId = intPlayerId;
		this.intOperationId = intOperationId;
		this.intReply = intReply;
	}

	// isHit
	public ServerData2(int intPlayerId, int intOperationId, int x, int y, boolean isSalva, int i) {

		this.intPlayerId = intPlayerId;
		this.intOperationId = intOperationId;
		this.x = x;
		this.y = y;
		this.isSalva = isSalva;
		// this.r = r;
		this.i = i;
	}

	// addShip
	public ServerData2(int intPlayerId, int intOperationId, int size, int x, int y, int endX, int endY,
			boolean isDestroyed, boolean isRotated) {
		this.intPlayerId = intPlayerId;
		this.intOperationId = intOperationId;
		this.size = size;
		this.x = x;
		this.y = y;
		this.endX = endX;
		this.endY = endY;
		this.isDestroyed = isDestroyed;
		this.isRotated = isRotated;

	}

	// setShip
	public ServerData2(int intPlayerId, int intOperationId, int x, int y) {
		this.intPlayerId = intPlayerId;
		this.intOperationId = intOperationId;
		this.x = x;
		this.y = y;
	}

	// startGame
	public ServerData2(int intPlayerId, int intOperationId, boolean isSalva) {
		this.intPlayerId = intPlayerId;
		this.intOperationId = intOperationId;
		this.isSalva = isSalva;
	}

	// login
	public ServerData2(int intPlayerId, int intOperationId, String strUname, String strPass) {
		this.intPlayerId = intPlayerId;
		this.intOperationId = intOperationId;
		this.strUname = strUname;
		this.strPass = strPass;
	}
}
