package com.tonini.diego.musicat.entity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * Created by Diego on 17/06/2015.
 */
public class BitmapDownloader {

    private String urlImage;

    public BitmapDownloader(String urlImage){
        this.urlImage = urlImage;
    }

    public byte[] getBitmap(){
        final DefaultHttpClient client = new DefaultHttpClient();
        //forming a HttoGet request
        final HttpGet getRequest = new HttpGet(urlImage);
        try {
            HttpResponse response = client.execute(getRequest);
            //check 200 OK for success
            final int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                Log.i(CoverLoaderAsynk.TAG_COVER, "Error " + statusCode +  " while retrieving bitmap from " + urlImage);
                return null;
            }
            final HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream inputStream = null;
                try {
                    // getting contents from the stream
                    inputStream = entity.getContent();
                    // decoding stream data back into image Bitmap that android understands
                    final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    return stream.toByteArray();
                    //return bitmap;
                } finally {
                    if (inputStream != null) {
                        inputStream.close();
                    }
                    entity.consumeContent();
                }
            }
        } catch (Exception e) {
            // You Could provide a more explicit error message for IOException
            getRequest.abort();
            Log.i(CoverLoaderAsynk.TAG_COVER, "Something went wrong while" +
                    " retrieving bitmap from " + urlImage + e.toString());
        }

        return null;
    }
}
