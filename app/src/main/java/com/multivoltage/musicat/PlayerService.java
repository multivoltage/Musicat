package com.multivoltage.musicat;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.RemoteControlClient;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.Mp3File;
import com.squareup.picasso.Picasso;
import com.multivoltage.musicat.custom.FloatingBubble;
import com.multivoltage.musicat.custom.OnSwipeTouchListener;
import com.multivoltage.musicat.entity.AudioFocusHelper;
import com.multivoltage.musicat.entity.BasicPlayer;
import com.multivoltage.musicat.entity.HelloServer;
import com.multivoltage.musicat.entity.LoadImageFileAsynk;
import com.multivoltage.musicat.entity.MediaButtonHelper;
import com.multivoltage.musicat.entity.MusicFocusable;
import com.multivoltage.musicat.entity.QueuePlayer;
import com.multivoltage.musicat.entity.RemoteControlClientCompat;
import com.multivoltage.musicat.entity.RemoteControlHelper;
import com.multivoltage.musicat.entity.ShakeEventListener;
import com.multivoltage.musicat.entity.Track;
import com.multivoltage.musicat.events.EventTrack;

import java.io.File;

import de.greenrobot.event.EventBus;

public class PlayerService extends Service implements MusicFocusable, BasicPlayer.OnCompleteCallBack {

    private WindowManager windowManager;
    private FloatingBubble floatingFaceBubble;
    private QueuePlayer player;
    private NotificationManager mNotificationManager;
    private Notification mNotification = null;
    private RemoteControlClientCompat mRemoteControlClientCompat;
    private ComponentName mMediaButtonReceiverComponent;
    private AudioManager mAudioManager;
    private AudioFocusHelper mAudioFocusHelper = null;
    private EventBus bus = EventBus.getDefault();
    private SensorManager mSensorManager;
    private ShakeEventListener mShakeEventListener;
    private MediaReceiver mediaReceiver;
    private HelloServer server = null;
    public PlayerService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate(){
        player = new QueuePlayer(getApplicationContext());
        player.setOnPlayAtCompleteListener(this);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mShakeEventListener = new ShakeEventListener();

        showNotification();
        updateNotification();
        mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        mAudioFocusHelper = new AudioFocusHelper(getApplicationContext(), this);
        mMediaButtonReceiverComponent = new ComponentName(this, MediaReceiver.class);

        switchOnShake(false);
        mediaReceiver = new MediaReceiver();
        registerReceiver(mediaReceiver, new IntentFilter(Intent.ACTION_HEADSET_PLUG));
        initBubble();
        showBubble(Utils.showBubble(getApplicationContext()));

        server = new HelloServer(player.getTracks());

        super.onCreate();
    }

    private void switchOnServer(boolean on){
        Log.i(MainActivity.TAG, "called switch " + on);
        if(on)
            try {
                server.start();
                //Toast.makeText(this,"Open browser to: "+Utils.wifiIpAddress(this)+ " on port 8080",Toast.LENGTH_LONG).show();
            } catch (Exception e) {
            }
        else
            try {
                server.stop();
                Toast.makeText(this,"Server Off",Toast.LENGTH_LONG).show();
            } catch (Exception e) {
            }
    }

