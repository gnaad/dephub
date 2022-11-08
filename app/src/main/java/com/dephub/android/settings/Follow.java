package com.dephub.android.settings;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.dephub.android.R;
import com.dephub.android.common.Snippet;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Follow extends AppCompatActivity {
    String[] ListViewTitle = new String[]{
            "Instagram"};
    int[] images = new int[]{
            R.drawable.ic_instagram};
    int[] external = new int[]{
            R.drawable.ic_external};

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Snippet.followNightModeInSystem();
        setContentView(R.layout.activity_settings_follow);

        Snippet.initializeInterstitialAd(Follow.this);

        AdView followAdView = findViewById(R.id.adFollow);
        AdRequest adRequest = new AdRequest.Builder().build();
        followAdView.loadAd(adRequest);

        followAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError adError) {
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

        getWindow().setNavigationBarColor(getResources().getColor(R.color.black));

        Toolbar toolbar = findViewById(R.id.toolbarFollow);
        toolbar.setTitle("Follow us on");
        toolbar.setNavigationIcon(R.drawable.ic_back);
        Snippet.toolbar(Follow.this, toolbar);
        setSupportActionBar(toolbar);

        List<HashMap<String, String>> hashMapArrayList = new ArrayList<HashMap<String, String>>();
        for (int x = 0; x <= 0; x++) {
            HashMap<String, String> hashMap = new HashMap<String, String>();
            hashMap.put("ListTitle", ListViewTitle[x]);
            hashMap.put("ListImages", Integer.toString(images[x]));
            hashMap.put("ListImagesExternal", Integer.toString(external[x]));
            hashMapArrayList.add(hashMap);
        }
        String[] from = {
                "ListTitle", "ListImages", "ListImagesExternal"
        };
        int[] to = {
                R.id.listview_text, R.id.listviewimage, R.id.listviewexternal
        };

        SimpleAdapter simpleAdapter = new SimpleAdapter(getBaseContext(), hashMapArrayList, R.layout.listview_items_with_external_no_desc, from, to);
        ListView listView = findViewById(R.id.favoriteListview);
        listView.setDivider(null);
        listView.setDividerHeight(1);
        listView.setAdapter(simpleAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/dephubapp"));
                    startActivityForResult(intent, 0);
                }
            }
        });
    }
}