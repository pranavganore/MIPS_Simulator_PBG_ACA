package Simulator;
/*
 * CMSC-611 Advanced Computer Architecture - Final Project (Fall - 2019)
 * Author : Pranav B Ganore [YI73732]
 * pganore1@umbc.edu
*/

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.TreeMap;

import caching.D_Cache_Manager;
import caching.I_Cache_Manager;
import instructions.DI;
import stages.Stage_Type;

public class Result_Generator {

	public static final Result_Generator instance = new Result_Generator();

	private final TreeMap<Integer, DI> instructionMap = new TreeMap<Integer, DI>();

	private BufferedWriter resultsWriter = null;

	private boolean HALT = false;
	private boolean secondHALT = false;

	public static final String instructionOutputFormatString = " %-25s  %-4s  %-4s  %-4s  %-4s  %-3s  %-3s  %-3s  %-6s ";

	public static final String instructionDebugFormatString = "%-20s %-10s %-10s %-3s %-3s %-3s %-3s";

	private Result_Generator() {
	}

	public void setResultsPath(String path) throws IOException {
		if (resultsWriter != null)
			return;
		resultsWriter = new BufferedWriter(new FileWriter(new File(path)));
	}

	public void printResults() {

		System.out.println(String.format(instructionOutputFormatString,
				"Instruction", "FT", "ID", "EX", "WB", "RAW", "WAR", "WAW",
				"Struct"));
		for (int key : instructionMap.keySet()) {
			DI inst = instructionMap.get(key);
//			 System.out.format("%-3s ", key);
			// System.out.println(inst.debugString());
			System.out.println(inst.getOutputString().toUpperCase());
		}
		System.out.println();
		System.out.println(I_Cache_Manager.getInstance().getICacheStatistics());
		System.out.println(D_Cache_Manager.instance.getDCacheStatistics());
	}

	public void writeResults() {
		try {
			resultsWriter.write(String.format(instructionOutputFormatString,
					"Instruction", "FT", "ID", "EX", "WB", "RAW", "WAR", "WAW",
					"Struct"));
			resultsWriter.newLine();
			for (int key : instructionMap.keySet()) {
				DI inst = instructionMap.get(key);
				resultsWriter.write(inst.getOutputString());
				resultsWriter.newLine();
			}
			resultsWriter.newLine();
			resultsWriter.newLine();
			resultsWriter.write(I_Cache_Manager.getInstance()
					.getICacheStatistics());
			resultsWriter.newLine();
			resultsWriter.write(D_Cache_Manager.instance.getDCacheStatistics());
			resultsWriter.newLine();
			resultsWriter.flush();
			resultsWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void queueInstructionForDisplay(DI instruction) {
		int key = instruction.entryCycle[Stage_Type.IFSTAGE.getId()];
		instructionMap.put(key, instruction);

	}

	public boolean isHALT() {
		
		return HALT;
	}

	public void setHALT(boolean halt) {
		this.HALT = halt;
	}
	
	public boolean isSecondHALT() {
		return secondHALT;
	}

	public void setSecondHALT(boolean secondhalt) {
		this.secondHALT = secondhalt;
	}
}
