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
    
    private GameController controller;
    private Game gomoku;
    
    public GameModel()
    {
        gomoku = new Game();
    }
    
    public void moveMade(int x, int y, int player)
    {
        gomoku.moveMade(x, y, player);
    }
    
    public void setGame(Game gomoku)
    {
        this.gomoku = gomoku;
    }
    
    public void setController(GameController gc)
    {
        this.controller = gc;
    }
    
}
