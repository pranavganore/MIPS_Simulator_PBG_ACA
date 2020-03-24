package functionalUnits;
/*
 * CMSC-611 Advanced Computer Architecture - Final Project (Fall - 2019)
 * Author : Pranav B Ganore [YI73732]
 * pganore1@umbc.edu
*/

import Simulator.Config_Manager;
import stages.Stage_Type;

public class FP_DIVISOR_UNIT extends FP_FUNCTIONAL_UNIT
{

    private static volatile FP_DIVISOR_UNIT instance;

    public static FP_DIVISOR_UNIT getInstance()
    {
        if (null == instance)
            synchronized (FP_DIVISOR_UNIT.class)
            {
                if (null == instance)
                    instance = new FP_DIVISOR_UNIT();
            }

        return instance;
    }

    private FP_DIVISOR_UNIT()
    {
        super();
        isPipelined = Config_Manager.instance.FPDividerPipelined;
        clockCyclesRequired = Config_Manager.instance.FPDivideLatency;
        pipelineSize = isPipelined ? Config_Manager.instance.FPDivideLatency : 1;
        stageId = Stage_Type.EXSTAGE;
        createPipelineQueue(pipelineSize);
    }
}
