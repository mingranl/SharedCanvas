package remote;

import java.rmi.Remote;
import java.rmi.RemoteException;

import remote.IRemoteClient;

public interface IRoomManagement extends Remote {
	
	public String LOOKUP_NAME = "Management";
	
	public IServerOperation createRoom(IRemoteClient manager, String roomname) throws RemoteException ;
	public IServerOperation getRoom(IRemoteClient client, String roomname) throws RemoteException ;
	public boolean closeRoom() throws RemoteException ;
	//public void createRoom() throws RemoteException ;
	//public void createRoom() throws RemoteException ;
	public boolean checkStatus(IRemoteClient manager) throws RemoteException;
}
