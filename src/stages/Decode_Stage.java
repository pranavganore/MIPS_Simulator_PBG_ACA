package stages;
/*
 * CMSC-611 Advanced Computer Architecture - Final Project (Fall - 2019)
 * Author : Pranav B Ganore [YI73732]
 * pganore1@umbc.edu
*/

import functionalUnits.DECODE_UNIT;
import instructions.DI;

public class Decode_Stage extends Stage
{

    private static volatile Decode_Stage instance;

    public static Decode_Stage getInstance()
    {

        if (null == instance)
            synchronized (Decode_Stage.class)
            {
                if (null == instance)
                    instance = new Decode_Stage();
            }

        return instance;
    }

    private DECODE_UNIT decode;

    private Decode_Stage()
    {
        super();
        decode = DECODE_UNIT.getInstance();
        this.stage_Type = Stage_Type.IDSTAGE;
    }

    @Override
    public void run() throws Exception
    {
        decode.executeUnit();
    }

    @Override
    public boolean checkIfFree(DI instruction) throws Exception
    {
        return decode.checkIfFree(instruction);
    }

    @Override
    public boolean acceptInstruction(DI instruction) throws Exception
    {
        if (!decode.checkIfFree(instruction))
            throw new Exception("DECODESTAGE: Illegal state exception "
                    + instruction.toString());

        decode.acceptInstruction(instruction);

        return true;
    }

}
