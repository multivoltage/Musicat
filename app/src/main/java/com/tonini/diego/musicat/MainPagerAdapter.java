package com.tonini.diego.musicat;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;


import com.tonini.diego.musicat.fragments.AlbumsFragment;
import com.tonini.diego.musicat.fragments.AllSongFragment;
import com.tonini.diego.musicat.fragments.ArtistFragment;
import com.tonini.diego.musicat.fragments.PlayListFragment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.util.ErrorDialogManager;


public class MainPagerAdapter extends FragmentPagerAdapter {

    private static String tabTitles[] = new String[] { "All Song","Albums","Artist","Playlist"};
    private List<Fragment> fragments;

    public MainPagerAdapter(FragmentManager fm,List<Fragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }


    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }


}

