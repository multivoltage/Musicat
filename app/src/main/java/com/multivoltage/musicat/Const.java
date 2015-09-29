package com.multivoltage.musicat;

import android.os.Environment;

import java.io.File;

/**
 * Created by Diego on 20/05/2015.
 */
public class Const {

    // PATH
    public static final String EXTERNAL_MUSIC_FOLDER_PATH = Environment.getExternalStorageDirectory().getPath()+File.separator+"Music";
    // THEME
    public static final int THEME_LIGHT = 10;
    public static final int THEME_DARK  = 20;

    // PREFERENCES
    public static final String MY_PREFERENCES = "com.tonini.diego.musicat.MY_PREFERENCES";
    public static final String KEY_PREF_LAST_TRACK_URI = "com.tonini.diego.musicat.MY_PREFERENCES.KEY_LAST_TRACK_PATH";
    public static final String KEY_PREF_PRIMARY_COLOR = "KEY_PREF_PRIMARY_COLOR";
    public static final String KEY_PREF_SECONDARY_COLOR = "KEY_PREF_SECONDARY_COLOR";
    public static final String KEY_PREF_THEME = "KEY_PREF_THEME";
    public static final String KEY_SHOW_NEWS = "KEY_SHOW_NEWS";
    public static final String KEY_SERVER_ON = "KEY_SERVER_ON";

    // EXTRA KEY
    public static final String KEY_SELECTED_TRACK = "KEY_SELECTED_TRACK";
    public static final String KEY_ADD_QUEUE_TRACK = "KEY_ADD_QUEUE_TRACK";
    public static final String KEY_EDIT_TRACK = "KEY_EDIT_TRACK";
    public static final int REQUEST_CODE_EDIT = 47;

    // SETTING FRAGMENT
    public static final String SHARED_PREF_KEY_SHOW_ART    = "SHARED_PREF_KEY_SHOW_ART";
    public static final String SHARED_PREF_KEY_SHAKE_PHONE = "SHARED_PREF_KEY_SHAKE_PHONE";
    public static final String SHARED_PREF_KEY_SHOW_BUBBLE = "SHARED_PREF_KEY_SHOW_BUBBLE";
    public static final String SHARED_PREF_KEY_SHOW_NOTICE = "SHARED_PREF_KEY_SHOW_CONTROLS";
    public static final String SHARED_PREF_KEY_SERVER      = "SHARED_PREF_KEY_SERVER";


    // ACTIONS
    public static final String ACTION_PLAY_TRACK = "com.tonini.diego.musicat.action.PLAY_TRACK"; // used from adapter to play specific track selected
    public static final String ACTION_PLAY_PAUSE = "com.tonini.diego.musicat.action.PLAY";
    //public static final String ACTION_PAUSE ="com.tonini.diego.musicat.action.PAUSE";
    public static final String ACTION_NEXT = "com.tonini.diego.musicat.action.NEXT";
    public static final String ACTION_PREV = "com.tonini.diego.musicat.action.PREV";
    public static final String ACTION_STOP = "com.tonini.diego.musicat.action.STOP";
    public static final String ACTION_REPEATING = "com.tonini.diego.musicat.action.REPEATING";
    public static final String ACTION_SKEEP_TO = "com.tonini.diego.musicat.action.SKEEP_TO";
    public static final String ACTION_DISMISS = "com.tonini.diego.musicat.action.DISMISS";
    public static final String ACTION_SHAKE_ON = "com.tonini.diego.musicat.action.ACTION_SHAKE_ON";
    public static final String ACTION_SHAKE_OFF = "com.tonini.diego.musicat.action.ACTION_SHAKE_OFF";
    public static final String ACTION_BUBBLE_ON = "com.tonini.diego.musicat.action.ACTION_BUBBLE_ON";
    public static final String ACTION_BUBBLE_OFF = "com.tonini.diego.musicat.action.ACTION_BUBBLE_OFF";
    public static final String ACTION_ADD_TO_QUEUE = "ACTION_ADD_TO_QUEUE";
    public static final String ACTION_REQUEST_STATE_PLAYING = "ACTION_REQUEST_STATE_PLAYING";
    public static final String ACTION_SHOW_NOTICE = "ACTION_SHOW_NOTICE";
    public static final String ACTION_HIDE_NOTICE = "ACTION_HIDE_NOTICE";
    public static final String ACTION_SERVER_ON = "ACTION_SERVER_ON";
    public static final String ACTION_SERVER_OFF = "ACTION_SERVER_OFF";
    public static final String ACTION_INCOMIN_CALL = "ACTION_INCOMIN_CALL";


    // EXTRA INTENT
    public static final String EXTRA_KEY_SKEEP_TO = "com.tonini.diego.musicat.extra.SKEEP_TO";

    // API GOOGLE IMAGE
    public static final String CSE_ID = "014879161634992552473:vlzst2sowkk";
    public static final String API_KEY = "AIzaSyDd5JlZlRK5o21FYzh0JkSDRsM-CjEq3U4";

    // EVENTS
    public static final String PLAY_EVENT = "PLAY_EVENT";
    public static final String PAUSE_EVENT = "PAUSE_EVENT";
    public static final String PREV_EVENT = "PREV_EVENT";
    public static final String NEXT_EVENT = "NEXT_EVENT";
    public static final String CHANGED_EVENT = "CHANGED_EVENT";
    public static final String LIST_LOADED_EVENT = "LIST_LOADED_EVENT";
    public static final String SKIP_TO_EVENT = "SKIP_TO_EVNET";
    public static final String PLAYING_STATE_EVENT = "LOADED_STORED_EVENT"; // used in inResume also for reload state of button and artwork and data text
    public static final String DISMIS_EVENT = "DISMIS_EVENT";
    public static final String SEARCH_EVENT = "SEARCH_EVENT";

    public static final String CRUD_DELETE_PLAYLIST = "CRUD_DELETE_PLAYLIST";
    public static final String CRUD_ADD_TRACK_TO_PLAYLIST = "CRUD_ADD_TRACK_TO_PLAYLIST";
    public static final String SITE_STRING = "<html>\n" +
            "    <head>\n" +
            "        <title>Musicat Web</title>\n" +
            "        <meta charset=\"UTF-8\">\n" +
            "        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
            "        <link href=\"css/style.css\" rel=\"stylesheet\" type=\"text/css\"/>\n" +
            "       \n" +
            "    </head>\n" +
            "    \n" +
            "    <body>\n" +
            "\n" +
            "        <div id=\"container\">\n" +
            "            \n" +
            "            <table>\n" +
            "                <thead>\n" +
            "                    thead\n" +
            "                </thead>\n" +
            "                <table style=\"width:100%\">\n" +
            "                    <tr>\n" +
            "                      <td>Jill</td>\n" +
            "                      <td>Smith</td> \n" +
            "                      <td>50</td>\n" +
            "                    </tr>\n" +
            "                    <tr>\n" +
            "                      <td>Eve</td>\n" +
            "                      <td>Jackson</td> \n" +
            "                      <td>94</td>\n" +
            "                    </tr>\n" +
            "                  </table>\n" +
            "            </table>\n" +
            "            \n" +
            "        </div>\n" +
            "        \n" +
            "    </body>\n" +
            "\n" +
            "</html>";
}
