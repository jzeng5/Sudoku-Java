package sudoku;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import sudoku.SudokuFile;
import cspSolver.BTSolver;
import cspSolver.BTSolver.ConsistencyCheck;
import cspSolver.BTSolver.ValueSelectionHeuristic;
import cspSolver.BTSolver.VariableSelectionHeuristic;

public class SolverStats {
	public static long TOTAL_START;
	public static long PREPROCESSING_START;
	public static long PREPROCESSING_DONE;
	public static long SEARCH_START;
	public static long SEARCH_DONE;
	public static long SOLUTION_TIME;
	public static String STATUS;
	public static String SOLUTION;
	public static int COUNT_NODES;
	public static int COUNT_DEADENDS;
	public static String TABLE_SOLUTION; //TODO: formated solution
	public static int TIME_OUT;
	
	public static void recordTotalTime() {
		TOTAL_START = System.currentTimeMillis() / 1000;
	}
	
	public static void recordPrepStart() {
		PREPROCESSING_START = System.currentTimeMillis() / 1000;
	}
	
	public static void recordPrepDone() {
		PREPROCESSING_DONE = System.currentTimeMillis() / 1000;
	}
	
	public static void recordSearchDone() {
		SEARCH_DONE = System.currentTimeMillis() / 1000;
	}
	
	public static void setTimeOut(int timeOut) {
		TIME_OUT = timeOut;
	}
	public static void generateStats(BTSolver solver, String outputFile) {
		SEARCH_START = solver.getStartTime() / 1000;
		//SEARCH_DONE = solver.getEndTime() / 1000;
		SOLUTION_TIME = (PREPROCESSING_DONE - PREPROCESSING_START) 
				+ (SEARCH_DONE - SEARCH_START);
		
		if(solver.hasSolution()) {
			STATUS = "success";
			SOLUTION = solver.getSolution().toTuple();
		} else if ((SEARCH_DONE - SEARCH_START) >= TIME_OUT) {
			STATUS = "timeout";
			SOLUTION = solver.getSolution().zeroTuple();
		} else {
			STATUS = "error";
			SOLUTION = solver.getSolution().zeroTuple();
		}
		
		COUNT_NODES = solver.getNumAssignments();
		COUNT_DEADENDS = solver.getNumBacktracks();
		//TABLE_SOLUTION = solver.getSolution().toString();
		
		writeStats(outputFile);
	}
	
	public static void writeStats(String outputFile)
	{
		String sep = System.getProperty("line.separator");
		File results = new File(outputFile);
		while(results.exists()) {
			System.out.println("Your ouput file name already exit");
			return;
		}
		
		try {
			FileWriter fw = new FileWriter(results);
			
			fw.write("TOTAL_START=" + Long.toString(TOTAL_START) + sep);
			fw.write("PREPROCESSING_START=" + Long.toString(PREPROCESSING_START) + sep);
			fw.write("PREPROCESSING_DONE="+Long.toString(PREPROCESSING_DONE)+sep);
			fw.write("SEARCH_START="+Long.toString(SEARCH_START)+sep);
			fw.write("SEARCH_DONE="+Long.toString(SEARCH_DONE)+sep);
			fw.write("SOLUTION_TIME="+Long.toString(SOLUTION_TIME)+sep);
			fw.write("STATUS="+STATUS+sep);
			fw.write("SOLUTION="+SOLUTION+sep);
			fw.write("COUNT_NODES="+Integer.toString(COUNT_NODES)+sep);
			fw.write("COUNT_DEADENDS="+Integer.toString(COUNT_DEADENDS)+sep);
			//fw.write("!DEBUG:" + TABLE_SOLUTION);
			fw.flush();
			fw.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}