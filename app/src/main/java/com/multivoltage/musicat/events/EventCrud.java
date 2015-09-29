package com.multivoltage.musicat.events;

/**
 * Created by Diego on 01/08/2015.
 */
public class EventCrud {

    private String operation;

    public EventCrud(String operation){
        this.operation = operation;
    }

    public String getOperation() {
        return operation;
    }
}
