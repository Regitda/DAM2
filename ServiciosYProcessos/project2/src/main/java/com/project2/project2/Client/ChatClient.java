package com.project2.project2.Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.function.Consumer;

public class ChatClient {
    private static final String LOCALHOST = "localhost";
    private static final int PORT = 5000;

    public static void main(String[] args) {
        System.out.println("Conectándose al servidor de chat en " + LOCALHOST + ":" + PORT);
        try (
                Socket socket = new Socket(LOCALHOST, PORT);
                BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
        ) {
            // Debug printer
            Consumer<String> consoleConsumer = System.out::println;

            ChatClientThread clientThread = new ChatClientThread(socket, consoleConsumer);
            clientThread.start();

            // Read from console to get input
            String userInput;
            while ((userInput = stdIn.readLine()) != null) {
                out.println(userInput);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
