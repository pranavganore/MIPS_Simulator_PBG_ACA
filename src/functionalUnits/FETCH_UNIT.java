package functionalUnits;
/*
 * CMSC-611 Advanced Computer Architecture - Final Project (Fall - 2019)
 * Author : Pranav B Ganore [YI73732]
 * pganore1@umbc.edu
*/

import stages.Processor_Params;
import stages.Stage_Type;
import stages.Decode_Stage;
import Simulator.Program_Manager;
import Simulator.Result_Generator;
import caching.I_Cache_Manager;
import instructions.DI;
import instructions.NOOP;

public class FETCH_UNIT extends FUNCTIONAL_UNIT {

	private static volatile FETCH_UNIT instance;

	public static FETCH_UNIT getInstance() {
		if (null == instance)
			synchronized (FETCH_UNIT.class) {
				if (null == instance)
					instance = new FETCH_UNIT();
			}

		return instance;
	}

	private FETCH_UNIT() {
		super();
		this.isPipelined = false;
		this.clockCyclesRequired = 1;
		this.pipelineSize = 1;
		this.stageId = Stage_Type.IFSTAGE;
		createPipelineQueue(pipelineSize);
	}

	@Override
	public int getClockCyclesRequiredForNonPipeLinedUnit() {
		return clockCyclesRequired;
	}

	@Override
	public void executeUnit() throws Exception {
		validateQueueSize();

		DI inst = peekFirst();

		if (!(inst instanceof NOOP)) {
			Thread.sleep(50L);
			System.out.println(Processor_Params.CC + " Fetch ");

			if (Decode_Stage.getInstance().checkIfFree(inst)) {

				Decode_Stage.getInstance().acceptInstruction(inst);
				updateExitClockCycle(inst);
				rotatePipe();
			}
		}

		fetchNextInstruction();
	}

	public void flushUnit() throws Exception {
		validateQueueSize();

		DI inst = peekFirst();
		
		Thread.sleep(50L);
		System.out.println("FetchUnit flushUnit called for inst: "+inst.toString());

		if (inst instanceof NOOP)
			return;

		// update inst exitcycle
		// updateEntryClockCycle(inst); // hack dont do this!!!
		updateExitClockCycle(inst);
		// send to result manager
		Result_Generator.instance.queueInstructionForDisplay(inst);
		// remove inst & add NOOP
		rotatePipe();

		validateQueueSize();
	}

	private void fetchNextInstruction() throws Exception {
		// fetch a new instruction only if ifStage is free
		if (checkIfFree()) {
			boolean checkInst = false;

			DI next = null;
			switch (Processor_Params.exeType) {
			case M:

				next = I_Cache_Manager.getInstance().getInstructionFromCache(
						Processor_Params.PC);
				if (next != null)
					checkInst = true;
				
				/*next = ProgramManager.instance
						.getInstructionAtAddress(CPU.PROGRAM_COUNTER);
				checkInst = true;*/
				break;

			case P:
				next = Program_Manager.instance
						.getInstructionAtAddress(Processor_Params.PC);
				checkInst = true;
				break;
			}

			if (checkInst && checkIfFree()) {
				acceptInstruction(next);
				Processor_Params.PC++;
			}

		} // end ifStage.checkIfFree

	}
}
