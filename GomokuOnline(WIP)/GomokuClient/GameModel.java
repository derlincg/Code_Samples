/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author PLUCSCE
 */
public class GameModel {

    private GameBoard board;
    private GameAI ai;
    private GameController controller;

    public GameModel() {
        System.out.println("A BRAND NEW GAMEMODEL");
        board = new GameBoard();
    }

    public String validateSelf(int x, int y) {
        if (board.getTurnOrder()) {
            if (board.validate(x, y)) {
                board.moveMadeSelf(x, y);
                if (board.isFiveInARowSelf(x, y)) {
                    System.out.println("win");
                    board.resetBoard();
                    return "win";
                } else {
                    System.out.println("valid");
                    return "valid";
                }
            } else {
                System.out.println("invalid");
                return "invalid";
            }
        } else {
            System.out.println("notTurn");
            return "notTurn";
        }


    }

    public String validateOpponent(int x, int y) {
        if (board.validate(x, y)) {
            board.moveMadeOpponent(x, y);
            if (board.isFiveInARowOpponent(x, y)) {
                board.resetBoard();
                return "win";
            } else {
                return "valid";
            }
        } else {
            return "invalid";
        }

    }

    public void setController(GameController gc) {
        this.controller = gc;
    }

    public void setGameBoard(GameBoard gb) {
        this.board = gb;
    }

    public void setAI(GameAI ai) {
        this.ai = ai;
    }

    public void setTurnOrder(boolean b) {
        board.setTurnOrder(b);

    }
    
    public boolean isValid(int x, int y){
    	return board.isValid( x,  y);
    	
    }

	public void makeMove(int x, int y) {
		board.moveMadeSelf(x, y);
		
	}

	public int getBoardValue(int x, int y) {
		// TODO Auto-generated method stub
		return board.getBoardValue(x, y);
	}

	public void resetBoard() {
		board.resetBoard();
		
	}
	
	
}