    @Override
    public void onDestroy(){
        Log.i(MainActivity.TAG, "called on destroy service");
        player.shutDown();
        unregisterReceiver(mediaReceiver); // jack in-out
        mNotificationManager.cancel(15);
        showBubble(false);
        switchOnShake(false);

    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        String action = intent.getAction();
        Log.i(MainActivity.TAG, "onStartCommand() with action: " + action);
        boolean updateNotice = true;

        if(action!=null){
            switch (action) {
                case Const.ACTION_PLAY_TRACK:
                    playTrack((Track) intent.getParcelableExtra(Const.KEY_SELECTED_TRACK));
                    break;
                case Const.ACTION_PLAY_PAUSE:
                    playPause();
                    break;
                case Const.ACTION_NEXT:
                    playNext();
                    break;
                case Const.ACTION_PREV:
                    playPrev();
                    break;
                case Const.ACTION_REPEATING:
                    setRepeating(!isRepeatingMode());
                    break;
                case Const.ACTION_SKEEP_TO:
                    int newPos = intent.getIntExtra(Const.EXTRA_KEY_SKEEP_TO, 0);
                    skeepTo(newPos);
                    break;
                case Const.ACTION_SHAKE_ON :
                    switchOnShake(true);
                    break;
                case Const.ACTION_SHAKE_OFF :
                    switchOnShake(false);
                    break;
                case Const.ACTION_BUBBLE_ON :
                    showBubble(true);
                    break;
                case Const.ACTION_BUBBLE_OFF :
                    showBubble(false);
                    break;
                case Const.ACTION_ADD_TO_QUEUE :
                    player.addToQueue((Track) intent.getParcelableExtra(Const.KEY_ADD_QUEUE_TRACK));
                    bus.post(new EventTrack(Const.ACTION_ADD_TO_QUEUE,getClass().getSimpleName()));
                    break;
                case Const.ACTION_REQUEST_STATE_PLAYING:
                    responseEventPlayState();
                    break;
                case Const.ACTION_DISMISS: updateNotice = false;
                    stopSelf();
                    break;
                case Const.ACTION_SERVER_ON:
                    switchOnServer(true);
                    break;
                case Const.ACTION_SERVER_OFF:
                    switchOnServer(false);
                    break;
                case Const.ACTION_INCOMIN_CALL:
                    if(isPlaying())
                        playPause();

                    break;
                case "android.intent.action.HEADSET_PLUG":
                    if (intent.getIntExtra("state", -1) == 0)
                        if(isPlaying())
                            playPause(); // only pause will call

                    break;
            }

            if(updateNotice) {
                updateNotification();
                updateLauncher();
                updateBubble();
            }
        }

        new CountDownTimer(10*1000,1000){
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                //stopSelf();
            }
        }.start();

