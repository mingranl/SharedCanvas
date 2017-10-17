package whiteboard;

import java.awt.AWTException;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Robot;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

import remote.IRemoteClient;
import remote.IServerOperation;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;

public class MyDraw extends JFrame {

	private int clientID;
	private IServerOperation remoteserver;
	
	private JScrollPane scrollPane;
	DefaultListModel listModel;
	JList list;
	
	public JFrame frame;
	public MyPanel mp = null;
	Graphics2D ggg;
	JButton line_btn;
	JButton circle_btn;
	JButton oval_btn;
	JButton rect_btn;
	JButton erase_btn;
	JButton free_btn;
	JButton clean_btn;
	JButton open;
	JButton save;
	JButton save_as;
	JComboBox jcb_color;
	JComboBox jcb_stroke;
	
	private int px = 0;
	private int py = 0;
	private int rx = 0;
	private int ry = 0;
	private int color=0;
	private int stroke=0;
	private int flag=0;

	// related to open save and saveas methods
	private boolean isSave = false;
	private String path = null;
	
	
	private String[] user_list={"a","b","c"};
	/**
	 * Launch the application.
	 
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MyDraw window = new MyDraw();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}*/

	/**
	 * Create the application.
	 */
	public MyDraw(int clientID, IServerOperation remoteserver) {
		this.clientID = clientID;
		this.remoteserver = remoteserver;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame("My Draw");
		frame.setBounds(30, 50, 1800, 800);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		mp = new MyPanel(clientID, remoteserver);
		mp.setBackground(Color.WHITE);
		mp.setBounds(210, 100, 1300, 500);
		mp.setLayout(null);
		//mp.setOpaque(false);
		frame.getContentPane().add(mp);
		

		JList jl=new JList(user_list);
		jl.setBounds(1160, 400, 100, 200);
		frame.getContentPane().add(jl);
		
		String strColor[] = { "black", "blue", "cyan", "dark gray", "gray", "light gray", "green", "magenta", "orange",
				"pink", "red", "white", "yellow" };
		jcb_color = new JComboBox(strColor);
		jcb_color.setBounds(1550, 150, 100, 30);
		jcb_color.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				int index=jcb_color.getSelectedIndex();
				System.out.println(index);
				color=index;
			}
		});
		frame.getContentPane().add(jcb_color);
		
		String strStroke[]={"1","2","3","4","5"};
		jcb_stroke=new JComboBox(strStroke);
		jcb_stroke.setBounds(1550, 400, 100, 30);
		jcb_stroke.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				int index=jcb_stroke.getSelectedIndex();
				System.out.println(index);
				stroke=index;
			}
		});
		frame.getContentPane().add(jcb_stroke);
		
		//open button
		open=new JButton("Open");
		open.setBounds(10,10,100,30);
		frame.getContentPane().add(open);
		open.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				try {
					open();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				mp.Image(path);
				mp.repaint();
			}
					
		});
				
		//save button
		save=new JButton("Save");
		save.setBounds(160,10,100,30);
		frame.getContentPane().add(save);
		save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(isSave) {
					try {
						save();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else {
					try {
						saveAs();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
				
		//save as button
		save_as=new JButton("Save As");
		save_as.setBounds(310,10,100,30);
		frame.getContentPane().add(save_as);
		save_as.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					saveAs();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		//free draw button
		free_btn=new JButton("free draw");
		free_btn.setBounds(50, 100, 100, 30);
		frame.getContentPane().add(free_btn);
		free_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.out.print("clicked free draw button\n");
				MouseListener[] listeners = mp.getMouseListeners();
				for (MouseListener listener : listeners) {
					mp.removeMouseListener(listener);
				}
				MouseMotionListener[] mmls=mp.getMouseMotionListeners();
				for(MouseMotionListener mml:mmls){
					mp.removeMouseMotionListener(mml);
				}
				mp.addMouseMotionListener(new MouseAdapter(){
					public void mouseDragged(MouseEvent e){
						px=e.getX();
						py=e.getY();
						mp.freeDraw(px, py, color, stroke);
						mp.repaint();
					}
				});
			}
		});
			
		//draw line button
		line_btn=new JButton("Draw Line");
		line_btn.setBounds(50, 150, 150, 30);
		frame.getContentPane().add(line_btn);
		line_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.out.print("clicked draw line button\n");
				MouseListener[] listeners = mp.getMouseListeners();
				for (MouseListener listener : listeners) {
					mp.removeMouseListener(listener);
				}
				MouseMotionListener[] mmls=mp.getMouseMotionListeners();
				for(MouseMotionListener mml:mmls){
					mp.removeMouseMotionListener(mml);
				}
				mp.addMouseListener(new MouseAdapter() {
					public void mousePressed(MouseEvent e) {
						px = e.getX();
						py = e.getY();
						System.out.println("mouse pressed at point (" + px + "," + py + ")\n");
					}
					public void mouseReleased(MouseEvent e) {
						rx = e.getX();
						ry = e.getY();
						System.out.println("mouse released at point (" + rx + "," + ry + ")\n");
						mp.drawLine(px, py, rx, ry,color,stroke);
						mp.repaint();
					}
				});
			}
		});
		
		//draw circle button
		circle_btn = new JButton("Draw Circle");
		circle_btn.setBounds(50, 200, 150, 30);
		frame.getContentPane().add(circle_btn);
		circle_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.out.print("clicked draw oval button\n");
				MouseListener[] listeners = mp.getMouseListeners();
				for (MouseListener listener : listeners) {
					mp.removeMouseListener(listener);
				}
				MouseMotionListener[] mmls=mp.getMouseMotionListeners();
				for(MouseMotionListener mml:mmls){
					mp.removeMouseMotionListener(mml);
				}
				mp.addMouseListener(new MouseAdapter() {
					public void mousePressed(MouseEvent e) {
						px = e.getX();
						py = e.getY();
						System.out.println("mouse pressed at point (" + px + "," + py + ")\n");
					}
					public void mouseReleased(MouseEvent e) {
						rx = e.getX();
						ry = e.getY();
						System.out.println("mouse released at point (" + rx + "," + ry + ")\n");
						mp.drawCircle(px, py, rx, ry,color,stroke);
						mp.repaint();
					}
				});
			}
		});

		//draw oval button
		oval_btn = new JButton("Draw Oval");
		oval_btn.setBounds(50, 250, 150, 30);
		frame.getContentPane().add(oval_btn);
		oval_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.out.print("clicked draw oval button\n");
				MouseListener[] listeners = mp.getMouseListeners();
				for (MouseListener listener : listeners) {
					mp.removeMouseListener(listener);
				}
				MouseMotionListener[] mmls=mp.getMouseMotionListeners();
				for(MouseMotionListener mml:mmls){
					mp.removeMouseMotionListener(mml);
				}
				mp.addMouseListener(new MouseAdapter() {
					public void mousePressed(MouseEvent e) {
						px = e.getX();
						py = e.getY();
						System.out.println("mouse pressed at point (" + px + "," + py + ")\n");
					}
					public void mouseReleased(MouseEvent e) {
						rx = e.getX();
						ry = e.getY();
						System.out.println("mouse released at point (" + rx + "," + ry + ")\n");
						mp.drawOval(px, py, rx, ry,color,stroke);
						mp.repaint();
					}
				});
			}
		});

		//draw rectangle button
		rect_btn = new JButton("Draw rectangle");
		rect_btn.setBounds(50, 300, 150, 30);
		frame.getContentPane().add(rect_btn);
		rect_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.out.print("clicked draw rectangle button\n");
				MouseListener[] listeners = mp.getMouseListeners();
				for (MouseListener listener : listeners) {
					mp.removeMouseListener(listener);
				}
				MouseMotionListener[] mmls=mp.getMouseMotionListeners();
				for(MouseMotionListener mml:mmls){
					mp.removeMouseMotionListener(mml);
				}
				mp.addMouseListener(new MouseAdapter() {
					public void mousePressed(MouseEvent e) {
						px = e.getX();
						py = e.getY();
						System.out.println("mouse pressed at point (" + px + "," + py + ")\n");
					}
					public void mouseReleased(MouseEvent e) {
						rx = e.getX();
						ry = e.getY();
						System.out.println("mouse released at point (" + rx + "," + ry + ")\n");
						mp.drawRect(px, py, rx, ry,color,stroke);
						mp.repaint();
					}
				});
			}
		});
		
		//eraser button
		erase_btn=new JButton("Eraser");
		erase_btn.setBounds(50, 350, 80, 30);
		frame.getContentPane().add(erase_btn);
		erase_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.out.print("clicked Eraser button\n");
				MouseListener[] listeners = mp.getMouseListeners();
				for (MouseListener listener : listeners) {
					mp.removeMouseListener(listener);
				}
				MouseMotionListener[] mmls=mp.getMouseMotionListeners();
				for(MouseMotionListener mml:mmls){
					mp.removeMouseMotionListener(mml);
				}
				mp.addMouseMotionListener(new MouseAdapter(){
					public void mouseDragged(MouseEvent e){
						px=e.getX();
						py=e.getY();
						mp.Eraser(px, py, stroke);
						mp.repaint();
					}
				});
			}
		});
		
		//clean button
		clean_btn=new JButton("Clean");
		clean_btn.setBounds(50, 400, 80, 30);
		frame.getContentPane().add(clean_btn);
		clean_btn.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				mp.Eraser(0, 0, 1000);
				mp.repaint();
			}
			
		});
		
		//JList
		listModel= new DefaultListModel();
		list = new JList(listModel);
		scrollPane = new JScrollPane(list,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setBounds(1044, 42, 120, 142);
		frame.getContentPane().add(scrollPane);
	}
	
	// open save and saveas methods
	public void open() throws IOException{
		File file = null;
		
		JFileChooser chooseFile = new JFileChooser();
		chooseFile.setDialogTitle("Open ...");
		MyFilter jpgFilter = new MyFilter(".jpg","JEPG format");
		MyFilter pngFilter = new MyFilter(".png","PNG format");
		chooseFile.addChoosableFileFilter(jpgFilter);
		chooseFile.addChoosableFileFilter(pngFilter);

		int returnVal = chooseFile.showOpenDialog(null);
		chooseFile.setVisible(true);
		
		switch (returnVal) {
		case JFileChooser.APPROVE_OPTION:
		{
			file = chooseFile.getSelectedFile().getAbsoluteFile();
			path = file.getAbsolutePath();
			if(!path.split("\\.")[path.split("\\.").length - 1].equals("jpg") && !path.split("\\.")[path.split("\\.").length - 1].equals("png"))
			{
				JOptionPane.showMessageDialog(MyDraw.this, "Wrong file format!");break;
			}
			isSave = true;
		}break;
		case JFileChooser.CANCEL_OPTION:
			break;
		case JFileChooser.ERROR_OPTION:
			break;
		default:
			break;
		}
	}
	
	public void save() throws IOException{
		File file = new File(path);
		BufferedImage myImage = null;
		OutputStream ops;
		
		try {
			myImage = new Robot().createScreenCapture(  
			        new Rectangle(mp.getLocationOnScreen().x, mp.getLocationOnScreen().y, mp.getWidth(), mp.getHeight()));
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ops = new FileOutputStream(file);
		ImageIO.write(myImage, path.split("\\.")[path.split("\\.").length - 1], ops);  
		ops.close();
	}
	
	public void saveAs() throws IOException {
		BufferedImage myImage = null;  
		File file = null;
		File newFile = null;
		
		JFileChooser chooseFile = new JFileChooser();
		chooseFile.setDialogTitle("Save As ...");
		MyFilter jpgFilter = new MyFilter(".jpg","JEPG format");
		MyFilter pngFilter = new MyFilter(".png","PNG format");
		chooseFile.addChoosableFileFilter(jpgFilter);
		chooseFile.addChoosableFileFilter(pngFilter);

		int returnVal = chooseFile.showSaveDialog(null);
		chooseFile.setVisible(true);
		
		switch (returnVal) {
		case JFileChooser.APPROVE_OPTION:
			file = chooseFile.getSelectedFile().getAbsoluteFile();
			MyFilter filter = (MyFilter) chooseFile.getFileFilter();
			String ends = filter.getEnds();
            try {
            	OutputStream ops;
            	myImage = new Robot().createScreenCapture(  
	                    new Rectangle(mp.getLocationOnScreen().x, mp.getLocationOnScreen().y, mp.getWidth(), mp.getHeight()));
            	if (file.getAbsolutePath().toUpperCase().endsWith(ends.toUpperCase())) {
    			    newFile = file;
    			} else {
    			    newFile = new File(file.getAbsolutePath() + ends);
    			}
            	if(newFile.exists()) { 
                    int i = JOptionPane.showConfirmDialog(MyDraw.this, "Same file name£¬do you want to overwrite it£¿");     
                    if(i == JOptionPane.YES_OPTION) {
                    	
                    }     
                    else {
                    	return ;    
                    }
                } 
            	ops = new FileOutputStream(newFile);
            	ImageIO.write(myImage, ends.substring(1), ops);  
            	ops.close();
            } catch (FileNotFoundException e1) {
                JOptionPane.showMessageDialog(MyDraw.this, "Saving Error:"+e1.getMessage());
            } catch (IOException e1) {
                e1.printStackTrace();
            } catch (AWTException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}break;
		case JFileChooser.CANCEL_OPTION:
			break;
		case JFileChooser.ERROR_OPTION:
			break;
		default:
			break;
		}
	}
}
