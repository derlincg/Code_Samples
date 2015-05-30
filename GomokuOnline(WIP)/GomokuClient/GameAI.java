import java.util.Random;

/**

 */
public class GameAI {
    private GameBoard board;
    private int difficulty;
    private GameModel model;
    private Constants consts = new Constants();
    private Random randomGenerator = new Random();
    private GameController controller;
    
    /**
     * Game AI Constructor
     * @param gc the game controller
     * @param difficulty 
     */
    public GameAI(String diff, GameController gc)
    {
    	System.out.println(diff);
        if(diff.equals("Easy")){
        	difficulty = 0;
        }else if(diff.equals("Moderate")){
        	difficulty = 1;
        }else if(diff.equals("Hard")){
        	difficulty = 2;
        }
        model = new GameModel();
        controller = gc;
        model.setTurnOrder(false);
    }
    
	/**
	 * associates game board with the GameAI class
	 * @param board the game board
	 */
    public void setGameBoard(GameBoard board)
    {
        this.board = board;
    }
    
   
    /**
     * Makes a move in the game board as an opponent player. calls the
	 * appropriate analyzeGameBoard depending on the difficulty.
     * @param x the x coordinate
     * @param y the y coordinate
     */
    public void makeMove(int x, int y)
    {
    	if(difficulty == 0){
    		System.out.println("Easy difficulty selected");
	        int i = randomGenerator.nextInt(30);
	        int j = randomGenerator.nextInt(30);
	        boolean flag = model.isValid(i, j);
	        while(!flag){
	        	i = randomGenerator.nextInt(30);
	            j = randomGenerator.nextInt(30);
	            flag = model.isValid(i, j);
	        }
	        
	        model.makeMove(i,j);
	        controller.aiMove(i, j);
	     
    	}
    	if (difficulty == 1){
    		analyzeGameboardMedium(x, y);
    	}
    	if (difficulty == 2){
	    	 analyzeGameboardHard(x , y);
	    	 
    	}
    }

	/**
	 * Analyzes the gameboard and determines the direction of the longest
	 * number of pieces the opponent has in a row using our search methods. 
	 * calls aiMakeMove with that direction.
	 * @param int x the x coordinate
	 * @param int y the y coordinate
	 */
	private void analyzeGameboardHard(int x, int y) {
		String direction = "";
		int max = 0;
		if(horizontalSearch(x,y)> max){
			max = horizontalSearch(x,y);
			direction = "h";
		}
		if(verticalSearch(x,y) > max) {
			max = verticalSearch(x,y);
			direction = "v";
		}
		if(diagonalFowardSearch(x,y)> max){
			max = diagonalFowardSearch(x,y);
			direction = "df";
		}
		if(diagonalBackSearch(x,y)> max){
			max = diagonalBackSearch(x,y);
			direction = "db";
		}
		System.out.println(direction);
		
		
		aiMakeMove(x,y,direction);
		
	}
	
	/**
	 * The AI makes a move based on the direction that is passed in.
	 * The method searches the board in that direction until a blank
	 * piece or an opponent piece is encountered. if a blank is encountered,
	 * the AI places its piece there, else it puts its piece at the other end
	 * of the opponents row.
	 * @param int x the x coordinate
	 * @param int y the y coordinate
	 * @param String dir the direction passed in from analyzeGameBoard;
	 */
	public void aiMakeMove(int x, int y, String dir)
	{
		boolean flag = true;
		int i = x - 1, j = y;
		if(dir.equals("h")){
			while ( i >= 0 && model.getBoardValue(i, y) == 1) {
				i--;
        	}
			if(i >= 0 && model.getBoardValue(i, y) == 0){
				model.makeMove(i, y);
				controller.aiMove(i, y);
				flag = false;
			}else{
				i = x+1;
				while(flag && i < 30 && model.getBoardValue(i, y) == 1){
					i++;
	        	}
				if(i < 30 && model.getBoardValue(i, y) == 0){
					model.makeMove(i, y);
					controller.aiMove(i, y);
					flag = false;
				}
			}
		
		}
		i = x;
		j = y-1;
		if(dir.equals("v")){
			while ( j >= 0 && model.getBoardValue(i, j) == 1) {
				j--;
        	}
			if(i >= 0 && model.getBoardValue(i, j) == 0){
				model.makeMove(i, j);
				controller.aiMove(i, j);
				flag = false;
			}else{
				j = y + 1;
				while(flag && j < 30 && model.getBoardValue(i, j) == 1){
					j++;
	        	}
				if(j < 30 && model.getBoardValue(i, j) == 0){
					model.makeMove(i, j);
					controller.aiMove(i, j);
					flag = false;
				}
			}
		
		}
		
		i = x +1;
		j = y - 1;
		
		if(dir.equals("df")){
			while ( j >= 0 && i < 30 && model.getBoardValue(i, j) == 1) {
				j--;
				i++;
        	}
			if(j >= 0 && i < 30 && model.getBoardValue(i, j) == 0){
				model.makeMove(i, j);
				controller.aiMove(i, j);
				flag = false;
			}else{
				i = x - 1;
				j = y + 1;
				while(flag && i >=0 && j < 30 && model.getBoardValue(i, j) == 1){
					j++;
					i--;
	        	}
				if(i >=0 && j < 30 && model.getBoardValue(i, j) == 0){
					model.makeMove(i, j);
					controller.aiMove(i, j);
					flag = false;
				}
			}
		
		}
		
		i = x - 1;
		j = y - 1;
		
		if(dir.equals("db")){
			while ( j >= 0 && i >= 0 && model.getBoardValue(i, j) == 1) {
				j--;
				i--;
        	}
			if( j >= 0 && i >= 0 && model.getBoardValue(i, j) == 0){
				model.makeMove(i, j);
				controller.aiMove(i, j);
				flag = false;
			}else{
				i = x + 1;
				j = y + 1;
				while(flag && i < 30 && j < 30 && model.getBoardValue(i, j) == 1){
					j--;
					i--;
	        	}
				if(i < 30 && j < 30 && model.getBoardValue(i, j) == 0){
					model.makeMove(i, j);
					controller.aiMove(i, j);
					flag = false;
				}
			}
		
		}
		
		if(flag){
			System.out.println("Easy difficulty selected");
	        i = randomGenerator.nextInt(30);
	        j = randomGenerator.nextInt(30);
	        boolean valid = model.isValid(i, j);
	        while(!valid){
	        	i = randomGenerator.nextInt(30);
	            j = randomGenerator.nextInt(30);
	            valid = model.isValid(i, j);
	        }
	        
	        model.makeMove(i,j);
	        controller.aiMove(i, j);
		}
		
	}

