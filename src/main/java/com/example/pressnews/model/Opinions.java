package com.example.pressnews.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Time;
import java.util.Date;

@Entity
public class Opinions {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private long author_id;
    private Date createDate;
    private long createTime;
    private byte[] img;
    private long views;
    private String link_name;
    private String author;
    private String author_position;
    private String title;
    private String text;
    private String author_position_kaz;
    private String title_kaz;
    private String text_kaz;
    private String link_name_kaz;

    public Opinions() {

    }

    public Opinions(long id, long author_id, Date createDate, long createTime, byte[] img, long views, String link_name, String author, String author_position, String title, String text, String author_position_kaz, String title_kaz, String text_kaz, String link_name_kaz) {
        this.id = id;
        this.author_id = author_id;
        this.createDate = createDate;
        this.createTime = createTime;
        this.img = img;
        this.views = views;
        this.link_name = link_name;
        this.author = author;
        this.author_position = author_position;
        this.title = title;
        this.text = text;
        this.author_position_kaz = author_position_kaz;
        this.title_kaz = title_kaz;
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

    public String getLink_name() {
        return link_name;
    }

    public void setLink_name(String link_name) {
        this.link_name = link_name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAuthor_position() {
        return author_position;
    }

    public void setAuthor_position(String author_position) {
        this.author_position = author_position;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getAuthor_position_kaz() {
        return author_position_kaz;
    }

    public void setAuthor_position_kaz(String author_position_kaz) {
        this.author_position_kaz = author_position_kaz;
    }

    public String getTitle_kaz() {
        return title_kaz;
    }

    public void setTitle_kaz(String title_kaz) {
        this.title_kaz = title_kaz;
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
