package lupa;

import java.awt.*;
import java.awt.event.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.*;
import java.io.*;

public class LupaWindow extends JFrame {
	private JFrame frame = this;
	private Container container = getContentPane();
	private JPopupMenu popupMenu = new JPopupMenu();
	private JMenuItem menuRefresh = new JMenuItem("Refrescar");
	private JMenuItem menuHide = new JMenuItem("Ocultar");
	private JMenuItem menuConfig = new JMenuItem("Configuraci\u00f3n");
	private JMenuItem menuAbout = new JMenuItem("Acerca de");
	private JMenuItem menuExit = new JMenuItem("Salir");
	private TimeUpdate timeUpdate = new TimeUpdate();
	private int setCoordinateX;
	private int setCoordinateY;
	private int absoluteCoordinateX;
	private int absoluteCoordinateY;
	private int relativeCoordinateXWhenMousePressed;
	private int relativeCoordinateYWhenMousePressed;
	private boolean mousePressedNow;
	private boolean lupaStopped = true;
	private ConfigData configData = new ConfigData();
	private int screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
	private int screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;
	private int lupaAncho = 600;
	private int lupaAlto = 400;
	private int lupaAmpliacion = 50;
	private int lupaRefresco = 0;
	private int updateScreenDelay = 50;
	private LupaPanel lupaPanel = new LupaPanel(lupaAncho, lupaAlto);

