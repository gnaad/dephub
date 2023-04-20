package com.dephub.android.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.dephub.android.BuildConfig;
import com.dephub.android.R;
import com.dephub.android.fragment.Button;
import com.dephub.android.fragment.Container;
import com.dephub.android.fragment.Google;
import com.dephub.android.fragment.Helper;
import com.dephub.android.fragment.Layout;
import com.dephub.android.fragment.Legacy;
import com.dephub.android.fragment.Others;
import com.dephub.android.fragment.Text;
import com.dephub.android.settings.Main;
import com.dephub.android.utility.Snippet;
import com.dephub.android.utility.Widget;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private String remoteVersionCode;
    ProgressDialog progressDialog;
    private FirebaseRemoteConfig remoteValue;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private InterstitialAd MainActivityInterstitialAd;
    private Boolean goBack;

    @SuppressLint({"ResourceAsColor", "UseCompatLoadingForDrawables", "RestrictedApi"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Snippet.followNightModeInSystem();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_mainactivity);

        getWindow().setNavigationBarColor(getResources().getColor(R.color.black));

        int limit = 10;
        tabLayout = findViewById(R.id.tabsMainActivity);
        viewPager = findViewById(R.id.viewPagerMainActivity);
        viewPager.setOffscreenPageLimit(limit);

        Toolbar toolbar = findViewById(R.id.toolbarMainActivity);
        findViewById(R.id.appbarMainActivity);
        toolbar.setTitle("DepHub");
        Snippet.toolbar(MainActivity.this, toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setElevation(0);

        progressDialog = new ProgressDialog(MainActivity.this, R.style.CustomAlertDialog);
        progressDialog.setMessage("Loading");
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);

        Drawable drawable = toolbar.getOverflowIcon();
        DrawableCompat.setTint(drawable.mutate(), getResources().getColor(R.color.toolbar_icon));
        toolbar.setOverflowIcon(drawable);
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

        remoteValue = FirebaseRemoteConfig.getInstance();
        remoteValue.fetch(0);
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(0)
                .build();
        remoteValue.setDefaultsAsync(R.xml.default_map);

        remoteValue.fetchAndActivate().addOnCompleteListener(this, new OnCompleteListener<Boolean>() {
            @Override
            public void onComplete(@NonNull Task<Boolean> task) {
                if (task.isSuccessful()) {
                    int versionName = BuildConfig.VERSION_CODE;
                    remoteVersionCode = remoteValue.getString("version_code");
                    if (Integer.parseInt(remoteVersionCode) != versionName) {
                        update();
                    }
                }
            }
        });

        remoteValue.fetchAndActivate().addOnCompleteListener(this, new OnCompleteListener<Boolean>() {
            @Override
            public void onComplete(@NonNull Task<Boolean> task) {
                if (task.isSuccessful()) {
                    final String blockUI = remoteValue.getString("visibility");
                    if (!blockUI.equals("visible")) {
                        blockUI();
                    }
                }
            }
        });

        remoteValue.fetchAndActivate().addOnCompleteListener(this, new OnCompleteListener<Boolean>() {
            @Override
            public void onComplete(@NonNull Task<Boolean> task) {
                if (task.isSuccessful()) {
                    final String server_busy = remoteValue.getString("server_busy");
                    if (server_busy.equals("yes")) {
                        serverBusy();
                    }
                }
            }
        });

        Snippet.initializeInterstitialAd(MainActivity.this);
        AdRequest MainActivityAdRequest = new AdRequest.Builder().build();

        Widget.showInterstitialAd(this, "ca-app-pub-3037529522611130/2378062737", MainActivityAdRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                MainActivity.super.onBackPressed();
            }

            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                MainActivityInterstitialAd = interstitialAd;

                interstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        if (goBack) {
                            MainActivity.super.onBackPressed();
                        }
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                        if (goBack) {
                            MainActivity.super.onBackPressed();
                        }
                    }
                });
            }
        });
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new Text(), "Text");
        adapter.addFragment(new Button(), "Button");
        adapter.addFragment(new com.dephub.android.fragment.Widget(), "Widgets");
        adapter.addFragment(new Layout(), "Layout");
        adapter.addFragment(new Container(), "Container");
        adapter.addFragment(new Helper(), "Helper");
        adapter.addFragment(new Google(), "Google");
        adapter.addFragment(new Legacy(), "Legacy");
        adapter.addFragment(new Others(), "Others");
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.mainactivity_settings, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                Intent intent1 = new Intent(this, Search.class);
                this.startActivity(intent1);
                break;
            case R.id.action_submit_your_dependency:
                Intent intent2 = new Intent(this, SubmitDependency.class);
                this.startActivity(intent2);
                break;
            case R.id.action_settings:
                Intent intent3 = new Intent(this, Main.class);
                this.startActivity(intent3);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void blockUI() {
        tabLayout.setVisibility(View.INVISIBLE);
        viewPager.setVisibility(View.INVISIBLE);
        Widget.alertDialog(MainActivity.this,
                false,
                getString(R.string.blockUI_message),
                "Ok",
                "Mail Us",
                (dialog, which) -> {
                    finish();
                },
                (dialog, which) -> {
                    finish();
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    String[] mailTo = {"mailtodephub@gmail.com"};
                    intent.putExtra(Intent.EXTRA_EMAIL, mailTo);
                    intent.putExtra(Intent.EXTRA_SUBJECT, "About DepHub App");
                    intent.putExtra(Intent.EXTRA_TEXT, "Hello\n\nI would like to say:\n");
                    intent.setType("message/rfc822");
                    startActivity(Intent.createChooser(intent, "Choose an email client"));
                }
        );
    }

    private void serverBusy() {
        tabLayout.setVisibility(View.INVISIBLE);
        viewPager.setVisibility(View.INVISIBLE);
        Widget.alertDialog(MainActivity.this,
                false,
                getString(R.string.server_busy_message),
                "Close",
                null,
                (dialog, which) -> {
                    finish();
                }, null
        );
    }

    private void update() {
        tabLayout.setVisibility(View.INVISIBLE);
        viewPager.setVisibility(View.INVISIBLE);
        Widget.alertDialog(MainActivity.this,
                false,
                getString(R.string.update_message),
                "Update Now",
                "No,Thanks",
                (dialog, which) -> {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.dephub.android"));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                },
                (dialog, which) -> {
                    tabLayout.setVisibility(View.VISIBLE);
                    viewPager.setVisibility(View.VISIBLE);
                    dialog.dismiss();
                }
        );
    }

    @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem() == 0) {
            getCacheDir().delete();
            if (MainActivityInterstitialAd != null) {
                goBack = true;
                MainActivityInterstitialAd.show(MainActivity.this);
            }
        } else {
            viewPager.setCurrentItem(0);
        }
    }

    @Override
    protected void onResume() {
        if (MainActivityInterstitialAd != null) {
            goBack = false;
            MainActivityInterstitialAd.show(MainActivity.this);
        }
        super.onResume();
    }

    static class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> FragmentList = new ArrayList<>();
        private final List<String> FragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return FragmentList.get(position);
        }

        @Override
        public int getCount() {
            return FragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            FragmentList.add(fragment);
            FragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return FragmentTitleList.get(position);
        }
    }
}