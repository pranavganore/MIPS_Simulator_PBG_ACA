package caching;
/*
 * CMSC-611 Advanced Computer Architecture - Final Project (Fall - 2019)
 * Author : Pranav B Ganore [YI73732]
 * pganore1@umbc.edu
*/

import Simulator.Config_Manager;
import instructions.DI;
import stages.Processor_Params;

public class D_Cache_Manager {

	private D_Cache cache;
	private DCacheRequestData request;
	// private DCacheRequestData request;

	private int dCacheAccessRequests;
	private int dCacheAccessHits;
	public static final D_Cache_Manager instance = new D_Cache_Manager();

	private D_Cache_Manager() {
		cache = new D_Cache();
		request = new DCacheRequestData();
		dCacheAccessRequests = 0;
		dCacheAccessHits = 0;
	}

	public boolean canProceed(DI inst) throws Exception {
		int address = (int) inst.address;
		
		
		
		int flag = 0; int flag2 = 0; int flag3 = 0;  int flag4 = 0;
		if (inst.addressDouble > 1) 
		{
			flag = 1; 
			flag2 = 2;
			
			
			boolean addressdoesExist = cache.doesAddressExist((int) inst.address);
			boolean addressDoubledoesExist = cache.doesAddressExist((int) inst.addressDouble);
			//i.e. if both the words exists in cache 
			//then fetch them in any sequence dosent matter
			if (!addressdoesExist) {	//but if first word address does not exist then go with it
				address = (int) inst.address;
			}else if (!addressDoubledoesExist) {
				address = (int) inst.addressDouble;
				flag4 = 1;
			}
		}
		
		while ((flag2 != 0 && flag == 1) || (flag == 0 && flag2 == 0)) {
			
			if (request.lastRequestInstruction == null ) {
				// first time request
				if (flag == 0 || flag2 == 2) {
					request.lastRequestInstruction = inst;
					request.lastRequestInstructionEntryClock = Processor_Params.CC;
					request.clockCyclesToBlock = 0;
				}				 
				
				if (DI.isStore(inst)) {

					// check if address of this instruction actually exists in
					// DCache
					// if it does, then just write to DCache & mark it dirty. --> it
					// is a cache hit

					dCacheAccessRequests++;
					if (cache.doesAddressExist(address))
						{
							request.clockCyclesToBlock += Config_Manager.instance.DCacheLatency;
							dCacheAccessHits++;
						}
						
					else {
						// if address doesnt exist in DCache, then check if a block
						// is
						// free in Dcache
						// if free, then just write to Dcache and mark it dirty
						if (cache.isThereAFreeBlock(address)) {
//							request.clockCyclesToBlock += ConfigManager.instance.DCacheLatency;
							// Is there supposed to be some logic pending here ?
							request.clockCyclesToBlock += MemoryBus_Manager.instance
									.getDelay();
//							if (MemoryBusManager.instance.getDelay() > 0) {
//								inst.STRUCT = true;
//							}
							request.clockCyclesToBlock += 2 * (Config_Manager.instance.DCacheLatency + Config_Manager.instance.MemoryLatency);
							
						} else {
							// if doesnt exist && lru block is dirty, then writeback
							// to
							// memory , then update dcache & mark dirty
							// if lru block is not dirty, then just write to dcache
							if (cache.isLRUBlockDirty(address)) {
								request.clockCyclesToBlock += MemoryBus_Manager.instance
										.getDelay();
//								if (MemoryBusManager.instance.getDelay() > 0) {
//									inst.STRUCT = true;
//								}
								request.clockCyclesToBlock += 2 * (Config_Manager.instance.DCacheLatency + Config_Manager.instance.MemoryLatency);
							} else {
								request.clockCyclesToBlock += Config_Manager.instance.DCacheLatency;
							}
						}
						if (inst.addressDouble > 1) {
							request.clockCyclesToBlock += 1;	//if its a SD instruction it will take one extra clock cycle to store 2nd word
						}

					}
				} else {		// if the instruction is just the load instruction and not the store instruction
					// Cache hit and found the same address - > return the value
					// from cache
					// latency is cache access time
					dCacheAccessRequests++;
					if (cache.doesAddressExist(address)) {
						request.clockCyclesToBlock += Config_Manager.instance.DCacheLatency;
						dCacheAccessHits++;
					} else {
						// Cache miss and cache block is free - > write in the cache
						// and
						// return the value
						// latency is 2(t + k)
						if (cache.isThereAFreeBlock(address)) {
							request.clockCyclesToBlock += MemoryBus_Manager.instance
									.getDelay();
							request.clockCyclesToBlock += 2 * (Config_Manager.instance.DCacheLatency + Config_Manager.instance.MemoryLatency);
						} else {
							// Cache miss and cache block is full - > Check if dirty
							// if dirty - > write back and update cache
							// else(not dirty) - > just update cache
							if (cache.isLRUBlockDirty(address)) {
								request.clockCyclesToBlock += MemoryBus_Manager.instance
										.getDelay();
								request.clockCyclesToBlock += (Config_Manager.instance.MemoryLatency);
								request.clockCyclesToBlock += 2 * (Config_Manager.instance.DCacheLatency + Config_Manager.instance.MemoryLatency);
							} else {
								request.clockCyclesToBlock += Config_Manager.instance.DCacheLatency;
							}
						}

					}
				}

			} else if (!request.lastRequestInstruction.equals(inst)) {
				throw new Exception(this.getClass().getSimpleName()
						+ " Cannot get different instructions from memory unit");
			} else {

				if (!request.hasAccessVariablesSet) {	//if AccessVariable is not set
					dCacheAccessRequests++;
					if (cache.doesAddressExist(address)) {
						dCacheAccessHits++;
						
					}
						
					request.hasAccessVariablesSet = true;
				}
				// For Store, find block, mark dirty & update address & lru
				// for Load, find block, update address & lru
				if (Processor_Params.CC - request.lastRequestInstructionEntryClock == 1 || Processor_Params.CC - request.lastRequestInstructionEntryClock == 0) {
					if (flag == 0 || flag2 == 2) {	// should run only in 1st iteration of while loop
						if (!cache.doesAddressExist(address)) {		//update cache only if address does not exist in it
							cache.updateBlock(address, DI.isStore(inst));
//							dCacheAccessHits++;
						}						
					}					
					if(flag == 1 && flag2 == 1 ) {	// should only run in 2nd iteration of while loop
						if (!cache.doesAddressExist(address)) {		//update cache only if address does not exist in it
							cache.updateBlock(address + 4, DI.isStore(inst));
							dCacheAccessHits++;
						}						
					}
					
				}
				/*
				 * if (ProcessorParams.CC - request.lastRequestInstructionEntryClock ==
				 * (request.clockCyclesToBlock-1)) { cache.updateBlock(address,
				 * DI.isStore(inst)); }
				 */
//				cache.updateBlock(address, DI.isStore(inst));
			}


							
			
			flag2--;
			if(flag == 1) {		//if its a load double instruction
				if (flag4 == 1) {
					address = (int) inst.address;
				}else
					address = (int) inst.addressDouble;
//				request.resetValues();
			}
			
			if(flag == 1 && flag2 == 0 ) {
				address = (int) inst.address;
			}
			
		}
				
		return validateClockCyclesToBlock();
	}

	private boolean validateClockCyclesToBlock() throws Exception {
		if (Processor_Params.CC - request.lastRequestInstructionEntryClock == request.clockCyclesToBlock) {
			// do any cache updates at this point?
			// cache.setInCache(request.lastRequestInstruction); // hack
			
			request.resetValues();
			
			return true;
		} else {
			return false;
		}
			
	}

	public String getDCacheStatistics() {
		String format = "%-60s %4s";
		StringBuilder sb = new StringBuilder();
		sb.append(String.format(format,
				"Total number of access requests for Data cache:", dCacheAccessRequests));
		sb.append('\n');
		sb.append(String.format(format, "Number of Data cache hits:", dCacheAccessHits));
		return sb.toString();
	}

}

class DCacheRequestData {
	DI lastRequestInstruction;
	int lastRequestInstructionEntryClock;
	int clockCyclesToBlock;
	boolean hasAccessVariablesSet;

	public DCacheRequestData() {
		resetValues();
	}

	public void resetValues() {

		lastRequestInstruction = null;
		lastRequestInstructionEntryClock = -1;
		clockCyclesToBlock = -1;
		hasAccessVariablesSet = false;
	}

}