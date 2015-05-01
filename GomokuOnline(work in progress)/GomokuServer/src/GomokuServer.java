
import javax.swing.JFrame;
import javax.swing.JPanel;

//package gomokuserver;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author PLUCSCE
 */
public class GomokuServer {
    
    public static void main(String[] args) {
        //Create the Model
        ServerModel model = new ServerModel();
        //Create a view and controller
        ServerView view = new ServerView();
        ServerController controller = new ServerController(model, view);
        //add controller to model and view
        model.setController(controller);
        view.setController(controller);
        
        view.setVisible(true);
        
        JFrame frame = new JFrame();
        JPanel panel = new GameView(30, 30);
        frame.add(panel);
        frame.setSize(500, 500);
        frame.setVisible(true);
    }
}
