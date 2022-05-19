package com.example.pressnews.model;

import javax.persistence.*;
import java.sql.Time;
import java.util.Date;

@Entity
public class Analytics {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private long author_id;
    private String title;
    private String description;
    private String text;
    private java.util.Date createDate;
    private long createTime;
    private byte[] img;
    private long views;
    private long time_to_read;
    private String link_name;

    public Analytics () {

    }

    public Analytics(long id, long author_id, String title, String description, String text, Date createDate, long createTime, byte[] img, long views, long time_to_read, String link_name) {
        this.id = id;
        this.author_id = author_id;
        this.title = title;
        this.description = description;
        this.text = text;
        this.createDate = createDate;
        this.createTime = createTime;
        this.img = img;
        this.views = views;
        this.time_to_read = time_to_read;
        this.link_name = link_name;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public byte[] getImg() {
        return img;
    }

    public void setImg(byte[] img) {
        this.img = img;
    }

    public long getViews() {
        return views;
    }

    public void setViews(long views) {
        this.views = views;
    }

    public long getTime_to_read() {
        return time_to_read;
    }

    public void setTime_to_read(long time_to_read) {
        this.time_to_read = time_to_read;
    }

    public String getLink_name() {
        return link_name;
    }

    public void setLink_name(String link_name) {
        this.link_name = link_name;
    }
}
