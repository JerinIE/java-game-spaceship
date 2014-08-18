package spaceship;


public class Star {
	private int x;
	private int y;
	private int size;
	private int speed;
	
	public Star() {
		this(0, 0, 1, 1);
	}
	
	public Star(int x, int y, int size, int speed) {
		this.x = x;
		this.y = y;
		this.size = size;
		this.speed = speed;
	}
	
	public int getX() {
		return this.x;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public int getY() {
		return this.y;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public int getSize() {
		return this.size;
	}
	
	public void setSize(int radius) {
		this.size = radius;
	}
	
	public int getSpeed() {
		return this.speed;
	}
	
	public void setSpeed(int speed) {
		this.speed = speed;
	}
	
	public void move() {
		this.y += speed;
	}
	
	public boolean isOutside(int width, int height) {
		if(this.y > height) {
			return true;
		}
		
		return false;
	}
	
	public static Star createRandomWithin(int width, int height) {
		int ranRadius = GameObject.randomWithRange(1, 2);
		int ranX = GameObject.randomWithRange(0, width-1-(ranRadius*2));
		int ranY = GameObject.randomWithRange(0, height-1-(ranRadius*2));
		int ranSpeed = ranRadius;
		
		return new Star(ranX, ranY, ranRadius, ranSpeed);
	}
}
