package com.tonini.diego.musicat.entity;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.mpatric.mp3agic.Mp3File;
import com.tonini.diego.musicat.R;
import com.tonini.diego.musicat.Utils;

import java.io.File;
import java.lang.ref.WeakReference;

/**
 * Created by multivoltage on 9/24/15.
 */
public abstract class LoadImageByteArrayAsynk extends AsyncTask<Void, Void, byte[]> {

    Uri uri;
    Context mContext;
    int  dimPixel;

    public LoadImageByteArrayAsynk(Uri uri,Context context,ImageView imageView){
        this.uri = uri;
        this.mContext = context;
        dimPixel = (int) Utils.convertDpToPixel(64, context);
    }
    @Override
    public void onPreExecute(){
    }
    @Override
    protected byte[] doInBackground(Void... params) {
        try {
            Mp3File mp3File = new Mp3File(new File(uri.toString()));
            if(mp3File.hasId3v2Tag()){
                return mp3File.getId3v2Tag().getAlbumImage();
            }
        } catch (Exception e) {
            // Log.i(EditActivity.TAG,e.toString());
        }
        return null;
    }

    @Override
    protected abstract void onPostExecute(byte[] bitmap);
}
