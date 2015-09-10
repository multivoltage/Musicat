package com.tonini.diego.musicat.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;
import com.melnykov.fab.FloatingActionButton;
import com.tonini.diego.musicat.MainActivity;
import com.tonini.diego.musicat.R;
import com.tonini.diego.musicat.entity.Track;
import com.tonini.diego.musicat.events.EventClick;
import com.tonini.diego.musicat.events.EventSearch;
import com.tonini.diego.musicat.events.EventTabSelected;
import com.tonini.diego.musicat.recycleviewlist.AGenericAdapterRecycle;
import com.tonini.diego.musicat.recycleviewlist.FastScroller;
import com.tonini.diego.musicat.recycleviewlist.LoaderRecycleAsynk;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

public abstract class AbstracTrackFragment extends Fragment {

   // protected AGenericAdapterRecycle mAdapter = null;
    protected ObservableRecyclerView r;
    protected FastScroller fastScroller;
    protected final int mHeaderDisplay = 56;
    protected final boolean mAreMarginsFixed = true;
    protected List<Track> mTracks = new ArrayList<Track>();
    private EventBus bus = EventBus.getDefault();
    protected LoaderRecycleAsynk.Type originType;
    protected boolean showBack = false;
    protected boolean fragmentVisible = false;


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        callBackOnVisible(isVisibleToUser);

    }

    public void callBackOnVisible(boolean fragmentVisible){
        // do nothing
    }

    // empty, not pass arguments inside contructor
    public AbstracTrackFragment() {
    }

    @Override
    public void onStop() {
        bus.unregister(this);
        super.onDestroy();
    }

    @Override
    public void onStart() {
        bus.register(this);
        super.onStart();
    }

    @Override
    public abstract View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);


    /* CALLED WHEN USER PERFORM A SEARCH ON TOOLBAR */
    public void onEvent(EventSearch event) {
        String q = event.getQuery();
        ((AGenericAdapterRecycle) r.getAdapter()).getFilter().filter(q);
    }

    /* CALLED WHEN USER PERFORM A SEARCH ON TOOLBAR */
    public void onEvent(EventClick event) {
        String filter;

        // prevent load asynk in ither fragment
        if (event.getItemType() == originType) {
            showBack = true;
            updateBackButton();

            Log.i(MainActivity.TAG, getClass().getSimpleName() + " type on event: " + event.getItemType());
            switch (event.getItemType()) {
                case TRACK:
                    // do nothing...user can click on item and start play track only
                    break;
                case ALBUM:
                    LoaderRecycleAsynk<Track> loaderFilteredForAlbum = new LoaderRecycleAsynk<Track>(getActivity(), LoaderRecycleAsynk.Type.TRACK, fastScroller, r);
                    loaderFilteredForAlbum.setFilterForAlbum(event.getAlbum().getName());
                    //loaderFilteredForAlbum.execute();
                    break;
                case ARTIST:
                    LoaderRecycleAsynk<Track> loaderFilteredForArtist = new LoaderRecycleAsynk<Track>(getActivity(), LoaderRecycleAsynk.Type.TRACK, fastScroller, r);
                    loaderFilteredForArtist.setFilterForArtist(event.getArtist().getName());
                    //loaderFilteredForArtist.execute();
                    break;
                case PLAYLIST:
                    LoaderRecycleAsynk<Track> loaderFilteredForPlayList = new LoaderRecycleAsynk<Track>(getActivity(), LoaderRecycleAsynk.Type.TRACK_P, fastScroller, r);
                    loaderFilteredForPlayList.setFilterForPlayList(event.getPlayList().getId());
                    //loaderFilteredForPlayList.execute();
                    break;

            }

        }

    }

    public void updateBackButton(){
        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fabBack);
        if(showBack){
            fab.setVisibility(View.VISIBLE);
            setUpFabClickListener();

        } else {
            fab.setVisibility(View.INVISIBLE);
        }
    }

    /* CALLED WHEN USER PERFORM A SEARCH ON TOOLBAR */
    public abstract void onEvent(EventTabSelected event);

    public abstract void setUpFabClickListener();

}
