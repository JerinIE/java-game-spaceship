package spaceship;

public class Ship {
	
	private int locationX = 500;
	private int locationY = 300;
	private int speed = 5;
	private int size = 60;
	private int screenWidth;
	private int screenHeight;
	
	public Ship(int screenWidth, int screenHeight) {
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
	}
	
	public int getLocationX() {
		return this.locationX;
	}
	
	public int getLocationY() {
		return this.locationY;
	}
	
	public int getSize() {
		return size;
	}
	
	public void moveUp() {
		if(locationY - speed > 0)
			locationY -= speed;
	}
	
	public void moveDown() {
		if(locationY + speed < screenHeight - 2*size)
			locationY += speed;
	}
	
	public void moveLeft() {
		if(locationX - speed > 0)
			locationX -= speed;
	}
	
	public void moveRight() {
		if(locationX + speed < screenWidth - size)
			locationX += speed;
	}
	
}
