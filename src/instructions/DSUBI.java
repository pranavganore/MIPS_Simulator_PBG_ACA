package instructions;

import functionalUnits.FunctionalUnit_Type;

public class DSUBI extends Type2Reg1Imm {

	public DSUBI(String sourceLabel, String destinationLabel, int immediate) {
		super(sourceLabel, destinationLabel, immediate);
		this.functionalUnit_Type = FunctionalUnit_Type.IU;
		this.instructionType = InstructionType.ARITHMETIC_IMM;
	}

	public DSUBI(DSUBI obj) {
		super(obj);
	}

	@Override
	public String toString() {
		return "DSUBI " + dest.getDestinationLabel() + ", "
				+ src1.getSourceLabel() + ", " + immediate;
	}

	@Override
	public void executeInstruction() {
		dest.setDestination(src1.getSource() - immediate);
	}
}
