package main;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import javax.imageio.ImageIO;
import shapes.*;


public class mapGeneration {
	final static String MOD_LOCATION = "C:\\Users\\johns\\OneDrive\\Desktop\\Other\\EU4 Mod";
	final static String EU4_PATH = "C:\\Program Files (x86)\\Steam\\steamapps\\common\\Europa Universalis IV";
	
	public static void main(String [] args) throws IOException {
		//mapFolderCreation();
		//historyFolderCreation();
		//localisationFolderCreation();
		System.out.println("Done");
	}	
	
	/* Map Folder Functions*/
	public static void mapFolderCreation() throws IOException {
		System.out.println("Start..Map");
		new File(MOD_LOCATION + "\\map").mkdir();
		imageCreation();
		mapFileCreation();
		areaTypeCreation();
		
		System.out.println("Done...Map\n");
		
	}
	// Image Creation Functions //
	public static void imageCreation() throws IOException {
		File file = new File(MOD_LOCATION + "\\dominons.bmp");
		BufferedImage img = ImageIO.read(file);
		file = new File(EU4_PATH + "\\map\\terrain.bmp");
		BufferedImage EU4_TERRAIN = ImageIO.read(file);
		file = new File(EU4_PATH + "\\map\\rivers.bmp");
		BufferedImage EU4_RIVERS = ImageIO.read(file);
		file = new File(EU4_PATH + "\\map\\heightmap.bmp");
		BufferedImage EU4_HEIGHTMAP = ImageIO.read(file);
		System.out.println("Done...Setup");
		
		terrainBMPCreation(img, EU4_TERRAIN);
		riversBMPCreation(img, EU4_RIVERS);
		heightBMPCreation(img, EU4_HEIGHTMAP);
		colormapBMPSimpleCreation(img);
		
		System.out.println("Done...Images");
	}
	public static void terrainBMPCreation(BufferedImage img, BufferedImage EU4_TERRAIN) throws IOException {
		// Basic Ocean Land Lake
		for (int y=0; y<EU4_TERRAIN.getHeight(); y++) {
			for (int x=0; x<EU4_TERRAIN.getWidth(); x++) {
				Color imgColor = new Color(img.getRGB(x, y), false);
				// Ocean
				if (imgColor.getRed() == 5 && imgColor.getGreen() == 20 && imgColor.getBlue() == 18) {
					EU4_TERRAIN.setRGB(x, y, new Color(8,31,130).getRGB());
				// Land
				} else if (imgColor.getRed() == 150 && imgColor.getGreen() == 68 && imgColor.getBlue() == 192) {
					EU4_TERRAIN.setRGB(x, y, new Color(86,124,27).getRGB());
				// Lake
				} else if (imgColor.getRed() == 0 && imgColor.getGreen() == 255 && imgColor.getBlue() == 0) {
					EU4_TERRAIN.setRGB(x, y, new Color(8,31,130).getRGB());
				}
			}
		}
		// Coastline Beach
		int Y = new Color(255,247,0).getRGB();
		for (int y=1; y<EU4_TERRAIN.getHeight()-1; y++) {
			for (int x=1; x<EU4_TERRAIN.getWidth()-1; x++) {
				int N = img.getRGB(x, y-1);
				int E = img.getRGB(x+1, y);
				int S = img.getRGB(x, y+1);
				int W = img.getRGB(x-1, y);
				
				if (N != S && (N != Y && S != Y)) {
					EU4_TERRAIN.setRGB(x, y, new Color(255,247,0).getRGB());
				}
				if (E != W && (E != Y && W != Y)) {
					EU4_TERRAIN.setRGB(x, y, new Color(255,247,0).getRGB());
				}
			}
		}
		
		File file = new File(MOD_LOCATION + "\\map\\terrain.bmp");
	    ImageIO.write(EU4_TERRAIN, "bmp", file);
	    System.out.println("Done...Terrain");
	}
	public static void riversBMPCreation(BufferedImage img, BufferedImage EU4_RIVERS) throws IOException {
		for (int y=0; y<EU4_RIVERS.getHeight(); y++) {
			for (int x=0; x<EU4_RIVERS.getWidth(); x++) {
				Color imgColor = new Color(img.getRGB(x, y), false);
				// Ocean
				if (imgColor.getRed() == 5 && imgColor.getGreen() == 20 && imgColor.getBlue() == 18) {
					EU4_RIVERS.setRGB(x, y, new Color(122,122,122).getRGB());
				// Land
				} else if (imgColor.getRed() == 150 && imgColor.getGreen() == 68 && imgColor.getBlue() == 192) {
					EU4_RIVERS.setRGB(x, y, new Color(255,255,255).getRGB());
				// Lake
				} else if (imgColor.getRed() == 0 && imgColor.getGreen() == 255 && imgColor.getBlue() == 0) {
					EU4_RIVERS.setRGB(x, y, new Color(122,122,122).getRGB());
				}
			}
		}
		for (int y=1; y<EU4_RIVERS.getHeight(); y++) {
			for (int x=5; x<EU4_RIVERS.getWidth()-5; x++) {
				Color imgColor = new Color(EU4_RIVERS.getRGB(x, y), false);
				if (imgColor.getRed() == 255 && imgColor.getGreen() == 255 && imgColor.getBlue() == 255) {
					EU4_RIVERS.setRGB(x-1, y, new Color(0,0,200).getRGB());
					EU4_RIVERS.setRGB(x, y, new Color(0,0,200).getRGB());
					EU4_RIVERS.setRGB(x+1, y, new Color(0,0,200).getRGB());
					EU4_RIVERS.setRGB(x+2, y, new Color(0,255,0).getRGB());
					File file = new File(MOD_LOCATION + "\\map\\rivers.bmp");
				    ImageIO.write(EU4_RIVERS, "bmp", file);
				    System.out.println("Done...Rivers");
					return;
				}
			}
		}
	}
	public static void heightBMPCreation(BufferedImage img, BufferedImage EU4_HEIGHTMAP) throws IOException {
		for (int y=0; y<EU4_HEIGHTMAP.getHeight(); y++) {
			for (int x=0; x<EU4_HEIGHTMAP.getWidth(); x++) {
				Color imgColor = new Color(img.getRGB(x, y), false);
				// Ocean
				if (imgColor.getRed() == 5 && imgColor.getGreen() == 20 && imgColor.getBlue() == 18) {
					EU4_HEIGHTMAP.setRGB(x, y, new Color(90,90,90).getRGB());
				// Land
				} else if (imgColor.getRed() == 150 && imgColor.getGreen() == 68 && imgColor.getBlue() == 192) {
					EU4_HEIGHTMAP.setRGB(x, y, new Color(110,110,110).getRGB());
				// Lake
				} else if (imgColor.getRed() == 0 && imgColor.getGreen() == 255 && imgColor.getBlue() == 0) {
					EU4_HEIGHTMAP.setRGB(x, y, new Color(90,90,90).getRGB());
				}
			}
		}
		File file = new File(MOD_LOCATION + "\\map\\heightmap.bmp");
	    ImageIO.write(EU4_HEIGHTMAP, "bmp", file);
	    System.out.println("Done...Heightmap");
	}
	public static void colormapBMPSimpleCreation(BufferedImage img) throws IOException {
		new File(MOD_LOCATION + "\\map\\terrain").mkdir();
		for (int y=0; y<img.getHeight(); y++) {
			for (int x=0; x<img.getWidth(); x++) {
				Color imgColor = new Color(img.getRGB(x, y), false);
				// Ocean
				if (imgColor.getRed() == 5 && imgColor.getGreen() == 20 && imgColor.getBlue() == 18) {
					img.setRGB(x, y, new Color(68,87,115).getRGB());
				// Land
				} else if (imgColor.getRed() == 150 && imgColor.getGreen() == 68 && imgColor.getBlue() == 192) {
					img.setRGB(x, y, new Color(41,52,16).getRGB());
				// Lake
				} else if (imgColor.getRed() == 0 && imgColor.getGreen() == 255 && imgColor.getBlue() == 0) {
					img.setRGB(x, y, new Color(68,87,115).getRGB());
				}
			}
		}
		File file = new File(MOD_LOCATION + "\\map\\terrain\\colormapSimple.bmp");
	    ImageIO.write(img, "bmp", file);
	    System.out.println("Done...Colormap");
	    
	    for (int y=0; y<img.getHeight(); y++) {
			for (int x=0; x<img.getWidth(); x++) {
				Color imgColor = new Color(img.getRGB(x, y), false);
				// Ocean
				if (imgColor.getRed() == 68 && imgColor.getGreen() == 87 && imgColor.getBlue() == 115) {
					img.setRGB(x, y, new Color(13,45,82).getRGB());
				// Land
				} else if (imgColor.getRed() == 41 && imgColor.getGreen() == 52 && imgColor.getBlue() == 16) {
					img.setRGB(x, y, new Color(10,74,206).getRGB());
				// Lake
				} else if (imgColor.getRed() == 68 && imgColor.getGreen() == 87 && imgColor.getBlue() == 115) {
					img.setRGB(x, y, new Color(13,45,82).getRGB());
				}
			}
		}
		file = new File(MOD_LOCATION + "\\map\\terrain\\watermapSimple.bmp");
	    ImageIO.write(img, "bmp", file);
	    System.out.println("Done...Watermap");
	}
	// Minor Map Folder Files Functions
	public static void mapFileCreation() throws IOException {
		File file = new File(MOD_LOCATION + "\\provinces.bmp");
		try {
			Files.copy(Paths.get(MOD_LOCATION + "\\provinces.bmp"), Paths.get(MOD_LOCATION + "\\map\\provinces.bmp"));
		} catch (Exception e) {
			File d = new File(MOD_LOCATION + "\\map\\provinces.bmp");
			d.delete();
			Files.copy(Paths.get(MOD_LOCATION + "\\provinces.bmp"), Paths.get(MOD_LOCATION + "\\map\\provinces.bmp"));
		}
		
		BufferedImage img = ImageIO.read(file);
		file = new File(MOD_LOCATION + "\\dominons.bmp");
		BufferedImage dom = ImageIO.read(file);
		System.out.println("Done...Setup");
		
		simpleMapFilesCreation(img, dom);
		provinceMiddleSave(img);
		System.out.println("Done...Middle");
		positionsFileCreation();
		
		System.out.println("Done...Minor");
	}
	public static void simpleMapFilesCreation(BufferedImage img, BufferedImage dom) throws IOException { 
		File file = new File(MOD_LOCATION + "\\map\\definition.csv");
		BufferedWriter def = new BufferedWriter(new FileWriter(file));
		file = new File(MOD_LOCATION + "\\map\\default.map");
		BufferedWriter fau = new BufferedWriter(new FileWriter(file));
		file = new File(MOD_LOCATION + "\\map\\adjacencies.csv");
		BufferedWriter adj = new BufferedWriter(new FileWriter(file));
		
		adj.write("From;To;Type;Through;start_x;start_y;stop_x;stop_y;Comment\n");
		adj.write("1;3;land;2,0;0;5;5;needed-to-run\n");
		adj.write("-1;-1;;-1;-1;-1;-1;-1;-1;");
		adj.close();
		System.out.println("Done...Adjacencies");
		
		ArrayList<Integer> added = new ArrayList<Integer>();
		def.write("province;red;green;blue;x;x\n");
		String name = "aaaa";
		// Land
		for (int y=0; y<img.getHeight(); y++) {
			for (int x=0; x<img.getWidth(); x++) {
				Color curColor = new Color(img.getRGB(x, y), false);
				if (dom.getRGB(x, y) == new Color(150,68,192).getRGB() && !added.contains(img.getRGB(x, y))) {
					String write = added.size()+1 + ";" + curColor.getRed() + ";" + curColor.getGreen() + ";" + curColor.getBlue() + ";pr" + name + ";x\n";
					name = nextName(name);
					def.write(write);
					def.flush();
					added.add(img.getRGB(x, y));
				}
			}
		}
		// Sea
		String seas = "   ";
		name = "aaaa";
		for (int y=0; y<img.getHeight(); y++) {
			for (int x=0; x<img.getWidth(); x++) {
				Color curColor = new Color(img.getRGB(x, y), false);
				if (dom.getRGB(x, y) == new Color(5,20,18).getRGB() && !added.contains(img.getRGB(x, y))) {
					String write = added.size()+1 + ";" + curColor.getRed() + ";" + curColor.getGreen() + ";" + curColor.getBlue() + ";se" + name + ";x\n";
					name = nextName(name);
					def.write(write);
					def.flush();
					seas += added.size()+1 + " ";
					added.add(img.getRGB(x, y));
				}
			}
		}
		// Lakes
		String lakes = "   ";
		name = "aaaa";
		for (int y=0; y<img.getHeight(); y++) {
			for (int x=0; x<img.getWidth(); x++) {				
				Color curColor = new Color(img.getRGB(x, y), false);
				if (dom.getRGB(x, y) == new Color(0,255,0).getRGB() && !added.contains(img.getRGB(x, y))) {
					String write = added.size()+1 + ";" + curColor.getRed() + ";" + curColor.getGreen() + ";" + curColor.getBlue() + ";la" + name + ";x\n";
					name = nextName(name);
					def.write(write);
					def.flush();
					lakes += added.size()+1 + " ";
					added.add(img.getRGB(x, y));
				}
			}
		}
		def.close();
		System.out.println("Done...Definition");
		
		
		fau.write("width = 5632\n"
				+ "height = 2048\n"
				+ "\n"
				+ "max_provinces = " + (added.size()+1) + "\n"
				+ "sea_starts = {\n" + seas + "\n" + "}\n" + "\n"
				+ "lakes = {\n" + lakes + "\n" + "}\n" + "\n"
				+ "only_used_for_random = {\n" + "}\n" + "\n"
				+ "force_coastal = {\n" + "}\n" + "\n" + "\n"
				+ "definitions = \"definition.csv\"\n"
				+ "provinces = \"provinces.bmp\"\n"
				+ "positions = \"positions.txt\"\n"
				+ "terrain = \"terrain.bmp\"\n"
				+ "rivers = \"rivers.bmp\"\n"
				+ "terrain_definition = \"terrain.txt\"\n"
				+ "heightmap = \"heightmap.bmp\"\n"
				+ "tree_definition = \"trees.bmp\"\n"
				+ "continent = \"continent.txt\"\n"
				+ "adjacencies = \"adjacencies.csv\"\n"
				+ "climate = \"climate.txt\"\n"
				+ "region = \"region.txt\"\n"
				+ "superregion = \"superregion.txt\"\n"
				+ "area = \"area.txt\"\n"
				+ "provincegroup = \"provincegroup.txt\"\n"
				+ "ambient_object = \"ambient_object.txt\"\n"
				+ "seasons = \"seasons.txt\"\n"
				+ "trade_winds = \"trade_winds.txt\"\n" + "\n" + "\n"
				+ "# Define which indices in trees.bmp palette which should count as trees for automatic terrain assignment\n"
				+ "tree = { 3 4 7 10 }\n"
				+ "");
		fau.close();
		System.out.println("Done...Default");
	}
	public static void positionsFileCreation() throws IOException {
		File file = new File(MOD_LOCATION + "\\map\\positions.txt");
		BufferedWriter pos = new BufferedWriter(new FileWriter(file));
		file = new File(MOD_LOCATION + "\\data.txt");
		BufferedReader data = new BufferedReader(new FileReader(file));
		file = new File(MOD_LOCATION + "\\map\\definition.csv");
		BufferedReader def = new BufferedReader(new FileReader(file));
		
		Map<Integer, Coordinate> positions = dataRetreave(data);
		
		String lines = def.readLine();
		lines = def.readLine();
		while (lines != null) {
			String line[] = lines.split(";");
			int curColor = new Color(Integer.parseInt(line[1]),Integer.parseInt(line[2]),Integer.parseInt(line[3])).getRGB();
			
			Coordinate curCord = positions.get(curColor);
			// x = x, y = 2048 - y
			pos.write("#" + line[4] + "\n");
			pos.write(line[0] + "={\n");
			pos.write("\tposition={\n");
			String p = curCord.getX() + ".000 " + (2048 - curCord.getY()) + ".000";
			p = p + " " + p + " " + p + " " + p + " " + p + " " + p + " " + p;
			pos.write("\t\t" + p + "\n");
			pos.write("\t}\n");
			pos.write("\trotation={\n");
			pos.write("\t\t0.000 0.000 0.000 0.000 0.000 0.000 0.000\n");
			pos.write("\t}\n");
			pos.write("\theight={\n");
			pos.write("\t\t0.000 0.000 1.000 0.000 0.000 0.000 0.000\n");
			pos.write("\t}\n");
			pos.write("}\n");
			pos.flush();
			lines = def.readLine();
		}
		def.close();
		data.close();
		pos.close();
	}
	// Map Folder Area type files
	public static void areaTypeCreation() throws IOException {
		File file = new File(MOD_LOCATION + "\\data.txt");
		BufferedReader data = new BufferedReader(new FileReader(file));
		file = new File(MOD_LOCATION + "\\map\\definition.csv");
		BufferedReader def = new BufferedReader(new FileReader(file));
		
		Map<Integer, Coordinate> positions = dataRetreave(data);
		Map<Integer, Integer> provNumber = new HashMap<Integer, Integer>();
		
		String lines = def.readLine();
		lines = def.readLine();
		while (lines != null) {
			String[] line = lines.split(";");
			provNumber.put(new Color(Integer.parseInt(line[1]), Integer.parseInt(line[2]), Integer.parseInt(line[3])).getRGB(), Integer.parseInt(line[0]));
			lines = def.readLine();
		}
		def.close();
		
		file = new File(MOD_LOCATION + "\\areas.bmp");
		BufferedImage area_image = ImageIO.read(file);
		file = new File(MOD_LOCATION + "\\regions.bmp");
		BufferedImage region_image = ImageIO.read(file);
		file = new File(MOD_LOCATION + "\\subcontinents.bmp");
		BufferedImage subcontinents_image = ImageIO.read(file);
		file = new File(MOD_LOCATION + "\\continents.bmp");
		BufferedImage continents_image = ImageIO.read(file);
		file = new File(MOD_LOCATION + "\\provinces.bmp");
		BufferedImage province_image = ImageIO.read(file);
		System.out.println("Done...Area Type prep");
		
		Map<Integer, ArrayList<Integer>> areas = new HashMap<>();
		for (Coordinate cord : positions.values()) {
			int curColor = area_image.getRGB(cord.getX(), cord.getY());
			
			int i = 1;
			while (curColor == -1 || curColor == -16777216 || curColor == -16444398) {
				int n, e, s, w;
				try { n = area_image.getRGB(cord.getX()+i, cord.getY()); } catch (ArrayIndexOutOfBoundsException er) { n = area_image.getRGB(cord.getX(), cord.getY()); }
				try { e = area_image.getRGB(cord.getX(), cord.getY()+i); } catch (ArrayIndexOutOfBoundsException er) { e = area_image.getRGB(cord.getX(), cord.getY()); }
				try { s = area_image.getRGB(cord.getX()-i, cord.getY()); } catch (ArrayIndexOutOfBoundsException er) { s = area_image.getRGB(cord.getX(), cord.getY()); }
				try { w = area_image.getRGB(cord.getX(), cord.getY()-i); } catch (ArrayIndexOutOfBoundsException er) { w = area_image.getRGB(cord.getX(), cord.getY()); }
				
				if (n != -1 || curColor == -16777216 || curColor == -16444398) { curColor = n; }
				else if (e != -1 || curColor == -16777216 || curColor == -16444398) { curColor = e; }
				else if (s != -1 || curColor == -16777216 || curColor == -16444398) { curColor = s; }
				else if (w != -1 || curColor == -16777216 || curColor == -16444398) { curColor = w; }
				i++;
			}
			
			ArrayList<Integer> curArea = areas.get(curColor);
			if (curArea == null) { curArea = new ArrayList<Integer>(); }
			curArea.add(provNumber.get(province_image.getRGB(cord.getX(), cord.getY())));
			Collections.sort(curArea);
			areas.put(curColor, curArea);
		}
		
		file = new File(MOD_LOCATION + "\\map\\area.txt");
		BufferedWriter areaFile = new BufferedWriter(new FileWriter(file));
		
		Map<Integer, String> areaNames = new HashMap<Integer, String>();
		String name = "aaaa";
		for (int curArea : areas.keySet()) {
			ArrayList<Integer> area = areas.get(curArea);
			areaFile.write(name + "_area = {\n");
			String areaString = area.toString().replace(", ", " ");
			areaString = areaString.substring(1,areaString.length()-1);
			areaFile.write("    " + areaString + "\n");
			areaFile.write("}\n");
			
			areaNames.put(curArea, name+"_area");
			name = nextName(name);
		}
		areaFile.close();
		System.out.println("Done...Areas");
		
		file = new File(MOD_LOCATION + "\\map\\region.txt");
		BufferedWriter regionFile = new BufferedWriter(new FileWriter(file));
		
		Map<Integer, ArrayList<String>> regions = new HashMap<>();
		Map<Integer, String> regionName = new HashMap<Integer, String>();
		name = "aaaa";
		
		for (Coordinate cord : positions.values()) {
			int curColor = region_image.getRGB(cord.getX(), cord.getY());
			
			int i = 1;
			while (curColor == -1 || curColor == -16777216 || curColor == -16444398) {
				int n, e, s, w;
				try { n = region_image.getRGB(cord.getX()+i, cord.getY()); } catch (ArrayIndexOutOfBoundsException er) { n = region_image.getRGB(cord.getX(), cord.getY()); }
				try { e = region_image.getRGB(cord.getX(), cord.getY()+i); } catch (ArrayIndexOutOfBoundsException er) { e = region_image.getRGB(cord.getX(), cord.getY()); }
				try { s = region_image.getRGB(cord.getX()-i, cord.getY()); } catch (ArrayIndexOutOfBoundsException er) { s = region_image.getRGB(cord.getX(), cord.getY()); }
				try { w = region_image.getRGB(cord.getX(), cord.getY()-i); } catch (ArrayIndexOutOfBoundsException er) { w = region_image.getRGB(cord.getX(), cord.getY()); }
				
				if (n != -1 || curColor == -16777216 || curColor == -16444398) { curColor = n; }
				else if (e != -1 || curColor == -16777216 || curColor == -16444398) { curColor = e; }
				else if (s != -1 || curColor == -16777216 || curColor == -16444398) { curColor = s; }
				else if (w != -1 || curColor == -16777216 || curColor == -16444398) { curColor = w; }
				i++;
			}
			
			ArrayList<String> curRegion = regions.get(curColor);
			if (curRegion == null) { curRegion = new ArrayList<String>(); }
			
			String newRegion = areaNames.get(area_image.getRGB(cord.getX(), cord.getY()));
			if (curRegion.contains(newRegion) || newRegion == null) { continue; }
			curRegion.add(newRegion);
			
			regions.put(curColor, curRegion);
		}
		
		for (int curRegion: regions.keySet()) {
			ArrayList<String> regionNames = regions.get(curRegion);
			regionName.put(curRegion, name + "_region");
			regionFile.write(name + "_region = {\n");
			regionFile.write("    areas = {");
			String names = regionNames.toString().replace(", ", "\n      ");
			names = names.substring(1, names.length()-1);
			regionFile.write("\n      " + names);
			regionFile.write("\n  }\n}\n\n");
			name = nextName(name);
		}
		
		regionFile.close();
		System.out.println("Done...Regions");
		
		file = new File(MOD_LOCATION + "\\map\\superregion.txt");
		BufferedWriter subFile = new BufferedWriter(new FileWriter(file));
		
		Map<Integer, ArrayList<String>> supperregion = new HashMap<>();
		Map<Integer, String> supperregionName = new HashMap<Integer, String>();
		name = "aaaa";
		
		for (Coordinate cord : positions.values()) {
			int curColor = subcontinents_image.getRGB(cord.getX(), cord.getY());
			
			int i = 1;
			while (curColor == -1 || curColor == -16777216 || curColor == -16444398) {
				int n, e, s, w;
				try { n = subcontinents_image.getRGB(cord.getX()+i, cord.getY()); } catch (ArrayIndexOutOfBoundsException er) { n = subcontinents_image.getRGB(cord.getX(), cord.getY()); }
				try { e = subcontinents_image.getRGB(cord.getX(), cord.getY()+i); } catch (ArrayIndexOutOfBoundsException er) { e = subcontinents_image.getRGB(cord.getX(), cord.getY()); }
				try { s = subcontinents_image.getRGB(cord.getX()-i, cord.getY()); } catch (ArrayIndexOutOfBoundsException er) { s = subcontinents_image.getRGB(cord.getX(), cord.getY()); }
				try { w = subcontinents_image.getRGB(cord.getX(), cord.getY()-i); } catch (ArrayIndexOutOfBoundsException er) { w = subcontinents_image.getRGB(cord.getX(), cord.getY()); }
				
				if (n != -1 || curColor == -16777216 || curColor == -16444398) { curColor = n; }
				else if (e != -1 || curColor == -16777216 || curColor == -16444398) { curColor = e; }
				else if (s != -1 || curColor == -16777216 || curColor == -16444398) { curColor = s; }
				else if (w != -1 || curColor == -16777216 || curColor == -16444398) { curColor = w; }
				i++;
			}
			
			ArrayList<String> curSupperregion = supperregion.get(curColor);
			if (curSupperregion == null) { curSupperregion = new ArrayList<String>(); }
			
			String newSuper = regionName.get(area_image.getRGB(cord.getX(), cord.getY()));
			if (curSupperregion.contains(newSuper) || newSuper == null) { continue; }
			curSupperregion.add(newSuper);
			
			supperregion.put(curColor, curSupperregion);
		}
		
		for (int curSupperregion: supperregion.keySet()) {
			ArrayList<String> regionNames = supperregion.get(curSupperregion);
			supperregionName.put(curSupperregion, name + "_superregion");
			subFile.write(name + "_superregion = {");
			String names = regionNames.toString().replace(", ", "\n\t");
			names = names.substring(1, names.length()-1);
			subFile.write("\n\t" + names);
			subFile.write("\n}\n\n");
			name = nextName(name);
		}
		
		subFile.close();
		System.out.println("Done...Supperregion");
		
		Map<Integer, ArrayList<Integer>> continents = new HashMap<>();
		for (Coordinate cord : positions.values()) {
			int curColor = continents_image.getRGB(cord.getX(), cord.getY());
			
			int i = 1;
			while (curColor == -1 || curColor == -16777216 || curColor == -16444398) {
				int n, e, s, w;
				try { n = continents_image.getRGB(cord.getX()+i, cord.getY()); } catch (ArrayIndexOutOfBoundsException er) { n = continents_image.getRGB(cord.getX(), cord.getY()); }
				try { e = continents_image.getRGB(cord.getX(), cord.getY()+i); } catch (ArrayIndexOutOfBoundsException er) { e = continents_image.getRGB(cord.getX(), cord.getY()); }
				try { s = continents_image.getRGB(cord.getX()-i, cord.getY()); } catch (ArrayIndexOutOfBoundsException er) { s = continents_image.getRGB(cord.getX(), cord.getY()); }
				try { w = continents_image.getRGB(cord.getX(), cord.getY()-i); } catch (ArrayIndexOutOfBoundsException er) { w = continents_image.getRGB(cord.getX(), cord.getY()); }
				
				if (n != -1 || curColor == -16777216 || curColor == -16444398) { curColor = n; }
				else if (e != -1 || curColor == -16777216 || curColor == -16444398) { curColor = e; }
				else if (s != -1 || curColor == -16777216 || curColor == -16444398) { curColor = s; }
				else if (w != -1 || curColor == -16777216 || curColor == -16444398) { curColor = w; }
				i++;
			}
			
			ArrayList<Integer> curContinent = continents.get(curColor);
			if (curContinent == null) { curContinent = new ArrayList<Integer>(); }
			curContinent.add(provNumber.get(province_image.getRGB(cord.getX(), cord.getY())));
			Collections.sort(curContinent);
			continents.put(curColor, curContinent);
		}
		
		file = new File(MOD_LOCATION + "\\map\\continent.txt");
		BufferedWriter contFile = new BufferedWriter(new FileWriter(file));
		file = new File(MOD_LOCATION + "\\map\\climate.txt");
		BufferedWriter climateFile = new BufferedWriter(new FileWriter(file));
		climateFile.write("tropical = {\n\t");
		Map<Integer, String> continentName = new HashMap<Integer, String>();
		name = "aaaa";
		for (int curCont : continents.keySet()) {
			if (curCont == new Color(0,0,255).getRGB()) { continue; }
			ArrayList<Integer> continent = continents.get(curCont);
			contFile.write(name + "_continent = {\n");
			continentName.put(curCont, name + "_continent");
			String contString = continent.toString().replace(", ", " ");
			contString = contString.substring(1,contString.length()-1);
			String c[] = contString.split(" ");
			contFile.write("  ");
			int count = 0;
			for (String num : c) { 
				contFile.write(num + " "); 
				climateFile.write(num + " ");
				if (count == 25) { 
					contFile.write("\n  "); 
					climateFile.write("\n\t");
					count = 0; 
				}
				count++;
			}
			
			contFile.write("\n}\n");
			
			name = nextName(name);
		}
		contFile.close();
		System.out.println("Done...Continents");
		
		climateFile.write("\n}");
		climateFile.close();
		System.out.println("Done...Climate");
	}
	
