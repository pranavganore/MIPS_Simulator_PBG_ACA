package Simulator;
/*
 * CMSC-611 Advanced Computer Architecture - Final Project (Fall - 2019)
 * Author : Pranav B Ganore [YI73732]
 * pganore1@umbc.edu
*/

import java.util.Map;
import java.util.TreeMap;

import parsers.Calculate_x_Sq;

public class DataMemory_Manager
{
    public static final DataMemory_Manager instance = new DataMemory_Manager();

    private DataMemory_Manager()
    {

    }

    // address to data
    // address starts at 0x100 or 256 decimal
    private Map<Integer, Integer> memoryDataMap = new TreeMap<Integer, Integer>();

    // Used only at the start by the parser
    public void setValueToAddress(int address, int data)
    {
        memoryDataMap.put(address, data);
    }

    // get Value from address - used for testing and pipeline only
    // implementation
    public int getValueFromAddress(int address) throws Exception
    {
        if (!memoryDataMap.containsKey(address))
            throw new Exception("Data Memory Address Not Found " + address);
        
        // getting a block with 4 word address stored
//        int[] Block_Array = new int[4];
//        Block_Array = getMemoryBlockwithArrayOfAddress(address , 4);
        
        return memoryDataMap.get(address);
    }

    // 256,260,264,268
    // 272,276,280,284
    
    // get block from memory given a base address, eg, if 257 asked, then give
    // 256,257,258,259
    /*
     * Default block size is size of 4
     */
    public Map<Integer, Integer> getMemoryBlockOfAddress(int address)
            throws Exception
    {
        return getMemoryBlockOfAddress(address, 4);
    }

    /**
     * 
     * @param address
     * @param blocksize
     *            should be a power of 2 only
     * @return
     */
    public Map<Integer, Integer> getMemoryBlockOfAddress(int address,
            int blocksize) throws Exception
    {
        Map<Integer, Integer> returnMap = new TreeMap<Integer, Integer>();

        if (!Calculate_x_Sq.xraisedTo2(blocksize))
            throw new Exception("Need a power of 2 only for block size "
                    + blocksize);
        int temp = address - (address % blocksize);

        //calculating a block with 4 addresses stored in it 
        for (int i = temp; i < temp + (blocksize*4); i=i+4)
        {
            if (!memoryDataMap.containsKey(i))
                System.out.println("Location not found in data memory: " + i);
            else
                returnMap.put(i, memoryDataMap.get(i));

        }
        return returnMap;
    }

    

    /**
     * 
     * @param address
     * @param blocksize
     *            should be a power of 2 only
     * @return
     */
    public int[] getMemoryBlockwithArrayOfAddress(int address,
            int blocksize) throws Exception
    {
        int Block_Array[] = new int[blocksize];

        if (!Calculate_x_Sq.xraisedTo2(blocksize))
            throw new Exception("Need a power of 2 only for block size "
                    + blocksize);
        int temp = address - (address % blocksize);

        int j=0;
        //calculating a block with 4 addresses stored in it 
        for (int i = temp; i < temp + (blocksize*4); i=i+4)
        {
            if (!memoryDataMap.containsKey(i))
                System.out.println("Location not found in data memory: " + i);
            else
                Block_Array[j] = i;
            	j++;
        }
        return Block_Array;
    }
    
    
    /**
     * printout contents of all memory locations
     */
    /*public void dumpAllMemory()
    {
        String leftAlignFormat = "| %-5d | %-10d |%n";
        for (Integer key : memoryDataMap.keySet())
        {
            System.out.format(leftAlignFormat, key, memoryDataMap.get(key));
        }
    }*/
}
