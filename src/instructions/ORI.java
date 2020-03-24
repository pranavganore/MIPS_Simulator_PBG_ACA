package instructions;

import functionalUnits.FunctionalUnit_Type;

public class ORI extends Type2Reg1Imm {

	public ORI(String sourceLabel, String destinationLabel, int immediate) {
		super(sourceLabel, destinationLabel, immediate);
		this.functionalUnit_Type = FunctionalUnit_Type.IU;
		this.instructionType = InstructionType.ARITHMETIC_IMM;
	}

	public ORI(ORI obj) {
		super(obj);
	}

	@Override
	public String toString() {
		return "ORI " + dest.getDestinationLabel() + ", "
				+ src1.getSourceLabel() + ", " + immediate;
	}

	@Override
	public void executeInstruction() {
		dest.setDestination(src1.getSource() | immediate);
	}

}
