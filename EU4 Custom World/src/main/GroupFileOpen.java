package main;

import java.awt.Desktop;
import java.io.*;
import java.util.Random;

public class GroupFileOpen {
	// File Location Strings
	final static String modLocal	 = "C:\\Users\\Colin\\Documents\\Paradox Interactive\\Europa Universalis IV\\mod\\Dominons";
	final static String provLocal 	 = modLocal + "\\history\\provinces\\";
	final static String countryLocal = modLocal + "\\history\\countries\\";
	final static String areaLocal 	 = modLocal + "\\map\\";
	final static String commonLocal  = modLocal + "\\common\\";
	final static String localLocal   = modLocal + "\\localisation\\";
	// Utility Strings
	final static String tradegoods 	 = "cloth fish fur grain livestock naval_supplies salt wine wool copper iron ivory slaves chinaware spices "
										+ "tea cocoa sugar tobacco dyes silk tropical_wood glass paper gems coal cloves unknown";
	
	// Main
	public static void main(String[] args) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		System.out.print("CMD: ");
		String input = reader.readLine();
		String lastTag = "";
		while (!input.equals("exit")) {
			String[] inputs = input.split(" ");
			// core 2001 A01
			// 2002 2003
			// area aaaa
			// region aaaa
			// tag A01 Eldahum 2001
			// dev 2001 3 5 4
			// tg 2001 grain
			// pra aaaa
			// prr aaaa
			
			switch (inputs[0]) {
				case "core": {
					// core prov tag : core 2001 A01
					System.out.println("CMD: CORE prov");
					core(inputs[1], inputs[2]);
					lastTag = inputs[2];
					break;
				}
				case "area": {
					// area area : area aaaa
					openArea(inputs[1]);
					break;
				}
				case "region": {
					// region region : region aaaa
					System.out.println("CMD: OPEN region");
					openRegion(inputs[1]);
					break;
				}
				case "tag": {
					// tag tag name capital : tag A01 Eldahum 2001
					System.out.println("CMD: TAG nation");
					tag(inputs[1], inputs[2], inputs[3]);
					lastTag = inputs[1];
					break;
				}
				case "dev": {
					// dev prov tax production manpower : dev 2001 5 5 5
					System.out.println("CMD: DEV " + (Integer.parseInt(inputs[2]) + Integer.parseInt(inputs[3]) + Integer.parseInt(inputs[4])));
					dev(inputs[1], inputs[2], inputs[3], inputs[4]);
					break;
				}
				case "tg": {
					// tg prov good : tg 2002 grain
					System.out.println("CMD: TRADEGOOD prov");
					tradegood(inputs[1], inputs[2]);
					break;
				}
				case "pra": {
					// pra area : pra aaaa
					System.out.println("CMD: PRINT prov");
					System.out.print("CMD: PROV = ");
					printArea(inputs[1]);
					System.out.println();
					break;
				}
				case "prr": {
					//prr region : prr aaaa
					System.out.println("CMD: PRINT region");
					System.out.print("CMD: PROV = ");
					printRegion(inputs[1]);
					System.out.println();
					break;
				}
				default: {
					if (lastTag != "") {
						try {
							// Used to test if input[0] is a number for multi province coring
							@SuppressWarnings("unused")
							int num = Integer.parseInt(inputs[0]);
							for (String n : inputs) {
								core(n, lastTag);
							}
							System.out.println("CMD: CORE provs");
						} catch (NumberFormatException e) {
							System.out.println("CMD: UNKNOWN Command");
						}
					} else { System.out.println("CMD: UNKNOWN Command"); }
				}
			}
			System.out.print("CMD: ");
			input = reader.readLine();
		}
		System.out.println("CMD: EXIT Command");
	}
	
	// Action Functions
	public static void core(String prov, String tag) throws IOException {
		String name = nameReverse(prov);
		replaceInFile(provLocal + prov + " - pr" + name + ".txt", "owner = ...", ("owner = " + tag));
		replaceInFile(provLocal + prov + " - pr" + name + ".txt", "controller = ...", ("controller = " + tag));
		replaceInFile(provLocal + prov + " - pr" + name + ".txt", "add_core = ...", ("add_core = " + tag));
	}
	public static void openArea(String area) throws IOException {
		String provs[] = getArea(area);
		System.out.println("CMD: AREA " + area);
		for (String prov : provs) {
			File file = new File(provLocal + prov + " - pr" + nameReverse(prov) + ".txt");
			Desktop.getDesktop().open(file);
		}
	}
	public static void openRegion(String region) throws IOException {
		String[] areas = getRegion(region);
		for (String area : areas) {
			openArea(area);
		}
	}
	public static void tag(String tag, String name, String capital) throws IOException {
		name = name.replace("_", " ");
		// History Countries File
		File file = new File(countryLocal + tag + " - " + name + ".txt");
		BufferedWriter newNation = new BufferedWriter(new FileWriter(file));
		newNation.write("government = monarchy\r\n"
				+ "add_government_reform = feudalism_reform\r\n"
				+ "mercantilism = 25\r\n"
				+ "technology_group = western\r\n"
				+ "primary_culture = american\r\n"
				+ "religion = catholic\r\n"
				+ "capital = " + capital + "\r\n");
		
		newNation.write("1444.11.10 = {\r\n"
				+ "	monarch = {\r\n"
				+ "		name = \"Temp I\"\r\n"
				+ "		dynasty = \"Temp\"\r\n"
				+ "		birth_date = 1388.12.3\r\n"
				+ "		adm = 3\r\n"
				+ "		dip = 3\r\n"
				+ "		mil = 3\r\n"
				+ "	}\r\n"
				+ "}\r\n"
				+ "1444.11.11 = {\r\n"
				+ "	heir = {\r\n"
				+ "		name = \"Temp II\"\r\n"
				+ "		dynasty = \"Temp\"\r\n"
				+ "		birth_date = 1421.7.4\r\n"
				+ "		claim = 65\r\n"
				+ "		adm = 3\r\n"
				+ "		dip = 3\r\n"
				+ "		mil = 3\r\n"
				+ "	}\r\n"
				+ "}");
		newNation.close();
		// Common Countries File    
		file = new File(commonLocal + "countries\\" + name + ".txt");
        newNation = new BufferedWriter(new FileWriter(file));
        file = new File(commonLocal + "countries\\Eldahum.txt");
        BufferedReader oldNation = new BufferedReader(new FileReader(file));
		
        String content = "";
        String curLine = oldNation.readLine();
        Random random = new Random();
        int r = random.nextInt(255-1+1) + 1;
        int g = random.nextInt(255-1+1) + 1;
        int b = random.nextInt(255-1+1) + 1;
        String c = r + " " + g + " " + b;
        while (curLine != null) {
        	if (curLine.contains("color = {")) {
        		content += "color = { " + c + " }\n";
        	} else if (curLine.contains("revolutionary_colors = {")) {
        		content += "revolutionary_colors = { " + c + " }\n";
        	} else {
        		content += curLine + "\n";
        	}
        	curLine = oldNation.readLine();
        }
        newNation.write(content);
		oldNation.close();
		newNation.close();
		// Common Country Tag File
        addToFile(commonLocal + "country_tags\\dom_countries.txt", tag + " = \"countries/" + name + ".txt\"\n");
		// Common Country Color File
        String toAdd = "# " + name + "\r\n"
        		+ tag + " = {\r\n"
        		+ "\tcolor1={ 193  26  14 }\r\n"
        		+ "\tcolor2={ 193  26  14 }\r\n"
        		+ "\tcolor3={ 193  26  14 }\r\n"
        		+ "}\n";
        addToFile(commonLocal + "country_colors\\dom_country_colors.txt", toAdd);
        // Localization
        toAdd 	= " " + tag + ": \"" + name +"\"\r\n"
        		+ " " + tag + "_ADJ: \"" + name + "\"\r\n"
        		+ " " + tag + "_ADJ2: \"" + name + "\"\r\n";
        addToFile(localLocal + "dom_nations_l_english.yml", toAdd);
        core(capital, tag);
	}
	public static void dev(String prov, String t, String p, String m) {
		String name = nameReverse(prov);
		replaceInFile(provLocal + prov + " - pr" + name + ".txt", "base_tax = .+", ("base_tax = " + t));
		replaceInFile(provLocal + prov + " - pr" + name + ".txt", "base_production = .+", ("base_production = " + p));
		replaceInFile(provLocal + prov + " - pr" + name + ".txt", "base_manpower = .+", ("base_manpower = " + m));
	}
	public static void tradegood(String prov, String good) {
		good = good.toLowerCase();
		if (!tradegoods.contains(good)) {
			System.out.println("CMD: UNKNOWN tradegood");
			return;
		}
		replaceInFile(provLocal + prov + " - pr" + nameReverse(prov) + ".txt", "trade_goods = .+", "trade_goods = " + good);
	}
	public static void printArea(String area) throws IOException {
		String[] provs = getArea(area);
		for (String prov : provs) { 
			System.out.print(prov + " ");
		}
	}
	public static void printRegion(String region) throws IOException {
		String[] areas = getRegion(region);
		for (String area : areas) {
			printArea(area);
		}
	}
	
	// Utility Functions
	public static String[] getArea(String area) throws IOException {
		File file = new File(areaLocal + "area.txt");
		BufferedReader areas = new BufferedReader(new FileReader(file));
		String line = areas.readLine();
		while (line != null && !line.contains(area)) {
			line = areas.readLine();
		}
		line = areas.readLine().strip();
		String provs[] = line.split(" ");
		areas.close();
		return provs;
	}
	public static String[] getRegion(String region) throws IOException {
		File file = new File(areaLocal + "region.txt");
		BufferedReader regions = new BufferedReader(new FileReader(file));
		String line = regions.readLine();
		while (line != null && !line.contains(region)) {
			line = regions.readLine();
		}
		regions.readLine();
		line = regions.readLine();
		String ret = "";
		while (line.contains("_area")) {
			ret += line.strip().substring(0,4) + " ";
			line = regions.readLine();
		}
		String[] r = ret.split(" ");
		regions.close();
		return r;
	}
	public static void replaceInFile(String f, String from, String to) {
		File file = new File(f);
		String oldContent = "";
		BufferedReader reader = null;
		FileWriter writer = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			String line = reader.readLine();
			while (line != null) {
				oldContent = oldContent + line + System.lineSeparator();
				line = reader.readLine();
			}
			String newContent = oldContent;
			newContent = newContent.replaceAll(from, to);
			writer = new FileWriter(file);
			writer.write(newContent);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	public static void addToFile(String f, String addition) {
		File file = new File(f);
		String oldContent = "";
		BufferedReader reader = null;
		FileWriter writer = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			String line = reader.readLine();
			while (line != null) {
				oldContent = oldContent + line + System.lineSeparator();
				line = reader.readLine();
			}
			String newContent = oldContent + addition;
			writer = new FileWriter(file);
			writer.write(newContent);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	public static String nameReverse(String n) {
		String ret = "aaaa";
		int num = Integer.parseInt(n);
		for (int x = 1; x < num; x++) {
			ret = nextName(ret);
		}
		return ret;
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
}