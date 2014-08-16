package spaceship;

public class Bullet extends GameObject {
	public Bullet(int locationX, int locationY, int panelWidth, int panelHeight) {
		setPanelSize(panelWidth, panelHeight);
		setType(GameObject.Type.BULLET);
		setX(locationX);
		setY(locationY);
		setWidth(2);
		setHeight(10);
		setSpeed(8);
		setBoundsBySize();
		limitToScreenBounds(false);
	}
}
