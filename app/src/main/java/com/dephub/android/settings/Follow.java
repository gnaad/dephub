package com.dephub.android.settings;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import com.dephub.android.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Follow extends AppCompatActivity {
    String[] Listviewtitle = new String[]{
            "Facebook Groups",
            "Instagram"};
    int[] images = new int[]{
            R.drawable.ic_facebook,
            R.drawable.ic_instagram};
    int[] external = new int[]{
            R.drawable.ic_external,
            R.drawable.ic_external};
    private AdView mAdView;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);

        setContentView(R.layout.activity_settings_follow);

        //

        MobileAds.initialize(this,new OnInitializationCompleteListener( ) {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.adfollow);
        AdRequest adRequest = new AdRequest.Builder( ).build( );
        mAdView.loadAd(adRequest);

        mAdView.setAdListener(new AdListener( ) {
            @Override
            public void onAdLoaded() {
            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
            }

            @Override
            public void onAdOpened() {
            }

            @Override
            public void onAdClicked() {
            }

            @Override
            public void onAdClosed() {
            }
        });

        //

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow( ).setNavigationBarColor(getResources( ).getColor(R.color.black));
        }

        Toolbar toolbar = findViewById(R.id.toolbarfollow);
        toolbar.setTitle("Follow us on");
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

        List<HashMap<String, String>> alist = new ArrayList<HashMap<String, String>>( );
        for (int x = 0; x <= 1; x++) {
            HashMap<String, String> hm = new HashMap<String, String>( );
            hm.put("ListTitle",Listviewtitle[x]);
            hm.put("Listimages",Integer.toString(images[x]));
            hm.put("Listimagesexternal",Integer.toString(external[x]));
            alist.add(hm);
        }
        String[] from = {
                "ListTitle","Listimages","Listimagesexternal"
        };
        int[] to = {
                R.id.listview_text,R.id.listviewimage,R.id.listviewexternal
        };

        SimpleAdapter simpleAdapter = new SimpleAdapter(getBaseContext( ),alist,R.layout.listview_items_with_external_no_desc,from,to);
        ListView listView = findViewById(R.id.listview2);
        listView.setDivider(null);
        listView.setDividerHeight(1);
        listView.setAdapter(simpleAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener( ) {

            @Override
            public void onItemClick(AdapterView<?> parent,View view,int position,long id) {
                if (position == 0) {
                    String facebookUrl = "https://www.facebook.com/groups/dephubapp";
                    try {
                        int versionCode = getPackageManager( ).getPackageInfo("com.facebook.katana",0).versionCode;
                        if (versionCode >= 3002850) {
                            Uri uri = Uri.parse("fb://facewebmodal/f?href=" + facebookUrl);
                            startActivity(new Intent(Intent.ACTION_VIEW,uri));
                        } else {
                            Uri uri = Uri.parse("fb://page/groups/dephubapp");
                            startActivity(new Intent(Intent.ACTION_VIEW,uri));
                        }
                    } catch (PackageManager.NameNotFoundException e) {
                        startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse(facebookUrl)));
                    }
                }
                if (position == 1) {
                    Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse("https://www.instagram.com/dephubapp"));
                    startActivityForResult(intent,1);
                }
            }
        });
    }
}