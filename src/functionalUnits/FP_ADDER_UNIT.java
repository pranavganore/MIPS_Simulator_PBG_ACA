package functionalUnits;
/*
 * CMSC-611 Advanced Computer Architecture - Final Project (Fall - 2019)
 * Author : Pranav B Ganore [YI73732]
 * pganore1@umbc.edu
*/

import Simulator.Config_Manager;
import stages.Stage_Type;

public class FP_ADDER_UNIT extends FP_FUNCTIONAL_UNIT
{

    private static volatile FP_ADDER_UNIT instance;

    public static FP_ADDER_UNIT getInstance()
    {
        if (null == instance)
            synchronized (FP_ADDER_UNIT.class)
            {
                if (null == instance)
                    instance = new FP_ADDER_UNIT();
            }

        return instance;
    }

    private FP_ADDER_UNIT()
    {
        super();
        isPipelined = Config_Manager.instance.FPAdderPipelined;
        clockCyclesRequired = Config_Manager.instance.FPAdderLatency;
        pipelineSize = isPipelined ? Config_Manager.instance.FPAdderLatency : 1;
        stageId = Stage_Type.EXSTAGE;
        createPipelineQueue(pipelineSize);
    }
}
