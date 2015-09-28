package com.tonini.diego.musicat.fragments;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;
import com.tonicartos.superslim.LayoutManager;
import com.tonini.diego.musicat.Const;
import com.tonini.diego.musicat.MainActivity;
import com.tonini.diego.musicat.R;
import com.tonini.diego.musicat.Utils;
import com.tonini.diego.musicat.entity.Album;
import com.tonini.diego.musicat.events.EventTabSelected;
import com.tonini.diego.musicat.recycleviewlist.FastScroller;
import com.tonini.diego.musicat.recycleviewlist.LoaderRecycleAsynk;

import static com.tonini.diego.musicat.recycleviewlist.LoaderRecycleAsynk.Type.ALBUM;

public class InfoFragment extends Fragment {


    private TextView tvAppNameL,tvVersionL,tvDeveloperL,tvAppNameR,tvVersionR,tvDeveloperR;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_info, container, false);

        tvAppNameL = (TextView) v.findViewById(R.id.tvAppNameL);
        tvDeveloperL = (TextView) v.findViewById(R.id.tvDeveloperL);
        tvVersionL = (TextView)  v.findViewById(R.id.tvVersionL);
        tvAppNameR = (TextView) v.findViewById(R.id.tvAppNameR);
        tvDeveloperR = (TextView) v.findViewById(R.id.tvDeveloperR);
        tvVersionR = (TextView) v.findViewById(R.id.tvVersionR);

        int theme = Utils.getTheme(getActivity());
        int darkColor = getResources().getColor(android.R.color.background_dark);
        int lightColor = getResources().getColor(android.R.color.background_light);

        if(theme== Const.THEME_DARK){

            v.findViewById(R.id.info_container).setBackgroundColor(darkColor);

            tvVersionL.setTextColor(lightColor);
            tvDeveloperL.setTextColor(lightColor);
            tvAppNameR.setTextColor(lightColor);
            tvVersionR.setTextColor(lightColor);
            tvDeveloperR.setTextColor(lightColor);
            tvAppNameL.setTextColor(lightColor);


        } else {
            v.findViewById(R.id.info_container).setBackgroundColor(lightColor);

            tvVersionL.setTextColor(darkColor);
            tvDeveloperL.setTextColor(darkColor);
            tvAppNameL.setTextColor(darkColor);
            tvVersionR.setTextColor(darkColor);
            tvDeveloperR.setTextColor(darkColor);
            tvAppNameR.setTextColor(darkColor);
        }

        PackageInfo pInfo = null;
        try {
            pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
            String version = pInfo.versionName;
            tvVersionR.setText(version);
        } catch (PackageManager.NameNotFoundException e) {
            tvVersionR.setText("n/a");
        }


        return v;
    }

}
