package instructions;

import java.util.List;

public class HLT2 extends DI {
	public HLT2() {
		super();
		this.instructionType = InstructionType.HALT;
	}

	public HLT2(HLT2 obj) {
		super(obj);
		setPrintableInstruction(obj.printableInstruction);
	}

	@Override
	public List<SourceObject> getSourceRegister() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public WriteBackObject getDestinationRegister() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toString() {
		return "HLT2";
	}

	@Override
	public void executeInstruction() {
		// Do nothing here

	}

}
