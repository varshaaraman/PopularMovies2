package com.example.codelabs.moviestage;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.example.codelabs.moviestage.Utils.OnSwipeTouchListener;

public class StarterActivity extends AppCompatActivity {
    private String OFFLINE_PREFERRANCE_KEY;
    static boolean offlinePref;
    Intent offlineIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starter);
        CoordinatorLayout starterLayout = (CoordinatorLayout)findViewById(R.id.layout_starter);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_settings);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(StarterActivity.this, SettingsActivity.class);
                startActivity(intent);




            }
        });
        starterLayout.setOnTouchListener(new OnSwipeTouchListener(StarterActivity.this) {
            public void onSwipeLeft() {
                Toast.makeText(StarterActivity.this, "left", Toast.LENGTH_SHORT).show();
                OFFLINE_PREFERRANCE_KEY =  getResources().getString(R.string.key_pref_offline_mode);
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(StarterActivity.this);
                offlinePref = preferences.getBoolean(OFFLINE_PREFERRANCE_KEY,false);
                if(offlinePref) {
                    offlineIntent = new Intent(StarterActivity.this, MainActivityOffline.class);
                    offlineIntent.setFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
                }
                else {
                    offlineIntent = new Intent(StarterActivity.this, MainActivity.class);
                    offlineIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                }
                startActivity(offlineIntent);
                StarterActivity.this.finish();

            }


        });

    }




    }
