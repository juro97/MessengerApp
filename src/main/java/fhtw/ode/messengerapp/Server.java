package fhtw.ode.messengerapp;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {
        example();
    }
    static void example() {
        try {
            ServerSocket server = new ServerSocket(4711);
            System.out.println("Server: waiting for connection");
            Socket client = server.accept();
            System.out.println("Server: connected to Client " +
                    client.getInetAddress());
            OutputStream stream = client.getOutputStream();
            String message = "Hello Client";
            byte[] bmessage = message.getBytes();
            stream.write(bmessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
