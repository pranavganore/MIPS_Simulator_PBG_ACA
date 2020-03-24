package stages;
/*
 * CMSC-611 Advanced Computer Architecture - Final Project (Fall - 2019)
 * Author : Pranav B Ganore [YI73732]
 * pganore1@umbc.edu
*/

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import org.apache.commons.collections4.ListUtils;

import functionalUnits.FP_FUNCTIONAL_UNIT;
import functionalUnits.FUNCTIONAL_UNIT;
import functionalUnits.FunctionalUnit_Type;
import functionalUnits.MEMORY_UNIT;
import instructions.DI;

public class Ex_Stage extends Stage {

	private static volatile Ex_Stage instance;

	public static Ex_Stage getInstance() {

		if (null == instance)
			synchronized (Ex_Stage.class) {
				if (null == instance)
					instance = new Ex_Stage();
			}

		return instance;
	}

	private functionalUnits.INTEGER_UNIT iu;
	private functionalUnits.MEMORY_UNIT mem;
	private functionalUnits.FP_ADDER_UNIT fpadd;
	private functionalUnits.FP_MULTIPLIER_UNIT fpmul;
	private functionalUnits.FP_DIVISOR_UNIT fpdiv;
	private List<FUNCTIONAL_UNIT> tieBreakerList;

	private Ex_Stage() {
		super();

		this.stage_Type = Stage_Type.EXSTAGE;

		iu = functionalUnits.INTEGER_UNIT.getInstance();
		mem = functionalUnits.MEMORY_UNIT.getInstance();
		fpadd = functionalUnits.FP_ADDER_UNIT.getInstance();
		fpmul = functionalUnits.FP_MULTIPLIER_UNIT.getInstance();
		fpdiv = functionalUnits.FP_DIVISOR_UNIT.getInstance();

		tieBreakerList = new ArrayList<FUNCTIONAL_UNIT>();
		tieBreakerList.add(mem);
		tieBreakerList.add(fpadd);
		tieBreakerList.add(fpmul);
		tieBreakerList.add(fpdiv);
	}

	@Override
	public void run() throws Exception {

		List<FUNCTIONAL_UNIT> readyList = new ArrayList<FUNCTIONAL_UNIT>();

		for (FUNCTIONAL_UNIT fu : tieBreakerList) {
			if (fu.isReadyToSend())
				readyList.add(fu);
		}

		if (readyList.size() <= 1) {
			for (FUNCTIONAL_UNIT fu : tieBreakerList)
				fu.executeUnit();
		} else {
			List<FUNCTIONAL_UNIT> winnerList = new ArrayList<FUNCTIONAL_UNIT>();
			winnerList.add(tieBreaker(readyList));

			if (winnerList.size() == 0)
				throw new Exception(
						"ExStage: units said ready to send but winnerslist is empty");

			List<FUNCTIONAL_UNIT> losersList = ListUtils.subtract(readyList,
					winnerList);

			List<FUNCTIONAL_UNIT> exeList = ListUtils.subtract(tieBreakerList,
					losersList);

			// for all losers, run mark StructHazard
			for (FUNCTIONAL_UNIT fu : losersList) {
				fu.markStructHazard();
				// TODO for pipelined FPFunctionalUnit, move things 1 right

				if (fu instanceof FP_FUNCTIONAL_UNIT)
					((FP_FUNCTIONAL_UNIT) fu).rotatePipelineOnHazard();

			}
			// for exeList, execute
			for (FUNCTIONAL_UNIT fu : exeList)
				fu.executeUnit();
		}

		iu.executeUnit(); // Special Handling for this

	}

	// This method will be called by ID while executing and passing on the
	// instruction
	@Override
	public boolean acceptInstruction(DI instruction) throws Exception {
		// TODO Implement this method
		FUNCTIONAL_UNIT fUNCTIONAL_UNIT = getFunctionalUnit(instruction);
		if (!fUNCTIONAL_UNIT.checkIfFree(instruction))
			throw new Exception("EXSTAGE: Illegal state exception "
					+ instruction.toString());

		fUNCTIONAL_UNIT.acceptInstruction(instruction);

		return true;
	}

	// This method will be called by ID while executing and passing on the
	// instruction, and check for STRUCT hazard
	@Override
	public boolean checkIfFree(DI instruction) throws Exception {
		FUNCTIONAL_UNIT fUNCTIONAL_UNIT = getFunctionalUnit(instruction);
		return fUNCTIONAL_UNIT.checkIfFree(instruction);
	}

	/**
	 * 
	 * @param instruction
	 *            to find which FU to use
	 * @return
	 * @throws Exception
	 *             defensive
	 */
	@SuppressWarnings("incomplete-switch")
	private FUNCTIONAL_UNIT getFunctionalUnit(DI instruction) throws Exception {

		if (instruction.functionalUnit_Type == FunctionalUnit_Type.UNKNOWN
				|| instruction.functionalUnit_Type == null)
			throw new Exception("EXSTAGE: Incorrect type"
					+ instruction.toString());

		switch (instruction.functionalUnit_Type) {

		case FPADD:
			return fpadd;

		case FPDIV:
			return fpdiv;

		case FPMUL:
			return fpmul;

		case IU:
			return iu;
		}

		return null;
	}

	private FUNCTIONAL_UNIT tieBreaker(List<FUNCTIONAL_UNIT> tieList) {
		TreeMap<Integer, FUNCTIONAL_UNIT> fUMap = new TreeMap<Integer, FUNCTIONAL_UNIT>();

		for (FUNCTIONAL_UNIT fu : tieList) {

			if (fu instanceof MEMORY_UNIT) {
				mergeFUMap(fu.clockCyclesRequired + 1, fu, fUMap);
				continue;
			}

			if (fu.isPipelined) {
				mergeFUMap(fu.clockCyclesRequired, fu, fUMap);
			} else {
				mergeFUMap(1000 + fu.clockCyclesRequired, fu, fUMap);
			}
		}

		return fUMap.get(fUMap.lastKey());
	}

	private void mergeFUMap(int calculatedKey, FUNCTIONAL_UNIT fu,
			TreeMap<Integer, FUNCTIONAL_UNIT> map) {

		if (map.containsKey(calculatedKey)) {

			FUNCTIONAL_UNIT mapEntry = map.get(calculatedKey);
			int fuEntry = fu.peekFirst().entryCycle[Stage_Type.IFSTAGE.getId()];
			int localEntry = mapEntry.peekFirst().entryCycle[Stage_Type.IFSTAGE
					.getId()];
			if (fuEntry < localEntry)
				map.put(calculatedKey, fu);

		} else {
			map.put(calculatedKey, fu);
		}
	}

}
