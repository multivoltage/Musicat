package com.multivoltage.musicat.entity;

import java.util.List;

/**
 * Created by Diego on 15/05/2015.
 */
public interface IPlugin {

    public Track getNextIndex(List<Track> tracks,Track currentTrack);

    public Track getPrevIndex(List<Track> tracks,Track currentTrack);

}
