package com.multivoltage.musicat;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.melnykov.fab.FloatingActionButton;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.multivoltage.musicat.fragments.InfoFragment;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.squareup.picasso.Picasso;
import com.multivoltage.musicat.entity.LoadImageFileAsynk;
import com.multivoltage.musicat.events.EventSearch;
import com.multivoltage.musicat.events.EventTrack;
import com.multivoltage.musicat.fragments.AlbumsFragment;
import com.multivoltage.musicat.fragments.AllSongFragment;
import com.multivoltage.musicat.fragments.ArtistFragment;
import com.multivoltage.musicat.fragments.PlayListFragment;
import com.multivoltage.musicat.fragments.SlideFragment;

import java.io.File;

import de.greenrobot.event.EventBus;

public class MainActivity extends AppCompatActivity implements SlidingUpPanelLayout.PanelSlideListener {

    public static final int SETTING_REQUEST = 12;  // The request code
    private SlideFragment slideFragment;
    private SlidingUpPanelLayout slidingUpPanelLayout;
    private Toolbar toolbar;
    private SearchView searchView;
    private EventBus bus = EventBus.getDefault();
    private Drawer result;
    private ImageView imageViewHeader;
    private FloatingActionButton fabAddlaylist,fabBack;
    private SecondaryDrawerItem serverDrawerItem;
    private CoordinatorLayout coordinatorLayout;
    public static final String TAG = "musicat.maintag";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_drawer);

        bus.register(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        setSupportActionBar(toolbar);
        fabAddlaylist = (FloatingActionButton) findViewById(R.id.fabAddPlayList);
        fabAddlaylist.hide();
        fabBack = (FloatingActionButton) findViewById(R.id.fabBack);

        /* SET UP TOOLBAR */
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));

        imageViewHeader = new ImageView(this);
        imageViewHeader.setLayoutParams(new ViewGroup.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        imageViewHeader.setAdjustViewBounds(true);
        imageViewHeader.setCropToPadding(false);
        imageViewHeader.setScaleType(ImageView.ScaleType.FIT_XY);

        int colResIdSlide  = Utils.getTheme(this)==Const.THEME_DARK ? R.color.md_black_1000 : R.color.md_white_1000;
        int colResTextItem = Utils.getTheme(this)==Const.THEME_DARK ? R.color.md_white_1000 : R.color.md_black_1000;
        int colPrimary = Utils.getPrimaryColor(this);

        serverDrawerItem =  new SecondaryDrawerItem().withName("Server")
                .withTextColorRes(colResTextItem)
                .withIcon(GoogleMaterial.Icon.gmd_cloud_upload)
                .withIconColor(colPrimary)
                .withBadgeTextColor(colPrimary);

        initColors();
        result = new DrawerBuilder()
                .withActivity(this)
                .withStickyHeader(imageViewHeader)
                .withToolbar(toolbar)

                .withSliderBackgroundColorRes(colResIdSlide)
                .addDrawerItems(
                        new SecondaryDrawerItem().withName(getString(R.string.all_songs)).withTextColorRes(colResTextItem).withIcon(GoogleMaterial.Icon.gmd_list).withIconColor(colPrimary),
                        new SecondaryDrawerItem().withName("Album").withTextColorRes(colResTextItem).withIcon(GoogleMaterial.Icon.gmd_collections).withIconColor(colPrimary),
                        new SecondaryDrawerItem().withName(getString(R.string.artists)).withTextColorRes(colResTextItem).withIcon(GoogleMaterial.Icon.gmd_face).withIconColor(colPrimary),
                        new SecondaryDrawerItem().withName("Playlist").withTextColorRes(colResTextItem).withIcon(GoogleMaterial.Icon.gmd_library_music).withIconColor(colPrimary),
                        new DividerDrawerItem(),
                        serverDrawerItem,
                        new SecondaryDrawerItem().withName(getString(R.string.action_settings)).withTextColorRes(colResTextItem).withIcon(GoogleMaterial.Icon.gmd_settings).withIconColor(colPrimary),
                        new SecondaryDrawerItem().withName("Info").withTextColorRes(colResTextItem).withIcon(GoogleMaterial.Icon.gmd_info).withIconColor(colPrimary)

                ).withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(AdapterView<?> adapterView, View view, int pos, long l, IDrawerItem iDrawerItem) {

                        Fragment f = null;
                        switch (pos) {
                            case 0: // All songs
                                f = new AllSongFragment();
                                break;
                            case 1: // Album
                                f = new AlbumsFragment();
                                break;
                            case 2: // Artist
                                f = new ArtistFragment();
                                break;
                            case 3: // PlayList
                                f = new PlayListFragment();
                                break;
                            case 5: // server
                                serverDialog();
                                break;
                            case 6: // settings
                                startActivityForResult(new Intent(MainActivity.this, PreferencesActivity.class), SETTING_REQUEST);
                                break;
                            case 7: // Info
                                f = new InfoFragment();
                                break;

                        }
                        fabBack.setVisibility(View.INVISIBLE);
                        if (f != null)
                            getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, f).commit();
                        return false;
                    }

                }).build();


        slideFragment = SlideFragment.newInstance();
        getSupportFragmentManager().beginTransaction().replace(R.id.slideFragment, slideFragment).commit();
        slidingUpPanelLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_root_layout);
        slidingUpPanelLayout.setPanelSlideListener(this);

        Fragment fragAllSong = new AllSongFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,fragAllSong).commit();


        initTheme();
    }

    @Override
    public void onStart(){
        GoogleAnalytics.getInstance(this).reportActivityStart(this);
        super.onStart();
    }
    @Override
    public void onStop(){
        GoogleAnalytics.getInstance(this).reportActivityStop(this);
        super.onStop();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.action_contact :
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, "Musicat - Help");
                intent.putExtra(Intent.EXTRA_TEXT, "Hi bro... ");
                Intent mailer = Intent.createChooser(intent, null);
                intent.setType("message/rfc822");
                startActivity(mailer);
                break;

            case R.id.action_git :
                String url = "https://github.com/multivoltage/Musicat";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
                break;

            case R.id.action_rate :
                String playstore = "https://play.google.com/store/apps/details?id="+getPackageName();
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(playstore)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(playstore)));
                }

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        final MenuItem searchMenuItem;
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        searchMenuItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (TextUtils.isEmpty(s)) {
                    EventSearch eventSearch = new EventSearch("");
                    EventBus.getDefault().post(eventSearch);
                } else {
                    EventSearch eventSearch = new EventSearch(s);
                    EventBus.getDefault().post(eventSearch);
                }
                return true;
            }
        });

        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean queryTextFocused) {
                if (!queryTextFocused) {
                    searchMenuItem.collapseActionView();
                    searchView.setQuery("", false);
                }
            }
        });
        return true;
    }


    private void serverDialog(){
        boolean isSeverOnLastUsage = Utils.isSeverOnLastUsage(this);
        String ip = Utils.wifiIpAddress(this);

        if(ip==null){
            Snackbar.make(slideFragment.getView(),"Please connect to wifi",Snackbar.LENGTH_SHORT).show();
        } else {
            new MaterialDialog.Builder(this)
                    .title("Music Server")
                    .theme(Utils.getTheme(MainActivity.this) == Const.THEME_DARK ? Theme.DARK : Theme.LIGHT)
                    .cancelable(false)
                    .content(getString(R.string.available) + " " + ip + ":8080")
                    .positiveText(getString(R.string.run))
                    .negativeText(getString(R.string.stop))
                    .neutralText(getString(R.string.do_nothing))
                    .callback(new MaterialDialog.ButtonCallback() {
                        @Override
                        public void onPositive(MaterialDialog dialog) {
                            startService(new Intent(MainActivity.this, PlayerService.class).setAction(Const.ACTION_SERVER_ON));
                            super.onPositive(dialog);
                        }

                        @Override
                        public void onNegative(MaterialDialog dialog) {
                            startService(new Intent(MainActivity.this, PlayerService.class).setAction(Const.ACTION_SERVER_OFF));
                            super.onNegative(dialog);
                        }
                    }).show();
        }
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onPanelSlide(View view, float v) {
    }
    @Override
    public void onPanelCollapsed(View view) {
        slideFragment.notifyPanelCollapsed();
    }
    @Override
    public void onPanelExpanded(View view) {
        slideFragment.notifyPanelExpanded();
    }
    @Override
    public void onPanelAnchored(View view) {
    }
    @Override
    public void onPanelHidden(View view) {
    }

    public void onEvent(EventTrack event) {/* Do something */
        switch (event.getEvent()){
            case Const.CHANGED_EVENT :      refreshHeader(event.getTrack().getTrackUri());
                break;
            case Const.NEXT_EVENT :         refreshHeader(event.getTrack().getTrackUri());
                break;
            case Const.PREV_EVENT :         refreshHeader(event.getTrack().getTrackUri());
                break;
            case Const.PLAYING_STATE_EVENT :refreshHeader(event.getTrack().getTrackUri());
                break;

        }
    };

    @Override
    public void onBackPressed() {
        if(slidingUpPanelLayout.getPanelState()== SlidingUpPanelLayout.PanelState.COLLAPSED)
            super.onBackPressed();
        else
            slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
    }

    @Override
    public void onPause(){
        if(isMyServiceRunning(PlayerService.class)){
            if(Utils.showBubble(this))
                startService(new Intent(this, PlayerService.class).setAction(Const.ACTION_BUBBLE_ON));
        }
        super.onPause();

    }
    @Override
    public void onResume(){
        if(isMyServiceRunning(PlayerService.class)){
            if(Utils.showBubble(this))
                startService(new Intent(this, PlayerService.class).setAction(Const.ACTION_BUBBLE_OFF));
        }
        super.onResume();
    }

    @Override
    public void onNewIntent(Intent intent){
        slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == SETTING_REQUEST) {
            // Make sure the request was successful
            if (resultCode == PreferencesActivity.RESULT_EDIT_ASPECT_OK) {
                finish();
                startActivity(new Intent(this, MainActivity.class));
            }
        }
    }

    private void initColors(){
        int colorPrimary = Utils.getPrimaryColor(getApplicationContext());
        int colorSecondary = Utils.getSecondaryColor(getApplicationContext());

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().setStatusBarColor(Utils.getDarkerColor(colorPrimary, 0.7f));
        }
        // this method colors only acion bar (no tabs)
        toolbar.setBackgroundColor(colorPrimary);
        // this method color only the tablayout
        // color fab button
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabBack);
        fab.setColorNormal(colorSecondary);
        fab.setColorRipple(colorSecondary);
        fab.setColorPressed(Utils.getDarkerColor(colorSecondary, 0.7f));
    }

    private void initTheme(){
        int theme = Utils.getTheme(getApplicationContext());
        switch (theme){
            case Const.THEME_LIGHT :
                findViewById(R.id.frame_container).setBackgroundColor(getResources().getColor(R.color.grey_100));
                break;
            case Const.THEME_DARK :
                findViewById(R.id.frame_container).setBackgroundColor(getResources().getColor(R.color.grey_800));
                break;
        }
        fabAddlaylist.setColorNormal(Utils.getPrimaryColor(this));
        fabAddlaylist.setColorPressed(Utils.getDarkerColor(Utils.getPrimaryColor(this), 0.8f));
        fabAddlaylist.setColorRipple(Utils.getSecondaryColor(this));
    }

    private final void refreshHeader(Uri fromUri){
        
        new LoadImageFileAsynk(new File(fromUri.toString()),MainActivity.this,100){
            @Override
            protected void onPostExecute(File bitmap) {
                Picasso.with(MainActivity.this)
                        .load(bitmap)
                        .resize(512,512)
                        .placeholder(R.drawable.unknow_background)
                        .into(imageViewHeader);
            }
        }.execute();
    }

    public void openBrowser(View view){

        String url = "";

        switch (view.getId()){
            case R.id.aboutEventBus:               url = "https://github.com/greenrobot/EventBus";
                break;
            case R.id.aboutFloatinButton:          url = "https://github.com/makovkastar/FloatingActionButton";
                break;
            case R.id.aboutAsynkHttp:              url = "https://github.com/loopj/android-async-http";
                break;
            case R.id.aboutCirlceImageView:        url = "https://github.com/hdodenhof/CircleImageView";
                break;
            case R.id.aboutMaterialDialogs:        url = "https://github.com/afollestad/material-dialogs";
                break;
            case R.id.aboutMaterialDrawer:         url = "https://github.com/mikepenz/MaterialDrawer";
                break;
            case R.id.aboutMp3agic:                url = "https://github.com/mpatric/mp3agic";
                break;
            case R.id.aboutNano:                   url = "https://github.com/NanoHttpd/nanohttpd";
                break;
            case R.id.aboutObservableScrolView:    url = "https://github.com/ksoichiro/Android-ObservableScrollView";
                break;
            case R.id.aboutOkHttp:                 url = "https://github.com/square/okhttp";
                break;
            case R.id.aboutPicasso:                url = "https://github.com/square/picasso";
                break;
            case R.id.aboutSlidePanel:             url = "https://github.com/umano/AndroidSlidingUpPanel";
                break;
            case R.id.aboutSuperSLIM:              url = "https://github.com/TonicArtos/SuperSLiM";
                break;
            //LICENSE
            case R.id.aboutLicenseIcon8:           url = "https://icons8.com/license/";
                break;

        }


        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);

    }

}
