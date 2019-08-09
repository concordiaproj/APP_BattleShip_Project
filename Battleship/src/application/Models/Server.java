package application.Models;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;
import java.util.TreeMap;

public class Server {
	public static BattleFieldPlayer bfPlayer;
	public static BattleFieldComputer bfComputer;
	public static int intServerPort = 1235;
	public static boolean isSalva = false;
	public static String strPlayerIP = "132.205.93.41";
	public static int intPlayerPort = 1234;
	public static String strComputerIP = "132.205.93.36";
	public static int intComputerPort = 1236;
	public static boolean isComputerTurn = false;
	public static String strUserFile = "src\\User.txt";

	/**
	 * Server will start and ready to receive the request from clients(Players).
	 * 
	 * @param args
	 *            arguments for main method
	 * @throws IOException
	 *             Input output Exception
	 * @throws ClassNotFoundException
	 *             Class not found exception
	 * @throws userAlreadyExistExc
	 * @throws allShipPlacedCorrectlyExc
	 * @throws nullSerializedResponseExc
	 * @throws fileNotExistExc
	 */
	public static void main(String[] args) throws IOException, ClassNotFoundException, userAlreadyExistExc,
			allShipPlacedCorrectlyExc, nullSerializedResponseExc, fileNotExistExc {
		// TODO Auto-generated method stub
		// 172.30.15.203
		bfPlayer = new BattleFieldPlayer("");
		bfComputer = new BattleFieldComputer("");
		InetAddress ip = InetAddress.getLocalHost();
		System.out.println(ip);
		DatagramSocket ds = new DatagramSocket(intServerPort);
		System.out.println(InetAddress.getLocalHost());
		DatagramPacket request = null;
		while (true) {
			System.out.println("Ready to listen");
			byte[] buffer = new byte[1000];
			request = new DatagramPacket(buffer, buffer.length);
			System.out.println("while..41");
			ds.receive(request);
			ServerData sd = (ServerData) deserialize(request.getData());
			doOperation(sd, request, ds);
			System.out.println(sd.intPlayerId + "in server");
		}
	}

