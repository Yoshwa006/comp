package com.example.comp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class GenerateToken {
    private String jwt;
    private int questionId;

    // Getters and Setters
    public String getJwt() { return jwt; }
    public void setJwt(String jwt) { this.jwt = jwt; }

    public int getQuestionId() { return questionId; }
    public void setQuestionId(int questionId) { this.questionId = questionId; }
}

