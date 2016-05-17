
/**
 * This class manages all pertinent data for the client application.
 * Communicates GUI transition from each controller to the frame class.
 */
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
 * Team One Gomoku CSCE 320 - Spring 2015 3/16/2015 Java - JVM Sources:
 *
 * Revisions: 3/14/2015 - Class created by Karen Bullinger. 4/5/2015 - Added
 * createFrame() method. 4/11/2015 - Updated newServerConnection to return
 * boolean
 */
public class ClientModel implements Runnable {

    private LeaderboardController leaderboardController;
    private LobbyController lobbyController;
    private LoginController loginController;
    private GameController gameController;
    private LeaderboardController leaderController;
    public Socket socket;
    public Socket challengeeSocket;
    public Socket opponent;
    public ServerSocket ss;
    public Frame frame;
    public String username;
    public final int gameHeight = 30;
    public final int gameWidth = 30;
    private Thread worker;
    private final String GAME = "game";
    private final String LOBBY = "lobby";
    private int port = 27200;
    public boolean turn;
    public GameView gameView;
    private Constants consts = new Constants();
    public boolean loggedIn = false;
    
    
    /**
     * 
     */
    public void setUsername(String u){
        this.username = u;
    }

    /**
     * 
     */
    public void setLoggedIn(boolean b){
        loggedIn = b;
    }
    
    /**
     * Creates a new frame to hold each needed view.
     *
     * @param login instance of login view
     * @param lobby instance of lobby view
     * @param game instance of game view
     */
    public void createFrame(LoginView login, LobbyView lobby, GameView game, LeaderboardView leader) {
        frame = new Frame(login, lobby, game, leader);
    }

    public void createGameView() {
        System.out.println("create new game.");
        frame.newGame();
    }

    /**
     * Creates new socket connection between client and server. Returns true if
     * socket is created.
     *
     * @param cont
     * @param host
     * @param port
     */
    public boolean newServerConnection() {

        try {
            socket = new Socket(loginController.host, loginController.port);

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
     *
     * @param cont
     */
    public void setLobbyController(LobbyController cont) {
        this.lobbyController = cont;// **see line 26
    }

    /**
     * Associates model with loginController.
     *
     * @param cont
     */
    public void setLoginController(LoginController cont) {
        this.loginController = cont;

    }

    /**
     * Associates model with gameController.
     *
     * @param cont
     */
    public void setGameController(GameController cont) {
        this.gameController = cont;
    }

    public void setLeaderboardController(LeaderboardController cont) {
        this.leaderController = cont;
    }

    /**
     * Updates frame to display lobby view. Also updates frame title bar to
     * display current username.
     *
     * @param user
     */
    public void loginLobbyTrans() {
        loginController.closeView(LOBBY);
        lobbyController.setupIOStreams();
        frame.setTitle("Gomoku || " + username);

    }

    void updateLobbyPlayers(String[] online) {
        //System.out.println(online instanceof String[]);
        lobbyController.updateOnlinePlayers(online);

    }

    /**
     * Creates a starts new thread that will accept incoming connection from
     * peer.
     */
    public void openGameConnection() {

        worker = new Thread(this);
        worker.start();

    }

    /**
     * Transitions from lobby view to game view.
     */
    public void lobbyGameTrans() {
        frame.updateView(consts.GAME);
    }

    public void gameLobbyTrans() {
        frame.updateView(consts.LOBBY);
        frame.newGame();
    }
    
    public void statsLobbytrans() {
    	frame.updateView(consts.LOBBY);
    }

    public void lobbyLeaderTrans() {
        frame.updateView(consts.LEADERBOARD);
        
        try {
            leaderController.setIOStreams(socket.getInputStream(), socket.getOutputStream());
           
        } catch (IOException e) {
            e.printStackTrace();
        }
         leaderController.getStats();

    }

    /**
     * Started by openGameConnection. Creates new serverSocket and waits for a
     * client to connect to it. Sets up IO streams
     */
    @Override
    public void run() {
        try {
            ss = new ServerSocket(port);
            challengeeSocket = ss.accept();
            gameController.setServerSocket(ss);
            System.out.println("Accepting connection from opponent.");//
            gameController.setInputStream(challengeeSocket.getInputStream());
            gameController.setOutputStream(challengeeSocket.getOutputStream());
            gameController.setMainInputStreams(socket.getInputStream(), socket.getOutputStream());
            gameController.newGame();
            gameController.setTurnOrder(true);

        } catch (IOException ex) {
            Logger.getLogger(ClientModel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
     public void gameLoginTrans(){
        frame.updateView("loginPane");
        frame.newGame();
    }

    /**
     * Connects to opponents server socket.
     *
     * @param m
     */
    void connectToOpponent(String m) {
        try {
            opponent = new Socket(m, port);
            System.out.println("Connect to opponnent");
            gameController.setSocket(opponent);
            gameController.setInputStream(opponent.getInputStream());
            gameController.setOutputStream(opponent.getOutputStream());
            gameController.setMainInputStreams(socket.getInputStream(), socket.getOutputStream());
            gameController.newGame();
            gameController.setTurnOrder(false);
        } catch (UnknownHostException ex) {
            Logger.getLogger(ClientModel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ClientModel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Handles GUI transition from login view to game view.
     *
     * @param difficulty
     */
    public void aiGameTrans(String difficulty) {
        gameController.setTurnOrder(true);
        gameController.setaiGame(true, difficulty);
        frame.updateView(GAME);

    }
    
    public void processStats(String[] s){
        leaderController.updateStatsBoard(s);
    }
    
    public void lostConnection(){
       // socket = null;
        frame.updateView("loginPane");
    }
}
