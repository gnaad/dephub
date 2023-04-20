package com.dephub.android.settings;

import android.annotation.SuppressLint;
import android.content.Context;
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

import com.dephub.android.BuildConfig;
import com.dephub.android.R;
import com.dephub.android.activity.Credits;
import com.dephub.android.activity.OpenSource;
import com.dephub.android.activity.WriteFeedback;
import com.dephub.android.utility.Snippet;
import com.dephub.android.utility.Widget;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Help extends AppCompatActivity {
    private InterstitialAd HelpInterstitialAd;
    public static final String pp = "https://gnanendraprasadp.github.io/dephub/privacypolicy";
    public static final String tos = "https://gnanendraprasadp.github.io/dephub/termsofservice";
    String[] ListViewTitle = new String[]{
            "Terms of Service",
            "Privacy Policy",
            "Write Feedback",
            "Open Source Licenses",
            "Credits",
            "Clear Cache",
            "Rate this App",
            "App info"};
    int[] images = new int[]{
            R.drawable.ic_tos,
            R.drawable.ic_pp,
            R.drawable.ic_feedback,
            R.drawable.ic_opensource,
            R.drawable.ic_credit,
            R.drawable.ic_broom,
            R.drawable.ic_rate,
            R.drawable.ic_info};
    int[] external = new int[]{
            R.drawable.ic_external,
            R.drawable.ic_external,
            R.drawable.ic_external,
            R.drawable.ic_external,
            R.drawable.ic_external,
            R.drawable.ic_external,
            R.drawable.ic_external,
            R.drawable.ic_external};

    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            //noinspection ConstantConditions
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if (dir != null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Snippet.followNightModeInSystem();
        setContentView(R.layout.activity_settings_help);

        Snippet.initializeInterstitialAd(Help.this);

        AdView helpAdView = findViewById(R.id.adHelp);
        AdRequest adRequest = new AdRequest.Builder().build();
        helpAdView.loadAd(adRequest);

        helpAdView.setAdListener(new AdListener() {
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

        Toolbar toolbar = findViewById(R.id.toolbarHelp);
        toolbar.setTitle("Help");
        toolbar.setNavigationIcon(R.drawable.ic_back);
        Snippet.toolbar(Help.this, toolbar);
        setSupportActionBar(toolbar);

        List<HashMap<String, String>> hashMapArrayList = new ArrayList<HashMap<String, String>>();
        for (int x = 0; x <= 7; x++) {
            HashMap<String, String> hashMap = new HashMap<String, String>();
            hashMap.put("ListTitle", ListViewTitle[x]);
            hashMap.put("ListImages", Integer.toString(images[x]));
            if (x == 0) {
                hashMap.put("ListImagesExternal", Integer.toString(external[x]));
            }
            if (x == 1) {
                hashMap.put("ListImagesExternal", Integer.toString(external[x]));
            }
            if (x == 7) {
                hashMap.put("ListImagesExternal", Integer.toString(external[x]));
            }
            hashMapArrayList.add(hashMap);
        }
        String[] from = {
                "ListTitle", "ListImages", "ListImagesExternal"
        };
        int[] to = {
                R.id.listview_text, R.id.listviewimage, R.id.listviewexternal
        };

        SimpleAdapter simpleAdapter = new SimpleAdapter(getBaseContext(), hashMapArrayList, R.layout.listview_items_with_external_no_desc, from, to);
        ListView listView = findViewById(R.id.helpListview);
        listView.setDivider(null);
        listView.setDividerHeight(1);
        listView.setAdapter(simpleAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    Widget.openInBrowser(Help.this, tos);
                }
                if (position == 1) {
                    Widget.openInBrowser(Help.this, pp);
                }
                if (position == 2) {
                    Intent intent = new Intent(view.getContext(), WriteFeedback.class);
                    startActivityForResult(intent, 2);
                }
                if (position == 3) {
                    Intent intent = new Intent(view.getContext(), OpenSource.class);
                    startActivityForResult(intent, 3);
                }
                if (position == 4) {
                    Intent intent = new Intent(view.getContext(), Credits.class);
                    startActivityForResult(intent, 4);
                }
                if (position == 5) {
                    Widget.alertDialog(Help.this,
                            true,
                            "Are you sure want to clear cache?\n\nNote: Please don't clear cache until or unless DepHub is running slow.",
                            "Clear Cache",
                            "Cancel",
                            (dialog, which) -> {
                                deleteCache(getApplicationContext());
                                Widget.Toast(Help.this, "Cache cleared successfully.");
                            },
                            (dialog, which) -> {
                                dialog.dismiss();
                            });
                }
                if (position == 6) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.dephub.android"));
                    startActivity(intent);
                }
                if (position == 7) {
                    String versionName = BuildConfig.VERSION_NAME;
                    int versionCode = BuildConfig.VERSION_CODE;
                    String installer = "";

                    if (!verifyInstaller(getApplicationContext())) {
                        installer = "•Not Installed from Google Play";
                    }

                    @SuppressLint("SimpleDateFormat")
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy");
                    if (BuildConfig.DEBUG) {
                        Widget.alertDialog(Help.this,
                                true,
                                "DepHub\nVersion v" + versionName + " (" + versionCode + ")\n•Development Version\n" + installer + "\n\nCopyright \u00a9 2020-" + simpleDateFormat.format(new Date()) + " DepHub",
                                null,
                                null,
                                null,
                                null);
                    } else {
                        Widget.alertDialog(Help.this,
                                true,
                                "DepHub\nVersion v" + versionName + " (" + versionCode + ")\n" + installer + "\nCopyright \u00A9 2020-" + simpleDateFormat.format(new Date()) + " DepHub",
                                null,
                                null,
                                null,
                                null);
                    }
                }
            }
        });
        Snippet.initializeInterstitialAd(Help.this);
        AdRequest HelpAdRequest = new AdRequest.Builder().build();

        Widget.showInterstitialAd(this, "ca-app-pub-3037529522611130/3494136300", HelpAdRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                Help.super.onBackPressed();
            }

            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                HelpInterstitialAd = interstitialAd;

                interstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        Help.super.onBackPressed();
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                        Help.super.onBackPressed();
                    }
                });
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (HelpInterstitialAd != null) {
            HelpInterstitialAd.show(Help.this);
        } else {
            super.onBackPressed();
        }
    }

    boolean verifyInstaller(Context context) {
        List<String> validInstallers = new ArrayList<>(Collections.singletonList("com.android.vending"));
        final String installer = context.getPackageManager().getInstallerPackageName(context.getPackageName());
        return installer != null && validInstallers.contains(installer);
    }
}