package com.tonini.diego.musicat.entity;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.tonini.diego.musicat.R;

/**
 * Created by multivoltage on 9/24/15.
 */
public class MyTargetImpl implements com.squareup.picasso.Target {

    ImageView mImageView;
    Uri mUri;

    public MyTargetImpl(ImageView imageView, Uri uri){
        this.mImageView = imageView;
        this.mUri = uri;
    }
    @Override
    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
        mImageView.setImageResource(R.mipmap.ic_action_play);
    }

    @Override
    public void onBitmapFailed(Drawable errorDrawable) {
        mImageView.setImageResource(R.mipmap.ic_launcher);
    }

    @Override
    public void onPrepareLoad(Drawable placeHolderDrawable) {

    }
}
