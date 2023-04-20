package com.dephub.android.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.dephub.android.BuildConfig;
import com.dephub.android.R;
import com.dephub.android.utility.Snippet;
import com.dephub.android.utility.Widget;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class WriteFeedback extends AppCompatActivity {

    private EditText Issue;
    private InterstitialAd WriteFeedbackInterstitialAd;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Snippet.followNightModeInSystem();
        setContentView(R.layout.activity_writefeedback);

        getWindow().setNavigationBarColor(getResources().getColor(R.color.black));

        Toolbar toolbar = findViewById(R.id.toolbarWriteFeedback);
        toolbar.setTitle("Write Feedback");
        Snippet.toolbar(WriteFeedback.this, toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        setSupportActionBar(toolbar);

        Issue = findViewById(R.id.userProb);
        Button btnSubmit = findViewById(R.id.feedbackSubmit);
        Button btnMailUs = findViewById(R.id.mailUs);

        Date date = new Date();
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = dateFormat.format(date.getTime());

        Calendar calendar = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm:ss a");
        String time = simpleDateFormat.format(calendar.getTime());

        String model = Build.MODEL;
        int version = Build.VERSION.SDK_INT;
        String versionRelease = Build.VERSION.RELEASE;
        String versionName = BuildConfig.VERSION_NAME;
        int versioncode = BuildConfig.VERSION_CODE;
        int width = Resources.getSystem().getDisplayMetrics().widthPixels;
        int height = Resources.getSystem().getDisplayMetrics().heightPixels;

        btnMailUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                String[] mailto = {"mailtodephub@gmail.com"};
                intent.putExtra(Intent.EXTRA_EMAIL, mailto);
                intent.putExtra(Intent.EXTRA_SUBJECT, "Feedback for DepHub App submitted on " + formattedDate + " at " + time);
                intent.putExtra(Intent.EXTRA_TEXT, "Model : " + model + "\nSDK Version : " + version + "\nAndroid Version : " + versionRelease + "\nVersion Name : " + versionName + "\nVersion Code : " + versioncode + "\nHeight : " + height + " pixels" + "\nWidth : " + width + " pixels\n\n-- Please don't edit anything above this line, it helps us to serve you better --" +
                        "\n\nHello\n\nI would like to say:\n");
                intent.setType("message/rfc822");
                startActivity(Intent.createChooser(intent, "Choose an email client"));
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String problemInput = Issue.getText().toString().trim();
                String[] wordCount = problemInput.split("\\s+");

                if (TextUtils.isEmpty(problemInput)) {
                    showAlertDialog("Please describe your problem");
                } else if (wordCount.length < 10) {
                    showAlertDialog("Please describe your problem atleast using ten words.");
                } else {
                    Widget.alertDialog(WriteFeedback.this,
                            true,
                            "Are you sure, You want to submit the feedback that you have entered?",
                            "Yes, Submit Now",
                            "No, I want to edit",
                            (dialog, which) -> {
                                Intent intent = new Intent(Intent.ACTION_SEND);
                                String[] mailto = {"mailtodephub@gmail.com"};
                                intent.putExtra(Intent.EXTRA_EMAIL, mailto);
                                intent.putExtra(Intent.EXTRA_SUBJECT, "Write Feedback");
                                intent.putExtra(Intent.EXTRA_TEXT, "Model : " + model + "\nSDK Version : " + version + "\nAndroid Version : " + versionRelease + "\nVersion Name : " + versionName + "\nVersion Code : " + versioncode + "\nHeight : " + height + " pixels" + "\nWidth : " + width + " pixels\n\n-- Please don't edit anything above this line, it helps us to serve you better --" +
                                        "\n\nHello\n\nI would like to say:\n\n" + problemInput);
                                intent.setType("message/rfc822");
                                startActivity(Intent.createChooser(intent, "Choose an email client"));
                            },
                            (dialog, which) -> {
                                dialog.dismiss();
                            });
                }
            }
        });

        Snippet.initializeInterstitialAd(WriteFeedback.this);
        AdRequest writeFeedbackAdRequest = new AdRequest.Builder().build();

        Widget.showInterstitialAd(this, "ca-app-pub-3037529522611130/3494136300", writeFeedbackAdRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                WriteFeedback.super.onBackPressed();
            }

            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                WriteFeedbackInterstitialAd = interstitialAd;

                interstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        WriteFeedback.super.onBackPressed();
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                        WriteFeedback.super.onBackPressed();
                    }
                });
            }
        });
    }

    @Override
    public void onBackPressed() {
        String problemInput = Issue.getText().toString().trim();
        if (TextUtils.isEmpty(problemInput)) {
            if (WriteFeedbackInterstitialAd != null) {
                WriteFeedbackInterstitialAd.show(WriteFeedback.this);
            } else {
                super.onBackPressed();
            }
        } else {
            backButtonPressed();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        String problemInput = Issue.getText().toString().trim();
        if (TextUtils.isEmpty(problemInput)) {
            if (WriteFeedbackInterstitialAd != null) {
                WriteFeedbackInterstitialAd.show(WriteFeedback.this);
            } else {
                super.onBackPressed();
            }
        } else {
            backButtonPressed();
        }
        return false;
    }

    private void backButtonPressed() {
        Widget.alertDialog(WriteFeedback.this,
                true,
                "Are you sure you want to go back?",
                "Yes",
                "No",
                (dialog, which) -> {
                    WriteFeedback.super.onBackPressed();
                },
                (dialog, which) -> {
                    dialog.dismiss();
                });
    }

    private void showAlertDialog(String message) {
        Widget.alertDialog(WriteFeedback.this,
                true,
                message,
                "Ok",
                null,
                (dialog, which) -> {
                    dialog.dismiss();
                },
                null);
    }
}