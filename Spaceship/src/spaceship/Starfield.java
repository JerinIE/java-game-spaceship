package spaceship;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

public class Starfield {
	
	private int width;
	private int height;
	private int density;
	private ArrayList<Star> stars;
	
	public Starfield(int width, int height, int density) {
		this.width = width;
		this.height = height;
		this.density = density;
		this.stars = new ArrayList<Star>();
		
		for(int i = 0; i < density; i++) {
			this.stars.add(Star.createRandomWithin(width, height));
		}
	}
	
	public void doLogic() {
		handleStarCreation();
		handleStarMovement();
		handleStarRemoval();
	}
	
	public void handleStarCreation() {
		for(int i = stars.size(); i < density; i++) {
			stars.add(Star.createRandomWithin(width, 5));
		}
	}
	
	public void handleStarMovement() {
		for(Star star : stars) {
			star.move();
		}
	}
	
	public void handleStarRemoval() {
		for(Star star : stars) {
			if(star.isOutside(width, height)) {
				stars.remove(star);
				handleStarRemoval();
				return;
			}
		}
	}
	
	public void draw(Graphics g) {
		
		for(Star star : stars) {
			g.setColor(Color.WHITE);
			g.fillOval(star.getX(), star.getY(), star.getSize(), star.getSize());
			//g.drawLine(star.getX(), star.getY(), star.getX(), star.getY());
		}
		
	}
}
