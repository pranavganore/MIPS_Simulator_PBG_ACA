package parsers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import Simulator.Config_Manager;

public class ConfigFile_Parser {

	public static void parse(String fileName) throws Exception {
		BufferedReader bfread = null;
		try {

			bfread = new BufferedReader(new FileReader(new File(fileName)));

			String line = null;
			int count = 0;

			while ((line = bfread.readLine()) != null) {
				line = line.trim();
				if (line.length() == 0)
					continue;
				count++;

				try {
					parseConfigurationLine(line);
				} catch (Exception e) {
					throw new Exception("Error in Config File line: " + line);
				}
			}
			System.out.println("Total Number of Config Elements = " + count);
		} finally {
			if (bfread != null)
				bfread.close();
		}

	}

	private static void parseConfigurationLine(String line) {
		String s[], s1[];
		line = line.trim();
		line = line.toLowerCase();

		s = line.split(":");

		String configItem = s[0].trim();

		switch (configItem) {
		case "fp adder":
			s1 = s[1].split(",");
			Config_Manager.instance.FPAdderLatency = Integer.parseInt(s1[0]
					.trim());
			Config_Manager.instance.FPAdderPipelined = s1[1].trim()
					.toLowerCase().equals("yes");
			break;

		case "fp multiplier":
			s1 = s[1].split(",");
			Config_Manager.instance.FPMultLatency = Integer.parseInt(s1[0]
					.trim());
			Config_Manager.instance.FPMultPipelined = s1[1].trim().toLowerCase()
					.equals("yes");
			break;

		case "fp divider":
			s1 = s[1].split(",");
			Config_Manager.instance.FPDivideLatency = Integer.parseInt(s1[0]
					.trim());
			Config_Manager.instance.FPDividerPipelined = s1[1].trim()
					.toLowerCase().equals("yes");
			break;

		case "main memory":
			Config_Manager.instance.MemoryLatency = Integer
					.parseInt(s[1].trim());
			break;

		case "i-cache":
			Config_Manager.instance.ICacheLatency = Integer
					.parseInt(s[1].trim());
			break;

		case "d-cache":
			Config_Manager.instance.DCacheLatency = Integer
					.parseInt(s[1].trim());
			break;
		}

	}
}