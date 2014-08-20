package spaceship;

import java.awt.Image;

public class Bullet extends GameObject {
	public Bullet(int locationX, int locationY, int panelWidth, int panelHeight, Image image, ObjectBounds bounds) {
		setPanelSize(panelWidth, panelHeight);
		setType(GameObject.Type.BULLET);
		setX(locationX);
		setY(locationY);
		setImage(image);
		setBounds(bounds);
		setSizeByImage();
		setSpeed(8);
		getBounds().setElevation(getX(), getY());
		limitToScreenBounds(false);
	}
}
