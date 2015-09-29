package com.multivoltage.musicat.entity;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

/**
 * Created by Diego on 07/08/2015.
 */
public class PlayListManager {

    public static int saveTrackOnPlayList(Context context,Track track, long idPlayList){
        ContentResolver resolver = context.getContentResolver();
        String[] cols = new String[] {
                "count(*)"
        };
        Uri uri = MediaStore.Audio.Playlists.Members.getContentUri("external", idPlayList);
        Cursor cur = resolver.query(uri, cols, null, null, null);
        cur.moveToFirst();
        final int base = cur.getInt(0);
        cur.close();
        ContentValues values = new ContentValues();
        values.put(MediaStore.Audio.Playlists.Members.PLAY_ORDER, track.getId());
        values.put(MediaStore.Audio.Playlists.Members.AUDIO_ID, track.getId());
        resolver.insert(uri, values);
        return base;
    }

    /**
     * Delete track given a playListId
     * @param context
     * @param track
     * @param idPlayList
     * @return number of row deleted
     */
    public static int removeTrackOnPlayList(Context context,Track track, long idPlayList){

        ContentResolver resolver = context.getContentResolver();

        Uri uri = MediaStore.Audio.Playlists.Members.getContentUri("external", idPlayList);
        Cursor cur = resolver.query(uri, null, null, null, null);
        cur.moveToFirst();
        final int base = cur.getInt(0);
        cur.close();

        int res = resolver.delete(uri, MediaStore.Audio.Playlists.Members.AUDIO_ID +" = "+track.getId(), null);
        return res;
    }

    public static long createPlayList(Context context,String name,long dateAddedMillis){
        long mPlaylistId = -1;
        ContentResolver mCr = context.getContentResolver();
        String[] PROJECTION_PLAYLIST = new String[] {
                MediaStore.Audio.Playlists._ID,
                MediaStore.Audio.Playlists.NAME,
                MediaStore.Audio.Playlists.DATA
        };

        ContentValues mInserts = new ContentValues();
        mInserts.put(MediaStore.Audio.Playlists.NAME, name);
        mInserts.put(MediaStore.Audio.Playlists.DATE_ADDED, System.currentTimeMillis());
        mInserts.put(MediaStore.Audio.Playlists.DATE_MODIFIED, System.currentTimeMillis());
        Uri mUri = mCr.insert(MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI, mInserts);
        if (mUri != null) {
            mPlaylistId = -1;
            Cursor c = mCr.query(mUri, PROJECTION_PLAYLIST, null, null, null);
            if (c != null && c.getCount()>0) {
                c.moveToFirst();
                mPlaylistId = c.getInt(c.getColumnIndex(MediaStore.Audio.Playlists._ID));
                c.close();
            }

        }
        return mPlaylistId;
    }

    /**
     * Delete a playlist with specific id
     * @param context
     * @param playListId id of playlist to delete
     * @return number of row delete
     */
    public static int deletePlaylist(Context context,long playListId){
        int rowDeleted = 0;
        ContentResolver resolver = context.getContentResolver();
        String where = MediaStore.Audio.Playlists._ID + "=?";
        String[] whereVal = {String.valueOf(playListId)};
        rowDeleted = resolver.delete(MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI, where, whereVal);
        return rowDeleted;
    }

}
