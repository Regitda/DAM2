package com.project2.project2.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ChatServer {

    // All client threads.
    private static final List<ChatServerThread> clientThreads = new ArrayList<>();

    private static final int PORT = 5000;

    public static void main(String[] args) {
        System.out.println("Iniciando servidor de chat en el puerto " + PORT + "...");
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                // Waits for requests
                Socket clientSocket = serverSocket.accept();
                System.out.println("Cliente conectado: " + clientSocket.getInetAddress());

                // Starts thread for communication
                ChatServerThread clientThread = new ChatServerThread(clientSocket, clientThreads);
                synchronized (clientThreads) {
                    clientThreads.add(clientThread);
                }
                clientThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

