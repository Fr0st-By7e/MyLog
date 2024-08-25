package com.example.test;

import java.io.Serializable;

public class Task implements Serializable {
    private String description;
    private String timestamp;
    private String dueDate;

    public Task(String description, String timestamp, String dueDate) {
        this.description = description;
        this.timestamp = timestamp;
        this.dueDate = dueDate;
    }

    public String getDescription() {
        return description;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) { //scratch that
        this.timestamp = timestamp;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    @Override
    public String toString() {
        return description + "\n\n[Created: " + timestamp + "]\n[Due: " + dueDate + "]";
    }
}

