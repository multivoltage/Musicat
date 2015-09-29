package com.multivoltage.musicat.entity;

import java.util.List;
import java.util.Random;

/**
 * Created by Diego on 16/05/2015.
 */
public class RandomPlugin implements IPlugin {
    @Override
    public Track getNextIndex(List<Track> tracks, Track currentTrack) {
        return tracks.get(new Random().nextInt(tracks.size()-1));
    }

    @Override
    public Track getPrevIndex(List<Track> tracks, Track currentTrack) {
        return tracks.get(new Random().nextInt(tracks.size()-1));
    }
}
