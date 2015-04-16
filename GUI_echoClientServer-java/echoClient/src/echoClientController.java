/*
 * Names: CARL DERLINE,
 *          Karen Bullinger
 * Class: CSCE 320
 * Date: March 2, 2015
 * Desc: Basic echo client with GUI. 
 * To run:  java EchoClient [ip address] [port]
 * Resources: http://www.java2s.com/Tutorial/Java/0320__Network/EchoClient.htm
 *      
 */
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
public class echoClientController implements Runnable {

    private Socket socket;
    private BufferedReader serverIn;
    private PrintWriter out;
    private echoClientview view = new echoClientview();
    private String msg;
    private Thread worker;
    private byte[] message = new byte[1000];
    
    private OutputStream outStream;
    private InputStream inStream;
    private DataInputStream dataIn;
    private DataOutputStream dataOut;

    /**
     *
     * @param host String
     * @param port int
     *
     * Creates new socket with given IP address and port. Creates input and
     * output streams for communication to and from server.
     */
    public echoClientController(String host, int port) {
        try {
            socket = new Socket(host, port);
            
            inStream = socket.getInputStream();
            dataIn = new DataInputStream(inStream);
            
            outStream = socket.getOutputStream();
            dataOut = new DataOutputStream(outStream);
            
            
            
            out = new PrintWriter(socket.getOutputStream());

        } catch (ConnectException ex){
            Logger.getLogger(echoClientController.class.getName()).log(Level.SEVERE, null, ex);
            view.displayErrorMessage("Unable to connect.");
            System.exit(1);
        } catch (UnknownHostException ex) {
            Logger.getLogger(echoClientController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(echoClientController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * @params none Waits for messages to come in from the server. Once messages
     * come in from the server, posts them to GUI message text area. If message
     * length is < 0, break.
     * Catches IO exception.
     */
    @Override
    public void run() {
        try {
            while (true) {          
                
                int len = dataIn.read(message);
                                              
                if (len > 0) {
                    view.updateMessages(new String(message, 0, len));
                   
                } else {
                    break;
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(echoClientController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * @params none
     * Instantiates new thread in order to read messages
     * from server.
     * Calls start() - which calls run().
     */
    public void begin() {
        worker = new Thread(this);
        worker.start();

    }
    /**
     * @param m String message being sent
     * Sends messages to server.
     * Clears output stream.
     */
    public void sendMessage(byte[] m) {
        try {        
            dataOut.write(m);
            dataOut.flush();
        } catch (IOException ex) {
            Logger.getLogger(echoClientController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * @param view echoClientview 
     * Associates view with current controller.
     */
    public void setView(echoClientview view) {
        this.view = view;
    }
}
