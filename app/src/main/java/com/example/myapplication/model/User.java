package com.example.myapplication.model;

public class User {
    private static User instance = new User();
    private int Id;
    private String Username;
    private String password;

    private User(){};

    public static User getInstance(){
        return instance;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
