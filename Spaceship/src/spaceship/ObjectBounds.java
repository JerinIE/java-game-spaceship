package spaceship;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

public class ObjectBounds implements Cloneable {
	
	private ArrayList<Point> points;
	
	private int xElevation = 0;
	private int yElevation = 0;
	
	public ObjectBounds(ArrayList<Point> points) {
		this.points = points;
	}
	
	public void setXElevation(int xElevation) {
		this.xElevation = xElevation;
	}
	
	public int getXElevation() {
		return this.xElevation;
	}
	
	public void setYElevation(int yElevation) {
		this.yElevation = yElevation;
	}
	
	public int getYElevation() {
		return this.yElevation;
	}
	
	public void setElevation(int xElevation, int yElevation) {
		this.xElevation = xElevation;
		this.yElevation = yElevation;
	}
	
	public ArrayList<Point> getPoints() {
		return this.points;
	}
	
	public boolean isInside(ObjectBounds objectBounds2) {
		for(Point point : points) {
			for(Point point2 : objectBounds2.getPoints()) {
				if(point.getX() + xElevation == point2.getX() + objectBounds2.xElevation) {
					if(point.getY() + yElevation == point2.getY() + objectBounds2.yElevation) {
						return true;
					}
				}
			}
		}
		
		return false;
	}
	
	public static ObjectBounds parseFromFile(URL fileUrl) {
		try {
			ArrayList<Point> points = new ArrayList<Point>();
			
			BufferedReader bReader = new BufferedReader(new InputStreamReader(fileUrl.openStream()));
			String fileContent = bReader.readLine();
			
			String[] rawPoints = fileContent.split("\\|");
			
			for(String rawPoint : rawPoints) {
				String[] splittedPoint = rawPoint.split(",");
				
				int x = Integer.parseInt(splittedPoint[0]);
				int y = Integer.parseInt(splittedPoint[1]);
				
				points.add(new Point(x, y));
			}
			
			bReader.close();
			
			return new ObjectBounds(points);
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		
		return null;
	}
	
	public Object clone() {
		try {
			return super.clone();
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		
		return null;
	}
}
