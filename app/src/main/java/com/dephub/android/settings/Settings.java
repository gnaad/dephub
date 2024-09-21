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
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.dephub.android.BuildConfig;
import com.dephub.android.R;
import com.dephub.android.constant.ApplicationConstant;
import com.dephub.android.utility.Snippet;
import com.dephub.android.utility.Widget;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class Settings extends AppCompatActivity {
    String[] title = new String[]{
            "Credits",
            "Write Feedback",
            "Open Source Libraries",
            "Clear Cache",
            "Privacy Policy",
            "Terms of Service",
            "View Website",
            "Invite a Friend",
            "Rate this App",
            "App info",
    };

    int[] images = new int[]{
            R.drawable.ic_credit,
            R.drawable.ic_feedback,
            R.drawable.ic_opensource,
            R.drawable.ic_broom,
            R.drawable.ic_pp,
            R.drawable.ic_tos,
            R.drawable.ic_website,
            R.drawable.ic_invite,
            R.drawable.ic_rate,
            R.drawable.ic_info,
    };

    int[] external = new int[]{
            R.drawable.ic_external,
            R.drawable.ic_external,
            R.drawable.ic_external,
            R.drawable.ic_external,
            R.drawable.ic_external,
            R.drawable.ic_external,
            R.drawable.ic_external,
            R.drawable.ic_external,
            R.drawable.ic_external,
            R.drawable.ic_external};

    ListView listView;
    SimpleAdapter simpleAdapter;
    TextView greetings;

    int click = 0;

    @SuppressLint({"SetTextI18n", "ResourceAsColor"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Snippet.followNightModeInSystem();
        Snippet.layoutInDisplayCutoutMode(Settings.this);
        Snippet.darkTheme(Settings.this, getApplicationContext());
        setContentView(R.layout.activity_settings);

        getWindow().setNavigationBarColor(getResources().getColor(R.color.black));

        greetings = findViewById(R.id.name);
        Calendar calendar = Calendar.getInstance();
        int timeOfDay = calendar.get(Calendar.HOUR_OF_DAY);

        if (timeOfDay < 12) {
            greetings.setText("Good Morning");
        } else if (timeOfDay < 17) {
            greetings.setText("Good Afternoon");
        } else {
            greetings.setText("Good Evening");
        }

        Toolbar toolbar = findViewById(R.id.toolbar_settings);
        toolbar.setTitle(ApplicationConstant.SETTINGS);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        Snippet.toolbar(Settings.this, toolbar);
        setSupportActionBar(toolbar);

        List<HashMap<String, String>> hashMapArrayList = new ArrayList<HashMap<String, String>>();
        for (int x = 0; x <= 9; x++) {
            HashMap<String, String> hashMap = new HashMap<String, String>();
            hashMap.put("title", title[x]);
            hashMap.put("images", Integer.toString(images[x]));
            if (x == 4) {
                hashMap.put("external", Integer.toString(external[x]));
            }
            if (x == 5) {
                hashMap.put("external", Integer.toString(external[x]));
            }
            if (x == 6) {
                hashMap.put("external", Integer.toString(external[x]));
            }
            if (x == 8) {
                hashMap.put("external", Integer.toString(external[x]));
            }
            hashMapArrayList.add(hashMap);
        }
        String[] from = {
                "title", "images", "external"
        };
        int[] to = {
                R.id.settings_list_text, R.id.settings_list_image, R.id.settings_list_external
        };

        simpleAdapter = new SimpleAdapter(getBaseContext(), hashMapArrayList, R.layout.settings_list, from, to);
        listView = findViewById(R.id.settings_listview);
        listView.setDivider(null);
        listView.setDividerHeight(1);
        listView.setAdapter(simpleAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @SuppressLint("ResourceAsColor")
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    Intent intent0 = new Intent(view.getContext(), Credits.class);
                    startActivity(intent0);
                }
                if (position == 1) {
                    Intent intent1 = new Intent(view.getContext(), WriteFeedback.class);
                    startActivity(intent1);
                }
                if (position == 2) {
                    Intent intent2 = new Intent(view.getContext(), OpenSource.class);
                    startActivity(intent2);
                }
                if (position == 3) {
                    Widget.alertDialog(Settings.this,
                            true,
                            "Are you sure want to clear cache?\n\nNote: Please don't clear cache until or unless DepHub is running slow.",
                            ApplicationConstant.CLEAR_CACHE,
                            ApplicationConstant.CANCEL,
                            (dialog, which) -> {
                                deleteCache(getApplicationContext());
                                Widget.Toast(Settings.this, "Cache cleared successfully.");
                            },
                            (dialog, which) -> {
                                dialog.dismiss();
                            });
                }
                if (position == 4) {
                    Widget.openInBrowser(Settings.this, ApplicationConstant.PRIVACY_POLICY);
                }
                if (position == 5) {
                    Widget.openInBrowser(Settings.this, ApplicationConstant.TERMS_OF_SERVICE);
                }
                if (position == 6) {
                    Widget.openInBrowser(Settings.this, ApplicationConstant.WEBSITE);
                }
                if (position == 7) {
                    Intent intent7 = new Intent(Intent.ACTION_SEND);
                    intent7.setType("text/plain");
                    String shareBody2 = "About DepHub App";
                    String shareSub2 = "Hi there\n\nI found this new app called DepHub. It has a collection of Android Dependencies. You can find varieties of dependencies in this app and also the user interface is super friendly.\n\nThe most exciting features are every dependency is assigned with QR Code, Some dependencies are provided with a YouTube video link which gives information about how to implement them. You can submit your dependency too.\n\nDownload this Android App: https://bit.ly/installdephubapp\n\nIf any query, feel free to mail them: mailtodephub@gmail.com\n\nThank You";
                    intent7.putExtra(Intent.EXTRA_SUBJECT, shareBody2);
                    intent7.putExtra(Intent.EXTRA_TEXT, shareSub2);
                    startActivity(Intent.createChooser(intent7, "Invite to DepHub using"));
                }
                if (position == 8) {
                    Intent intent8 = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.dephub.android"));
                    startActivity(intent8);
                }
                if (position == 9) {
                    String versionName = BuildConfig.VERSION_NAME;
                    int versionCode = BuildConfig.VERSION_CODE;
                    String installer = "";

                    if (!verifyInstaller(getApplicationContext())) {
                        installer = "•Not Installed from Google Play";
                    }

                    @SuppressLint("SimpleDateFormat")
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy");
                    if (BuildConfig.DEBUG) {
                        Widget.alertDialog(Settings.this,
                                true,
                                "DepHub\nVersion v" + versionName + " (" + versionCode + ")\n•Development Version\n" + installer + "\n\nCopyright 2020-2024 DepHub",
                                null,
                                null,
                                null,
                                null);
                    } else {
                        Widget.alertDialog(Settings.this,
                                true,
                                "DepHub\nVersion v" + versionName + " (" + versionCode + ")\n" + installer + "\nCopyright 2020-2024 DepHub",
                                null,
                                null,
                                null,
                                null);
                    }
                }
            }
        });
    }

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

    boolean verifyInstaller(Context context) {
        List<String> validInstallers = new ArrayList<>(Collections.singletonList("com.android.vending"));
        final String installer = context.getPackageManager().getInstallerPackageName(context.getPackageName());
        return installer != null && validInstallers.contains(installer);
    }

    @Override
    protected void onDestroy() {
        click = 0;
        super.onDestroy();
    }
}