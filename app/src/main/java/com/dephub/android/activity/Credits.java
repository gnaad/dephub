package com.dephub.android.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.dephub.android.R;
import com.dephub.android.common.Snippet;

public class Credits extends AppCompatActivity {

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Snippet.followNightModeInSystem();
        setContentView(R.layout.activity_credits);

        getWindow().setNavigationBarColor(getResources().getColor(R.color.black));

        Toolbar toolbar = findViewById(R.id.toolbarCredits);
        toolbar.setTitle("Credits");
        toolbar.setNavigationIcon(R.drawable.ic_back);
        Snippet.toolbar(Credits.this, toolbar);
        setSupportActionBar(toolbar);
    }
}