	public LupaWindow(String windowTitle) {
		super(windowTitle);
		Image img;
		try {
			img = ImageIO.read(getClass().getResource("/resources/Lupa.png"));
			setIconImage(img);
		} catch (IOException e) {
		}
		
		setUndecorated(true);
		setAlwaysOnTop(true);
		setBackground(new Color(0, 0, 0, 0));
		
		container.add(lupaPanel);
		addMouseListener(new MouseFunctions());
		addMouseMotionListener(new MouseMotionFunctions());
		timeUpdate.start();
		menuRefresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateScreen();
			}
		});
		menuHide.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setState(JFrame.ICONIFIED);
			}
		});
		menuConfig.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				lupaStopped = true;
				LupaConfig config = new LupaConfig(frame, screenWidth, screenHeight, lupaAncho, lupaAlto, lupaAmpliacion, lupaRefresco);
				lupaAncho = config.ancho;
				lupaAlto = config.alto;
				lupaRefresco = config.refresco;
				lupaAmpliacion = config.amplia;
				updateSize(lupaAncho, lupaAlto);
				updateAmpliacion(lupaAmpliacion);
				updateRefresco();	
			}
		});
	    menuAbout.addActionListener(
	    	      new ActionListener(){
	    	        public void actionPerformed(ActionEvent e){
	    	          JOptionPane.showMessageDialog(null,"Lupa v0.1\nJuan Hevilla\n23/10/2015 (Co\u00edn, M\u00e1laga) ","Acerca de", JOptionPane.INFORMATION_MESSAGE);
	    	        }
	    	      }
	    	    );
		menuExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});	
		
		popupMenu.addPopupMenuListener(new PopupMenuListener() {
			@Override
			public void popupMenuWillBecomeVisible(PopupMenuEvent arg0) {

			}
			@Override
			public void popupMenuWillBecomeInvisible(PopupMenuEvent arg0) {				
				updateRefresco();
			}
			@Override
			public void popupMenuCanceled(PopupMenuEvent arg0) {
				updateRefresco();
			}
		});
		
		popupMenu.add(menuRefresh);
		popupMenu.add(menuHide);
		popupMenu.add(menuConfig);
		popupMenu.add(menuAbout);
		popupMenu.add(menuExit);

		updateSize(lupaAncho, lupaAlto);
		updateAmpliacion(lupaAmpliacion);
		updateRefresco();
		
		openFile();
		setVisible(true);
	}
	
	public void updateRefresco() {
		if (lupaRefresco == 0) {
			lupaStopped = true;
		} else {
			lupaStopped = false;
		}
	}
	
	public void updateSize(int lupaAncho, int lupaAlto) {
		lupaPanel.setlupaSize(lupaAncho, lupaAlto);
		setSize(lupaAncho, lupaAlto);
		validate();
	}

	public void updateAmpliacion(int ampliacion) {
		lupaPanel.setlupaAmpliacion(ampliacion);
	}
	
	public void updateScreen() {
		setVisible(false);
		try {
			Thread.sleep(updateScreenDelay);
		} catch (InterruptedException e) {
		}
		lupaPanel.getScreen();
		setVisible(true);
	}

	private class TimeUpdate extends Thread {
		public void run() {
			while (true) {
				if (lupaStopped == false) {
					setVisible(false);
					try {
						Thread.sleep(updateScreenDelay);
					} catch (InterruptedException e) {
					}
					lupaPanel.getScreen();
					setVisible(true);
				}				
				try {
					Thread.sleep(lupaRefresco);
				} catch (InterruptedException e) {
				}
			}
		}
	}

	private class MouseFunctions extends MouseAdapter {
		public void mousePressed(MouseEvent e) {
			if (e.getClickCount() == 1) {
				mousePressedNow = true;
				relativeCoordinateXWhenMousePressed = e.getX();
				relativeCoordinateYWhenMousePressed = e.getY();
			}
			if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2) {

			}
			if (e.getButton() == MouseEvent.BUTTON3) {
				botonDerecho(e);
			}
		}

		public void mouseReleased(MouseEvent e) {
			mousePressedNow = false;
			saveFile();
			if (e.isPopupTrigger()) {
				botonDerecho(e);
			}
		}
		private void botonDerecho(MouseEvent e) {
			lupaStopped = true;
			popupMenu.show(e.getComponent(), e.getX(), e.getY());
		}
	}

	private class MouseMotionFunctions extends MouseMotionAdapter {
		public void mouseDragged(MouseEvent e) {
			if (mousePressedNow == true) {
				lupaStopped = true;
				absoluteCoordinateX = LupaWindow.this.getLocationOnScreen().x + e.getX();
				absoluteCoordinateY = LupaWindow.this.getLocationOnScreen().y + e.getY();
				setCoordinateX = absoluteCoordinateX - relativeCoordinateXWhenMousePressed;
				setCoordinateY = absoluteCoordinateY - relativeCoordinateYWhenMousePressed;
				
				int desplazaX = 0;
				if (setCoordinateX > screenWidth - lupaAncho) {
					desplazaX = setCoordinateX - (screenWidth - lupaAncho); 
					setCoordinateX = screenWidth - lupaAncho; 
				}

				int desplazaY = 0;
				if (setCoordinateY > screenHeight - lupaAlto) {
					desplazaY = setCoordinateY - (screenHeight - lupaAlto); 
					setCoordinateY = screenHeight - lupaAlto; 
				}
				
				lupaPanel.setlupaLocation(setCoordinateX, setCoordinateY, desplazaX, desplazaY);
				setLocation(setCoordinateX, setCoordinateY);
				updateRefresco();
			}
		}
	}

	public void saveFile() {
		ObjectOutputStream out;
		configData.lupaXCoordinate = getLocation().x;
		configData.lupaYCoordinate = getLocation().y;
		configData.lupaAncho = lupaAncho;
		configData.lupaAlto = lupaAlto;
		configData.lupaAmpliacion = lupaAmpliacion;
		configData.lupaRefresco = lupaRefresco;
		try {
			out = new ObjectOutputStream(new FileOutputStream("lupa.cfg"));
			out.writeObject(configData);
			out.flush();
			out.close();
		} catch (IOException e) {
		}
	}

	public void openFile() {
		ObjectInputStream in;
		try {
			in = new ObjectInputStream(new FileInputStream("lupa.cfg"));
			try {
				configData = (ConfigData) in.readObject();
				setCoordinateX = configData.lupaXCoordinate;
				setCoordinateY = configData.lupaYCoordinate;
				lupaAncho = configData.lupaAncho;
				lupaAlto = configData.lupaAlto;
				lupaAmpliacion = configData.lupaAmpliacion;
				lupaRefresco = configData.lupaRefresco;
				
				updateRefresco();				
				updateSize(lupaAncho, lupaAlto);
				updateAmpliacion(lupaAmpliacion);
				
			} catch (ClassNotFoundException e) {
			} catch (IOException e) {
			}
			in.close();
		} catch (IOException e) {
		}
	}
}

class ConfigData implements Serializable {
	public int lupaXCoordinate;
	public int lupaYCoordinate;
	public int lupaAncho;
	public int lupaAlto;
	public int lupaAmpliacion;
	public int lupaRefresco;
}
