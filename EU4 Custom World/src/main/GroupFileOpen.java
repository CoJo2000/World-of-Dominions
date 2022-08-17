package main;

import java.awt.Desktop;
import java.io.*;
import java.util.Random;

public class GroupFileOpen {
	/*
	 * TODO List
	 * Dev area: Dev command to randomly generate development of a given area, with given average for area (possible avg range)
	 * Culture creation: Command to make a culture in the necessary files 
	 * Culture change: Command to make a province a culture, same as core/multi-core
	 * Religion creation:Command to make a religion in the necessary files
	 * Religion change: Command to make a province a religion, same as core/multi-core
	 * Linux Style CMD: Change the core/dev/culture/religion/area/region commands to accept "-" commands to streamline code
	 * 		Ex:
	 * 		Core 2001 A01 -l | -l to indicate list coming, keep out of main call to streamline
	 * 			2002 2003 2004 -l
	 * 			2005 2006 | lack of -l, switch command
	 * 		area aaaa and pra aaaa vs area aaaa -o and area aaaa -p | Viseversa with Region
	 * 
	 *   4781 4914 4768 14463
	 *  - 950  950 2100  4000
	 *  =3931 3964 2668 10463
	 */
	
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
	
	/*
	 * Main
	 * core 2001 A01 | 2002 2003
	 * area aaaa | region aaaa
	 * tag A01 Eldahum 2001
	 * dev 2001 3 5 4 | deva aaaa 20 5
	 * tg 2001 grain
	 * pra aaaa | prr aaaa
	 * cul 12 elder | rel 12 cult
	 */
	public static void main(String[] args) throws IOException {
		devCount();
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		System.out.print("CMD: ");
		String input = reader.readLine();
		String lastTag = "";
		// Command Input While Loop
		while (!input.equals("exit")) {
			String[] inputs = input.split(" ");
			switch (inputs[0]) {
				// Core CMD: changes a single province's owner/core to given tag
				case "core": {
					// core prov tag : core 2001 A01
					System.out.println("CMD: CORE prov");
					core(inputs[1], inputs[2]);
					lastTag = inputs[2];
					break;
				}
				// Area CMD: Opens the province files of a given area
				case "area": {
					// area area : area aaaa
					openArea(inputs[1]);
					break;
				}
				// Region CMD: Opens the province files of a given region
				case "region": {
					// region region : region aaaa
					System.out.println("CMD: OPEN region");
					openRegion(inputs[1]);
					break;
				}
				// Tag CMD: Creates a new tag with the given tag, name, and capital province to which it cores
				case "tag": {
					// tag tag name capital : tag A01 Eldahum 2001
					System.out.println("CMD: TAG nation");
					tag(inputs[1], inputs[2], inputs[3]);
					lastTag = inputs[1];
					break;
				}
				// Dev CMD: Sets the given province with the development of three given values 
				case "dev": {
					// dev prov tax production manpower : dev 2001 5 5 5
					System.out.println("CMD: DEV " + (Integer.parseInt(inputs[2]) + Integer.parseInt(inputs[3]) + Integer.parseInt(inputs[4])));
					dev(inputs[1], inputs[2], inputs[3], inputs[4]);
					break;
				}
				// DevA CMD: Sets the given area with random development of a given average value
				case "deva": {
					// deva area average range : deva aaaa 20 1 | Provinces can be 19 20 21 dev total
					System.out.println("CMD: DEV");
					deva(inputs[1], inputs[2], inputs[3]);
					break;
				}
				// DevR CMD: Sets the given region with random development of a given average value
				case "devr": {
					// deva area average range : deva aaaa 20 1 | Provinces can be 19 20 21 dev total
					System.out.println("CMD: DEV");
					devr(inputs[1], inputs[2], inputs[3]);
					break;
				}
				
				// Tg CMD: Sets the given province with the given tradegood
				case "tg": {
					// tg prov good : tg 2002 grain
					System.out.println("CMD: TRADEGOOD prov");
					tradegood(inputs[1], inputs[2]);
					break;
				}
				// Pra CMD: Prints out the province numbers of a given area
				case "pra": {
					// pra area : pra aaaa
					System.out.println("CMD: PRINT prov");
					System.out.print("CMD: PROV = ");
					printArea(inputs[1]);
					System.out.println();
					break;
				}
				// Prr CMD: Prints out the province numbers of a given region
				case "prr": {
					// prr region : prr aaaa
					System.out.println("CMD: PRINT region");
					System.out.print("CMD: PROV = ");
					printRegion(inputs[1]);
					System.out.println();
					break;
				}
				// Cul CMD: Sets the given provinces with the given culture
				case "cul": {
					// cul prov culture : cul 2002 Elder
					System.out.println("CMD: CUL prov");
					if (inputs.length > 3) {
						for (int x=3; x<inputs.length; x++) {
							culture(inputs[x],inputs[2]);
						}
					}
					culture(inputs[1],inputs[2]);
					break;
				}
				// Rel CMD: Sets the given provinces with the given religion
				case "rel": {
					// cul prov religion : cul 2002 cult_of_elder
					System.out.println("CMD: REL prov");
					if (inputs.length > 3) {
						for (int x=3; x<inputs.length; x++) {
							religion(inputs[x],inputs[2]);
						}
					}
					religion(inputs[1],inputs[2]);
					break;
				}
				// NationSet CMD: Goes through each nation and sets their culture and religion to that of their capital's
				case "nationSet": {
					// nationSet
					System.out.println("CMD: NATIONSET");
					nationSet();
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
		/**
		 * Changes a province's core/owner to the given tag's
		 * @param prov String representing the Province ID to change
		 * @param tag String representing the new core/owner 
		 * @throws IOException
		 */
		String name = nameReverse(prov);
		// ReplaceInFile calls
		replaceInFile(provLocal + prov + " - pr" + name + ".txt", "owner = ...", ("owner = " + tag));
		replaceInFile(provLocal + prov + " - pr" + name + ".txt", "controller = ...", ("controller = " + tag));
		replaceInFile(provLocal + prov + " - pr" + name + ".txt", "add_core = ...", ("add_core = " + tag));
	}
	public static void openArea(String area) throws IOException {
		/**
		 * Opens the Province Files of a given Area
		 * @param area String representing the area to open
		 * @throws IOException
		 */
		// Gets Province IDs within the Area
		String provs[] = getArea(area);
		System.out.println("CMD: AREA " + area);
		// Opens each File
		for (String prov : provs) {
			File file = new File(provLocal + prov + " - pr" + nameReverse(prov) + ".txt");
			Desktop.getDesktop().open(file);
		}
	}
	public static void openRegion(String region) throws IOException {
		/**
		 * Opens the Province Files of a given Region
		 * @param region String representing the region to open
		 * @throws IOException
		 */
		// Gets Areas within the Region
		String[] areas = getRegion(region);
		// Opens each Area individually
		for (String area : areas) {
			openArea(area);
		}
	}
	public static void tag(String tag, String name, String capital) throws IOException {
		/**
		 * Creates a new Tag with all connecting files and file changes
		 * @param tag String three char representing the Variable name in game
		 * @param name String representing the nation's name, "_" for multi word's
		 * @param capital String representing the province ID
		 * @throws IOException
		 */
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
		/**
		 * Replaces the Development of a province with new development
		 * @param prov String representing the province ID
		 * @param t String representing Trade
		 * @param p String representing Production
		 * @param m String representing Manpower
		 */
		// Single call for name of province
		String name = nameReverse(prov);
		// ReplaceInFile call for each development segment
		replaceInFile(provLocal + prov + " - pr" + name + ".txt", "base_tax = .+", ("base_tax = " + t));
		replaceInFile(provLocal + prov + " - pr" + name + ".txt", "base_production = .+", ("base_production = " + p));
		replaceInFile(provLocal + prov + " - pr" + name + ".txt", "base_manpower = .+", ("base_manpower = " + m));
	}
	public static void deva(String area, String a, String r) throws IOException {
		/**
		 * Replaces the Development of an area with random range of development
		 * @param area String representing the Area
		 * @param a String representing the average of the province
		 * @param r String representing the range of value off the average, add and subtract
		 * @throws IOException
		 */
		int average = Integer.parseInt(a);
		int range = Integer.parseInt(r);
		int avg = 0;
		String provs[] = getArea(area);
		Random rand = new Random();
		
		for (String prov : provs) {
			int t = (int) Math.round(average * 0.37) + 1;
			int p = (int) Math.round(average * 0.37) + 1;
			int m = (int) Math.round(average * 0.25);
			
			for ( int x=range; x>0; x--) {
				int c = rand.nextInt(2);
				if (c==0) {
					t = t + rand.nextInt(3)-2;
				}
				if (c==1) {
					p = p + rand.nextInt(3)-2;
				}
				if (c==2) {
					m = m + rand.nextInt(3)-2;
				}
			}
			if (t<=1) { t = 1; }
			if (p<=1) { p = 1; }
			if (m<=1) { m = 1; }
			avg += t+p+m;
			System.out.println(prov + ": " + t + " " + p + " "  + m + " | " + (t+p+m));
			dev(prov, String.valueOf(t), String.valueOf(p), String.valueOf(p));
		}
		System.out.println("Avg: " + avg/provs.length);
	}
	public static void devr(String region, String a, String r) throws IOException {
		/**
		 * Replaces the Development of a region with random range of development
		 * @param region String representing the Region
		 * @param a String representing the average of the provinces
		 * @param r String Representing the range of value off the average, add and subtract
		 * @throws IOException
		 */
		String areas[] = getRegion(region);
		for (String area : areas) {
			deva(area, a, r);
		}
	}
	public static void tradegood(String prov, String good) {
		/**
		 * Replaces the Tradegood of a given province with a given good
		 * @param prov String representing the province to change
		 * @param good String representing the tradegood to be, must be a valid tradegood.
		 */
		good = good.toLowerCase();
		// Valid Tradegood Check
		if (!tradegoods.contains(good)) {
			System.out.println("CMD: UNKNOWN tradegood");
			return;
		}
		// ReplaceInFile call
		replaceInFile(provLocal + prov + " - pr" + nameReverse(prov) + ".txt", "trade_goods = .+", "trade_goods = " + good);
	}
	public static void printArea(String area) throws IOException {
		/**
		 * Prints the Area ID Values of the given Area to the User
		 * @param area String representing the area to print off
		 * @throws IOException
		 */
		// Collect Area Province ID Numbers 
		String[] provs = getArea(area);
		for (String prov : provs) { 
			// Print off each number, Does Not print New Line
			System.out.print(prov + " ");
		}
	}
	public static void printRegion(String region) throws IOException {
		/**
		 * Prints the Province ID Values of the given Region to the User
		 * @param region String representing the region to print off
		 * @throws IOException
		 */
		// Collect Area Names within region
		String[] areas = getRegion(region);
		for (String area : areas) {
			// Calls Print Area to print individual Areas and their Province IDs
			printArea(area);
		}
	}
	public static void culture(String prov, String culture) throws IOException {
		/**
		 * Replaces the Culture of a province with the new culture
		 * @param prov String representing the Province ID
		 * @param culture String representing the new culture
		 * @throws IOException
		 */
		replaceInFile(provLocal + prov + " - pr" + nameReverse(prov) + ".txt", "culture = .+", ("culture = " + culture));
	}
	public static void religion(String prov, String religion) throws IOException {
		/**
		 * Replaces the Religion of a province with the new religion
		 * @param prov String representing the Province ID
		 * @param religion String representing the new religion
		 * @throws IOException
		 */
		replaceInFile(provLocal + prov + " - pr" + nameReverse(prov) + ".txt", "religion = .+", ("religion = " + religion));
	}
	public static void nationSet() throws IOException {
		/**
		 * goes through each nation and changes their main culture and religion to that of the capital's
		 * @throws IOException
		 */
		String[] files = new File(countryLocal).list();
		for (int x= 0; x<files.length-3; x++) {
			File file = new File(countryLocal + files[x]);
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = reader.readLine();
			while (line != null && !line.contains("capital = ")) {
				line = reader.readLine();
			}
			reader.close();
			line = line.substring(10);
			
			file = new File(provLocal + line + " - pr" + nameReverse(line) + ".txt");
			reader = new BufferedReader(new FileReader(file));
			line = reader.readLine();
			String cul = "";
			String rel = "";
			while (line != null) {
				if (line.contains("religion = ")) {
					rel = line.substring(11);
				}
				if (line.contains("culture = ")) {
					cul = line.substring(10);
				}
				
				line = reader.readLine();
			}
			replaceInFile(countryLocal + files[x], "religion = .+", "religion = " + rel);
			replaceInFile(countryLocal + files[x], "culture = .+", "culture = " + cul);
		}
	}
	
	// Utility Functions
	public static String[] getArea(String area) throws IOException {
		/**
		 * Collects all the Province ID Numbers of a given area and returns them in an Array
		 * @param area String representing the Area to retrieve
		 * @return provs String[] holding all the Province ID Numbers within the given area as a String
		 * @throws IOException
		 */
		File file = new File(areaLocal + "area.txt");
		BufferedReader areas = new BufferedReader(new FileReader(file));
		String line = areas.readLine();
		while (line != null && !line.contains(area + "_area")) {
			line = areas.readLine();
		}
		line = areas.readLine().strip();
		String provs[] = line.split(" ");
		areas.close();
		return provs;
	}
	public static String[] getRegion(String region) throws IOException {
		/**
		 * Collects all the Area Names of a given region and returns them in an Array
		 * @param region String representing the Region to retrieve
		 * @return r String[] holding all the Areas within the given Region
		 * @throws IOException
		 */
		// Variable Declarations
		File file = new File(areaLocal + "region.txt");
		BufferedReader regions = new BufferedReader(new FileReader(file));
		// Iterates through region.txt file to find the desired region
		String line = regions.readLine();
		while (line != null && !line.contains(region + "_region")) {
			line = regions.readLine();
		}
		regions.readLine();
		line = regions.readLine();
		String ret = "";
		// Adds all Area Names to the return String/Array
		while (line.contains("_area")) {
			ret += line.strip().substring(0,4) + " ";
			line = regions.readLine();
		}
		String[] r = ret.split(" ");
		regions.close();
		return r;
	}
	public static void replaceInFile(String f, String from, String to) {
		/**
		 * Replaces a given String/Regex with a given String/Regex in a given File
		 * @param f String representing file path
		 * @param from String representing String/Regex to be replaced
		 * @param to String representing String/Regex to place in
		 */
		// Variable Declarations
		File file = new File(f);
		String oldContent = "";
		BufferedReader reader = null;
		FileWriter writer = null;
		try {
			// Copy Contents of the File
			reader = new BufferedReader(new FileReader(file));
			String line = reader.readLine();
			while (line != null) {
				oldContent = oldContent + line + System.lineSeparator();
				line = reader.readLine();
			}
			String newContent = oldContent;
			// Replace old with new
			newContent = newContent.replaceAll(from, to);
			// Rewrite file with new content
			writer = new FileWriter(file);
			writer.write(newContent);
		} catch (IOException e) {
			// Unknown FIle Catch
			e.printStackTrace();
		} finally {
			// Close Reader and Writer
			try {
				reader.close();
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	public static void addToFile(String f, String addition) {
		/**
		 * Adds a given String to the end of a given File
		 * @param f String representing file path
		 * @param addition String to be added to the end of a file
		 */
		// Variable Declarations
		File file = new File(f);
		String oldContent = "";
		BufferedReader reader = null;
		FileWriter writer = null;
		try {
			// Copy Contents of the File
			reader = new BufferedReader(new FileReader(file));
			String line = reader.readLine();
			while (line != null) {
				oldContent = oldContent + line + System.lineSeparator();
				line = reader.readLine();
			}
			// Add new Content
			String newContent = oldContent + addition;
			// Writes over File with copied content and addition
			writer = new FileWriter(file);
			writer.write(newContent);
		} catch (IOException e) {
			// Unknown File Catch
			e.printStackTrace();
		} finally {
			// Close Reader and Writer
			try {
				reader.close();
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	public static String nameReverse(String n) {
	/**
	 * Increments a simple four letter name by the given number
	 * @param n String representing a number
	 * @return ret String representing the province from the number
	 */
		String ret = "aaaa";
		int num = Integer.parseInt(n);
		for (int x = 1; x < num; x++) {
			ret = nextName(ret);
		}
		return ret;
	}
	public static String nextName(String name) {
		/**
		 * Increments a simple four letter name by 1, increment by alphabetical order 
		 * @param name String to change
		 * @return name String modified name
		 */
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

	// Other Fuctions
	public static void devCount() throws IOException {
		File file = new File(areaLocal + "continent.txt");
		BufferedReader cont = new BufferedReader(new FileReader(file));
		String line = cont.readLine();
		int t = 0;
		int p = 0;
		int m = 0;
		while(line != null) {
			if (line.contains("_continent = {")) {
				System.out.print(line.substring(0,4) + ": ");
				t=0; p=0; m=0;
				line = cont.readLine();
				continue;
			}
			String provs[] = line.strip().split(" ");
			if (line.contains("}") && (t+p+m != 0)) {
				System.out.println(t + " " + p + " " + m + " " + (t+p+m));
				line = cont.readLine();
				continue;
			}
			
			for (String prov : provs ) {
				try {
					file = new File(provLocal + prov + " - pr" + nameReverse(prov) + ".txt");
				} catch(NumberFormatException e) {
					break;
				}
				
				BufferedReader pr = null;
				try {
					pr = new BufferedReader(new FileReader(file));
				} catch (FileNotFoundException e) {
					continue;
				}
				pr.readLine(); pr.readLine(); pr.readLine(); pr.readLine(); pr.readLine(); 
				pr.readLine(); pr.readLine();
				
				String curLine = pr.readLine(); if (curLine == null) { continue; }
				curLine = curLine.replace("base_tax = ", " ");
				t += Integer.parseInt(curLine.strip());
				
				curLine = pr.readLine();
				curLine = curLine.replace("base_production = ", " ");
				p += Integer.parseInt(curLine.strip());
				
				curLine = pr.readLine();
				curLine = curLine.replace("base_manpower = ", " ");
				m += Integer.parseInt(curLine.strip());
				
				pr.close();
			}
			
			line = cont.readLine();
		}
		cont.close();
	}
}