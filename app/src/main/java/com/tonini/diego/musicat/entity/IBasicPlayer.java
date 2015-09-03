package com.tonini.diego.musicat.entity;

import java.util.List;

/**
 * Created by Diego on 15/05/2015.
 */
public interface IBasicPlayer {

    public void setShuffleModeEnable(boolean active);
    public void setRepeatingMode(boolean active);
    public void play();
    public void playSpecificTrack(Track track);
    public void skeepTo(int pos);
    public void pause();
    public void next();
    public void prev();
    public void shutDown();
    public boolean isRepeatingMode();
    public boolean isShuffleMode();
    public int getTracksSize();
    public int getDuration();
    public int getCurrentPosition();
    public Track getCurrentTrack();
    public List<Track> getTracks();
}
