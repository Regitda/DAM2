package com.project2.project2.Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketException;
import java.util.function.Consumer;

public class ChatClientThread extends Thread {
    private final Socket socket;
    private final Consumer<String> messageConsumer;

    public ChatClientThread(Socket socket, Consumer<String> messageConsumer) {
        this.socket = socket;
        this.messageConsumer = messageConsumer;
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            String serverMessage;
            while ((serverMessage = in.readLine()) != null) {
                messageConsumer.accept(serverMessage);
            }
        } catch (SocketException se) {
            // No lotta red lines
            if ("Socket closed".equals(se.getMessage())) {
                System.out.println("La conexión se ha cerrado correctamente.");
            } else {
                se.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            messageConsumer.accept("Se ha cerrado la conexión con el servidor.");
        }
    }
}
