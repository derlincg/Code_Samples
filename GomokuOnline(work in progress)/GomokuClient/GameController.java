/**
 * This class will manage game play set up.
 */
/**
 * Team One Gomoku CSCE 320 - Spring 2015 3/16/2015 Java - JVM Sources:
 *
 * Revisions: 3/14/2015 - Class created by Joseph Bowley.
 */
public class GameController {

    private GameBoard board;
    private GameAI ai;
    private ClientModel model;
    private GameView view;
    private GameModel gameModel;

    public void setModel(ClientModel m) {
        this.model = m;
    }

    public void setView(GameView v) {
        this.view = v;
    }

    void newGame() {
        System.out.println("NEW GAME METHOD");
        
    }
    
    public void setGameModel(GameModel gm)
    {
        this.gameModel = gm;
    }
    
    public void setGameBoard(GameBoard gb)
    {
        this.board = gb;
    }
    
    public void setAI(GameAI ai)
    {
        this.ai = ai;
    }
    
    public void moveMade(int x, int y, int player)
    {
        gameModel.moveMade(x, y, player);
    }
}
