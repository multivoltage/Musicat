package com.tonini.diego.musicat.entity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.util.Log;

import java.io.IOException;

/**
 * Created by Diego on 16/06/2015.
 */
public class ID3TagLoader {

    private String file;
    private MediaMetadataRetriever retriver;

    public ID3TagLoader(String fileMp3){
        this.file = fileMp3;
    }


    public Bitmap getBitmap(){
        retriver = new MediaMetadataRetriever();
        retriver.setDataSource(file);

        try {
            byte[] art = retriver.getEmbeddedPicture();
            if(art!=null) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                retriver.release();
                retriver=null;
                return BitmapFactory.decodeByteArray(art, 0, art.length,options);
            }
        } catch (Exception e){
            Log.i(CoverLoaderAsynk.TAG_COVER,"getBitmap(): "+e.toString());
        }
        retriver.release();
        retriver=null;
        return null;
    }
}
