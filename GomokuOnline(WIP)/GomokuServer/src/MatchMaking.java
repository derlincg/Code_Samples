/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

//package gomokuserver;

import java.util.HashMap;

/**
 * This class is used for matchmaking and communication between players before they enter a peer to peer connection.
 * @author Joseph Bowley
 */
public class MatchMaking {
    //fields
    HashMap<String, Player> playerMap;
    
    @SuppressWarnings("Convert2Diamond")
    public MatchMaking()
    {
        playerMap = new HashMap<String, Player>();
    }
    
    /**
     * Adds a player to the player map with their username as the key
     * @param name username of the player to be added
     * @param p the player object that is added to the map
     */
    public void addPlayer(String name, Player p)
    {
        playerMap.put(name, p);
    }
    
    /**
     * Sends a message from a player to another player.
     * @param toPlayer player to receive the message
     * @param message message to be sent to toPlayer
     */
    public void sendMessage(String toPlayer, String message)
    {
        playerMap.get(toPlayer).sendMessage(message);
    }
    
    /**
     * Helper method for sending challenge requests to players.
     * @param toPlayer player to receive the challenge request
     * @param challengeRequest the challenge request to be sent to the player
     */
    public void challenge(String toPlayer, String challengeRequest)
    {
        sendMessage(toPlayer, challengeRequest);
    }
    
    /**
     * Sends a response from a player who has been challenged to the challenger
     * @param toPlayer
     * @param response 
     */
    public void respond(String toPlayer, String response){
        sendMessage(toPlayer, response);
        
    }
    
    /**
     * Returns the specified users IP Address as a string without an extra characters
     * @param user
     * @return 
     */
    public String getUsersIPAddress(String user)
    {
        return playerMap.get(user).getRemoteIPAddress().split(":")[0].substring(1);
    }
    
}