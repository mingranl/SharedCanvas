package whiteboard;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import remote.IRemoteClient;
import remote.IServerOperation;

public class MyPanel extends JPanel {
	
	private int clientID;
	private IServerOperation remoteserver;

	Graphics2D g2;

	// list that store all graphics
	private List<Graph> graps = new ArrayList<Graph>();

	MyPanel(int clientID, IServerOperation remoteserver){
		this.clientID = clientID;
		this.remoteserver = remoteserver;
	}
	
	// set graps, used to initialize the canvas
	public void setGraps(ArrayList<Graph> graps) {
		this.graps = graps;
	}
	
	// get graps
	public List<Graph> getGraps(){
		return graps;
	}
	
	// free draw class
	public static class Free implements Graph,Serializable {
		int x, y, color, stroke;

		public Free(int x, int y, int color, int stroke) {
			this.x = x;
			this.y = y;
			this.color = color;
			this.stroke = stroke;
		}

		@Override
		public int getType() {
			// TODO Auto-generated method stub
			return TYPE_FREE;
		}
	}

	// line class
	public static class Line implements Graph,Serializable {
		int x1, y1, x2, y2, color, stroke;

		public Line(int x1, int y1, int x2, int y2, int color, int stroke) {
			this.x1 = x1;
			this.y1 = y1;
			this.x2 = x2;
			this.y2 = y2;
			this.color = color;
			this.stroke = stroke;
		}

		@Override
		public int getType() {
			// TODO Auto-generated method stub
			return TYPE_LINE;
		}
	}

	// circle class
	public static class Circle implements Graph,Serializable {
		int x1, y1, x2, y2, color, stroke;

		public Circle(int x1, int y1, int x2, int y2, int color, int stroke) {
			this.x1 = x1;
			this.y1 = y1;
			this.x2 = x2;
			this.y2 = y2;
			this.color = color;
			this.stroke = stroke;
		}

		@Override
		public int getType() {
			// TODO Auto-generated method stub
			return TYPE_CIRCLE;
		}
	}

	// oval class
	public static class Oval implements Graph,Serializable {
		int x1, y1, x2, y2, color, stroke;

		public Oval(int x1, int y1, int x2, int y2, int color, int stroke) {
			this.x1 = x1;
			this.y1 = y1;
			this.x2 = x2;
			this.y2 = y2;
			this.color = color;
			this.stroke = stroke;
		}

		@Override
		public int getType() {
			// TODO Auto-generated method stub
			return TYPE_OVAL;
		}
	}

	// rectangle class
	public static class Rect implements Graph,Serializable {
		int x1, y1, x2, y2, color, stroke;

		public Rect(int x1, int y1, int x2, int y2, int color, int stroke) {
			this.x1 = x1;
			this.y1 = y1;
			this.x2 = x2;
			this.y2 = y2;
			this.color = color;
			this.stroke = stroke;
		}

		@Override
		public int getType() {
			// TODO Auto-generated method stub
			return TYPE_RECT;
		}
	}

	// eraser class
	public static class Eraser implements Graph,Serializable {
		int x, y, stroke;

		public Eraser(int x, int y, int stroke) {
			this.x = x;
			this.y = y;
			this.stroke = stroke;
		}

		@Override
		public int getType() {
			// TODO Auto-generated method stub
			return TYPE_ERASER;
		}
	}
	
	//image class
	public static class Image implements Graph,Serializable{
		String path;
		
		public Image(String path){
			this.path=path;
		}
		
		@Override
		public int getType() {
			// TODO Auto-generated method stub
			return TYPE_IMAGE;
		}
		
	}

