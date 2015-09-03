package com.tonini.diego.musicat.entity;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class Track extends Item implements Parcelable{

    private long id;
    private long albumId;
    private String album = "Music";
    private String genres = "n/a";
    private Uri artUri;
    private Uri trackUri; // the .mp3..

    // for listview
    public int sectionManager;
    public int sectionFirstPosition;
    public boolean isHeader;

    public Track(){}

    // Parcelling part
    public Track(Parcel in){
        // long, 6x String
        id = in.readLong();
        setTitle(in.readString());
        setArtist(in.readString());
        album = in.readString();
        genres = in.readString();
        artUri = Uri.parse(in.readString());
        trackUri = Uri.parse(in.readString());
        albumId = in.readLong();
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public void setArtist(String artist) {
        super.setSecondTitle(artist);
    }

    public void setGenres(String genres) {
        this.genres = genres;
    }

    public void setTitle(String title) {
        super.setFirstTitle(title);
    }

    public void setAlbumId(long albumId) {
        this.albumId = albumId;
    }

    // GETTER


    public long getAlbumId() {
        return albumId;
    }

    public String getAlbum() {
        return album;
    }

    public String getArtist() {
        return super.getSecondTitle();
    }

    public String getTitle() {
        return super.getFirstTitle();
    }

    public String getGenres() {
        return genres;
    }

    public void setArtUri(Uri artUri) {
        this.artUri = artUri;
    }

    public Uri getArtUri() {
        return artUri;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setTrackUri(Uri trackUri) {
        this.trackUri = trackUri;
    }

    public Uri getTrackUri() {
        return trackUri;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.getTitle());
        dest.writeString(this.getArtist());
        dest.writeString(this.album);
        dest.writeString(this.genres);
        dest.writeString(this.artUri.toString());
        dest.writeString(this.trackUri.toString());
        dest.writeLong(this.albumId);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Track createFromParcel(Parcel in) {
            return new Track(in);
        }

        @Override
        public Track[] newArray(int size) {
            return new Track[size];
        }

    };
}
