package com.dephub.android.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.dephub.android.R;
import com.github.paolorotolo.appintro.AppIntro2;
import com.github.paolorotolo.appintro.AppIntroFragment;

public class AppIntro extends AppIntro2 {

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getWindow( ).getAttributes( ).layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;

        }

        addSlide(AppIntroFragment.newInstance(
                "How Dependencies are divided",
                "About",
                "Dependencies are divided based on \nAndroid Studio XML Editor Palette",
                "Description",
                R.drawable.intro1,
                ContextCompat.getColor(getApplicationContext( ),R.color.introwhite),
                ContextCompat.getColor(getApplicationContext( ),R.color.introblack),
                ContextCompat.getColor(getApplicationContext( ),R.color.introblack)
        ));

        addSlide(AppIntroFragment.newInstance(
                "Open Copy Share",
                "About",
                "• Tap on GitHub icon to open repository\n• Long hold on any two icons to copy the details\n• Tap on the share icon to share Dependency",
                "Description",
                R.drawable.intro2,
                ContextCompat.getColor(getApplicationContext( ),R.color.introwhite),
                ContextCompat.getColor(getApplicationContext( ),R.color.introblack),
                ContextCompat.getColor(getApplicationContext( ),R.color.introblack)
        ));
        addSlide(AppIntroFragment.newInstance(
                "Submit Dependency",
                "About",
                "If your dependency is not listed in DepHub\nYou can send a request in Submit Dependency",
                "Description",
                R.drawable.intro3,
                ContextCompat.getColor(getApplicationContext( ),R.color.introwhite),
                ContextCompat.getColor(getApplicationContext( ),R.color.introblack),
                ContextCompat.getColor(getApplicationContext( ),R.color.introblack)
        ));
        addSlide(AppIntroFragment.newInstance(
                "Found any dependency on Internet",
                "About",
                "• Tap Share\n• Scroll the list and Search for DepHub Icon\n• Tap the DepHub Icon to Submit Dependency",
                "Description",
                R.drawable.intro4,
                ContextCompat.getColor(getApplicationContext( ),R.color.introwhite),
                ContextCompat.getColor(getApplicationContext( ),R.color.introblack),
                ContextCompat.getColor(getApplicationContext( ),R.color.introblack)
        ));
        addSlide(AppIntroFragment.newInstance(
                "Share QR Code",
                "About",
                "Every dependency is assigned with QR Codes which can be shared to other apps",
                "Description",
                R.drawable.intro5,
                ContextCompat.getColor(getApplicationContext( ),R.color.introwhite),
                ContextCompat.getColor(getApplicationContext( ),R.color.introblack),
                ContextCompat.getColor(getApplicationContext( ),R.color.introblack)
        ));
        addSlide(AppIntroFragment.newInstance(
                "YouTube Video",
                "About",
                "Some dependencies are provided with YouTube video links that give information about how to implement them",
                "Description",
                R.drawable.intro6,
                ContextCompat.getColor(getApplicationContext( ),R.color.introwhite),
                ContextCompat.getColor(getApplicationContext( ),R.color.introblack),
                ContextCompat.getColor(getApplicationContext( ),R.color.introblack)
        ));
        addSlide(AppIntroFragment.newInstance(
                "Stay Updated with News",
                "About",
                "Get Information about new features and much more stuff",
                "Description",
                R.drawable.intro7,
                ContextCompat.getColor(getApplicationContext( ),R.color.introwhite),
                ContextCompat.getColor(getApplicationContext( ),R.color.introblack),
                ContextCompat.getColor(getApplicationContext( ),R.color.introblack)
        ));

        setColorTransitionsEnabled(true);
        showStatusBar(false);
        setNavBarColor(R.color.introwhite);
        setWizardMode(true);
        setImmersiveMode(false);
        setIndicatorColor(
                selectedIndicatorColor = getColor(R.color.introblack),
                unselectedIndicatorColor = getColor(R.color.introblue));
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);

        Intent intent = new Intent(AppIntro.this,SplashScreen.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish( );
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);

        Intent intent = new Intent(AppIntro.this,SplashScreen.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish( );
    }
}
