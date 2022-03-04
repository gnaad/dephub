package com.dephub.android.activity;

import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.dephub.android.R;

public class Favorite extends AppCompatActivity {
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        recyclerView = findViewById(R.id.favoriterv);

        Toolbar toolbar = findViewById(R.id.toolbarfav);
        toolbar.setTitle("My Favorites");
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