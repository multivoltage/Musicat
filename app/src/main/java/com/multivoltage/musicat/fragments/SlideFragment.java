package com.multivoltage.musicat.fragments;

import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.multivoltage.musicat.Const;
import com.multivoltage.musicat.PlayerService;
import com.multivoltage.musicat.R;
import com.multivoltage.musicat.Utils;
import com.multivoltage.musicat.entity.LoadImageFileAsynk;
import com.multivoltage.musicat.entity.Track;
import com.multivoltage.musicat.events.EventTrack;

import java.io.File;
import java.util.concurrent.TimeUnit;

import de.greenrobot.event.EventBus;


public class SlideFragment extends Fragment implements SeekBar.OnSeekBarChangeListener ,View.OnClickListener{

    private TextView tvTitleHeader,tvArtistHeader,tvTimePassed,tvTimeTotal,tvGoYoutTube,tvGoWikipedia;
    private ImageView imgPrevHeader, imgPPHeader, imgNextHeader,imgPrev, imgPP, imgNext,imgActiveRepeat,imgIsActiveRepeat;
    private SeekBar seekBar;
    private ImageView circleImageView;
    private ImageView imageViewHeader;
    private int duration, current = 0,delta=0;
    private Track currentTrack = null; // used for wiki and youtube
    private Handler customHandler = new Handler();
    private EventBus bus = EventBus.getDefault();
    private LinearLayout linearControlsHeader;
    private RelativeLayout relativeLayoutContainer;

    public static SlideFragment newInstance() {
        SlideFragment fragment = new SlideFragment();
        return fragment;
    }
    public SlideFragment() {
    }

    @Override
    public void onResume(){;
        getActivity().startService(new Intent(getActivity(), PlayerService.class).setAction(Const.ACTION_REQUEST_STATE_PLAYING));
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_slide_panel, container, false);

        tvTitleHeader = (TextView) v.findViewById(R.id.tvTitleHeader);
        tvArtistHeader = (TextView) v.findViewById(R.id.tvArtistHeader);
        tvTimeTotal = (TextView) v.findViewById(R.id.tvTimeTotal);
        tvTimePassed = (TextView) v.findViewById(R.id.tvTimePassed);
        tvGoYoutTube = (TextView) v.findViewById(R.id.tvGoYoutTube);
        tvGoWikipedia = (TextView) v.findViewById(R.id.tvGoWikiPedia);

        tvGoWikipedia.setOnClickListener(this);
        tvGoYoutTube.setOnClickListener(this);

        // header
        imgPrevHeader = (ImageView) v.findViewById(R.id.imgPrevHeader);
        imgNextHeader = (ImageView) v.findViewById(R.id.imgNextHeader);
        imgPPHeader = (ImageView) v.findViewById(R.id.imgPPHeader);
        // normal
        imgPrev = (ImageView) v.findViewById(R.id.imgPrev);
        imgNext = (ImageView) v.findViewById(R.id.imgNext);
        imgPP = (ImageView) v.findViewById(R.id.imgPP);
        imgActiveRepeat = (ImageView) v.findViewById(R.id.imgActiveRepeat);
        imgIsActiveRepeat = (ImageView) v.findViewById(R.id.imgIsActiveRepeat);

        // header
        imgPrevHeader.setOnClickListener(this);
        imgPPHeader.setOnClickListener(this);
        imgNextHeader.setOnClickListener(this);
        // normal
        imgPrev.setOnClickListener(this);
        imgPP.setOnClickListener(this);
        imgNext.setOnClickListener(this);
        imgActiveRepeat.setOnClickListener(this);


        circleImageView = (ImageView) v.findViewById(R.id.profile_image);
        imageViewHeader = (ImageView) v.findViewById(R.id.profile_image_header);

        seekBar = (SeekBar) v.findViewById(R.id.progress);
        seekBar.setOnSeekBarChangeListener(this);

        relativeLayoutContainer = (RelativeLayout) v.findViewById(R.id.relativeLayoutContainer);

