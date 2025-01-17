package com.project2.project2.Server;

import com.project2.project2.MessageLogic.MessageLogger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

public class ChatServerThread extends Thread {
    private final Socket socket;
    private final List<ChatServerThread> clientThreads;
    private PrintWriter out;
    private String userName;
    private final MessageLogger messageLogger;

    public ChatServerThread(Socket socket, List<ChatServerThread> clientThreads) {
        this.socket = socket;
        this.clientThreads = clientThreads;
        this.messageLogger = new MessageLogger();
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            out = new PrintWriter(socket.getOutputStream(), true);

            userName = in.readLine();
            if (userName == null || userName.trim().isEmpty()) {
                userName = "Anon-" + this.getId();
            }

            broadcastMessage(">> " + userName + " se ha unido al chat.");
            messageLogger.logMessage(">> " + userName + " se ha unido al chat.");

            String msg;
            while ((msg = in.readLine()) != null) {
                String formattedMessage = userName + ": " + msg;
                broadcastMessage(formattedMessage);
                messageLogger.logMessage(formattedMessage);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    private void broadcastMessage(String message) {
        System.out.println("[Servidor] " + message); // Log en consola servidor
        synchronized (clientThreads) {
            for (ChatServerThread clientThread : clientThreads) {
                clientThread.sendMessage(message);
            }
        }
    }

    // Sends out the message
    private void sendMessage(String message) {
        if (out != null) {
            out.println(message);
        }
    }

    // Cleanup
    private void closeConnection() {
        synchronized (clientThreads) {
            clientThreads.remove(this);
        }
        broadcastMessage("<< " + userName + " ha salido del chat.");
        messageLogger.logMessage("<< " + userName + " ha salido del chat.");
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
