package stages;
/*
 * CMSC-611 Advanced Computer Architecture - Final Project (Fall - 2019)
 * Author : Pranav B Ganore [YI73732]
 * pganore1@umbc.edu
*/

import instructions.DI;

public class WriteBack_Stage extends Stage
{

    private static volatile WriteBack_Stage instance;

    public static WriteBack_Stage getInstance()
    {

        if (null == instance)
            synchronized (WriteBack_Stage.class)
            {
                if (null == instance)
                    instance = new WriteBack_Stage();
            }

        return instance;
    }

    private functionalUnits.WRITEBACK_UNIT writeBack;

    private WriteBack_Stage()
    {
        super();

        this.stage_Type = Stage_Type.WBSTAGE;

        writeBack = functionalUnits.WRITEBACK_UNIT.getInstance();
    }

    @Override
    public void run() throws Exception
    {
        /*
         * System.out.println("------------------------------");
         * System.out.println("WRITEBACK - "); writeBack.dumpUnitDetails();
         * System.out.println("------------------------------");
         */

        writeBack.executeUnit();
    }

    @Override
    public boolean acceptInstruction(DI instruction) throws Exception
    {
        writeBack.acceptInstruction(instruction);
        return true;
    }

    @Override
    public boolean checkIfFree(DI instruction) throws Exception
    {
        return writeBack.checkIfFree(instruction);
    }
}
