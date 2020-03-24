package stages;
/*
 * CMSC-611 Advanced Computer Architecture - Final Project (Fall - 2019)
 * Author : Pranav B Ganore [YI73732]
 * pganore1@umbc.edu
*/

import instructions.DI;

public abstract class Stage
{
    protected Stage_Type stage_Type;

    public abstract void run() throws Exception;

    public abstract boolean checkIfFree(DI instruction)
            throws Exception;

    public boolean checkIfFree() throws Exception
    {
        return checkIfFree(null); // will throw exception for ExStage if used
    }

    public abstract boolean acceptInstruction(DI instruction)
            throws Exception;
}
