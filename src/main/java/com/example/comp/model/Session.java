package com.example.comp.model;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "session")
public class Session {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private int id;
    private String createdBy;
    private String token;
    private String joinedBy;
    private String who_won;
    private String question_id;


    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getJoinedBy() {
        return joinedBy;
    }

    public void setJoinedBy(String joinedBy) {
        this.joinedBy = joinedBy;
    }

    public String getWho_won() {
        return who_won;
    }

    public void setWho_won(String who_won) {
        this.who_won = who_won;
    }

    public String getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(String question_id) {
        this.question_id = question_id;
    }


}
