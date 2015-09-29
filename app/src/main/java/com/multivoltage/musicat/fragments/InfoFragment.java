package com.multivoltage.musicat.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.multivoltage.musicat.Const;
import com.multivoltage.musicat.R;
import com.multivoltage.musicat.Utils;

public class InfoFragment extends Fragment {




    private TextView tvVersionR;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_info, container, false);


        tvVersionR = (TextView) v.findViewById(R.id.tvVersionR);

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
