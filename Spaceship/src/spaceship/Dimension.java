package spaceship;

public class Dimension {
	
	private Point a; // top left corner
	private Point b; // top right corner
	private Point c; // bottom left corner
	private Point d; // bottom right corner
	
	public Dimension(Point p, int width, int height) {
		this(p.getX(), p.getY(), width, height);
	}
	
	public Dimension(int locationX, int locationY, int width, int height) {
		this.a = new Point(locationX, locationY);
		this.b = new Point(locationX + width, locationY);
		this.c = new Point(locationX, locationY + height);
		this.d = new Point(locationX + width, locationY + height);
	}
	
	public boolean anyPointIsInside(Dimension db) {
		
		if(this.a.isBigger(db.a) && this.a.isSmaller(db.d)) {
			return true;
		}
		
		if(this.b.isBigger(db.a) && this.b.isSmaller(db.d)) {
			return true;
		}
		
		if(this.c.isBigger(db.a) && this.c.isSmaller(db.d)) {
			return true;
		}
		
		if(this.d.isBigger(db.a) && this.d.isSmaller(db.d)) {
			return true;
		}
		
		return false;
	}
	
	public boolean allPointsAreInside(Dimension db) {
		
		if(this.a.isBigger(db.a) && this.a.isSmaller(db.d)) {
			if(this.b.isBigger(db.a) && this.b.isSmaller(db.d)) {
				if(this.c.isBigger(db.a) && this.c.isSmaller(db.c)) {
					if(this.d.isBigger(db.a) && this.c.isSmaller(db.c)) {
						return true;
					}
				}
			}
		}
		
		return false;
	}
	
}
