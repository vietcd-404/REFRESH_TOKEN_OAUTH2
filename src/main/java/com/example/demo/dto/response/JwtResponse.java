package com.example.demo.dto.response;

import java.util.List;

public class JwtResponse {
    private String token;
    private String type ="Bearer";
    private String username;
    private String email;
    private String phone;
    private List<String> listRole;


    public JwtResponse(String token, String username, String email, String phone, List<String> listRole) {
        this.token = token;
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.listRole = listRole;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getListRole() {
        return listRole;
    }

    public void setListRole(List<String> listRole) {
        this.listRole = listRole;
    }
}
