package com.multivoltage.musicat.events;

/**
 * Created by Diego on 07/08/2015.
 */
public class EventTabSelected {

    private int item = 0;

    public EventTabSelected(int item){
        this.item = item;
    }

    public int getItem() {
        return item;
    }
}
