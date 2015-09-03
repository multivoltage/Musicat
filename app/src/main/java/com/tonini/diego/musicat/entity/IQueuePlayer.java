package com.tonini.diego.musicat.entity;

/**
 * Created by Diego on 17/05/2015.
 */
public interface IQueuePlayer extends IBasicPlayer {

    public int addToQueue(Track track);
    public int removeFromQueue(Track track);
    public boolean isInQueue(Track track);
}
