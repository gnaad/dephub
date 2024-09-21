package com.dephub.android.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.dephub.android.R;
import com.dephub.android.constant.ApplicationConstant;
import com.dephub.android.utility.Snippet;
import com.dephub.android.utility.Widget;

public class SubmitDependency extends AppCompatActivity {
    Button submitNow;
    EditText dependencyName, dependencyURL, dependencyDesc;
    String depName, depURL, depDesc;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Snippet.followNightModeInSystem();
        Snippet.layoutInDisplayCutoutMode(SubmitDependency.this);
        Snippet.darkTheme(SubmitDependency.this, getApplicationContext());
        setContentView(R.layout.activity_submit_your_dependency);

        getWindow().setNavigationBarColor(getResources().getColor(R.color.black));

        Toolbar toolbar = findViewById(R.id.toolbar_submit_your_dependency);
        toolbar.setTitle(ApplicationConstant.SUBMIT_DEPENDENCY);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        Snippet.toolbar(SubmitDependency.this, toolbar);
        setSupportActionBar(toolbar);

        dependencyName = findViewById(R.id.et_dependency_name);
        dependencyURL = findViewById(R.id.et_dependency_url);
        dependencyDesc = findViewById(R.id.et_dependency_desc);
        submitNow = findViewById(R.id.submit_form);

        submitNow.setOnClickListener(v -> {
            depName = dependencyName.getText().toString().trim();
            depURL = dependencyURL.getText().toString().trim();
            depDesc = dependencyDesc.getText().toString().trim();

            if (TextUtils.isEmpty(depName)) {
                showDialog(ApplicationConstant.ALERT_DEPENDENCY_NAME);
            } else if (TextUtils.isEmpty(depURL)) {
                showDialog(ApplicationConstant.ALERT_DEPENDENCY_URL);
            } else if (TextUtils.isEmpty(depDesc)) {
                showDialog(ApplicationConstant.ALERT_DEPENDENCY_DESCRIPTION);
            } else if (!(depURL.startsWith(ApplicationConstant.HTTP) || depURL.startsWith(ApplicationConstant.HTTPS))) {
                showDialog(ApplicationConstant.ALERT_DEPENDENCY_FORMATTED_URL);
            } else {
                Widget.alertDialog(SubmitDependency.this,
                        true,
                        ApplicationConstant.ALERT_DEPENDENCY_MESSAGE,
                        ApplicationConstant.ALERT_DEPENDENCY_POSITIVE_BUTTON,
                        ApplicationConstant.ALERT_DEPENDENCY_NEGATIVE_BUTTON,
                        (dialog, which) -> {
                            Intent intent = new Intent(Intent.ACTION_SEND);
                            intent.putExtra(Intent.EXTRA_EMAIL, ApplicationConstant.MAIL_TO);
                            intent.putExtra(Intent.EXTRA_SUBJECT, ApplicationConstant.DEPENDENCY_SUBMISSION);
                            intent.putExtra(Intent.EXTRA_TEXT, "Hello\n\nI would like to submit a dependency for DepHub Android app. Details of dependency are below:" +
                                    "\n\n•Dependency Name : " + dependencyName + "\n•Dependency URL : " + dependencyURL + "\n•Dependency Description : " + dependencyDesc + "\n\nPlease add this dependency in DepHub App.\n\nThank You");
                            intent.setType(ApplicationConstant.MESSAGE_RFC);
                            startActivity(Intent.createChooser(intent, ApplicationConstant.EMAIL_CLIENT));
                        },
                        (dialog, which) -> {
                            dialog.dismiss();
                        });
            }
        });

        Intent receivedIntent = getIntent();
        String receivedAction = receivedIntent.getAction();
        String receivedType = receivedIntent.getType();

        SharedPreferences prefs = getSharedPreferences(ApplicationConstant.POLICY, MODE_PRIVATE);
        boolean firstStart = prefs.getBoolean(ApplicationConstant.AGREED, false);

        if (firstStart) {
            if (ApplicationConstant.ACTION_SEND.equals(receivedAction) && receivedType != null) {
                if (receivedType.startsWith("text/")) {
                    String receivedText = receivedIntent.getStringExtra(Intent.EXTRA_TEXT);
                    if (receivedText != null) {
                        String[] splitText = receivedText.split("\\s+");
                        if (receivedText.startsWith(ApplicationConstant.HTTP) || receivedText.startsWith(ApplicationConstant.HTTPS)) {
                            dependencyURL.setText(receivedText);
                        } else if (splitText.length >= 4) {
                            dependencyDesc.setText(receivedText);
                        } else {
                            dependencyName.setText(receivedText);
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
        depName = dependencyName.getText().toString().trim();
        depURL = dependencyURL.getText().toString().trim();
        depDesc = dependencyDesc.getText().toString().trim();

        if (TextUtils.isEmpty(depName) && TextUtils.isEmpty(depURL) && TextUtils.isEmpty(depDesc)) {
            onBackPressed();
        } else {
            backButton();
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        depName = dependencyName.getText().toString().trim();
        depURL = dependencyURL.getText().toString().trim();
        depDesc = dependencyDesc.getText().toString().trim();

        if (TextUtils.isEmpty(depName) && TextUtils.isEmpty(depURL) && TextUtils.isEmpty(depDesc)) {
            super.onBackPressed();
        } else {
            backButton();
        }
    }

    private void backButton() {
        Widget.alertDialog(SubmitDependency.this,
                true,
                ApplicationConstant.GO_BACK_MESSAGE,
                ApplicationConstant.YES,
                ApplicationConstant.NO,
                (dialog, which) -> {
                    SubmitDependency.super.onBackPressed();
                },
                (dialog, which) -> {
                    dialog.dismiss();
                });
    }

    private void showDialog(String message) {
        Widget.alertDialog(SubmitDependency.this,
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