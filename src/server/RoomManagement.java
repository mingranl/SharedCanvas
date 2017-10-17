package server;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import remote.IRoomManagement;
import remote.IServerOperation;
import remote.IRemoteClient;
import remote.IServerOperation;

public class RoomManagement extends UnicastRemoteObject implements IRoomManagement  {

	private static int roomNum;
	private Map<String ,IServerOperation> roomMap;
	//private Map<IServerOperation ,ArrayList<IRemoteClient>> clientsMap;
	private Registry registry;
	public static final String SERVER_NAME = "Whiteboard";
	public static final int SERVER_PORT = 1099;
	
	protected RoomManagement() throws RemoteException {
		super();
		// TODO Auto-generated constructor stub
		roomNum = 0;
		roomMap = new ConcurrentHashMap<String, IServerOperation>();
		registry = LocateRegistry.getRegistry("localhost");
	}
	
	@Override
	public IServerOperation createRoom(IRemoteClient client, String roomname) throws RemoteException {
		if(this.checkStatus(client)) {
			if(roomMap.containsKey(roomname)) {
				String msg = "room name exist";
				client.alert(msg);
				return null;
			}else {
				roomNum++;
				IServerOperation remoteserver = new ServerOperation(roomname);
				//remoteserver.addSet();
				// bind this remote server by using room name
				try {
					registry.bind(roomname, remoteserver);
				} catch (AlreadyBoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				roomMap.put(roomname, remoteserver);
				String msg = roomname + " created";
				client.alert(msg);
				return remoteserver;
			}
		}else {
			client.alert("unauthorized");
			return null;
		}
	}

	@Override
	public IServerOperation getRoom(IRemoteClient client, String roomname) throws RemoteException {
		// TODO Auto-generated method stub
		if(roomMap.containsKey(roomname)) {
			client.alert(roomname + " found! Please wait for permission!");
			return roomMap.get(roomname);
		}else {
			client.alert(roomname+" does not exist.");
			return null;
		}
	}

	@Override
	public boolean closeRoom() throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean checkStatus(IRemoteClient client) throws RemoteException {
		if(client.getStatus().equals("Manager")) {
			return true;
		}			
		else {
			return false;
		}
	}
	
}
