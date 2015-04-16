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
import java.util.ArrayList;
public class serverModel{
    public ArrayList<ClientConnection> connections = 
            new ArrayList<ClientConnection>();  
    
    /** 
     * @param c the ClientConnection to be added to the ArrayList.
     * 
     * Adds the connection c to the connections ArrayList.
     */
    public void addConnection(ClientConnection c){
        connections.add(c);
    }
    
    /**
     * @param c the connection to be removed from the ArrayList.
     * 
     * Removes the connection c from the connections ArrayList.
     */
    public void removeConnection(ClientConnection c){
        connections.remove(c);
        
    }

    /**
     * @return the size of the connections ArrayList.
     * 
     * This method returns the size of the connections ArrayList using the
     * size method.
     */
   public int getConnections(){
       return connections.size();
   }
}
