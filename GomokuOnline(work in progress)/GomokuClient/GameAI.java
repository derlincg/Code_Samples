import java.util.Random;

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
public class GameAI {
    private GameBoard board;
    private int difficulty;
    private GameModel model;
    private Constants consts = new Constants();
    private Random randomGenerator = new Random();
    private GameController controller;
    
    /**
     * Game AI Constructor
     * @param gm 
     * @param v 
     * @param difficulty 
     */
    public GameAI(String diff, GameController gc)
    {
        if(diff.equals("easy")){
        	difficulty = 0;
        }else if(diff.equals("medium")){
        	difficulty = 1;
        }else if(diff.equals("hard")){
        	difficulty = 2;
        }
        model = new GameModel();
        controller = gc;
        model.setTurnOrder(false);
    }
    
    public void setGameBoard(GameBoard board)
    {
        this.board = board;
    }
    
    /**
     * Method to start the AI
     */
    public void playGame()
    {
        
    }
    
    /**
     * Makes a move in the game board as an opponent player.
     * @param x
     * @param y 
     */
    public void makeMove(int x, int y)
    {
    	String move;
    	//System.out.println(analyzeGameboardHard(x, y));
    	if(difficulty == 0){
    		/*System.out.println("Easy difficulty selected");
	        int i = randomGenerator.nextInt(30);
	        int j = randomGenerator.nextInt(30);
	        boolean flag = model.isValid(i, j);
	        while(!flag){
	        	i = randomGenerator.nextInt(30);
	            j = randomGenerator.nextInt(30);
	            flag = model.isValid(i, j);
	        }
	        move = analyzeGameboardHard(x, y);
	        System.out.println("x: " + x + ", y: " + y);
	        System.out.println("move: " + move);
	        model.makeMove(i,j);
	        controller.aiMove(i, j);*/
	        
    		aiMakeMove(x, y);
    	}
    	if (difficulty == 1){
    		move = analyzeGameboardMedium(x, y);
    	}
    	if (difficulty == 2){
    		System.out.println("hard difficulty selected.");
	    	  //move = analyzeGameboardHard(x , y);
	    	  //aiMakeMove(move);
    	}
    }

	private String analyzeGameboardHard(int x, int y) {
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
		return direction;
		
	}
	
	public void aiMakeMove(int x, int y)
	{
		boolean flag = true;
		int temp = 1, i = x, j = y;
		//System.out.println("x: " + x + ", y: " + y);
		String move = analyzeGameboardHard(x, y);
		System.out.println("Value of move: " + move + " at " + x + " " + y);
		if(move.equals("h"))
		{
			int nextCell = model.getBoardValue(x+1, y);
			while(flag)
			{
				if(nextCell == 2) {
					System.out.println("nextCell = 2");
					nextCell = model.getBoardValue(x+1, y);
				}
				else if(nextCell == 0) {
					System.out.println("nextCell = 0");
					model.makeMove(x, y);
			        controller.aiMove(x+temp, y);
			        flag = false;
				}
				else {
					System.out.println("nextCell = 1");
					nextCell = model.getBoardValue(x-1, y);
					if(nextCell == 2) {
						nextCell = model.getBoardValue(x-1, y);
					}
					else if(nextCell == 0) {
						model.makeMove(x, y);
				        controller.aiMove(x-temp, y);
				        flag = false;
					}
					else {
						break;
					}
				}
			}
		}
		
		else if(move.equals("v"))
		{
			int nextCell = model.getBoardValue(x, y-1);
			System.out.println("Got next cell.");
			while(flag)
			{
				if(nextCell == 2) {
					System.out.println("nextCell = 2");
					nextCell = model.getBoardValue(x, y-1);
				}
				else if(nextCell == 0) {
					System.out.println("nextCell = 0");
					model.makeMove(x, y);
			        controller.aiMove(x, y-temp);
			        flag = false;
				}
				else {
					System.out.println("nextCell = 1");
					nextCell = model.getBoardValue(x, y+1);
					if(nextCell == 2) {
						nextCell = model.getBoardValue(x, y+1);
					}
					else if(nextCell == 0) {
						model.makeMove(x, y);
				        controller.aiMove(x, y+temp);
				        flag = false;
					}
					else {
						break;
					}
				}
			}
		}
	}

	private String analyzeGameboardMedium(int x, int y) {
		return "";
		
	}

	public void sendMove(int x, int y) {
		 String jonsajerk = model.validateOpponent(x, y);
	              if(jonsajerk.equals(consts.VALID)){
	                    makeMove(x,y);
	                } else if (jonsajerk.equals(consts.WIN)) {
	                  
	                }else if(jonsajerk.equals(consts.INVALID)){
	                   //should this do something?
	                }
	                else if(jonsajerk.equals(consts.NOTTURN)){
	                    //should this do something?
	                }
		
	}
	
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
