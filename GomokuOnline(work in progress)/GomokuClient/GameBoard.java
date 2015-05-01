
import java.util.Arrays;

/**
 * Team One
 * Gomoku
 * CSCE 320 - Spring 2015
 * 3/16/2015
 * Java - JVM
 * Sources:
 * 
 * Revisions:
 * 3/14/2015 - Class created by Joseph Bowley.
 */
public class GameBoard {
    private final int[][] board;
    private final int NOTPLAYED = 0;
    private final int PLAYERONE = 1;
    private final int PLAYERTWO = 2;
    
    /**
     * GameBoard constructor.
     * Passes in dimensions of the board.
     * @param x
     * @param y 
     */
    public GameBoard(int x, int y)
    {
        board = new int[x][y];
        Arrays.fill(board, 0);
        
    }
    
    /**
     * Makes a move on the board for one of the two players active in a game.
     * If player is true, the user is making the move.
     * If player is false, an opponent is making the move.
     * @param x
     * @param y
     * @param player 
     */
    public void moveMade(int x, int y, int player)
    {
        board[x][y] = player;
        printBoard();
    }
    
    /**
     * Returns the representation of the gameBoard
     * @return 
     */
    public int[][] getBoard()
    {
        return board;
    }
    
    /**
     * Returns if there is a five in a row on the board.
     * @return 
     */
    public boolean isFiveInARow()
    {
        return false; // not yet made
    }
    public void printBoard()
    {
        Arrays.toString(board);
    }
}
