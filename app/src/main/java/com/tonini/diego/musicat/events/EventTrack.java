package com.tonini.diego.musicat.events;

import com.tonini.diego.musicat.entity.Track;

import java.util.ArrayList;
import java.util.List;

public class EventTrack{

    private String event = "not setted - ERROR :(";
    private Track track = null;
    private int duration = 0;
    private int current = 0;
    private int currentIndexList = 0;
    private String className;
    private List<Track> list = new ArrayList<Track>();
    public static final int IS_PLAY = 0;
    private int state;
    private String text = "";

    public EventTrack(String event,String className/*boolean isRequest*/){
        this.event = event;
        this.className = className;
        //this.isRequest = isRequest;
    }

    public EventTrack withTrack(Track track){
        this.track = track;
        return this;
    }
    public EventTrack withDuration(int duration){
        this.duration = duration;
        return this;
    }
    public EventTrack withCurrent(int current){
        this.current = current;
        return this;
    }
    public EventTrack withTracks(List<Track> list){
        this.list.clear();
        this.list.addAll(list);
        return this;
    }
    public EventTrack withState(int state){
        this.state = state;
        return this;
    }

    public int getState() {
        return state;
    }

    public EventTrack withCurrentIndexList(int currentIndexList){
        this.currentIndexList = currentIndexList;
        return this;
    }

    public String getClassName() {
        return className;
    }

    public int getDuration() {
        return duration;
    }

    public int getCurrent() {
        return current;
    }

    public String getEvent() {
        return event;
    }

    public Track getTrack() {
        return track;
    }

    public int getCurrentIndexList() {
        return currentIndexList;
    }


    public List<Track> getList() {
        return list;
    }


    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
