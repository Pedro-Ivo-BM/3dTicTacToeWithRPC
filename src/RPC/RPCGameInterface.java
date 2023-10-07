package RPC;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RPCGameInterface extends Remote {

	void sendMessage(String message) throws RemoteException;

	void sendSystemMessage(String message) throws RemoteException;

	void setMeAsFirstPlayer() throws RemoteException;

	void setPlayerTurn(int player) throws RemoteException;

	void setPlayerAction(String choosedCell, int player) throws RemoteException;

	void setActualPlayer(int actualPlayer) throws RemoteException;
}