	/* History Folder Functions */
	public static void historyFolderCreation() throws IOException {
		System.out.println("Start..History");
		new File(MOD_LOCATION + "\\history").mkdir();
		new File(MOD_LOCATION + "\\history\\advisors").mkdir();
		new File(MOD_LOCATION + "\\history\\countries").mkdir();
		new File(MOD_LOCATION + "\\history\\diplomacy").mkdir();
		new File(MOD_LOCATION + "\\history\\provinces").mkdir();
		new File(MOD_LOCATION + "\\history\\wars").mkdir();
		
		historyProvinceFilesCreation();
		historyCountryFileCopy();
		
		System.out.println("Done...History\n");
	}
	// History Province Files
	public static void historyProvinceFilesCreation() throws IOException {
		File file = new File(MOD_LOCATION + "\\map\\definition.csv");
		BufferedReader def = new BufferedReader(new FileReader(file));
		
		String lines = def.readLine();
		lines = def.readLine();
		while (lines != null) {
			String[] line = lines.split(";");
			file = new File(MOD_LOCATION + "\\history\\provinces\\" + line[0] + " - " + line[4] + ".txt");
			BufferedWriter curFile = new BufferedWriter(new FileWriter(file));
			if (line[4].substring(0,2).equals("se") || line[4].substring(0,2).equals("la")) {
				curFile.close();
				lines = def.readLine();
				continue;
			}
			
			curFile.write("owner = USA\n");
			curFile.write("controller = USA\n");
			curFile.write("capital = \"" + line[4]+ "\"\n");
			curFile.write("is_city = yes\n");
			curFile.write("culture = american\n");
			curFile.write("religion = catholic\n");
			curFile.write("hre = no\n");
			curFile.write("base_tax = 3\n");
			curFile.write("base_production = 3\n");
			curFile.write("base_manpower = 4\n");
			curFile.write("trade_goods = grain\n");
			curFile.write("add_core = USA\n");
			
			lines = def.readLine();
			curFile.close();
		}
		System.out.println("Done...Provinces");
		def.close();
	}
	public static void historyCountryFileCopy() throws IOException {
		try {
			Files.copy(Paths.get(EU4_PATH + "\\history\\countries\\USA - United States.txt"), Paths.get(MOD_LOCATION + "\\history\\countries\\USA - United States.txt"));
		} catch (Exception e) {
			File d = new File(MOD_LOCATION + "\\history\\countries\\USA - United States.txt");
			d.delete();
			Files.copy(Paths.get(EU4_PATH + "\\history\\countries\\USA - United States.txt"), Paths.get(MOD_LOCATION + "\\history\\countries\\USA - United States.txt"));
		}
	}
	
