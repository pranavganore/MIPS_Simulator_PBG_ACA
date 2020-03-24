

import Simulator.Execution_Type;
import Simulator.Result_Generator;
import parsers.ConfigFile_Parser;
import parsers.DataFile_Parser;
import parsers.InstFile_Parser;
import parsers.RegFile_Parser;
import stages.Decode_Stage;
import stages.Ex_Stage;
import stages.Fetch_Stage;
import stages.Processor_Params;
import stages.WriteBack_Stage;

public class Simulator {
	/**
	 * 
	 * @param args
	 *            inst.txt data.txt reg.txt config.txt result.txt
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

		
		//	Initializing the CPU parameters
		Processor_Params.CC = 0;		// ClockCycle
		Processor_Params.PC = 0;		// Program Counter
		Processor_Params.exeType = Execution_Type.M;	// Execution Type - M->with Memory (use 'P'->for without memory)
		
		//	Parsing
		InstFile_Parser.parse(args[0]);		//	Parsing inst.txt file
		DataFile_Parser.parse(args[1]);		//	Parsing data.txt file
		RegFile_Parser.parse(args[2]);		//	Parsing reg.txt file
		ConfigFile_Parser.parse(args[3]);	//	Parsing config.txt file
		//Initializing result file
		Result_Generator.instance.setResultsPath(args[4]);

		
//		Initialize singleton instances of all the four stages
		WriteBack_Stage wbStage = WriteBack_Stage.getInstance();	//	Initialize singleton instance of WriteBack Stage
		Ex_Stage ex_Stage = Ex_Stage.getInstance();				//	Initialize singleton instance of Execution Stage
		Decode_Stage idStage = Decode_Stage.getInstance();		//	Initialize singleton instance of Decode Stage
		Fetch_Stage ifStage = Fetch_Stage.getInstance();			//	Initialize singleton instance of Fetch Stage

		try {
			// Extra clock cycles after 2nd HALT to Flush the pipeline (taking extra care)
			int extraCLKCount = 5000;
			while (extraCLKCount != 0) {
				
				wbStage.run();
				ex_Stage.run();

				//Process the 2nd HALT instructions and accordingly execute IS and FT stages
				if ( Result_Generator.instance.isSecondHALT() == false) {	//if 2nd HLT is encountered dont run idStage
					idStage.run();
					
					if (Result_Generator.instance.isSecondHALT() == false) {	//if 2nd HLT is encountered dont run ifStage
						ifStage.run();

					}
				}else
					extraCLKCount--;
				

				Processor_Params.CC++;	//	Increment the Clock Cycle to indicate start of next clock cycle
			}
		} catch (Exception e) {
			System.out.println("ERROR: CLOCK=" + Processor_Params.CC);
			e.printStackTrace();
		} finally {
		}
		Thread.sleep(200L);
		System.out.println("Results");
		Result_Generator.instance.printResults();	//	Print / output result on the Console
		Result_Generator.instance.writeResults();	//	Write the results to result.txt file.

	}
}
