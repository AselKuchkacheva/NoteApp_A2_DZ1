package com.example.noteapp_a2.models;

import java.io.Serializable;

public class Note implements Serializable {
    private String title;
    private String createdAt;


    public Note(String title, String createdAt) {
        this.title = title;
        this.createdAt = createdAt;
    }
    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
