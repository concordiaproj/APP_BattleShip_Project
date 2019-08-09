package application.Models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SaveAndLoadData implements Serializable {
	String strUser1;
	String strUser0;
	ArrayList<ArrayList<Block>> gameBoard1 = new ArrayList<ArrayList<Block>>(10);
	ArrayList<ArrayList<Block>> gameBoard0 = new ArrayList<ArrayList<Block>>(10);
	int intAliveShips1;
	int intAliveShips0;
	boolean isSalva;
	int intScore1;
	int intScore0;
	List<Ship2> lstShip1 = new ArrayList<>();
	List<Ship2> lstShip0 = new ArrayList<>();
	HashMap<String, ShipLatestLocation> mapShipLocation1 = new HashMap<>();
	HashMap<String, ShipLatestLocation> mapShipLocation0 = new HashMap<>();

	public SaveAndLoadData(String strUser1, String strUser0, ArrayList<ArrayList<Block>> gameBoard1,
			ArrayList<ArrayList<Block>> gameBoard0, int intAliveShips1, int intAliveShips0, boolean isSalva,
			int intScore1, int intScore0, List<Ship2> lstShip1, List<Ship2> lstShip0,
			HashMap<String, ShipLatestLocation> mapShipLocation1,
			HashMap<String, ShipLatestLocation> mapShipLocation0) {
		// TODO Auto-generated constructor stub
		this.strUser1 = strUser1;
		this.strUser0 = strUser0;
		this.gameBoard1 = gameBoard1;
		this.gameBoard0 = gameBoard0;
		this.intAliveShips1 = intAliveShips1;
		this.intAliveShips0 = intAliveShips0;
		this.isSalva = isSalva;
		this.intScore1 = intScore1;
		this.intScore0 = intScore0;
		this.lstShip1 = lstShip1;
		this.lstShip0 = lstShip0;
		this.mapShipLocation1 = mapShipLocation1;
		this.mapShipLocation0 = mapShipLocation0;
	}

}
