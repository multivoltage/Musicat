package com.tonini.diego.musicat;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.melnykov.fab.FloatingActionButton;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.typeface.FontAwesome;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.util.DrawerImageLoader;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.squareup.picasso.Picasso;
import com.tonini.diego.musicat.entity.RotatePageTransformer;
import com.tonini.diego.musicat.events.EventSearch;
import com.tonini.diego.musicat.events.EventTabSelected;
import com.tonini.diego.musicat.fragments.AlbumsFragment;
import com.tonini.diego.musicat.fragments.AllSongFragment;
import com.tonini.diego.musicat.fragments.ArtistFragment;
import com.tonini.diego.musicat.fragments.PlayListFragment;
import com.tonini.diego.musicat.fragments.SlideFragment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

public class MainActivity extends AppCompatActivity implements SlidingUpPanelLayout.PanelSlideListener /*TabLayout.OnTabSelectedListener*/{

    static final int SETTING_REQUEST = 12;  // The request code
    public static final String TAG = "fucking.TAG";
    //private ViewPager pager;
    // private MainPagerAdapter mAdapter;
    private EventBus bus = EventBus.getDefault();
    private SearchView searchView;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private SlideFragment slideFragment;
    private SlidingUpPanelLayout slidingUpPanelLayout;
    private FloatingActionButton fabAddlaylist;
    //private List<Fragment> fragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_drawer);

        /*if (!isMyServiceRunning(PlayerService.class)) {
            startService(new Intent(this, PlayerService.class));
        }*/

        initView();
        // SET-UP COLOR
        // this method color the notification bar (which muts be +200 dark)
        //initColors();
        //initTheme();

        GoogleAnalytics.getInstance(MainActivity.this).reportActivityStart(this);

