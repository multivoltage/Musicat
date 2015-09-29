package com.multivoltage.musicat.events;

import com.multivoltage.musicat.entity.Album;
import com.multivoltage.musicat.entity.Artist;
import com.multivoltage.musicat.entity.PlayList;
import com.multivoltage.musicat.entity.Track;
import com.multivoltage.musicat.recycleviewlist.LoaderRecycleAsynk;

/**
 * Created by Diego on 01/08/2015.
 */
public class EventClick {

    private Track track;
    private Album album;
    private Artist artist;
    private PlayList playList;
    private LoaderRecycleAsynk.Type itemType;

    public EventClick(LoaderRecycleAsynk.Type type){
        itemType = type;
    }


    public EventClick withTrack(Track track){
        this.track = track;
        return this;
    }

    public EventClick withAlbum(Album album){
        this.album = album;
        return this;
    }

    public EventClick withArtst(Artist artist){
        this.artist = artist;
        return this;
    }

    public EventClick withPlayList(PlayList playList){
        this.playList = playList;
        return this;
    }

    public PlayList getPlayList() {
        return playList;
    }

    public Album getAlbum() {
        return album;
    }

    public Artist getArtist() {
        return artist;
    }

    public Track getTrack() {
        return track;
    }

    public LoaderRecycleAsynk.Type getItemType(){
        return itemType;
    }


}
