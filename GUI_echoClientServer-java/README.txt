This is an echo client-server GUI application. The clients send messages, and the server takes that message
and distributes that message to all the clients connected to the server (Essentially a group chat). 
Both the Client and the Server use MVC design.

RUN VIA COMMAND PROMPT:
1. navigate to the src folder of the server, and run: java Server2 [ipv4_of_copmputer] [port #]
2. click the start server button.
3. navigate to the src folder of the client, and run: java Client [ipv4 of computer running the server] [port #]
Feel free to connect as many clients as you want to the server, on the same computer or on a different computer.

RUN VIA NETBEANS:
1. open up both the client and server projects in NetBeans
2. For the Server, Go to File -> project properties -> Run
3. In the arguments section, type in the ip address of the computer in which the server is running, and port number separated by a space.
4. Run the server program and click "start server".
5. Client - follow steps 2 and 3.
5. Run 1 or more clients.