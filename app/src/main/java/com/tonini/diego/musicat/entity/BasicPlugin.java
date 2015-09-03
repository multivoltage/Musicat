package com.tonini.diego.musicat.entity;

import java.util.List;

/**
 * Created by Diego on 16/05/2015.
 */
public class BasicPlugin implements IPlugin {

    @Override
    public Track getNextIndex(List<Track> tracks, Track currentTrack) {
        int index = tracks.indexOf(currentTrack);
        if(index==tracks.size()-1)
            return tracks.get(0);
        else
            return tracks.get(index+1);
    }

    @Override
    public Track getPrevIndex(List<Track> tracks, Track currentTrack) {
        int index = tracks.indexOf(currentTrack);
        if(index==0)
            return tracks.get(tracks.size()-1);
        else
            return tracks.get(index-1);
    }
}
