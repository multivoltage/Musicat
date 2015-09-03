package com.tonini.diego.musicat.entity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.Mp3File;
import com.tonini.diego.musicat.Utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by Diego on 01/09/2015.
 */
public abstract class LoadImageFileAsynk extends AsyncTask<Void,Void,File> {

    File file;
    Context mContext;
    int  dimPixel;
    int quality;

    public LoadImageFileAsynk(File audioFile,Context context,int quality){
        this.file = audioFile;
        this.mContext = context;
        dimPixel = (int) Utils.convertDpToPixel(64, context);
        this.quality = quality;
    }
    @Override
    public void onPreExecute(){
    }
    @Override
    protected File doInBackground(Void... params) {
        try {
            Mp3File mp3File = new Mp3File(file);
            if(mp3File.hasId3v2Tag()){
                ID3v2 tag = mp3File.getId3v2Tag();
                // Log.i(EditActivity.TAG, "albumimage is " + tag.getAlbumImage() == null ? "null" : ("notnull" + " withmimetype: " + tag.getAlbumImageMimeType()));
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;

                Bitmap yourSelectedImage = BitmapFactory.decodeByteArray(tag.getAlbumImage(), 0, tag.getAlbumImage().length,options);

                //create a file to write bitmap data
                File f = new File(mContext.getCacheDir(), file.getName());
                f.createNewFile();

                //Convert bitmap to byte array
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                yourSelectedImage.compress(Bitmap.CompressFormat.JPEG, quality /*ignored for PNG*/, bos);
                byte[] bitmapdata = bos.toByteArray();

                //write the bytes in file
                FileOutputStream fos = new FileOutputStream(f);
                fos.write(bitmapdata);
                fos.flush();
                fos.close();

                return f;
            }
        } catch (Exception e) {
            // Log.i(EditActivity.TAG,e.toString());
        }
        return null;
    }

    @Override
    protected abstract void onPostExecute(File bitmap);
}
