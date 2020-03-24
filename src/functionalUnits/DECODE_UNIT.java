package functionalUnits;
/*
 * CMSC-611 Advanced Computer Architecture - Final Project (Fall - 2019)
 * Author : Pranav B Ganore [YI73732]
 * pganore1@umbc.edu
*/

import java.util.List;

import Simulator.Program_Manager;
import Simulator.Result_Generator;
import instructions.BEQ;
import instructions.BNE;
import instructions.CB;
import instructions.DI;
import instructions.HLT;
import instructions.HLT2;
import instructions.J;
import instructions.NOOP;
import instructions.SourceObject;
import instructions.WriteBackObject;
import registers.RegisterManager;
import stages.Processor_Params;
import stages.Stage_Type;
import stages.Ex_Stage;
import stages.Fetch_Stage;

public class DECODE_UNIT extends FUNCTIONAL_UNIT {

	private static volatile DECODE_UNIT instance;
	private static boolean firststhaltidentifier;
	
	
	public static DECODE_UNIT getInstance() {
		if (null == instance)
			synchronized (DECODE_UNIT.class) {
				if (null == instance)
					instance = new DECODE_UNIT();
			}

		return instance;
	}

	private DECODE_UNIT() {
		super();
		isPipelined = false;
		clockCyclesRequired = 1;
		pipelineSize = 1;
		stageId = Stage_Type.IDSTAGE;
		createPipelineQueue(pipelineSize);
//		firststhaltidentifier = false;

	}

	@Override
	public int getClockCyclesRequiredForNonPipeLinedUnit() {
		return clockCyclesRequired;
	}

	@Override
	public void executeUnit() throws Exception {
		// Called by the decode stage
		validateQueueSize();

		DI inst = peekFirst();

		if (inst instanceof NOOP)
			return;

		if (Result_Generator.instance.isHALT() == false) {
			Thread.sleep(50L);
			System.out.println(Processor_Params.CC + " Decode " + inst.toString());
		}
		

		boolean hazards = processHazards(inst);

		if ((!hazards))
			executeDecode(inst);

		validateQueueSize();

	}

	private void executeDecode(DI inst) throws Exception {

		
		updateExitClockCycle(inst);
//		if (Display.instance.isHALT() == false) {
//			Display.instance.queueInstructionForDisplay(inst);
//		}
		
		
//		updateExitClockCycle(inst);
//		Display.instance.queueInstructionForDisplay(inst);

		// read source registers
		List<SourceObject> sources = inst.getSourceRegister();
		if (sources != null) {
			for (SourceObject register : sources) {
				register.setSource(RegisterManager.instance
						.getRegisterValue(register.getSourceLabel()));
			}
		}

		// lock destination register
		WriteBackObject destReg = inst.getDestinationRegister();
		if (destReg != null)
			RegisterManager.instance.setRegisterBusy(destReg
					.getDestinationLabel());

		// process J instruction
		if (inst instanceof J) {
			// update PC to label address
			Processor_Params.PC = Program_Manager.instance
					.getInstructionAddreessForLabel(((J) inst)
							.getDestinationLabel());

			Fetch_Stage.getInstance().flushStage();

		}
		// process BNE,BEQ instruction
		else if (inst instanceof CB) {
			if (inst instanceof BEQ) {
				if (((CB) inst).compareRegisters()) {
					// update PC
					Processor_Params.PC = Program_Manager.instance
							.getInstructionAddreessForLabel(((BEQ) inst)
									.getDestinationLabel());
					// Flush fetch stage
					Fetch_Stage.getInstance().flushStage();
				}
			} else if (inst instanceof BNE) {
				if (!((CB) inst).compareRegisters()) {
					// update PC
					Processor_Params.PC = Program_Manager.instance
							.getInstructionAddreessForLabel(((BNE) inst)
									.getDestinationLabel());
					// Flush fetch stage
					Fetch_Stage.getInstance().flushStage();
				}
			}
		}
		// process HLT instruction
		else if (inst instanceof HLT) {
			
			Result_Generator.instance.setHALT(true);
//			FetchStage.getInstance().flushStage();
			if (firststhaltidentifier == true) {	
				
				Result_Generator.instance.setSecondHALT(true);
				Fetch_Stage.getInstance().flushStage();
				
			}			
			firststhaltidentifier = true;
			
		}else {

			if (!Ex_Stage.getInstance().checkIfFree(inst))
				throw new Exception(
						"DecodeUnit: failed in exstage.checkIfFree after resolving struct hazard "
								+ inst.toString());

			Ex_Stage.getInstance().acceptInstruction(inst);

		}
//		updateExitClockCycle(inst);
		if (Result_Generator.instance.isSecondHALT() == false) {
			Result_Generator.instance.queueInstructionForDisplay(inst);
		}
		rotatePipe();
	}

	private boolean processStruct(DI inst) throws Exception {
		// Check for possible STRUCT hazards
		FunctionalUnit_Type type = inst.functionalUnit_Type;
		if (!type.equals(FunctionalUnit_Type.UNKNOWN)) {
			if (!(Ex_Stage.getInstance().checkIfFree(inst))) {
				inst.STRUCT = true;
				return true;
			}
		}

		return false;
	}

	private boolean processRAW(DI inst) throws Exception {
		// Check for possible RAW hazards

		List<SourceObject> sources = inst.getSourceRegister();
		if (sources != null) {
			for (SourceObject register : sources) {
				if (!RegisterManager.instance.isRegisterFree(register
						.getSourceLabel())) {
					inst.RAW = true;
					return true;
				}
			}
		}

		return false;
	}

	private boolean processWAW(DI inst) throws Exception {
		WriteBackObject dest = inst.getDestinationRegister();
		if (dest != null) {

			if (!RegisterManager.instance.isRegisterFree(dest
					.getDestinationLabel())) {
				inst.WAW = true;
				return true;
			}
		}
		return false;
	}

	private boolean processWAR(DI inst) {
		return false;
	}

	private boolean processHazards(DI inst) throws Exception {
		return (processRAW(inst) || processWAR(inst) || processWAW(inst) || processStruct(inst));
	}
}
