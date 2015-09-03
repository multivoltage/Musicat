package com.tonini.diego.musicat.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Diego on 24/07/2015.
 */
public class Artist extends Item{


    public Artist(){}

    public void setName(String name){
        super.setFirstTitle(name);
    }

    public void setNumberAlbum(int numberAlbum){
        super.setSecondTitle(String.valueOf(numberAlbum));
    }

    public String getName(){

        return super.getFirstTitle();
    }

    public int getNumberAlbum(){
        return Integer.parseInt(super.getSecondTitle());
    }
}
