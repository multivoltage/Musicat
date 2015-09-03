package com.tonini.diego.musicat.entity;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.List;


// use this class only for fetch info, and not for write bitmap
public class TrackFinder {

    Context mContext;

    public TrackFinder(Context context){
        mContext = context;
    }

    public List<Track> getTracks(String orderBy) {

        List<Track> tracks = new ArrayList<>();
        ContentResolver contentResolver = mContext.getContentResolver();
        Uri musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";
        Cursor musicCursor = contentResolver.query(musicUri, null,selection,null, orderBy);

        if(musicCursor!=null && musicCursor.moveToFirst()){
            //get columns
            int titleColumn     = musicCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int idColumn        = musicCursor.getColumnIndex(MediaStore.Audio.Media._ID);
            int artistColumn    = musicCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int albumColumn     = musicCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM);
            int albumIdColum    = musicCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID);
            int trackuri        = musicCursor.getColumnIndex(MediaStore.Audio.Media.DATA);
            Uri sArtworkUri     = Uri.parse("content://media/external/audio/albumart");



            do {
                long thisId = musicCursor.getLong(idColumn);
                long thidAlbumId = musicCursor.getLong(albumIdColum);
                String thisTitle = musicCursor.getString(titleColumn);
                String thisArtist = musicCursor.getString(artistColumn);
                String album = musicCursor.getString(albumColumn);
                Uri trackUri = Uri.parse(musicCursor.getString(trackuri));
                Uri albumArtUri = ContentUris.withAppendedId(sArtworkUri, musicCursor.getLong(albumIdColum));

                Track t = new Track();
                t.setId(thisId);
                t.setAlbumId(thidAlbumId);
                t.setArtist(thisArtist);
                t.setTitle(thisTitle);
                t.setAlbum(album);
                t.setArtUri(albumArtUri);
                t.setTrackUri(trackUri);


                /*try {
                    Mp3File mp3File = new Mp3File(trackUri.toString());

                    if(mp3File.hasId3v1Tag()){
                        ID3v1 tag = mp3File.getId3v1Tag();
                        Log.i(MainActivity.TAG,"title: "+tag.getTitle());
                        Log.i(MainActivity.TAG, "album" + tag.getAlbum());
                        Log.i(MainActivity.TAG,"artist"+tag.getArtist());
                    } else if(mp3File.hasId3v2Tag()) {
                        ID3v2 tag = mp3File.getId3v2Tag();
                        Log.i(MainActivity.TAG, "title: " + tag.getTitle());
                        Log.i(MainActivity.TAG, "album" + tag.getAlbum());
                        Log.i(MainActivity.TAG, "artist" + tag.getArtist());

                    }
                } catch (Exception e) {
                    Log.i(MainActivity.TAG,e.toString());
                }*/

                tracks.add(t);
            }
            while (musicCursor.moveToNext());

            musicCursor.close();

        }
        return tracks;
    }


    public List<PlayList> getPlayList(){
        List<PlayList> playLists = new ArrayList<>();

        ContentResolver contentResolver = mContext.getContentResolver();
        Uri playlistUri = MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI;
        Cursor playListCursor = contentResolver.query(playlistUri, null, null, null, null);

        if(playListCursor!=null && playListCursor.moveToFirst()){
            //get columns ,
            int nameColum = playListCursor.getColumnIndex(MediaStore.Audio.Playlists.NAME);
            int idColum  = playListCursor.getColumnIndex(MediaStore.Audio.Playlists._ID);

            do {
                String thisName = playListCursor.getString(nameColum);
                int    thisId   = playListCursor.getInt(idColum);

                PlayList p = new PlayList();
                p.setTitle(thisName);
                p.setId(thisId);

                playLists.add(p);
            }
            while (playListCursor.moveToNext());

            playListCursor.close();
        }
        return playLists;
    }

    public List<Track> getPlaylistsById(long playlistId){
        List<Track> list = new ArrayList<>();

        String[] projection = {
                MediaStore.Audio.Playlists.Members.TITLE,
                MediaStore.Audio.Playlists.Members._ID,
                MediaStore.Audio.Playlists.Members.ARTIST,
                MediaStore.Audio.Playlists.Members.ALBUM,
                MediaStore.Audio.Playlists.Members.ALBUM_ID,
                MediaStore.Audio.Playlists.Members.DATA

        };

        Cursor tracksPLayListCursor = mContext.getContentResolver().query(
                MediaStore.Audio.Playlists.Members.getContentUri("external", playlistId),
                projection,
                MediaStore.Audio.Media.IS_MUSIC + " != 0 ",
                null,
                null);


        if(tracksPLayListCursor!=null && tracksPLayListCursor.moveToFirst()){

            int titleColum      = tracksPLayListCursor.getColumnIndex(MediaStore.Audio.Playlists.Members.TITLE);
            int idColumn        = tracksPLayListCursor.getColumnIndex(MediaStore.Audio.Playlists.Members._ID);
            int artistColumn    = tracksPLayListCursor.getColumnIndex(MediaStore.Audio.Playlists.Members.ARTIST);
            int albumColumn     = tracksPLayListCursor.getColumnIndex(MediaStore.Audio.Playlists.Members.ALBUM);
            int albumIdColum    = tracksPLayListCursor.getColumnIndex(MediaStore.Audio.Playlists.Members.ALBUM_ID);
            int trackuri        = tracksPLayListCursor.getColumnIndex(MediaStore.Audio.Playlists.Members.DATA);
            Uri sArtworkUri     = Uri.parse("content://media/external/audio/albumart");

           // Log.i(MainActivity.TAG,"row: "+tracksPLayListCursor.getCount()+" colum count: "+tracksPLayListCursor.getColumnCount());
            do {
                long thisId = tracksPLayListCursor.getLong(idColumn);
                String thisTitle = tracksPLayListCursor.getString(titleColum);
                String thisArtist = tracksPLayListCursor.getString(artistColumn);
                String album = tracksPLayListCursor.getString(albumColumn);
                Uri trackUri = Uri.parse(tracksPLayListCursor.getString(trackuri));
                Uri albumArtUri = ContentUris.withAppendedId(sArtworkUri, tracksPLayListCursor.getLong(albumIdColum));

                Track t = new Track();
                t.setId(thisId);
                t.setArtist(thisArtist);
                t.setTitle(thisTitle);
                t.setAlbum(album);
                t.setArtUri(albumArtUri);
                t.setTrackUri(trackUri);

                list.add(t);
            }
            while (tracksPLayListCursor.moveToNext());
            tracksPLayListCursor.close();

        }
        return list;
    }
}
