package com.multivoltage.musicat;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


import java.util.List;


public class MainPagerAdapter extends FragmentPagerAdapter {

    private static String tabTitles[] = new String[] {
            String.valueOf(R.string.all_songs),
            "Albums",
            String.valueOf(R.string.artists),
            "Playlist"
    };
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

