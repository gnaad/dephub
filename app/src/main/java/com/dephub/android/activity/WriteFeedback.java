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
import com.dephub.android.settings.Help;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class WriteFeedback extends AppCompatActivity {

    private EditText Problem;
    private Button btnsubmit, btnmailus;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);

        setContentView(R.layout.activity_writefeedback);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow( ).setNavigationBarColor(getResources( ).getColor(R.color.black));
        }

        Toolbar toolbar = findViewById(R.id.toolbarwritefeedback);
        toolbar.setTitle("Write Feedback");
        int nightModeFlags = getResources( ).getConfiguration( ).uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
            int white = Color.parseColor("#ffffff");
            toolbar.setTitleTextColor(white);
        } else {
            int black = Color.parseColor("#000000");
            toolbar.setTitleTextColor(black);
        }
        toolbar.setNavigationIcon(R.drawable.ic_back);
        setSupportActionBar(toolbar);


        Problem = findViewById(R.id.nhwuprob);
        btnsubmit = findViewById(R.id.nhwusubmit);
        btnmailus = findViewById(R.id.mailus);

        Date date = new Date( );
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = df.format(date.getTime( ));

        Calendar calander = Calendar.getInstance( );
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm:ss a");
        String time = simpleDateFormat.format(calander.getTime( ));

        String model = Build.MODEL;
        int version = Build.VERSION.SDK_INT;
        String versionRelease = Build.VERSION.RELEASE;
        String versionName = BuildConfig.VERSION_NAME;
        int versioncode = BuildConfig.VERSION_CODE;
        int width = Resources.getSystem( ).getDisplayMetrics( ).widthPixels;
        int height = Resources.getSystem( ).getDisplayMetrics( ).heightPixels;

        btnmailus.setOnClickListener(new View.OnClickListener( ) {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                String[] mailto = {"mailtodephub@gmail.com"};
                intent.putExtra(Intent.EXTRA_EMAIL,mailto);
                intent.putExtra(Intent.EXTRA_SUBJECT,"Feedback for DepHub App submitted on " + formattedDate + " at " + time);
                intent.putExtra(Intent.EXTRA_TEXT,"Model : " + model + "\nSDK Version : " + version + "\nAndroid Version : " + versionRelease + "\nVersion Name : " + versionName + "\nVersion Code : " + versioncode + "\nHeight : " + height + " pixels" + "\nWidth : " + width + " pixels\n\n-- Please don't edit anything above this line, it helps us to serve you better --" +
                        "\n\nHello\n\nI would like to say:\n");
                intent.setType("message/rfc822");
                startActivity(Intent.createChooser(intent,"Choose an email client"));
            }
        });

        btnsubmit.setOnClickListener(new View.OnClickListener( ) {
            @Override
            public void onClick(View v) {
                String probleminput = Problem.getText( ).toString( ).trim( );

                if (TextUtils.isEmpty(probleminput)) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(WriteFeedback.this,R.style.CustomAlertDialog);

                    alertDialogBuilder.setCancelable(true);
                    alertDialogBuilder.setMessage("Please describe your problem");
                    alertDialogBuilder.setPositiveButton("Ok",new DialogInterface.OnClickListener( ) {
                        public void onClick(DialogInterface dialog,int id) {
                            dialog.dismiss( );
                        }
                    });
                    AlertDialog alertDialog = alertDialogBuilder.create( );
                    alertDialog.show( );
                    alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources( ).getColor(R.color.colorAccent));
                    alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources( ).getColor(R.color.colorAccent));
                    return;
                }
                String[] wordcount = probleminput.split("\\s+");
                if (wordcount.length < 10) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(WriteFeedback.this,R.style.CustomAlertDialog);

                    alertDialogBuilder.setCancelable(true);
                    alertDialogBuilder.setMessage("Please describe your problem atleast using ten words.");
                    alertDialogBuilder.setPositiveButton("Ok",new DialogInterface.OnClickListener( ) {
                        public void onClick(DialogInterface dialog,int id) {
                            dialog.dismiss( );
                        }
                    });
                    AlertDialog alertDialog = alertDialogBuilder.create( );
                    alertDialog.show( );
                    alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources( ).getColor(R.color.colorAccent));
                    alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources( ).getColor(R.color.colorAccent));
                } else {

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(WriteFeedback.this,R.style.CustomAlertDialog);

                    alertDialogBuilder.setCancelable(true);
                    alertDialogBuilder.setMessage("Are you sure, You want to submit the feedback that you have entered?");
                    alertDialogBuilder.setPositiveButton("Yes, Submit Now",new DialogInterface.OnClickListener( ) {
                        public void onClick(DialogInterface dialog,int id) {

                            Intent intent = new Intent(Intent.ACTION_SEND);
                            String[] mailto = {"mailtodephub@gmail.com"};
                            intent.putExtra(Intent.EXTRA_EMAIL,mailto);
                            intent.putExtra(Intent.EXTRA_SUBJECT,"Write Feedback");
                            intent.putExtra(Intent.EXTRA_TEXT,"Model : " + model + "\nSDK Version : " + version + "\nAndroid Version : " + versionRelease + "\nVersion Name : " + versionName + "\nVersion Code : " + versioncode + "\nHeight : " + height + " pixels" + "\nWidth : " + width + " pixels\n\n-- Please don't edit anything above this line, it helps us to serve you better --" +
                                    "\n\nHello\n\nI would like to say:\n\n" + probleminput);
                            intent.setType("message/rfc822");
                            startActivity(Intent.createChooser(intent,"Choose an email client"));

                        }
                    });
                    alertDialogBuilder.setNegativeButton("No, I want to edit",new DialogInterface.OnClickListener( ) {
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
            }
        });
    }

    @Override
    public void onBackPressed() {
        String probleminput = Problem.getText( ).toString( ).trim( );

        if (TextUtils.isEmpty(probleminput)) {
            super.onBackPressed( );
        } else {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(WriteFeedback.this,R.style.CustomAlertDialog);
            alertDialogBuilder.setCancelable(true);
            alertDialogBuilder.setMessage("Are you sure you want to go back?");
            alertDialogBuilder.setPositiveButton("Yes",new DialogInterface.OnClickListener( ) {
                public void onClick(DialogInterface dialog,int id) {
                    startActivity(new Intent(WriteFeedback.this,Help.class));
                }
            });
            alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener( ) {
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
    }

    @Override
    public boolean onSupportNavigateUp() {
        String probleminput = Problem.getText( ).toString( ).trim( );

        if (TextUtils.isEmpty(probleminput)) {
            super.onBackPressed( );
        } else {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(WriteFeedback.this,R.style.CustomAlertDialog);
            alertDialogBuilder.setCancelable(true);
            alertDialogBuilder.setMessage("Are you sure you want to go back?");
            alertDialogBuilder.setPositiveButton("Yes",new DialogInterface.OnClickListener( ) {
                public void onClick(DialogInterface dialog,int id) {
                    startActivity(new Intent(WriteFeedback.this,Help.class));
                }
            });
            alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener( ) {
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
        return false;
    }
}