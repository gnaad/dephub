package com.dephub.android.activity;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.dephub.android.R;
import com.dephub.android.fragment.New;
import com.dephub.android.fragment.Top10;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class Leaderboard extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_leaderboard);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow( ).setNavigationBarColor(getResources( ).getColor(R.color.black));
        }

        int limit = 2;
        tabLayout = findViewById(R.id.tabslb);
        viewPager = findViewById(R.id.viewpagerlb);
        viewPager.setOffscreenPageLimit(limit);

        Toolbar toolbar = findViewById(R.id.toolbarlb);
        AppBarLayout appBarLayout = findViewById(R.id.appbarlb);
        toolbar.setTitle("Leaderboard");
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

        getSupportActionBar( ).setElevation(0);
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        MainActivity.ViewPagerAdapter adapter = new MainActivity.ViewPagerAdapter(getSupportFragmentManager( ));
        adapter.addFragment(new Top10( ),"Top 10");
        adapter.addFragment(new New( ),"New");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem( ) == 0) {
            getCacheDir( ).delete( );
            super.onBackPressed( );
        } else {
            viewPager.setCurrentItem(0);
        }
    }


    static class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>( );
        private final List<String> mFragmentTitleList = new ArrayList<>( );

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
            return mFragmentList.size( );
        }

        public void addFragment(Fragment fragment,String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}