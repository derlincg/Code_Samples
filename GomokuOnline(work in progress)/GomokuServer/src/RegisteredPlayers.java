//package gomokuserver;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RegisteredPlayers 
{
    @SuppressWarnings("Convert2Diamond")
    private static final Map<String, String> users = new HashMap<String, String>();
    private final File f = new File("users.txt");
    private FileWriter fw;
    private PrintWriter pw;
    
    /**
     * Constructor. Fills a hashmap with values from a text file stored
     * locally. The hashmap will contain the registered users as the key, and
     * their passwords as the value.
     */
    public RegisteredPlayers()
    {
        try {
            BufferedReader br = new BufferedReader(new FileReader(f));
            String line, delims = "[,]";
            String[] UaP;
            
            while((line = br.readLine()) != null)
            {
                UaP = line.split(delims);
                users.put(UaP[0], UaP[1]);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(RegisteredPlayers.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException e) {
            Logger.getLogger(RegisteredPlayers.class.getName()).log(Level.SEVERE, null, e);
	}
    }
    
    /**
     * @param uName the username of the player.
     * @param pWord the password associated with the players username
     * @return true if the map contains the user, false otherwise.
     */
    public boolean loginIsRegistered(String uName, String pWord)
    {
        return pWord.equals(users.get(uName));
    }
    
    /**
     * @param uName the username of the player.
     * @param pass the password of the player.
     * @return false if the player is already registered, true if the player
     * registered successfully.
     * This method checks if the username of the player is in the map, and
     * if not, it appends their username and password to the file and also
     * adds them into the map of users.
     */
    public boolean registerIsRegistered(String uName, String pass)
    {
        
        if(users.containsKey(uName))
            return false;
        else
        {
            addPlayer(uName, pass);
            try {
                fw = new FileWriter(f, true);
                pw = new PrintWriter(fw);
                pw.println(uName + "," + pass);
                pw.close();
            } catch (IOException ex) {
                Logger.getLogger(RegisteredPlayers.class.getName()).log(Level.SEVERE, null, ex);
            }
            return true;
        }
    }
    
    /**
     * @param uName the username of the player.
     * @param pass the password of the player.
     * adds a player to the map of users.
     */
    public void addPlayer(String uName, String pass)
    {
        users.put(uName, pass);
    }
    
    /**
     * @param uName the username of the player
     * removes a player from the map of users.
     */
    public void removePlayer(String uName)
    {
        users.remove(uName);
    }
    
    /*
    public static void printMap(Map m)
    {
        for(Object key : users.keySet())
        {
            System.out.println("Key: " + key + "\tValue: " + users.get(key));
        }
    } 
    */
}
