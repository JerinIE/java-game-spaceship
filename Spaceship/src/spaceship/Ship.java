package spaceship;

import java.awt.Image;

public class Ship extends GameObject {
	public Ship(Image image, ObjectBounds bounds, int panelWidth, int panelHeight) {
		setPanelSize(panelWidth, panelHeight);
		setType(GameObject.Type.SHIP);
		
		setImage(image);
		setBounds(bounds);
		
		setSizeByImage();
		setSpeed(5);
		
		// Position the spaceship at the bottom center of the screen
		setX((int)((panelWidth / 2) - (getWidth() / 2)));
		setY(panelHeight - getHeight());
		
		getBounds().setElevation(getX(), getY());
		
		limitToScreenBounds(true);
	}
}
