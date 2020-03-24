package instructions;

import functionalUnits.FunctionalUnit_Type;

public class SD extends StoreInstruction {

	public SD(String sourceLabel, String sourceLabel2, int immediate) {
		super(sourceLabel, sourceLabel2, immediate);
		this.functionalUnit_Type = FunctionalUnit_Type.IU;
		this.instructionType = InstructionType.MEMORY_FPREG;
//		this.addressDouble = this.address + 4;
	}

	public SD(SD obj) {
		super(obj);
	}

	@Override
	public String toString() {
		return "SD " + src1.getSourceLabel() + ", " + immediate + "("
				+ src2.getSourceLabel() + ")";
	}
//	@Override
//	public void executeInstruction() {
//		this.address = immediate + src1.getSource();
//		this.addressDouble = this.address + 4;
//	}
}