	public void freeDraw(int x, int y, int color, int stroke) {
		Free free = new Free(x, y, color, stroke);
		graps.add(free);
		try {
			remoteserver.updateElements(clientID, free);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void drawLine(int x1, int y1, int x2, int y2, int color, int stroke) {
		Line line = new Line(x1, y1, x2, y2, color, stroke);
		graps.add(line);
		try {
			remoteserver.updateElements(clientID, line);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void drawCircle(int x1, int y1, int x2, int y2, int color, int stroke) {
		Circle circle = new Circle(x1, y1, x2, y2, color, stroke);
		graps.add(circle);
		try {
			remoteserver.updateElements(clientID, circle);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void drawOval(int x1, int y1, int x2, int y2, int color, int stroke) {
		Oval oval = new Oval(x1, y1, x2, y2, color, stroke);
		graps.add(oval);
		try {
			remoteserver.updateElements(clientID, oval);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void drawRect(int x1, int y1, int x2, int y2, int color, int stroke) {
		Rect rect = new Rect(x1, y1, x2, y2, color, stroke);
		graps.add(rect);
		try {
			remoteserver.updateElements(clientID, rect);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void Eraser(int x, int y, int stroke) {
		Eraser eraser = new Eraser(x, y, stroke);
		graps.add(eraser);
		try {
			remoteserver.updateElements(clientID, eraser);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void Image(String path){
        Image image=new Image(path);
        graps.add(image);
        try {
			remoteserver.updateElements(clientID, image);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void colorSelect(int color) {
		// paint color
		int colorSelect = color;
		switch (colorSelect) {
		case 0:
			g2.setColor(Color.BLACK);
			break;
		case 1:
			g2.setColor(Color.BLUE);
			break;
		case 2:
			g2.setColor(Color.CYAN);
			break;
		case 3:
			g2.setColor(Color.DARK_GRAY);
			break;
		case 4:
			g2.setColor(Color.GRAY);
			break;
		case 5:
			g2.setColor(Color.LIGHT_GRAY);
			break;
		case 6:
			g2.setColor(Color.GREEN);
			break;
		case 7:
			g2.setColor(Color.MAGENTA);
			break;
		case 8:
			g2.setColor(Color.ORANGE);
			break;
		case 9:
			g2.setColor(Color.PINK);
			break;
		case 10:
			g2.setColor(Color.RED);
			break;
		case 11:
			g2.setColor(Color.WHITE);
			break;
		case 12:
			g2.setColor(Color.YELLOW);
			break;
		}
	}

	protected void paintComponent(Graphics g) {

		super.paintComponent(g);
		g2 = (Graphics2D) g;

		// g2.setStroke(new BasicStroke(1));
		for (Graph graph : graps) {
			switch (graph.getType()) {
			case Graph.TYPE_FREE:
				Free free = (Free) graph;
				colorSelect(free.color);
				g2.setStroke(new BasicStroke(free.stroke + 1));
				g2.drawRect(free.x, free.y, free.stroke, free.stroke);
				break;

			case Graph.TYPE_LINE:
				Line line = (Line) graph;
				colorSelect(line.color);
				g2.setStroke(new BasicStroke(line.stroke + 1));
				g2.drawLine(line.x1, line.y1, line.x2, line.y2);
				break;

			case Graph.TYPE_CIRCLE:
				Circle circle = (Circle) graph;
				int widthC = Math.abs(circle.x2 - circle.x1);
				int heightC = Math.abs(circle.y2 - circle.y1);
				int r = 0;
				if (widthC <= heightC)
					r = widthC;
				if (widthC > heightC)
					r = heightC;
				int xC = 0;
				int yC = 0;
				if (circle.x1 < circle.x2)
					xC = circle.x1;
				if (circle.x1 > circle.x2)
					xC = circle.x2;
				if (circle.y1 < circle.y2)
					yC = circle.y1;
				if (circle.y1 > circle.y2)
					yC = circle.y2;
				colorSelect(circle.color);
				g2.setStroke(new BasicStroke(circle.stroke + 1));
				g2.drawOval(xC, yC, r, r);
				break;

			case Graph.TYPE_OVAL:
				Oval oval = (Oval) graph;
				int widthO = Math.abs(oval.x2 - oval.x1);
				int heightO = Math.abs(oval.y2 - oval.y1);
				int xO = 0;
				int yO = 0;
				if (oval.x1 < oval.x2)
					xO = oval.x1;
				if (oval.x1 > oval.x2)
					xO = oval.x2;
				if (oval.y1 < oval.y2)
					yO = oval.y1;
				if (oval.y1 > oval.y2)
					yO = oval.y2;
				colorSelect(oval.color);
				g2.setStroke(new BasicStroke(oval.stroke + 1));
				g2.drawOval(xO, yO, widthO, heightO);
				break;

			case Graph.TYPE_RECT:
				Rect rect = (Rect) graph;
				int widthR = Math.abs(rect.x2 - rect.x1);
				int heightR = Math.abs(rect.y2 - rect.y1);
				int xR = 0;
				int yR = 0;
				if (rect.x1 < rect.x2)
					xR = rect.x1;
				if (rect.x1 > rect.x2)
					xR = rect.x2;
				if (rect.y1 < rect.y2)
					yR = rect.y1;
				if (rect.y1 > rect.y2)
					yR = rect.y2;
				colorSelect(rect.color);
				g2.setStroke(new BasicStroke(rect.stroke + 1));
				g2.drawRect(xR, yR, widthR, heightR);
				break;

			case Graph.TYPE_ERASER:
				Eraser eraser = (Eraser) graph;
				g2.setColor(Color.WHITE);
				g2.setStroke(new BasicStroke(eraser.stroke + 1));
				g2.drawRect(eraser.x, eraser.y, eraser.stroke, eraser.stroke);
				break;
				
			case Graph.TYPE_IMAGE:
				Image image=(Image) graph;
				String str;
				BufferedImage bi;
				try {
					str=image.path;
					bi = ImageIO.read(new File(str));
			        g.drawImage(bi, 0, 0, bi.getWidth(), bi.getHeight(), null);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}

	}
}