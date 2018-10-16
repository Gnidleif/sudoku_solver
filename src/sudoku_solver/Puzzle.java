package sudoku_solver;

/**
 * 
 * @author timf
 * The Puzzle interface defines a set of functions required by any class
 * that's supposed to algorithmically solve a puzzle
 */
public interface Puzzle {
	/**
	 * isSolved checks the current state of the puzzle
	 * @return true if the puzzle is correctly solved
	 */
	boolean isSolved();
	/**
	 * isValid checks if the supplied unsolved puzzle is valid
	 * @return true if the supplied unsolved puzzle meets a specific set of constraints
	 */
	boolean isValid();
	/**
	 * solve attempts to algorithmically solve the puzzle
	 * @return the puzzle itself if the algorithm is successful
	 */
	Puzzle solve();
	/**
	 * load reads a given file and creates an unsolved puzzle from it
	 * @param path points to the specific file required by the puzzle
	 */
	void load(String path);
	/**
	 * save writes the solved puzzle information to a file
	 * @param path points to the file to be created and written to
	 */
	void save(String path);
}