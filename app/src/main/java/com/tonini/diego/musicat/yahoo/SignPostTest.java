package com.tonini.diego.musicat.yahoo;

import android.net.Uri;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tonini.diego.musicat.Const;
import com.tonini.diego.musicat.entity.CoverLoaderAsynk;
import com.tonini.diego.musicat.gcs.Item;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.BasicConfigurator;
import org.json.JSONArray;
import org.json.JSONObject;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.basic.DefaultOAuthConsumer;

import static com.google.common.base.CharMatcher.WHITESPACE;
import static java.lang.String.format;

public class SignPostTest {

    protected static String yahooServer = "https://yboss.yahooapis.com/ysearch/";
    // Please provide your consumer key here
    private static String consumer_key = "dj0yJmk9NTRhZE5OTHBDVUtWJmQ9WVdrOVVHbENkbE5KTnpRbWNHbzlNQS0tJnM9Y29uc3VtZXJzZWNyZXQmeD03ZA--";
    // Please provide your consumer secret here
    private static String consumer_secret = "80b255e743afaab4a9a5b6e61af542e437d2ac6c";
    private static StHttpRequest httpsRequest = new StHttpRequest();
    private static final String ENCODE_FORMAT = "UTF-8";
    private static final String callType = "images";
    private static final int HTTP_STATUS_OK = 200;
    private String q  = "";
    private String urlImage = null;

    public SignPostTest(String q){
        this.q = q;
    }
    public String getUrlImage() throws UnsupportedEncodingException, Exception{

        if(this.isConsumerKeyExists() && this.isConsumerSecretExists()) {
            // Start with call Type
            // Add query
            //params = params.concat("?q=");
            // Encode Query string before concatenating
            //params = params.concat(, "UTF-8"));
            // Create final URL

            String url = getUri();
            Log.i(CoverLoaderAsynk.TAG_COVER,"url request: "+url);
            // Create oAuth Consumer
            OAuthConsumer consumer = new DefaultOAuthConsumer(consumer_key, consumer_secret);
            // Set the HTTPS request correctly
            httpsRequest.setOAuthConsumer(consumer);

            try {
                //Log.i(CoverLoaderAsynk.TAG_COVER,"sending get request to" + url);
                int responseCode = httpsRequest.sendGetRequest(url);

                // Send the request
                if(responseCode == HTTP_STATUS_OK) {
                   // Log.i(CoverLoaderAsynk.TAG_COVER, "Response ");
                } else {
                    Log.i(CoverLoaderAsynk.TAG_COVER, "Error in response due to status code = " + responseCode);
                }
                String body = httpsRequest.getResponseBody();
                //Log.i(CoverLoaderAsynk.TAG_COVER, body);

                JSONArray jsonArray = new JSONObject(body).getJSONObject("bossresponse").getJSONObject("images").getJSONArray("results");
                //Log.i(CoverLoaderAsynk.TAG_COVER, "jsonArray: "+jsonArray.toString());
                Type listType = new TypeToken<ArrayList<Result>>() {
                }.getType();
                List<Result> list  = new Gson().fromJson(jsonArray.toString(), listType);
                if(list.size()>0){
                    Collections.sort(list, new Comparator<Result>() {
                        @Override
                        public int compare(Result r1, Result r2) {
                            int max1 = Math.max(r1.getHeight(), r1.getWidth());
                            int min1 = Math.min(r1.getHeight(), r1.getWidth());
                            int max2 = Math.max(r2.getHeight(), r2.getWidth());
                            int min2 = Math.min(r2.getHeight(), r2.getWidth());
                            float ratio1 = (float) max1 / min1;
                            float ratio2 = (float) max2 / min2;
                            return Float.compare(ratio1, ratio2);
                        }
                    });
                    urlImage = list.get(0).getUrl();
                }

            } catch(UnsupportedEncodingException e) {
                Log.e(CoverLoaderAsynk.TAG_COVER, "Encoding/Decording error");
            } catch (IOException e) {
                Log.e(CoverLoaderAsynk.TAG_COVER, "Error with HTTP IO", e);
            } catch (Exception e) {
                Log.e(CoverLoaderAsynk.TAG_COVER,httpsRequest.getResponseBody(), e);
            }

        } else {
            Log.e(CoverLoaderAsynk.TAG_COVER, "Key/Secret does not exist");
        }
        return urlImage;
    }

    // - rimane ma mette + invece che %20 Uri.encode mette %20
    private String getUri() throws UnsupportedEncodingException {// api, cs start
        return "https://yboss.yahooapis.com/ysearch/images?q="+Uri.encode(q)+"&format=json&count=5&dimensions=medium";
    }

    private void setSearchString(String q){
        this.q = q;
    }


    private boolean isConsumerKeyExists() {
        if(consumer_key.isEmpty()) {
            Log.e(CoverLoaderAsynk.TAG_COVER, "Consumer Key is missing. Please provide the key");
            return false;
        }
        return true;
    }

    private boolean isConsumerSecretExists() {
        if(consumer_secret.isEmpty()) {
            Log.e(CoverLoaderAsynk.TAG_COVER, "Consumer Secret is missing. Please provide the key");
            return false;
        }
        return true;
    }
}