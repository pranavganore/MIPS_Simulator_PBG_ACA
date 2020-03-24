package caching;
/*
 * CMSC-611 Advanced Computer Architecture - Final Project (Fall - 2019)
 * Author : Pranav B Ganore [YI73732]
 * pganore1@umbc.edu
*/

public class MemoryBus_Manager
{

    public static final MemoryBus_Manager instance = new MemoryBus_Manager();
    private static int MemoryBusBlockedDelay = 0;
    

    private MemoryBus_Manager()
    {

    }

	public int getDelay()
    {
        // TODO Auto-generated method stub
        return MemoryBusBlockedDelay;
    }
	public void setDelay(int delaysent)
	{
		MemoryBusBlockedDelay = delaysent;
		 
	}
	public void releaseMemoryBus() {
		MemoryBusBlockedDelay = 0;
	}
	public boolean isBusBusy() {
		if (MemoryBusBlockedDelay > 0) {
			return true;
		}else
			return false;
		
	}

}
