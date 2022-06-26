package com.example.pressnews.model;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;

@Entity
public class News {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private long author_id;
    private Timestamp createDate;
    private byte[] img;
    private long views;
    private String link_name;
    private String title;
    private String description;
    private String text;

    private String title_kaz;
    private String description_kaz;
    private String text_kaz;
    private String link_name_kaz;

    public News(){

    }

    public News(long id, long author_id, Timestamp createDate, byte[] img, long views, String link_name, String title, String description, String text, String title_kaz, String description_kaz, String text_kaz, String link_name_kaz) {
        this.id = id;
        this.author_id = author_id;
        this.createDate = createDate;
        this.img = img;
        this.views = views;
        this.link_name = link_name;
        this.title = title;
        this.description = description;
        this.text = text;
        this.title_kaz = title_kaz;
        this.description_kaz = description_kaz;
        this.text_kaz = text_kaz;
        this.link_name_kaz = link_name_kaz;
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

    public Timestamp getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
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

    public String getLink_name() {
        return link_name;
    }

    public void setLink_name(String link_name) {
        this.link_name = link_name;
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

    public String getTitle_kaz() {
        return title_kaz;
    }

    public void setTitle_kaz(String title_kaz) {
        this.title_kaz = title_kaz;
    }

    public String getDescription_kaz() {
        return description_kaz;
    }

    public void setDescription_kaz(String description_kaz) {
        this.description_kaz = description_kaz;
    }

    public String getText_kaz() {
        return text_kaz;
    }

    public void setText_kaz(String text_kaz) {
        this.text_kaz = text_kaz;
    }

    public String getLink_name_kaz() {
        return link_name_kaz;
    }

    public void setLink_name_kaz(String link_name_kaz) {
        this.link_name_kaz = link_name_kaz;
    }
}
