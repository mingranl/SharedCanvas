package remote;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

import whiteboard.Graph;

public interface IServerOperation extends Remote{

	// client operations
	public void addClient(IRemoteClient client) throws RemoteException;
	
	// whiteboard operations
	public ArrayList<Graph> getGraps() throws RemoteException;
	public void testString() throws RemoteException;
	public void updateElements(int clientID, Graph element) throws RemoteException;

	// send permission request to manager
	public boolean permission(String name) throws RemoteException;
	//public void addSet() throws RemoteException;
}
