package com.dephub.android.activity;

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
import com.dephub.android.constant.ApplicationConstant;
import com.dephub.android.utility.Snippet;
import com.dephub.android.utility.Widget;

public class Login extends AppCompatActivity {
    Button continueNow;
    ClickableSpan clickableSpanTerms;
    ForegroundColorSpan fcsTerms;
    ForegroundColorSpan fcsPrivacy;
    SpannableString spannableStringFooter;
    TextView termsAndCondition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Snippet.followNightModeInSystem();
        Snippet.layoutInDisplayCutoutMode(Login.this);
        Snippet.loginFullScreen(Login.this);
        Snippet.uiVisibility(Login.this);
        setContentView(R.layout.activity_login);

        termsAndCondition = findViewById(R.id.footer);
        String footer = ApplicationConstant.LOGIN_FOOTER;
        termsAndCondition.setText(footer);

        spannableStringFooter = new SpannableString(footer);
        clickableSpanTerms = new ClickableSpan() {
            @Override
            public void updateDrawState(@NonNull TextPaint textPaint) {
                textPaint.setUnderlineText(false);
            }

            @Override
            public void onClick(@NonNull View widget) {
                Widget.openInBrowser(Login.this, ApplicationConstant.PRIVACY_POLICY);
            }
        };

        ClickableSpan clickableSpanPrivacy = new ClickableSpan() {
            @Override
            public void updateDrawState(TextPaint textPaint) {
                textPaint.setUnderlineText(false);
            }

            @Override
            public void onClick(@NonNull View widget) {
                Widget.openInBrowser(Login.this, ApplicationConstant.TERMS_OF_SERVICE);
            }
        };

        spannableStringFooter.setSpan(clickableSpanPrivacy, 36, 52, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableStringFooter.setSpan(clickableSpanTerms, 57, 71, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        fcsTerms = new ForegroundColorSpan(getColor(R.color.colorAccent));
        fcsPrivacy = new ForegroundColorSpan(getColor(R.color.colorAccent));

        spannableStringFooter.setSpan(fcsTerms, 36, 52, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableStringFooter.setSpan(fcsPrivacy, 57, 71, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        termsAndCondition.setText(spannableStringFooter);
        termsAndCondition.setMovementMethod(LinkMovementMethod.getInstance());


        continueNow = findViewById(R.id.continue_now);

        SharedPreferences sharedPreferences = getSharedPreferences(ApplicationConstant.POLICY, MODE_PRIVATE);
        boolean firstStart = sharedPreferences.getBoolean(ApplicationConstant.AGREED, false);

        if (firstStart) {
            startActivity(new Intent(Login.this, MainActivity.class));
            finish();
        }

        continueNow.setOnClickListener(view -> {
            SharedPreferences preferences = getSharedPreferences(ApplicationConstant.POLICY, MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean(ApplicationConstant.AGREED, true);
            editor.apply();

            Intent intent = new Intent(Login.this, AppIntroduction.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }
}