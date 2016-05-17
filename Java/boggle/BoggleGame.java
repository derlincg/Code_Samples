/*Name: Carl Derline
  Assignment: Lab 8
  Title: Boggle
  Course: CSCE 270
  Lab Section: 2
  Semester: Spring, 2014
  Instructor: Kenneth Blaha
  Date: 4-23-14
  Sources consulted: tutor for boggleBoard display()
  Program description: This program is Boggle! run the program and enter any word you
  					   see on the board. score as many points as you can! To end the game,
  					   press enter without entering anything.
  Known Bugs: none
  */
import java.util.ArrayList;
import java.util.Scanner;

public class BoggleGame
{
	private static BoggleBoard myBoard;
	private static Dictionary myDictionary;
	static Scanner scan = new Scanner(System.in);
	
	/**
	 * main method. creates and prints a new board, initializes the dictionary, and lets the user play the game.
	 */
	public static void main(String[] args) 
	{
		BoggleBoard.generateBoard();
		ArrayList<String> usedWords = new ArrayList<String>();
		myDictionary = new Dictionary("words.txt"); //load the dictionary
		myBoard = new BoggleBoard("board.txt"); //load the board: board.txt for standard board, board2.txt for random board
		myBoard.display(); //display the board
		int score = 0;
		
		System.out.println();
		System.out.print("\nEnter a word (or enter nothing at any point to end the game): ");
		String input = scan.nextLine();
		input = input.toLowerCase();
		
		while(!input.equals("")) //user inputs a word
		{
			if(input.length() < 3) //words less than 3 letters don't count
				System.out.println("That word is too short.");
			else if(Dictionary.search(input, 0, myDictionary.size-1) < 0) //user made up a word
				System.out.println("That word is not in the dictionary.");
			else if(!myBoard.findWord(input.toUpperCase())) //word not on the board
				System.out.println("That word is a valid word, but is not on the board.");
			else // myDictionary.search() && myBoard.findWord(input)
			{
				if(!usedWords.contains(input))
				{
					//account for grammar
					if(myBoard.score(input) == 1)
					{
						System.out.println("That is a good word! You score " + myBoard.score(input) + " point!");
						score += myBoard.score(input);
						usedWords.add(input); //add searched words to usedWords to make sure they cant be used again
					}
					else
					{
						System.out.println("That is a good word! You score " + myBoard.score(input) + " points!");
						score += myBoard.score(input);
						usedWords.add(input);
					}
				}
				else
					System.out.println("That word was already used.");
			}
			
			System.out.println();
			myBoard.display();
			
			System.out.print("Used words: ");
			for(String d : usedWords)
				System.out.print(d + ", ");
			
			System.out.print("\nEnter a word: ");
			input = scan.nextLine();
			input = input.toLowerCase();
			
		}
		System.out.println("Total score: " + score); //end of the game.
		System.out.println("Thanks for playing!");
	}
}
