package spaceship;

public class Explosion {
	
	private int locationX;
	private int locationY;
	private int animationStep = 0;
	private int animationStopAt = 17;
	private int size;
	private boolean done = false;
	
	public Explosion(int locationX, int locationY, int size) {
		this.locationX = locationX;
		this.locationY = locationY;
		this.size = size;
	}
	
	public int getX() {
		return this.locationX;
	}
	
	public int getY() {
		return this.locationY;
	}
	
	public int getSize() {
		return this.size;
	}
	
	public boolean isDone() {
		return this.done;
	}
	
	public void stepInto() {
		if(animationStep == animationStopAt) {
			done = true;
			return;
		}
		
		animationStep++;
	}
	
	public int getAnimationStep() {
		return this.animationStep;
	}
	
}
