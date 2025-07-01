package com.example.comp.dto;

import lombok.Data;

@Data
public class AuthResponce {

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    String token;
}
