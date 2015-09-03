package com.tonini.diego.musicat.events;

/**
 * Created by Diego on 01/08/2015.
 */
public class EventSearch {

    private String query;

    public EventSearch(String query){
        this.query = query;
    }

    public String getQuery() {
        return query;
    }
}
