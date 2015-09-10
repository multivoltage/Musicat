package com.tonini.diego.musicat.fragments;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;
import com.melnykov.fab.FloatingActionButton;
import com.tonicartos.superslim.LayoutManager;
import com.tonini.diego.musicat.Const;
import com.tonini.diego.musicat.MainActivity;
import com.tonini.diego.musicat.R;
import com.tonini.diego.musicat.Utils;
import com.tonini.diego.musicat.entity.PlayList;
import com.tonini.diego.musicat.entity.PlayListManager;
import com.tonini.diego.musicat.events.EventCrud;
import com.tonini.diego.musicat.events.EventTabSelected;
import com.tonini.diego.musicat.recycleviewlist.FastScroller;
import com.tonini.diego.musicat.recycleviewlist.LoaderRecycleAsynk;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

public class PlayListFragment extends AbstracTrackFragment implements View.OnClickListener {

    private FloatingActionButton fabAddPlayList;
    LoaderRecycleAsynk<PlayList> l;
    boolean execute = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tracks_rec, container, false);

        originType = LoaderRecycleAsynk.Type.PLAYLIST;
        r = (ObservableRecyclerView) v.findViewById(R.id.recycler_view);
        r.setLayoutManager(new LayoutManager(getActivity()));
        fastScroller = (FastScroller) v.findViewById(R.id.fast_scroller);

        fabAddPlayList = (FloatingActionButton) getActivity().findViewById(R.id.fabAddPlayList);
        fabAddPlayList.setOnClickListener(this);

        l = new LoaderRecycleAsynk<PlayList>(getActivity(), LoaderRecycleAsynk.Type.PLAYLIST,fastScroller,r);

        return v;
    }

    @Override
    public void setUpFabClickListener() {
        getActivity().findViewById(R.id.fabBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //new LoaderRecycleAsynk<PlayList>(getActivity(), LoaderRecycleAsynk.Type.PLAYLIST, fastScroller, r).execute();
                showBack = false;
                getActivity().findViewById(R.id.fabBack).setVisibility(View.GONE);
            }
        });

    }

    public void onEvent(EventTabSelected event) {
        if(event.getItem()==3){
            Log.d(MainActivity.TAG, "EventBackPress catch by "+getClass().getSimpleName());
            updateBackButton();
        }
    }

    public void onEvent(EventCrud event){
        switch (event.getOperation()){
            case Const.CRUD_DELETE_PLAYLIST :
                //new LoaderRecycleAsynk<PlayList>(getActivity(), LoaderRecycleAsynk.Type.PLAYLIST,fastScroller,r).execute();
                break;
        }
    }

    @Override
    public void onClick(View v) {

        GregorianCalendar calendar = new GregorianCalendar(Locale.getDefault());
        String year = String.valueOf(calendar.get(Calendar.YEAR));
        String month = String.valueOf(calendar.get(Calendar.MONTH));
        String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        String hh  = String.valueOf(calendar.get(Calendar.HOUR));
        String mm  = String.valueOf(calendar.get(Calendar.MINUTE));
        String ss  = String.valueOf(calendar.get(Calendar.SECOND));

        final String title = "Playlist_"+hh+"_"+mm+"_"+ss+","+day+"/"+month+"/"+year;
        // dialog to create PlayLIst
       new MaterialDialog.Builder(getActivity())
                .theme(Utils.getTheme(getActivity()) == Const.THEME_LIGHT ? Theme.LIGHT : Theme.DARK)
                .title("New Playlist")
                .inputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_NUMBER)
                .input("playlist name", title, new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(MaterialDialog dialog, CharSequence input) {
                                long idNewPlayList = PlayListManager.createPlayList(getActivity(), input.toString(), System.currentTimeMillis());
                                if (idNewPlayList != -1) {
                                    Snackbar.make(getView(), "Playlist " + input.toString() + " created" + String.valueOf(idNewPlayList), Snackbar.LENGTH_LONG).show();
                                   // new LoaderRecycleAsynk<PlayList>(getActivity(), LoaderRecycleAsynk.Type.PLAYLIST, fastScroller, r).execute();
                                } else
                                    Snackbar.make(getView(), "I 'm sorry. Please contact me to fix error instead put negative review" + String.valueOf(idNewPlayList), Snackbar.LENGTH_LONG).show();
                            }
                        }

                ).

                    show();

                }
    }
