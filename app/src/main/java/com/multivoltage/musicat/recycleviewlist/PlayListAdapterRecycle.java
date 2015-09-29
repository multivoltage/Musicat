package com.multivoltage.musicat.recycleviewlist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filterable;

import com.multivoltage.musicat.R;
import com.multivoltage.musicat.entity.PlayList;

import java.util.List;

/**
 * Created by Diego on 03/07/2015.
 */
public class PlayListAdapterRecycle extends AGenericAdapterRecycle implements Filterable {

    public PlayListAdapterRecycle(Context context, int headerMode, List<PlayList> tracks) {
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
        return new PlayListViewHolder(view,mContext);
    }
}
