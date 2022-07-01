package com.dephub.android.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
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
import com.dephub.android.fragment.Widget;
import com.dephub.android.settings.Options;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
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

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_mainactivity);

        getWindow().setNavigationBarColor(getResources().getColor(R.color.black));

        int limit = 10;
        tabLayout = findViewById(R.id.tabsMainActivity);
        viewPager = findViewById(R.id.viewPagerMainActivity);
        viewPager.setOffscreenPageLimit(limit);

        Toolbar toolbar = findViewById(R.id.toolbarMainActivity);
        AppBarLayout appBarLayout = findViewById(R.id.appbarMainActivity);
        toolbar.setTitle("DepHub");

        int nightModeFlags = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
            int white = Color.parseColor("#ffffff");
            toolbar.setTitleTextColor(white);
        } else {
            int black = Color.parseColor("#000000");
            toolbar.setTitleTextColor(black);
        }

        setSupportActionBar(toolbar);

        progressDialog = new ProgressDialog(MainActivity.this, R.style.CustomAlertDialog);
        progressDialog.setMessage("Loading");
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);

        Drawable drawable = toolbar.getOverflowIcon();
        //noinspection ConstantConditions
        DrawableCompat.setTint(drawable.mutate(), getResources().getColor(R.color.toolbaricon));
        toolbar.setOverflowIcon(drawable);

        //noinspection ConstantConditions
        getSupportActionBar().setElevation(0);
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
                    if (Integer.parseInt(remoteVersionCode) == versionName) {

                    } else {
                        update();
                    }
                }
            }
        });

        remoteValue.fetchAndActivate().addOnCompleteListener(this, new OnCompleteListener<Boolean>() {
            @Override
            public void onComplete(@NonNull Task<Boolean> task) {
                if (task.isSuccessful()) {
                    final String visibility = remoteValue.getString("visibility");

                    //noinspection StatementWithEmptyBody
                    if (visibility.equals("visible")) {

                    } else {
                        invisible();
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

        // Interstitial Ad start
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        AdRequest MainActivityAdRequest = new AdRequest.Builder().build();

        InterstitialAd.load
                (this, "ca-app-pub-3037529522611130/2378062737", MainActivityAdRequest, new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        MainActivityInterstitialAd = interstitialAd;

                        interstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                            @Override
                            public void onAdShowedFullScreenContent() {
                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                                if (goBack) {
                                    MainActivity.super.onBackPressed();
                                } else {
                                }
                            }

                            @Override
                            public void onAdDismissedFullScreenContent() {
                                if (goBack) {
                                    MainActivity.super.onBackPressed();
                                } else {
                                }
                            }
                        });
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {

                    }
                });
        // Interstitial Ad End
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new Text(), "Text");
        adapter.addFragment(new Button(), "Button");
        adapter.addFragment(new Widget(), "Widgets");
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
            case R.id.settings_news:
                Intent intent1 = new Intent(this, News.class);
                this.startActivity(intent1);
                break;
            case R.id.action_search:
                Intent intent2 = new Intent(this, Search.class);
                this.startActivity(intent2);
                break;
            case R.id.settings_submityourdependency:
                Intent intent3 = new Intent(this, SubmitDependency.class);
                this.startActivity(intent3);
                break;
            case R.id.settings:
                Intent intent5 = new Intent(this, Options.class);
                this.startActivity(intent5);
                break;
            case R.id.favactivity:
                Intent intent6 = new Intent(this, Favorite.class);
                this.startActivity(intent6);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void invisible() {
        tabLayout.setVisibility(View.INVISIBLE);
        viewPager.setVisibility(View.INVISIBLE);
        final AlertDialog dialog = new AlertDialog.Builder(this, R.style.CustomAlertDialog)
                .setCancelable(false)
                .setMessage(Html.fromHtml("<font color='#000000'>Please come back after some time.<br/><br/>If you have any query Mail Us.</font>"))
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNegativeButton("Mail Us", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        String[] mailTo = {"mailtodephub@gmail.com"};
                        intent.putExtra(Intent.EXTRA_EMAIL, mailTo);
                        intent.putExtra(Intent.EXTRA_SUBJECT, "About DepHub App");
                        intent.putExtra(Intent.EXTRA_TEXT, "Hello\n\nI would like to say:\n");
                        intent.setType("message/rfc822");
                        startActivity(Intent.createChooser(intent, "Choose an email client"));
                    }
                })
                .create();
        dialog.show();
        alertDialogButton(dialog);
    }

    private void serverBusy() {
        tabLayout.setVisibility(View.INVISIBLE);
        viewPager.setVisibility(View.INVISIBLE);
        final AlertDialog dialog = new AlertDialog.Builder(this, R.style.CustomAlertDialog)
                .setCancelable(false)
                .setMessage(Html.fromHtml("<font color='#000000'>Our Server is under Maintenance<br/><br/>Our Server is under maintenance. But we will be back within few minutes.<br/><br/>We are Improving DepHub to provide you more information about Dependencies.<br/><br/>Thanks for your patience.<br/>We apologize for the inconvenience caused.<br/><br/>Regards<br/>DepHub<br/><br/>We will send you notification once its ready.</font>"))
                .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .create();
        dialog.show();
        alertDialogButton(dialog);
    }

    private void update() {
        tabLayout.setVisibility(View.INVISIBLE);
        viewPager.setVisibility(View.INVISIBLE);
        final AlertDialog dialog = new AlertDialog.Builder(this, R.style.CustomAlertDialog)
                .setCancelable(false)
                .setMessage(Html.fromHtml("<font color='#000000'>Update Available<br/><br/>New version of this app is available now. Please update to get more features.<br/><br/>Get it on Google PlayStore</font>"))
                .setPositiveButton("Update Now", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.dephub.android"));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                })
                .setNegativeButton("No,Thanks", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        tabLayout.setVisibility(View.VISIBLE);
                        viewPager.setVisibility(View.VISIBLE);
                        dialog.dismiss();
                    }
                })
                .create();
        dialog.show();
        alertDialogButton(dialog);
    }

    private void alertDialogButton(AlertDialog dialog) {
        android.widget.Button PositiveButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        PositiveButton.setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
        android.widget.Button NegativeButton = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        NegativeButton.setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
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
        } else {
        }
        super.onResume();
    }

    static class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}