	/**
	 * Analyzes the gameboard and determines the direction of the longest
	 * number of pieces the opponent has in a row. Generates a random integer
	 * and one quarter of the time, the AI will make a random move instead of
	 * blocking the opponent. This is our "Medium" difficulty. calls aiMakeMove.
	 * @param int x the x coordinate
	 * @param int y the y coordinate
	 */
	private void analyzeGameboardMedium(int x, int y) {
		String direction = "";
		int max = 0;
		int rand = randomGenerator.nextInt(4);
		if(rand<3){
			if(horizontalSearch(x,y)> max){
				max = horizontalSearch(x,y);
				direction = "h";
			}
			if(verticalSearch(x,y) > max) {
				max = verticalSearch(x,y);
				direction = "v";
			}
			if(diagonalFowardSearch(x,y)> max){
				max = diagonalFowardSearch(x,y);
				direction = "df";
			}
			if(diagonalBackSearch(x,y)> max){
				max = diagonalBackSearch(x,y);
				direction = "db";
			}
		}
		
		aiMakeMove(x,y,direction);
	}

	/**
	 * Receives move from the player
	 * @param int x the x coordinate
	 * @param int y the y coordinate
	 */
	public void sendMove(int x, int y) {
		String jonsajerk = model.validateOpponent(x, y);
		if(jonsajerk.equals(consts.VALID)){
			makeMove(x,y);
		}
	}
	
	/**
	 * Horizontal search for counting how many in a row a player has.
	 * Starts on one side of the passed in x,y coordinate and counts
	 * the number of player pieces in a row, then does the same on the
	 * other side of the x,y coordinate.
	 * @param int x the x coordinate
	 * @param int y the y coordinate
	 * @return int result how many pieces a player has in a row.
	 */
	 private int horizontalSearch(int x, int y) {
	        int result = 1;
	        boolean flag = false;
	        int i = x - 1;

	        while ( i >= 0 && model.getBoardValue(i, y) == 2) {
	            result++;
	            i--;
	        }
	     
	        i = x + 1;

	        while (i < 30 && model.getBoardValue(i, y) == 2) {
	            result++;
	            i++;
	        }

	        if (result >= 5) {
	            flag = true;
	        }
	        return result;
	    }
	 
	 /**
	 * Diagonal back search for counting how many in a row a player has.
	 * Starts on one side of the passed in x,y coordinate and counts
	 * the number of player pieces in a row, then does the same on the
	 * other side of the x,y coordinate.
	 * @param int x the x coordinate
	 * @param int y the y coordinate
	 * @return int result how many pieces a player has in a row.
	 */
	 private int diagonalBackSearch(int x, int y) {
	        int result = 1;
	        int i = x - 1;
	        int j = y - 1;

	        while (i >= 0 && j >= 0 && model.getBoardValue(i, j) == 2) {
	            result++;
	            i--;
	            j--;
	        }

	    
	        i = x + 1;
	        j = y + 1;
	        while (i < 30 && j < 30 && model.getBoardValue(i,j)==2) {
	            result++;
	            i++;
	            j++;
	        }
	        return result;
	    }

		/**
		 * Diagonal forward search for counting how many in a row a player has.
		 * Starts on one side of the passed in x,y coordinate and counts
		 * the number of player pieces in a row, then does the same on the
		 * other side of the x,y coordinate.
		 * @param int x the x coordinate
		 * @param int y the y coordinate
		 * @return int result how many pieces a player has in a row.
		 */
	    private int diagonalFowardSearch(int x, int y) {
	        int result = 1;
	        int i = x - 1;
	        int j = y + 1;

	        while (i >= 0 && j < 30 &&  model.getBoardValue(i,j) == 2) {
	       
	            result++;
	            i--;
	            j++;
	        }
	        i = x + 1;
	        j = y - 1;
	        while ( i < 30 && j >= 0 && model.getBoardValue(i,j) == 2) {
	            result++;
	            i++;
	            j--;
	        
	        }

	        return result;
	    }

		/**
		 * Vertical search for counting how many in a row a player has.
		 * Starts on one side of the passed in x,y coordinate and counts
		 * the number of player pieces in a row, then does the same on the
		 * other side of the x,y coordinate.
		 * @param int x the x coordinate
		 * @param int y the y coordinate
		 * @return int result how many pieces a player has in a row.
		 */
	    private int verticalSearch(int x, int y) {
	     
	        int result = 1;
	        int i = y - 1;

	        while (i >= 0 && model.getBoardValue(x,i) == 2) {
	            result++;
	            i--;
	        }

	     
	        i = y + 1;
	        while (i < 30 && model.getBoardValue(x,i) == 2) {
	            result++;
	            i++;
	        }  

	        return result;
	    }
}
