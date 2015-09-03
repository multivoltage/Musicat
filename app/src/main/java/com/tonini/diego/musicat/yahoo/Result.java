package com.tonini.diego.musicat.yahoo;

import com.tonini.diego.musicat.entity.Image;

/**
 * Created by Diego on 19/06/2015.
 */
public class Result {

    private String url;
    private int height;
    private int width;

    public Result(){
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public String toString(){
        return ""+url+", width: "+width+", height: "+height;
    }
}
