package com.example.carwash.entity;



public class AuthRequest {


    private String username;

    private String password;

//    private String role;  // New field for role

    // Constructors
    public AuthRequest() {}

    public AuthRequest(String username, String password,String role) {
        this.username = username;
        this.password = password;
//        this.role=role;
    }

    // Getter and Setter for username
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    // Getter and Setter for password
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

//     Getter and Setter for role
//    public String getRole() {
//        return role;
//    }
//    public void setRole(String role) {
//        this.role = role;
//    }
}