	/**
	 * This method will recognize the operation id and do the required operation of
	 * received request and send the response back to the client(s).
	 * 
	 * @param sd
	 *            Object of ServerData class.
	 * @param dp
	 *            Object of DatagramPacket.
	 * @param ds
	 *            Object of DatagramSocket.
	 * @throws IOException
	 * @throws userAlreadyExistExc
	 * @throws allShipPlacedCorrectlyExc
	 * @throws nullSerializedResponseExc
	 * @throws fileNotExistExc
	 */
	private static void doOperation(ServerData sd, DatagramPacket dp, DatagramSocket ds) throws IOException,
			userAlreadyExistExc, allShipPlacedCorrectlyExc, nullSerializedResponseExc, fileNotExistExc {
		int intOperationId = sd.intOperationId;
		System.out.println("operation ID : " + intOperationId);
		ServerData sdTemp = null;
		ServerData sdTemp0 = null;
		boolean boolReply = false;
		switch (intOperationId) {
		case 1:// onDiscartGame

			System.out.println("case 1 Server");
			sdTemp = new ServerData(sd.intPlayerId, intOperationId, true);
			break;
		case 2:
			System.out.println("case 2 Server");
			boolReply = checkAllValidPositions(sd);
			System.out.println("all Valid Position:" + boolReply);
			sdTemp = new ServerData(sd.intPlayerId, intOperationId, boolReply, bfPlayer.intScore, bfComputer.intScore,
					bfPlayer.intTotalAliveShips, bfComputer.intTotalAliveShips, '\0', -1, -1, false);
			if (!boolReply) {
				throw new allShipPlacedCorrectlyExc("All ships are not placed Correctly");
			}
			break;
		case 3:// startGame //send reply to both
			System.out.println("case 3 Server");
			int intReply = wantToStartGame(sd);
			sdTemp = new ServerData(sd.intPlayerId, intOperationId, intReply);
			break;
		case 4:// addShip
			System.out.println("case 4 Server");
			doAddShip(sd);
			break;
		case 5:// isHit //send reply to both
			System.out.println("case 5 Server");
			sdTemp = checkIsHit(sd);
			sdTemp.intPlayerId = sd.intPlayerId;
			sdTemp.intOperationId = intOperationId;
			sdTemp.intPlayerScore = bfPlayer.intScore;
			sdTemp.intCompScore = bfComputer.intScore;
			sdTemp.intPlayerAliveShip = bfPlayer.intTotalAliveShips;
			sdTemp.intCompAliveShip = bfComputer.intTotalAliveShips;
			break;
		case 6:// set computer turn // send reply to both
			System.out.println("case 6 Server");
			setTurn(sd);
			sdTemp = new ServerData(sd.intPlayerId, intOperationId, isComputerTurn, false);
			break;
		case 9:// setShip
			System.out.println("case 9 Server");
			boolReply = doSetShip(sd);
			sdTemp = new ServerData(sd.intPlayerId, intOperationId, boolReply, bfPlayer.intScore, bfComputer.intScore,
					bfPlayer.intTotalAliveShips, bfComputer.intTotalAliveShips, '\0', -1, -1, false);
			if (sdTemp == null) {
				System.out.println("it is null");
				throw new nullSerializedResponseExc("Response to client is null serialized");
			}
			break;
		case 10:// save game
			System.out.println("case 12 Server");
			doSaveGame();
			sdTemp = new ServerData(sd.intPlayerId, intOperationId);
			break;
		case 11:// reload game
			String str = getFileName();
			File f = new File("src\\" + str);
			if (f.exists()) {
				System.out.println("Exists");
				FileInputStream fin = null;
				ObjectInputStream ois = null;
				SaveAndLoadData saveAndLoad;

				try {
					fin = new FileInputStream("src\\" + str);
					ois = new ObjectInputStream(fin);
					saveAndLoad = (SaveAndLoadData) ois.readObject();
					reloadParameters(saveAndLoad);
					String str1 = "";
					String str0 = "";
					for (int i = 0; i < 10; i++) {
						for (int j = 0; j < 10; j++) {
							System.out.println(i + "," + j);
							str1 += bfPlayer.gameBoard.get(i).get(j).getCharOccupiedFor();
							str0 += bfComputer.gameBoard.get(i).get(j).getCharOccupiedFor();
						}
					}
					sdTemp = new ServerData(sd.intPlayerId, intOperationId, str1, str0, bfPlayer.intScore,
							bfComputer.intScore, bfPlayer.intTotalAliveShips, bfComputer.intTotalAliveShips, true);
					sdTemp0 = new ServerData(sd.intPlayerId, intOperationId, str0, str1, bfComputer.intScore,
							bfPlayer.intScore, bfComputer.intTotalAliveShips, bfPlayer.intTotalAliveShips, true);
					sdTemp.isSalva = bfPlayer.isSalva;
					sdTemp0.isSalva = bfPlayer.isSalva;
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}

			} else {
				sdTemp = new ServerData(sd.intPlayerId, intOperationId, "", "", 00, 00, 0, 0, false);
				sdTemp0 = new ServerData(sd.intPlayerId, intOperationId, "", "", 00, 00, 0, 0, false);
			}
			break;
		case 12:
			if (!checkForUserExist(sd.strUname, sd.strPass)) {
				System.out.println(" not exist");
				makeNewUserInFile(sd.strUname, sd.strPass);

			} else {
				throw new userAlreadyExistExc("Game Discarded");
			}
			setUserName(sd);
			sdTemp = new ServerData(sd.intPlayerId, intOperationId, sd.strUname, "");
			break;
		case 13:// restart
			String strP = bfPlayer.strUserName;
			String strC = bfComputer.strUserName;
			bfPlayer = new BattleFieldPlayer(strP);
			bfComputer = new BattleFieldComputer(strC);
			sdTemp = new ServerData(sd.intPlayerId, intOperationId);
			break;
		}
		if (sdTemp != null) {
			System.out.println("operation id to send to client:" + intOperationId);
			if (intOperationId == 1 || intOperationId == 3 || intOperationId == 5 || intOperationId == 6
					|| intOperationId == 10 || intOperationId == 13) {
				DatagramPacket DpSend = new DatagramPacket(serialize(sdTemp), serialize(sdTemp).length,
						InetAddress.getByName(strPlayerIP), intPlayerPort);

				ds.send(DpSend);
				DpSend = new DatagramPacket(serialize(sdTemp), serialize(sdTemp).length,
						InetAddress.getByName(strComputerIP), intComputerPort);

				ds.send(DpSend);
			} else if (intOperationId == 11) {
				DatagramPacket DpSend = new DatagramPacket(serialize(sdTemp), serialize(sdTemp).length,
						InetAddress.getByName(strPlayerIP), intPlayerPort);

				ds.send(DpSend);
				DpSend = new DatagramPacket(serialize(sdTemp0), serialize(sdTemp0).length,
						InetAddress.getByName(strComputerIP), intComputerPort);

				ds.send(DpSend);
			} else {
				System.out.println(dp.getAddress() + "--" + dp.getPort());
				if (sd.intPlayerId == 1) {
					System.out.println(dp.getAddress() + "--" + dp.getPort());
					DatagramPacket DpSend = new DatagramPacket(serialize(sdTemp), serialize(sdTemp).length,
							InetAddress.getByName(strPlayerIP), intPlayerPort);

					ds.send(DpSend);
				} else {
					DatagramPacket DpSend = new DatagramPacket(serialize(sdTemp), serialize(sdTemp).length,
							InetAddress.getByName(strComputerIP), intComputerPort);

					ds.send(DpSend);
				}
			}
		}
	}

