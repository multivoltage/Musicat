package com.tonini.diego.musicat.entity;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.util.Log;

import com.tonini.diego.musicat.MainActivity;
import com.tonini.diego.musicat.Utils;

import java.io.IOException;
import java.util.List;

/**
 * Created by Diego on 15/05/2015.
 */
public abstract class BasicPlayer implements IBasicPlayer, MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener {

    protected IPlugin mMode;
    protected List<Track> mTracks;
    private Track currentTrack; // get from shared pref
    protected MediaPlayer mMediaPlayer;
    private boolean repeating = false;
    private boolean shuffle = false;
    private Context mContext;
    private OnCompleteCallBack mCallBackOnComplete;

    public BasicPlayer(Context context){
        mContext = context;
        mTracks = new TrackFinder(mContext).getTracks(MediaStore.Audio.Media.TITLE);
        mMode = new BasicPlugin();
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setWakeMode(context, PowerManager.PARTIAL_WAKE_LOCK);
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setOnCompletionListener(this);
        mMediaPlayer.setOnErrorListener(this);

        Uri savedUri = Utils.loadLastTrack(mContext);
        boolean found = false;
        for(Track track : mTracks) {
            if(savedUri.toString().equals(track.getTrackUri().toString())) {
                found = true;
                currentTrack = track;
                break;
            }
        }
        if(!found)
            currentTrack = mTracks.get(0);

        try {
            mMediaPlayer.setDataSource(mContext,getCurrentTrack().getTrackUri());
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            mMediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void setShuffleModeEnable(boolean active) {
        if(active) {
            mMode = new RandomPlugin();
            shuffle = true;

        }  else {
            mMode = new BasicPlugin();
            shuffle = false;
        }
    }

    @Override
    public void setRepeatingMode(boolean repeating) {
        this.repeating = repeating;
    }


    @Override
    public void next() {
        currentTrack = mMode.getNextIndex(mTracks, currentTrack);
        mMediaPlayer.reset();

            updateDataSource();
            try {
                mMediaPlayer.prepare();
                play();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    @Override
    public void prev() {
        currentTrack = mMode.getPrevIndex(mTracks, currentTrack);
        mMediaPlayer.reset();

            updateDataSource();
            try {
                mMediaPlayer.prepare();
                play();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    @Override
    public int getTracksSize() {
        return mTracks.size();
    }

    @Override
    public int getDuration() {
        return mMediaPlayer.getDuration();
    }

    @Override
    public int getCurrentPosition() {
        return mMediaPlayer.getCurrentPosition();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if(repeating)
            play();
        else
            next();

        mCallBackOnComplete.playAtComplete();
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public void play() {
        mMediaPlayer.start();
    }

    @Override
    public void pause() {
        mMediaPlayer.pause();
    }

    @Override
    public void shutDown() {
        mMediaPlayer.stop();
        mMediaPlayer.release();
    }

    public final int getIndexOfCurrent(){
        return mTracks.indexOf(currentTrack);
    }

    public boolean isPlaying() {
        return mMediaPlayer != null && mMediaPlayer.isPlaying();
    }

    protected void updateDataSource(){
        // save on pref last track
        Utils.saveLastTrack(currentTrack.getTrackUri(),mContext);
        try {
            mMediaPlayer.setDataSource(mContext,currentTrack.getTrackUri());
         } catch (IOException e) {
            Log.i(MainActivity.TAG,e.toString());
        }
    }

    @Override
    public Track getCurrentTrack(){
        return currentTrack;
    }

    @Override
    public void playSpecificTrack(Track track){
        currentTrack = track;
        mMediaPlayer.reset();
        updateDataSource();

        try {
            mMediaPlayer.prepare();
            mMediaPlayer.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void skeepTo(int pos){
        mMediaPlayer.seekTo(pos);
    }

    @Override
    public boolean isRepeatingMode(){
        return repeating;
    }

    @Override
    public boolean isShuffleMode() {
        return shuffle;
    }

    @Override
    public List<Track> getTracks(){
        return mTracks;
    }

    public interface OnCompleteCallBack {
        void playAtComplete();
    }

    public void setOnPlayAtCompleteListener(OnCompleteCallBack listener) {
        mCallBackOnComplete = listener;
    }

}
