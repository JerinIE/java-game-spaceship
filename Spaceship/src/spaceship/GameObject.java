package spaceship;

import java.awt.Image;
import java.util.ArrayList;

public class GameObject {
	
	private Type type;
	private int x;
	private int y;
	private int width;
	private int height;
	private int speed;
	private Image image;
	private ObjectBounds bounds;
	private boolean limitToScreenBounds;
	private int panelWidth;
	private int panelHeight;
	
	public Type getType() {
		return this.type;
	}
	
	public void setType(Type type) {
		this.type = type;
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
	
	public int getWidth() {
		return this.width;
	}
	
	public void setWidth(int width) {
		this.width = width;
	}
	
	public int getHeight() {
		return this.height;
	}
	
	public void setHeight(int height) {
		this.height = height;
	}
	
	public int getSpeed() {
		return this.speed;
	}
	
	public void setSpeed(int speed) {
		this.speed = speed;
	}
	
	public Image getImage() {
		return this.image;
	}
	
	public void setImage(Image image) {
		this.image = image;
	}
	
	public ObjectBounds getBounds() {
		return this.bounds;
	}
	
	public void setBounds(ObjectBounds bounds) {
		this.bounds = bounds;
	}
	
	public boolean isLimitedToScreenBounds() {
		return this.limitToScreenBounds;
	}
	
	public void limitToScreenBounds(boolean limit) {
		this.limitToScreenBounds = limit;
	}
	
	public int getPanelWidth() {
		return this.panelWidth;
	}
	
	public void setPanelWidth(int panelWidth) {
		this.panelWidth = panelWidth;
	}
	
	public int getPanelHeight() {
		return this.panelHeight;
	}
	
	public void setPanelHeight(int panelHeight) {
		this.panelHeight = panelHeight;
	}
	
	public void setPanelSize(int panelWidth, int panelHeight) {
		this.panelWidth = panelWidth;
		this.panelHeight = panelHeight;
	}
	
	public void setBoundsBySize() {
		ArrayList<Point> points = new ArrayList<Point>();
		
		for(int y = 0; y < this.height; y++) {
			for(int x = 0; x < this.width; x++) {
				points.add(new Point(x, y));
			}
		}
		
		this.bounds = new ObjectBounds(points);
	}
	
	public void advanceX(Direction direction) {
		if(limitToScreenBounds) {
			if(direction == Direction.LEFT) {
				if((this.x - speed) < 0) {
					// Prevent movement, object is at min screen bounds
					return;
				}
			} else if(direction == Direction.RIGHT) {
				if((this.x + speed) > (this.panelWidth - this.width)) {
					// Prevent movement, object is at max screen bounds
					return;
				}
			}
		}
		
		if(direction == Direction.LEFT) {
			this.x -= speed;
		} else if(direction == Direction.RIGHT) {
			this.x += speed;
		} else {
			System.out.println("Unsupported direction for x axis!");
		}
		
		this.bounds.setElevation(this.x, this.y);
	}
	
	public void advanceY(Direction direction) {
		if(limitToScreenBounds) {
			if(direction == Direction.UP) {
				if((this.y - speed) < 0) {
					// Prevent movement, object is at min screen bounds
					return;
				}
			} else if(direction == Direction.DOWN) {
				if((this.y + speed) > (this.panelHeight - this.height)) {
					// Prevent movement, object is at max screen bounds
					return;
				}
			}
		}
		
		if(direction == Direction.UP) {
			this.y -= speed;
		} else if(direction == Direction.DOWN) {
			this.y += speed;
		} else {
			System.out.println("Unsupported direction for y axis!");
		}
		
		this.bounds.setElevation(this.x, this.y);
	}
	
	public void setSizeByImage() {
		this.width = image.getWidth(null);
		this.height = image.getHeight(null);
	}
	
	public boolean isOutsideScreen(Direction direction) {
		if(direction == Direction.DOWN) {
			if(this.y > this.panelHeight) {
				return true;
			}
		} else if(direction == Direction.UP) {
			if((this.y + this.height) < 0) {
				return true;
			}
		}
		
		return false;
	}
	
	public enum Type {
		SHIP, BULLET, ROCK32, ROCK48, ROCK64
	}
	
	public enum Direction {
		LEFT, RIGHT, UP, DOWN
	}
	
	public static int randomWithRange(int min, int max)
	{
	   int range = (max - min) + 1;     
	   return (int)(Math.random() * range) + min;
	}
	
}
