package fhtw.ode.messengerapp;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.*;
import java.net.Socket;

//TODO: Die gsendeten Daten müssen auf auf das Protokoll adaptiert werden => hinzufügen der commands etc
//TODO: Das gleiche für das Empfangen der Daten => Abfrage auf command und dann zuordnung zum richtigen Chat

public class ClientController {

    //----------------------- Class Variables ---------------------------------------------------------
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


    //----------------------- ClientController Methods ---------------------------------------------------------
    @FXML
    public void connectToServer(ActionEvent event) throws IOException {
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

        //Here Add the Protocol Header according to the target (Broadcast oder Client)

        // Send the message to the server
        OutputStream output = clientSocket.getOutputStream();
        PrintWriter writer = new PrintWriter(output, true);
        writer.println(message);
    }


    @FXML
    private void initialize() {
        try {
            // Create a buffered reader to read messages from the server
            BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            // Start a new thread to listen for incoming messages from the server
            new Thread(() -> {
                while (true) {
                    try {
                        // Read the message sent by the server
                        String message = input.readLine();

                        // Update the GUI with the message
                        Platform.runLater(() -> {
                            txt_log.appendText(message + "\n");
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}