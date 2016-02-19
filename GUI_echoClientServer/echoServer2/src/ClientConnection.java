/*
 * Names: Carl Derline
 *        Karen Bullinger
 * Class: CSCE 320
 * Date: March 9, 2015
 * Desc: Basic Echo Server with GUI.
 * To run:  java EchoServer [ip address] [port]
 * Resources: Dr. Hauser
 *      
 */
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientConnection implements Runnable {

    protected DataInputStream dataIn;
    protected DataOutputStream dataOut;
    private serverController controller;
    protected Socket socket;
    private Thread worker;
    private byte[] message = new byte[1000];
    private String msgString;
    public ArrayList<String> messages = new ArrayList<String>();

    /**
     * @param in DataInputStream
     * @param out DataOutputStream
     * @param s Socket
     * @param aThis controller
     * 
     * Associates the parameters with the global variables declared above.
     * 
     * Starts a new thread for the clientConnection to operate on.
     */
    public ClientConnection(DataInputStream in, DataOutputStream out, 
            Socket s, serverController aThis) {

        this.dataIn = in;
        this.dataOut = out;
        this.socket = s;
        this.controller = aThis;

        worker = new Thread(this);
        worker.start();

    }

    /**
     * Reads incoming messages sent from a client. Then adds the message
     * to the messages ArrayList and calls newMessageReceived with the
     * last element of the ArrayList, which sends out the message to all the
     * clients.
     * 
     * Updates the traffic textArea.
     * 
     * If the read returns a -1 for any of the clients, connectionEnded is
     * called, which removes that connection from the connections ArrayList.
     * This stops messages from being sent to clients that are no longer
     * connected.
     */
    @Override
    public void run() {
        try {
            while (true) {
                int len = dataIn.read(message);

                if (len > 0) {
                    msgString = new String(message, 0, len);
                    messages.add(msgString);
                    controller.newMessageReceived(messages.get(messages.size() - 1));
                    controller.updateTrafficView(this.socket.getInetAddress());
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(ClientConnection.class.getName()).log(Level.SEVERE, null, ex);
            controller.connectionEnded(this);
        }
    }
}
