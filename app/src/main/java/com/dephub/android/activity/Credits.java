package com.dephub.android.activity;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import com.dephub.android.R;

public class Credits extends AppCompatActivity {

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);

        setContentView(R.layout.activity_credits);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow( ).setNavigationBarColor(getResources( ).getColor(R.color.black));
        }

        Toolbar toolbar = findViewById(R.id.toolbarcredits);
        toolbar.setTitle("Credits");
        toolbar.setNavigationIcon(R.drawable.ic_back);
        int nightModeFlags = getResources( ).getConfiguration( ).uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
            int white = Color.parseColor("#ffffff");
            toolbar.setTitleTextColor(white);
        } else {
            int black = Color.parseColor("#000000");
            toolbar.setTitleTextColor(black);
        }
        setSupportActionBar(toolbar);
    }
}