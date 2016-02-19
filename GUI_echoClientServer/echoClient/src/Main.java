/*
 * Names: CARL DERLINE,
 *          Karen Bullinger
 * Class: CSCE 320
 * Date: March 2, 2015
 * Desc: Basic echo client with GUI. 
 * To run:  java Main [ip address] [port]
 * Resources: http://www.java2s.com/Tutorial/Java/0320__Network/EchoClient.htm
 *      
 */

public class Main {

    /**
     * @param args the command line arguments Launches GUI application using IP
     * address and port provided. Creates controller and view, associates them.
     * Begins new thread that listens for server messages.
     */
    public static void main(String[] args) {

        if (args.length != 2) {
            System.out.println("To run: java EchoClient [ip address] [port]");
            System.exit(1);
        }

        int port = Integer.parseInt(args[1]);
        echoClientController controller = new echoClientController(args[0], port);
        echoClientview view = new echoClientview();
        controller.setView(view);
        view.setController(controller);
        view.setVisible(true);
        controller.begin();
    }
}
