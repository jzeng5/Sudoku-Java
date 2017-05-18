import cspSolver.BTSolver;
import cspSolver.BTSolver.ConsistencyCheck;
import cspSolver.BTSolver.ValueSelectionHeuristic;
import cspSolver.BTSolver.VariableSelectionHeuristic;
import sudoku.SolverStats;
import sudoku.SudokuBoardGenerator;
import sudoku.SudokuBoardReader;
import sudoku.SudokuFile;


public class HeroSudoku {
	public static int timeOut;
	//private 
	
	public static void main(String[] args) {
		// Record the time when program start
		SolverStats.recordTotalTime();
		
		// load file as specified in parameter
		SudokuFile sudokuFile = sudokuFileIni(args);
		
		// initialize solver
		BTSolver solver = new BTSolver(sudokuFile);
		sudokuSet (args,solver);
		String outputFile = args[1];
		
		// AC Preprocessor
		SolverStats.recordPrepStart();
		SolverStats.recordPrepDone();
		
		// solve sudoku in a new thread
		sudokuSolverThread(solver);
		
		// write output file
		SolverStats.generateStats(solver, outputFile);
	}
	
	public static SudokuFile sudokuFileIni(String[] args) {
		String inputFile = args[0];
		try{
			timeOut = Integer.parseInt(args[2]);
		} catch (Exception e) {
			System.out.print("Invalid time-out parameter, use default(=60s) instead");
			timeOut = 60;
		}
		SolverStats.setTimeOut(timeOut);
		
		return SudokuBoardReader.readFile(inputFile);
	}
	
	public static void sudokuSet (String[] args,BTSolver solver){
		boolean check = false;
		if (args.length >= 4){
			for (int index = 3; index < args.length; index++) {
				// set consistency check
				if(args[index].equals("FC")){
					check = true;
					solver.setConsistencyChecks(ConsistencyCheck.ForwardChecking);
				} else if(args[index].equals("ARC")){
					check = true;
					solver.setConsistencyChecks(ConsistencyCheck.ArcConsistency);
				} 
			}
		} 
		
		if (!check) {
			solver.setConsistencyChecks(ConsistencyCheck.None);
		}
		
		solver.setValueSelectionHeuristic(ValueSelectionHeuristic.None);
		solver.setVariableSelectionHeuristic(VariableSelectionHeuristic.None);
			// to do, set heuristic
		
	}
	
	public static void sudokuSolverThread(BTSolver solver){
		// run solver within defined time limit
		Thread t1 = new Thread(solver);
		try {
			t1.start();
			t1.join(timeOut * 1000);
			if(t1.isAlive()) {
				t1.interrupt();
			}
		} catch (InterruptedException e){
		}
		SolverStats.recordSearchDone();
	}
}