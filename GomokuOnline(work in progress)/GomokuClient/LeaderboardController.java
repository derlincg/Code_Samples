
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Team One Gomoku CSCE 320 - Spring 2015 3/16/2015 Java - JVM Sources:
 *
 * Revisions: 3/14/2015 - Class created by Karen Bullinger.
 */
public class LeaderboardController implements Runnable {

    private LeaderboardView leaderboard;
    private ClientModel model;
    private DataInputStream dataIn;
    private DataOutputStream dataOut;
    private byte[] msg = new byte[1024];
    private String receivedMsg;
    private boolean waiting = true;

    public void setView(LeaderboardView view) {
        this.leaderboard = view;
    }

    public void setModel(ClientModel m) {
        this.model = m;
    }

    public void setIOStreams(InputStream s, OutputStream o) {
        System.out.println("setting io streams in leader");
        dataIn = new DataInputStream(s);
        System.out.println(dataIn);
        dataOut = new DataOutputStream(o);
    }

    public void getStats() {
        try {
            dataOut.write("stats".getBytes());
            dataOut.flush();
            System.out.println("Waiting for incoming message");
         
            /*while (waiting) {
            int len = dataIn.read(msg);
            System.out.println("Receive message");

                if (len > 0) {
                    receivedMsg = new String(msg, 0, len);
                    
                    String[] stats;
                    stats = receivedMsg.split("[ ]+");
                    
                    //updateStatsBoard(stats);
                    for( int i = 1; i < stats.length; i=i+3){
                         System.out.println( stats[i] + " " + stats[i+1] + " " + stats[i+2]);
                    }
                waiting = false;
                }
            }*/
            }
            
        
                  catch (IOException ex) {
            Logger.getLogger(LeaderboardController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    


    @Override
        public void run() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    public void updateStatsBoard(String[] m){
        for( int i = 1; i < m.length; i=i+3){
            System.out.println( m[i] + " " + m[i+1] + " " + m[i+2]);
            
            
        }
        leaderboard.updateTable(m);
        
    }
    
    public void statsLobbyTrans()
    {
    	model.statsLobbytrans();
    }
}
