package com.dephub.android.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.dephub.android.R;
import com.github.aakira.compoundicontextview.CompoundIconTextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class News extends AppCompatActivity {
    public static final String url = "https://gnanendraprasadp.github.io/DepHub-Web/news";
    ProgressDialog progressDialog;
    Activity activity;
    CompoundIconTextView compoundIconTextView;
    String button, container, google, helper, layout, legacy, others, text, widget, date;
    int one, two, three, four, five, six, seven, eight, nine, total;
    WebView webview;

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);

        setContentView(R.layout.activity_news);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow( ).setNavigationBarColor(getResources( ).getColor(R.color.black));
        }

        Toolbar toolbar = findViewById(R.id.toolbarnews);
        toolbar.setTitle("News");
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

        Drawable drawable = toolbar.getOverflowIcon( );
        //noinspection ConstantConditions
        DrawableCompat.setTint(drawable.mutate( ),getResources( ).getColor(R.color.toolbaricon));
        toolbar.setOverflowIcon(drawable);

        compoundIconTextView = findViewById(R.id.nointernetnews);

        DatabaseReference ref;
        ref = FirebaseDatabase.getInstance( ).getReference( ).child("Dependency Count");
        ref.addValueEventListener(new ValueEventListener( ) {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //noinspection ConstantConditions
                button = snapshot.child("Button").getValue( ).toString( );
                //noinspection ConstantConditions
                container = snapshot.child("Container").getValue( ).toString( );
                //noinspection ConstantConditions
                google = snapshot.child("Google").getValue( ).toString( );
                //noinspection ConstantConditions
                helper = snapshot.child("Helper").getValue( ).toString( );
                //noinspection ConstantConditions
                layout = snapshot.child("Layout").getValue( ).toString( );
                //noinspection ConstantConditions
                legacy = snapshot.child("Legacy").getValue( ).toString( );
                //noinspection ConstantConditions
                others = snapshot.child("Others").getValue( ).toString( );
                //noinspection ConstantConditions
                text = snapshot.child("Text").getValue( ).toString( );
                //noinspection ConstantConditions
                widget = snapshot.child("Widget").getValue( ).toString( );

                date = snapshot.child("Date").getValue( ).toString( );

                one = Integer.valueOf(button);
                two = Integer.valueOf(container);
                three = Integer.valueOf(google);
                four = Integer.valueOf(helper);
                five = Integer.valueOf(layout);
                six = Integer.valueOf(legacy);
                seven = Integer.valueOf(others);
                eight = Integer.valueOf(text);
                nine = Integer.valueOf(widget);

                total = one + two + three + four + five + six + seven + eight + nine;

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext( ),error.toString( ),Toast.LENGTH_SHORT).show( );
            }
        });

        activity = this;

        progressDialog = new ProgressDialog(News.this,R.style.CustomAlertDialog);
        progressDialog.setMessage("Loading");
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.show( );

        webview = findViewById(R.id.news);
        webview.getSettings( ).setJavaScriptEnabled(true);
        webview.setLongClickable(false);
        webview.getSettings( ).setLoadWithOverviewMode(true);
        webview.getSettings( ).setCacheMode(WebSettings.LOAD_NO_CACHE);

        if (Build.VERSION.SDK_INT >= 19) {
            webview.setLayerType(View.LAYER_TYPE_HARDWARE,null);
        } else {
            webview.setLayerType(View.LAYER_TYPE_SOFTWARE,null);
        }

        if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
            webview.getSettings( ).setForceDark(WebSettings.FORCE_DARK_ON);
        }

        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.swipenewsrefresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.blacktowhite);

        webview.setWebViewClient(new WebViewClient( ) {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view,String url) {
                progressDialog.show( );
                return false;
            }

            @Override
            public void onReceivedError(WebView view,int errorCode,String description,String failingUrl) {
                swipeRefreshLayout.setVisibility(View.INVISIBLE);
                progressDialog.cancel( );
                webview.setVisibility(View.INVISIBLE);
                compoundIconTextView.setVisibility(View.VISIBLE);
                webview.setBackgroundColor(Color.WHITE);
                swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener( ) {
                    @Override
                    public void onRefresh() {
                        new Handler( ).postDelayed(new Runnable( ) {
                            @Override
                            public void run() {
                                compoundIconTextView.setVisibility(View.INVISIBLE);
                                webview.setVisibility(View.VISIBLE);
                                swipeRefreshLayout.setRefreshing(false);
                                webview.reload( );
                            }
                        },3000);
                    }
                });
            }

            @Override
            public void onPageFinished(WebView view,String url) {
                progressDialog.cancel( );
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener( ) {
            @Override
            public void onRefresh() {
                new Handler( ).postDelayed(new Runnable( ) {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                        webview.reload( );
                    }
                },3000);
            }
        });
        webview.loadUrl(url);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater( );
        menuInflater.inflate(R.menu.news_settings,menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId( )) {
            case R.id.count:

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(News.this,R.style.CustomAlertDialog);

                alertDialogBuilder.setCancelable(true);
                alertDialogBuilder.setMessage("Dependency Count\n\nText : " + text + "\nButton : " + button + "\nWidget : " + widget + "\nLayout : " + layout + "" +
                        "\nContainer : " + container + "\nHelper : " + helper + "\nGoogle : " + google + "\nLegacy : " + legacy + "\nOthers : " + others + "\n\nTotal : " + total + " Dependencies\n\nLast Modified : " + date);
                alertDialogBuilder.setPositiveButton("Close",new DialogInterface.OnClickListener( ) {
                    public void onClick(DialogInterface dialog,int id) {
                        dialog.dismiss( );
                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create( );
                alertDialog.show( );
                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources( ).getColor(R.color.colorAccent));
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources( ).getColor(R.color.colorAccent));
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        webview.clearCache(true);
        super.onBackPressed( );
    }

    @Override
    public boolean onKeyDown(int keyCode,KeyEvent event) {
        if (event.getAction( ) == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    WebView webview = findViewById(R.id.news);
                    if (webview.canGoBack( )) {
                        webview.goBack( );
                    } else {
                        finish( );
                    }
                    return true;
            }
        }
        return super.onKeyDown(keyCode,event);
    }
}