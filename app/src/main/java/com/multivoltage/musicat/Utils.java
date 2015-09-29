package com.multivoltage.musicat;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;

import com.multivoltage.musicat.gcs.Item;

import java.io.File;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteOrder;

/**
 * Created by Diego on 29/05/2015.
 */
public class Utils {

    public static int getProgressPercentage(long currentDuration, long totalDuration){
        Double percentage = (double) 0;

        long currentSeconds = (int) (currentDuration / 1000);
        long totalSeconds = (int) (totalDuration / 1000);

        // calculating percentage
        percentage =(((double)currentSeconds)/totalSeconds)*100;

        // return percentage
        return percentage.intValue();
    }

    public static int progressToTimer(int progress, int totalDuration) {
        int currentDuration = 0;
        totalDuration = (int) (totalDuration / 1000);
        currentDuration = (int) ((((double)progress) / 100) * totalDuration);

        // return current duration in milliseconds
        return currentDuration * 1000;
    }


    public static String getNameWithoutExt(String s){
        if(s.contains(".")){
            return s.substring(0,s.lastIndexOf("."));
        }
        return s;
    }

    public static void logState(Class c,String method){
        Log.i(MainActivity.TAG, method + " called by " + c.getSimpleName());
    }




    public static float getRatio(Item item){
        int w = item.getImage().getWidth();
        int h = item.getImage().getHeight();
        int max = Math.max(w, h);
        int min = Math.min(w, h);
        float ratio = (float) max/min;
        return ratio;
    }

    public static float convertDpToPixel(float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return px;
    }

    public static float convertPixelsToDp(float px, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / (metrics.densityDpi / 160f);
        return dp;
    }

    public static int[] colorChoice(Context context){

        int[] mColorChoices=null;
        String[] color_array = context.getResources().getStringArray(R.array.default_color_choice_values);

        if (color_array!=null && color_array.length>0) {
            mColorChoices = new int[color_array.length];
            for (int i = 0; i < color_array.length; i++) {
                mColorChoices[i] = Color.parseColor(color_array[i]);
            }
        }
        return mColorChoices;
    }

    /**
     * Parse whiteColor
     *
     * @return
     */
    public static int parseWhiteColor(){
        return Color.parseColor("#FFFFFF");
    }

    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }


    public static void saveLastTrack(Uri uri,Context context){
        SharedPreferences.Editor editor = context.getSharedPreferences(Const.MY_PREFERENCES,Context.MODE_PRIVATE).edit();
        editor.putString(Const.KEY_PREF_LAST_TRACK_URI,uri.toString());
        editor.apply();
    }

    public static Uri loadLastTrack(Context context){
        return Uri.parse(context.getSharedPreferences(Const.MY_PREFERENCES,Context.MODE_PRIVATE).getString(Const.KEY_PREF_LAST_TRACK_URI,"not_existing"));
    }

    public static int getDarkerColor(int colorRes,float valueComponent){
        float[] hsv = new float[3];
        Color.colorToHSV(colorRes, hsv);
        hsv[2] *= valueComponent; //0.8f; // value component
        return Color.HSVToColor(hsv);
    }

    public static void savePrimaryColor(Context context,int color){
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putInt(Const.KEY_PREF_PRIMARY_COLOR, color);
    }
    public static int getPrimaryColor(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context).getInt(Const.KEY_PREF_PRIMARY_COLOR, R.color.ColorPrimary);
    }
    public static void saveSecondaryyColor(Context context,int color){
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putInt(Const.KEY_PREF_SECONDARY_COLOR, color);
    }
    public static int getSecondaryColor(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context).getInt(Const.KEY_PREF_SECONDARY_COLOR,R.color.ColorPrimary);
    }
    public static int getTheme(Context context){
        int defaultTheme = Const.THEME_LIGHT;
        String theme = PreferenceManager.getDefaultSharedPreferences(context).getString(Const.KEY_PREF_THEME, String.valueOf(defaultTheme));
        return Integer.parseInt(theme);
    }

    public static boolean showBubble(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(Const.SHARED_PREF_KEY_SHOW_BUBBLE, false);
    }

    public static void vibrate(Context context,long ms){
        Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        v.vibrate(ms);
    }

    public static boolean isSeverOnLastUsage(Context context){
       return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(Const.KEY_SERVER_ON,false);
    }

    public static String wifiIpAddress(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        int ipAddress = wifiManager.getConnectionInfo().getIpAddress();

        // Convert little-endian to big-endianif needed
        if (ByteOrder.nativeOrder().equals(ByteOrder.LITTLE_ENDIAN)) {
            ipAddress = Integer.reverseBytes(ipAddress);
        }

        byte[] ipByteArray = BigInteger.valueOf(ipAddress).toByteArray();

        String ipAddressString;
        try {
            ipAddressString = InetAddress.getByAddress(ipByteArray).getHostAddress();
        } catch (UnknownHostException ex) {
            Log.e("WIFIIP", "Unable to get host address.");
            ipAddressString = null;
        }

        return ipAddressString;
    }

    public static boolean canShowNotice(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(Const.SHARED_PREF_KEY_SHOW_NOTICE,true);
    }
    public static boolean canShowArt(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(Const.SHARED_PREF_KEY_SHOW_ART,true);
    }


}
