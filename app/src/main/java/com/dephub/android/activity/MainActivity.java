package com.dephub.android.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    String remoteversioncode;
    ProgressDialog progressDialog;
    private FirebaseRemoteConfig mFirebaseRemoteConfig;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint({"ResourceAsColor","UseCompatLoadingForDrawables","RestrictedApi"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_mainactivity);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow( ).setNavigationBarColor(getResources( ).getColor(R.color.black));
        }

        int limit = 10;
        tabLayout = findViewById(R.id.tabsma);
        viewPager = findViewById(R.id.viewpagerma);
        viewPager.setOffscreenPageLimit(limit);

        Toolbar toolbar = findViewById(R.id.toolbarma);
        AppBarLayout appBarLayout = findViewById(R.id.appbarma);
        toolbar.setTitle("DepHub");

        int nightModeFlags = getResources( ).getConfiguration( ).uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
            int white = Color.parseColor("#ffffff");
            toolbar.setTitleTextColor(white);
        } else {
            int black = Color.parseColor("#000000");
            toolbar.setTitleTextColor(black);
        }

        setSupportActionBar(toolbar);

        progressDialog = new ProgressDialog(MainActivity.this,R.style.CustomAlertDialog);
        progressDialog.setMessage("Loading");
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);

        Drawable drawable = toolbar.getOverflowIcon( );
        //noinspection ConstantConditions
        DrawableCompat.setTint(drawable.mutate( ),getResources( ).getColor(R.color.toolbaricon));
        toolbar.setOverflowIcon(drawable);

        //noinspection ConstantConditions
        getSupportActionBar( ).setElevation(0);
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance( );
        mFirebaseRemoteConfig.fetch(0);
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder( )
                .setMinimumFetchIntervalInSeconds(0)
                .build( );

        mFirebaseRemoteConfig.setDefaultsAsync(R.xml.default_map);


        mFirebaseRemoteConfig.fetchAndActivate( ).addOnCompleteListener(this,new OnCompleteListener<Boolean>( ) {
            @Override
            public void onComplete(@NonNull Task<Boolean> task) {
                if (task.isSuccessful( )) {

                    int versionName = BuildConfig.VERSION_CODE;
                    remoteversioncode = mFirebaseRemoteConfig.getString("version_code");
                    if (Integer.parseInt(remoteversioncode) == versionName) {

                    } else {
                        update("com.dephub.android");
                    }
                }
            }
        });


        mFirebaseRemoteConfig.fetchAndActivate( ).addOnCompleteListener(this,new OnCompleteListener<Boolean>( ) {
            @Override
            public void onComplete(@NonNull Task<Boolean> task) {
                if (task.isSuccessful( )) {
                    final String visibility = mFirebaseRemoteConfig.getString("visibility");

                    //noinspection StatementWithEmptyBody
                    if (visibility.equals("visible")) {

                    } else {
                        invisible( );
                    }
                }
            }
        });

        mFirebaseRemoteConfig.fetchAndActivate( ).addOnCompleteListener(this,new OnCompleteListener<Boolean>( ) {
            @Override
            public void onComplete(@NonNull Task<Boolean> task) {
                if (task.isSuccessful( )) {
                    final String server_busy = mFirebaseRemoteConfig.getString("server_busy");

                    if (server_busy.equals("yes")) {
                        serverbusy( );
                    }
                }
            }
        });
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager( ));
        adapter.addFragment(new Text( ),"Text");
        adapter.addFragment(new Button( ),"Button");
        adapter.addFragment(new Widget( ),"Widgets");
        adapter.addFragment(new Layout( ),"Layout");
        adapter.addFragment(new Container( ),"Container");
        adapter.addFragment(new Helper( ),"Helper");
        adapter.addFragment(new Google( ),"Google");
        adapter.addFragment(new Legacy( ),"Legacy");
        adapter.addFragment(new Others( ),"Others");
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater( );
        menuInflater.inflate(R.menu.mainactivity_settings,menu);

        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId( )) {
            case R.id.settings_news:
                Intent intent1 = new Intent(this,News.class);
                this.startActivity(intent1);
                break;
            case R.id.action_search:
                Intent intent2 = new Intent(this,Search.class);
                this.startActivity(intent2);
                break;
            case R.id.settings_submityourdependency:
                Intent intent3 = new Intent(this,SubmitDependency.class);
                this.startActivity(intent3);
                break;
            case R.id.settings_leaderboard:
                Intent intent4 = new Intent(this,Leaderboard.class);
                this.startActivity(intent4);
                break;
            case R.id.settings:
                Intent intent5 = new Intent(this,Options.class);
                this.startActivity(intent5);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void invisible() {
        tabLayout.setVisibility(View.INVISIBLE);
        viewPager.setVisibility(View.INVISIBLE);
        final AlertDialog dialog = new AlertDialog.Builder(this,R.style.CustomAlertDialog)
                .setCancelable(false)
                .setMessage(Html.fromHtml("<font color='#000000'>Please come back after some time.<br/><br/>If you have any query Mail Us.</font>"))
                .setPositiveButton("Ok",new DialogInterface.OnClickListener( ) {
                    @Override
                    public void onClick(DialogInterface dialog,int which) {
                        finish( );
                    }
                })
                .setNegativeButton("Mail Us",new DialogInterface.OnClickListener( ) {
                    @Override
                    public void onClick(DialogInterface dialog,int which) {
                        finish( );
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        String[] stringto = {"mailtodephub@gmail.com"};
                        intent.putExtra(Intent.EXTRA_EMAIL,stringto);
                        intent.putExtra(Intent.EXTRA_SUBJECT,"About DepHub App");
                        intent.putExtra(Intent.EXTRA_TEXT,"Hello\n\nI would like to say:\n");
                        intent.setType("message/rfc822");
                        // intent.setPackage("com.google.android.gm");
                        startActivity(Intent.createChooser(intent,"Choose an email client"));
                    }
                })
                .create( );
        dialog.show( );
        android.widget.Button buttonPositive = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        buttonPositive.setTextColor(ContextCompat.getColor(this,R.color.colorAccent));
        android.widget.Button buttonNegative = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        buttonNegative.setTextColor(ContextCompat.getColor(this,R.color.colorAccent));
    }

    private void serverbusy() {
        tabLayout.setVisibility(View.INVISIBLE);
        viewPager.setVisibility(View.INVISIBLE);
        final AlertDialog dialog = new AlertDialog.Builder(this,R.style.CustomAlertDialog)
                .setCancelable(false)
                .setMessage(Html.fromHtml("<font color='#000000'>Our Server is under Maintenance<br/><br/>Our Server is under maintenance. But we will be back within few minutes.<br/><br/>We are Improving DepHub to provide you more information about Dependencies.<br/><br/>Thanks for your patience.<br/>We apologize for the inconvenience caused.<br/><br/>Regards<br/>DepHub<br/><br/>We will send you notification once its ready.</font>"))
                .setPositiveButton("Close",new DialogInterface.OnClickListener( ) {
                    @Override
                    public void onClick(DialogInterface dialog,int which) {
                        finish( );
                    }
                })
                .create( );
        dialog.show( );
        android.widget.Button buttonPositive = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        buttonPositive.setTextColor(ContextCompat.getColor(this,R.color.colorAccent));
        android.widget.Button buttonNegative = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        buttonNegative.setTextColor(ContextCompat.getColor(this,R.color.colorAccent));
    }

    private void update(final String appPackageName) {
        tabLayout.setVisibility(View.INVISIBLE);
        viewPager.setVisibility(View.INVISIBLE);
        final AlertDialog dialog = new AlertDialog.Builder(this,R.style.CustomAlertDialog)

                .setCancelable(false)
                .setMessage(Html.fromHtml("<font color='#000000'>Update Available<br/><br/>New version of this app is available now. Please update to get more features.<br/><br/>Get it on Google PlayStore</font>"))
                .setPositiveButton("Update Now",new DialogInterface.OnClickListener( ) {
                    @Override
                    public void onClick(DialogInterface dialog,int which) {

                        Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse("https://play.google.com/store/apps/details?id=com.dephub.android"));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish( );
                    }
                })
                .setNegativeButton("No,Thanks",new DialogInterface.OnClickListener( ) {
                    @Override
                    public void onClick(DialogInterface dialog,int which) {
                        tabLayout.setVisibility(View.VISIBLE);
                        viewPager.setVisibility(View.VISIBLE);
                        dialog.dismiss( );
                    }
                })
                .create( );
        dialog.show( );

        android.widget.Button buttonPositive = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        buttonPositive.setTextColor(ContextCompat.getColor(this,R.color.colorAccent));
        android.widget.Button buttonNegative = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        buttonNegative.setTextColor(ContextCompat.getColor(this,R.color.colorAccent));
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