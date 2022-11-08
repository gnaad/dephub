package com.dephub.android.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.dephub.android.R;
import com.dephub.android.common.Component;
import com.dephub.android.common.Snippet;

public class Login extends AppCompatActivity {
    public static final String privacyPolicy = "https://gnanendraprasadp.github.io/DepHub-Web/privacypolicy";
    public static final String termsOfService = "https://gnanendraprasadp.github.io/DepHub-Web/termsofservice";

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Snippet.followNightModeInSystem();
        Snippet.layoutInDisplayCutoutMode(Login.this);
        Snippet.loginFullScreen(Login.this);

        getSupportActionBar().hide();

        setContentView(R.layout.activity_login);

        TextView termsAndCondition = findViewById(R.id.footer);
        String footer = "By continuing you agree to DepHub's Terms of Service and Privacy Policy.";
        termsAndCondition.setText(footer);

        SpannableString spannableStringFooter = new SpannableString(footer);
        ClickableSpan clickableSpanTerms = new ClickableSpan() {
            @Override
            public void updateDrawState(TextPaint textPaint) {
                textPaint.setUnderlineText(false);
            }

            @Override
            public void onClick(@NonNull View widget) {
                Component.openInBrowser(Login.this, privacyPolicy);
            }
        };

        ClickableSpan clickableSpanPolicy = new ClickableSpan() {
            @Override
            public void updateDrawState(TextPaint textPaint) {
                textPaint.setUnderlineText(false);
            }

            @Override
            public void onClick(@NonNull View widget) {
                Component.openInBrowser(Login.this, termsOfService);
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

        Snippet.uiVisibility(Login.this);

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
}