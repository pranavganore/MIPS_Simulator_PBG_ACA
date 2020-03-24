package stages;
/*
 * CMSC-611 Advanced Computer Architecture - Final Project (Fall - 2019)
 * Author : Pranav B Ganore [YI73732]
 * pganore1@umbc.edu
*/

import instructions.DI;

public class Fetch_Stage extends Stage
{

    private static volatile Fetch_Stage instance;

    public static Fetch_Stage getInstance()
    {

        if (null == instance)
            synchronized (Fetch_Stage.class)
            {
                if (null == instance)
                    instance = new Fetch_Stage();
            }

        return instance;
    }

    private functionalUnits.FETCH_UNIT fetch;

    private Fetch_Stage()
    {
        super();
        fetch = functionalUnits.FETCH_UNIT.getInstance();
        this.stage_Type = Stage_Type.IFSTAGE;
    }

    @Override
    public void run() throws Exception
    {
        fetch.executeUnit();
    }

    @Override
    public boolean checkIfFree(DI instruction) throws Exception
    {
        return fetch.checkIfFree(instruction);
    }

    @Override
    public boolean acceptInstruction(DI instruction) throws Exception
    {
        if (!fetch.checkIfFree(instruction))
            throw new Exception("FetchStage: Illegal state exception "
                    + instruction.toString());

        fetch.acceptInstruction(instruction);
        return true;
    }

    public void flushStage() throws Exception
    {
        fetch.flushUnit();
    }

}
