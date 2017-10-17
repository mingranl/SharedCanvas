package server;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.swing.JOptionPane;

import remote.IRemoteClient;
import remote.IServerOperation;
import whiteboard.Graph;

public class ServerOperation extends UnicastRemoteObject  implements IServerOperation {

	String roomName;
	
	// list all clients
	ArrayList<IRemoteClient> clients;
	
	// list that store all graphics
	ArrayList<Graph> graps;
	
	ServerOperation(String roomName) throws RemoteException {
		super();
		this.roomName = roomName;
		clients = new ArrayList<IRemoteClient>();
		graps = new ArrayList<Graph>();
	}
	
	/*public void addSet() {
		clients = new ArrayList<IRemoteClient>();
		graps = new ArrayList<Graph>();
	}*/
	
	@Override
	public void addClient(IRemoteClient client) throws RemoteException {
		// TODO Auto-generated method stub
		int clientNum = clients.size();
		client.setClientID(clientNum);
		clients.add(client);
	}

	@Override
	public void testString() throws RemoteException {
		// TODO Auto-generated method stub
		JOptionPane.showMessageDialog(null, "testSting() in ServerOperation!"); 
	}

	//@Override
	public void updateElements(int clientID, Graph element) throws RemoteException {
		// TODO Auto-generated method stub
		graps.add(element);
		for(IRemoteClient client : clients) {
			System.out.println("clients id: " + client.getClientID());
			System.out.println("clients name: " + client.getName());
			if(client.getClientID() != clientID) {
				client.updateClientElement(element);
			}
		}
		for(int i = 0; i < clients.size(); i++) {
			
		}
		System.out.println("clients size: " + clients.size());
		System.out.println("graps size: " + graps.size());
	}

	@Override
	public boolean permission(String name) throws RemoteException {
		// TODO Auto-generated method stub
		return clients.get(0).authorization(name);
	}

	@Override
	public ArrayList<Graph> getGraps() throws RemoteException {
		// TODO Auto-generated method stub
		return graps;
	}
}
