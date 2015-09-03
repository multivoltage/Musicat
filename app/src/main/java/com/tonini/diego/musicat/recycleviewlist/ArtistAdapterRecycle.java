package com.tonini.diego.musicat.recycleviewlist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filterable;

import com.tonini.diego.musicat.R;
import com.tonini.diego.musicat.entity.Album;
import com.tonini.diego.musicat.entity.Artist;

import java.util.List;

/**
 * Created by Diego on 03/07/2015.
 */
public class ArtistAdapterRecycle extends AGenericAdapterRecycle implements Filterable {

    public ArtistAdapterRecycle(Context context, int headerMode, List<Artist> tracks) {
        super(context, headerMode, tracks);
    }

    @Override
    public AGenericViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        // same TYPE of Track- All song
        View view = null;
        switch (viewType){
            case VIEW_TYPE_HEADER :
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.header_item, parent, false);
                break;

            case VIEW_TYPE_CONTENT :
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.row_tracks_layout_no_img, parent, false);
                break;



        }
        return new ArtistViewHolder(view,mContext);
    }
}
