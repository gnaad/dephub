package com.dephub.android.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.dephub.android.R;
import com.dephub.android.common.Component;
import com.dephub.android.common.Snippet;
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
                "How Dependencies are divided",
                "Dependencies are divided based on \nAndroid Studio XML Editor Palette",
                R.drawable.intro1,
                R.color.introwhite,
                R.color.introblack,
                R.color.introblack
        ));
        addSlide(AppIntroFragment.createInstance(
                "Open Copy Share",
                "• Tap on GitHub icon to open repository\n• Long hold on any two icons to copy details\n• Tap on share icon to share Dependency",
                R.drawable.intro2,
                R.color.introwhite,
                R.color.introblack,
                R.color.introblack
        ));
        addSlide(AppIntroFragment.createInstance(
                "Add to Favorites",
                "Add any Dependency to your Favorite list to access it quickly",
                R.drawable.intro8,
                R.color.introwhite,
                R.color.introblack,
                R.color.introblack
        ));
        addSlide(AppIntroFragment.createInstance(
                "Dependency Overview",
                "Get an overview of any Dependency like stars, pull request, license and many more by long holding Dependency Name",
                R.drawable.intro9,
                R.color.introwhite,
                R.color.introblack,
                R.color.introblack
        ));
        addSlide(AppIntroFragment.createInstance(
                "Submit Dependency",
                "If your Dependency is not listed in DepHub\nYou can send a request in Submit Dependency",
                R.drawable.intro3,
                R.color.introwhite,
                R.color.introblack,
                R.color.introblack
        ));
        addSlide(AppIntroFragment.createInstance(
                "Found any dependency on Internet",
                "• Tap Share\n• Search for DepHub Icon\n• Tap DepHub Icon to Submit Dependency",
                R.drawable.intro4,
                R.color.introwhite,
                R.color.introblack,
                R.color.introblack
        ));
        addSlide(AppIntroFragment.createInstance(
                "Search Dependency",
                "Quickly Search any Dependency by typing its Name, Developer Name or Dependency Id",
                R.drawable.intro10,
                R.color.introwhite,
                R.color.introblack,
                R.color.introblack
        ));
        addSlide(AppIntroFragment.createInstance(
                "Share QR Code",
                "Every Dependency is assigned with QR Code which can be shared to other apps",
                R.drawable.intro5,
                R.color.introwhite,
                R.color.introblack,
                R.color.introblack
        ));
        addSlide(AppIntroFragment.createInstance(
                "YouTube Video",
                "Some Dependencies are provided with YouTube video links that give information about how to implement them",
                R.drawable.intro6,
                R.color.introwhite,
                R.color.introblack,
                R.color.introblack
        ));
        addSlide(AppIntroFragment.createInstance(
                "Stay Updated with News",
                "Get Information about new features and much more stuff",
                R.drawable.intro7,
                R.color.introwhite,
                R.color.introblack,
                R.color.introblack
        ));
        setColorTransitionsEnabled(true);
        showStatusBar(false);
        setNavBarColor(R.color.introwhite);
        setWizardMode(true);
        setImmersive(false);
        setIndicatorEnabled(true);
        setIndicatorColor(
                getColor(R.color.introblack),
                getColor(R.color.introunselected)
        );
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);

        FirebaseMessaging.getInstance().subscribeToTopic("dev").addOnCompleteListener(task -> {
            String message = "🖥";
            if (!task.isSuccessful()) {
                message = "";
            }
            Component.Toast(AppIntroduction.this, "Happy Coding " + message);
        });

        Intent intent = new Intent(AppIntroduction.this, SplashScreen.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}