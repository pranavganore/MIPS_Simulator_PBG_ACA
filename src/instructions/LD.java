package instructions;

import functionalUnits.FunctionalUnit_Type;

public class LD extends Type2Reg1Imm {
	public LD(String sourceLabel, String destinationLabel, int immediate) {
		super(sourceLabel, destinationLabel, immediate);
		this.functionalUnit_Type = FunctionalUnit_Type.IU;
		this.instructionType = InstructionType.MEMORY_FPREG;
	}

	public LD(LD obj) {
		super(obj);
	}

	@Override
	public String toString() {
		return "LD " + dest.getDestinationLabel() + ", " + immediate + "("
				+ src1.getSourceLabel() + ")";
	}

	@Override
	public void executeInstruction() {
		this.address = immediate + src1.getSource();
		this.addressDouble = this.address + 4;
	}

}
