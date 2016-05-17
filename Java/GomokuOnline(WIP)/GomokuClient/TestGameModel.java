
public class TestGameModel {
	 public static void main(String[] args) {
	 
		  GameModel model =  new GameModel();
		  GameBoard board = new GameBoard();
		  model.setGameBoard(board);
		  
		  model.validateSelf(0,0);
		  model.validateSelf(0,2);
		  model.validateSelf(29,29);
		  model.validateOpponent(28,29);
		  model.validateOpponent(29, 29);
		  board.printBoard();
	 }

}
