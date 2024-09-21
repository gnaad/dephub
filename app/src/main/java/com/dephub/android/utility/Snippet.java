package com.dephub.android.utility;

import android.app.Activity;
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
import com.dephub.android.cardview.Dependency;
import com.dephub.android.constant.ApplicationConstant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Comparator;

public class Snippet {
    public static void followNightModeInSystem() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
    }

    public static void layoutInDisplayCutoutMode(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            activity.getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
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

    public static void darkTheme(Activity activity, Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if ((context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES) {
                activity.getWindow().getDecorView().setSystemUiVisibility(0);
            } else {
                activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        }
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

    public static void vibrate(Context context, String linkToCopy) {
        Vibrator vibrator = (Vibrator) context.getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(28);
        ClipboardManager clipboard1 = (ClipboardManager) context.getApplicationContext().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(ApplicationConstant.DEPHUB, linkToCopy);
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

    public static void fetchDependency(String dependencyType, JSONArray response, ArrayList<Dependency> cardText) {
        for (int i = 0; i < response.length(); i++) {
            try {
                JSONObject jsonObject = response.getJSONObject(i);
                String type = jsonObject.getString(ApplicationConstant.TYPE);
                if (type.equals(dependencyType)) {
                    String id = jsonObject.getString(ApplicationConstant.ID);
                    String dependencyName = jsonObject.getString(ApplicationConstant.DEPENDENCY_NAME);
                    String developerName = jsonObject.getString(ApplicationConstant.DEVELOPER_NAME);
                    String fullName = jsonObject.getString(ApplicationConstant.FULL_NAME);
                    String githubLink = jsonObject.getString(ApplicationConstant.GITHUB_LINK);
                    cardText.add(new Dependency(id, dependencyName, developerName, fullName, githubLink));
                    cardText.sort(Comparator.comparing(Dependency::getDependencyName));
                }
            } catch (JSONException e) {
                cardText.add(new Dependency());
            }
        }
    }

    public static void openWeb(Context context, Dependency model) {
        String githubLink = model.getGithubLink();

        Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(ApplicationConstant.GITHUB_PACKAGE_ID);
        if (launchIntent != null) {
            Intent githubIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(githubLink));
            githubIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            githubIntent.setPackage(ApplicationConstant.GITHUB_PACKAGE_ID);
            context.startActivity(githubIntent);
        } else {
            Widget.openInBrowser((Activity) context, githubLink);
        }
    }

}