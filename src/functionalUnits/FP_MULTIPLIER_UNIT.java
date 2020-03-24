package functionalUnits;
/*
 * CMSC-611 Advanced Computer Architecture - Final Project (Fall - 2019)
 * Author : Pranav B Ganore [YI73732]
 * pganore1@umbc.edu
*/

import Simulator.Config_Manager;
import stages.Stage_Type;

public class FP_MULTIPLIER_UNIT extends FP_FUNCTIONAL_UNIT
{

    private static volatile FP_MULTIPLIER_UNIT instance;

    public static FP_MULTIPLIER_UNIT getInstance()
    {
        if (null == instance)
            synchronized (FP_MULTIPLIER_UNIT.class)
            {
                if (null == instance)
                    instance = new FP_MULTIPLIER_UNIT();
            }

        return instance;
    }

    private FP_MULTIPLIER_UNIT()
    {
        super();
        isPipelined = Config_Manager.instance.FPMultPipelined;
        clockCyclesRequired = Config_Manager.instance.FPMultLatency;
        pipelineSize = isPipelined ? Config_Manager.instance.FPMultLatency : 1;
        stageId = Stage_Type.EXSTAGE;
        createPipelineQueue(pipelineSize);
    }
}
