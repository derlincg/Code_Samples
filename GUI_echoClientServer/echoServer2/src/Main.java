/*
 * Names: Carl Derline
 *        Karen Bullinger
 * Class: CSCE 320
 * Date: March 9, 2015
 * Desc: Basic Echo Server with GUI.
 * To run:  java Main [ip address] [port]
 * Resources: Dr. Hauser
 *      
 */
public class Main {

    /**
     * @param args the command line arguments
     * Main Method. Creates a model, view, and a controller. The parameters
     * for the model are the model, view, InetAddress, and port.
     * 
     * View is then associated with the controller and is set to be visible.
     */
    public static void main(String[] args) {
       if(args.length != 2){
           System.out.println("To run:  java EchoServer [ip address] [port]");
           System.exit(0);
       }
               serverController controller;

        serverModel model = new serverModel();
        serverView view = new serverView();

        controller = 
         new serverController(model, view, Integer.parseInt(args[1]), args[0]);
        
        view.setController(controller);

        view.setVisible(true);


    }
}
