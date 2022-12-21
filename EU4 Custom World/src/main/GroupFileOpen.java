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
	 */
	
	// File Location Strings
	final static String modLocal	 = "C:\\Users\\johns\\OneDrive\\Documents\\Paradox Interactive\\"
									 + "Europa Universalis IV\\mod\\Dominons";
	final static String provLocal 	 = modLocal + "\\history\\provinces\\";
	final static String countryLocal = modLocal + "\\history\\countries\\";
	final static String areaLocal 	 = modLocal + "\\map\\";
	final static String commonLocal  = modLocal + "\\common\\";
	final static String localLocal   = modLocal + "\\localisation\\";
	// Utility Strings
	final static String tradegoods 	 = "cloth fish fur grain livestock naval_supplies salt wine wool copper iron ivory slaves "
			+ "chinaware spices tea cocoa sugar tobacco dyes silk tropical_wood glass paper gems coal cloves unknown";
	
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
		//tradeNodeCount();
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
		boolean found = false;
		while(line != null) {
			if (line.contains("_continent = {")) {
				System.out.print(line.substring(0,4) + ": ");
				t=0; p=0; m=0;
				line = cont.readLine();
				found = true;
				continue;
			}
			String provs[] = line.strip().split(" ");
			if (line.contains("}") && (t+p+m != 0) && found) {
				System.out.println(t + " " + p + " " + m + " " + (t+p+m));
				line = cont.readLine();
				found = false;
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
	public static void devSet() throws IOException {
		String[] files = new File(provLocal).list();
		for (String f : files) {
			File file = new File(provLocal + f);
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = reader.readLine();
			while (line != null && !line.contains("base_tax = ")) {
				line = reader.readLine();
			}
			if (line == null) { 
				continue;
			}
			
			int tax = Integer.parseInt(line.substring(11));
			line = reader.readLine();
			int pro = Integer.parseInt(line.substring(18));
			line = reader.readLine();
			int man = Integer.parseInt(line.substring(16));
			
			int temp = 0;
			if (man > tax) {
				temp = tax;
				tax = man;
				man = temp;
				temp = 0;
			}
			if (man > pro) {
				temp = pro;
				pro = man;
				man = temp;
				temp = 0;
			}
			replaceInFile(provLocal + f, "base_tax = .+", ("base_tax = " + tax));
			replaceInFile(provLocal + f, "base_production = .+", ("base_production = " + pro));
			replaceInFile(provLocal + f, "base_manpower = .+", ("base_manpower = " + man));
			reader.close();
		}
	}
	public static void randomDevRemoval() throws IOException {
		String[] provs = { "1" , "2" , "3" , "4" , "5" , "6" , "7" , "8" , "12" , "14" , "18" , "21" , "22" , "23" , "24" , "26" , "28" , "29" , "31" , "35" , "36" , "37" , "38" , "39" , "41"
				, "43" , "44" , "46" , "48" , "51" , "55" , "63" , "64" , "66" , "68" , "69" , "71" , "72" , "79" , "80" , "82" , "85" , "86" , "88" , "89" , "90" , "91" , "92" , "94" , "97"
				, "98" , "101" , "104" , "105" , "106" , "108" , "109" , "111" , "114" , "115" , "117" , "118" , "120" , "121" , "122" , "123" , "125" , "128" , "129" , "130" , "132" , "135" , "137" , "138" , "144"
				, "145" , "146" , "148" , "149" , "150" , "151" , "152" , "153" , "154" , "156" , "160" , "162" , "163" , "165" , "166" , "167" , "168" , "170" , "171" , "172" , "177" , "179" , "180" , "181" , "182"
				, "183" , "185" , "188" , "189" , "190" , "192" , "194" , "197" , "198" , "200" , "201" , "202" , "203" , "206" , "207" , "208" , "209" , "210" , "211" , "213" , "214" , "215" , "216" , "217" , "218"
				, "219" , "222" , "223" , "224" , "225" , "226" , "227" , "228" , "232" , "233" , "234" , "235" , "236" , "237" , "238" , "239" , "240" , "241" , "242" , "243" , "244" , "245" , "247" , "248" , "249"
				, "250" , "251" , "252" , "253" , "254" , "257" , "258" , "259" , "260" , "265" , "266" , "267" , "268" , "269" , "270" , "271" , "272" , "273" , "274" , "276" , "277" , "278" , "279" , "280" , "281"
				, "282" , "283" , "284" , "285" , "288" , "289" , "292" , "293" , "294" , "296" , "297" , "298" , "300" , "301" , "303" , "307" , "310" , "311" , "312" , "313" , "314" , "316" , "317" , "318" , "319"
				, "320" , "322" , "323" , "325" , "326" , "328" , "329" , "330" , "331" , "332" , "333" , "334" , "335" , "336" , "337" , "338" , "339" , "340" , "342" , "343" , "344" , "348" , "349" , "350" , "351"
				, "352" , "353" , "354" , "356" , "357" , "358" , "359" , "362" , "363" , "365" , "368" , "369" , "371" , "372" , "373" , "374" , "375" , "376" , "378" , "379" , "380" , "382" , "384" , "385" , "386"
				, "387" , "388" , "391" , "392" , "393" , "394" , "396" , "398" , "399" , "400" , "401" , "402" , "403" , "404" , "405" , "406" , "407" , "408" , "409" , "411" , "412" , "413" , "414" , "415" , "416"
				, "417" , "419" , "420" , "421" , "422" , "423" , "424" , "426" , "427" , "428" , "429" , "431" , "433" , "434" , "435" , "436" , "437" , "438" , "439" , "440" , "443" , "444" , "445" , "446" , "447"
				, "448" , "449" , "451" , "452" , "453" , "455" , "456" , "457" , "459" , "460" , "461" , "462" , "463" , "464" , "465" , "466" , "467" , "468" , "469" , "470" , "471" , "473" , "475" , "476" , "478"
				, "482" , "483" , "484" , "485" , "486" , "487" , "488" , "489" , "490" , "491" , "492" , "493" , "494" , "496" , "497" , "498" , "499" , "500" , "501" , "502" , "503" , "504" , "507" , "508" , "509"
				, "510" , "512" , "515" , "516" , "518" , "520" , "521" , "522" , "523" , "526" , "527" , "528" , "529" , "533" , "534" , "535" , "536" , "537" , "538" , "539" , "540" , "541" , "542" , "543" , "545"
				, "546" , "549" , "550" , "551" , "554" , "555" , "558" , "559" , "563" , "564" , "565" , "566" , "567" , "572" , "573" , "575" , "578" , "579" , "580" , "581" , "582" , "583" , "586" , "589" , "590"
				, "591" , "592" , "594" , "595" , "596" , "598" , "599" , "600" , "602" , "603" , "605" , "606" , "607" , "608" , "610" , "611" , "612" , "613" , "615" , "616" , "618" , "619" , "622" , "623" , "624"
				, "627" , "628" , "630" , "631" , "634" , "635" , "636" , "639" , "641" , "642" , "643" , "644" , "646" , "647" , "648" , "650" , "651" , "652" , "653" , "654" , "656" , "657" , "659" , "662" , "665"
				, "666" , "669" , "670" , "673" , "674" , "675" , "676" , "677" , "679" , "680" , "682" , "684" , "685" , "688" , "690" , "691" , "692" , "696" , "696" , "697" , "700" , "701" , "704" , "705" , "706"
				, "707" , "708" , "709" , "712" , "716" , "717" , "718" , "720" , "721" , "722" , "723" , "724" , "725" , "726" , "728" , "733" , "736" , "737" , "738" , "739" , "742" , "743" , "747" , "748" , "754"
				, "755" , "757" , "758" , "763" , "766" , "766" , "767" , "768" , "769" , "771" , "772" , "773" , "774" , "775" , "780" , "782" , "783" , "784" , "785" , "786" , "788" , "789" , "790" , "790" , "792"
				, "793" , "794" , "795" , "797" , "801" , "802" , "805" , "806" , "808" , "809" , "810" , "812" , "813" , "814" , "820" , "824" , "825" , "831" , "832" , "833" , "834" , "836" , "839" , "840" , "841"
				, "843" , "844" , "845" , "846" , "847" , "848" , "850" , "851" , "853" , "854" , "855" , "863" , "864" , "865" , "866" , "867" , "869" , "870" , "871" , "872" , "873" , "875" , "876" , "877" , "879"
				, "880" , "881" , "882" , "883" , "887" , "888" , "889" , "892" , "893" , "894" , "895" , "896" , "897" , "898" , "899" , "902" , "903" , "904" , "905" , "906" , "907" , "908" , "909" , "910" , "911"
				, "914" , "915" , "917" , "919" , "921" , "922" , "924" , "925" , "926" , "929" , "933" , "934" , "936" , "937" , "938" , "939" , "943" , "944" , "946" , "947" , "952" , "953" , "954" , "957" , "958"
				, "959" , "960" , "962" , "963" , "964" , "967" , "968" , "971" , "974" , "975" , "976" , "977" , "979" , "981" , "982" , "983" , "984" , "985" , "986" , "987" , "988" , "990" , "991" , "992" , "993"
				, "994" , "995" , "999" , "1000" , "1001" , "1004" , "1005" , "1008" , "1011" , "1013" , "1014" , "1017" , "1020" , "1021" , "1023" , "1024" , "1025" , "1027" , "1028" , "1031" , "1032" , "1033" , "1034" , "1035" , "1036"
				, "1037" , "1038" , "1039" , "1044" , "1045" , "1046" , "1047" , "1050" , "1056" , "1057" , "1058" , "1059" , "1060" , "1061" , "1062" , "1065" , "1066" , "1067" , "1075" , "1076" , "1077" , "1078" , "1080" , "1081" , "1084"
				, "1085" , "1086" , "1090" , "1091" , "1092" , "1093" , "1096" , "1097" , "1098" , "1102" , "1103" , "1104" , "1105" , "1109" , "1110" , "1111" , "1113" , "1114" , "1115" , "1118" , "1120" , "1122" , "1123" , "1126" , "1127"
				, "1128" , "1130" , "1131" , "1132" , "1133" , "1135" , "1136" , "1137" , "1138" , "1142" , "1143" , "1144" , "1145" , "1148" , "1150" , "1151" , "1152" , "1154" , "1155" , "1157" , "1158" , "1159" , "1164" , "1165" , "1166"
				, "1167" , "1168" , "1171" , "1172" , "1173" , "1174" , "1175" , "1180" , "1183" , "1184" , "1185" , "1186" , "1189" , "1190" , "1191" , "1192" , "1193" , "1194" , "1197" , "1198" , "1199" , "1202" , "1205" , "1206" , "1207"
				, "1211" , "1212" , "1213" , "1216" , "1219" , "1220" , "1221" , "1222" , "1224" , "1225" , "1226" , "1230" , "1231" , "1232" , "1234" , "1235" , "1236" , "1237" , "1238" , "1239" , "1240" , "1242" , "1244" , "1245" , "1246"
				, "1249" , "1250" , "1251" , "1253" , "1254" , "1258" , "1262" , "1263" , "1264" , "1265" , "1266" , "1267" , "1268" , "1271" , "1272" , "1276" , "1277" , "1278" , "1279" , "1281" , "1282" , "1284" , "1285" , "1286" , "1290"
				, "1291" , "1294" , "1295" , "1296" , "1299" , "1302" , "1303" , "1306" , "1307" , "1308" , "1309" , "1310" , "1311" , "1312" , "1314" , "1315" , "1316" , "1317" , "1320" , "1321" , "1323" , "1324" , "1325" , "1326" , "1327"
				, "1328" , "1331" , "1336" , "1337" , "1341" , "1342" , "1345" , "1346" , "1347" , "1348" , "1350" , "1351" , "1352" , "1353" , "1355" , "1356" , "1358" , "1359" , "1360" , "1361" , "1365" , "1366" , "1367" , "1368" , "1370"
				, "1372" , "1373" , "1382" , "1383" , "1386" , "1392" , "1393" , "1394" , "1395" , "1396" , "1402" , "1405" , "1406" , "1409" , "1412" , "1413" , "1414" , "1416" , "1417" , "1419" , "1421" , "1423" , "1426" , "1427" , "1428"
				, "1431" , "1432" , "1433" , "1435" , "1436" , "1437" , "1439" , "1440" , "1443" , "1446" , "1449" , "1456" , "1457" , "1460" , "1461" , "1463" , "1466" , "1469" , "1470" , "1471" , "1472" , "1473" , "1476" , "1477" , "1478"
				, "1480" , "1481" , "1483" , "1484" , "1487" , "1490" , "1491" , "1493" , "1495" , "1496" , "1497" , "1498" , "1501" , "1503" , "1507" , "1510" , "1511" , "1512" , "1518" , "1519" , "1520" , "1523" , "1524" , "1527" , "1528"
				, "1534" , "1535" , "1536" , "1537" , "1538" , "1539" , "1542" , "1543" , "1545" , "1546" , "1549" , "1550" , "1551" , "1552" , "1553" , "1556" , "1557" , "1559" , "1563" , "1570" , "1571" , "1579" , "1581" , "1582" , "1585"
				, "1589" , "1591" , "1592" , "1594" , "1595" , "1596" , "1597" , "1601" , "1602" , "1607" , "1608" , "1612" , "1618" , "1622" , "1625" , "1626" , "1627" , "1633" , "1634" , "1636" , "1644" , "1647" , "1650" , "1656" , "1657"
				, "1662" , "1663" , "1665" , "1669" , "1672" , "1676" , "1687" , "1690" , "1697" , "1699" , "1700" , "1701" , "1705" , "1708" , "1714" , "1717" , "1726" , "1727" , "1730" , "1734" , "1736" , "1739" , "1742" , "1746" , "1748"
				, "1756" , "1760" , "1763" , "1766" , "1767" , "1773" , "1776" , "1777" , "1780" , "1783" , "1785" , "1788" , "1797" , "1798" , "1803" , "1809" , "1811" , "1812" , "1817" , "1818" , "1823" , "1830" , "1833" , "1834" , "1835"
				, "1838" , "1839" , "1840" , "1844" , "1845" , "1846" , "1860" , "1863" , "1864" , "1866" , "1868" , "1872" , "1874" , "1877" , "1878" , "1882" , "1883" , "1884" , "1887" , "1889" , "1895" , "1902" , "1904" , "1907" , "1912"
				, "1913" , "1915" , "1923" , "1926" , "1927" , "1928" , "1931" , "1936" , "1947" , "1948" , "1950" , "1951" , "1952" , "1957" , "1968" , "1971" , "1972" , "1977" , "1978" , "1980" , "1981" , "1985" , "1989" , "1990" , "1992"
				, "1993" , "1994" , "1996" , "1997" , "1999" , "2001" , "2006" , "2008" , "2010" , "2011" , "2012" , "2016" , "2019" , "2020" , "2021" , "2023" , "2030" , "2039" , "2042" , "2044" , "2047" , "2048" , "2052" , "2060" , "2063"
				, "2064" , "2067" , "2070" , "2071" , "2077" , "2080" , "2083" , "2084" , "2085" , "2100" , "2101" , "2105" , "2106" , "2107" , "2109" , "2110" , "2114" , "2118" , "2120" , "2121" , "2122" , "2124" , "2130" , "2131" , "2132"
				, "2138" , "2142" , "2147" , "2148" , "2154" , "2160" , "2164" , "2165" , "2170" , "2179" , "2180" , "2181" , "2183" , "2186" , "2191" , "2202" , "2205" , "2212" , "2213" , "2214" , "2223" , "2231" , "2234" , "2235" , "2236"
				, "2240" , "2247" , "2249" , "2251" , "2264" , "2267" , "2271" , "2272" , "2276" , "2287" , "2289" , "2307" , "2308" , "2312" , "2317" , "2336" , "2342" , "2344" , "2345" , "2346" , "2353" , "2371" , "2374" , "2383" , "2396"
				, "2401" , "2405" , "2418" , "2426" , "2429" , "2431" , "2441" , "2452" , "2483" };
		int devTakeOff = 1815;
		Random rand = new Random();
		
		for (int x=0; x<devTakeOff; x++) {
			String randProv = provs[rand.nextInt(provs.length)];
			File file = new File(provLocal + randProv + " - pr" + nameReverse(randProv) + ".txt");
			BufferedReader reader = new BufferedReader(new FileReader(file));
			
			String line = reader.readLine();while (line != null && !line.contains("base_tax = ")) {
				line = reader.readLine();
			}
			if (line == null) { 
				x--;
				continue;
			}
			
			int tax = Integer.parseInt(line.substring(11));
			line = reader.readLine();
			int pro = Integer.parseInt(line.substring(18));
			line = reader.readLine();
			int man = Integer.parseInt(line.substring(16));
			reader.close();
			
			tax--;
			//pro--;
			//man--;
			
			dev(randProv, String.valueOf(tax), String.valueOf(pro), String.valueOf(man));
		}
	}
	public static void tradeNodeCount() throws IOException {
		File file = new File(commonLocal + "tradenodes\\00_tradenodes.txt");
		BufferedReader nodes = new BufferedReader(new FileReader(file));
		String line = nodes.readLine();
		int average = 0;
		int count = 0;
		int max = Integer.MIN_VALUE;
		int min = Integer.MAX_VALUE;
		while (line != null) {
			if (line.contains("members={")) {
				line = nodes.readLine();
				String[] provs = line.strip().split(" ");
				System.out.print(provs.length + " ");
				average += provs.length;
				count++;
				if (max < provs.length) { max = provs.length; }
				if (min > provs.length) { min = provs.length; }
			}
			line = nodes.readLine();
		}
		System.out.println("\nAvg: " + (average/count) + "\nMax: " + max + "\nMin: " + min);
		nodes.close();
	}
}