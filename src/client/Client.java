package client;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import remote.IRemoteClient;
import remote.IServerOperation;
import whiteboard.Graph;
import whiteboard.MyDraw;

public class Client  extends UnicastRemoteObject implements IRemoteClient {
	
	private String status;
	private int clientID;
	private String name;
	private MyDraw md;
	
	Client(String status, String name) throws RemoteException {
		this.status = status;
		this.name = name;
	}
	
	public MyDraw getMd() {
		return md;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getStatus() {
		return status;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public void setClientID(int clientID) {
		this.clientID = clientID;
	}
	
	public int getClientID() {
		return clientID;
	}
	
	// initialize canvas
	public void initializition(ArrayList<Graph> graps) {
		md.mp.setGraps(graps);
		md.mp.repaint();
	}
	
	@Override
	public void addMyDraw(IServerOperation remoteserver) {
		md = new MyDraw(clientID,remoteserver);
		md.frame.setVisible(true);
	}

	@Override
	public void alert(String msg) throws RemoteException {
		// TODO Auto-generated method stub
		JOptionPane.showMessageDialog(null, msg);  
	}

	@Override
	public void updateClientElement(Graph element) throws RemoteException {
		// TODO Auto-generated method stub
		md.mp.getGraps().add(element);
		md.mp.repaint();
	}

	@Override
	public boolean authorization(String name) throws RemoteException {
		// TODO Auto-generated method stub
		int n = JOptionPane.showConfirmDialog(null, "Allow " + name + " access to this room?", "authorization", JOptionPane.YES_NO_OPTION);
		if(n == 0) {
			return true;
		}
		return false;
	}
}
