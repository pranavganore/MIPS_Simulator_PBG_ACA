package instructions;

import functionalUnits.FunctionalUnit_Type;

public class DADDI extends Type2Reg1Imm {

	public DADDI(String sourceLabel, String destinationLabel, int immediate) {
		super(sourceLabel, destinationLabel, immediate);
		this.functionalUnit_Type = FunctionalUnit_Type.IU;
		this.instructionType = InstructionType.ARITHMETIC_IMM;
	}

	public DADDI(DADDI obj) {
		super(obj);
	}

	@Override
	public String toString() {
		return "DADDI " + dest.getDestinationLabel() + ", "
				+ src1.getSourceLabel() + ", " + immediate;
	}

	@Override
	public void executeInstruction() {
		dest.setDestination(src1.getSource() + immediate);
	}

}
