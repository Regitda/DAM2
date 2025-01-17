package com.project2.project2.Controllers;

import com.project2.project2.Client.ChatClientThread;
import com.project2.project2.MessageLogic.MessageLogger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class ChatUIController {



    @FXML private TextArea chatArea;
    @FXML private TextField messageField;
    @FXML private Button sendButton; // TODO refractor
    @FXML private Label userId;

    private static final int PORT = 5000;

    private Socket socket;
    private PrintWriter out;
    private String username;
    private MessageLogger messageLogger;

    public void setUsername(String username) {
        this.username = username;
        userId.setText(username);
    }

    @FXML
    public void initialize() {
        messageLogger = new MessageLogger();
    }
    public void startChat() {
        try {
            socket = new Socket("localhost", PORT);
            out = new PrintWriter(socket.getOutputStream(), true);

            // Lets hope it finally works now.
            out.println(username);

            ChatClientThread clientThread = new ChatClientThread(socket, message -> {
                Platform.runLater(() -> {
                    chatArea.appendText(message + "\n");
                    messageLogger.logMessage(message);
                });
            });
            clientThread.start();


        } catch (IOException e) {
            e.printStackTrace();
            chatArea.appendText("No se pudo conectar al servidor.\n");
        }
    }


    @FXML
    private void handleSendAction(ActionEvent event) {
        String text = messageField.getText().trim();
        if (text.isEmpty()) {
            return;
        }

        out.println(text);
        messageField.clear();

        messageLogger.logMessage(text);
    }

    public void closeConnection() {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}