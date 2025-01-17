package com.project2.project2;

import com.project2.project2.Server.ChatServer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class Start extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        // Starts the login page
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("loginUI.fxml")));
        Scene scene = new Scene(root, 400, 300);
        stage.setTitle("Iniciar sesión / Registrar");
        stage.setScene(scene);
        stage.show();

        // Threading the server
        new Thread(() -> {
            ChatServer.main(new String[]{});}).start();
    }

    public static void main(String[] args) {
        launch();
    }
}
