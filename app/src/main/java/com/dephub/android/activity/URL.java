package com.dephub.android.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.dephub.android.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class URL extends AppCompatActivity {
    ProgressBar progressBar;
    TextView textView, home;
    String id;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_url);

        progressBar = findViewById(R.id.progress);
        textView = findViewById(R.id.loading);
        home = findViewById(R.id.goToHome);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(URL.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        SharedPreferences prefs = getSharedPreferences("policy", MODE_PRIVATE);
        boolean firstStart = prefs.getBoolean("agreed", false);

        if (firstStart) {
            Uri uri = getIntent().getData();
            if (uri != null) {
                String parameters = uri.getLastPathSegment();
                if (parameters == null) {
                    textView.setText("URL doesn't exist");
                    progressBar.setVisibility(View.INVISIBLE);
                    home.setVisibility(View.VISIBLE);
                } else {
                    if (parameters.equals("") || parameters.isEmpty() || parameters.length() == 0) {
                        textView.setText("Dependency parameters is missing");
                        progressBar.setVisibility(View.INVISIBLE);
                        home.setVisibility(View.VISIBLE);
                    } else {
                        if (TextUtils.isDigitsOnly(parameters)) {
                            if (parameters.length() == 4 || parameters.length() == 3) {
                                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                                requestQueue.getCache().clear();
                                JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, "https://gnaad.github.io/dephub/json/dependency.json", null, new Response.Listener<JSONArray>() {

                                    @Override
                                    public void onResponse(JSONArray response) {
                                        for (int i = 0; i < response.length(); i++) {
                                            try {
                                                textView.setText("Loading");
                                                progressBar.setVisibility(View.VISIBLE);

                                                JSONObject jsonObject = response.getJSONObject(i);

                                                id = jsonObject.getString("Id");

                                                if (id.equals(parameters)) {

                                                    String dependencyName = jsonObject.getString("Dependency Name");
                                                    String developerName = jsonObject.getString("Developer Name");
                                                    String githubLink = jsonObject.getString("Github Link");
                                                    String youtubeLink = jsonObject.getString("YouTube Link");
                                                    String license = jsonObject.getString("License");
                                                    String licenseLink = jsonObject.getString("License Link");

                                                    Intent intent = new Intent(URL.this, Web.class);
                                                    intent.putExtra("id", id);
                                                    intent.putExtra("dependencyName", dependencyName);
                                                    intent.putExtra("developerName", developerName);
                                                    intent.putExtra("githubLink", githubLink);
                                                    intent.putExtra("youtubeLink", youtubeLink);
                                                    intent.putExtra("license", license);
                                                    intent.putExtra("licenseLink", licenseLink);
                                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    }
                                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    startActivity(intent);
                                                    break;
                                                } else {
                                                    textView.setText("Dependency doesn't exist");
                                                    progressBar.setVisibility(View.INVISIBLE);
                                                    home.setVisibility(View.VISIBLE);
                                                }
                                            } catch (JSONException e) {
                                                textView.setText("Loading");
                                                progressBar.setVisibility(View.VISIBLE);
                                                home.setVisibility(View.GONE);
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        textView.setText("There is some error while loading Dependency");
                                        progressBar.setVisibility(View.INVISIBLE);
                                        home.setVisibility(View.VISIBLE);
                                    }
                                });
                                jsonArrayRequest.setShouldCache(false);
                                requestQueue.add(jsonArrayRequest);
                            } else {
                                textView.setText("Dependency Id should contain 4 digits only");
                                progressBar.setVisibility(View.INVISIBLE);
                                home.setVisibility(View.VISIBLE);
                            }
                        } else {
                            textView.setText("Dependency Id should contain numbers only");
                            progressBar.setVisibility(View.INVISIBLE);
                            home.setVisibility(View.VISIBLE);
                        }
                    }
                }
            } else {
                textView.setText("URL is Invalid");
                progressBar.setVisibility(View.INVISIBLE);
                home.setVisibility(View.VISIBLE);
            }
        } else {
            startActivity(new Intent(URL.this, Login.class));
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        getCacheDir().delete();
        super.onDestroy();
    }
}