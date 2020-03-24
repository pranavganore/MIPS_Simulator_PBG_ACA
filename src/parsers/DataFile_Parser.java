package parsers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import Simulator.DataMemory_Manager;

// NOTE I break out of parsing after encountering the first empty line or file finishes
public class DataFile_Parser {
	public static void parse(String fileName) throws Exception {
		BufferedReader bfread = null;
		try {

			bfread = new BufferedReader(new FileReader(new File(fileName)));

			String line = null;
			int count = 0;
			int initialAddress = 0x100;

			while ((line = bfread.readLine()) != null) {
				line = line.trim();
				if (line.length() == 0)
					break; // break on the first empty line
				int value = Integer.parseInt(line, 2);

				DataMemory_Manager.instance.setValueToAddress(initialAddress++,
						value);

				count++;
			}
			System.out.println("Total Number of memory locations = " + count);
		} finally {
			if (bfread != null)
				bfread.close();
		}

	}
}
