package com.app.lfc.scooter.Model;

public class Fanpage {
    String name, link;
    long id;

    public Fanpage(){

    }

    public Fanpage(String name, String link, long id) {
        this.name = name;
        this.link = link;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
