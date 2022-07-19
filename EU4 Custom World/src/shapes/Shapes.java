package shapes;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Shapes {
	private Map<Integer, Shape> shapeList;
	public Shapes() {
		this.shapeList = new HashMap<Integer, Shape>();
	}
	
	public void put(int color, Coordinate cord) {
		if (!this.shapeList.keySet().contains(color)) {
			this.shapeList.put(color, new Shape(color));
		}
		this.shapeList.get(color).addCord(cord);
	}
	public void put(int color, int x, int y) {
		this.put(color, new Coordinate(x,y));
	}
	
	public int size() {
		return this.shapeList.size();
	}
	public Shape get(int color) {
		return this.shapeList.get(color);
	}
	
	public Set<Integer> keySet() {
		return this.shapeList.keySet();
	}
}
