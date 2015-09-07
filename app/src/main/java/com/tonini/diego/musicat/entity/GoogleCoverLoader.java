package com.tonini.diego.musicat.entity;

import android.util.Log;

import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tonini.diego.musicat.Const;
import com.tonini.diego.musicat.MainActivity;
import com.tonini.diego.musicat.gcs.Item;
import com.tonini.diego.musicat.gcs.Result;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.google.common.base.CharMatcher.WHITESPACE;
import static java.lang.String.format;

public class GoogleCoverLoader {

    private String q;
    private int num;
    private OkHttpClient client;
    private Request request;
    private Response response;
    private String urlImage = null;

    public GoogleCoverLoader(String q) {
        this.q = q;
    }

    private String getUri(String query) {// api, cs start
        return format("https://www.googleapis.com/customsearch/v1?key=%s&cx=%s&imgSize=%s&q=%s&searchType=%s&alt=json",
                Const.API_KEY,
                Const.CSE_ID,
                "large",
                WHITESPACE.trimAndCollapseFrom(query, '+'),
                "image");
    }

    public List<Item> getUrlImage(){

        List<Item> list = new ArrayList<Item>();
        client = new OkHttpClient();
        request = new Request.Builder().url(getUri(q)).build();
        try {
            response = client.newCall(request).execute();
            if(response.code()==200){
                Result result = new Gson().fromJson(response.body().charStream(),Result.class);
                if(result!=null){
                    /*List<Item>*/ list = result.getItems();
                    if(list.size()>0){
                        Collections.sort(list, new Comparator<Item>() {
                            @Override
                            public int compare(Item i1, Item i2) {
                                int max1 = Math.max(i1.getImage().getHeight(), i1.getImage().getWidth());
                                int min1 = Math.min(i1.getImage().getHeight(), i1.getImage().getWidth());
                                int max2 = Math.max(i2.getImage().getHeight(), i2.getImage().getWidth());
                                int min2 = Math.min(i2.getImage().getHeight(), i2.getImage().getWidth());
                                float ratio1 = (float) max1 / min1;
                                float ratio2 = (float) max2 / min2;
                                return Float.compare(ratio1, ratio2);
                            }
                        });
                        urlImage = list.get(0).getLink();
                    }
                }
            }
        } catch (IOException e) {
            Log.i(MainActivity.TAG,"getUrlImage(): "+e.toString());
        }

        return list;
    }

}
