package main;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import javax.imageio.ImageIO;
import shapes.*;

public class fill {
	public static void floodFill(BufferedImage img, int x, int y, Color findColor, Color replaceColor) {
		Queue<Coordinate> q = new LinkedList<Coordinate>();
		ArrayList<Coordinate> visited = new ArrayList<Coordinate>();
		q.add(new Coordinate(x,y));
		
		while (q.size() != 0) {
			Coordinate curCord = q.remove();
			if (visited.contains(curCord)) { continue; }
			visited.add(curCord);
			
			if (curCord.getX()<0 || curCord.getX()>img.getWidth()-1) { continue; }
			if (curCord.getY()<0 || curCord.getY()>img.getWidth()-1) { continue; }
			
			if (img.getRGB(curCord.getX(), curCord.getY()) == findColor.getRGB()) {
				img.setRGB(curCord.getX(), curCord.getY(), replaceColor.getRGB());
				q.add(new Coordinate(curCord.getX()-1, curCord.getY()));
				q.add(new Coordinate(curCord.getX()+1, curCord.getY()));
				q.add(new Coordinate(curCord.getX(), curCord.getY()-1));
				q.add(new Coordinate(curCord.getX(), curCord.getY()+1));
			}
		}
	}
	
	public static void edgeFill(BufferedImage img, int x, int y, Color findColor, Color replaceColor) {
		Queue<Coordinate> q = new LinkedList<Coordinate>();
		ArrayList<Coordinate> visited = new ArrayList<Coordinate>();
		q.add(new Coordinate(x,y));
		
		while (q.size() != 0) {
			Coordinate curCord = q.remove();
			if (visited.contains(curCord)) { continue; }
			visited.add(curCord);
			
			if (curCord.getX()<0 || curCord.getX()>img.getWidth()-1) { continue; }
			if (curCord.getY()<0 || curCord.getY()>img.getWidth()-1) { continue; }
			
			if (img.getRGB(curCord.getX(), curCord.getY()) == findColor.getRGB()) {
				q.add(new Coordinate(curCord.getX()-1, curCord.getY()));
				q.add(new Coordinate(curCord.getX()+1, curCord.getY()));
				q.add(new Coordinate(curCord.getX(), curCord.getY()-1));
				q.add(new Coordinate(curCord.getX(), curCord.getY()+1));
			} else {
				img.setRGB(curCord.getX(), curCord.getY(), replaceColor.getRGB());
			}
		}
	}
}
