package com.dephub.android.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.fragment.app.Fragment;

import com.dephub.android.R;
import com.dephub.android.constant.ApplicationConstant;
import com.dephub.android.utility.Snippet;
import com.dephub.android.utility.Widget;
import com.github.appintro.AppIntro2;
import com.github.appintro.AppIntroFragment;

public class AppIntroduction extends AppIntro2 {

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Snippet.followNightModeInSystem();
        Snippet.layoutInDisplayCutoutMode(AppIntroduction.this);
        EdgeToEdge.enable(this);

        addSlide(AppIntroFragment.createInstance(
                ApplicationConstant.SLIDE_TITLE1,
                ApplicationConstant.SLIDE_DESCRIPTION1,
                R.drawable.ic_intro1,
                R.color.intro_white,
                R.color.intro_black,
                R.color.intro_black
        ));
        addSlide(AppIntroFragment.createInstance(
                ApplicationConstant.SLIDE_TITLE2,
                ApplicationConstant.SLIDE_DESCRIPTION2,
                R.drawable.ic_intro2,
                R.color.intro_white,
                R.color.intro_black,
                R.color.intro_black
        ));
        addSlide(AppIntroFragment.createInstance(
                ApplicationConstant.SLIDE_TITLE3,
                ApplicationConstant.SLIDE_DESCRIPTION3,
                R.drawable.ic_intro3,
                R.color.intro_white,
                R.color.intro_black,
                R.color.intro_black
        ));
        addSlide(AppIntroFragment.createInstance(
                ApplicationConstant.SLIDE_TITLE4,
                ApplicationConstant.SLIDE_DESCRIPTION4,
                R.drawable.ic_intro4,
                R.color.intro_white,
                R.color.intro_black,
                R.color.intro_black
        ));
        addSlide(AppIntroFragment.createInstance(
                ApplicationConstant.SLIDE_TITLE5,
                ApplicationConstant.SLIDE_DESCRIPTION5,
                R.drawable.ic_intro5,
                R.color.intro_white,
                R.color.intro_black,
                R.color.intro_black
        ));
        addSlide(AppIntroFragment.createInstance(
                ApplicationConstant.SLIDE_TITLE6,
                ApplicationConstant.SLIDE_DESCRIPTION6,
                R.drawable.ic_intro6,
                R.color.intro_white,
                R.color.intro_black,
                R.color.intro_black
        ));
        addSlide(AppIntroFragment.createInstance(
                ApplicationConstant.SLIDE_TITLE7,
                ApplicationConstant.SLIDE_DESCRIPTION7,
                R.drawable.ic_intro7,
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
        Widget.Toast(AppIntroduction.this, getString(R.string.welcome_message));
        Intent intent = new Intent(AppIntroduction.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}