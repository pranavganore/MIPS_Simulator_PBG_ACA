package caching;
/*
 * CMSC-611 Advanced Computer Architecture - Final Project (Fall - 2019)
 * Author : Pranav B Ganore [YI73732]
 * pganore1@umbc.edu
*/

class D_Cache_Block
{
    int     baseAddress;
    boolean dirty;
    int[] blockData;

    public D_Cache_Block(int baseAddress)
    {
        this.baseAddress = baseAddress;
        this.dirty = false;
        this.blockData = new int[4];
    }

    public boolean isFree()
    {
        return (baseAddress == -1);
    }
    
    /*public int[] populate_block(int address , int blocksize){
    	
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
    }*/
    


}