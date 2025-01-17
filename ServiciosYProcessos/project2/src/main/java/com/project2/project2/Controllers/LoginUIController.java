package com.project2.project2.Controllers;

import com.project2.project2.Controllers.ChatUIController;
import com.project2.project2.UserManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginUIController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label statusLabel;

    private final UserManager userManager;

    public LoginUIController() {
        userManager = new UserManager();
    }


    @FXML
    private void handleLoginAction() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            statusLabel.setText("Por favor, rellena ambos campos.");
            return;
        }

        if (userManager.authenticate(username, password)) {
            statusLabel.setText("Inicio de sesión exitoso.");
            launchChatUI(username);
        } else {
            statusLabel.setText("Usuario o contraseña incorrectos.");
        }
    }

    @FXML
    private void handleRegisterAction() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            statusLabel.setText("Por favor, rellena ambos campos.");
            return;
        }

        if (userManager.registerUser(username, password)) {
            statusLabel.setText("Registro exitoso. Ahora puedes iniciar sesión.");
        } else {
            statusLabel.setText("El usuario ya existe.");
        }
    }

    private void launchChatUI(String username) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/project2/project2/chatUI.fxml"));
            Parent root = loader.load();

            ChatUIController chatController = loader.getController();
            chatController.setUsername(username);  // Pass the username so you can have nice message

            // New window
            Stage chatStage = new Stage();
            chatStage.setScene(new Scene(root));
            chatStage.setTitle("Chat - " + username);
            chatStage.show();

            // Start connection after all that was done
            chatController.startChat();

            chatStage.setOnCloseRequest(event -> {
                chatController.closeConnection();
            });

        } catch (IOException e) {
            e.printStackTrace();
            statusLabel.setText("Error al cargar la interfaz de chat.");
        }
    }
}
