package instructions;

import functionalUnits.FunctionalUnit_Type;

public class DADD extends Type3Reg {
	public DADD(String sourceLabel1, String sourceLabel2,
			String destinationLabel) {
		super(sourceLabel1, sourceLabel2, destinationLabel);
		this.functionalUnit_Type = FunctionalUnit_Type.IU;
		this.instructionType = InstructionType.ARITHMETIC_REG;
	}

	public DADD(DADD obj) {
		super(obj);
	}

	@Override
	public String toString() {
		return "DADD " + dest.getDestinationLabel() + ", "
				+ src1.getSourceLabel() + ", " + src2.getSourceLabel();
	}

	@Override
	public void executeInstruction() {
		dest.setDestination(src1.getSource() + src2.getSource());
	}

}
