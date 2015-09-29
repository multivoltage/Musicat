package com.multivoltage.musicat.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;
import com.tonicartos.superslim.LayoutManager;
import com.multivoltage.musicat.MainActivity;
import com.multivoltage.musicat.R;
import com.multivoltage.musicat.entity.Album;
import com.multivoltage.musicat.events.EventTabSelected;
import com.multivoltage.musicat.recycleviewlist.FastScroller;
import com.multivoltage.musicat.recycleviewlist.LoaderRecycleAsynk;

import static com.multivoltage.musicat.recycleviewlist.LoaderRecycleAsynk.Type.ALBUM;

public class AlbumsFragment extends AbstracTrackFragment {


    LoaderRecycleAsynk<Album> l;
    boolean execute = false;

    @Override
    public void setUpFabClickListener() {
        getActivity().findViewById(R.id.fabBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LoaderRecycleAsynk<Album>(getActivity(), LoaderRecycleAsynk.Type.ALBUM, fastScroller, r).execute();
                showBack = false;
                getActivity().findViewById(R.id.fabBack).setVisibility(View.GONE);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tracks_rec, container, false);

        originType = ALBUM;
        r = (ObservableRecyclerView) v.findViewById(R.id.recycler_view);
        r.setLayoutManager(new LayoutManager(getActivity()));
        fastScroller = (FastScroller) v.findViewById(R.id.fast_scroller);

        l = new LoaderRecycleAsynk<Album>(getActivity(), LoaderRecycleAsynk.Type.ALBUM,fastScroller,r);
        l.execute();
        return v;
    }


    @Override
    public void onEvent(EventTabSelected event) {
        if(event.getItem()==1){
            Log.d(MainActivity.TAG, "EventBackPress catch by "+getClass().getSimpleName());
            updateBackButton();
        }
    }
}
