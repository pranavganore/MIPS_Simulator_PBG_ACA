package instructions;

import functionalUnits.FunctionalUnit_Type;

public class SW extends StoreInstruction {

	public SW(String sourceLabel, String sourceLabel2, int immediate) {
		super(sourceLabel, sourceLabel2, immediate);
		this.functionalUnit_Type = FunctionalUnit_Type.IU;
		this.instructionType = InstructionType.MEMORY_REG;
	}

	public SW(SW obj) {
		super(obj);
	}

	@Override
	public String toString() {
		return "SW " + src1.getSourceLabel() + ", " + immediate + "("
				+ src2.getSourceLabel() + ")";
	}
}
