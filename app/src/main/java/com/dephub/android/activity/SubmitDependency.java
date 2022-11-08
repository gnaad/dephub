package com.dephub.android.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.dephub.android.R;
import com.dephub.android.common.Component;
import com.dephub.android.common.Snippet;

public class SubmitDependency extends AppCompatActivity {
    EditText dependencyDeveloperName, dependencyURL, dependencyDescription;
    Button SubmitNow;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Snippet.followNightModeInSystem();
        setContentView(R.layout.activity_submityourdependency);

        getWindow().setNavigationBarColor(getResources().getColor(R.color.black));

        Toolbar toolbar = findViewById(R.id.toolbarSubmitYourDependency);
        toolbar.setTitle("Submit Dependency");
        toolbar.setNavigationIcon(R.drawable.ic_back);
        Snippet.toolbar(SubmitDependency.this, toolbar);
        setSupportActionBar(toolbar);

        dependencyDeveloperName = findViewById(R.id.etDependencyName);
        dependencyURL = findViewById(R.id.etDependencyUrl);
        dependencyDescription = findViewById(R.id.etDependencyDesc);
        SubmitNow = findViewById(R.id.submitForm);

        SubmitNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dependencyName = dependencyDeveloperName.getText().toString().trim();
                String dependencyUrl = dependencyURL.getText().toString().trim();
                String dependencyDesc = dependencyDescription.getText().toString().trim();

                if (TextUtils.isEmpty(dependencyName)) {
                    showDialog("Please enter Dependency Name");
                } else if (TextUtils.isEmpty(dependencyUrl)) {
                    showDialog("Please include Dependency URL");
                } else if (TextUtils.isEmpty(dependencyDesc)) {
                    showDialog("Please include Dependency Description");
                } else if (!(dependencyUrl.startsWith("http://") || dependencyUrl.startsWith("https://"))) {
                    showDialog("Please include a valid Dependency URL starts with http or https");
                } else {
                    Component.alertDialog(SubmitDependency.this,
                            true,
                            "Are you sure, You want to submit all the details that you have entered?",
                            "Yes, Submit Now",
                            "No, I want to edit",
                            (dialog, which) -> {
                                Intent intent = new Intent(Intent.ACTION_SEND);
                                String[] mailto = {"mailtodephub@gmail.com"};
                                intent.putExtra(Intent.EXTRA_EMAIL, mailto);
                                intent.putExtra(Intent.EXTRA_SUBJECT, "Dependency Submission");
                                intent.putExtra(Intent.EXTRA_TEXT, "Hello\n\nI would like to submit a dependency for DepHub Android app. Details of dependency are below:" +
                                        "\n\n•Dependency Name : " + dependencyName + "\n•Dependency URL : " + dependencyUrl + "\n•Dependency Description : " + dependencyDesc + "\n\nPlease add this dependency in DepHub App.\n\nThank You");
                                intent.setType("message/rfc822");
                                startActivity(Intent.createChooser(intent, "Choose an email client"));
                            },
                            (dialog, which) -> {
                                dialog.dismiss();
                            });
                }
            }
        });

        Intent receivedIntent = getIntent();
        String receivedAction = receivedIntent.getAction();
        String receivedType = receivedIntent.getType();

        SharedPreferences prefs = getSharedPreferences("policy", MODE_PRIVATE);
        boolean firstStart = prefs.getBoolean("agreed", false);

        if (firstStart) {
            if ("android.intent.action.SEND".equals(receivedAction) && receivedType != null) {
                if (receivedType.startsWith("text/")) {

                    String receivedText = receivedIntent.getStringExtra(Intent.EXTRA_TEXT);
                    String[] splitText = receivedText.split("\\s+");
                    //noinspection ConstantConditions
                    if (receivedText != null) {
                        if (receivedText.startsWith("http://") || receivedText.startsWith("https://")) {
                            dependencyURL.setText(receivedText);
                        } else if (splitText.length >= 4) {
                            dependencyDescription.setText(receivedText);
                        } else {
                            dependencyDeveloperName.setText(receivedText);
                        }
                    }
                }
            }
        } else {
            startActivity(new Intent(SubmitDependency.this, Login.class));
            finish();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        String depName = dependencyDeveloperName.getText().toString().trim();
        String depUrl = dependencyURL.getText().toString().trim();
        String depDesc = dependencyDescription.getText().toString().trim();

        if (TextUtils.isEmpty(depName) && TextUtils.isEmpty(depUrl) && TextUtils.isEmpty(depDesc)) {
            onBackPressed();
        } else {
            backButton();
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        String depName = dependencyDeveloperName.getText().toString().trim();
        String depUrl = dependencyURL.getText().toString().trim();
        String depDesc = dependencyDescription.getText().toString().trim();

        if (TextUtils.isEmpty(depName) && TextUtils.isEmpty(depUrl) && TextUtils.isEmpty(depDesc)) {
            super.onBackPressed();
        } else {
            backButton();
        }
    }

    private void backButton() {
        Component.alertDialog(SubmitDependency.this,
                true,
                "Are you sure you want to go back?",
                "Yes",
                "No",
                (dialog, which) -> {
                    SubmitDependency.super.onBackPressed();
                },
                (dialog, which) -> {
                    dialog.dismiss();
                });
    }

    private void showDialog(String message) {
        Component.alertDialog(SubmitDependency.this,
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