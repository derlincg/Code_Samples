import java.util.Arrays;

/**
 * Team One Gomoku CSCE 320 - Spring 2015 3/16/2015 Java - JVM Sources:
 *
 * Revisions: 3/14/2015 - Class created by Joseph Bowley.
 */
public class GameBoard {

    private final int[][] board;
    private final int NOTPLAYED = 0;
    private final int PLAYERONE = 1;
    private final int PLAYERTWO = 2;
    private boolean turnOrder;

    /**
     * GameBoard constructor. Passes in dimensions of the board.
     *
     * @param x
     * @param y
     */
    public GameBoard(int x, int y) {
        board = new int[x][y];
        for (int i = 0; i < x; i++) {
            for (int k = 0; k < y; k++) {
                board[i][k] = 0;
            }
        }
        turnOrder = true;
    }

    public GameBoard() {
        board = new int[30][30];
        for (int i = 0; i < 30; i++) {
            for (int k = 0; k < 30; k++) {
                board[i][k] = 0;
            }
        }
        turnOrder = true;
    }

    public void setTurnOrder(boolean to) {
        turnOrder = to;
    }

    public boolean getTurnOrder() {
        return turnOrder;
    }

    /**
     * Makes a move on the board for one of the user player active in a game.
     *
     * @param x
     * @param y
     */
    public void moveMadeSelf(int x, int y) {
        board[x][y] = 1;
        turnOrder = false;
    }

    /**
     * Makes a move on the board for one of the opponent player active in a
     * game.
     *
     * @param x
     * @param y
     */
    public void moveMadeOpponent(int x, int y) {
        board[x][y] = 2;
        turnOrder = true;

    }

    /**
     * Returns the representation of the gameBoard
     *
     * @return
     */
    public int[][] getBoard() {
        return board;
    }

    /**
     * Returns if there is a five in a row on the board.
     *
     * @return
     */
    public boolean isFiveInARowSelf(int x, int y) {

        if (horizontalSearch(x, y, 1)) {
            return true;
        } else if (verticalSearch(x, y, 1)) {
            return true;
        } else if (diagonalFowardlSearch(x, y, 1)) {
            return true;
        } else if (diagonalBackSearch(x, y, 1)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isFiveInARowOpponent(int x, int y) {

        if (horizontalSearch(x, y, 2)) {
            return true;
        } else if (verticalSearch(x, y, 2)) {
            return true;
        } else if (diagonalFowardlSearch(x, y, 2)) {
            return true;
        } else if (diagonalBackSearch(x, y, 2)) {
            return true;
        } else {
            return false;
        }
    }

    private boolean diagonalBackSearch(int x, int y, int z) {
        int count = 0;
        int result = 1;
        boolean flag = false;
        int i = x - 1;
        int j = y - 1;

        while (count < 5 && i >= 0 && j >= 0 && board[i][j] == z) {
            result++;
            i--;
            j--;
        }

        count = 0;
        i = x + 1;
        j = y + 1;
        while (count < 5 && i < 30 && j < 30 && board[i][j] == z) {
            result++;
            i++;
            j++;
        }

        if (result >= 5) {
            flag = true;
        }
        return flag;
    }

    private boolean diagonalFowardlSearch(int x, int y, int z) {
        int count = 0;
        int result = 1;
        boolean flag = false;
        int i = x - 1;
        int j = y + 1;

        while (count < 5 && i >= 0 && j < 30 && board[i][j] == z) {
            result++;
            i--;
            j++;
        }
        count = 0;
        i = x + 1;
        j = y - 1;
        while (count < 5 && i < 30 && j >= 0 && board[i][j] == z) {
            result++;
            i++;
            j--;
            //System.out.println()
        }

        if (result >= 5) {
            flag = true;
        }
        return flag;
    }

    private boolean verticalSearch(int x, int y, int z) {
        int count = 0;
        int result = 1;
        boolean flag = false;
        int i = y - 1;

        while (count < 5 && i >= 0 && board[x][i] == z) {
            result++;
            i--;
        }

        count = 0;
        i = y + 1;
        while (count < 5 && i < 30 && board[x][i] == z) {
            result++;
            i++;
        }

        if (result >= 5) {
            flag = true;
        }

        return flag;
    }

    private boolean horizontalSearch(int x, int y, int z) {
        int count = 0;
        int result = 1;
        boolean flag = false;
        int i = x - 1;

        while (count < 5 && i >= 0 && board[i][y] == z) {
            result++;
            i--;
        }

        count = 0;
        i = x + 1;

        while (count < 5 && i < 30 && board[i][y] == z) {
            result++;
            i++;
        }

        if (result >= 5) {
            flag = true;
        }
        return flag;
    }

    public void printBoard() {
        for (int i = 0; i < 30; i++) {
            for (int k = 0; k < 30; k++) {
                System.out.print(board[i][k]);
            }
            System.out.println();
        }
    }

    /**
     * Checks to see if a selected move is a valid move.
     *
     * @param x
     * @param y
     */
    public boolean validate(int x, int y) {
        if (board[x][y] == 0) {
            return true;
        } else {
            return false;
        }
    }

    public void resetBoard() {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                board[i][j] = 0;
            }
        }
    }
    
    public boolean isValid(int x, int y) {
		//System.out.println("Checking Validty" + board[x][y]);
		if(board[x][y] == 0){
			//System.out.println("Is Valid");
			return true;
		}else{
		return false;
		}
    }

	public int getBoardValue(int x, int y) {
		// TODO Auto-generated method stub
		return board[x][y];
	}
}
