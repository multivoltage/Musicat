package com.multivoltage.musicat.entity;

import android.content.Context;
import android.util.Log;

import com.multivoltage.musicat.MainActivity;

import java.util.ArrayList;
import java.util.List;


public class QueuePlayer extends BasicPlayer implements  IQueuePlayer{

    private List<Track> queue = new ArrayList<Track>();
    int queueIndex = 0;
    private int ADDED = 1;
    private int NOT_ADDED = -1;
    private int REMOVED = 2;
    private int NOT_REMOVED = -2;
    private Track currentTrack;


    public QueuePlayer(Context context) {
        super(context);
    }

    @Override
    public int addToQueue(Track track) {
        if(isInQueue(track))
            return NOT_ADDED;

        if(queue.isEmpty())
            currentTrack = track;

        queue.add(track);
        Log.i(MainActivity.TAG, "added to queue: " +track.getTitle());


        return ADDED;
    }

    @Override
    public int removeFromQueue(Track track) {
       if(!isInQueue(track))
           return NOT_REMOVED;

        queue.remove(track);
        return REMOVED;
    }

    @Override
    public boolean isInQueue(Track track) {
        return queue.contains(track);
    }

    @Override
    public void next() {
        if(queue.size()>0){
            currentTrack = mMode.getNextIndex(queue, currentTrack);
            playSpecificTrack(currentTrack);
            queue.remove(currentTrack);
        } else {
            super.next();
        }
    }

}
