package com.tonini.diego.musicat.entity;

/**
 * Created by Diego on 29/07/2015.
 */
public class PlayList extends Item {

    private long id;

    public PlayList(){}

    public void setTitle(String title){
        super.setFirstTitle(title);
    }

    public void setTrackNumber(int number){
        super.setSecondTitle(String.valueOf(number));
    }

    public String getTitle(){
        return super.getFirstTitle();
    }

    public int getTrackNumber(){
        return Integer.parseInt(super.getSecondTitle());
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }
}
