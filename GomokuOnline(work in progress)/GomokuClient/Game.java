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
public class Game {
    private GameBoard board;
    private GameAI ai;
    
    public Game()
    {
        board = new GameBoard(30, 30);
    }
    
    /**
     * Game Constructor
     * @param it
     */
    public Game(GameAI it)
    {
        
    }
    
    public void setGameBoard(GameBoard gb)
    {
        this.board = gb;
    }
    
    public void setAI(GameAI ai)
    {
        this.ai = ai;
    }
    
    /**
     * Method that will start a game.
     */
    public void playGame()
    {
        
    }
    
    public void moveMade(int x, int y, int player)
    {
        board.moveMade(x, y, player);
    }
}
