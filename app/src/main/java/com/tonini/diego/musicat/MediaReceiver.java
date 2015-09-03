package com.tonini.diego.musicat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;


import de.greenrobot.event.EventBus;

/**
 * Created by Diego on 22/05/2015.
 */
public class MediaReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();
        Log.i(MainActivity.TAG, "action received normal: " + action);

        if(action.equals(Intent.ACTION_MEDIA_BUTTON)){
            KeyEvent keyEvent = (KeyEvent) intent.getExtras().get(Intent.EXTRA_KEY_EVENT);

            if (keyEvent.getAction() != KeyEvent.ACTION_DOWN)
                return;

            switch (keyEvent.getKeyCode()) {

                case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
                    context.startService(new Intent(context,PlayerService.class).setAction(Const.ACTION_PLAY_PAUSE));
                    break;
                case KeyEvent.KEYCODE_MEDIA_PLAY:
                    context.startService(new Intent(context,PlayerService.class).setAction(Const.ACTION_PLAY_PAUSE));
                    break;
                case KeyEvent.KEYCODE_MEDIA_PAUSE:
                    context.startService(new Intent(context,PlayerService.class).setAction(Const.ACTION_PLAY_PAUSE));
                    break;
                case KeyEvent.KEYCODE_MEDIA_NEXT:
                    context.startService(new Intent(context,PlayerService.class).setAction(Const.ACTION_NEXT));
                    break;
                case KeyEvent.KEYCODE_MEDIA_PREVIOUS:
                    context.startService(new Intent(context,PlayerService.class).setAction(Const.ACTION_PREV));
                    break;
                case KeyEvent.KEYCODE_MEDIA_STOP:
                    context.startService(new Intent(context,PlayerService.class).setAction(Const.ACTION_DISMISS));
                    break;
            }
        } else {
            Intent i = new Intent(context, PlayerService.class).setAction(intent.getAction()).putExtra("state", intent.getIntExtra("state", -1));
            context.startService(i);
        }
    }
}
