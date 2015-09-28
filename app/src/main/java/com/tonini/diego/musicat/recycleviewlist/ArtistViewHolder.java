package com.tonini.diego.musicat.recycleviewlist;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.tonini.diego.musicat.R;
import com.tonini.diego.musicat.Utils;
import com.tonini.diego.musicat.entity.Artist;
import com.tonini.diego.musicat.events.EventClick;

import java.util.Random;

import de.greenrobot.event.EventBus;

/**
 * ViewHolder used for AllSongFragment
 */
public class ArtistViewHolder extends AGenericViewHolder<Artist> {

    private TextView tvRound;
    private int[] mColorChoices;
    private int index = 1;
    private int colorSecondary;

    public ArtistViewHolder(View view, Context context) {
        super(view, context);
        mColorChoices=null;
        String[] color_array = mContext.getResources().getStringArray(R.array.default_color_choice_values);

        if (color_array!=null && color_array.length>0) {
            mColorChoices = new int[color_array.length];
            for (int i = 0; i < color_array.length; i++) {
                mColorChoices[i] = Color.parseColor(color_array[i]);
            }
        }

        tvRound = (TextView) view.findViewById(R.id.tvRound);
        colorSecondary = Utils.getSecondaryColor(mContext);
    }

    @Override
    protected void menuItemChosed(MenuItem menuItem,Artist artist) {

    }

    /*
             arr[(offset + i) % arr.length];
             */
    @Override
    public void bindItem(final Artist artist, boolean isHeader) {
        if (firstTitle != null)
            firstTitle.setText(isHeader ? artist.getName().substring(0,1) : artist.getName());

        if(!isHeader) {

            tvRound.setText(artist.getName().substring(0, 1));
            tvRound.setBackgroundResource(R.drawable.shape_round_textview);
            GradientDrawable drawable = (GradientDrawable) tvRound.getBackground();
            drawable.setColor(mColorChoices[new Random().nextInt(mColorChoices.length)]);

            index++;
            if (secondTitle != null)
                secondTitle.setText(String.valueOf(artist.getNumberAlbum())+" Albums");

            if(imageViewOver!=null)
                imageViewOver.setVisibility(View.GONE);

        } else {
            if(firstTitle!=null)
                firstTitle.setTextColor(colorSecondary);
        }

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new EventClick(LoaderRecycleAsynk.Type.ARTIST).withArtst(artist));
            }
        });
    }


}
