package com.tonini.diego.musicat.entity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.tonini.diego.musicat.MainActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Diego on 17/06/2015.
 */
public class BitmapSaver {

    private File file;
    private Bitmap bitmap;

    public BitmapSaver(Bitmap bitmap,String fileJpg){
        this.bitmap = bitmap;
        this.file = new File(fileJpg);
    }

    public void saveBitmap() {

        if(!file.exists()) {
            try {
                boolean created = file.createNewFile();

                int dotIndex = file.getName().lastIndexOf(".");
                if (dotIndex > 0) {

                    String extension = file.getName().substring(dotIndex + 1); // return jpg png (no dot)
                    if (extension.equals("mus")){

                        FileOutputStream out = null;
                        try {
                            BitmapFactory.Options options = new BitmapFactory.Options();
                            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                            out = new FileOutputStream(file);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
                            out.flush();

                        } catch (Exception e) {
                            Log.i(CoverLoaderAsynk.TAG_COVER, "saveBitmap() internal catch" + e.toString());
                        } finally {
                            try {
                                if (out != null) {
                                    out.close();
                                }
                            } catch (IOException e) {
                                Log.i(CoverLoaderAsynk.TAG_COVER, "saveBitmap() finally " + e.toString());
                            }
                        }
                    }
                }
            } catch (IOException e) {
                Log.i(CoverLoaderAsynk.TAG_COVER, "saveBitmap() catch finale " + e.toString());
            }

        }
    }
}
