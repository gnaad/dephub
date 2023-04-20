package com.dephub.android.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.dephub.android.R;
import com.dephub.android.utility.Snippet;
import com.dephub.android.utility.Widget;
import com.github.appintro.AppIntro2;
import com.github.appintro.AppIntroFragment;
import com.google.firebase.messaging.FirebaseMessaging;

public class AppIntroduction extends AppIntro2 {

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Snippet.followNightModeInSystem();
        Snippet.layoutInDisplayCutoutMode(AppIntroduction.this);

        addSlide(AppIntroFragment.createInstance(
                getString(R.string.slide1_title),
                getString(R.string.slide1_description),
                R.drawable.intro1,
                R.color.intro_white,
                R.color.intro_black,
                R.color.intro_black
        ));
        addSlide(AppIntroFragment.createInstance(
                getString(R.string.slide2_title),
                getString(R.string.slide2_description),
                R.drawable.intro2,
                R.color.intro_white,
                R.color.intro_black,
                R.color.intro_black
        ));
        addSlide(AppIntroFragment.createInstance(
                getString(R.string.slide3_title),
                getString(R.string.slide3_description),
                R.drawable.intro8,
                R.color.intro_white,
                R.color.intro_black,
                R.color.intro_black
        ));
        addSlide(AppIntroFragment.createInstance(
                getString(R.string.slide4_title),
                getString(R.string.slide4_description),
                R.drawable.intro9,
                R.color.intro_white,
                R.color.intro_black,
                R.color.intro_black
        ));
        addSlide(AppIntroFragment.createInstance(
                getString(R.string.slide5_title),
                getString(R.string.slide5_description),
                R.drawable.intro3,
                R.color.intro_white,
                R.color.intro_black,
                R.color.intro_black
        ));
        addSlide(AppIntroFragment.createInstance(
                getString(R.string.slide6_title),
                getString(R.string.slide6_description),
                R.drawable.intro4,
                R.color.intro_white,
                R.color.intro_black,
                R.color.intro_black
        ));
        addSlide(AppIntroFragment.createInstance(
                getString(R.string.slide7_title),
                getString(R.string.slide7_description),
                R.drawable.intro10,
                R.color.intro_white,
                R.color.intro_black,
                R.color.intro_black
        ));
        setColorTransitionsEnabled(true);
        showStatusBar(false);
        setNavBarColor(R.color.intro_white);
        setWizardMode(true);
        setImmersive(false);
        setIndicatorEnabled(true);
        setIndicatorColor(
                getColor(R.color.intro_black),
                getColor(R.color.intro_unselected)
        );
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);

        FirebaseMessaging.getInstance().subscribeToTopic("dev").addOnCompleteListener(task -> {
            String message = " 🖥";
            if (!task.isSuccessful()) {
                message = "";
            }
            Widget.Toast(AppIntroduction.this, getString(R.string.WelcomeToast) + message);
        });

        Intent intent = new Intent(AppIntroduction.this, SplashScreen.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}