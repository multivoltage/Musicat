package com.tonini.diego.musicat.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;
import com.tonicartos.superslim.LayoutManager;
import com.tonini.diego.musicat.MainActivity;
import com.tonini.diego.musicat.R;
import com.tonini.diego.musicat.entity.Artist;
import com.tonini.diego.musicat.events.EventTabSelected;
import com.tonini.diego.musicat.recycleviewlist.FastScroller;
import com.tonini.diego.musicat.recycleviewlist.LoaderRecycleAsynk;


public class ArtistFragment extends AbstracTrackFragment {

    LoaderRecycleAsynk<Artist> l;
    boolean execute = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tracks_rec, container, false);

        originType = LoaderRecycleAsynk.Type.ARTIST;
        r = (ObservableRecyclerView) v.findViewById(R.id.recycler_view);
        r.setLayoutManager(new LayoutManager(getActivity()));
        fastScroller = (FastScroller) v.findViewById(R.id.fast_scroller);

        l = new LoaderRecycleAsynk<Artist>(getActivity(), LoaderRecycleAsynk.Type.ARTIST,fastScroller,r);
        return v;
    }

    @Override
    public void callBackOnVisible(){
        if(!execute){
            execute = true;
            l.execute();
        }
    }

    @Override
    public void setUpFabClickListener() {
        getActivity().findViewById(R.id.fabBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LoaderRecycleAsynk<Artist>(getActivity(), LoaderRecycleAsynk.Type.ARTIST, fastScroller, r).execute();
                showBack = false;
                getActivity().findViewById(R.id.fabBack).setVisibility(View.GONE);
            }
        });

    }

    @Override
    public void onEvent(EventTabSelected event) {
        if(event.getItem()==2){
            Log.d(MainActivity.TAG, "EventBackPress catch by "+getClass().getSimpleName()+", item: "+event.getItem());
            updateBackButton();
        }
    }

}
