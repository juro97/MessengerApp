package fhtw.ode.messengerapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ClientController {

    private Socket clientSocket;

    @FXML
    private Button btn_send;

    @FXML
    private Button btn_testServerConnection;

    @FXML
    private TextArea txt_log;

    @FXML
    private TextField txt_message;

    @FXML
    private TextArea txt_receivedMessage;

    @FXML
    private Font x1;

    @FXML
    private Color x2;

    @FXML
    private Font x3;

    @FXML
    private Color x4;

    public ClientController() throws IOException {
    }

    @FXML
    public void testServerConnection(ActionEvent event) throws IOException {
        try {
            clientSocket = new Socket("localhost", 4711);
            System.out.println("Client: connected to " + clientSocket.getInetAddress());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void sendMessage(ActionEvent event) throws IOException {

        //Read Message from Message Field
        String message = txt_message.getText();

        // Send the message to the server
        OutputStream output = clientSocket.getOutputStream();
        PrintWriter writer = new PrintWriter(output, true);
        writer.println(message);

        // Receive a response from the server
        InputStream input = clientSocket.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        String response = reader.readLine();
        txt_receivedMessage.setText(response);

        // Close the socket
        clientSocket.close();

    }

}








