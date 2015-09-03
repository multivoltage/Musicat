package com.tonini.diego.musicat.http;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;
import com.tonini.diego.musicat.Helper;
import com.tonini.diego.musicat.MusixMatchException;
import com.tonini.diego.musicat.config.Constants;
import com.tonini.diego.musicat.config.Methods;

import org.apache.http.Header;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Diego on 10/05/2015.
 */
public class Aink {


    private AsyncHttpClient client;
    private String url = "http://api.musixmatch.com/ws/1.1/album.get?";

    public Aink() throws MusixMatchException {
    }

    public void provs(Context context) {


        String apiKey = "7f55f6f0f21092fe958525befa12474b";
        String q_track = "rosso relativo";
        String q_artist = "dd";

        Map<String, Object> params = new HashMap<String, Object>();

        params.put(Constants.API_KEY, apiKey);
        params.put(Constants.QUERY_TRACK, q_track);
        params.put(Constants.QUERY_ARTIST, q_artist);


        String requestString = null;
        try {
            requestString = Helper.getURLString(Methods.MATCHER_TRACK_GET, params);
        } catch (MusixMatchException e) {
            Log.i("erorr", e.toString());
        }
        String apiUrl = Constants.API_URL + Constants.API_VERSION + Constants.URL_DELIM + requestString;
        client = new AsyncHttpClient();
        client.get(context, apiUrl, null, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i("onFailure", "statuscode: " + statusCode);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Log.i("onSucces", "statuscode: " + statusCode + "\n\n" + responseString);

            }
        });

    }
}
