package com.multivoltage.musicat.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;
import com.tonicartos.superslim.LayoutManager;
import com.multivoltage.musicat.R;
import com.multivoltage.musicat.entity.Track;
import com.multivoltage.musicat.events.EventClick;
import com.multivoltage.musicat.events.EventTabSelected;
import com.multivoltage.musicat.recycleviewlist.FastScroller;
import com.multivoltage.musicat.recycleviewlist.LoaderRecycleAsynk;


public class AllSongFragment extends AbstracTrackFragment {

    LoaderRecycleAsynk<Track> l;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tracks_rec, container, false);

        originType = LoaderRecycleAsynk.Type.TRACK;

        r = (ObservableRecyclerView) v.findViewById(R.id.recycler_view);
        r.setLayoutManager(new LayoutManager(getActivity()));
        fastScroller = (FastScroller) v.findViewById(R.id.fast_scroller);

        l = new  LoaderRecycleAsynk<Track>(getActivity(), LoaderRecycleAsynk.Type.TRACK, fastScroller, r);
        l.execute();
        return v;
    }




    public void onEvent(EventClick event) {
        // do nothing
    }

    @Override
    public void onEvent(EventTabSelected event) {
    }

    @Override
    public void setUpFabClickListener() {
    }
}