	/**
	 * This method will make a entry of user name and password of new user in
	 * User.txt.
	 * 
	 * @param strUserName
	 *            User name of the user who's entry need to be wrote in text file.
	 * @param strPassWord
	 *            Password of the user who's entry need to be wrote in text file.
	 */
	public static void makeNewUserInFile(String strUserName, String strPassWord) {
		// TODO Auto-generated method stub
		try {
			// TODO Auto-generated method stub
			BufferedWriter out = new BufferedWriter(new FileWriter(strUserFile, true));
			out.write(strUserName + ";" + strPassWord + "\n");
			out.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * This method get a user name from ServerData object and set it as a player's
	 * user name.
	 * 
	 * @param sd
	 *            Object of ServerData class
	 */
	public static void setUserName(ServerData sd) {
		// TODO Auto-generated method stub
		System.out.println(sd.strUname);
		if (sd.intPlayerId == 1)
			bfPlayer.strUserName = sd.strUname;
		else
			bfComputer.strUserName = sd.strUname;

	}

	/**
	 * This method will check that the user is exist or a new user.
	 * 
	 * @param strUserName
	 *            User name of a player.
	 * @param strPassWord
	 *            Password of a player.
	 * @return Returns true if user is exist else returns false.
	 * @throws fileNotExistExc
	 */
	public static boolean checkForUserExist(String strUserName, String strPassWord) throws fileNotExistExc {
		// TODO Auto-generated method stub
		File file = new File(strUserFile);
		Scanner sc;
		try {
			if (!file.exists()) {
				file.createNewFile();
				throw new fileNotExistExc("File not Exist on given location");
			}
			sc = new Scanner(file);
			while (sc.hasNextLine()) {
				String strLine = sc.nextLine();
				String strArr[] = strLine.split(";");
				if (strArr[0].trim().equals(strUserName) && strArr[1].trim().equals(strPassWord))
					return true;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * This method will create a file name from names of both players.
	 * Format:"_Player1_Player2"
	 * 
	 * @return Returns string of file name.
	 */
	private static String getFileName() {
		// TODO Auto-generated method stub
		TreeMap<String, Integer> treeMapTemp = new TreeMap<>();
		treeMapTemp.put(bfPlayer.strUserName, 0);
		treeMapTemp.put(bfComputer.strUserName, 0);
		String str = "";
		for (String s : treeMapTemp.keySet()) {
			str += "_" + s;
		}
		return str + ".txt";
	}

	/**
	 * This method will take data from class object and set them as current values.
	 * 
	 * @param saveAndLoad
	 *            Object of SaveAndLoadData class.
	 */
	private static void reloadParameters(SaveAndLoadData saveAndLoad) {
		// TODO Auto-generated method stub

		if (bfPlayer.strUserName.equals(saveAndLoad.strUser1) && bfComputer.strUserName.equals(saveAndLoad.strUser0)) {
			System.out.println(saveAndLoad.gameBoard1.size() + " : size of gameBoard");
			bfPlayer.gameBoard.clear();
			bfPlayer.gameBoard = saveAndLoad.gameBoard1;
			bfComputer.gameBoard.clear();
			bfComputer.gameBoard = saveAndLoad.gameBoard0;
			bfPlayer.intTotalAliveShips = saveAndLoad.intAliveShips1;
			bfComputer.intTotalAliveShips = saveAndLoad.intAliveShips0;
			bfPlayer.isReady = saveAndLoad.isSalva;
			bfComputer.isReady = saveAndLoad.isSalva;
			bfPlayer.intScore = saveAndLoad.intScore1;
			bfComputer.intScore = saveAndLoad.intScore0;
			bfPlayer.lstShip.clear();
			bfPlayer.lstShip = saveAndLoad.lstShip1;
			bfComputer.lstShip.clear();
			bfComputer.lstShip = saveAndLoad.lstShip0;
			bfPlayer.mapShipLocation.clear();
			bfPlayer.mapShipLocation = saveAndLoad.mapShipLocation1;
			bfComputer.mapShipLocation.clear();
			bfComputer.mapShipLocation = saveAndLoad.mapShipLocation0;

		} else if (bfPlayer.strUserName.equals(saveAndLoad.strUser0)
				&& bfComputer.strUserName.equals(saveAndLoad.strUser1)) {
			System.out.println(saveAndLoad.gameBoard1.size() + " : size of gameBoard");
			bfPlayer.gameBoard.clear();
			bfPlayer.gameBoard = saveAndLoad.gameBoard0;
			bfComputer.gameBoard.clear();
			bfComputer.gameBoard = saveAndLoad.gameBoard1;
			bfPlayer.intTotalAliveShips = saveAndLoad.intAliveShips0;
			bfComputer.intTotalAliveShips = saveAndLoad.intAliveShips1;
			bfPlayer.isReady = saveAndLoad.isSalva;
			bfComputer.isReady = saveAndLoad.isSalva;
			bfPlayer.intScore = saveAndLoad.intScore0;
			bfComputer.intScore = saveAndLoad.intScore1;
			bfPlayer.lstShip.clear();
			bfPlayer.lstShip = saveAndLoad.lstShip0;
			bfComputer.lstShip.clear();
			bfComputer.lstShip = saveAndLoad.lstShip1;
			bfPlayer.mapShipLocation.clear();
			bfPlayer.mapShipLocation = saveAndLoad.mapShipLocation0;
			bfComputer.mapShipLocation.clear();
			bfComputer.mapShipLocation = saveAndLoad.mapShipLocation1;
		}

	}

	/**
	 * This Method will save a state of a game at this moment in
	 * _player1_player2.txt file.
	 */
	private static void doSaveGame() {
		// TODO Auto-generated method stub
		SaveAndLoadData saveAndLoad = new SaveAndLoadData(bfPlayer.strUserName, bfComputer.strUserName,
				bfPlayer.gameBoard, bfComputer.gameBoard, bfPlayer.intTotalAliveShips, bfComputer.intTotalAliveShips,
				bfPlayer.isSalva, bfPlayer.intScore, bfComputer.intScore, bfPlayer.lstShip, bfComputer.lstShip,
				bfPlayer.mapShipLocation, bfComputer.mapShipLocation);
		FileOutputStream fout = null;
		ObjectOutputStream oos = null;
		TreeMap<String, Integer> treeMapTemp = new TreeMap<>();
		treeMapTemp.put(bfPlayer.strUserName, 0);
		treeMapTemp.put(bfComputer.strUserName, 0);
		String str = "";
		for (String s : treeMapTemp.keySet()) {
			str += "_" + s;
		}
		try {
			fout = new FileOutputStream("src\\" + str + ".txt");
			oos = new ObjectOutputStream(fout);
			oos.writeObject(saveAndLoad);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (fout != null) {
				try {
					fout.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (oos != null) {
				try {
					oos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * It will set a turn of computer player.
	 * 
	 * @param sd
	 *            Object of ServerData class.
	 */
	private static void setTurn(ServerData sd) {
		// TODO Auto-generated method stub
		isComputerTurn = sd.isComputerTurn;
	}

	/**
	 * 
	 * @param sd
	 *            Object of ServerData class.
	 * @return Returns 1 if both players are ready to play on same mode of the
	 *         game(Normal/Salva)<br>
	 *         Returns 2 if other player is ready to play but on salva mode which
	 *         different from player1<br>
	 *         Returns 3 if other player is ready to play but on Normal mode which
	 *         different from player1<br>
	 *         Returns 4 If other player is not ready to play
	 */
	private static int wantToStartGame(ServerData sd) {
		// TODO Auto-generated method stub
		if (sd.intPlayerId == 1) {
			bfPlayer.isReady = true;
			bfPlayer.isSalva = sd.isSalva;
			if (bfComputer.isReady) {
				if (bfPlayer.isSalva == bfComputer.isSalva)
					return 1;
				else if (bfComputer.isSalva) {
					bfPlayer.isReady = false;
					return 2;
				} else {
					bfPlayer.isReady = false;
					return 3;
				}
			} else
				return 4;
		} else {
			bfComputer.isReady = true;
			bfComputer.isSalva = sd.isSalva;
			if (bfPlayer.isReady) {
				if (bfPlayer.isSalva == bfComputer.isSalva)
					return 1;
				else if (bfPlayer.isSalva) {
					bfComputer.isReady = false;
					return 2;
				} else {
					bfComputer.isReady = false;
					return 3;
				}
			} else
				return 4;
		}

	}

	/**
	 * This method will set the ship.
	 * 
	 * @param sd
	 *            Object of ServerData class.
	 * @return Returns true if ship set successfully else returns false
	 */
	private static boolean doSetShip(ServerData sd) {
		// TODO Auto-generated method stub
		if (sd.intPlayerId == 1) {
			if (bfPlayer.setShip_method(sd.x, sd.y))
				return true;
			else
				return false;
		} else {
			if (bfComputer.setShip_method(sd.x, sd.y))
				return true;
			else
				return false;
		}
	}

	/**
	 * This method will add the ships in data structure;
	 * 
	 * @param sd
	 *            Object of ServerData class
	 */
	private static void doAddShip(ServerData sd) {
		// TODO Auto-generated method stub
		if (sd.intPlayerId == 1)
			bfPlayer.addShip(sd.size, sd.x, sd.y, sd.endX, sd.endY, sd.isDestroyed, sd.isRotated);
		else
			bfComputer.addShip(sd.size, sd.x, sd.y, sd.endX, sd.endY, sd.isDestroyed, sd.isRotated);
	}

	/**
	 * This method will check the hit is successful or not
	 * 
	 * @param sd
	 *            Object of ServerData class
	 * @return Returns object which contains couple of details
	 */
	private static ServerData checkIsHit(ServerData sd) {
		// TODO Auto-generated method stub
		if (sd.intPlayerId == 1) {
			return bfComputer.isHit(sd.x, sd.y, sd.isSalva, sd.i);
		} else {
			return bfPlayer.isHit(sd.x, sd.y, sd.isSalva, sd.i);
		}
	}

	/**
	 * This method will check that is all ships have correct position of not.
	 * 
	 * @param sd
	 *            Object of ServerData class
	 * @return Returns true if all ships having correct position
	 */
	private static boolean checkAllValidPositions(ServerData sd) {
		// TODO Auto-generated method stub
		if (sd.intPlayerId == 1) {
			if (bfPlayer.allValidPositions(sd.mapShipLocation))
				return true;
			else
				return false;
		} else {
			if (bfComputer.allValidPositions(sd.mapShipLocation))
				return true;
			else
				return false;
		}
	}

	/**
	 * This method will serialize the object
	 * 
	 * @param obj
	 *            Object which need to be serialized
	 * @return Returns serialized byte array
	 * @throws IOException
	 *             input output exception
	 */
	public static byte[] serialize(Object obj) throws IOException {
		try (ByteArrayOutputStream b = new ByteArrayOutputStream()) {
			try (ObjectOutputStream o = new ObjectOutputStream(b)) {
				o.writeObject(obj);
			}
			return b.toByteArray();
		}
	}

	/**
	 * This method will deserialize the byte array
	 * 
	 * @param bytes
	 *            Byte array which need to be deserialized
	 * @return Returns deserialized object
	 * @throws IOException
	 *             input output exception
	 * @throws ClassNotFoundException
	 *             Class not found exception
	 */
	public static Object deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
		try (ByteArrayInputStream b = new ByteArrayInputStream(bytes)) {
			try (ObjectInputStream o = new ObjectInputStream(b)) {
				return o.readObject();
			}
		}
	}

}
