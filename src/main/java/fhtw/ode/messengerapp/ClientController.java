package fhtw.ode.messengerapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class ClientController {

    @FXML
    private Font x1;

    @FXML
    private Color x2;

    @FXML
    private Font x3;

    @FXML
    private Color x4;

    @FXML
    public void testServerConnection(ActionEvent event) {
        try {
            Socket client = new Socket("localhost", 4711);
            System.out.println("Client: connected to " + client.getInetAddress());
            InputStream in = client.getInputStream();
            byte[] b = new byte[12];
            int bytes = in.read(b);
            System.out.println("Client: received " + bytes + " Bytes from Server");
            String message = new String(b);
            System.out.println("Client: Message from Server: " + message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}



