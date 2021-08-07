package com.example.stickynotes;

public class Task {
    private long ID;
    private String title;
    private String date;
    private String time;
    private String email;
    private int status;

    Task() {

    }

    Task(String title, String date, String time, String email, int status) {
        this.title = title;
        this.date = date;
        this.time = time;
        this.email = email;
        this.status = status;
    }

    Task(long ID, String title, String date, String time, String email, int status){
        this.ID = ID;
        this.title = title;
        this.date = date;
        this.time = time;
        this.email = email;
        this.status = status;
    }
    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
