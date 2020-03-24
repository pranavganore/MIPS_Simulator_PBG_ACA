package caching;
/*
 * CMSC-611 Advanced Computer Architecture - Final Project (Fall - 2019)
 * Author : Pranav B Ganore [YI73732]
 * pganore1@umbc.edu
*/

public class I_Cache_Block {

	int[] words;
	int tag;

	public I_Cache_Block() {
		words = new int[4];
		for(int i =0; i< 4; i++)
			words[i] = -1;
		tag = 1;
	}
}