        return Service.START_NOT_STICKY;
    }

    @Override
    public void onGainedAudioFocus() {
        Toast.makeText(getApplicationContext(), "gained audio focus.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLostAudioFocus(boolean canDuck) {
        Log.i(MainActivity.TAG, "lost audio focus");
        playPause();
    }

    @Override
    public void playAtComplete() {
         bus.post(new EventTrack(Const.CHANGED_EVENT, getClass().getSimpleName())
                 .withTrack(getCurrentTrack())
                 .withCurrent(getCurrentPosition())
                 .withDuration(getDuration())
                 .withCurrentIndexList(player.getIndexOfCurrent()));
    }

    public void showNotification(){

        RemoteViews rm = new RemoteViews(getPackageName(),  R.layout.notification_layout);
        //the intent that is started when the notification is clicked (works)
        Intent ppIntent    = new Intent(this, MediaReceiver.class);
        Intent nextIntent  = new Intent(this, MediaReceiver.class);
        Intent prevIntent  = new Intent(this, MediaReceiver.class);
        Intent stopIntent  = new Intent(this, MediaReceiver.class);
        Intent dismissIntent=new Intent(this, MediaReceiver.class);

        ppIntent.setAction(Const.ACTION_PLAY_PAUSE);
        nextIntent.setAction(Const.ACTION_NEXT);
        prevIntent.setAction(Const.ACTION_PREV);
        stopIntent.setAction(Const.ACTION_STOP);
        dismissIntent.setAction(Const.ACTION_DISMISS);

        PendingIntent pendingPP        = PendingIntent.getBroadcast(getApplicationContext(), 0, ppIntent, 0);
        PendingIntent pendingNext      = PendingIntent.getBroadcast(getApplicationContext(), 0, nextIntent, 0);
        PendingIntent pendingPrev      = PendingIntent.getBroadcast(getApplicationContext(), 0, prevIntent, 0);
        PendingIntent pendingDismiss   = PendingIntent.getBroadcast(getApplicationContext(), 0, dismissIntent, 0);

        rm.setOnClickPendingIntent(R.id.imgNoticePlayPause, pendingPP);
        rm.setOnClickPendingIntent(R.id.imgNoticeNext, pendingNext);
        rm.setOnClickPendingIntent(R.id.imgNoticePrev, pendingPrev);
        rm.setOnClickPendingIntent(R.id.imgNoticeDismiss, pendingDismiss);

        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        PendingIntent pIntent = PendingIntent.getActivity(this, 10, intent, 0);

        mNotification  = new Notification.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setOngoing(true)
                .setAutoCancel(false)
                .setContent(rm)
                .setContentIntent(pIntent)
                .build();

        mNotificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        if(Utils.canShowNotice(this))
            mNotificationManager.notify(15, mNotification);
        else
            mNotificationManager.cancel(15);
    }

    public boolean isPlaying(){
        return player.isPlaying();
    }

    private void updateNotification(){
        int theme = Utils.getTheme(getApplicationContext());
        if(isPlaying())
            mNotification.contentView.setImageViewResource(R.id.imgNoticePlayPause,/*theme==Const.THEME_DARK ? R.mipmap.pause_minimal : */R.mipmap.pause_minimal_dark);
        else
            mNotification.contentView.setImageViewResource(R.id.imgNoticePlayPause,/*theme==Const.THEME_DARK ? R.mipmap.play_minimal : */R.mipmap.play_minimal_dark);

        mNotification.contentView.setTextViewText(R.id.tvTitleNotice, getCurrentTrack().getTitle());
        mNotification.contentView.setTextViewText(R.id.tvArtistNotice, getCurrentTrack().getArtist());

        // if is lollipo or newer notification backgroudn is white so text color become black
        if (Build.VERSION.SDK_INT >= 21) {
            // white background so color is white and grey
            mNotification.contentView.setTextColor(R.id.tvTitleNotice, getResources().getColor(R.color.primary_dark_material_dark));
        } else {
            mNotification.contentView.setTextColor(R.id.tvTitleNotice, getResources().getColor(android.R.color.white));
        }
        mNotification.contentView.setTextColor(R.id.tvArtistNotice, getResources().getColor(R.color.primary_dark_material_light));

        mNotification.contentView.setImageViewResource(R.id.imgNoticeCover, R.mipmap.unknow);


        new LoadImageFileAsynk(new File(getCurrentTrack().getTrackUri().toString()), getApplicationContext(), 50) {
            @Override
            protected void onPostExecute(File fileImage) {
                if (fileImage != null && fileImage.exists()) {
                    float dp = 72;// Utils.convertDpToPixel(64,getApplicationContext());
                    Picasso.with(getApplicationContext())
                            .load(fileImage)
                            .resize((int) dp, (int) dp)
                            //.transform(new RoundedTransformation(90, 0))
                            .into(mNotification.contentView, R.id.imgNoticeCover, 15, mNotification);
                }
            }
        }.execute();

        // I think Picasso call this automatic
        mNotificationManager.notify(15, mNotification);
    }

    public Track getCurrentTrack(){
        return player.getCurrentTrack();
    }

    public void playTrack(Track track){
        player.playSpecificTrack(track);
        bus.post(new EventTrack(Const.CHANGED_EVENT, getClass().getName())
                .withTrack(getCurrentTrack())
                .withCurrent(getCurrentPosition())
                .withDuration(getDuration())
                .withCurrentIndexList(player.getIndexOfCurrent()));
    }

    public void playNext(){
        player.next();
        bus.post(new EventTrack(Const.NEXT_EVENT, getClass().getName())
                .withTrack(getCurrentTrack())
                .withCurrent(getCurrentPosition())
                .withDuration(getDuration())
                .withCurrentIndexList(player.getIndexOfCurrent()));
    }

    public void playPrev(){
        player.prev();
        bus.post(new EventTrack(Const.PREV_EVENT, getClass().getName())
                .withTrack(getCurrentTrack())
                .withCurrent(getCurrentPosition())
                .withDuration(getDuration())
                .withCurrentIndexList(player.getIndexOfCurrent()));
    }

    public void playPause(){
        if(player.isPlaying()) {
            player.pause();
            bus.post(new EventTrack(Const.PAUSE_EVENT, getClass().getName())
                    .withCurrentIndexList(player.getIndexOfCurrent())
                    .withDuration(getDuration())
                    .withTrack(getCurrentTrack()));
            if(isRemoteControlClientWorking()) {
                if (mRemoteControlClientCompat != null)
                    mRemoteControlClientCompat.setPlaybackState(RemoteControlClient.PLAYSTATE_PAUSED);
            }

        } else {
            player.play();
            bus.post(new EventTrack(Const.PLAY_EVENT, getClass().getName())
                    .withCurrentIndexList(player.getIndexOfCurrent())
                    .withDuration(getDuration())
                    .withTrack(getCurrentTrack()));
            if(isRemoteControlClientWorking()){
                if(mRemoteControlClientCompat != null)
                    mRemoteControlClientCompat.setPlaybackState(RemoteControlClient.PLAYSTATE_PLAYING);
            }
        }
        //updateNotification();
    }

    private void updateLauncher(){
        mAudioManager.requestAudioFocus(new AudioManager.OnAudioFocusChangeListener() {
            @Override
            public void onAudioFocusChange(int focusChange) {

            }
        }, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        MediaButtonHelper.registerMediaButtonEventReceiverCompat(mAudioManager, mMediaButtonReceiverComponent);

        if(isRemoteControlClientWorking()) {
            if (mRemoteControlClientCompat == null) {
                Intent intent = new Intent(Intent.ACTION_MEDIA_BUTTON);
                intent.setComponent(mMediaButtonReceiverComponent);
                mRemoteControlClientCompat = new RemoteControlClientCompat(PendingIntent.getBroadcast(this /*context*/, 0 /*requestCode, ignored*/, intent /*intent*/, 0 /*flags*/));
                RemoteControlHelper.registerRemoteControlClient(mAudioManager, mRemoteControlClientCompat);
            }

            mRemoteControlClientCompat.setPlaybackState(RemoteControlClient.PLAYSTATE_PLAYING);
            mRemoteControlClientCompat.setTransportControlFlags(
                    RemoteControlClient.FLAG_KEY_MEDIA_PLAY |
                            RemoteControlClient.FLAG_KEY_MEDIA_PAUSE |
                            RemoteControlClient.FLAG_KEY_MEDIA_NEXT |
                            RemoteControlClient.FLAG_KEY_MEDIA_PREVIOUS |
                            RemoteControlClient.FLAG_KEY_MEDIA_STOP |
                            RemoteControlClient.FLAG_KEY_MEDIA_STOP);

            // I dont know this work also in lollipop. It shouldn' t work
            try {
                mRemoteControlClientCompat.editMetadata(true)
                        .putString(MediaMetadataRetriever.METADATA_KEY_ARTIST, getCurrentTrack().getArtist())
                        .putString(MediaMetadataRetriever.METADATA_KEY_TITLE, getCurrentTrack().getTitle())
                        .putLong(MediaMetadataRetriever.METADATA_KEY_DURATION, getDuration())
                        .apply();

                try {
                    if(Utils.canShowArt(getApplicationContext())){
                        Mp3File mp3File = new Mp3File(new File(getCurrentTrack().getTrackUri().toString()));
                        if(mp3File.hasId3v2Tag()){
                            ID3v2 tag = mp3File.getId3v2Tag();
                            // Log.i(EditActivity.TAG, "albumimage is " + tag.getAlbumImage() == null ? "null" : ("notnull" + " withmimetype: " + tag.getAlbumImageMimeType()));
                            BitmapFactory.Options options = new BitmapFactory.Options();
                            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                            Bitmap yourSelectedImage = BitmapFactory.decodeByteArray(tag.getAlbumImage(), 0, tag.getAlbumImage().length,options);
                            if(yourSelectedImage!=null){
                                mRemoteControlClientCompat.editMetadata(true)
                                        .putBitmap(RemoteControlClientCompat.MetadataEditorCompat.METADATA_KEY_ARTWORK,yourSelectedImage)
                                        .apply();
                            }
                        }
                    }
                } catch (Exception e) {
                    // Log.i(EditActivity.TAG,e.toString());
                }
            } catch (Exception e){
                Log.e(MainActivity.TAG,"Expception to edit meta-data on mRemoteControlClientCompat: "+e.toString());

            }

        }
        if(isMediaSessionWorking()){

        }

    }

    public void setRepeating(boolean repeating){
        player.setRepeatingMode(repeating);
    }

    public int getCurrentPosition(){
        return player.getCurrentPosition();
    }

    public int getDuration(){
        return player.getDuration();
    }

    public boolean isRepeatingMode(){
        return player.isRepeatingMode();
    }

    public boolean isShuffleMode(){ return  player.isShuffleMode(); }

    public void setShuffleMode(boolean shuffle){ player.setShuffleModeEnable(shuffle);}

    public void skeepTo(int newPos){
        player.skeepTo(newPos);
    }

    private boolean isRemoteControlClientWorking(){
        return true;
       //return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) && ((Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP));
    }
    private boolean isMediaSessionWorking(){

        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH;
    }

    public void switchOnShake(boolean on){
        if(on){
            mShakeEventListener.setOnShakeListener(new ShakeEventListener.OnShakeListener() {
                @Override
                public void onShake() {
                    playNext();
                    updateNotification();
                    updateLauncher();
                    if(Utils.showBubble(getApplicationContext()))
                        updateBubble();
                }
            });
            mSensorManager.registerListener(mShakeEventListener,mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),SensorManager.SENSOR_DELAY_UI);
        } else {
            mSensorManager.unregisterListener(mShakeEventListener);
        }
    }

    private void responseEventPlayState(){
        bus.post(new EventTrack(Const.PLAYING_STATE_EVENT, getClass().getName())
                .withTrack(getCurrentTrack())
                .withCurrent(getCurrentPosition())
                .withDuration(getDuration())
                .withCurrentIndexList(player.getIndexOfCurrent())
                .withState(isPlaying() ? EventTrack.IS_PLAY : -1)); // -1 is "NOT_PLAYING"
    }

    final WindowManager.LayoutParams myParams = new WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_PHONE,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT);

    private final void initBubble() {
        floatingFaceBubble = new FloatingBubble(this);
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        //here is all the science of params
        myParams.gravity = Gravity.TOP | Gravity.LEFT;
        myParams.x = 0;
        myParams.y = 500;
        // add a floatingfacebubble icon in window

        //windowManager.addView(floatingFaceBubble, myParams);
        try {
            floatingFaceBubble.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Utils.vibrate(getApplicationContext(), 40);
                    Log.i(MainActivity.TAG, "want to set movementListener");
                    floatingFaceBubble.setOnTouchListener(movementTouchListener);
                    return false;
                }
            });
            // gesture
            floatingFaceBubble.setOnTouchListener(new OnSwipeTouchListener(getApplicationContext()) {
                @Override
                public void onSwipeRight() {
                    startService(new Intent(getApplicationContext(), PlayerService.class).setAction(Const.ACTION_NEXT));
                }

                @Override
                public void onSwipeTop() {
                    startService(new Intent(getApplicationContext(), PlayerService.class).setAction(Const.ACTION_PLAY_PAUSE));
                }
            });

        } catch (Exception e){
            Log.i(MainActivity.TAG,e.toString());
        }

    }
    private void showBubble(boolean show){
        Log.i(MainActivity.TAG,"called showBubble "+show);
        if(show){
            try {
                windowManager.addView(floatingFaceBubble, myParams);
            } catch (Exception e){
                Log.e(MainActivity.TAG,e.toString());
            }
        } else {
            try {
                windowManager.removeView(floatingFaceBubble);
            } catch (Exception e){
                Log.e(MainActivity.TAG,e.toString());
            }
        }
    }
    private void updateBubble(){
        floatingFaceBubble.setFirstTitle(getCurrentTrack().getTitle());
        floatingFaceBubble.setSubTitle(getCurrentTrack().getArtist());
        floatingFaceBubble.setImageRes(isPlaying() ? R.mipmap.pause_minimal : R.mipmap.play_minimal);
    }

    private View.OnTouchListener movementTouchListener = new View.OnTouchListener() {

        private int initialX;
        private int initialY;
        private float initialTouchX;
        private float initialTouchY;
        private long touchStartTime = 0;
        private boolean enableMovement = false;
        boolean firstTouch = true;

        @Override
        public boolean onTouch(View v, MotionEvent event) {

            if(firstTouch){
                initialX = myParams.x;
                initialY = myParams.y;
                initialTouchX = event.getRawX();
                initialTouchY = event.getRawY();
                firstTouch = false;
            }
            switch (event.getAction()) {

                // RELESED
                case MotionEvent.ACTION_UP:
                    Utils.vibrate(getApplicationContext(), 20);
                    floatingFaceBubble.setOnTouchListener(new OnSwipeTouchListener(getApplicationContext()) {

                        @Override
                        public void onSwipeRight() {
                            startService(new Intent(getApplicationContext(), PlayerService.class).setAction(Const.ACTION_NEXT));
                        }
                        @Override
                        public void onSwipeTop() {
                            startService(new Intent(getApplicationContext(), PlayerService.class).setAction(Const.ACTION_PLAY_PAUSE));
                        }

                        @Override
                        public void onSwipeBottom() {
                            startService(new Intent(getApplicationContext(), PlayerService.class).setAction(Const.ACTION_PREV));
                        }
                    });
                    break;

                case MotionEvent.ACTION_MOVE:
                    myParams.x = initialX + (int) (event.getRawX() - initialTouchX);
                    myParams.y = initialY + (int) (event.getRawY() - initialTouchY);
                    windowManager.updateViewLayout(v, myParams);
                    break;

            }

            return false;
        }
    };


}
