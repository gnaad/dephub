package com.dephub.android.settings;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.dephub.android.R;
import com.dephub.android.constant.ApplicationConstant;
import com.dephub.android.utility.Snippet;

public class Credits extends AppCompatActivity {

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Snippet.followNightModeInSystem();
        Snippet.layoutInDisplayCutoutMode(Credits.this);
        Snippet.darkTheme(Credits.this, getApplicationContext());
        setContentView(R.layout.activity_credits);

        getWindow().setNavigationBarColor(getResources().getColor(R.color.black));

        Toolbar toolbar = findViewById(R.id.toolbar_credits);
        toolbar.setTitle(ApplicationConstant.CREDITS);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        Snippet.toolbar(Credits.this, toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp() {
        super.onBackPressed();
        return false;
    }
}