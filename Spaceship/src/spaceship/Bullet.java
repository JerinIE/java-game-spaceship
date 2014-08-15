package spaceship;

public class Bullet {
	
	private int locationX;
	private int locationY;
	
	private int speed = 8;
	private int length = 10;
	//private int damage = 50;
	
	public Bullet(int locationX, int locationY) {
		this.locationX = locationX;
		this.locationY = locationY;
	}
	
	public void move() {
		locationY -= speed;
	}
	
	public int getLocationX() {
		return this.locationX;
	}
	
	public int getLocationY() {
		return this.locationY;
	}
	
	public int getLength() {
		return this.length;
	}
	
}