	/* Localisation Folder Functions */
	public static void localisationFolderCreation() throws IOException{
		System.out.println("Start..Localisation");
		new File(MOD_LOCATION + "\\localisation").mkdir();
		
		provinceNameLocalisation();
		areaNameLocalisation();
		System.out.println("Done...Localisation\n");
	}
	public static void provinceNameLocalisation() throws IOException {
		File file = new File(MOD_LOCATION + "\\localisation\\prov_names_l_english.yml");
		BufferedWriter provNames = new BufferedWriter(new FileWriter(file));
		file = new File(MOD_LOCATION + "\\localisation\\prov_names_adj_l_english.yml");
		BufferedWriter provNamesAdj = new BufferedWriter(new FileWriter(file));
		file = new File(MOD_LOCATION + "\\map\\definition.csv");
		BufferedReader def = new BufferedReader(new FileReader(file));
		provNames.write("l_english:\n"); provNamesAdj.write("l_english:\n");
		
		String lines = def.readLine();
		lines = def.readLine();
		while (lines != null) {
			String line[] = lines.split(";");
			provNames.write(" PROV" + line[0] + ":0 \"" + line[4] +  "\"\n");
			provNamesAdj.write(" PROV_ADJ" + line[0] + ":0 \"" + line[4] +  "\"\n");
			lines = def.readLine();
		}
		provNames.close();
		provNamesAdj.close();
		def.close();
		System.out.println("Done...Province");
	}
	public static void areaNameLocalisation() throws IOException {
		File file = new File(MOD_LOCATION + "\\localisation\\area_regions_l_english.yml");
		BufferedWriter areaNames = new BufferedWriter(new FileWriter(file));
		areaNames.write("l_english:\n");
		
		file = new File(MOD_LOCATION + "\\map\\area.txt");
		BufferedReader areas = new BufferedReader(new FileReader(file));
		file = new File(MOD_LOCATION + "\\map\\region.txt");
		BufferedReader regions = new BufferedReader(new FileReader(file));
		file = new File(MOD_LOCATION + "\\map\\superregion.txt");
		BufferedReader superRegions = new BufferedReader(new FileReader(file));
		
		String lines = areas.readLine();
		while (lines != null) {
			String line[] = lines.split(" ");
			if (line[0].contains("_area")) {
				areaNames.write(" " + line[0] + ":0 \"Area " + line[0].substring(0,4) + "\"\n");
				areaNames.write(" " + line[0] + "_name:0 \"Area " + line[0].substring(0,4) + "\"\n");
				areaNames.write(" " + line[0] + "_adj:0 \"Area " + line[0].substring(0,4) + "\"\n");
			}
			lines = areas.readLine();
		}
		
		lines = regions.readLine();
		while (lines != null) {
			String line[] = lines.split(" ");
			if (line[0].contains("_region")) {
				areaNames.write(" " + line[0] + ":0 \"Region " + line[0].substring(0,4) + "\"\n");
				areaNames.write(" " + line[0] + "_name:0 \"Region " + line[0].substring(0,4) + "\"\n");
			}
			lines = regions.readLine();
		}
		
		lines = superRegions.readLine();
		while (lines != null) {
			String line[] = lines.split(" ");
			if (line[0].contains("_superregion")) {
				areaNames.write(" " + line[0] + ":0 \"Superregion " + line[0].substring(0,4) + "\"\n");
				areaNames.write(" " + line[0] + "_name:0 \"Superregion " + line[0].substring(0,4) + "\"\n");
			}
			lines = superRegions.readLine();
		}
		
		areaNames.close();
		areas.close();
		regions.close();
		superRegions.close();
		System.out.println("Done...Areas");
	}
	
