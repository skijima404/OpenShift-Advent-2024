
package com.example.task.model;

import java.time.LocalDate;

public class Task {
    private String id;
    private String name;
    private String description;
    private LocalDate due;
    private String pri;
    private String status;

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public LocalDate getDue() { return due; }
    public void setDue(LocalDate due) { this.due = due; }
    public String getPri() { return pri; }
    public void setPri(String pri) { this.pri = pri; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
