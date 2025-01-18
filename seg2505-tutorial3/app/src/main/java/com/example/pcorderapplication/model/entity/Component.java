package com.example.pcorderapplication.model.entity;


public class Component {
    private String type;
    private String subtype;
    private String title;
    private int quantity;
    private String comment;
    private String creation_Date;
    private String modification_date;

    private int requestCount; //je vais peut etre l'effacer

    public Component(String title, String type, String subtype, int quantity, String comment, String creation_Date, String modification_date) {
        this.title = title;
        this.type = type;
        this.subtype = subtype;
        this.quantity = quantity;
        this.comment = comment;
        this.creation_Date = creation_Date;
        this.modification_date = modification_date;
        this.requestCount = requestCount;
    }
    public Component(){}

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSubtype() {
        return subtype;
    }

    public void setSubtype(String subtype) {
        this.subtype = subtype;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCreation_Date() {
        return creation_Date;
    }

    public void setCreation_Date(String creation_Date) {
        this.creation_Date = creation_Date;
    }

    public String getModification_date() {
        return modification_date;
    }

    public void setModification_date(String modification_date) {
        this.modification_date = modification_date;
    }

    //je vais peut etre les effacer
    public int getRequestCount() {
        return requestCount;
    }

    public void setRequestCount(int requestCount) {
        this.requestCount = requestCount;
    }
}
