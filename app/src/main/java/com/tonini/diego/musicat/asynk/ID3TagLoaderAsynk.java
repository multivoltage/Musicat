package com.tonini.diego.musicat.asynk;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.widget.ImageView;

import com.tonini.diego.musicat.R;

/**
 * Created by Diego on 16/06/2015.
 */
public class ID3TagLoaderAsynk extends AsyncTask<Void,Void,Bitmap>{

    ContentResolver contentResolver;
    private ImageView imageView;
    int id;

    public ID3TagLoaderAsynk(ContentResolver c, int id,ImageView imageView){
        this.contentResolver = c;
        this.imageView = imageView;
        this.id = id;
    }

    @Override
    protected Bitmap doInBackground(Void... params) {
        final BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inSampleSize = 2;
        return MediaStore.Images.Thumbnails.getThumbnail(contentResolver, id, MediaStore.Images.Thumbnails.MINI_KIND, bmOptions);
    }

    @Override
    protected void onPostExecute(Bitmap bitmap){
        if(bitmap!=null){
            imageView.setImageBitmap(bitmap);
        } else {
            imageView.setImageResource(R.mipmap.unknow_cover);
        }
    }
}
