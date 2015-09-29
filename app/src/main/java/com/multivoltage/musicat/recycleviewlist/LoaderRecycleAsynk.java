package com.multivoltage.musicat.recycleviewlist;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.multivoltage.musicat.entity.Album;
import com.multivoltage.musicat.entity.Artist;
import com.multivoltage.musicat.entity.Pair;
import com.multivoltage.musicat.entity.PlayList;
import com.multivoltage.musicat.entity.Track;
import com.multivoltage.musicat.entity.TrackFinder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Diego on 03/08/2015.
 */
public class LoaderRecycleAsynk<I> extends AsyncTask<Void,Void,List<I>> {

    public static enum Type {
        TRACK,TRACK_P,ALBUM,ARTIST,PLAYLIST
    }
    Type type;

    protected ProgressDialog dialog;
    private Context mContext;
    private RecyclerView mRecyclerView;
    private FastScroller mFastScroller;
    protected AGenericAdapterRecycle mAdapter = null;
    protected final int mHeaderDisplay = 56;
    protected final boolean mAreMarginsFixed = true;
    private String mFilterForArtist     = null;
    private String mFilterForAlbum      = null;
    private long mFilterIdForPlayList = -1L;
    static String TAG = "Loader.TAG";



    public LoaderRecycleAsynk(final Context context,Type type,FastScroller fastScroller,RecyclerView recyclerView){
        mContext = context;
        dialog = new ProgressDialog(mContext);
        this.type = type;
        mFastScroller = fastScroller;
        mRecyclerView = recyclerView;
    }

    public void setFilterForArtist(String artistName){
        mFilterForArtist = artistName;
    }
    public void setFilterForAlbum(String albumName){
        mFilterForAlbum = albumName;
    }
    public void setFilterForPlayList(long idFilter){
        mFilterIdForPlayList = idFilter;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog.setMessage("Load tracks...");
        //dialog.show();
    }

    @Override
    protected List<I> doInBackground(Void... voids) {
        switch (type){
            case TRACK:      return getTrack();
            case TRACK_P:    return getTrackOfPlayList(mFilterIdForPlayList);
            case ALBUM:      return getAlbum();
            case ARTIST:     return getArtist();
            case PLAYLIST:   return getPlayList();
        }

        // never return null. Maybe forgot to define correct Type in constructor
        return null;
    }


    @Override
    public void onPostExecute(List<I> result) {
        //super.onPostExecute(result);

        switch (type){
            case TRACK:
                mAdapter = new TrackAdapterRecycle(mContext, mHeaderDisplay, (List<Track>) result);
                break;
            case ALBUM:
                mAdapter = new AlbumAdapterRecycle(mContext, mHeaderDisplay, (List<Album>) result);
                break;
            case ARTIST:
                mAdapter = new ArtistAdapterRecycle(mContext, mHeaderDisplay, (List<Artist>) result);
                break;
            case PLAYLIST:
                mAdapter = new PlayListAdapterRecycle(mContext, mHeaderDisplay, (List<PlayList>) result);
                break;
            case TRACK_P:
                mAdapter = new TrackAdapterRecycle(mContext,mHeaderDisplay,(List<Track>) result);
                break;

        }

        if(mAdapter!=null){
            mAdapter.setMarginsFixed(mAreMarginsFixed);
            mAdapter.setHeaderDisplay(mHeaderDisplay);
            mRecyclerView.setAdapter(mAdapter);
            mFastScroller.setRecyclerView(mRecyclerView);
        }
        //dialog.dismiss();

    }

    private List<I> getAlbum(){
        List<Track> tracks = new TrackFinder(mContext).getTracks(null);
        List<Album> albums = new ArrayList<>();
        // album     autor  path
        Map<String,Pair<String,Uri>> map = new HashMap<>();
        for(Track t : tracks){
            map.put(t.getAlbum(),new Pair<String, Uri>(t.getArtist(),t.getArtUri()));
        }
        for(String albumTitle : map.keySet()){
            Album album = new Album();
            album.setName(albumTitle);
            album.setAuthor(map.get(albumTitle).getX());
            album.setAlmbumUri(map.get(albumTitle).getY());
            albums.add(album);
        }

        Collections.sort(albums, new Comparator<Album>() {
            @Override
            public int compare(Album lhs, Album rhs) {
                return lhs.getName().compareTo(rhs.getName());
            }
        });
        return (List<I>) albums;
    }
    private List<I> getTrack(){
        List<Track> tracks = new TrackFinder(mContext).getTracks(MediaStore.Audio.Media.TITLE);

        if(mFilterForArtist==null && mFilterForAlbum==null && mFilterIdForPlayList==-1)
            return (List<I>) tracks;
        else {
            List<Track> trackFiltered = new ArrayList<>();
            for(Track track : tracks){
                if(mFilterForAlbum!=null) {
                    Log.i(LoaderRecycleAsynk.TAG, "mFilterForAlbum: " + mFilterForAlbum);
                    if(mFilterForAlbum.equals(track.getAlbum())) {
                        trackFiltered.add(track);
                    }
                } else if(mFilterForArtist!=null) {
                    Log.i(LoaderRecycleAsynk.TAG,"mFilterForArtist: "+mFilterForArtist);
                    if(track.getArtist().equals(mFilterForArtist)) {
                        trackFiltered.add(track);
                    }
                } else if(mFilterIdForPlayList!=-1) {

                }
            }
            return (List<I>) trackFiltered;
        }
    }

    private List<I> getArtist(){
        List<Track> tracks = new TrackFinder(mContext).getTracks(null);
        // artista    // album names
        Map<String,Set<String>> map = new HashMap<>();

        for(Track track : tracks){
            String artistName = track.getArtist();
            String albumName  = track.getAlbum();

            // if artist is not stored
            if(!map.containsKey(artistName)){
                Set<String> setAlbum = new HashSet<>();
                setAlbum.add(albumName);
                map.put(artistName, setAlbum);

            } else {
                // already stored this artist
                Set<String> albumNamesSet = map.get(artistName);
                if(!albumNamesSet.contains(albumName)) {
                    albumNamesSet.add(albumName);
                    map.put(artistName, albumNamesSet);

                }
            }
        }

        List<Artist> artists = new ArrayList<>();
        for(String artistName : map.keySet()){
            Artist artist = new Artist();
            artist.setName(artistName.equals("<unknown>") ? "Music" : artistName);
            artist.setNumberAlbum(map.get(artistName).size());

            artists.add(artist);
        }

        Collections.sort(artists, new Comparator<Artist>() {
            @Override
            public int compare(Artist lhs, Artist rhs) {
                return lhs.getName().compareTo(rhs.getName());
            }
        });


        return (List<I>) artists;
    }
    private List<I> getPlayList(){
        List<PlayList> playLists = new TrackFinder(mContext).getPlayList();
        Collections.sort(playLists, new Comparator<PlayList>() {
            @Override
            public int compare(PlayList lhs, PlayList rhs) {
                return lhs.getTitle().compareTo(rhs.getTitle());
            }
        });

        return (List<I>) playLists;
    }

    private List<I> getTrackOfPlayList(long id){
        List<Track> list = new TrackFinder(mContext).getPlaylistsById(id);
        Collections.sort(list, new Comparator<Track>() {
            @Override
            public int compare(Track lhs, Track rhs) {
                return lhs.getTitle().compareTo(rhs.getTitle());
            }
        });
        return (List<I>) list;
    }


}
