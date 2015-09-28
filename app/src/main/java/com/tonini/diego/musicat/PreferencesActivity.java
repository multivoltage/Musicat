package com.tonini.diego.musicat;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import com.tonini.diego.musicat.fragments.MyPreferencesFragment;

public class PreferencesActivity extends AppCompatActivity implements MyPreferencesFragment.OnAspectChaned{

    private Toolbar toolbar;
    public static final int RESULT_EDIT_ASPECT_OK = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction().replace(R.id.content_frame, new MyPreferencesFragment()).commit();
        }

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Settings");
        setSupportActionBar(toolbar);
        // this method show home button in toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        initColors();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy(){
        setResult(RESULT_OK);
        super.onDestroy();
    }

    private void initColors(){
        int colorPrimary = Utils.getPrimaryColor(getApplicationContext());

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().setStatusBarColor(Utils.getDarkerColor(colorPrimary, 0.7f));
        }
        // this method colors only acion bar (no tabs)
        toolbar.setBackgroundColor(colorPrimary);
    }


    @Override
    public void notifyPrimaryColorSelected() {
        initColors();
        setResult(RESULT_EDIT_ASPECT_OK);
    }
    @Override
    public void notifySecondaryColorSelected() {
        setResult(RESULT_EDIT_ASPECT_OK);
    }

    @Override
    public void notifyThemeChanged() {


        setResult(RESULT_EDIT_ASPECT_OK);
    }
}
