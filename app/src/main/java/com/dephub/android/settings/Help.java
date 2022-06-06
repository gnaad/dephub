package com.dephub.android.settings;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.core.content.ContextCompat;

import com.dephub.android.BuildConfig;
import com.dephub.android.R;
import com.dephub.android.activity.Credits;
import com.dephub.android.activity.Opensource;
import com.dephub.android.activity.WriteFeedback;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Help extends AppCompatActivity {
    public static final String pp = "https://gnanendraprasadp.github.io/DepHub-Web/privacypolicy";
    public static final String tos = "https://gnanendraprasadp.github.io/DepHub-Web/termsofservice";
    String[] Listviewtitle = new String[]{
            "Terms of Service",
            "Privacy Policy",
            "Write Feedback",
            "Tell us how to improve",
            "Open Source Licenses",
            "Credits",
            "Clear Cache",
            "Rate this App",
            "App info"};
    int[] images = new int[]{
            R.drawable.ic_tos,
            R.drawable.ic_pp,
            R.drawable.ic_feedback,
            R.drawable.ic_mail,
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
            R.drawable.ic_external,
            R.drawable.ic_external};
    private AdView mAdView;

    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir( );
            deleteDir(dir);
        } catch (Exception e) {
            e.printStackTrace( );
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory( )) {
            String[] children = dir.list( );
            //noinspection ConstantConditions
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir,children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete( );
        } else if (dir != null && dir.isFile( )) {
            return dir.delete( );
        } else {
            return false;
        }
    }

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);

        setContentView(R.layout.activity_settings_help);

        MobileAds.initialize(this,new OnInitializationCompleteListener( ) {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.adhelp);
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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow( ).setNavigationBarColor(getResources( ).getColor(R.color.black));
        }

        Toolbar toolbar = findViewById(R.id.toolbarshelp);
        toolbar.setTitle("Help");
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
        for (int x = 0; x <= 8; x++) {
            HashMap<String, String> hm = new HashMap<String, String>( );
            hm.put("ListTitle",Listviewtitle[x]);
            hm.put("Listimages",Integer.toString(images[x]));
            if (x == 0) {
                hm.put("Listimagesexternal",Integer.toString(external[x]));
            }
            if (x == 1) {
                hm.put("Listimagesexternal",Integer.toString(external[x]));
            }
            if (x == 7) {
                hm.put("Listimagesexternal",Integer.toString(external[x]));
            }
            alist.add(hm);
        }
        String[] from = {
                "ListTitle","Listimages","Listimagesexternal"
        };
        int[] to = {
                R.id.listview_text,R.id.listviewimage,R.id.listviewexternal
        };

        SimpleAdapter simpleAdapter = new SimpleAdapter(getBaseContext( ),alist,R.layout.listview_items_with_external_no_desc,from,to);
        ListView listView = findViewById(R.id.info_list);
        listView.setDivider(null);
        listView.setDividerHeight(1);
        listView.setAdapter(simpleAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener( ) {

            @Override
            public void onItemClick(AdapterView<?> parent,View view,int position,long id) {
                if (position == 0) {
                    CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder( );

                    builder.setToolbarColor(ContextCompat.getColor(getApplicationContext( ),R.color.colorPrimary));
                    builder.setShowTitle(true);
                    builder.addDefaultShareMenuItem( );
                    builder.setUrlBarHidingEnabled(true);
                    builder.setStartAnimations(Help.this,R.anim.slide_up,R.anim.trans);
                    builder.setExitAnimations(Help.this,R.anim.trans,R.anim.slide_down);
                    CustomTabsIntent customTabsIntent = builder.build( );
                    customTabsIntent.launchUrl(Help.this,Uri.parse(tos));
                }
                if (position == 1) {
                    CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder( );

                    builder.setToolbarColor(ContextCompat.getColor(Help.this,R.color.colorPrimary));
                    builder.setShowTitle(true);
                    builder.addDefaultShareMenuItem( );
                    builder.setUrlBarHidingEnabled(true);
                    builder.setStartAnimations(Help.this,R.anim.slide_up,R.anim.trans);
                    builder.setExitAnimations(Help.this,R.anim.trans,R.anim.slide_down);
                    CustomTabsIntent customTabsIntent = builder.build( );
                    customTabsIntent.launchUrl(Help.this,Uri.parse(pp));
                }
                if (position == 2) {
                    Intent intent = new Intent(view.getContext( ),WriteFeedback.class);
                    startActivityForResult(intent,2);
                }
                if (position == 3) {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    String[] stringto = {"mailtodephub@gmail.com"};
                    intent.putExtra(Intent.EXTRA_EMAIL,stringto);
                    intent.putExtra(Intent.EXTRA_SUBJECT,"Users opinion about how to improve DepHub App");
                    intent.putExtra(Intent.EXTRA_TEXT,"Hello\n\nI would like to say:\n");
                    intent.setType("message/rfc822");
                    startActivity(Intent.createChooser(intent,"Choose an email client"));
                }
                if (position == 4) {
                    Intent intent = new Intent(view.getContext( ),Opensource.class);
                    startActivityForResult(intent,5);
                }
                if (position == 5) {
                    Intent intent = new Intent(view.getContext( ),Credits.class);
                    startActivityForResult(intent,5);
                }

                if (position == 6) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Help.this,R.style.CustomAlertDialog);

                    alertDialogBuilder.setCancelable(true);
                    alertDialogBuilder.setMessage("Are you sure want to clear cache?\n\nNote: Please don't clear cache until or unless DepHub is running slow.");
                    alertDialogBuilder.setPositiveButton("Clear Cache",new DialogInterface.OnClickListener( ) {
                        public void onClick(DialogInterface dialog,int id) {
                            deleteCache(getApplicationContext( ));
                            Toast.makeText(Help.this,"Cache cleared successfully.",Toast.LENGTH_SHORT).show( );
                        }
                    });
                    alertDialogBuilder.setNegativeButton("Cancel",new DialogInterface.OnClickListener( ) {
                        @Override
                        public void onClick(DialogInterface dialog,int which) {
                            dialog.dismiss( );
                        }
                    });
                    AlertDialog alertDialog = alertDialogBuilder.create( );
                    alertDialog.show( );
                    alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources( ).getColor(R.color.colorAccent));
                    alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources( ).getColor(R.color.colorAccent));
                }
                if (position == 7) {
                    Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse("https://play.google.com/store/apps/details?id=com.dephub.android"));
                    startActivity(intent);
                }
                if (position == 8) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Help.this,R.style.CustomAlertDialog);

                    String versionName = BuildConfig.VERSION_NAME;
                    int versionCode = BuildConfig.VERSION_CODE;

                    String installer = "";

                    if (!verifyinstaller(getApplicationContext( ))) {
                        installer = "•Unknown Resources";
                    }

                    if (BuildConfig.DEBUG) {
                        @SuppressLint("SimpleDateFormat")
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy");

                        alertDialogBuilder.setCancelable(true);
                        alertDialogBuilder.setMessage("DepHub\nVersion v" + versionName + " (" + versionCode + ")\n•Debug Mode\n" + installer + "\n\nCopyright \u00a9 2020-" + simpleDateFormat.format(new Date( )) + " DepHub");
                        AlertDialog alertDialog = alertDialogBuilder.create( );
                        alertDialog.show( );
                        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources( ).getColor(R.color.colorAccent));
                        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources( ).getColor(R.color.colorAccent));
                    } else {
                        @SuppressLint("SimpleDateFormat")
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy");

                        alertDialogBuilder.setCancelable(true);
                        alertDialogBuilder.setMessage("DepHub\nVersion v" + versionName + " (" + versionCode + ")\n" + installer + "\nCopyright \u00A9 2020-" + simpleDateFormat.format(new Date( )) + " DepHub");
                        AlertDialog alertDialog = alertDialogBuilder.create( );
                        alertDialog.show( );
                        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources( ).getColor(R.color.colorAccent));
                        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources( ).getColor(R.color.colorAccent));
                    }
                }
            }
        });
    }

    boolean verifyinstaller(Context context) {
        List<String> validInstallers = new ArrayList<>(Collections.singletonList("com.android.vending"));
        final String installer = context.getPackageManager( ).getInstallerPackageName(context.getPackageName( ));
        return installer != null && validInstallers.contains(installer);
    }
}