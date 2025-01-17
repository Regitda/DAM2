package com.project2.project2.MessageLogic;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

public class MessageLogger {
    private static final String MESSAGE_FILE = "chat_messages.txt";

    // Logging system TODO add reader
    public synchronized void logMessage(String message) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(MESSAGE_FILE, true))) {
            String timestamp = LocalDateTime.now().toString();
            writer.write("[" + timestamp + "] " + message);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
