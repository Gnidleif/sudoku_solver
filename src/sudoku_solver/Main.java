package sudoku_solver;

public class Main {
	public static void main(String[] args) {
		Sudoku s = new Sudoku();
		s.load("./example1.txt");
		if(!s.isValid()) {
			System.out.println("supplied sudoku is invalid");
		}
		Puzzle p = s.solve();
		if(p != null && p.isSolved()) {
			System.out.println("sudoku was solved and saved in output.txt");
			s.save("./output.txt");
		} else {
			System.out.println("sudoku is unsolvable!");
		}
	}
}
