package caching;
/*
 * CMSC-611 Advanced Computer Architecture - Final Project (Fall - 2019)
 * Author : Pranav B Ganore [YI73732]
 * pganore1@umbc.edu
*/

import Simulator.Config_Manager;
import Simulator.Program_Manager;
import Simulator.Result_Generator;
import instructions.DI;
import stages.Processor_Params;

/**
 * Called by main while fetching instructions if instructions not present in
 * cache keep track of when to populate the instruction in the cache
 * 
 * 
 * 
 * Instruction isInstructionInCache(int pc){ if instruction is not found in the
 * cache track of when to populate the instruction in the cache by marking it in
 * the tracker return null; else return instruction from cache
 * 
 * }
 */

public class I_Cache_Manager {

	int lastRequestInstruction;
	int lastRequestCycle;
	int clockCyclesToBlock;
	int delayToBus;
	boolean cacheHit;
	private int iCacheAccessRequests;
	private int iCacheAccessHits;

	private static volatile I_Cache_Manager instance;

	public static I_Cache_Manager getInstance() {
		if (null == instance)
			synchronized (I_Cache_Manager.class) {
				if (null == instance)
					instance = new I_Cache_Manager();
			}

		return instance;
	}

	private I_Cache_Manager() {

		resetValues();

	}

	private void resetValues() {

		lastRequestInstruction = -1;
		lastRequestCycle = -1;
		clockCyclesToBlock = -1;
		delayToBus = -1;
		cacheHit = false;

	}

	public DI getInstructionFromCache(int pc) throws Exception {

		if (lastRequestInstruction == -1) {
			if (Result_Generator.instance.isHALT() == false)
				iCacheAccessRequests++;
			if (I_Cache.getInstance().isInstructionInCache(pc)) {

				lastRequestInstruction = pc;
				lastRequestCycle = Processor_Params.CC;
				clockCyclesToBlock = Config_Manager.instance.ICacheLatency - 1;
				cacheHit = true;
				if (Result_Generator.instance.isHALT() == false)
					iCacheAccessHits++;

				if (clockCyclesToBlock == 0) {

					resetValues();
					MemoryBus_Manager.instance.releaseMemoryBus();
					return Program_Manager.instance.getInstructionAtAddress(pc);

				} else
					return null;

			} else {

				lastRequestInstruction = pc;
				lastRequestCycle = Processor_Params.CC;
				delayToBus = MemoryBus_Manager.instance.getDelay();
				clockCyclesToBlock = 2
						* (Config_Manager.instance.ICacheLatency + Config_Manager.instance.MemoryLatency)
						+ delayToBus - 1;

				MemoryBus_Manager.instance.setDelay(clockCyclesToBlock - 1);	//Block Memory bus for these many clock cycles
				return null;

			}

		} else {

			if (Processor_Params.CC - lastRequestCycle == clockCyclesToBlock) {
				if (!cacheHit)
					I_Cache.getInstance().populateICache(pc);
				resetValues();
				return Program_Manager.instance.getInstructionAtAddress(pc);

			} else
				return null;

		}

	}

	public String getICacheStatistics() {
		String format = "%-60s %4s";
		StringBuilder sb = new StringBuilder();
		sb.append(String.format(format,
				"Total number of access requests for instruction cache:",
				getICacheAccessRequests()));
		sb.append('\n');
		sb.append(String.format(format, "Number of instruction cache hits:",
				getICacheAccessHits()));
		return sb.toString();
	}

	public int getICacheAccessRequests() {
		return iCacheAccessRequests;
	}

	public int getICacheAccessHits() {
		return iCacheAccessHits;
	}
}
