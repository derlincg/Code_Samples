/*
 * CardLayoutDemo.java
 *
 */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
 
public class Layout implements ItemListener {
    JPanel cards; //a panel that uses CardLayout
    
     
    public void addComponentToPane(Container pane) {
        //Put the JComboBox in a JPanel to get a nicer look.
        LoginView login = new LoginView();
        LobbyView lobby = new LobbyView();        
         
        //Create the "cards".

        
         
        //Create the panel that contains the "cards".
        cards = new JPanel((LayoutManager) new CardLayout());
        cards.add(login, "Login");
        cards.add(lobby, "Lobby");
        
        pane.add(lobby, BorderLayout.CENTER); 
        pane.add(login, BorderLayout.PAGE_START);
        
    }
        
        public void change(String evt) {
            CardLayout cl = (CardLayout)(cards.getLayout());
            cl.next(cards);
    }
     
    /** 
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event dispatch thread.
     */
    public static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("Gomoku");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         
        //Create and set up the content pane.
        Layout demo = new Layout();
        demo.addComponentToPane(frame.getContentPane());
         
        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }
    public void itemStateChanged(ItemEvent evt) {
    CardLayout cl = (CardLayout)(cards.getLayout());
    cl.show(cards, (String)evt.getItem());
}
  
    
    public static void main(String[] args) {
        /* Use an appropriate Look and Feel */
        try {
            //UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
        } catch (UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        } catch (InstantiationException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        /* Turn off metal's use of bold fonts */
        UIManager.put("swing.boldMetal", Boolean.FALSE);
         
        //Schedule a job for the event dispatch thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}
