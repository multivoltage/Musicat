package com.multivoltage.musicat.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.multivoltage.musicat.Const;
import com.multivoltage.musicat.PlayerService;
import com.multivoltage.musicat.R;
import com.multivoltage.musicat.Utils;

public class MyPreferencesFragment extends PreferenceFragment {

    private SharedPreferences pref;
    public static final String SHARED_PREF_KEY_SHOW_NOTICE = "SHARED_PREF_KEY_SHOW_NOTICE";
    public static final String SHARED_PREF_KEY_SHOW_ART = "SHARED_PREF_KEY_SHOW_ART";
    public static final String SHARED_PREF_KEY_JACK_INSERT = "SHARED_PREF_KEY_JACK_INSERT";
    public static final String SHARED_PREF_KEY_SHAKE_PHONE = "SHARED_PREF_KEY_SHAKE_PHONE";

    public static final String SHARED_PREF_KEY_COLOR_PRIMARY = "SHARED_PREF_KEY_COLOR_PRIMARY";
    public static final String SHARED_PREF_KEY_COLOR_PRIMARY_DARK = "SHARED_PREF_KEY_COLOR_PRIMARY_DARK";
    public static final String SHARED_PREF_KEY_COLOR_SECONDARY = "SHARED_PREF_KEY_COLOR_SECONDARY";
    public static final String SHARED_PREF_KEY_TEXT_COLOR= "SHARED_PREF_KEY_TEXT_COLOR";

    private View mView;

    OnAspectChaned mCallback;
    Context mContext;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        mContext = activity;
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnAspectChaned) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnAspectChaned");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        mView = view;
        setUpTheme();
        return view;
    }


    void setUpTheme(){
        int theme = Utils.getTheme(getActivity());
        if(theme==Const.THEME_DARK)
            mView.setBackgroundColor(getResources().getColor(R.color.grey_700));
        else
            mView.setBackgroundColor(getResources().getColor(R.color.grey_100));

    }
    @Override
    public void onCreate(final Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        preferences.registerOnSharedPreferenceChangeListener(spChanged);

    }

    SharedPreferences.OnSharedPreferenceChangeListener spChanged = new  SharedPreferences.OnSharedPreferenceChangeListener(){
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            if(key.equals(Const.SHARED_PREF_KEY_SHAKE_PHONE)){
                SwitchPreference switchShake = (SwitchPreference) findPreference(key);
                boolean canShake = switchShake.isChecked();
                mContext.startService(new Intent(mContext, PlayerService.class).setAction(canShake ? Const.ACTION_SHAKE_ON : Const.ACTION_SHAKE_OFF));
                Toast.makeText(getActivity(),canShake ? "shake on" : "shake off",Toast.LENGTH_SHORT).show();
            } else if (key.equals(Const.SHARED_PREF_KEY_SHOW_BUBBLE)) {
                CheckBoxPreference switchShake = (CheckBoxPreference) findPreference(key);
                boolean showBubble = switchShake.isChecked();
                mContext.startService(new Intent(mContext, PlayerService.class).setAction(showBubble ? Const.ACTION_BUBBLE_ON : Const.ACTION_BUBBLE_OFF));
                Toast.makeText(getActivity(),showBubble ? "bubble on" : "bubble off",Toast.LENGTH_SHORT).show();
            } else if(key.equals(Const.SHARED_PREF_KEY_SHOW_NOTICE)){
                CheckBoxPreference checkBoxPreference = (CheckBoxPreference) findPreference(key);
                boolean canShow = checkBoxPreference.isChecked();
                mContext.startService(new Intent(mContext,PlayerService.class).setAction(canShow ? Const.ACTION_SHOW_NOTICE : Const.ACTION_HIDE_NOTICE));
            } else if(key.equals(Const.SHARED_PREF_KEY_SERVER)){

                SwitchPreference switchPreference = (SwitchPreference) findPreference(key);
                boolean canSwitchOn = switchPreference.isChecked();
                mContext.startService(new Intent(mContext,PlayerService.class).setAction(canSwitchOn ? Const.ACTION_SERVER_ON : Const.ACTION_SERVER_OFF));

            } else if(key.equals(Const.KEY_PREF_PRIMARY_COLOR)){
                // notify activity to refresh toolba
                mCallback.notifyPrimaryColorSelected();
            } else if(key.equals(Const.KEY_PREF_SECONDARY_COLOR)){
                mCallback.notifySecondaryColorSelected();
            } else if(key.equals(Const.KEY_PREF_THEME)){
                setUpTheme();
                mCallback.notifyThemeChanged();

            }
        }
    };

    public interface  OnAspectChaned {
        void notifyPrimaryColorSelected();
        void notifyThemeChanged();
        void notifySecondaryColorSelected();
    }


}
