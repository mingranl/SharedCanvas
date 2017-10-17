package remote;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

import whiteboard.Graph;
import whiteboard.MyDraw;

public interface IRemoteClient extends Remote{

	// methods about client entity
	public void initializition(ArrayList<Graph> graps) throws RemoteException;
	public MyDraw getMd() throws RemoteException;
	public void setStatus(String status) throws RemoteException;
	public void setClientID(int clientID) throws RemoteException;
	public void setName(String name) throws RemoteException;
	public String getStatus() throws RemoteException;
	public int getClientID() throws RemoteException;
	public String getName() throws RemoteException;
	
	// notification method
	public void alert(String msg) throws RemoteException ;

	// create local whiteboard
	public void addMyDraw(IServerOperation remoteserver) throws RemoteException;
	
	// update element in the room
	public void updateClientElement(Graph element) throws RemoteException;
	
	// authorization method
	public boolean authorization(String name) throws RemoteException;

}
