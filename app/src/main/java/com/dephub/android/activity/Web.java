package com.dephub.android.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
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

import com.dephub.android.BuildConfig;
import com.dephub.android.R;
import com.dephub.android.common.Component;
import com.dephub.android.common.Snippet;
import com.dephub.android.favorite.DatabaseHelper;
import com.github.aakira.compoundicontextview.CompoundIconTextView;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Web extends AppCompatActivity {
    Activity activity;
    CompoundIconTextView compoundIconTextView;
    FloatingActionButton floatingActionButton, floatingActionButton1;
    String devName, qrCodeLink, qrCodeTitle, qrCodeId, githubLink, license, licenseLink, title, model, versionRelease, versionName, id, cardBackground, youtubeLink, fullName;
    int version, versioncode;
    WebView webView;
    DatabaseHelper databaseHelper;
    private ProgressDialog progressDialog;
    private InterstitialAd githubInterstitialAd, youtubeInterstitialAd, backButtonInterstitialAd;
    Boolean goBack;

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @SuppressLint({"SetJavaScriptEnabled", "ResourceAsColor"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Snippet.followNightModeInSystem();
        setContentView(R.layout.activity_webview);

        getWindow().setNavigationBarColor(getResources().getColor(R.color.black));
        databaseHelper = new DatabaseHelper(this);

        compoundIconTextView = findViewById(R.id.noInternet);
        activity = this;

        progressDialog = new ProgressDialog(Web.this, R.style.CustomAlertDialog);
        progressDialog.setMessage("Loading");
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.show();

        youtubeLink = getIntent().getExtras().getString("youtubeLink");
        cardBackground = getIntent().getExtras().getString("cardbg");
        githubLink = getIntent().getExtras().getString("githubLink");
        title = getIntent().getExtras().getString("dependencyName");
        devName = getIntent().getExtras().getString("developerName");
        license = getIntent().getExtras().getString("license");
        licenseLink = getIntent().getExtras().getString("licenseLink");
        id = getIntent().getExtras().getString("id");
        fullName = getIntent().getExtras().getString("fullName");
        model = Build.MODEL;
        version = Build.VERSION.SDK_INT;
        versionRelease = Build.VERSION.RELEASE;
        versionName = BuildConfig.VERSION_NAME;
        versioncode = BuildConfig.VERSION_CODE;

        qrCodeLink = githubLink;
        qrCodeId = id;
        qrCodeTitle = title;

        Snippet.initializeInterstitialAd(Web.this);
        AdView webAdView = findViewById(R.id.adWeb);
        AdRequest adRequest1 = new AdRequest.Builder().build();
        webAdView.loadAd(adRequest1);

        webAdView.setAdListener(new AdListener() {
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
        //Banner Ad End

        // Interstitial Ad start GitHub Icon
        Snippet.initializeInterstitialAd(Web.this);
        AdRequest adRequestGithub = new AdRequest.Builder().build();
        InterstitialAd.load
                (this,
                        "ca-app-pub-3037529522611130/3494136300",
                        adRequestGithub,
                        new InterstitialAdLoadCallback() {
                            @Override
                            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                                githubInterstitialAd = interstitialAd;

                                githubInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                                    @Override
                                    public void onAdShowedFullScreenContent() {
                                    }

                                    @Override
                                    public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                                        Intent launchIntent = getApplication().getPackageManager().getLaunchIntentForPackage("com.github.android");
                                        if (launchIntent != null) {
                                            openGithub();
                                        } else {
                                            Component.openInBrowser(Web.this, githubLink);
                                        }
                                    }

                                    @Override
                                    public void onAdDismissedFullScreenContent() {
                                        Intent launchIntent = getApplication().getPackageManager().getLaunchIntentForPackage("com.github.android");
                                        if (launchIntent != null) {
                                            openGithub();
                                        } else {
                                            Component.openInBrowser(Web.this, githubLink);
                                        }
                                    }
                                });
                            }

                            @Override
                            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                                githubInterstitialAd = null;
                            }
                        });
        // Interstitial Ad End GitHub Icon

        // Interstitial Ad start YouTube Icon
        Snippet.initializeInterstitialAd(Web.this);
        AdRequest adRequestYoutube = new AdRequest.Builder().build();
        InterstitialAd.load
                (this, "ca-app-pub-3037529522611130/1476337817",
                        adRequestYoutube,
                        new InterstitialAdLoadCallback() {
                            @Override
                            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                                youtubeInterstitialAd = interstitialAd;
                                youtubeInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                                    @Override
                                    public void onAdShowedFullScreenContent() {
                                    }

                                    @Override
                                    public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                                        openYoutube();
                                    }

                                    @Override
                                    public void onAdDismissedFullScreenContent() {
                                        openYoutube();
                                    }
                                });
                            }

                            @Override
                            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                                youtubeInterstitialAd = null;
                            }
                        });
        // Interstitial Ad End YouTube Icon

        // Interstitial Ad back pressed
        Snippet.initializeInterstitialAd(Web.this);
        AdRequest adBackButton = new AdRequest.Builder().build();

        InterstitialAd.load
                (this, "ca-app-pub-3037529522611130/4429511003", adBackButton, new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        backButtonInterstitialAd = interstitialAd;

                        interstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                            @Override
                            public void onAdShowedFullScreenContent() {
                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                                webView.clearCache(true);
                                if (goBack) {
                                    Web.super.onBackPressed();
                                }
                            }

                            @Override
                            public void onAdDismissedFullScreenContent() {
                                webView.clearCache(true);
                                if (goBack) {
                                    Web.super.onBackPressed();
                                }
                            }
                        });
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                    }
                });

        floatingActionButton = findViewById(R.id.github);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                githubInterstitialAd.show(Web.this);
            }
        });

        floatingActionButton1 = findViewById(R.id.youtube);

        if (youtubeLink.equals("no")) {
            floatingActionButton1.setVisibility(View.INVISIBLE);
        } else {
            floatingActionButton1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    youtubeInterstitialAd.show(Web.this);
                }
            });
            floatingActionButton1.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Snippet.vibrate(Web.this, youtubeLink);
                    Component.Toast(Web.this, "Link copied");
                    return false;
                }
            });
        }

        Toolbar toolbar = findViewById(R.id.toolbarWeb);
        AppBarLayout appBarLayout = findViewById(R.id.appbarWeb);
        toolbar.setTitle(title);
        toolbar.setSubtitle(devName);

        toolbar.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Component.Toast(Web.this, "Dependency Id : " + id);
                return false;
            }
        });

        Snippet.toolbar(Web.this, toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        setSupportActionBar(toolbar);

        Drawable drawable = toolbar.getOverflowIcon();
        DrawableCompat.setTint(drawable.mutate(), getResources().getColor(R.color.toolbaricon));
        toolbar.setOverflowIcon(drawable);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        webView = findViewById(R.id.webViewActivity);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
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

        if (youtubeLink.equals("no")) {
            floatingActionButton1.setVisibility(View.INVISIBLE);
        } else {
            floatingActionButton1.hide();
        }

        if (youtubeLink.equals("no")) {
            floatingActionButton1.setVisibility(View.INVISIBLE);
        } else {
            floatingActionButton1.show();
        }

        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.swipeRefreshWeb);
        swipeRefreshLayout.setColorSchemeResources(R.color.blacktowhite);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                progressDialog.show();
                view.loadUrl(githubLink);
                return true;
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                swipeRefreshLayout.setVisibility(View.INVISIBLE);
                progressDialog.cancel();
                webView.setVisibility(View.INVISIBLE);
                floatingActionButton.setVisibility(View.INVISIBLE);
                floatingActionButton1.setVisibility(View.INVISIBLE);
                compoundIconTextView.setVisibility(View.VISIBLE);
                webView.setBackgroundColor(Color.WHITE);
                swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                compoundIconTextView.setVisibility(View.INVISIBLE);
                                webView.setVisibility(View.VISIBLE);
                                swipeRefreshLayout.setRefreshing(false);
                                floatingActionButton.setVisibility(View.VISIBLE);
                                floatingActionButton1.setVisibility(View.VISIBLE);
                                webView.reload();
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
        webView.loadUrl(githubLink);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                        webView.reload();
                    }
                }, 3000);
            }
        });
    }

    private void openYoutube() {
        Intent youtubrIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(youtubeLink));
        youtubrIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(youtubrIntent);
    }

    private void openGithub() {
        Intent githubIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(githubLink));
        githubIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        githubIntent.setPackage("com.github.android");
        getApplication().startActivity(githubIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.webview_settings, menu);

        Cursor cursor = databaseHelper.getFavorite();
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String databaseId = cursor.getString(1);
                if (databaseId.equals(id)) {
                    menu.findItem(R.id.addtofavorite).setTitle("Remove From Favorites");
                } else {
                    menu.findItem(R.id.addtofavorite).setTitle("Add To Favorites");
                }
            }
        }
        menu.findItem(R.id.licensetype).setTitle("License : " + license);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.addtofavorite:
                if (item.getTitle().equals("Add To Favorites")) {
                    boolean inserted = databaseHelper.insertData(id, title, devName, githubLink, cardBackground, fullName, license, licenseLink, youtubeLink);
                    if (inserted) {
                        Component.Toast(Web.this, "Added to Favorites");
                    } else {
                        Component.Toast(Web.this, "It's already in My Favorites");
                    }
                }

                if (item.getTitle().equals("Remove From Favorites")) {
                    Integer deleteFavorite = databaseHelper.deleteFavorite(id);
                    if (deleteFavorite > 0) {
                        Component.Toast(Web.this, "Removed From Favorites");
                    } else {
                        Component.Toast(Web.this, "It's already Removed From Favorites");
                    }
                }
                break;
            case R.id.licensetype:
                if (license.equals("No License")) {
                    Component.alertDialog(Web.this,
                            true,
                            "This dependency has No License.",
                            "Close",
                            null,
                            (dialog, which) -> {
                                dialog.dismiss();
                            }, null);
                } else {
                    Component.openInBrowser(Web.this, licenseLink);
                }
                break;

            case R.id.share:
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                String shareBody = "About Android Dependency";
                String shareSub = "Hi there\n\nDependency Name : " + title + "\nDependency Website : " + githubLink + "\n\nInformation Delivered by : DepHub\nInformation Provided by : Github\n\nDownload our Android App : https://bit.ly/installdephubapp\n\nThank You\nLet's code for a better tomorrow";
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, shareBody);
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareSub);
                startActivity(Intent.createChooser(shareIntent, "Share this Dependency using"));
                break;

            case R.id.reportbug:
                Intent bugIntent = new Intent(Intent.ACTION_SEND);
                String[] emailAddress = {"mailtodephub@gmail.com"};
                bugIntent.putExtra(Intent.EXTRA_EMAIL, emailAddress);
                bugIntent.putExtra(Intent.EXTRA_SUBJECT, "Bug Report - " + title);
                bugIntent.putExtra(Intent.EXTRA_TEXT, "Model : " + model + "\nSDK Version : " + version + "\nAndroid Version : " + versionRelease + "\nVersion Name : " + versionName + "\nVersion Code : " + versioncode + "\n\n-- Please don't edit anything above this line, it helps us to serve you better --" +
                        "\n\nI landed up with a problem while using DepHub. There's a bug in " + title + "\n\nPlease describe your problem below:\n");
                bugIntent.setType("message/rfc822");
                startActivity(Intent.createChooser(bugIntent, "Choose an email client"));
                break;

            case R.id.qrCode:
                Intent qrcodeIntent = new Intent(getApplicationContext(), QRCode.class);
                qrcodeIntent.putExtra("qrCodeLink", qrCodeLink);
                qrcodeIntent.putExtra("qrCodeTitle", qrCodeTitle);
                qrcodeIntent.putExtra("developerName", devName);
                qrcodeIntent.putExtra("qrCodeId", qrCodeId);
                startActivity(qrcodeIntent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        databaseHelper.close();
        webView.clearCache(true);
        super.onDestroy();
    }

    @Override
    public boolean onSupportNavigateUp() {
        if (backButtonInterstitialAd != null) {
            goBack = true;
            backButtonInterstitialAd.show(Web.this);
        } else {
            super.onBackPressed();
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (backButtonInterstitialAd != null) {
            goBack = true;
            backButtonInterstitialAd.show(Web.this);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        if (backButtonInterstitialAd != null) {
            goBack = false;
            backButtonInterstitialAd.show(Web.this);
        }
        super.onResume();
    }
}