package com.dephub.android.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.dephub.android.R;
import com.dephub.android.utility.Snippet;
import com.dephub.android.utility.Widget;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

public class Credits extends AppCompatActivity {
    private InterstitialAd creditsInterstitialAd;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Snippet.followNightModeInSystem();
        setContentView(R.layout.activity_credits);

        getWindow().setNavigationBarColor(getResources().getColor(R.color.black));

        Toolbar toolbar = findViewById(R.id.toolbarCredits);
        toolbar.setTitle(R.string.Credits);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        Snippet.toolbar(Credits.this, toolbar);
        setSupportActionBar(toolbar);

        Snippet.initializeInterstitialAd(Credits.this);
        AdRequest creditsAdRequest = new AdRequest.Builder().build();

        Widget.showInterstitialAd(this, "ca-app-pub-3037529522611130/3494136300", creditsAdRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                Credits.super.onBackPressed();
            }

            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                creditsInterstitialAd = interstitialAd;

                interstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        Credits.super.onBackPressed();
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                        Credits.super.onBackPressed();
                    }
                });
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (creditsInterstitialAd != null) {
            creditsInterstitialAd.show(Credits.this);
        } else {
            super.onBackPressed();
        }
    }
}