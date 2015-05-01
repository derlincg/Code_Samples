
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;

/**
 * Team One
 * Gomoku
 * CSCE 320 - Spring 2015
 * 3/16/2015
 * Java - JVM
 * Sources:
 * 
 * Revisions:
 * 3/14/2015 - Class created by Karen Bullinger.
 * 4/5/2015 - Added createFrame() method.
 * 4/11/2015 - Updated newServerConnection to return boolean
 */
public class ClientModel implements Runnable{
    private LeaderboardController leaderboardController;
    private LobbyController lobbyController; 
    private LoginController loginController;
    private GameController gameController;
    public Socket socket;
    public Socket challengeeSocket;
    public Socket opponent;
    public ServerSocket ss;
    public Frame frame;
    public String username;
    private OutputStream outStream;
    private InputStream inStream;
    private DataInputStream dataIn;
    private DataOutputStream dataOut;
    public final int gameHeight = 30;
    public final int gameWidth = 30;
    private Thread worker;
    private final String GAME = "game";
    private final String LOBBY = "lobby";
   
    private int port = 27200;
    
  
    /**
     * Associates ClientModel and LoginController.  Opens new socket to
     * specified host and port.
     * @param cont
     * @param host
     * @param port 
     */
  public void createFrame(LoginView login, LobbyView lobby, GameView game){
      frame  = new Frame(login, lobby, game);
  } 
   /**
    * Creates new socket connection between client and server. Returns true if
    * socket is created.
    * @param cont
    * @param host
    * @param port 
    */
  public boolean newServerConnection() {
        
        try {
            socket = new Socket( loginController.host, loginController.port);
            
        } catch (UnknownHostException ex) {
            return false;
        } catch (IOException ex) {
            Logger.getLogger(ClientModel.class.getName()).log(Level.SEVERE, null, ex);
          return false;

        }
        return true;
       
    }
  /**
   * Associates model with lobbyController.
   * @param cont 
   */
  public void setLobbyController(LobbyController cont){
      this.lobbyController =  cont;// **see line 26
  }
  /**
   * Associates model with loginController.
   * @param cont 
   */
   public void setLoginController(LoginController cont){
      this.loginController =  cont;
 
  }
  
   public void setGameController( GameController cont ){
       this.gameController = cont;
    }
 
  /**
   * Updates frame to display lobby view.
   * Also updates frame title bar to display
   * current username.
   * @param user 
   */
  public void loginLobbyTrans(String user){  
      loginController.closeView(LOBBY);
      lobbyController.setupIOStreams();
      frame.setTitle("Gomoku || " + user);
      
  }

    void updateLobbyPlayers(String[] online) {
        //System.out.println(online instanceof String[]);
        lobbyController.updateOnlinePlayers(online);
        
    }

    void openGameConnection() {
        
       worker = new Thread(this);
       worker.start();
       
    }

    void lobbyGameTrans() {
        frame.updateView(GAME);
    }

    @Override
    public void run() {
         try {
             System.out.println("Thread started");
            ss = new ServerSocket(port);
            challengeeSocket = ss.accept(); //
            inStream = challengeeSocket.getInputStream();
            dataIn = new DataInputStream(inStream);
            outStream = challengeeSocket.getOutputStream();
            dataOut = new DataOutputStream(outStream);
            System.out.println("Calling gameController.newGame()");
            gameController.newGame();
            
        } catch (IOException ex) {
            Logger.getLogger(ClientModel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void connectToOpponent(String m) {
        try {
            opponent = new Socket( m, port );
        } catch (UnknownHostException ex) {
            Logger.getLogger(ClientModel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ClientModel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void aiGameTrans(String difficulty) {
		frame.updateView(GAME);
		
	}
  


  
}
