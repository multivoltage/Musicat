package com.tonini.diego.musicat.entity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;

/**
 * DO NOT USE DIRECT METHOD IN MAIN THREAD
 */
public class BitmapLoader {

    private String file;

    public BitmapLoader(String file){
        this.file = file;
    }

    public Bitmap getBitmap(){
        File f = new File(file);
        Bitmap bitmap = null;
        if (f.exists()) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            bitmap =  BitmapFactory.decodeFile(f.getAbsolutePath(), options);

        }
        return bitmap;
    }
}
