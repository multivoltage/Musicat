package com.multivoltage.musicat.entity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;

import com.multivoltage.musicat.Const;
import com.multivoltage.musicat.PlayerService;

/**
 * Created by multivoltage on 9/28/15.
 */
public class MyPhoneStateListener extends PhoneStateListener {

    Context mContext;

    public MyPhoneStateListener(Context context){
        mContext = context;
    }

    public void onCallStateChanged(int state, String incomingNumber) {

        // Log.d("MyPhoneListener",state+"   incoming no:"+incomingNumber);

        // 1 incoming
        // 2 speaking
        // 0 close call
        switch (state){
            case 1:

                if(isMyServiceRunning(PlayerService.class,mContext))
                    mContext.startService(new Intent(mContext, PlayerService.class).setAction(Const.ACTION_INCOMIN_CALL));
                break;
            /*case 0:
                mContext.startService(new Intent(mContext, PlayerService.class).setAction(Const.ACTION_PLAY_PAUSE));
                break;*/

        }
    }

    private boolean isMyServiceRunning(Class<?> serviceClass,Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
