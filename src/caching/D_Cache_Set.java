package caching;
/*
 * CMSC-611 Advanced Computer Architecture - Final Project (Fall - 2019)
 * Author : Pranav B Ganore [YI73732]
 * pganore1@umbc.edu
*/

import java.util.Arrays;

class D_Cache_Set
{
    D_Cache_Block[] d_Cache_Blocks;
    int           lru;

    public D_Cache_Set()
    {
        d_Cache_Blocks = new D_Cache_Block[2];
        d_Cache_Blocks[0] = new D_Cache_Block(-1);
        d_Cache_Blocks[1] = new D_Cache_Block(-1);
        lru = 0;
    }

    public void toggleLRU(D_Cache_Block block)
    {
        lru = (d_Cache_Blocks[0].equals(block)) ? 1 : 0;
    }

    public boolean isFree(int id)
    {
        return d_Cache_Blocks[id].isFree();
    }

    public boolean hasFreeBlock()
    {
        return isFree(0) || isFree(1);
    }

    public boolean doesAddressExist(int baseAddress)
    {
    	//TODO PBG 
//        boolean doesExist = Arrays.asList(dCacheBlocks[0].blockData).contains(baseAddress); 
//        return (dCacheBlocks[0].baseAddress == baseAddress)
//                || (dCacheBlocks[1].baseAddress == baseAddress);
    	for (int i = 0; i < (d_Cache_Blocks[0].blockData).length ;  i++) { 
    		  
            // accessing each element of array 
            if (d_Cache_Blocks[0].blockData[i] == baseAddress) {
            	d_Cache_Blocks[0].baseAddress = baseAddress;
            	return true;
            }

        } 
    	
    	for (int i = 0; i < (d_Cache_Blocks[1].blockData).length ;  i++) { 
  		  
            // accessing each element of array 
            if (d_Cache_Blocks[1].blockData[i] == baseAddress) {
            	d_Cache_Blocks[1].baseAddress = baseAddress;
            	return true;
            }

        } 
        
        return false;
         
    }

    public boolean isLRUBlockDirty()
    {
        return d_Cache_Blocks[lru].dirty;
    }

    public D_Cache_Block getAddressBlock(int baseAddress)
    {

        if (!doesAddressExist(baseAddress))
            return null;

        if (d_Cache_Blocks[0].baseAddress == baseAddress)
            return d_Cache_Blocks[0];
        return d_Cache_Blocks[1];
    }

    public D_Cache_Block getEmptyBlock(int baseAddress)
    {
        if (!hasFreeBlock())	//if does not have free block
            return null;
        if (d_Cache_Blocks[0].baseAddress == -1)	// if block0 is free return it
            return d_Cache_Blocks[0];
        return d_Cache_Blocks[1];					// else it implies block 1 is free and is returned
    }

    public D_Cache_Block getLRUBlock()
    {
        return d_Cache_Blocks[lru];
    }

}