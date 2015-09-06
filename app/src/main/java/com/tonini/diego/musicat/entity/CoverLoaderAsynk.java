package com.tonini.diego.musicat.entity;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.tonini.diego.musicat.MainActivity;
import com.tonini.diego.musicat.Utils;

import java.io.File;

/**
 * Created by Diego on 16/06/2015.
 */
public class CoverLoaderAsynk extends AsyncTask<Void,Void,Void> {

    public static final String TAG_COVER = "com.cover.loader";
    private String file;

    public CoverLoaderAsynk(String fileMp3){
        this.file = fileMp3;
        Log.i(MainActivity.TAG,"create CoverLoaderAsynk with "+fileMp3);
    }

    @Override
    protected Void doInBackground(Void... params) {

        Bitmap bitmap = new ID3TagLoader(file).getBitmap();

        if(bitmap!=null){

            String coverFilePath = Environment.getExternalStorageDirectory().getPath()+File.separator+"Musicat"+File.separator+"Covers"+File.separator+Utils.getNameWithoutExt(new File(file).getName())+".mus";
            new BitmapSaver(bitmap,coverFilePath).saveBitmap();
        }

        return null;
    }


    public void doInMainThread(){

        Bitmap bitmap = new ID3TagLoader(file).getBitmap();

        if(bitmap!=null){

            String coverFilePath = Environment.getExternalStorageDirectory().getPath()+File.separator+"Musicat"+File.separator+"Covers"+File.separator+Utils.getNameWithoutExt(new File(file).getName())+".mus";
            new BitmapSaver(bitmap,coverFilePath).saveBitmap();
        }

    }




}
