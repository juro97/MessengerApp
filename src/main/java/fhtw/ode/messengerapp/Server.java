package fhtw.ode.messengerapp;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {
        example();
    }
    static void example() {
        try {
            ServerSocket server = new ServerSocket(4711);
            Socket client = server.accept();

            // Read the message from the client
            System.out.println("Server: connected to Client " + client.getInetAddress());
            InputStream input = client.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            String message = reader.readLine();

            // Write the message to a log file
            File logFile = new File("log.txt");
            BufferedWriter writer = new BufferedWriter(new FileWriter(logFile, true));
            writer.write(message);
            writer.newLine();
            writer.close();

            // Send a response to the client
            OutputStream output = client.getOutputStream();
            PrintWriter printer = new PrintWriter(output, true);
            printer.println("Message received and logged.");

            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}