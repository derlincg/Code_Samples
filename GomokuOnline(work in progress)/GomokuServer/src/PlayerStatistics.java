
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Stores and manages data used in player game statistics
 */

/**
 *
 * @author PLUCSCE
 */
public class PlayerStatistics {
    
    private final Map<String, String> stats = new HashMap<String, String>();
    private final File f = new File("stats.gmk");
    private FileWriter fw;
    private PrintWriter pw;
    private String data;
    
    /**
     * PlayerStatistics constructor
     * Reads player stats from a file and stores them in memory for easy access
     */
    public PlayerStatistics()
    {
        try {
            BufferedReader br = new BufferedReader(new FileReader(f));
            String line, delims = "[,]";
            String[] UaP;
            
            while((line = br.readLine()) != null)
            {
                UaP = line.split(delims);
                stats.put(UaP[0], UaP[1] + " " + UaP[2]);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(RegisteredPlayers.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException e) {
            Logger.getLogger(RegisteredPlayers.class.getName()).log(Level.SEVERE, null, e);
	}
    }
    
    /**
     * Adds a player to the statistics file and hashmap
     * @param name 
     */
    public void addPlayerStats(String name)
    {
        stats.put(name, "0 0");
        try(PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(f, true)))) {
            out.println(name + ",0,0");
        }catch (IOException e) {
            Logger.getLogger(RegisteredPlayers.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    
    /**
     * Adds a win to the specified user
     * @param user 
     */
    public void addWin(String user)
    {
        String[] winLoss = stats.get(user).split(" ");
        int wins = Integer.parseInt(winLoss[0]);
        wins++;
        stats.put(user, String.valueOf(wins) + " " + winLoss[1]);
        updateLine(user + "," + winLoss[0] + "," + winLoss[1], user + "," + String.valueOf(wins) + "," + winLoss[1]);
    }
    
    /**
     * adds a loss to the specified user
     * @param user 
     */
    public void addLoss(String user)
    {
        String[] winLoss = stats.get(user).split(" ");
        int losses = Integer.parseInt(winLoss[1]);
        losses++;
        stats.put(user, winLoss[1] + " " + String.valueOf(losses));
        updateLine(user + "," + winLoss[0] + "," + winLoss[1], user + "," + winLoss[0] + "," + String.valueOf(losses));
    }
    
    
    /**
     * used to update a specific line in a file
     * @param toUpdate
     * @param updated 
     */
    @SuppressWarnings("null")
    private void updateLine(String toUpdate, String updated){
        try {
            BufferedReader file = null;
            try {
                file = new BufferedReader(new FileReader("stats.gmk"));
            } catch (FileNotFoundException ex) {
                Logger.getLogger(PlayerStatistics.class.getName()).log(Level.SEVERE, null, ex);
            }
            String line;
            String input = "";
            
            try {
                while ((line = file.readLine()) != null)
                    input += line + System.lineSeparator();
            } catch (IOException|NullPointerException ex){
                Logger.getLogger(PlayerStatistics.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            input = input.replace(toUpdate, updated);
            
            FileOutputStream os = null;
            try {
                os = new FileOutputStream("stats.gmk");
            } catch (FileNotFoundException ex) {
                Logger.getLogger(PlayerStatistics.class.getName()).log(Level.SEVERE, null, ex);
            }
            os.write(input.getBytes());
            
            file.close();
            os.close();
        } catch (IOException|NullPointerException ex) {
            Logger.getLogger(PlayerStatistics.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Returns a string representation of all player statistics
     * @return 
     */
    public String getAllStats()
    {
        StringBuilder sb = new StringBuilder("stats ");
        for(Map.Entry<String, String> entry : stats.entrySet())
        {
            sb.append(entry.getKey());
            sb.append(" ");
            sb.append(entry.getValue());
            sb.append(" ");
        }
        System.out.println(sb.toString());
        return sb.toString();
    }
    
    
    
}
