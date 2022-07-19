package shapes;

import java.awt.Color;
import java.util.ArrayList;

public class Shape {
	private int color;
	private ArrayList<Coordinate> cords;
	
	public Shape(int color) {
		this.color = color;
		this.cords = new ArrayList<Coordinate>();
	}
	
	public void setC(int color) {
		this.color = color;
	}
	public void resetCords() {
		this.cords = new ArrayList<Coordinate>();
	}
	
	public boolean contains(Coordinate cord) {
		return this.cords.contains(cord);
	}
	public void addCord(Coordinate cord) {
		if (this.contains(cord)) { return; }
		this.cords.add(cord);
	}
	
	public Coordinate findCenter() {
		int xMin = Integer.MAX_VALUE;
		int xMax = Integer.MIN_VALUE;
		int yMin = Integer.MAX_VALUE;
		int yMax = Integer.MIN_VALUE;
		for (Coordinate curCord : this.cords) {
			if (xMin > curCord.getX()) { xMin = curCord.getX(); }
			if (xMax < curCord.getX()) { xMax = curCord.getX(); }
			if (yMin > curCord.getY()) { yMin = curCord.getY(); }
			if (yMax < curCord.getY()) { yMax = curCord.getY(); }
		}
		return new Coordinate(((xMax - xMin) / 2 + xMin), ((yMax - yMin) / 2 + yMin));
	}
	
	@Override
	public String toString() {
		String ret = "";
		for (Coordinate cord : this.cords) {
			ret += cord.toString() + " ";
		}
		return ret;
	}
}
