package com.app.lfc.scooter.Model;

public class XeError {

    String name, kp, img;

    public XeError(){

    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public XeError(String name, String kp, String img) {
        this.name = name;
        this.kp = kp;
        this.img = img;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKp() {
        return kp;
    }

    public void setKp(String kp) {
        this.kp = kp;
    }
}
