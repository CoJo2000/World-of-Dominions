package main;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class GroupFileOpen {
	final static String provLocal = "C:\\Users\\johns\\OneDrive\\Documents\\Paradox Interactive\\Europa Universalis IV\\mod\\Dominons\\history\\provinces\\";
	final static String areaLocal = "C:\\Users\\johns\\OneDrive\\Documents\\Paradox Interactive\\Europa Universalis IV\\mod\\Dominons\\map\\";
	final static String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

	public static void main(String[] args) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		System.out.print("CMD: ");
		String input = reader.readLine();
		String lastTag = "";
		while (!input.equals("exit")) {
			String[] inputs = input.split(" ");
			switch (inputs[0]) {
			case "core": {
				// core prov tag
				// core 2001 A01
				System.out.println("CMD: CORE prov");
				core(inputs[1], inputs[2]);
				lastTag = inputs[2];
				break;
			}
			case "area": {
				// area area
				// area aaaa
				openArea(inputs[1]);
				break;
			}
			case "region": {
				// region region
				// region aaaa
				System.out.println("CMD: OPEN region");
				openRegion(inputs[1]);
				break;
			}
			default: {
				if (lastTag != "") {
					int num;
					try {
						num = Integer.parseInt(inputs[0]);
						System.out.println("CMD: CORE prov");
						core(inputs[0], lastTag);
					} catch (NumberFormatException e) {
						num = -1;
						System.out.println(num);
					}
				} else {
					System.out.println("CMD: UNKNOWN Command");
				}
			}
			}

			System.out.print("CMD: ");
			input = reader.readLine();
		}
		System.out.println("CMD: EXIT Command");
	}

	public static void core(String prov, String tag) throws IOException {
		File file = new File(provLocal + prov + " - pr" + nameReverse(prov) + ".txt");
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
			String newContent = oldContent.replaceAll("USA", tag);
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
		//Desktop.getDesktop().open(file);
	}

	public static void openArea(String area) throws IOException {
		File file = new File(areaLocal + "area.txt");
		BufferedReader areas = new BufferedReader(new FileReader(file));
		String line = areas.readLine();
		while (line != null && !line.contains(area)) {
			line = areas.readLine();
		}
		line = areas.readLine().strip();
		String provs[] = line.split(" ");
		System.out.println("CMD: AREA " + area);
		for (String prov : provs) {
			file = new File(provLocal + prov + " - pr" + nameReverse(prov) + ".txt");
			Desktop.getDesktop().open(file);
		}
		areas.close();
	}

	public static void openRegion(String region) throws IOException {
		File file = new File(areaLocal + "region.txt");
		BufferedReader regions = new BufferedReader(new FileReader(file));
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		String line = regions.readLine();
		while (line != null && !line.contains(region)) {
			line = regions.readLine();
		}
		regions.readLine();
		line = regions.readLine();
		while (line.contains("_area")) {
			openArea(line.strip().substring(0, 4));
			line = regions.readLine();
		}
		regions.close();
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
