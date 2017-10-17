package whiteboard;

import java.awt.AWTException;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.TextArea;
import java.awt.TextField;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
	JComboBox jcb_color;
	JComboBox jcb_stroke;
	JLabel color_select;
	JLabel stroke_select;
	JLabel user_list;
	JLabel chat_history;
	JLabel chat_input;

	private int px = 0;
	private int py = 0;
	private int rx = 0;
	private int ry = 0;
	private int color = 0;
	private int stroke = 0;
	private int flag = 0;

	// related to open save and saveas methods
	private boolean isSave = false;
	private String path = null;

	// chat
	private String user_name = "Misakaaa";
	private String current_time = null;
	public static final int CHATMAX = 100;
	private ArrayList<String> chatHistory = new ArrayList<String>(CHATMAX);

	/**
	 * Launch the application.
	 * 
	 * public static void main(String[] args) { EventQueue.invokeLater(new
	 * Runnable() { public void run() { try { MyDraw window = new MyDraw();
	 * window.frame.setVisible(true); } catch (Exception e) {
	 * e.printStackTrace(); } } }); }
	 */

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
		mp.setBounds(210, 100, 800, 500);
		mp.setLayout(null);

		frame.getContentPane().add(mp);

		// menu
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBounds(0, 0, 1800, 30);

		// file menu name
		JMenu file = new JMenu("File (F)");
		// ALT + F to open that menu
		file.setMnemonic('F');
		// open
		JMenuItem open_item = new JMenuItem("Open");
		open_item.addActionListener(new ActionListener() {
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
		// save
		JMenuItem save_item = new JMenuItem("Save");
		save_item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (isSave) {
					try {
						save();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					try {
						saveAs();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		// save as
		JMenuItem saveAs_item = new JMenuItem("Save As...");
		saveAs_item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					saveAs();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		// quite
		JMenuItem quit_item = new JMenuItem("Exit");

		file.add(open_item);
		// set menu separator
		file.addSeparator();
		file.add(save_item);
		// set menu separator
		file.addSeparator();
		file.add(saveAs_item);
		// set menu separator
		file.addSeparator();
		file.add(quit_item);

		menuBar.add(file);

		// color menu name
		JMenu color_menu = new JMenu("Color (C)");
		// ALT + C to open that menu
		file.setMnemonic('C');
		JMenuItem color_black = new JMenuItem("black");
		color_black.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				color=0;
			}
		});
		JMenuItem color_blue = new JMenuItem("blue");
		color_blue.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				color=1;
			}
		});
		JMenuItem color_cyan = new JMenuItem("cyan");
		color_cyan.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				color=2;
			}
		});
		JMenuItem color_darkgray = new JMenuItem("dark gray");
		color_darkgray.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				color=3;
			}
		});
		JMenuItem color_gray = new JMenuItem("gray");
		color_gray.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				color=4;
			}
		});
		JMenuItem color_lightgray = new JMenuItem("light gray");
		color_lightgray.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				color=5;
			}
		});
		JMenuItem color_green = new JMenuItem("green");
		color_green.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				color=6;
			}
		});
		JMenuItem color_magenta = new JMenuItem("magenta");
		color_magenta.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				color=7;
			}
		});
		JMenuItem color_orange = new JMenuItem("orange");
		color_orange.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				color=8;
			}
		});
		JMenuItem color_pink = new JMenuItem("pink");
		color_pink.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				color=9;
			}
		});
		JMenuItem color_red = new JMenuItem("red");
		color_red.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				color=10;
			}
		});
		JMenuItem color_white = new JMenuItem("white");
		color_white.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				color=11;
			}
		});
		JMenuItem color_yellow = new JMenuItem("yellow");
		color_yellow.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				color=12;
			}
		});
		
		color_menu.add(color_black);
		color_menu.add(color_blue);
		color_menu.add(color_cyan);
		color_menu.add(color_darkgray);
		color_menu.add(color_gray);
		color_menu.add(color_lightgray);
		color_menu.add(color_green);
		color_menu.add(color_magenta);
		color_menu.add(color_orange);
		color_menu.add(color_pink);
		color_menu.add(color_red);
		color_menu.add(color_white);
		color_menu.add(color_blue);
		
		menuBar.add(color_menu);

		// menu name
		JMenu stroke_menu = new JMenu("Stroke (S)");
		// ALT + S to open that menu
		file.setMnemonic('S');
		JMenuItem stroke_1 = new JMenuItem("1");
		stroke_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				stroke=0;
			}
		});
		JMenuItem stroke_2 = new JMenuItem("2");
		stroke_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				stroke=1;
			}
		});
		JMenuItem stroke_3 = new JMenuItem("3");
		stroke_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				stroke=2;
			}
		});
		JMenuItem stroke_4 = new JMenuItem("4");
		stroke_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				stroke=3;
			}
		});
		JMenuItem stroke_5 = new JMenuItem("5");
		stroke_5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				stroke=4;
			}
		});
		stroke_menu.add(stroke_1);
		stroke_menu.add(stroke_2);
		stroke_menu.add(stroke_3);
		stroke_menu.add(stroke_4);
		stroke_menu.add(stroke_5);
		menuBar.add(stroke_menu);

		frame.getContentPane().add(menuBar);
		

		// JList
		listModel = new DefaultListModel();
		list = new JList(listModel);
		scrollPane = new JScrollPane(list, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setBounds(1178, 70, 118, 450);
		frame.getContentPane().add(scrollPane);
		
		JButton kick=new JButton("kick user");
		kick.setBounds(1181, 523, 113, 27);
		kick.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int a=list.getSelectedIndex();
				System.out.println(a);
			}
		});
		frame.getContentPane().add(kick);

		// chat label
		chat_history = new JLabel();
		chat_history.setBounds(1300, 30, 100, 30);
		chat_history.setText("chat window");
		frame.getContentPane().add(chat_history);

		// chat area
		TextArea ta = new TextArea();
		ta.setBounds(1300, 70, 400, 500);

		// input window
		chat_input = new JLabel();
		chat_input.setBounds(1300, 570, 100, 30);
		chat_input.setText("text field");
		frame.getContentPane().add(chat_input);

		// input area
		TextField tf = new TextField();
		tf.setBounds(1300, 600, 400, 100);
		tf.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String text = tf.getText().trim();

				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// set
																					// time
																					// format
				current_time = sdf.format(new Date());// new Date(): get current
														// time

				String message = "\n" + user_name + "  " + current_time + "\n" + text + "\n";
				chatHistory.add(message);

				String op = "";

				for (int i = 0; i < chatHistory.size(); i++) {
					op += chatHistory.get(i);
					ta.setText(op);
					tf.setText("");
				}
			}
		});

		frame.getContentPane().add(ta);
		frame.getContentPane().add(tf);

		// free draw button
		free_btn = new JButton("free draw");
		free_btn.setBounds(50, 100, 100, 30);
		frame.getContentPane().add(free_btn);
		free_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.out.print("clicked free draw button\n");
				MouseListener[] listeners = mp.getMouseListeners();
				for (MouseListener listener : listeners) {
					mp.removeMouseListener(listener);
				}
				MouseMotionListener[] mmls = mp.getMouseMotionListeners();
				for (MouseMotionListener mml : mmls) {
					mp.removeMouseMotionListener(mml);
				}
				mp.addMouseMotionListener(new MouseAdapter() {
					public void mouseDragged(MouseEvent e) {
						px = e.getX();
						py = e.getY();
						mp.freeDraw(px, py, color, stroke);
						mp.repaint();
					}
				});
			}
		});

		// draw line button
		line_btn = new JButton("Draw Line");
		line_btn.setBounds(50, 150, 150, 30);
		frame.getContentPane().add(line_btn);
		line_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.out.print("clicked draw line button\n");
				MouseListener[] listeners = mp.getMouseListeners();
				for (MouseListener listener : listeners) {
					mp.removeMouseListener(listener);
				}
				MouseMotionListener[] mmls = mp.getMouseMotionListeners();
				for (MouseMotionListener mml : mmls) {
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
						mp.drawLine(px, py, rx, ry, color, stroke);
						mp.repaint();
					}
				});
			}
		});

		// draw circle button
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
				MouseMotionListener[] mmls = mp.getMouseMotionListeners();
				for (MouseMotionListener mml : mmls) {
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
						mp.drawCircle(px, py, rx, ry, color, stroke);
						mp.repaint();
					}
				});
			}
		});

		// draw oval button
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
				MouseMotionListener[] mmls = mp.getMouseMotionListeners();
				for (MouseMotionListener mml : mmls) {
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
						mp.drawOval(px, py, rx, ry, color, stroke);
						mp.repaint();
					}
				});
			}
		});

		// draw rectangle button
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
				MouseMotionListener[] mmls = mp.getMouseMotionListeners();
				for (MouseMotionListener mml : mmls) {
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
						mp.drawRect(px, py, rx, ry, color, stroke);
						mp.repaint();
					}
				});
			}
		});

		// eraser button
		erase_btn = new JButton("Eraser");
		erase_btn.setBounds(50, 350, 80, 30);
		frame.getContentPane().add(erase_btn);
		erase_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.out.print("clicked Eraser button\n");
				MouseListener[] listeners = mp.getMouseListeners();
				for (MouseListener listener : listeners) {
					mp.removeMouseListener(listener);
				}
				MouseMotionListener[] mmls = mp.getMouseMotionListeners();
				for (MouseMotionListener mml : mmls) {
					mp.removeMouseMotionListener(mml);
				}
				mp.addMouseMotionListener(new MouseAdapter() {
					public void mouseDragged(MouseEvent e) {
						px = e.getX();
						py = e.getY();
						mp.Eraser(px, py, stroke);
						mp.repaint();
					}
				});
			}
		});

		// clean button
		clean_btn = new JButton("Clean");
		clean_btn.setBounds(50, 400, 80, 30);
		frame.getContentPane().add(clean_btn);
		clean_btn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				mp.Eraser(0, 0, 1000);
				mp.repaint();
			}

		});

	}

	// open save and saveas methods
	public void open() throws IOException {
		File file = null;

		JFileChooser chooseFile = new JFileChooser();
		chooseFile.setDialogTitle("Open ...");
		MyFilter jpgFilter = new MyFilter(".jpg", "JEPG format");
		MyFilter pngFilter = new MyFilter(".png", "PNG format");
		chooseFile.addChoosableFileFilter(jpgFilter);
		chooseFile.addChoosableFileFilter(pngFilter);

		int returnVal = chooseFile.showOpenDialog(null);
		chooseFile.setVisible(true);

		switch (returnVal) {
		case JFileChooser.APPROVE_OPTION: {
			file = chooseFile.getSelectedFile().getAbsoluteFile();
			path = file.getAbsolutePath();
			if (!path.split("\\.")[path.split("\\.").length - 1].equals("jpg")
					&& !path.split("\\.")[path.split("\\.").length - 1].equals("png")) {
				JOptionPane.showMessageDialog(MyDraw.this, "Wrong file format!");
				break;
			}
			isSave = true;
		}
			break;
		case JFileChooser.CANCEL_OPTION:
			break;
		case JFileChooser.ERROR_OPTION:
			break;
		default:
			break;
		}
	}

	public void save() throws IOException {
		File file = new File(path);
		BufferedImage myImage = null;
		OutputStream ops;

		try {
			myImage = new Robot().createScreenCapture(new Rectangle(mp.getLocationOnScreen().x,
					mp.getLocationOnScreen().y, mp.getWidth(), mp.getHeight()));
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
		MyFilter jpgFilter = new MyFilter(".jpg", "JEPG format");
		MyFilter pngFilter = new MyFilter(".png", "PNG format");
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
				myImage = new Robot().createScreenCapture(new Rectangle(mp.getLocationOnScreen().x,
						mp.getLocationOnScreen().y, mp.getWidth(), mp.getHeight()));
				if (file.getAbsolutePath().toUpperCase().endsWith(ends.toUpperCase())) {
					newFile = file;
				} else {
					newFile = new File(file.getAbsolutePath() + ends);
				}
				if (newFile.exists()) {
					int i = JOptionPane.showConfirmDialog(MyDraw.this, "Same file name£¬do you want to overwrite it£¿");
					if (i == JOptionPane.YES_OPTION) {

					} else {
						return;
					}
				}
				ops = new FileOutputStream(newFile);
				ImageIO.write(myImage, ends.substring(1), ops);
				ops.close();
			} catch (FileNotFoundException e1) {
				JOptionPane.showMessageDialog(MyDraw.this, "Saving Error:" + e1.getMessage());
			} catch (IOException e1) {
				e1.printStackTrace();
			} catch (AWTException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case JFileChooser.CANCEL_OPTION:
			break;
		case JFileChooser.ERROR_OPTION:
			break;
		default:
			break;
		}
	}
}
