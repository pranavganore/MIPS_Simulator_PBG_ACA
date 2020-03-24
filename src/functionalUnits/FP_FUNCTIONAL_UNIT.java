package functionalUnits;
/*
 * CMSC-611 Advanced Computer Architecture - Final Project (Fall - 2019)
 * Author : Pranav B Ganore [YI73732]
 * pganore1@umbc.edu
*/

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Iterator;

import instructions.DADDI;
import instructions.DI;
import instructions.HLT;
import instructions.J;
import instructions.NOOP;
import stages.WriteBack_Stage;

public abstract class FP_FUNCTIONAL_UNIT extends FUNCTIONAL_UNIT
{

    @Override
    public void executeUnit() throws Exception
    {
        validateQueueSize();

        DI inst = peekFirst();
        inst.executeInstruction();

        // TODO clean this up!!!
        if (!(inst instanceof NOOP))
        {
            if (isReadyToSend())
            {
                if (!WriteBack_Stage.getInstance().checkIfFree(inst))
                    throw new Exception(this.getClass().getSimpleName()
                            + " won tie, WB Stage should always be free");

                WriteBack_Stage.getInstance().acceptInstruction(inst);
                updateExitClockCycle(inst);
            }
            else if (!isPipelined)
            {
                return;
            }
        }

        // This is the same effect as running pipleine once :)
        rotatePipe();
    }

    @Override
    public int getClockCyclesRequiredForNonPipeLinedUnit()
    {
        return clockCyclesRequired;
    }

    public void rotatePipelineOnHazard() throws Exception
    {
        validateQueueSize();
        if (!isPipelined)
            return;
        // non pipelined, now iterate in reverse

        DI objects[] = pipelineToArray();

        for (int i = 0; i < objects.length - 1; i++)
        {
            if (objects[i] instanceof NOOP)
            {
                DI temp = objects[i];
                objects[i] = objects[i + 1];
                objects[i + 1] = temp;
            }
        }

        createPipelineQueue(0);
        for (int i = 0; i < objects.length; i++)
        {
            addLast(objects[i]);
        }
        validateQueueSize();
    }

    public static void main(String[] args)
    {
        ArrayDeque<DI> deque = new ArrayDeque<DI>();
        deque.add(new HLT());
        deque.add(new NOOP());
        deque.add(new J("Jump"));
        deque.add(new NOOP());
        deque.add(new NOOP());
        deque.add(new DADDI("src1", "src2", 123));

        DI[] objects = deque
                .toArray(new DI[deque.size()]);

        System.out.println(Arrays.toString(objects));

        for (int i = 0; i < objects.length - 1; i++)
        {
            if (objects[i] instanceof NOOP)
            {
                DI temp = objects[i];
                objects[i] = objects[i + 1];
                objects[i + 1] = temp;
            }
        }

        System.out.println(Arrays.toString(objects));

        deque = new ArrayDeque<DI>();
        for (DI instruction : objects)
        {
            deque.add(instruction);
        }

        for (Iterator<DI> itr = deque.iterator(); itr.hasNext();)
        {
            System.out.print(itr.next().toString() + " ");
        }

    }
}
