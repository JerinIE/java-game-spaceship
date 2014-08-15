package spaceship;

public class Rock {
	
	private int locationX;
	private int locationY;
	//private int health = 50;
	private int speed = 2;
	private int size = 20;
	
	public Rock(int screenWidth, int screenHeight) {
		this.size = random(30, 100);
		locationY = -(size);
		locationX = random(0, screenWidth-size);
	}
	
	public int getLocationX() {
		return this.locationX;
	}
	
	public int getLocationY() {
		return this.locationY;
	}
	
	public int getSize() {
		return this.size;
	}
	
	public void move() {
		this.locationY += speed;
	}
	
	private int random(int min, int max)
	{
	   int range = (max - min) + 1;     
	   return (int)(Math.random() * range) + min;
	}
	
}
