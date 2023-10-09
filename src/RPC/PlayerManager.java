package RPC;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Random;
import java.util.function.Consumer;

import game.GameManager;

public class PlayerManager extends UnicastRemoteObject implements RPCGameInterface {
	public String guestLastMessage = "";
	public String guestSystemLastMessage = "";
	public String guestLastPlay = "";

	public boolean isGameOn = false;
	public final int thisPlayer;
	public int actualPlayer = 0;
	public boolean iAmFirst = false;
	public int countPlaysForDraw = 0;

	public RPCGameInterface guestRPCConnection;

	public static final int PORT1 = 3000;
	public static final int PORT2 = 3001;
	public static final String HOST1 = "rmi://localhost:" + PORT1 + "/3dTicTacToe";
	public static final String HOST2 = "rmi://localhost:" + PORT2 + "/3dTicTacToe";

	public GameManager gameManager = new GameManager();

	public PlayerManager(int playerNumber) throws RemoteException {
		super();
		this.thisPlayer = playerNumber;

	}

	public boolean startConnectionToOtherPlayer() {
		try {
			this.guestRPCConnection = thisPlayer == 1 ? (RPCGameInterface) Naming.lookup(PlayerManager.HOST2)
					: (RPCGameInterface) Naming.lookup(PlayerManager.HOST1);
			return true;
		} catch (Exception e) {
			System.out.println("Erro");
			return false;
		}
	}

	@Override
	public void sendMessage(String message) throws RemoteException {
		guestLastMessage = message;
	}

	@Override
	public void setMeAsFirstPlayer() throws RemoteException {
		iAmFirst = true;

	}

	@Override
	public void sendSystemMessage(String message) throws RemoteException {
		guestSystemLastMessage = message;

	}

	@Override
	public void setPlayerTurn(int player) throws RemoteException {
		actualPlayer = player;

	}

	@Override
	public void setPlayerAction(String choosedCell, int player) throws RemoteException {
		int board = Character.getNumericValue(choosedCell.charAt(0));
		int line = Character.getNumericValue(choosedCell.charAt(1));
		int column = Character.getNumericValue(choosedCell.charAt(2));

		gameManager.makePlay(board, line, column, player);

		countPlaysForDraw += 1;

		this.guestLastPlay = "" + choosedCell + player;

	}

	@Override
	public void setActualPlayer(int actualPlayer) throws RemoteException {
		this.actualPlayer = actualPlayer;

	}

}
