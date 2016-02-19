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
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class serverController implements Runnable {

    private ServerSocket ss;
    private serverModel model;
    private serverView view;
    
    private InetAddress ad;
    private Socket newConnection;
    private Thread worker;
    private ClientConnection newClient;
    
    private InputStream inStream;
    private DataInputStream dataIn;
    private OutputStream outStream;
    private DataOutputStream dataOut;
    
    private int port;
    private ClientConnection temp;
  
   
    /**
     * 
     * @param m serverModel
     * @param v serverView
     * @param p port
     * @param ip ip address
     * 
     * Constructor gathers all necessary pieces of information for opening
     * new server socket.
     * 
     * Associates controller with model and view.
     */
    public serverController(serverModel m, serverView v, int p, String ip) {
        try {
            this.model = m;
            this.view = v;
            port = p;
            ad = InetAddress.getByName(ip);
        } catch (UnknownHostException ex) {
            Logger.getLogger(serverController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
/**
 * Creates new server socket and thread for listening for new connections.
 */
    public void listenForConnections() {
        try {
            ss = new ServerSocket(port, 50, ad);
        } catch (IOException ex) {
            Logger.getLogger(serverController.class.getName()).log(Level.SEVERE, null, ex);
        }
        worker = new Thread(this);
        worker.start();
    }

    /**
     * Accepts incoming connections, gathers input and output streams,
     *  creates new ClientConnection object and adds to ClientConnections 
     *  ArrayList in model.
     *  Updates number of connections in view.
     */
    @Override
    public void run() {
        try {
            while (true) {
                newConnection = ss.accept();
                
                inStream = newConnection.getInputStream();
                dataIn = new DataInputStream(inStream);
                outStream = newConnection.getOutputStream();
                dataOut = new DataOutputStream(outStream);
                
                newClient = 
                    new ClientConnection(dataIn, dataOut, newConnection, this);
                
                model.addConnection(newClient);
                updateConnectionsView();
            }
        } catch (IOException ex) {
            Logger.getLogger(serverController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /**
     * @param get message to send out to all clients
     * 
     * For each client in ClientConnection ArrayList, sends message.
     */
    public void newMessageReceived(String get) {
        try {
            for (int i = 0; i < model.connections.size(); i++) {
                temp = model.connections.get(i);
                temp.dataOut.write(get.getBytes());
                dataOut.flush();
            }
        } catch (IOException ex) {
            Logger.getLogger(serverController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
   
    /**
     * 
     * @param aThis ClientConnection object
     * 
     * When a client connection is ended, connection is removed from 
     * ClientConnection ArrayList, closes socket.  Also updates number of active connections
     * in view.
     */
    public void connectionEnded(ClientConnection aThis) {
        model.removeConnection(aThis);
        updateConnectionsView();
        try {
            aThis.socket.close();
        } catch (IOException ex) {
            Logger.getLogger(serverController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
/**
 * Updates number of active connections in the view.
 */
    public void updateConnectionsView() {
        view.updateActiveConnections(model.getConnections());
    }
/**
 * @param ad InetAddress of client sending the message
 * 
 * Updates message traffic text area in view with IP address of sender.
 */
    public void updateTrafficView(InetAddress ad) {
        view.updateTraffic(ad);
    }
}
