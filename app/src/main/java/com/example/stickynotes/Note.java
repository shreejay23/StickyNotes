package com.example.stickynotes;

public class Note {

    long ID;
    String title;
    String content;
    String date;
    String time;
    String email;

    Note(){

    }

    Note(String title, String content, String date, String time, String email){   // , String email
        this.title = title;
        this.content = content;
        this.date = date;
        this.time = time;
        this.email = email;
    }

    Note(long ID, String title, String content, String date, String time, String email){          //String email
        this.ID = ID;
        this.title = title;
        this.content = content;
        this.date = date;
        this.time = time;
        this.email = email;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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
}
