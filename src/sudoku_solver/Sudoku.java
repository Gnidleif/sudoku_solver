package sudoku_solver;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author timf
 * The Sudoku class attempts to algorithmically solve a 9x9 size sudoku puzzle
 * It uses the recursive backtracking algorithm to do this (https://en.wikipedia.org/wiki/Sudoku_solving_algorithms#Backtracking)
 * The layout for the unfinished sudoku needs to be loaded from a file
 * The solved sudoku is stored in a new file
 * The layout for the unfinished sudoku has the following constraints:
 * 	1. Needs to contain 9 rows
 * 	2. Each row needs to be 9 characters wide
 * 	3. At least 17 numbers needs to be defined before solving
 */
public class Sudoku implements Puzzle {
	public static final int SIZE = 9;
	private final List<String> lines;
	private final int[][] board;
	
	public Sudoku() {
		this.board = new int[SIZE][SIZE];
		this.lines = new ArrayList<String>();
	}
	
	@Override
	public boolean isSolved() {
		for(int i = 0; i < SIZE; i++) {
			if(!this.checkRow(this.board[i][0], i)) {
				return false;
			}
			for(int j = 0; j < SIZE; j++) {
				if(!this.checkCol(this.board[0][j], j)) {
					return false;
				}
				if(!this.checkBox(this.board[i][j], i, j)) {
					return false;
				}
			}
		}
		return true;
	}

	@Override
	public boolean isValid() {
		if(this.lines.size() != SIZE) {
			return false;
		}
		int numCount = 0;
		for(int i = 0; i < this.lines.size(); i++) {
			if(this.lines.get(i).length() != SIZE) {
				return false;
			}
			for(int j = 0; j < this.lines.get(i).length(); j++) {
				char ch = this.lines.get(i).charAt(j);
				if(Character.isDigit(ch)) {
					numCount++;
				}
			}
		}
		if(numCount < 17) {
			return false;
		}
		return true;
	}

	@Override
	public Puzzle solve() {
		// iterate each row
		for(int row = 0; row < SIZE; row++) {
			// iterate each column
			for(int col = 0; col < SIZE; col++) {
				// a cell with the value 0 signifies that the cell is mutable
				if(this.board[row][col] == 0) {
					// iterate 1-9 in order to find a number
					for(int num = 1; num <= 9; num++) {
						// check each row, column and box for the specific number
						if(this.checkAll(num, row, col)) {
							// set the cell to the specified number and run the solve-function again
							this.board[row][col] = num;
							if(this.solve() != null) {
								return this;
							} else {
								// if the given number turned out to be wrong after the recursive check, set the cell to 0 again
								this.board[row][col] = 0;
							}
						}
					}
					return null;
				}
			}
		}
		return this;
	}
 
	@Override
	public void load(String path)  {
		try (BufferedReader br = new BufferedReader(new FileReader(path))) {
			String line;
			while((line = br.readLine()) != null) {
				this.lines.add(line);
			}
			if(this.isValid()) {
				for(int i = 0; i < this.lines.size(); i++) {
					for(int j = 0; j < this.lines.get(i).length(); j++) {
						char ch = this.lines.get(i).charAt(j);
						if(ch != '.') {
							this.board[i][j] = Character.getNumericValue(ch);
						}
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void save(String path) {
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(path))) {
			for(int i = 0; i < SIZE; i++) {
				String line = "";
				for(int y = 0; y < SIZE; y++) {
					line += this.board[i][y];
				}
				bw.write(line + System.lineSeparator());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * checkRow attempts to find the number within the row of its supplied cell
	 * @param num is the suggested number (1-9) to check the validity of 
	 * @param row is the current row number
	 * @return true if the given number does exist in the row of the cell
	 */
	private boolean checkRow(int num, int row) {
		for(int i = 0; i < SIZE; i++) {
			if(this.board[row][i] == num) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * checkCol attempts to find the number within the column of its supplied cell
	 * @param num is the suggested number (1-9) to check the validity of 
	 * @param col is the current column number
	 * @return true if the given number does exist in the column of the cell
	 */
	private boolean checkCol(int num, int col) {
		for(int i = 0; i < SIZE; i++) {
			if(this.board[i][col] == num) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * checkBox attempts to find the number within the box of its supplied cell
	 * @param num is the suggested number (1-9) to check the validity of 
	 * @param row is the current row number
	 * @param col is the current column number
	 * @return true if the given number does exist within the box of the cell
	 */
	private boolean checkBox(int num, int row, int col) {
		int startX = row - row % 3;
		int startY = col - col % 3;
		for(int i = startX; i < startX + 3; i++) {
			for(int j = startY; j < startY + 3; j++) {
				if(this.board[i][j] == num) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * checkAll is a convenience function used to check row, column and box all at once
	 * @param num is the suggested number (1-9) to check the validity of 
	 * @param row is the current row number
	 * @param col is the current column number
	 * @return true if the suggested number doesn't already exist in the row, column or box
	 */
	private boolean checkAll(int num, int row, int col) {
		return !this.checkBox(num, row, col) && !this.checkRow(num, row) && !this.checkCol(num, col);
	}
}
