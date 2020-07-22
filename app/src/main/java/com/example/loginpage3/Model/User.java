package com.example.loginpage3.Model;

public class User {

    private String username;
    private String password;
    private String phoneNo;

    public User(){

    }

    public User(String username, String password, String phoneNo) {
        this.username = username;
        this.password = password;
        this.phoneNo = phoneNo;
    }

    public User(String toString) {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }
}
