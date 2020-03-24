package functionalUnits;
/*
 * CMSC-611 Advanced Computer Architecture - Final Project (Fall - 2019)
 * Author : Pranav B Ganore [YI73732]
 * pganore1@umbc.edu
*/

import Simulator.Result_Generator;
import instructions.DI;
import instructions.NOOP;
import instructions.WriteBackObject;
import registers.RegisterManager;
import stages.Processor_Params;
import stages.Stage_Type;

public class WRITEBACK_UNIT extends FUNCTIONAL_UNIT
{

    private static volatile WRITEBACK_UNIT instance;

    public static WRITEBACK_UNIT getInstance()
    {
        if (null == instance)
            synchronized (WRITEBACK_UNIT.class)
            {
                if (null == instance)
                    instance = new WRITEBACK_UNIT();
            }

        return instance;
    }

    private WRITEBACK_UNIT()
    {
        super();
        isPipelined = false;
        clockCyclesRequired = 1;
        pipelineSize = 1;
        stageId = Stage_Type.WBSTAGE;
        createPipelineQueue(pipelineSize);
    }

    @Override
    public void executeUnit() throws Exception
    {
        DI inst = peekFirst();

        if (inst instanceof NOOP)
            return;
        Thread.sleep(50L);
        System.out.println(Processor_Params.CC + " WriteBack " + inst.toString());

        // Write back the data to the destination register if any and unlock
        // destination register as Free
        WriteBackObject writeBackObject = inst.getDestinationRegister();

        if (writeBackObject != null)
        {
            RegisterManager.instance.setRegisterValue(
                    writeBackObject.getDestinationLabel(),
                    writeBackObject.getDestination());
            RegisterManager.instance.setRegisterFree(writeBackObject
                    .getDestinationLabel());
        }

        // Update the exit cycle in the instruction and pass it on to the result
        updateExitClockCycle(inst);
        // manager for printing
        Result_Generator.instance.queueInstructionForDisplay(inst);

        // Remove the instruction from the queue and enqueue a NOOP
        rotatePipe();
    }

    @Override
    public int getClockCyclesRequiredForNonPipeLinedUnit()
    {
        // TODO Auto-generated method stub
        return clockCyclesRequired;
    }
}
