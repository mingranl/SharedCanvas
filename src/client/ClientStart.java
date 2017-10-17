package client;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

import javax.swing.JOptionPane;

import remote.IRemoteClient;
import remote.IRoomManagement;
import remote.IServerOperation;
import whiteboard.MyDraw;

public class ClientStart {
	public static void main(String[] args) throws RemoteException, NotBoundException {
		//Retrieve the stub/proxy for the remote object from the registry
		Registry registry = LocateRegistry.getRegistry("localhost");
		
		IRoomManagement sharedCanvas = (IRoomManagement) registry.lookup(IRoomManagement.LOOKUP_NAME);
		
		System.out.println("Input:");
		Scanner sc = new Scanner(System.in);
		String roomname = sc.next();
		String status = sc.next();
		String name = sc.nextLine();
		
		//String roomname ="whiteboard1";
		//IRemoteClient manager = new Client("Manager", "Feng Zhao");
		// create new client entity
		IRemoteClient client = new Client(status, name);
		
		// create or get room remote server
		IServerOperation remoteserver = null;
		if(status.equals("Manager")) {
			remoteserver = sharedCanvas.createRoom(client, roomname);
			remoteserver.addClient(client);
			client.addMyDraw(remoteserver);
		}
		else if(status.equals("Member")){
			remoteserver = sharedCanvas.getRoom(client, roomname);
			boolean permission = remoteserver.permission(name);
			if(permission) {
				// add client to room and open client canvas
				remoteserver.addClient(client);
				client.addMyDraw(remoteserver);
				client.initializition(remoteserver.getGraps());
			}
			else {
				JOptionPane.showMessageDialog(null, "You are not allowed to join!");  
				remoteserver = null;
			}
		}
	}
}
