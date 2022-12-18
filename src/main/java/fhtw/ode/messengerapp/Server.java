package fhtw.ode.messengerapp;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/*
 *   Messaging Format:          /command target "message"
 *   Broadcast example:         /broadcast "Hallo Welt!"
 *   Client Target example:     /send Thomas "Hallo wie gehts?"
 */

//TODO: Anhängen bzw. Entfernen der Protokoll-Commandos sollte automatisch im Hintergrund passieren
//TODO: MessageSendingProtokoll könnte man noch besser gestalten, da aktuell keine Leerzeichen in der User-Nachricht sein dürfen

public class Server {
    //----------------------- Class Variables ---------------------------------------------------------
    private static ServerSocket server;
    private static List<Socket> ClientSocketList;
    private static ServerLogger logger;


    //----------------------- Class Methods ---------------------------------------------------------

    /**
     *
     * @param args: standard args from outside
     * @throws IOException: If there is a problem with the logger file, an exception is thrown
     */
    public static void main(String[] args) throws IOException {

        //Setup Server
        logger = new ServerLogger("log-file.txt");
        logger.log("New log-file created");

        server = new ServerSocket(4711);
        logger.log("Server at Port 4711");

        ClientSocketList = new ArrayList<>();

        //Run Server
        runServer();

        //Server-Ressourcen Freigeben
        server.close();
        logger.close();
    }

    /**
     * This Function is responsible for handling the server at runtime
     */
    static void runServer() {
        try {

            while (true){
                // Accept incoming connections
                Socket socket = server.accept();

                /*
                //Frage ab, ob der Client bereits mit dem Server verbunden ist
                if(ClientSocketList.contains(socket)) {
                    logger.log("Client " + socket.getInetAddress() + " is already connected");
                    continue;
                }
                */


                //Add Client Socket to Socket List
                ClientSocketList.add(socket);
                logger.log("New Client from: " + socket.getInetAddress());

                // Create a new thread for the Client to handle the connection
                Thread thread = new Thread(new ClientHandler(socket));
                logger.log("Started Thread for Client " + socket.getInetAddress());
                thread.start();
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     *
     * @param message: The message to be broadcasted
     */
    public static void broadcast(String message) throws IOException {
        for (Socket socket : ClientSocketList) {
            try {
                OutputStream output = socket.getOutputStream();
                PrintWriter writer = new PrintWriter(output, true);
                writer.println(message);
            } catch (IOException e) {
                e.printStackTrace();
                logger.log("Something went wrong during broadcasting to client " + socket.getInetAddress());
                continue;
            }
            logger.log("Broadcast message sent to Client: " + socket.getInetAddress());
        }
    }


    /**
     *
     * @param target: The Target Client to which the message should be sent
     * @param message: The Message to be sent to the client
     */
    public static void send(String target, String message) {
        //go through every client in the registered client list
        for (Socket socket : ClientSocketList) {
            try {
                // Get clients hostname
                String hostname = socket.getInetAddress().getHostName();

                // check if clients hostname matches the target the message should be sent to
                if (hostname.equals(target)) {
                    OutputStream output = socket.getOutputStream();
                    PrintWriter writer = new PrintWriter(output, true);
                    writer.println(message);
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    static class ClientHandler implements Runnable {
        private Socket socket;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                // Read the message from the client
                System.out.println("Server: connected to Client " + socket.getInetAddress());
                InputStream input = socket.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                String message = reader.readLine();

                // Write the message to the log-file
                logger.log("Client " + socket.getInetAddress() + " sent => " + message);

                // Parse the message to see if it is a broadcast or a targeted message
                String[] parts = message.split(" ");
                if (parts[0].equals("/broadcast")) {
                    // Broadcast the message to all clients
                    Server.broadcast(parts[1]);
                } else if (parts[0].equals("/send")) {
                    // Send the message to the specified target
                    Server.send(parts[1], parts[2]);
                } else {
                    // Invalid command, send an error message back to the client
                    OutputStream output = socket.getOutputStream();
                    PrintWriter printWriter = new PrintWriter(output, true);
                    printWriter.println("Invalid command");
                    logger.log("There was an invalid command from clien " + socket.getInetAddress());
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}