        ImageView i = new ImageView(this);
        i.setImageResource(R.mipmap.ic_launcher);
        Drawer result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withHeader(i)
                .addDrawerItems(

                        new DividerDrawerItem(),
                        new SecondaryDrawerItem().withName("Album"),
                        new SecondaryDrawerItem().withName("Artist"),
                        new SecondaryDrawerItem().withName("Playlist")
                ).withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(AdapterView<?> adapterView, View view, int pos, long l, IDrawerItem iDrawerItem) {
                        switch (pos){
                            case 0: // All songs
                                Fragment fsong = AllSongFragment.instantiate(MainActivity.this,"allsong");
                                getSupportFragmentManager().beginTransaction().replace(R.id.content_frame,fsong).commit();
                                break;
                            case 2: // Album
                                Fragment falbum = AllSongFragment.instantiate(MainActivity.this,"allsong");
                                getSupportFragmentManager().beginTransaction().replace(R.id.content_frame,falbum).commit();
                                break;
                            case 3: // Artist
                                break;
                            case 4: // PlayList
                                break;

                        }
                        return false;
                    }

                }).build();

       /* if(Utils.newVersion(this)){
            new MaterialDialog.Builder(this)
                    .title("New version")
                    .content("- Added web server player, see settings\n\n-Now bubble became hidden when user stay in this app\n\n-Add github url and help me\n\n- Fixed some text\n\n- Fixed not update theme\n\n- Correct clear grey text color on white background")
                    .theme(Theme.LIGHT)
                    .cancelable(false)
                     .positiveText("Go")
                    .show();
        }
        getSharedPreferences(Const.MY_PREFERENCES,MODE_PRIVATE).edit().putBoolean(Const.KEY_SHOW_NEWS,false).commit();
        */
    }

    @Override
    public void onStart(){
        super.onStart();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        //GoogleAnalytics.getInstance(MainActivity.this).reportActivityStop(this);
    }

    private void initView() {
        /* CREATE LISTfRAGMENT */
    /*    fragments = new ArrayList<>();
        fragments.add(new AllSongFragment());
        fragments.add(new AlbumsFragment());
        fragments.add(new ArtistFragment());
        fragments.add(new PlayListFragment());
     */
        fabAddlaylist = (FloatingActionButton) findViewById(R.id.fabAddPlayList);
        fabAddlaylist.hide();

        /* SET UP TOOLBAR */
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));

        // SET UP PAGER (NOW ARE EMPTY VIEW)
     /*   pager = (ViewPager) findViewById(R.id.viewPager);
        mAdapter = new MainPagerAdapter(getSupportFragmentManager(), fragments);
        pager.setAdapter(mAdapter);
        pager.setPageTransformer(true, new RotatePageTransformer());
        pager.setOffscreenPageLimit(4);
    */
        /* SET UP TABLAYOUT */
    /*    tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(pager);
        tabLayout.setOnTabSelectedListener(this);
    */
        /* INSERT SLIDE FRAGMENT (EMPTY VIEW LIKE BEFORE) */
     /*   slideFragment = SlideFragment.newInstance();
        getSupportFragmentManager().beginTransaction().replace(R.id.slideFragment, slideFragment).commit();
        slidingUpPanelLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_root_layout);
        slidingUpPanelLayout.setPanelSlideListener(this);

*/
    }

    private void initTheme(){
        int theme = Utils.getTheme(getApplicationContext());
        switch (theme){
            case Const.THEME_LIGHT :
                //pager.setBackgroundColor(getResources().getColor(android.R.color.white));
                break;
            case Const.THEME_DARK :
                //pager.setBackgroundColor(getResources().getColor(R.color.grey_700));
                break;
        }
        fabAddlaylist.setColorNormal(Utils.getPrimaryColor(this));
        fabAddlaylist.setColorPressed(Utils.getDarkerColor(Utils.getPrimaryColor(this), 0.8f));
        fabAddlaylist.setColorRipple(Utils.getSecondaryColor(this));

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
        tabLayout.setBackgroundColor(colorPrimary);
        // color fab button
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabBack);
        fab.setColorNormal(colorSecondary);
        fab.setColorRipple(colorSecondary);
        fab.setColorPressed(Utils.getDarkerColor(colorSecondary, 0.7f));
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
                    bus.post(eventSearch);
                } else {
                    EventSearch eventSearch = new EventSearch(s);
                    bus.post(eventSearch);
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


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.action_setting :
                startActivityForResult(new Intent(MainActivity.this, PreferencesActivity.class), SETTING_REQUEST);
                break;
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
        }
        return super.onOptionsItemSelected(item);
    }

    /*@Override
    public void onTabSelected(TabLayout.Tab tab) {

    //    pager.setCurrentItem(tab.getPosition());
      //  toolbar.setTitle(mAdapter.getPageTitle(pager.getCurrentItem()));
      //  EventBus.getDefault().post(new EventTabSelected(pager.getCurrentItem()));
    /*    if(pager.getCurrentItem()==0){
            int visibility = findViewById(R.id.fabBack).getVisibility();
            if(visibility==View.VISIBLE)
                findViewById(R.id.fabBack).setVisibility(View.INVISIBLE);
        }
        if(pager.getCurrentItem()==3){
            fabAddlaylist.show(true);
        } else {
            fabAddlaylist.hide(true);
        }

    }*/

  /*  @Override
    public void onTabUnselected(TabLayout.Tab tab) {}
    @Override
    public void onTabReselected(TabLayout.Tab tab) {}*/

    @Override
    public void onPanelSlide(View view, float v) {
    }

    @Override
    public void onPanelCollapsed(View view) {
        //slideFragment.notifyPanelCollapsed();

    }
    @Override
    public void onPanelExpanded(View view) {
        //slideFragment.notifyPanelExpanded();
    }
    @Override
    public void onPanelAnchored(View view) {}
    @Override
    public void onPanelHidden(View view) {}

    @Override
    public void onBackPressed() {
        /*if(slidingUpPanelLayout.getPanelState()== SlidingUpPanelLayout.PanelState.COLLAPSED){
            super.onBackPressed();
        } else {
            slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        }*/
    }

    @Override
    public void onPause(){
        /*if(isMyServiceRunning(PlayerService.class)){
            if(Utils.showBubble(this))
                startService(new Intent(this, PlayerService.class).setAction(Const.ACTION_BUBBLE_ON));
        }*/
        super.onPause();

    }
    @Override
    public void onResume(){
       /* if(isMyServiceRunning(PlayerService.class)){
            if(Utils.showBubble(this))
                startService(new Intent(this, PlayerService.class).setAction(Const.ACTION_BUBBLE_OFF));
        }*/
        super.onResume();
    }

    @Override
    public void onNewIntent(Intent intent){
        //slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.i(MainActivity.TAG, "result catch :"+resultCode);
        // Check which request we're responding to
        if (requestCode == SETTING_REQUEST) {
            // Make sure the request was successful
            if (resultCode == PreferencesActivity.RESULT_EDIT_ASPECT_OK) {
                finish();
                startActivity(new Intent(this, MainActivity.class));

            }
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
}
