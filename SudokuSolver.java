import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *	SudokuSolver - Solves an incomplete Sudoku puzzle using recursion and backtracking
 *
 *	@author	Charles Chang
 *	@since	23 November, 2024
 *
 */
public class SudokuSolver {

	private int[][] puzzle;		// the Sudoku puzzle
	
	private String PUZZLE_FILE = "puzzle1.txt";	// default puzzle file
	
	/* Constructor */
	public SudokuSolver() {
		puzzle = new int[9][9];
		// fill puzzle with zeros
		for (int row = 0; row < puzzle.length; row++)
			for (int col = 0; col < puzzle[0].length; col++)
				puzzle[row][col] = 0;
	}
	
	public static void main(String[] args) {
		SudokuSolver sm = new SudokuSolver();
		sm.run(args);
	}
	
	public void run(String[] args) {
		// get the name of the puzzle file
		String puzzleFile = PUZZLE_FILE;
		if (args.length > 0) puzzleFile = args[0];
		
		System.out.println("\nSudoku Puzzle Solver");
		// load the puzzle
		System.out.println("Loading puzzle file " + puzzleFile);
		loadPuzzle(puzzleFile);
		printPuzzle();
		// solve the puzzle starting in (0,0) spot (upper left)
		solvePuzzle(0, 0);
		printPuzzle();
	}
	
	/**	Load the puzzle from a file
	 *	@param filename		name of puzzle file
	 */
	public void loadPuzzle(String filename) {
		Scanner infile = FileUtils.openToRead(filename);
		for (int row = 0; row < 9; row++)
			for (int col = 0; col < 9; col++)
				puzzle[row][col] = infile.nextInt();
		infile.close();
	}
	
	/**	Solve the Sudoku puzzle using brute-force method. 
	 * 	@param	int		row
	 * 	@param	int		col
	 * 	@param	boolean		does the previous solution work?
	 */
	public boolean solvePuzzle(int row, int col) {
		//	Base case
		if (row == 8 && col > 8) return true;
		//	Next row
		if (row < 8 && col > 8) return solvePuzzle(row + 1, 0);
		//	Check if the current square is a starting square
		if (puzzle[row][col] != 0) return solvePuzzle(row, col + 1);
		
		//	Array to keep track of which numbers were used
		boolean[] usedNums = new boolean[9];
		//	Keep track of whether number is valid
		boolean isValid = false;
		//	Keep trying different numbers until true is returned
		while (!isValid) {
			boolean areNumsUsed = true;
			//	If all numbers used, return false
			for (int i = 0; i < 9; i++) 
				if (!usedNums[i])
					areNumsUsed = false;
			if (areNumsUsed) {
				puzzle[row][col] = 0;
				return false;
			}
			//	Random number
			int rand = randNum(usedNums);
			//	Note used number
			usedNums[rand - 1] = true;
			//	Set
			puzzle[row][col] = rand;
			//	Check if number matches row, col, and 3 x 3
			if (!isDupe(row, col)) {
				//	If next square is true, this is valid
				isValid = solvePuzzle(row, col + 1);
			}
			else puzzle[row][col] = 0;
		}
		return true;
	}
	
	/**
	 * 	Check if the the number is doubled in its column, row, or 3x3
	 * 	@param	int		row
	 * 	@param	int		col
	 * 	@return	boolean	is number duplicate
	 */
	public boolean isDupe(int row, int col) {
		//	Check row
		for (int i = 0; i < 9; i++)
			if (i != col && puzzle[row][i] == puzzle[row][col])
				return true;
		//	Check col
		for (int i = 0; i < 9; i++)
			if (i != row && puzzle[i][col] == puzzle[row][col])
				return true;
		//	Check local 3x3
		for (int i = (int)(row / 3) * 3; i < (int)(row / 3) * 3 + 3; i++)
			for (int j = (int)(col / 3) * 3; j < (int)(col / 3) * 3 + 3; j++)
				if (i != row && j != col && puzzle[i][j] == puzzle[row][col])
					return true;
		return false;
	}
	
	/**
	 * 	Generate a random number from 1 - 9, and make sure no repeats
	 * 	@param	boolean[]	which numbers were used
	 * 	@return	int			random number
	 */
	public int randNum(boolean[] usedNums) {
		//	Declarations
		boolean isValid = false;
		int out;
		//	Keep randomly generating then checking until valid
		while (!isValid) {
			out = (int)(Math.random() * 9) + 1;
			if (usedNums[out - 1] == false) return out;
		}
		return -1;
	}
	
		
	/**
	 *	printPuzzle - prints the Sudoku puzzle with borders
	 *	If the value is 0, then print an empty space; otherwise, print the number.
	 */
	public void printPuzzle() {
		System.out.print("  +-----------+-----------+-----------+\n");
		String value = "";
		for (int row = 0; row < puzzle.length; row++) {
			for (int col = 0; col < puzzle[0].length; col++) {
				// if number is 0, print a blank
				if (puzzle[row][col] == 0) value = " ";
				else value = "" + puzzle[row][col];
				if (col % 3 == 0)
					System.out.print("  |  " + value);
				else
					System.out.print("  " + value);
			}
			if ((row + 1) % 3 == 0)
				System.out.print("  |\n  +-----------+-----------+-----------+\n");
			else
				System.out.print("  |\n");
		}
	}
}
