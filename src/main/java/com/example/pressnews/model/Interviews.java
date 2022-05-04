package com.example.pressnews.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Date;
import java.sql.Time;

@Entity
public class Interviews {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private long author_id;
    private String title;
    private java.sql.Date date;
    private java.sql.Time time;
    private String img;
    private String text;
    private long views;
    private int time_to_read;

    public Interviews () {

    }

    public Interviews(long id, long author_id, String title, Date date, Time time, String img, String text, long views, int time_to_read) {
        this.id = id;
        this.author_id = author_id;
        this.title = title;
        this.date = date;
        this.time = time;
        this.img = img;
        this.text = text;
        this.views = views;
        this.time_to_read = time_to_read;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getAuthor_id() {
        return author_id;
    }

    public void setAuthor_id(long author_id) {
        this.author_id = author_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getViews() {
        return views;
    }

    public void setViews(long views) {
        this.views = views;
    }

    public int getTime_to_read() {
        return time_to_read;
    }

    public void setTime_to_read(int time_to_read) {
        this.time_to_read = time_to_read;
    }
}
