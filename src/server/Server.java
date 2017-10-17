package server;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import remote.IRoomManagement;

public class Server {
	public static final String SERVER_NAME = "Whiteboard";
    public static final int SERVER_PORT = 1099;
public static void main(String[] args)  {
		try {
			IRoomManagement rm = new RoomManagement();
            Registry registry = LocateRegistry.createRegistry(SERVER_PORT);          
            registry.bind(IRoomManagement.LOOKUP_NAME, rm);      
            System.out.println("Server ready!");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
