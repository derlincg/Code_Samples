This is an echo client-server application. The clients send messages, and the server takes that message
and distributes that message to all the clients connected to the server. Both the Client and the Server
use MVC design.

TO RUN VIA COMMAND PROMPT:
1. navigate to the src folder of the server, and run: java Server2 [ip_of_copmputer] [port #]
2. click the start server button.
3. navigate to the src folder of the client, and run: java Client [ip_of_computer] [port #]
Feel free to connect as many clients as you want to the server.

RUN VIA NETBEANS:
1. open up both the client and server projects in NetBeans
2. Go to File -> project properties -> Run
3. in the arguments section, type in the ip and port number separated by a space. do this for both
the client and the server.
4. run the server and click start server.
5. run 1 or more clients.