package lupa;

import java.awt.*;
import javax.swing.*;
import java.awt.geom.*;

public class LupaPanel extends JPanel {
	private Image screenImage;
	private int lupaAncho;
	private int lupaAlto;
	private int locationX;
	private int locationY;
	private int ampliacion;
	private Area border1;
	private Area border2;
	private Area rectangle1;
	private Area rectangle2;
	private Color darkGlassColor;
	private Robot robot;
	private int desplazaX;
	private int desplazaY;

	public LupaPanel(int lupaAncho, int lupaAlto) {
		try {
			robot = new Robot();
		} catch (AWTException e) {
		}
		
		getScreen();
		setlupaSize(lupaAncho, lupaAlto);
	}

	public void setlupaLocation(int locationX, int locationY, int desplazaX, int desplazaY) {
		this.locationX = locationX;
		this.locationY = locationY;
		this.desplazaX = desplazaX;
		this.desplazaY = desplazaY;
		updatelupaPicture();
	}

	public void setlupaAmpliacion(int ampliacion) {
		this.ampliacion = ampliacion;
		updatelupaPicture();
	}
	
	public void setImage(Image screenImage) {
		this.screenImage = screenImage;
		updatelupaPicture();
	}

	public void getScreen() {
		screenImage = robot.createScreenCapture(new Rectangle(0, 0, Toolkit.getDefaultToolkit().getScreenSize().width,
				Toolkit.getDefaultToolkit().getScreenSize().height));
	}

	public void setlupaSize(int lupaAncho, int lupaAlto) {
		this.lupaAncho = lupaAncho;
		this.lupaAlto = lupaAlto;

		border1 = new Area(new Rectangle2D.Double(0, 0, lupaAncho, lupaAlto));
		border2 = new Area(new Rectangle2D.Double(10, 10, lupaAncho-20, lupaAlto-20));
		border1.subtract(border2);
		
		rectangle1 = new Area(new Rectangle2D.Double(0, 0, lupaAncho, lupaAlto));
		rectangle2 = new Area(new Rectangle2D.Double(0, 0, lupaAncho, lupaAlto));
		rectangle2.subtract(rectangle1);
		
		darkGlassColor = new Color(50, 50, 50, 150);

		setPreferredSize(new Dimension(lupaAncho, lupaAlto));
		if (getParent() != null)
			getParent().repaint();
		updatelupaPicture();
	}

	public void updatelupaPicture() {
		if (getParent() != null)
			getParent().repaint();
		else
			repaint();
	}

	public void paintComponent(Graphics g) {
		super.paintComponent((Graphics2D) g);
		drawScreenRectangle((Graphics2D) g);
	}

	private void drawScreenRectangle(Graphics2D g) {
		g.drawImage(screenImage, 
				0, 0, 
				lupaAncho, lupaAlto, 
				locationX + desplazaX, 
				locationY + desplazaY,
				locationX + desplazaX + lupaAncho * (100 - ampliacion) / 100, 
				locationY + desplazaY + lupaAlto * (100 - ampliacion) / 100, 
				this);
		
		g.setColor(darkGlassColor);
		g.fill(border1);
	}
}







