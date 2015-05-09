
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

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
 */
public class LeaderboardController implements Runnable{
    private LeaderboardView leaderboard;
    private ClientModel model;
      private DataInputStream dataIn;
    private DataOutputStream dataOut;
    
    
    
    public void setView(LeaderboardView view){
        this.leaderboard = view;
    }
    
    public void setModel(ClientModel m){
        this.model = m;
    }
    
    public void setIOStreams(InputStream s, OutputStream o){
        dataIn = new DataInputStream(s);
        dataOut = new DataOutputStream(o);
    }
    
  
    
    @Override
    public void run() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
}
