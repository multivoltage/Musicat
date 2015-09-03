package com.tonini.diego.musicat;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;

/**
 * Created by Diego on 24/05/2015.
 */
public class BitmapGenerator {

    public static Bitmap generateBitmapFromFile(File f) {

        if (f.exists()) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            return BitmapFactory.decodeFile(f.getAbsolutePath(), options);
        }

        return null;
    }
}
