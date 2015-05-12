
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class will manage game play set up.
 */
/**
 * Team One Gomoku CSCE 320 - Spring 2015 3/16/2015 Java - JVM Sources:
 *
 * Revisions: 3/14/2015 - Class created by Joseph Bowley.
 */
public class GameController implements Runnable {

    private GameBoard board;
    private GameAI ai;
    private ClientModel model;
    private GameView view;
    private GameModel gameModel;
    private OutputStream outStream;
    private InputStream inStream;
    private DataInputStream dataIn;
    private DataOutputStream dataOut;
    private Thread worker;
    private byte[] msg = new byte[1024];
    private String returnedMsg;
    private final String MOVE = "move";
    private OutputStream outStreamOp;
    private InputStream inStreamOp;
    private DataInputStream dataInOp, mainServerIn;
    private DataOutputStream dataOutOp, mainServerOut;
    private Constants consts = new Constants();
    private Socket captainCarl;
    private ServerSocket ss;
    private boolean server;
    public boolean aiGame; //if true it is an ai game. If false it is not. 

    
    public boolean loggedIn()
    {
        return model.loggedIn;
    }
    public void setaiGame(boolean isAI, String difficulty){
 	   aiGame = isAI;
 	   ai = new GameAI(difficulty, this);
 	   gameModel.setTurnOrder(true);
    }
   
    public void setSocket(Socket sock){
        this.captainCarl = sock;
        
        server = false;
        System.out.println("SetSocket Method: " + server);
    }
    
    public void setServerSocket( ServerSocket sock){
        this.ss = sock;
        
        server = true;
        System.out.println("SetServerSocket: " + server);
    }
    public void setInputStream(InputStream s) {
        System.out.println(" Set Input Stream");
        dataIn = new DataInputStream(s);
    }

    public void setOutputStream(OutputStream o) {
        System.out.println("Set Output Stream");
        dataOut = new DataOutputStream(o);
    }

     public void setMainInputStreams(InputStream s, OutputStream o){
        this.mainServerIn = new DataInputStream(s);
        this.mainServerOut = new DataOutputStream(o);
    
    }
     
    public void setModel(ClientModel m) {
        this.model = m;
    }

    public void setView(GameView v) {
        this.view = v;
    }

    public void newGame() {
        worker = new Thread(this);
        worker.start();

    }

    public void setGameModel(GameModel gm) {
        this.gameModel = gm;
    }

    public void setGameBoard(GameBoard gb) {
        this.board = gb;
    }

    public void setAI(GameAI ai) {
        this.ai = ai;
    }

    public String validateSelf(int x, int y) {
    	String validatedMove = gameModel.validateSelf(x, y);
        if (validatedMove.equals(consts.WIN)) {
            if(aiGame){
                return "win";	
            }else{
            sendMove(x, y);
            sendWin();
            closeSockets();
            return consts.WIN;
            }
        } else if (validatedMove.equals(consts.VALID)) {
            if(aiGame){
            	ai.sendMove(x, y);	
            	return consts.VALID;
            }else{
            sendMove(x, y);
            return consts.VALID;
            }
        } else if (validatedMove.equals(consts.INVALID)) {
            return consts.INVALID;
        } else {
            return consts.NOTTURN;
        }


    }
    
   public void sendWin(){
        try {
            String win = "game " + model.username + " win";
            System.out.println(win);
            mainServerOut.write(win.getBytes());
            dataOut.flush();
        } catch (IOException ex) {
            Logger.getLogger(GameController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void sendLose(){
        try {
            String lose = "game " + model.username + " lose";
            System.out.println(lose);
            mainServerOut.write(lose.getBytes());
            dataOut.flush();
        } catch (IOException ex) {
            Logger.getLogger(GameController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void sendResign(String res){
        try {
            dataOut.write(res.getBytes());
            dataOut.flush();
        } catch (IOException ex) {
            Logger.getLogger(GameController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    public void sendMove(int x, int y) {
        try {
            String move = "move " + x + " " + y;
            System.out.println("Send move: " + move);
            dataOut.write(move.getBytes());
            view.updateTurnNotification(false);
            dataOut.flush();
        } catch (IOException ex) {
            Logger.getLogger(GameController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void processMessage(String[] s) {
        String m = s[0];
        if(s.length > 1 ){
            int x = Integer.parseInt(s[1]);
            int y = Integer.parseInt(s[2]);
        
        String jonsajerk = gameModel.validateOpponent(x, y);
        
              if(jonsajerk.equals(consts.VALID)){
                    view.displayMove(x,y);
                    view.updateTurnNotification(true);
                } else if (jonsajerk.equals(consts.WIN)) {
                    view.displayMove(x, y);
                    view.lose(); 
                    sendLose();
                    closeSockets();
                }
            }
                else if(m.equals(consts.RESIGN)){
            try {
                view.displayErrorMessage("Opponent has resigned.");
                view.win();
                
                if(server){
                    try {
                        ss.close();
                    } catch (IOException ex) {
                        Logger.getLogger(GameController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                else
                    captainCarl.close();
                
                model.gameLobbyTrans();
            }
           
            catch (IOException ex) {
                Logger.getLogger(GameController.class.getName()).log(Level.SEVERE, null, ex);
            }
                }
                
               
    }

    @Override
    public void run() {
        System.out.println("Thread started");
        while (true) {
            try {
                int len = dataIn.read(msg);

                if (len > 0) {
                    returnedMsg = new String(msg, 0, len);
                    System.out.println("Msg received: " + returnedMsg);
                    String[] msgArray;
                    msgArray = returnedMsg.split("[ ]+");
                    processMessage(msgArray);

                }
            } catch (IOException ex) {
                //Logger.getLogger(GameController.class.getName()).log(Level.SEVERE, null, ex);
                return;
                
            }
        }
    }
    
    public void switchToLobby() {
        model.gameLobbyTrans();
    }
    public void switchToLogin(){
        model.gameLoginTrans();
        gameModel.resetBoard();
    }

	public void setTurnOrder(boolean b) {
		gameModel.setTurnOrder(b);
                displayTurnNotice(b);
		
	}
        
    public void displayTurnNotice(boolean b){
            if(b){
                view.updateTurnNotification(true);
            }
            else
                view.updateTurnNotification(false);
     }
        
        public void resign(){
            
            if(model.loggedIn && aiGame){
                switchToLobby();
            }
            else if(model.loggedIn && !aiGame){
                sendResign(consts.RESIGN);
                switchToLogin();
                closeSockets();
             }
             else
                switchToLogin();
        }
        
        public void closeSockets()
        {
            if(server){
                try {
                    ss.close();
                   
                } catch (IOException ex) {
                    //Logger.getLogger(GameController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            else try {
                captainCarl.close();
              
            } catch (IOException ex) {
                //Logger.getLogger(GameController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    public void aiMove(int x, int y){
        	String validatedMove = gameModel.validateOpponent(x, y);
                  if(validatedMove.equals(consts.VALID)){
                        view.displayMove(x,y);
                    } else if (validatedMove.equals(consts.WIN)) {
                        view.displayMove(x, y);
                        view.lose();
                        System.out.println("Game controller aiMove lose");
                    }
        }
}
