package com.project2.project2;

import java.io.Serializable;

public class UsersSerializable implements Serializable {

    //TODO make it actually usefull :)
    private final String username;
    private final String passwordHash;

    public UsersSerializable(String username, String passwordHash) {
        this.username = username;
        this.passwordHash = passwordHash;
    }

    public String getUsername() {
        return username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }


}
