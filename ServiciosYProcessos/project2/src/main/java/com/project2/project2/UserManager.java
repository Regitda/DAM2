package com.project2.project2;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import org.mindrot.jbcrypt.BCrypt;

public class UserManager {
    private static final String USER_FILE = "users.ser";
    private final Map<String, String> users;

    public UserManager() {
        users = loadUsers();
    }

    // Yes, I know its bad to hold it in txt. But im tired
    @SuppressWarnings("unchecked")
    private Map<String, String> loadUsers() {
        File file = new File(USER_FILE);
        if (!file.exists()) {
            return new HashMap<>();
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (Map<String, String>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    // At least its encrypted password. Yoinked from stackoverflow
    private void saveUsers() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(USER_FILE))) {
            oos.writeObject(users);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean registerUser(String username, String password) {
        if (users.containsKey(username)) {
            return false; // User exists
        }
        String hashed = BCrypt.hashpw(password, BCrypt.gensalt());
        users.put(username, hashed);
        saveUsers();
        return true;
    }

    public boolean authenticate(String username, String password) {
        if (!users.containsKey(username)) {
            return false;
        }
        String hashed = users.get(username);
        return BCrypt.checkpw(password, hashed);
    }
}

