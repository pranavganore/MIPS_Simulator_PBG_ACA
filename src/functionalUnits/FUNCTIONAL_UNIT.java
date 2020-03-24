package functionalUnits;
/*
 * CMSC-611 Advanced Computer Architecture - Final Project (Fall - 2019)
 * Author : Pranav B Ganore [YI73732]
 * pganore1@umbc.edu
*/

import java.util.ArrayDeque;

import Simulator.Result_Generator;
import instructions.DI;
import instructions.NOOP;
import stages.Processor_Params;
import stages.Stage_Type;

public abstract class FUNCTIONAL_UNIT
{

    public boolean                  isPipelined;
    public int                      clockCyclesRequired;
    public int                      pipelineSize;
    public Stage_Type                stageId;
    private ArrayDeque<DI> instructionQueue;

    public abstract void executeUnit() throws Exception;

    public abstract int getClockCyclesRequiredForNonPipeLinedUnit()
            throws Exception;

    // TODO: Increment entry cycle and exit cycle clock depending on stage
    // number
    public void acceptInstruction(DI instruction) throws Exception
    {

        if (!checkIfFree(instruction))
            throw new Exception("FUNCTIONALUNIT: Illegal state of queue "
                    + this.getClass().getSimpleName());

        removeLast();
        addLast(instruction);

        updateEntryClockCycle(instruction);

        validateQueueSize();

        /*
         * System.out.format("%-3s  %-20s %50s %n", CPU.CLOCK, this.getClass()
         * .getSimpleName(), instruction.debugString());
         */

    }

    protected void validateQueueSize() throws Exception
    {
        if (instructionQueue.size() != pipelineSize)
            throw new Exception("FUNCTIONALUNIT: Invalid Queue Size for unit "
                    + this.getClass().getName());
    }

    // This is being done for the execute stage functional units.
    public boolean checkIfFree(DI instruction) throws Exception
    {
        validateQueueSize();
        return (peekLast() instanceof NOOP) ? true : false;

    }

    public boolean checkIfFree() throws Exception
    {
        return checkIfFree(null);
    }

    /*
     * TODO may have to override this for If functionalunit
     */
    public boolean isReadyToSend() throws Exception
    {

        if (isPipelined)
        {
            if (!(peekFirst() instanceof NOOP))
            {
                return true;
            }
        }
        else
        {
            if (!(peekFirst() instanceof NOOP)
                    && ((Processor_Params.CC - peekFirst().entryCycle[stageId.getId()]) >= getClockCyclesRequiredForNonPipeLinedUnit()))
            {
                return true;
            }
        }

        return false;
    }

    /**
     * This will mark ONLY the last instruction in pipeline as Struct hazard
     * 
     * @throws Exception
     */
    public void markStructHazard() throws Exception
    {
        // defensive, call validateQueueSize, may call isReadyToSend too!
        validateQueueSize();

        // TODO find this out else do
        peekFirst().STRUCT = true;

        // // starting from last inst till we reach first of Q or a NOOP mark
        // // Struct Hazard
//         for (Iterator<Instruction> itr = this.instructionQueue
//         .descendingIterator(); itr.hasNext();)
//         {
//         Instruction inst = itr.next();
//         if (inst instanceof NOOP)
//         break;
//        
//         inst.STRUCT = true;
//         }
    }

    protected void updateEntryClockCycle(DI inst)
    {
//    	if (Display.instance.isSecondHALT() == false)
    		inst.entryCycle[this.stageId.getId()] = Processor_Params.CC;
    }

    protected void updateExitClockCycle(DI inst)
    {
    	inst.exitCycle[this.stageId.getId()] = Processor_Params.CC;
    	if (Result_Generator.instance.isSecondHALT() == true) {		// quick fix - because the instructions are processed post cycle
    		inst.exitCycle[this.stageId.getId()] = Processor_Params.CC - 1;
    	}
    }

    /**
     * Functions to update instructionQueue
     * 
     */

    protected DI[] pipelineToArray()
    {
        return instructionQueue
                .toArray(new DI[instructionQueue.size()]);
    }

    protected void createPipelineQueue(int size)
    {
        instructionQueue = new ArrayDeque<DI>();
        for (int i = 0; i < size; i++)
            instructionQueue.addLast(new NOOP());
    }

    protected void rotatePipe() throws Exception
    {
        validateQueueSize();
        instructionQueue.removeFirst();
        instructionQueue.addLast(new NOOP());
    }

    public DI peekFirst()
    {
        return instructionQueue.peekFirst();
    }

    public DI peekLast()
    {
        return instructionQueue.peekLast();
    }

    protected void addFirst(DI inst)
    {
        instructionQueue.addFirst(inst);
    }

    protected void addLast(DI inst)
    {
        instructionQueue.addLast(inst);
    }

    protected DI removeFirst()
    {
        return instructionQueue.removeFirst();
    }

    protected DI removeLast()
    {
        return instructionQueue.removeLast();
    }
}
