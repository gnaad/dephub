package com.dephub.android.settings;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.dephub.android.R;
import com.dephub.android.utility.Widget;
import com.dephub.android.utility.Snippet;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class Main extends AppCompatActivity {
    String[] ListViewTitle = new String[]{
            "Help",
            "View Website",
            "Invite a Friend"};

    String[] ListViewDescription = new String[]{
            "Feedback, Policies, App Info",
            "View our official website",
            "Tell them about DepHub"};

    int[] images = new int[]{
            R.drawable.ic_help,
            R.drawable.ic_website,
            R.drawable.ic_invite};

    int[] external = new int[]{
            R.drawable.ic_external,
            R.drawable.ic_external,
            R.drawable.ic_external};

    TextView txt;
    int click = 0;

    @SuppressLint({"SetTextI18n", "ResourceAsColor"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Snippet.followNightModeInSystem();
        setContentView(R.layout.activity_settings_options);

        getWindow().setNavigationBarColor(getResources().getColor(R.color.black));

        AdView mAdView = findViewById(R.id.adOptions);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mAdView.setAdListener(new AdListener() {
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

        txt = findViewById(R.id.name);
        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

        txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click++;
                if (click > 10) {
                    FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
                        @Override
                        public void onComplete(@NonNull Task<String> task) {
                            if (!task.isSuccessful()) {
                                Widget.Toast(Main.this, "You haven't subscribed to Notifications." + task.getException());
                            }
                            String token = task.getResult();
                            Snippet.vibrate(Main.this, token);
                            Widget.Toast(Main.this, "You have subscribed to Notifications.");
                        }
                    });
                }
            }
        });

        //noinspection ConstantConditions
        if (timeOfDay >= 0 && timeOfDay < 12) {
            txt.setText("Good Morning");
        } else if (timeOfDay >= 12 && timeOfDay < 17) {
            txt.setText("Good Afternoon");
        } else if (timeOfDay >= 17 && timeOfDay < 24) {
            txt.setText("Good Evening");
        }

        Toolbar toolbar = findViewById(R.id.toolbarSettings);
        toolbar.setTitle("Settings");
        toolbar.setNavigationIcon(R.drawable.ic_back);
        Snippet.toolbar(Main.this, toolbar);
        setSupportActionBar(toolbar);

        List<HashMap<String, String>> hashMapArrayList = new ArrayList<HashMap<String, String>>();
        for (int x = 0; x <= 2; x++) {
            HashMap<String, String> hashMap = new HashMap<String, String>();
            hashMap.put("ListTitle", ListViewTitle[x]);
            hashMap.put("ListDescription", ListViewDescription[x]);
            hashMap.put("ListImages", Integer.toString(images[x]));
            if (x == 2) {
                hashMap.put("ListImagesExternal", Integer.toString(external[x]));
            }
            hashMapArrayList.add(hashMap);
        }
        String[] from = {
                "ListTitle", "ListDescription", "ListImages", "ListImagesExternal"
        };
        int[] to = {
                R.id.listview_text, R.id.listview_description, R.id.listviewimage, R.id.listviewexternal
        };

        SimpleAdapter simpleAdapter = new SimpleAdapter(getBaseContext(), hashMapArrayList, R.layout.listview_items_with_ext_desc, from, to);
        ListView listView = findViewById(R.id.settingsListview);
        listView.setDivider(null);
        listView.setDividerHeight(1);
        listView.setAdapter(simpleAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @SuppressLint("ResourceAsColor")
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    Intent intent = new Intent(view.getContext(), Help.class);
                    startActivity(intent);
                }
                if (position == 1) {
                    String website = "https://gnanendraprasadp.github.io/dephub";
                    Widget.openInBrowser(Main.this, website);
                }
                if (position == 2) {
                    Intent intent52 = new Intent(Intent.ACTION_SEND);
                    intent52.setType("text/plain");
                    String shareBody2 = "About DepHub App";
                    String shareSub2 = "Hi there\n\nI found this new app called DepHub. It has a collection of Android Dependencies. You can find varieties of dependencies in this app and also the user interface is super friendly.\n\nThe most exciting features are every dependency is assigned with QR Code, Some dependencies are provided with a YouTube video link which gives information about how to implement them. You can submit your dependency too.\n\nDownload this Android App: https://bit.ly/installdephubapp\n\nIf any query, feel free to mail them: mailtodephub@gmail.com\n\nThank You";
                    intent52.putExtra(Intent.EXTRA_SUBJECT, shareBody2);
                    intent52.putExtra(Intent.EXTRA_TEXT, shareSub2);
                    startActivity(Intent.createChooser(intent52, "Invite to DepHub using"));
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        click = 0;
        super.onDestroy();
    }
}