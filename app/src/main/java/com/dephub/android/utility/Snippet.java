package com.dephub.android.utility;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Vibrator;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.dephub.android.R;
import com.dephub.android.cardview.DependencyModel;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Snippet {
    public static void followNightModeInSystem() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
    }

    public static void layoutInDisplayCutoutMode(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            activity.getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.
                    LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }
    }

    public static void loginFullScreen(Activity activity) {
        activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    public static void uiVisibility(Activity activity) {
        View decorView = activity.getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    public static void toolbar(Context context, Toolbar toolbar) {
        int nightModeFlags = context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
            int white = Color.parseColor("#ffffff");
            toolbar.setTitleTextColor(white);
            toolbar.setSubtitleTextColor(white);
        } else {
            int black = Color.parseColor("#000000");
            toolbar.setTitleTextColor(black);
            toolbar.setSubtitleTextColor(black);
        }
    }


    public static void initializeInterstitialAd(Context context) {
        MobileAds.initialize(context, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
    }

    public static void vibrate(Context context, String linkToCopy) {
        Vibrator vibrator = (Vibrator) context.getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(28);
        ClipboardManager clipboard1 = (ClipboardManager) context.getApplicationContext().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("DepHub", linkToCopy);
        clipboard1.setPrimaryClip(clip);
    }

    public static void dependencyArray(Context context,
                                       String fetchUrl,
                                       Response.Listener<JSONArray> listener,
                                       Response.ErrorListener errorListener) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.getCache().clear();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, fetchUrl, null, listener, errorListener);
        jsonArrayRequest.setShouldCache(false);
        requestQueue.add(jsonArrayRequest);
    }

    public static void dependencyObject(Context context,
                                        String fetchUrl,
                                        Response.Listener<JSONObject> listener,
                                        Response.ErrorListener errorListener) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.getCache().clear();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, fetchUrl, null, listener, errorListener);
        jsonObjectRequest.setShouldCache(false);
        requestQueue.add(jsonObjectRequest);
    }

    public static void fetchDependency(String dependencyType, Context context, JSONArray response, ArrayList<DependencyModel> cardText, ProgressDialog progressDialog) {
        for (int i = 0; i < response.length(); i++) {
            try {
                JSONObject jsonObject = response.getJSONObject(i);
                String type = jsonObject.getString("type");
                if (type.equals(dependencyType)) {
                    String dependencyName = jsonObject.getString("dependency_name");
                    String developerName = jsonObject.getString("developer_name");
                    String fullName = jsonObject.getString("full_name");
                    String githubLink = jsonObject.getString("github_link");
                    @SuppressLint("ResourceType")
                    String bg = context.getString(R.color.white_to_black);
                    String youtube = jsonObject.getString("youtube_link");
                    String id = jsonObject.getString("id");
                    String license = jsonObject.getString("license");
                    String licenseLink = jsonObject.getString("license_link");
                    cardText.add(new DependencyModel(dependencyName, developerName, githubLink, bg, youtube, id, license, licenseLink, fullName));

                    Collections.sort(cardText, new Comparator<DependencyModel>() {
                        @Override
                        public int compare(DependencyModel o1, DependencyModel o2) {
                            return o1.getDependencyName().compareTo(o2.getDependencyName());
                        }
                    });
                }
            } catch (JSONException e) {
                progressDialog.dismiss();
                e.printStackTrace();
            }
        }
    }

    public static void openWeb(Context context, DependencyModel model) {
        String githubLink = model.getGithubLink();

        Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage("com.github.android");
        if (launchIntent != null) {
            Intent githubIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(githubLink));
            githubIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            githubIntent.setPackage("com.github.android");
            context.startActivity(githubIntent);
        } else {
            Widget.openInBrowser((Activity) context, githubLink);
        }
    }
}