package com.tonini.diego.musicat.entity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorEventListener2;
import android.hardware.SensorManager;
import android.os.SystemClock;
import android.util.Log;

import com.tonini.diego.musicat.MainActivity;

/**
 * Created by Diego on 05/08/2015.
 */
public class ShakeEventListener implements SensorEventListener {

    private static final int MIN_FORCE = 15; // original was 10 for 3 axis
    private static final int MIN_DIRECTION = 3;
    private static final int MAX_PAUSE_BETWEEN_DIRECTION_CHANGE = 200; //ms
    private static final int MAX_TOTAL_DURATION_OF_SHAKE = 300;
    private long mFirstDirectionChangeTime = 0;
    private long mLastDirectionChangeTime;
    private int mDirectionCount = 0;

    private float lastX = 0;
    private float lastY = 0;
    private float lastZ = 0;

    private OnShakeListener mShakeListener;

    public interface OnShakeListener {
        void onShake();
    }

    public void setOnShakeListener(OnShakeListener listener){
        mShakeListener = listener;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        //Log.i(MainActivity.TAG,"x y z :"+x+"-----"+y+"-------"+z);


        // calc movement
        float totalMovement = Math.abs(x+y+z-lastX-lastY-lastZ);
        if(totalMovement > MIN_FORCE){
            // get time
            long currentTime = System.currentTimeMillis();

            if(mFirstDirectionChangeTime == 0){
                mFirstDirectionChangeTime = currentTime;
                mLastDirectionChangeTime  = currentTime;
            }

            long lastChangeWasAgo = currentTime - mLastDirectionChangeTime;
            if(lastChangeWasAgo < MAX_PAUSE_BETWEEN_DIRECTION_CHANGE){

                // store data
                mLastDirectionChangeTime = currentTime;
                mDirectionCount++;

                lastX = x;
                lastY = y;
                lastZ = x;

                if(mDirectionCount>=MIN_DIRECTION){
                    long totaleDuration = currentTime- mFirstDirectionChangeTime;
                    if(totaleDuration < MAX_TOTAL_DURATION_OF_SHAKE){
                        Log.i(MainActivity.TAG,"shaked count: "+mDirectionCount);
                        mShakeListener.onShake();
                        resetParams();
                    }
                }

            } else {
                resetParams();
            }
        }
    }

    private void resetParams(){
        mFirstDirectionChangeTime = 0;
        mDirectionCount = 0;
        mLastDirectionChangeTime = 0;
        lastX = 0;
        lastY = 0;
        lastZ = 0;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // nothing to do
    }
}
