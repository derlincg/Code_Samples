/**
 * This class manages sending, receiving, accepting and rejecting game requests.
 * 
 */
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;

/**
 * Team One Gomoku CSCE 320 - Spring 2015 3/16/2015 Java - JVM Sources:
 *
 * Revisions: 3/14/2015 - Class created by Karen Bullinger.
 */
public class LobbyController implements Runnable {

    private LobbyView lobby;
    private ClientModel model;
    private OutputStream outStream;
    private InputStream inStream;
    private DataInputStream dataIn;
    private DataOutputStream dataOut;
    private Thread worker;
    private byte[] msg = new byte[1024];
    private String receivedMsg;
    //Message processing constants
    final private String ONLINE = "online";
    final private String CHALLENGE = "challenge";
    final private String STATS = "stats";
    final private String ACCEPT = "accept";
    final private String REJECT = "reject";
    final private String RESCIND = "rescind";

    /**
     * Sets model in controller.
     * @param m
     */
    public void setModel(ClientModel m) {
        this.model = m;
    }

    /**
     *
     * @param v
     */
    public void setLobbyView(LobbyView v) {

        this.lobby = v;

    }

    /**
     * Updates list of online players in lobby view.
     *
     * @param online list of online players received from server.
     */
    public void updateOnlinePlayers(String[] online) {
        lobby.updateOnlinePlayerList(online);
    }

    /**
     * Sets up IO streams for lobby.
     */
    public void setupIOStreams() {
        try {
            inStream = model.socket.getInputStream();
            dataIn = new DataInputStream(inStream);
            outStream = model.socket.getOutputStream();
            dataOut = new DataOutputStream(outStream);
            worker = new Thread(this);
            worker.start();


        } catch (IOException ex) {
            Logger.getLogger(LobbyController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Listens for messages from server until server goes offline or user quits
     * application
     */
    @Override
    public void run() {
        while (true) {
            try {
                int len = dataIn.read(msg);

                if (len > 0) {
                    receivedMsg = new String(msg, 0, len);
                    System.out.println("Message from server in LobbyController: " + receivedMsg);
                    String[] msgArray;
                    msgArray = receivedMsg.split("[ ]+");

                    processMessage(msgArray);
                }
            } catch (IOException ex) {
                Logger.getLogger(LobbyController.class.getName()).log(Level.SEVERE, null, ex);
                //need to add error handling for server going offline while in lobby
            }
        }
    }

    /**
     * Processes messages received from server and routes them to appropriate
     * method for handling.
     *
     * @param msg message to be routed
     */
    private void processMessage(String[] msg) {
        if(msg.length >= 2){
        String temp = msg[0];
        String name = msg[1];
        switch (temp) {
            case CHALLENGE:
                challengeReceived(name);
                break;
            case ACCEPT:
                System.out.println("Challenge accepted.");
                challengeAccepted(msg[2]);
                break;
            case REJECT:
                challengeRejected(name);
                break;
            case ONLINE:
                lobby.updateOnlinePlayerList(msg);
                break;
            case RESCIND:
                challengeRescind(name);
                break;
            default:
                break;
        }
        }
    }

    /**
     * Rescinds challenge.
     *
     * @param name
     */
    private void challengeRescind(String name) {
        lobby.removeFromReceivedChallenges(name);

    }

    /**
     * When a challenge is received, this method is called to update the
     * Challenges Received list in the Lobby view.
     *
     * @param name name to add to challenges received list
     */
    private void challengeReceived(String name) {
        lobby.updateReceivedList(name);
    }

    /**
     * Method is called when a user's challenge is accepted by the other user.
     * Transitions user from Lobby to Game view and connects to accepting user.
     *
     * @param m IP address of accepting user (server host)
     */
    private void challengeAccepted(String m) {
        model.lobbyGameTrans();
        model.createGameView();
        model.connectToOpponent(m);
        sendRescindResponse(lobby.getSentList());

    }

    /**
     * Method is called when user's challenge is reject by the other user.
     * Removes the chalengee's username from the Challenges Sent list in the
     * Lobby.
     *
     * @param name name of user that rejected the challenge
     */
    private void challengeRejected(String name) {

        lobby.removeFromSentChallenges(name);
    }

    /**
     * Method is called when user clicks "Challenge" button in Lobby View.
     * Writes message to server with following format: "challenge challengee
     * challenger"
     *
     * @param challengee Person to be challenged
     */
    public void sendChallenge(String challengee) {

        try {
            String send = CHALLENGE + " " + challengee + " " + model.username;
            dataOut.write(send.getBytes());
            dataOut.flush();
        } catch (IOException ex) {
            Logger.getLogger(LobbyController.class.getName()).log(Level.SEVERE, null, ex);
            //need to add error handling
        }
    }

    /**
     * Method is called when user clicks "Accept" button in Lobby. Writes out a
     * message to server with following format: "accept chalengee challenger"
     * Triggers transition from Lobby view to Game view and initiates a new
     * game.
     *
     * @param challengee
     */
    public void sendAcceptResponse(String challengee) {

        try {
            String accept = ACCEPT + " " + challengee + " " + model.username;
            dataOut.write(accept.getBytes());
            dataOut.flush();
            model.createGameView();
            model.lobbyGameTrans();
            newChallengeGame();
            sendRescindResponse(lobby.getSentList());
        } catch (IOException ex) {
            Logger.getLogger(LobbyController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Method is called when user clicks "Reject" button in Lobby view. Writes
     * out a message to server with following format: "reject challengee
     * challenger"
     *
     * @param challengee
     */
    public void sendRejectResponse(String challengee) {

        try {
            String reject = REJECT + " " + challengee + " " + model.username;
            dataOut.write(reject.getBytes());
            dataOut.flush();
            lobby.removeFromReceivedChallenges(challengee);

        } catch (IOException ex) {
            Logger.getLogger(LobbyController.class.getName()).log(Level.SEVERE, null, ex);
            //Add error handling
        }

    }

    /**
     * Called when a user accepts a challenge. Creates a new server socket in
     * the model.
     */
    public void newChallengeGame() {
        model.openGameConnection();
    }

    /**
     * Used by view to get username from model.
     */
    public String getUsername() {

        return model.username;
    }

    /**
     * Called when a user enters a game. Sends rescind message to all challenges
     * the player sent out.
     */
    public void sendRescindResponse(DefaultListModel<String> sentModel) {
        try {
            while (!sentModel.isEmpty()) {
                String reject = RESCIND + " " + sentModel.getElementAt(0) + " " + model.username;
                dataOut.write(reject.getBytes());
                dataOut.flush();
                lobby.rescindFromSentChallenges(sentModel.getElementAt(0));
            }

        } catch (IOException ex) {
            Logger.getLogger(LobbyController.class.getName()).log(Level.SEVERE, null, ex);
            //Add error handling
        }

    }
    
    public void lobbyLeaderTrans(){
        model.lobbyLeaderTrans();
    }
}
