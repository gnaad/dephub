package com.dephub.android.settings;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.dephub.android.R;
import com.dephub.android.constant.ApplicationConstant;
import com.dephub.android.utility.Snippet;

public class OpenSource extends AppCompatActivity {

    WebView webView;

    @SuppressLint({"SetJavaScriptEnabled", "ResourceAsColor"})
    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Snippet.followNightModeInSystem();
        Snippet.layoutInDisplayCutoutMode(OpenSource.this);
        Snippet.darkTheme(OpenSource.this, getApplicationContext());
        setContentView(R.layout.activity_open_source);

        getWindow().setNavigationBarColor(getResources().getColor(R.color.black));

        Toolbar toolbar = findViewById(R.id.toolbar_open_source);
        toolbar.setTitle(ApplicationConstant.OPEN_SOURCE_LICENSES);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        Snippet.toolbar(OpenSource.this, toolbar);
        setSupportActionBar(toolbar);

        webView = findViewById(R.id.os_web);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);

        if (Build.VERSION.SDK_INT >= 19) {
            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else {
            webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

        int nightModeFlags = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
            webView.getSettings().setForceDark(WebSettings.FORCE_DARK_ON);
        }

        webView.setLongClickable(true);
        webView.setOnLongClickListener(v -> true);

        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.loadUrl("file:///android_asset/license.html");
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