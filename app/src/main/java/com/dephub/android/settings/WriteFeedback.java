package com.dephub.android.settings;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.dephub.android.BuildConfig;
import com.dephub.android.R;
import com.dephub.android.constant.ApplicationConstant;
import com.dephub.android.utility.Snippet;
import com.dephub.android.utility.Widget;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class WriteFeedback extends AppCompatActivity {

    Button btnSubmit, btnMailUs;
    EditText issue;
    Toolbar toolbar;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Snippet.followNightModeInSystem();
        Snippet.layoutInDisplayCutoutMode(WriteFeedback.this);
        Snippet.darkTheme(WriteFeedback.this, getApplicationContext());
        setContentView(R.layout.activity_write_feedback);

        getWindow().setNavigationBarColor(getResources().getColor(R.color.black));

        toolbar = findViewById(R.id.toolbar_write_feedback);
        toolbar.setTitle(ApplicationConstant.WRITE_FEEDBACK);
        Snippet.toolbar(WriteFeedback.this, toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        setSupportActionBar(toolbar);

        issue = findViewById(R.id.user_prob);
        btnSubmit = findViewById(R.id.feedback_submit);
        btnMailUs = findViewById(R.id.mail_us);

        Date date = new Date();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = dateFormat.format(date.getTime());

        Calendar calendar = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm:ss a");
        String time = simpleDateFormat.format(calendar.getTime());

        String model = Build.MODEL;
        int version = Build.VERSION.SDK_INT;
        String releaseVersion = Build.VERSION.RELEASE;
        String versionName = BuildConfig.VERSION_NAME;
        int versionCode = BuildConfig.VERSION_CODE;
        int width = Resources.getSystem().getDisplayMetrics().widthPixels;
        int height = Resources.getSystem().getDisplayMetrics().heightPixels;

        btnMailUs.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_SEND);
            String[] mailto = {"mailtodephub@gmail.com"};
            intent.putExtra(Intent.EXTRA_EMAIL, mailto);
            intent.putExtra(Intent.EXTRA_SUBJECT, "Feedback for DepHub App submitted on " + formattedDate + " at " + time);
            intent.putExtra(Intent.EXTRA_TEXT, "Model : " + model + "\nSDK Version : " + version + "\nAndroid Version : " + releaseVersion + "\nVersion Name : " + versionName + "\nVersion Code : " + versionCode + "\nHeight : " + height + " pixels" + "\nWidth : " + width + " pixels\n\n-- Please don't edit anything above this line, it helps us to serve you better --" +
                    "\n\nHello\n\nI would like to say:\n");
            intent.setType("message/rfc822");
            startActivity(Intent.createChooser(intent, "Choose an email client"));
        });

        btnSubmit.setOnClickListener(v -> {
            String problem = issue.getText().toString().trim();
            String[] wordCount = problem.split("\\s+");

            if (TextUtils.isEmpty(problem)) {
                showAlertDialog("Please describe your problem");
            } else if (wordCount.length < 10) {
                showAlertDialog("Please describe your problem at least using ten words.");
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
                            intent.putExtra(Intent.EXTRA_TEXT, "Model : " + model + "\nSDK Version : " + version + "\nAndroid Version : " + releaseVersion + "\nVersion Name : " + versionName + "\nVersion Code : " + versionCode + "\nHeight : " + height + " pixels" + "\nWidth : " + width + " pixels\n\n-- Please don't edit anything above this line, it helps us to serve you better --" +
                                    "\n\nHello\n\nI would like to say:\n\n" + problem);
                            intent.setType("message/rfc822");
                            startActivity(Intent.createChooser(intent, "Choose an email client"));
                        },
                        (dialog, which) -> {
                            dialog.dismiss();
                        });
            }
        });
    }

    @Override
    public void onBackPressed() {
        String problemInput = issue.getText().toString().trim();
        if (TextUtils.isEmpty(problemInput)) {
            super.onBackPressed();
        } else {
            backButtonPressed();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        String problemInput = issue.getText().toString().trim();
        if (TextUtils.isEmpty(problemInput)) {
            super.onBackPressed();
        } else {
            backButtonPressed();
        }
        return false;
    }

    private void backButtonPressed() {
        Widget.alertDialog(WriteFeedback.this,
                true,
                "Are you sure you want to go back?",
                ApplicationConstant.YES,
                ApplicationConstant.NO,
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
                ApplicationConstant.OK,
                null,
                (dialog, which) -> {
                    dialog.dismiss();
                },
                null);
    }
}