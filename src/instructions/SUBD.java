package instructions;

import functionalUnits.FunctionalUnit_Type;

public class SUBD extends Type3Reg {
	public SUBD(String sourceLabel1, String sourceLabel2,
			String destinationLabel) {
		super(sourceLabel1, sourceLabel2, destinationLabel);
		this.functionalUnit_Type = FunctionalUnit_Type.FPADD;
		this.instructionType = InstructionType.ARITHMETIC_FPREG;
	}

	public SUBD(SUBD obj) {
		super(obj);
	}

	@Override
	public String toString() {
		return "SUBD " + dest.getDestinationLabel() + ", "
				+ src1.getSourceLabel() + ", " + src2.getSourceLabel();
	}

	@Override
	public void executeInstruction() {
		dest.setDestination(src1.getSource() - src2.getSource());
	}

}
