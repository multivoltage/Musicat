package com.tonini.diego.musicat.entity;

import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.util.Log;
import android.widget.Toast;

import com.tonini.diego.musicat.Const;
import com.tonini.diego.musicat.MainActivity;
import com.tonini.diego.musicat.PlayerService;

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
                mContext.startService(new Intent(mContext, PlayerService.class).setAction(Const.ACTION_PLAY_PAUSE));
                break;
            case 0:
                mContext.startService(new Intent(mContext, PlayerService.class).setAction(Const.ACTION_PLAY_PAUSE));
                break;

        }
    }
}
