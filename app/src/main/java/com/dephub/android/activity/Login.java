package com.dephub.android.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.core.content.ContextCompat;

import com.dephub.android.R;

public class Login extends AppCompatActivity {
    public static final String privacyPolicy = "https://gnanendraprasadp.github.io/DepHub-Web/privacypolicy";
    public static final String termsOfService = "https://gnanendraprasadp.github.io/DepHub-Web/termsofservice";

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(LayoutParams.FLAG_FULLSCREEN, LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(LayoutParams.FLAG_FULLSCREEN);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getWindow().getAttributes().layoutInDisplayCutoutMode = LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;

        }
        getSupportActionBar().hide();

        setContentView(R.layout.activity_login);

        TextView termsAndCondition = findViewById(R.id.footer);

        String footer = "By continuing you agree to DepHub's Terms of Service and Privacy Policy.";
        termsAndCondition.setText(footer);

        SpannableString spannableStringFooter = new SpannableString(footer);

        ClickableSpan clickableSpanTerms = new ClickableSpan() {
            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setUnderlineText(false);
            }

            @Override
            public void onClick(@NonNull View widget) {
                openCustomTabs(privacyPolicy);
            }
        };

        ClickableSpan clickableSpanPolicy = new ClickableSpan() {
            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setUnderlineText(false);
            }

            @Override
            public void onClick(@NonNull View widget) {
                openCustomTabs(termsOfService);
            }
        };

        spannableStringFooter.setSpan(clickableSpanPolicy, 36, 52, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableStringFooter.setSpan(clickableSpanTerms, 57, 71, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        ForegroundColorSpan fcsTerms = new ForegroundColorSpan(getColor(R.color.colorAccent));
        ForegroundColorSpan fcsPolicy = new ForegroundColorSpan(getColor(R.color.colorAccent));

        spannableStringFooter.setSpan(fcsTerms, 36, 52, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableStringFooter.setSpan(fcsPolicy, 57, 71, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        termsAndCondition.setText(spannableStringFooter);
        termsAndCondition.setMovementMethod(LinkMovementMethod.getInstance());

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        Button signInButton = findViewById(R.id.sign_in);

        SharedPreferences prefs = getSharedPreferences("policy", MODE_PRIVATE);
        boolean firstStart = prefs.getBoolean("agreed", false);

        if (firstStart) {
            startActivity(new Intent(Login.this, SplashScreen.class));
            finish();
        }

        signInButton.setOnClickListener(view -> {
            SharedPreferences prefs1 = getSharedPreferences("policy", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs1.edit();
            editor.putBoolean("agreed", true);
            editor.apply();

            Intent intent = new Intent(Login.this, AppIntroduction.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }

    private void openCustomTabs(String url) {
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.setToolbarColor(ContextCompat.getColor(Login.this, R.color.colorPrimary));
        builder.setShowTitle(true);
        builder.addDefaultShareMenuItem();
        builder.setUrlBarHidingEnabled(false);
        builder.setStartAnimations(Login.this, R.anim.slide_up, R.anim.trans);
        builder.setExitAnimations(Login.this, R.anim.trans, R.anim.slide_down);
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(Login.this, Uri.parse(url));
    }
}