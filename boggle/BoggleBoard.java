import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class BoggleBoard 
{
	private static char[][] board;
	private static int size;
	
	/**
	 * This is the constructor for the boggle board, and it
	 * loads the characters in a text file into a 2 dimensional
	 * array.
	 * @param filename the file you want to load, containing the
	 * boggle board.
	 */
	public BoggleBoard(String filename)
	{
		Scanner infile = null;
		try {
			infile = new Scanner(new File(filename));
		} catch (FileNotFoundException e) {
			System.out.println("file not found " + filename);
			e.printStackTrace();
		}
		
		size = infile.nextInt();
		board = new char[size][size];
		infile.nextLine();
		for(int i = 0; i < size; i++)
		{
			for(int j = 0; j < size; j++)
				board[i][j] = infile.next().charAt(0);
		}
	}
	
	/**
	 * This method generates a boggleboard with random letters. it will be named board2.txt
	 * note: to use this, edit the boggleboard declaration in BoggleGame.java to "board2.txt".
	 */
	public static void generateBoard()
	{
		PrintWriter outfile = null;
		try {
			outfile = new PrintWriter("board2.txt");
			outfile.println("4");
			for(int i = 0; i < 16; i++)
				outfile.print((char)((int)'A'+Math.random()*((int)'Z'-(int)'A'+1)) + " ");
		} catch (FileNotFoundException e) {
			System.out.println("file not found ");
			e.printStackTrace();
		}
		outfile.close();
	}
	
	/**
	 * This method displays the boggle board.
	 */
	public void display()
	{
		System.out.println("+---------+");
		for(int i = 0; i < size; i++)
		{
			System.out.print("| ");
			for(int j = 0; j < size; j++)
			{
				System.out.print(board[i][j] + " ");
				if(j % (size-1) == 0 && j != 0)
				{
					System.out.print("|");
					System.out.println();
				}
			}
		}
		System.out.println("+---------+");
	}
	
	/**
	 * This method searches for the word that the user inputs.
	 * It checks to see if r and c are legal values, and if the first character
	 * it looks at is the first letter of the desired word. If it is, the method
	 * recursively searches for the remaining letters of the word, backing up if
	 * a letter leads to a dead end.
	 * @param r the row of the boggle board
	 * @param c the column of the boggle board
	 * @param w the desired word
	 * @return true if the word is found, false otherwise.
	 */
	private boolean findWord(int r, int c, String w)
	{
		if(w.length() == 0)
			return true;
		if(r < 0 || r >= size || c < 0 || c >= size)
			return false;
		if(w.charAt(0) != board[r][c])
			return false;
		
		if(w.charAt(0) == board[r][c])
		{
			board[r][c] = '?';
			String w1 = w.substring(1);
			
			if(findWord(r, c-1, w1))
			{
				board[r][c] = w.charAt(0);
				return true;
			}
			if(findWord(r, c+1, w1))
			{
				board[r][c] = w.charAt(0);
				return true;
			}
			if(findWord(r-1, c, w1))
			{
				board[r][c] = w.charAt(0);
				return true;
			}
			if(findWord(r+1, c, w1))
			{
				board[r][c] = w.charAt(0);
				return true;
			}
			if(findWord(r-1, c-1, w1))
			{
				board[r][c] = w.charAt(0);
				return true;
			}
			if(findWord(r-1, c+1, w1))
			{
				board[r][c] = w.charAt(0);
				return true;
			}
			if(findWord(r+1, c-1, w1))
			{
				board[r][c] = w.charAt(0);
				return true;
			}
			if(findWord(r+1, c+1, w1))
			{
				board[r][c] = w.charAt(0);
				return true;
			}
		}
		board[r][c] = w.charAt(0);
		return false;
	}
	
	/**
	 * this method calls the other findWord method to test if
	 * the character it is looking at matches the first letter of
	 * the desired word.
	 * @param word the desired word.
	 * @return true if the word is found, false otherwise.
	 */
	public boolean findWord(String word)
	{
		for(int i = 0; i < size; i++)
			for(int j = 0; j < size; j++)
				if(findWord(i, j, word))
					return true;
		return false;
	}
	
	/**
	 * this method calculates the score earned based on the length of the word entered.
	 * @param w the word entered.
	 * @return the number of points earned depending on the length of the word.
	 */
	public int score(String w)
	{
		if(w.length() < 3)
			return 0;
		else if(w.length() == 3 || w.length() == 4)
			return 1;
		else if(w.length() == 5)
			return 2;
		else if(w.length() == 6)
			return 3;
		else if(w.length() == 7)
			return 5;
		else
			return 11;
	}
}
