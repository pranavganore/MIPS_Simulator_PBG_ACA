package functionalUnits;
/*
 * CMSC-611 Advanced Computer Architecture - Final Project (Fall - 2019)
 * Author : Pranav B Ganore [YI73732]
 * pganore1@umbc.edu
*/

import caching.MemoryBus_Manager;
import instructions.DI;
import instructions.NOOP;
import stages.Processor_Params;
import stages.Stage_Type;

public class INTEGER_UNIT extends FUNCTIONAL_UNIT
{

    private static volatile INTEGER_UNIT instance;

    public static INTEGER_UNIT getInstance()
    {
        if (null == instance)
            synchronized (INTEGER_UNIT.class)
            {
                if (null == instance)
                    instance = new INTEGER_UNIT();
            }

        return instance;
    }

    private INTEGER_UNIT()
    {
        super();
        isPipelined = false;
        clockCyclesRequired = 1;
        pipelineSize = 1;
        stageId = Stage_Type.EXSTAGE;
        createPipelineQueue(pipelineSize);

    }

    @Override
    public void executeUnit() throws Exception
    {
        validateQueueSize();

        DI inst = peekFirst();

        if (inst instanceof NOOP)
            return;

        inst.executeInstruction();
        int p = Processor_Params.CC;
        if (MEMORY_UNIT.getInstance().checkIfFree(inst))
        {
            MEMORY_UNIT.getInstance().acceptInstruction(inst);
            updateExitClockCycle(inst);
            rotatePipe();
        }
        else
        {
            markStructHazard();
        }

    }

    @Override
    public int getClockCyclesRequiredForNonPipeLinedUnit()
    {
        return clockCyclesRequired;
    }
}
