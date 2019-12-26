package com.example.myapplication.model;

public class Account {

    private static Account instance = new Account();
    private String accountNumber;
    private String password;

    private Account(){};

    public static Account getInstance(){
        return instance;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}
