package com.dmb.pruebapi;

/**
 * Created by davidmari on 4/12/17.
 */

public class Champion {

    String name;
    String key;
    String image;

    public Champion(String n, String k, String i){
        this.name = n;
        this.key = k;
        this.image = i;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
