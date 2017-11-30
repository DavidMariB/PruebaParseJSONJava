package com.dmb.pruebapi;

import android.widget.ImageView;

/**
 * Created by davidmari on 30/11/17.
 */

public class Match {

    private int champImg;
    private String score,result,champName,lane;

    public Match(int ci, String s, String r, String cn,String l){

        this.champImg = ci;
        this.score = s;
        this.result = r;
        this.champName = cn;
        this.lane = l;
    }

    public int getChampImg() {
        return champImg;
    }

    public void setChampImg(int champImg) {
        this.champImg = champImg;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getChampName() {
        return champName;
    }

    public void setChampName(String champName) {
        this.champName = champName;
    }

    public String getLane() {
        return lane;
    }

    public void setLane(String lane) {
        this.lane = lane;
    }
}
