package com.dephub.android.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
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

import com.dephub.android.R;

public class SubmitDependency extends AppCompatActivity {
    EditText Dependency_Name, Dependency_URL, Dependency_Description;
    Button Submitnow;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);

        setContentView(R.layout.activity_submityourdependency);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow( ).setNavigationBarColor(getResources( ).getColor(R.color.black));
        }

        Toolbar toolbar = findViewById(R.id.toolbarsubmityourdependency);
        toolbar.setTitle("Submit Dependency");
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

        Dependency_Name = findViewById(R.id.etdepname);
        Dependency_URL = findViewById(R.id.etdepurl);
        Dependency_Description = findViewById(R.id.etdepdes);
        Submitnow = findViewById(R.id.formsubmit);

        Submitnow.setOnClickListener(new View.OnClickListener( ) {
            @Override
            public void onClick(View v) {
                String depname = Dependency_Name.getText( ).toString( ).trim( );
                String depurl = Dependency_URL.getText( ).toString( ).trim( );
                String depdes = Dependency_Description.getText( ).toString( ).trim( );

                if (TextUtils.isEmpty(depname)) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SubmitDependency.this,R.style.CustomAlertDialog);

                    alertDialogBuilder.setCancelable(true);
                    alertDialogBuilder.setMessage("Please enter Dependency Name");
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
                if (TextUtils.isEmpty(depurl)) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SubmitDependency.this,R.style.CustomAlertDialog);

                    alertDialogBuilder.setCancelable(true);
                    alertDialogBuilder.setMessage("Please include Dependency URL");
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
                if (TextUtils.isEmpty(depdes)) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SubmitDependency.this,R.style.CustomAlertDialog);

                    alertDialogBuilder.setCancelable(true);
                    alertDialogBuilder.setMessage("Please include Dependency Description");
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
                if (!(depurl.startsWith("http://") || depurl.startsWith("https://"))) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SubmitDependency.this,R.style.CustomAlertDialog);

                    alertDialogBuilder.setCancelable(true);
                    alertDialogBuilder.setMessage("Please include a valid Dependency URL starts with http or https");
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
                } else {

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SubmitDependency.this,R.style.CustomAlertDialog);

                    alertDialogBuilder.setCancelable(true);
                    alertDialogBuilder.setMessage("Are you sure, You want to submit all the details that you have entered?");
                    alertDialogBuilder.setPositiveButton("Yes, Submit Now",new DialogInterface.OnClickListener( ) {
                        public void onClick(DialogInterface dialog,int id) {

                            Intent intent = new Intent(Intent.ACTION_SEND);
                            String[] mailto = {"mailtodephub@gmail.com"};
                            intent.putExtra(Intent.EXTRA_EMAIL,mailto);
                            intent.putExtra(Intent.EXTRA_SUBJECT,"Dependency Submission");
                            intent.putExtra(Intent.EXTRA_TEXT,"Hello\n\nI would like to submit a dependency for DepHub Android app. Details of dependency are below:" +
                                    "\n\n•Dependency Name : " + depname + "\n•Dependency URL : " + depurl + "\n•Dependency Description : " + depdes + "\n\nPlease add this dependency in DepHub App.\n\nThank You");
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

        Intent receiverdIntent = getIntent( );
        String receivedAction = receiverdIntent.getAction( );
        String receivedType = receiverdIntent.getType( );

        SharedPreferences prefs = getSharedPreferences("policy",MODE_PRIVATE);
        boolean firstStart = prefs.getBoolean("agreed",false);

        if (firstStart) {
            if ("android.intent.action.SEND".equals(receivedAction) && receivedType != null) {
                if (receivedType.startsWith("text/")) {

                    String receivedText = receiverdIntent.getStringExtra(Intent.EXTRA_TEXT);
                    String[] splittedtext = receivedText.split("\\s+");
                    //noinspection ConstantConditions
                    if (receivedText != null) {
                        if (receivedText.startsWith("http://") || receivedText.startsWith("https://")) {
                            Dependency_URL.setText(receivedText);
                        } else if (splittedtext.length >= 4) {
                            Dependency_Description.setText(receivedText);
                        } else {
                            Dependency_Name.setText(receivedText);
                        }
                    }
                }
            }
        } else {
            startActivity(new Intent(SubmitDependency.this,Login.class));
            finish( );
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        String depname = Dependency_Name.getText( ).toString( ).trim( );
        String depurl = Dependency_URL.getText( ).toString( ).trim( );
        String depdesc = Dependency_Description.getText( ).toString( ).trim( );

        if (TextUtils.isEmpty(depname) && TextUtils.isEmpty(depurl) && TextUtils.isEmpty(depdesc)) {
            onBackPressed( );
        } else {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SubmitDependency.this,R.style.CustomAlertDialog);
            alertDialogBuilder.setCancelable(true);
            alertDialogBuilder.setMessage("Are you sure you want to go back?");
            alertDialogBuilder.setPositiveButton("Yes",new DialogInterface.OnClickListener( ) {
                public void onClick(DialogInterface dialog,int id) {
                    SubmitDependency.super.onBackPressed( );
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
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        String depname = Dependency_Name.getText( ).toString( ).trim( );
        String depurl = Dependency_URL.getText( ).toString( ).trim( );
        String depdesc = Dependency_Description.getText( ).toString( ).trim( );

        if (TextUtils.isEmpty(depname) && TextUtils.isEmpty(depurl) && TextUtils.isEmpty(depdesc)) {
            super.onBackPressed( );
        } else {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SubmitDependency.this,R.style.CustomAlertDialog);
            alertDialogBuilder.setCancelable(true);
            alertDialogBuilder.setMessage("Are you sure you want to go back?");
            alertDialogBuilder.setPositiveButton("Yes",new DialogInterface.OnClickListener( ) {
                public void onClick(DialogInterface dialog,int id) {
                    SubmitDependency.super.onBackPressed( );
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
}