	// Information Functions //
	public static void provinceInfo(BufferedImage img) throws IOException { 
		File file = new File(MOD_LOCATION + "\\dominons.bmp");
		BufferedImage dom = ImageIO.read(file);
		int b = new Color(5,20,18).getRGB();
		ArrayList colorList = new ArrayList();
		for (int y=0; y<img.getHeight(); y++) {
			for (int x=0; x<img.getWidth(); x++) {
				int color = img.getRGB(x, y);
				if (dom.getRGB(x, y) != b && !colorList.contains(color)) {
					colorList.add(color);
				}
			}
		}
		System.out.println("Cur:   " + colorList.size());
		System.out.println("Vs EU: " + ((int) Math.round(((double) colorList.size() / 3137) * 100)) + "%");
		System.out.println("Done...Info");
	}
	
	// Utility Functions //
	public static void nonUsedAreas() throws IOException {
		File file = new File(EU4_PATH + "\\map\\area.txt");
		BufferedReader area = new BufferedReader(new FileReader(file));
		ArrayList<String> areas = new ArrayList<String>();
		
		String line = area.readLine();
		while (line != null) {
			if (line.contains(" = {") && !line.contains("color")) {
				areas.add(line.replace('\n', ' ').replace('}', ' ').strip() + "\n}\n");
			}
			line = area.readLine();
		}
		area.close();
		
		file = new File(EU4_PATH + "\\map\\region.txt");
		BufferedReader region = new BufferedReader(new FileReader(file));
		ArrayList<String> regions = new ArrayList<String>();
		
		line = region.readLine();
		while (line != null) {
			if (line.contains(" = {") && line.contains("_region")) {
				regions.add(line.replace('\n', ' ').replace('}', ' ').strip() + "\n}\n");
			}
			line = region.readLine();
		}
		region.close();
		
		file = new File(EU4_PATH + "\\map\\superregion.txt");
		BufferedReader superregion = new BufferedReader(new FileReader(file));
		ArrayList<String> superregions = new ArrayList<String>();
		
		line = superregion.readLine();
		while (line != null) {
			if (line.contains(" = {") && line.contains("_superregion")) {
				superregions.add(line.replace('\n', ' ').replace('}', ' ').strip() + "\n}\n");
			}
			line = superregion.readLine();
		}
		superregion.close();
		
		file = new File(EU4_PATH + "\\map\\continent.txt");
		BufferedReader continent = new BufferedReader(new FileReader(file));
		ArrayList<String> continents = new ArrayList<String>();
		
		line = continent.readLine();
		while (line != null) {
			if (line.contains(" = {")) {
				continents.add(line.replace('\n', ' ').replace('}', ' ').strip() + "\n}\n");
			}
			line = continent.readLine();
		}
		continent.close();
		
		file = new File(MOD_LOCATION + "\\nonArea.txt");
		BufferedWriter areaFile = new BufferedWriter(new FileWriter(file));
		file = new File(MOD_LOCATION + "\\nonRegion.txt");
		BufferedWriter regionFile = new BufferedWriter(new FileWriter(file));
		file = new File(MOD_LOCATION + "\\nonSuperregion.txt");
		BufferedWriter supFile = new BufferedWriter(new FileWriter(file));
		file = new File(MOD_LOCATION + "\\nonContinent.txt");
		BufferedWriter contFile = new BufferedWriter(new FileWriter(file));
		for (String s : areas) { areaFile.write(s); }
		for (String s : regions) { regionFile.write(s); }
		for (String s : superregions) { supFile.write(s); }
		for (String s : continents) { contFile.write(s); }
		
		areaFile.close();
		regionFile.close();
		supFile.close();
		contFile.close();
	}
	public static void provinceMiddleSave(BufferedImage img) throws IOException {
		File file = new File(MOD_LOCATION + "\\data.txt");
		if (file.exists()) {
			return;
		}
		Shapes shapes = new Shapes();
		System.out.print("Done...");
		for (int y=0; y<img.getHeight(); y++) {
			for (int x=0; x<img.getWidth(); x++) {
				int color = img.getRGB(x, y);
				shapes.put(color, x, y);
			}
			if (y%400 == 0) {
				System.out.print((int) Math.round((double) y/2048 * 100) + "% ");
			}
		}
		System.out.println();
		
		BufferedWriter fp = new BufferedWriter(new FileWriter(file));
		int count = 0;
		System.out.print("Done...");
		for (int key : shapes.keySet()) {
			Shape curShape = shapes.get(key);
			String write = key + "," + curShape.findCenter().toString() + "\n";
			fp.write(write);
			fp.flush();
			count++;
			if (count%400 == 0) {
				System.out.print((int) Math.round((double) count/shapes.size() * 100) + "% ");
			}
		}
		System.out.println();
		fp.close();
	}
	public static String nextName(String name) {
		char a = name.charAt(0);
		char b = name.charAt(1);
		char c = name.charAt(2);
		char d = name.charAt(3);
		d++;
		if (d > 'z') {
			d = 'a';
			c++;
		}
		if (c > 'z') {
			c = 'a';
			b++;
		}
		if (b > 'z') {
			b = 'a';
			a++;
		}
		return "" + a + b + c + d;
	}
	public static Map<Integer, Coordinate> dataRetreave(BufferedReader data) throws IOException {
		Map<Integer,Coordinate> ret = new HashMap<Integer, Coordinate>();
		
		String lines = data.readLine();
		while (lines != null) {
			String line[] = lines.split(",");
			
			int color = Integer.parseInt(line[0]);
			line[1] = line[1].substring(1, line[1].length() - 1);
			int x = Integer.parseInt(line[1].substring(0, line[1].indexOf('|')));
			int y = Integer.parseInt(line[1].substring(line[1].indexOf('|')+1, line[1].length()));
			
			ret.put(color, new Coordinate(x,y));
			
			lines = data.readLine();
		}
		
		return ret;
	}
	public static void simpleMapCreation(BufferedImage img, BufferedImage dom) throws IOException {
		for (int y=0; y<img.getHeight(); y++) {
			for (int x=0; x<img.getWidth(); x++) {
				if (dom.getRGB(x, y) == new Color(5,20,18).getRGB() || dom.getRGB(x, y) == new Color(0,255,0).getRGB()) {
					img.setRGB(x, y, new Color(0,0,255).getRGB());
				}
			}
		}
		
		for (int y=0; y<img.getHeight(); y++) {
			for (int x=1; x<img.getWidth()-1; x++) {
				int l = img.getRGB(x-1, y);
				int r = img.getRGB(x+1, y);
				int c = img.getRGB(x, y);
								
				if (l == new Color(0,0,0).getRGB() 
						|| r == new Color(0,0,0).getRGB() 
						|| c == new Color(0,0,0).getRGB()) 
				{ 
					continue;
				}
				if (l != r && (l == c || r == c) ) {
					img.setRGB(x, y, new Color(0,0,0).getRGB());
				}
			}
		}
		
		for (int y=1; y<img.getHeight()-1; y++) {
			for (int x=0; x<img.getWidth(); x++) {
				int u = img.getRGB(x, y-1);
				int d = img.getRGB(x, y+1);
				int c = img.getRGB(x, y);
				
				
				if (u == new Color(0,0,0).getRGB() 
						|| d == new Color(0,0,0).getRGB() 
						|| c == new Color(0,0,0).getRGB()) 
				{ 
					continue;
				}
				if (u != d && (u == c || d == c) ) {
					img.setRGB(x, y, new Color(0,0,0).getRGB());
				}
			}
		}
		
		for (int y=1; y<img.getHeight()-1; y++) {
			for (int x=0; x<img.getWidth(); x++) {
				if (img.getRGB(x, y) == new Color(0,0,0).getRGB()) { continue; }
				if (img.getRGB(x, y) == new Color(0,0,255).getRGB()) { continue; }
				//fill.floodFill(img, x, y, new Color(img.getRGB(x, y), false), new Color(0,0,0));
				img.setRGB(x, y, new Color(255,255,255).getRGB());
			}
		}
		
		File file = new File(MOD_LOCATION + "\\simpleMap.bmp");
	    ImageIO.write(img, "bmp", file);
	    System.out.println("Done...Blank");
	}
	public static void provinceSizes() throws IOException {
		File file = new File(MOD_LOCATION + "\\provinces.bmp");
		BufferedImage img = ImageIO.read(file);
		Map<Integer, Integer> map = new HashMap<>();
		for (int y=0; y<img.getHeight(); y++) {
			for (int x=0; x<img.getWidth(); x++) {
				int color = img.getRGB(x, y);
				if (map.containsKey(color)) {
					Integer a = map.get(color) + 1;
					map.put(color, a);
				} else {
					map.put(color, 1);
				}
			}
		}
		for (int key : map.keySet()) {
			if (map.get(key) > 100294) {
				Color color = new Color(key);
				System.out.println(color.getRed() + " " + color.getGreen() + " " + color.getBlue());
			}
		}
	}
}