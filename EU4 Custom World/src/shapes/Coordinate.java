package shapes;

import java.awt.Color;

public class Coordinate {
	private int x,y;
	public Coordinate(int x, int y) {
		this.x = x; 
		this.y = y;
	}
	
	public int getX() { return this.x; }
	public int getY() { return this.y; }
	
	public void setX(int x) { this.x = x; }
	public void setY(int y) { this.y = y; }
	
	@Override
	public boolean equals(Object O) {
		Coordinate o = (Coordinate) O;
		if (o.getX() == this.getX() && o.getY() == this.getY()) {
			return true;
		}
		return false;
	}
	@Override
	public String toString() {
		return "(" + this.x + "|" + this.y + ")";
	}
}
