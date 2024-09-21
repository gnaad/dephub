package com.dephub.android.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.dephub.android.R;
import com.dephub.android.constant.ApplicationConstant;
import com.dephub.android.fragment.Tab;
import com.dephub.android.settings.Settings;
import com.dephub.android.utility.Snippet;
import com.dephub.android.utility.Widget;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Boolean goBack;
    private ViewPager viewPager;

    Drawable drawable;
    TabLayout tabLayout;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Snippet.followNightModeInSystem();
        Snippet.layoutInDisplayCutoutMode(MainActivity.this);
        Snippet.darkTheme(MainActivity.this, getApplicationContext());
        setContentView(R.layout.activity_main_activity);

        if (!isNetworkAvailable()) {
            Widget.alertDialog(
                    MainActivity.this,
                    false,
                    ApplicationConstant.CONNECTION_ISSUE,
                    ApplicationConstant.CLOSE,
                    ApplicationConstant.RETRY,
                    (dialog, which) -> {
                        finish();
                    },
                    (dialog, which) -> {
                        finish();
                        startActivity(getIntent());
                    }
            );
        }

        getWindow().setNavigationBarColor(getResources().getColor(R.color.black));

        int limit = 10;
        tabLayout = findViewById(R.id.tabs_main_activity);
        viewPager = findViewById(R.id.view_pager_main_activity);
        viewPager.setOffscreenPageLimit(limit);

        toolbar = findViewById(R.id.toolbar_main_activity);
        findViewById(R.id.appbar_main_activity);
        toolbar.setTitle(ApplicationConstant.DEPHUB);
        Snippet.toolbar(MainActivity.this, toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setElevation(0);

        drawable = toolbar.getOverflowIcon();
        DrawableCompat.setTint(drawable.mutate(), getResources().getColor(R.color.toolbar_icon));
        toolbar.setOverflowIcon(drawable);
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        for (int i = 0; i < 9; i++) {
            adapter.addFragment(new Tab(ApplicationConstant.TABS[i]), ApplicationConstant.TABS[i]);
        }
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.settings, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_search) {
            Intent intent1 = new Intent(this, Search.class);
            this.startActivity(intent1);
        } else if (itemId == R.id.action_submit_your_dependency) {
            Intent intent2 = new Intent(this, SubmitDependency.class);
            this.startActivity(intent2);
        } else if (itemId == R.id.action_settings) {
            Intent intent3 = new Intent(this, Settings.class);
            this.startActivity(intent3);
        } else {
            return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem() == 0) {
            getCacheDir().delete();
            goBack = true;
            super.onBackPressed();
        } else {
            viewPager.setCurrentItem(0);
        }
    }

    @Override
    protected void onResume() {
        goBack = false;
        super.onResume();
    }

    static class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> listFragment = new ArrayList<>();
        private final List<String> titleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return listFragment.get(position);
        }

        @Override
        public int getCount() {
            return listFragment.size();
        }

        public void addFragment(Fragment fragment, String title) {
            listFragment.add(fragment);
            titleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titleList.get(position);
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
