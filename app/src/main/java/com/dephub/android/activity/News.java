package com.dephub.android.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.dephub.android.R;
import com.dephub.android.common.Component;
import com.dephub.android.common.Snippet;
import com.github.aakira.compoundicontextview.CompoundIconTextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class News extends AppCompatActivity {
    public static final String newsURL = "https://gnaad.github.io/dephub/news";
    ProgressDialog progressDialog;
    Activity activity;
    CompoundIconTextView compoundIconTextView;
    String button_db_value, container_db_value, google_db_value, helper_db_value, layout_db_value, legacy_db_value, others_db_value, text_db_value, widget_db_value, date_db_value;
    int button, container, google, helper, layout, legacy, others, text, widget, total;
    WebView webview;

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @SuppressLint({"ResourceAsColor", "SetJavaScriptEnabled"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Snippet.followNightModeInSystem();
        setContentView(R.layout.activity_news);

        getWindow().setNavigationBarColor(getResources().getColor(R.color.black));

        Toolbar toolbar = findViewById(R.id.toolbarNews);
        toolbar.setTitle("News");
        toolbar.setNavigationIcon(R.drawable.ic_back);
        Snippet.toolbar(News.this, toolbar);
        setSupportActionBar(toolbar);

        Drawable drawable = toolbar.getOverflowIcon();
        assert drawable != null;
        DrawableCompat.setTint(drawable.mutate(), getResources().getColor(R.color.toolbaricon));
        toolbar.setOverflowIcon(drawable);

        compoundIconTextView = findViewById(R.id.noInternetNews);

        DatabaseReference ref;
        ref = FirebaseDatabase.getInstance().getReference().child("Dependency Count");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                button_db_value = snapshot.child("Button").getValue().toString();
                container_db_value = snapshot.child("Container").getValue().toString();
                google_db_value = snapshot.child("Google").getValue().toString();
                helper_db_value = snapshot.child("Helper").getValue().toString();
                layout_db_value = snapshot.child("Layout").getValue().toString();
                legacy_db_value = snapshot.child("Legacy").getValue().toString();
                others_db_value = snapshot.child("Others").getValue().toString();
                text_db_value = snapshot.child("Text").getValue().toString();
                widget_db_value = snapshot.child("Widget").getValue().toString();
                date_db_value = snapshot.child("Date").getValue().toString();

                button = Integer.parseInt(button_db_value);
                container = Integer.parseInt(container_db_value);
                google = Integer.parseInt(google_db_value);
                helper = Integer.parseInt(helper_db_value);
                layout = Integer.parseInt(layout_db_value);
                legacy = Integer.parseInt(legacy_db_value);
                others = Integer.parseInt(others_db_value);
                text = Integer.parseInt(text_db_value);
                widget = Integer.parseInt(widget_db_value);

                total = button + container + google + helper + layout + legacy + others + text + widget;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Component.Toast(News.this, error.toString());
            }
        });

        activity = this;

        progressDialog = new ProgressDialog(News.this, R.style.CustomAlertDialog);
        progressDialog.setMessage("Loading");
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.show();

        webview = findViewById(R.id.webviewNews);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.setLongClickable(false);
        webview.getSettings().setLoadWithOverviewMode(true);
        webview.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);

        if (Build.VERSION.SDK_INT >= 19) {
            webview.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else {
            webview.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

        int nightModeFlags = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
            webview.getSettings().setForceDark(WebSettings.FORCE_DARK_ON);
        }

        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.swipeNewsRefresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.blacktowhite);

        webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                progressDialog.show();
                return false;
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                swipeRefreshLayout.setVisibility(View.INVISIBLE);
                progressDialog.cancel();
                webview.setVisibility(View.INVISIBLE);
                compoundIconTextView.setVisibility(View.VISIBLE);
                webview.setBackgroundColor(Color.WHITE);
                swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                compoundIconTextView.setVisibility(View.INVISIBLE);
                                webview.setVisibility(View.VISIBLE);
                                swipeRefreshLayout.setRefreshing(false);
                                webview.reload();
                            }
                        }, 3000);
                    }
                });
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                progressDialog.cancel();
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                        webview.reload();
                    }
                }, 3000);
            }
        });
        webview.loadUrl(newsURL);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.news_settings, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.count) {
            Component.alertDialog(
                    News.this,
                    true,
                    "Dependency Count\n\nText : " + text_db_value + "\nButton : " + button_db_value + "\nWidget : " + widget_db_value + "\nLayout : " + layout_db_value + "" +
                            "\nContainer : " + container_db_value + "\nHelper : " + helper_db_value + "\nGoogle : " + google_db_value + "\nLegacy : " + legacy_db_value + "\nOthers : " + others_db_value + "\n\nTotal : " + total + " Dependencies\n\nLast Modified : " + date_db_value,
                    "Close",
                    null,
                    (dialog, which) -> {
                        dialog.dismiss();
                    }, null
            );
        } else {
            return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        if (webview.canGoBack()) {
            webview.goBack();
        } else {
            webview.clearCache(true);
            super.onBackPressed();
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        if (webview.canGoBack()) {
            webview.goBack();
        } else {
            webview.clearCache(true);
            super.onBackPressed();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                WebView webview = findViewById(R.id.webviewNews);
                if (webview.canGoBack()) {
                    webview.goBack();
                } else {
                    finish();
                }
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}