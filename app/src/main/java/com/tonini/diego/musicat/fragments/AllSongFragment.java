package com.tonini.diego.musicat.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;
import com.tonicartos.superslim.LayoutManager;
import com.tonini.diego.musicat.R;
import com.tonini.diego.musicat.entity.Track;
import com.tonini.diego.musicat.events.EventClick;
import com.tonini.diego.musicat.events.EventTabSelected;
import com.tonini.diego.musicat.recycleviewlist.FastScroller;
import com.tonini.diego.musicat.recycleviewlist.LoaderRecycleAsynk;

import java.util.List;


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
        List<Track> list = l.doInBackground();
        l.onPostExecute(list);
        Toast.makeText(getActivity(),"size:"+l.doInBackground().size(),Toast.LENGTH_SHORT).show();
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