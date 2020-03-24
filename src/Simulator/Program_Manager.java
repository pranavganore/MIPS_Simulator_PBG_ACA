package Simulator;

import java.util.Map;
import java.util.TreeMap;

import instructions.DI;

public class Program_Manager
{
    public static final Program_Manager instance = new Program_Manager();

    private Program_Manager()
    {
    }

    public int                       instructionCount = 0;
    public Map<Integer, DI> InstructionList  = new TreeMap<Integer, DI>();
    public Map<String, Integer>      LabelMap         = new TreeMap<String, Integer>();

    /**
     * Print out the Entire Program
     */
    /*public void dumpProgram()
    {

        for (int key : InstructionList.keySet())
        {
            System.out.println(key + " " + InstructionList.get(key).toString());
        }
        System.out.println(instructionCount);

        for (String Key : LabelMap.keySet())
        {
            System.out.println(Key + " " + LabelMap.get(Key));
        }
    }*/

    public DI getInstructionAtAddress(int address) throws Exception
    {
        if (!InstructionList.containsKey(address))
            throw new Exception("Instruction NOT Found at address: " + address);

        DI inst = InstructionList.get(address);
        return inst.getClass().getConstructor(inst.getClass())
                .newInstance(inst);
    }

    public int getInstructionAddreessForLabel(String label) throws Exception
    {
        if (!LabelMap.containsKey(label))
            throw new Exception("Instruction NOT Found at LABEL: " + label);

        return LabelMap.get(label);
    }
}
