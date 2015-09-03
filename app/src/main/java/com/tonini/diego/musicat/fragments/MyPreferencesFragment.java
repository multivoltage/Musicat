package com.tonini.diego.musicat.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.widget.Toast;

import com.tonini.diego.musicat.Const;
import com.tonini.diego.musicat.PlayerService;
import com.tonini.diego.musicat.R;

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
                getActivity().startService(new Intent(getActivity(), PlayerService.class).setAction(canShake ? Const.ACTION_SHAKE_ON : Const.ACTION_SHAKE_OFF));
                Toast.makeText(getActivity(),canShake ? "shake enable" : "shake disable",Toast.LENGTH_SHORT).show();
            } else if (key.equals(Const.SHARED_PREF_KEY_SHOW_BUBBLE)) {
                CheckBoxPreference switchShake = (CheckBoxPreference) findPreference(key);
                boolean showBubble = switchShake.isChecked();
                getActivity().startService(new Intent(getActivity(), PlayerService.class).setAction(showBubble ? Const.ACTION_BUBBLE_ON : Const.ACTION_BUBBLE_OFF));
                Toast.makeText(getActivity(),showBubble ? "bubble enable" : "buble disable",Toast.LENGTH_SHORT).show();
            }
        }
    };


}
