package caching;
/*
 * CMSC-611 Advanced Computer Architecture - Final Project (Fall - 2019)
 * Author : Pranav B Ganore [YI73732]
 * pganore1@umbc.edu
*/

public class D_Cache
{
    D_Cache_Set[] d_Cache_Set;

    public D_Cache()
    {
        d_Cache_Set = new D_Cache_Set[2];
        d_Cache_Set[0] = new D_Cache_Set();
        d_Cache_Set[1] = new D_Cache_Set();
    }
    
    private D_Cache_Set getSet(int address)
    {
//        int setId = address & 0b10000;
//        setId = setId >> 4;
    	int blocksize = 4;
    	int Num1 = address / blocksize;
    	int offset = Num1 % blocksize;
    	int Num2 = Num1 - offset;
    	int block_base_Address = Num2 * blocksize;
    	
    	
    	// Block Address = word address/word per blocks
    	int Block_Addr = block_base_Address / blocksize;
    	//Set Number = (Block Adrr) Modulo (No of sets)
    	int setID = block_base_Address % 2;
        return d_Cache_Set[setID];
    }

    private int getBaseAddress(int address)
    {
//        int baseAddress = address >> 2;
//        baseAddress = baseAddress << 2;
    	int blocksize = 4;
    	int Num1 = address / blocksize;
    	int offset = Num1 % blocksize;
    	int Num2 = Num1 - offset;
    	int block_base_Address = Num2 * blocksize;
    	return block_base_Address;
//        return baseAddress;
    }

    public boolean doesAddressExist(int address)
    {
        D_Cache_Set set = getSet(address);
        int baseAddress = getBaseAddress(address);
        return set.doesAddressExist(baseAddress);
    }

    public boolean isThereAFreeBlock(int address)
    {
        D_Cache_Set set = getSet(address);
        return set.hasFreeBlock();
    }

    public boolean isLRUBlockDirty(int address)
    {
        D_Cache_Set set = getSet(address);
        return set.isLRUBlockDirty();
    }

    public void updateBlock(int address, boolean store) throws Exception
    {
        // TODO Auto-generated method stub
        D_Cache_Set set = getSet(address);
        int baseAddress = getBaseAddress(address);

        D_Cache_Block block = null;
        // update same address block, if not then free block , if not then
        // lrublock
        if (doesAddressExist(address))
        {
            block = set.getAddressBlock(baseAddress);
        }
        else if (isThereAFreeBlock(address))
        {
            block = set.getEmptyBlock(baseAddress);
        }
        else
        {
            block = set.getLRUBlock();
        }
        if (block == null)
            throw new Exception("DCache cannot find a null block");
        block.baseAddress = baseAddress;
        block.dirty = store;
        block.blockData = populate_block(baseAddress , 4);
        set.toggleLRU(block);
    }
    
    public int[] populate_block(int address , int blocksize){
    	
    	int[] Block_Array = new int[blocksize];
    	
    	int Num1 = address / blocksize;
    	int offset = Num1 % blocksize;
    	int Num2 = Num1 - offset;
    	int Addr0 = Num2 * blocksize;
    			
        //int temp = address - (address % blocksize);

        int j=0;
        //calculating a block with 4 addresses stored in it 
        for (int i = Addr0; i < Addr0 + (blocksize*4); i=i+4)
        {
//            if (!memoryDataMap.containsKey(i))
//                System.out.println("Location not found in data memory: " + i);
//            else
                Block_Array[j] = i;
            	j++;
        }
        return Block_Array;
    }

}