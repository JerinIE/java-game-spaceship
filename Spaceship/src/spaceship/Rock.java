package spaceship;

import java.awt.Image;

public class Rock extends GameObject {
	public Rock(int type, Image image, ObjectBounds bounds, int panelWidth, int panelHeight) {
		setPanelSize(panelWidth, panelHeight);
		setType(GameObject.Type.values()[type]);
		setImage(image);
		setBounds(bounds);
		setSizeByImage();
		setSpeed(3);
		setX(GameObject.randomWithRange(0, panelWidth - getWidth()));
		setY(-getHeight());
		limitToScreenBounds(false);
	}
}