        linearControlsHeader = (LinearLayout) v.findViewById(R.id.linearControlsHeader);

        tvTitleHeader = (TextView) v.findViewById(R.id.tvTitleHeader);
        tvTitleHeader.setSelected(true);


        // SET-UP COLOR
        int colorPrimary = Utils.getPrimaryColor(getActivity());
        v.findViewById(R.id.relContainerHeader).setBackgroundColor(colorPrimary);
        v.findViewById(R.id.linearLayoutFooterControls).setBackgroundColor(colorPrimary);

        return v;
    }
    Rect rect;
    public void updateDataForNewTrack(int duration){
        // duration in millisec, -1 if not available
        seekBar.setMax(duration);           // with new track
        seekBar.setProgress(current);
        tvTimeTotal.setText(String.format("%d:%d",
                TimeUnit.MILLISECONDS.toMinutes(duration),
                TimeUnit.MILLISECONDS.toSeconds(duration) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration))
        ));
    }

    private void removeCallBack(){
        customHandler.removeCallbacks(runnable);// sempre
    }

    private void animateCircle(boolean animate){
        if(animate)// play
            customHandler.postDelayed(runnable, 200);
    }


    private void updateDataTextForNewTrack(Track track){
        String title = track.getTitle();
        String artist = track.getArtist();

        if(title!=null)
            tvTitleHeader.setText(title);
        if(artist!=null)
            tvArtistHeader.setText(artist);
    }

    Runnable runnable = new Runnable() {
        @Override public void run() {
            seekBar.setProgress(seekBar.getProgress()+200);
            tvTimePassed.setText(String.format("%d:%d",
                    TimeUnit.MILLISECONDS.toMinutes(seekBar.getProgress()),
                    TimeUnit.MILLISECONDS.toSeconds(seekBar.getProgress()) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(seekBar.getProgress()))
            ));
            customHandler.postDelayed(runnable, 200);

        }
    };


    @Override
    public void onStop() {
        bus.unregister(this);
        super.onDestroy();
    }

    @Override
    public void onStart(){
        bus.register(this);
        super.onStart();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {}
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        customHandler.removeCallbacks(runnable);
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        customHandler.postDelayed(runnable, 200);
        getActivity().startService(new Intent(getActivity(), PlayerService.class).setAction(Const.ACTION_SKEEP_TO).putExtra(Const.EXTRA_KEY_SKEEP_TO,seekBar.getProgress()));
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            // header
            case R.id.imgPrevHeader: getActivity().startService(new Intent(getActivity(), PlayerService.class).setAction(Const.ACTION_PREV));
                break;
            case R.id.imgPPHeader:   getActivity().startService(new Intent(getActivity(), PlayerService.class).setAction(Const.ACTION_PLAY_PAUSE));
                break;
            case R.id.imgNextHeader: getActivity().startService(new Intent(getActivity(), PlayerService.class).setAction(Const.ACTION_NEXT));
                break;

            // normal
            case R.id.imgPrev: getActivity().startService(new Intent(getActivity(), PlayerService.class).setAction(Const.ACTION_PREV));
                break;
            case R.id.imgPP:   getActivity().startService(new Intent(getActivity(), PlayerService.class).setAction(Const.ACTION_PLAY_PAUSE));
                break;
            case R.id.imgNext: getActivity().startService(new Intent(getActivity(), PlayerService.class).setAction(Const.ACTION_NEXT));
                break;
            case R.id.imgActiveRepeat: getActivity().startService(new Intent(getActivity(), PlayerService.class).setAction(Const.ACTION_REPEATING));
                if(imgIsActiveRepeat.getVisibility()==View.VISIBLE)
                    imgIsActiveRepeat.setVisibility(View.INVISIBLE);
                else
                    imgIsActiveRepeat.setVisibility(View.VISIBLE);
                break;
            case R.id.tvGoWikiPedia :
                /*try {
                    String url = java.net.URLEncoder.encode("https://www.google.it/webhp?q=");//+currentTrack.getTitle(), "UTF-8").replace(" ", "+");
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(browserIntent);
                } catch (UnsupportedEncodingException e) {
                    Log.e(MainActivity.TAG,e.toString());
                };*/
                break;
            case R.id.tvGoYoutTube :
                String url = "http://m.youtube.com/results?q="+currentTrack.getTitle();
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(browserIntent);
                break;
        }

    }


    public interface OnNewPosSelectedListener {
        void onNewPosSelected(int position);
    }


    public void onEvent(EventTrack event) {

        if(event.getTrack()!=null)
            currentTrack = event.getTrack();

        switch (event.getEvent()) {
            case Const.PLAY_EVENT:
                removeCallBack();// sempre
                animateCircle(true);
                imgPPHeader.setImageResource(R.mipmap.pause_minimal);
                imgPP.setImageResource(R.mipmap.pause_minimal);
                break;
            case Const.PAUSE_EVENT:
                removeCallBack();// sempre
                animateCircle(false);
                imgPPHeader.setImageResource(R.mipmap.play_minimal);
                imgPP.setImageResource(R.mipmap.play_minimal);
                break;
            case Const.NEXT_EVENT:
                current = 0;
                removeCallBack();
                updateDataForNewTrack(event.getDuration());
                updateDataTextForNewTrack(event.getTrack());
                updateImage(event.getTrack().getArtUri());
                animateCircle(true);
                imgPPHeader.setImageResource(R.mipmap.pause_minimal);
                imgPP.setImageResource(R.mipmap.pause_minimal);
                break;
            case Const.PREV_EVENT:
                current = 0;
                removeCallBack();
                updateDataForNewTrack(event.getDuration());
                updateDataTextForNewTrack(event.getTrack());
                updateImage(event.getTrack().getTrackUri());
                animateCircle(true);
                imgPPHeader.setImageResource(R.mipmap.pause_minimal);
                imgPP.setImageResource(R.mipmap.pause_minimal);
                break;
            case Const.PLAYING_STATE_EVENT:
                removeCallBack();
                current = event.getCurrent();
                updateDataForNewTrack(event.getDuration());
                updateDataTextForNewTrack(event.getTrack());
                updateImage(event.getTrack().getTrackUri());
                if(event.getState()==EventTrack.IS_PLAY){
                    animateCircle(true);
                    imgPP.setImageResource(R.mipmap.pause_minimal);
                    imgPPHeader.setImageResource(R.mipmap.pause_minimal);
                } else {
                    animateCircle(false);
                    imgPP.setImageResource(R.mipmap.play_minimal);
                    imgPPHeader.setImageResource(R.mipmap.play_minimal);
                }
                break;
            case Const.CHANGED_EVENT:
                current = 0;
                removeCallBack();
                updateDataForNewTrack(event.getDuration());
                updateDataTextForNewTrack(event.getTrack());
                updateImage(event.getTrack().getTrackUri());
                animateCircle(true);
                imgPPHeader.setImageResource(R.mipmap.pause_minimal);
                imgPP.setImageResource(R.mipmap.pause_minimal);
                break;

        }


    }

    public void notifyPanelExpanded(){
        linearControlsHeader.setVisibility(View.GONE);
        tvTitleHeader.setEnabled(true);
    }

    public void notifyPanelCollapsed(){
        linearControlsHeader.setVisibility(View.VISIBLE);
        tvTitleHeader.setEnabled(true);
    }

    private void updateImage(Uri uri){

        new LoadImageFileAsynk(new File(uri.toString()), getActivity(),100) {

            @Override
            protected void onPostExecute(File fileImage) {
                Picasso.with(getActivity())
                        .load(fileImage)
                        .placeholder(R.drawable.unknow_background)
                        .into(circleImageView);
                Picasso.with(getActivity())
                        .load(fileImage)
                        .placeholder(R.mipmap.unknow)
                        .into(imageViewHeader);
            }
        }.execute();
    }
}
