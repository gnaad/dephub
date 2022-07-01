package com.dephub.android.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import com.dephub.android.BuildConfig;
import com.dephub.android.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class WriteFeedback extends AppCompatActivity {

    private EditText Problem;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);

        setContentView(R.layout.activity_writefeedback);

        getWindow().setNavigationBarColor(getResources().getColor(R.color.black));

        Toolbar toolbar = findViewById(R.id.toolbarWriteFeedback);
        toolbar.setTitle("Write Feedback");
        int nightModeFlags = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
            int white = Color.parseColor("#ffffff");
            toolbar.setTitleTextColor(white);
        } else {
            int black = Color.parseColor("#000000");
            toolbar.setTitleTextColor(black);
        }
        toolbar.setNavigationIcon(R.drawable.ic_back);
        setSupportActionBar(toolbar);


        Problem = findViewById(R.id.userProb);
        Button btnSubmit = findViewById(R.id.feedbackSubmit);
        Button btnMailUs = findViewById(R.id.mailUs);

        Date date = new Date();
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = df.format(date.getTime());

        Calendar calander = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm:ss a");
        String time = simpleDateFormat.format(calander.getTime());

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
                String problemInput = Problem.getText().toString().trim();

                if (TextUtils.isEmpty(problemInput)) {
                    showAlertDialog("Please describe your problem");
                    return;
                }
                String[] wordCount = problemInput.split("\\s+");
                if (wordCount.length < 10) {
                    showAlertDialog("Please describe your problem atleast using ten words.");
                } else {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(WriteFeedback.this, R.style.CustomAlertDialog);
                    alertDialogBuilder.setCancelable(true);
                    alertDialogBuilder.setMessage("Are you sure, You want to submit the feedback that you have entered?");
                    alertDialogBuilder.setPositiveButton("Yes, Submit Now", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            Intent intent = new Intent(Intent.ACTION_SEND);
                            String[] mailto = {"mailtodephub@gmail.com"};
                            intent.putExtra(Intent.EXTRA_EMAIL, mailto);
                            intent.putExtra(Intent.EXTRA_SUBJECT, "Write Feedback");
                            intent.putExtra(Intent.EXTRA_TEXT, "Model : " + model + "\nSDK Version : " + version + "\nAndroid Version : " + versionRelease + "\nVersion Name : " + versionName + "\nVersion Code : " + versioncode + "\nHeight : " + height + " pixels" + "\nWidth : " + width + " pixels\n\n-- Please don't edit anything above this line, it helps us to serve you better --" +
                                    "\n\nHello\n\nI would like to say:\n\n" + problemInput);
                            intent.setType("message/rfc822");
                            startActivity(Intent.createChooser(intent, "Choose an email client"));

                        }
                    });
                    alertDialogBuilder.setNegativeButton("No, I want to edit", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                    alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorAccent));
                    alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorAccent));
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        String problemInput = Problem.getText().toString().trim();

        if (TextUtils.isEmpty(problemInput)) {
            super.onBackPressed();
        } else {
            backButtonPressed();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        String problemInput = Problem.getText().toString().trim();

        if (TextUtils.isEmpty(problemInput)) {
            super.onBackPressed();
        } else {
            backButtonPressed();
        }
        return false;
    }

    private void backButtonPressed() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(WriteFeedback.this, R.style.CustomAlertDialog);
        alertDialogBuilder.setCancelable(true);
        alertDialogBuilder.setMessage("Are you sure you want to go back?");
        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                WriteFeedback.super.onBackPressed();
            }
        });
        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorAccent));
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorAccent));
    }

    private void showAlertDialog(String s) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(WriteFeedback.this, R.style.CustomAlertDialog);
        alertDialogBuilder.setCancelable(true);
        alertDialogBuilder.setMessage(s);
        alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorAccent));
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorAccent));
        return;
    }
}