package caching;
/*
 * CMSC-611 Advanced Computer Architecture - Final Project (Fall - 2019)
 * Author : Pranav B Ganore [YI73732]
 * pganore1@umbc.edu
*/

public class I_Cache {

	public I_Cache_Block[] iCache = new I_Cache_Block[4];
	private static volatile I_Cache instance;

	public static I_Cache getInstance() {
		if (null == instance)
			synchronized (I_Cache.class) {
				if (null == instance)
					instance = new I_Cache();
			}

		return instance;
	}

	private I_Cache() {

		for (int i = 0; i < 4; i++) {
			I_Cache_Block iCBObj = new I_Cache_Block();
			iCache[i] = iCBObj;
		}

	}

	public boolean isInstructionInCache(int pc) throws Exception {

		int wordIndex, blockIndex;
		int pcounter = pc;

		wordIndex = pcounter & 0b11;
		pcounter = pcounter >> 2;
		blockIndex = pcounter & 0b11;

		I_Cache_Block iBlock = this.iCache[blockIndex];

		if (iBlock.words[wordIndex] == pc)
			return true;

		return false;

	}

	// Fetch instruction from Memory to store in the cache

	public void populateICache(int pc) {

		int startPointer;
		int blockIndex;

		startPointer = pc % 4 == 0 ? pc : pc - (pc % 4);

		I_Cache_Block iblock = new I_Cache_Block();

		for (int i = 0; i < 4; i++) {
			iblock.words[i] = startPointer++;
		}

		pc = pc >> 2;
		blockIndex = pc & 0b11;

		this.iCache[blockIndex] = iblock;

	}

}
