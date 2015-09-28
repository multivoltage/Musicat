package com.tonini.diego.musicat.recycleviewlist;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.TouchDelegate;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Request;
import com.squareup.picasso.RequestHandler;
import com.tonini.diego.musicat.R;
import com.tonini.diego.musicat.Utils;
import com.tonini.diego.musicat.custom.RoundedTransformation;
import com.tonini.diego.musicat.entity.Image;
import com.tonini.diego.musicat.entity.LoadImageByteArrayAsynk;
import com.tonini.diego.musicat.entity.LoadImageFileAsynk;
import com.tonini.diego.musicat.entity.MyTargetImpl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * ViewHolder used for AllSongFragment
 */
public abstract class AGenericViewHolder<I> extends RecyclerView.ViewHolder {

    protected ImageView imageView;
    protected TextView firstTitle;
    protected TextView secondTitle;
    protected ImageButton imageViewOver;
    protected LinearLayout linearLeft;
    protected int dimPixel;
    protected Context mContext;
    protected int theme;

    public AGenericViewHolder(View view, Context context) {
        super(view);
        mContext = context;
        firstTitle = (TextView) view.findViewById(R.id.tvRowTitle);
        secondTitle = (TextView) view.findViewById(R.id.tvRowSubtitle);         // MAKE SURE THAH INSIDE BOTH HEADER AND FULL LAYOUT ROW THIS VIEW HAS THE SAME ID
        imageView = (ImageView) view.findViewById(R.id.imageViewRowCover);
        imageViewOver = (ImageButton) view.findViewById(R.id.imageViewOver);
        linearLeft = (LinearLayout) view.findViewById(R.id.linearLeft);
        dimPixel = (int) Utils.convertDpToPixel(64, context);
        theme = Utils.getTheme(mContext);
    }


    public abstract void bindItem(I i, boolean isHeader);

    public void loadPicasso(final ImageView imageView, final Uri uri) {

        Picasso.with(mContext)
                .load(uri)
                .transform(new RoundedTransformation(90,10))
                .placeholder(R.mipmap.unknow_cover)
                .into(imageView);
    }

    protected void setUpPopUpMenu(final int menuRes,final I i){
        if(linearLeft!=null){
            linearLeft.post(new Runnable() {
                @Override
                public void run() {
                    Rect delegateArea = new Rect();
                    imageViewOver.setEnabled(true);
                    imageViewOver.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            PopupMenu popupMenu = new PopupMenu(mContext, imageViewOver);
                            popupMenu.inflate(menuRes);
                            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(MenuItem item) {
                                    menuItemChosed(item,i);
                                    return false;
                                }
                            });
                            popupMenu.show();
                        }
                    });

                    // The hit rectangle for the ImageButton
                    imageViewOver.getHitRect(delegateArea);

                    // Extend the touch area of the ImageButton beyond its bounds
                    // on the right and bottom.
                    delegateArea.right += 40;
                    delegateArea.bottom += 40;
                    delegateArea.left += 40;
                    delegateArea.top += 40;


                    // Instantiate a TouchDelegate.
                    // "delegateArea" is the bounds in local coordinates of
                    // the containing view to be mapped to the delegate view.
                    // "myButton" is the child view that should receive motion
                    // events.
                    TouchDelegate touchDelegate = new TouchDelegate(delegateArea,imageViewOver);

                    // Sets the TouchDelegate on the parent view, such that touches
                    // within the touch delegate bounds are routed to the child.
                    if (View.class.isInstance(imageViewOver.getParent())) {
                        ((View) imageViewOver.getParent()).setTouchDelegate(touchDelegate);
                    }
                }
            });
        }
    }

    protected abstract void menuItemChosed(MenuItem menuItem,I i);

}
