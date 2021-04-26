package com.example.CodeFellowship;

import javax.persistence.*;

import java.sql.Timestamp;
import java.util.Date;

@Entity
public class PostModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @ManyToOne
    ApplicationUser applicationUser;

    String body;
    Timestamp createdAt;

    public PostModel(String body,  ApplicationUser applicationUser) {
        this.body = body;
        this.applicationUser = applicationUser;
        this.createdAt = new Timestamp(new Date().getTime());
    }

    public PostModel() {
    }

    public int getId() {
        return id;
    }


    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

}
