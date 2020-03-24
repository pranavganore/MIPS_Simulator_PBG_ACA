package functionalUnits;
/*
 * CMSC-611 Advanced Computer Architecture - Final Project (Fall - 2019)
 * Author : Pranav B Ganore [YI73732]
 * pganore1@umbc.edu
*/

import stages.Processor_Params;
import stages.Stage_Type;
import stages.WriteBack_Stage;
import Simulator.Config_Manager;
import Simulator.DataMemory_Manager;
import caching.D_Cache_Manager;
import caching.MemoryBus_Manager;
import instructions.DI;
import instructions.InstructionType;
import instructions.NOOP;
import instructions.StoreInstruction;


public class MEMORY_UNIT extends FUNCTIONAL_UNIT {

	private static volatile MEMORY_UNIT instance;

	public static MEMORY_UNIT getInstance() {
		if (null == instance)
			synchronized (MEMORY_UNIT.class) {
				if (null == instance)
					instance = new MEMORY_UNIT();
			}

		return instance;
	}

	private MEMORY_UNIT() {
		super();
		isPipelined = false;
		clockCyclesRequired = Config_Manager.instance.MemoryLatency;
		pipelineSize = 1;
		stageId = Stage_Type.EXSTAGE;
		createPipelineQueue(pipelineSize);
	}

	@Override
	public void executeUnit() throws Exception {
		validateQueueSize();

		DI inst = peekFirst();
		if (!(inst instanceof NOOP)) {

			// TODO for pipelined execution
			// check if inst has spent enough time in this unit

			switch (Processor_Params.exeType) {
			case M:
				// TODO call DCacheManager only if inst is type of Memory
				// Operation
				if (DI.isLoadStore(inst)
						&& !D_Cache_Manager.instance.canProceed(inst)) {
//					if(inst.addressDouble > 1) { 
//						inst.address = inst.addressDouble;
//						if(!DCacheManager.instance.canProceed(inst)) { 
//							inst.address = inst.address-4;
//							return; 
//							} 
//						}								
					if(MemoryBus_Manager.instance.isBusBusy()) {
						inst.STRUCT = true;
					}
					Thread.sleep(50L);
					System.out.println(Processor_Params.CC + " Memory " + inst.toString());
					return;
				}
			
				break;
			case P:
				if (!((Processor_Params.CC - inst.entryCycle[stageId.getId()]) >= this
						.getClockCyclesRequiredForNonPipeLinedUnit()))
					return;
				break;
			default:
				throw new Exception("MemoryUnit Illegal CPU.RUN_TYPE ");
			}

			// TODO for cache, check if data is available yet

			if (DI.isLoadStore(inst)) {
				if (inst instanceof StoreInstruction)
					DataMemory_Manager.instance.setValueToAddress(
							(int) inst.address, ((StoreInstruction) inst)
									.getValueToWrite().getSource());
				else
					inst.getDestinationRegister().setDestination(
							DataMemory_Manager.instance
									.getValueFromAddress((int) inst.address));
			}

			if (!WriteBack_Stage.getInstance().checkIfFree(inst))
				throw new Exception(
						"MemoryUnit: won tie, WB Stage should always be free");

			WriteBack_Stage.getInstance().acceptInstruction(inst);
			updateExitClockCycle(inst);
		}
		rotatePipe();

	}

	@Override
	public int getClockCyclesRequiredForNonPipeLinedUnit() throws Exception {
		// TODO Auto-generated method stub
		DI inst = peekFirst();
		if (inst.instructionType.equals(InstructionType.MEMORY_FPREG)
				|| inst.instructionType.equals(InstructionType.MEMORY_REG))
			return clockCyclesRequired;
		else if (inst.instructionType.equals(InstructionType.ARITHMETIC_REG)
				|| inst.instructionType.equals(InstructionType.ARITHMETIC_IMM))
			return 1;

		throw new Exception("MemoryUnit: Illegal instruction in Memory Unit: "
				+ inst.toString());
	}

	// TODO override acceptInstruction here, first call super.accept , then get
	// data from datamanager

}
