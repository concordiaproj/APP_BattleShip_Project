package application.Models;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Server {
	public static BattleFieldPlayer bfPlayer;
	public static BattleFieldComputer bfComputer;
	public static int intServerPort = 1235;
	public static boolean isSalva = false;
	public static String strPlayerIP = "132.205.93.29";
	public static int intPlayerPort = 1234;
	public static String strComputerIP = "132.205.93.19";
	public static int intComputerPort = 1236;
	public static boolean isComputerTurn = false;

	public static void main(String[] args) throws IOException, ClassNotFoundException {
		// TODO Auto-generated method stub
		// 172.30.15.203
		bfPlayer = new BattleFieldPlayer();
		bfComputer = new BattleFieldComputer();
		InetAddress ip = InetAddress.getLocalHost();
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
			// Data dt=(Data) deserialize(request.getData());
			System.out.println(sd.intPlayerId + "in server");
			// System.out.println(new String(buffer) + " " + sd.intPlayerId + " " +
			// sd.intOperationId);
			// byte buf[] = null;
			// buf = ("Hi Everyone").getBytes();
			//
			// DatagramPacket DpSend = new DatagramPacket(buf, buf.length, ip, 1235);
			//
			// ds.send(DpSend);
		}
		// ds.close();
	}

	private static void doOperation(ServerData sd, DatagramPacket dp, DatagramSocket ds) throws IOException {
		// TODO Auto-generated method stub
		// DatagramSocket ds = new DatagramSocket(intServerPort);
		int intOperationId = sd.intOperationId;
		ServerData sdTemp = null;
		boolean boolReply = false;
		switch (intOperationId) {
		case 1:// onDiscartGame
			bfPlayer = new BattleFieldPlayer();
			bfComputer = new BattleFieldComputer();
			sdTemp = new ServerData(sd.intPlayerId, intOperationId, true);
			break;
		case 2:
			boolReply = checkAllValidPositions(sd);
			System.out.println("all Valid Position:" + boolReply);
			sdTemp = new ServerData(sd.intPlayerId, intOperationId, boolReply, bfPlayer.intScore, bfComputer.intScore,
					bfPlayer.intTotalAliveShips, bfComputer.intTotalAliveShips, '\0', -1, -1, false);
			break;
		case 3:// startGame //send reply to both
			int intReply = wantToStartGame(sd);
			sdTemp = new ServerData(sd.intPlayerId, intOperationId, intReply);
			break;
		case 4:// addShip
			doAddShip(sd);
			break;
		case 5:// isHit //send reply to both
			sdTemp = checkIsHit(sd);
			sdTemp.intPlayerId = sd.intPlayerId;
			sdTemp.intOperationId = intOperationId;
			sdTemp.intPlayerScore = bfPlayer.intScore;
			sdTemp.intCompScore = bfComputer.intScore;
			sdTemp.intPlayerAliveShip = bfPlayer.intTotalAliveShips;
			sdTemp.intCompAliveShip = bfComputer.intTotalAliveShips;
			break;
		case 6:// set computer turn // send reply to both
			setTurn(sd);
			sdTemp = new ServerData(sd.intPlayerId, intOperationId, isComputerTurn, false);
			break;
		case 9:// setShip
			boolReply = doSetShip(sd);
			sdTemp = new ServerData(sd.intPlayerId, intOperationId, boolReply, bfPlayer.intScore, bfComputer.intScore,
					bfPlayer.intTotalAliveShips, bfComputer.intTotalAliveShips, '\0', -1, -1, false);
			if (sdTemp == null)
				System.out.println("it is null");
			break;
		}
		if (sdTemp != null) {
			if (intOperationId == 1 || intOperationId == 3 || intOperationId == 5 || intOperationId == 6) {
				DatagramPacket DpSend = new DatagramPacket(serialize(sdTemp), serialize(sdTemp).length,
						InetAddress.getByName(strPlayerIP), intPlayerPort);

				ds.send(DpSend);
				DpSend = new DatagramPacket(serialize(sdTemp), serialize(sdTemp).length,
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
		// ds.close();
	}

	private static void setTurn(ServerData sd) {
		// TODO Auto-generated method stub
		isComputerTurn = sd.isComputerTurn;
	}

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

	private static void doAddShip(ServerData sd) {
		// TODO Auto-generated method stub
		if (sd.intPlayerId == 1)
			bfPlayer.addShip(sd.size, sd.x, sd.y, sd.endX, sd.endY, sd.isDestroyed, sd.isRotated);
		else
			bfComputer.addShip(sd.size, sd.x, sd.y, sd.endX, sd.endY, sd.isDestroyed, sd.isRotated);
	}

	private static ServerData checkIsHit(ServerData sd) {
		// TODO Auto-generated method stub
		if (sd.intPlayerId == 1) {
			return bfComputer.isHit(sd.x, sd.y, sd.isSalva, sd.i);
		} else {
			return bfPlayer.isHit(sd.x, sd.y, sd.isSalva, sd.i);
		}
	}

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

	// private static void initializeObjects(ServerData sd) {
	// // TODO Auto-generated method stub
	// System.out.println("initializing object");
	// if (sd.intPlayerId == 1)
	// bfPlayer = new BattleFieldPlayer();
	// else
	// bfComputer = new BattleFieldComputer();
	// }

	public static byte[] serialize(Object obj) throws IOException {
		try (ByteArrayOutputStream b = new ByteArrayOutputStream()) {
			try (ObjectOutputStream o = new ObjectOutputStream(b)) {
				o.writeObject(obj);
			}
			return b.toByteArray();
		}
	}

	public static Object deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
		try (ByteArrayInputStream b = new ByteArrayInputStream(bytes)) {
			try (ObjectInputStream o = new ObjectInputStream(b)) {
				return o.readObject();
			}
		}
	}

}
