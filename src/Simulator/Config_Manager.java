package Simulator;
/*
 * CMSC-611 Advanced Computer Architecture - Final Project (Fall - 2019)
 * Author : Pranav B Ganore [YI73732]
 * pganore1@umbc.edu
*/

public class Config_Manager {

	public static final Config_Manager instance = new Config_Manager();

	private Config_Manager() {

	}

	public int FPAdderLatency = 0;
	public int FPMultLatency = 0;
	public int FPDivideLatency = 0;
	public boolean FPAdderPipelined = false;
	public boolean FPMultPipelined = false;
	public boolean FPDividerPipelined = false;
	public int MemoryLatency = 0;
	public int ICacheLatency = 0;
	public int DCacheLatency = 0;

}
