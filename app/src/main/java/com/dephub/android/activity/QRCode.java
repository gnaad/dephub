package com.dephub.android.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import com.dephub.android.R;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class QRCode extends AppCompatActivity {
    RelativeLayout relativeLayout;
    private InterstitialAd qrCodeInterstitialAd;
    Button download;
    Boolean goBack;

    @SuppressLint({"WrongThread", "ResourceAsColor", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);

        setContentView(R.layout.activity_qrcode);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        String qrCodeLink = getIntent().getExtras().getString("qrCodeLink");
        String qrCodeTitle = getIntent().getExtras().getString("qrCodeTitle");
        String qrCodeId = getIntent().getExtras().getString("qrCodeId");
        String devName = getIntent().getExtras().getString("developerName");

        ImageView logo = findViewById(R.id.dephubLogo);

        TextView title = findViewById(R.id.qrCodeTitle);
        title.setText("Dependency : " + qrCodeTitle);

        title.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(QRCode.this, "Dependency Id : " + qrCodeId, Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        TextView developerName = findViewById(R.id.qrCodeDeveloper);
        developerName.setText("Developer : " + devName);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {
            }
        });

        AdView qrCodeAdView = findViewById(R.id.adQrCode);
        AdRequest adRequest = new AdRequest.Builder().build();
        qrCodeAdView.loadAd(adRequest);

        qrCodeAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
            }

            @Override
            public void onAdOpened() {
            }

            @Override
            public void onAdClicked() {
            }

            @Override
            public void onAdClosed() {
            }
        });

        //

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.black));
        }

        Toolbar toolbar = findViewById(R.id.toolbarQrCode);
        toolbar.setTitle("QR Code");
        toolbar.setNavigationIcon(R.drawable.ic_back);

        int nightModeFlags = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
            int white = Color.parseColor("#ffffff");
            toolbar.setTitleTextColor(white);
        } else {
            int black = Color.parseColor("#000000");
            toolbar.setTitleTextColor(black);
        }

        setSupportActionBar(toolbar);

        ImageView imageView = findViewById(R.id.qrCode);
        QRGEncoder qrCodeCreate = new QRGEncoder("https://dephub.co/app/" + qrCodeId, null, QRGContents.Type.TEXT, 650);
        qrCodeCreate.setColorBlack(Color.BLACK);
        qrCodeCreate.setColorWhite(Color.WHITE);

        Bitmap bitmap = qrCodeCreate.getBitmap();

        Resources mResources = getResources();

        float cornerRadius = 50.0f;
        RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(mResources, bitmap);
        roundedBitmapDrawable.setCornerRadius(cornerRadius);
        roundedBitmapDrawable.setAntiAlias(true);
        imageView.setImageDrawable(roundedBitmapDrawable);

        relativeLayout = findViewById(R.id.relativelayout);

        // Interstitial Ad start
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {
            }
        });

        AdRequest qrCodeAd = new AdRequest.Builder().build();

        InterstitialAd.load
                (QRCode.this, "ca-app-pub-3037529522611130/4061112510", qrCodeAd, new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        qrCodeInterstitialAd = interstitialAd;

                        interstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                            @Override
                            public void onAdShowedFullScreenContent() {
                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                                if (goBack) {
                                    QRCode.super.onBackPressed();
                                } else {
                                }
                            }

                            @Override
                            public void onAdDismissedFullScreenContent() {
                                if (goBack) {
                                    QRCode.super.onBackPressed();
                                } else {
                                }
                            }
                        });
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {

                    }
                });


        // Interstitial Ad End

        download = findViewById(R.id.download);
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logo.setVisibility(View.VISIBLE);
                Bitmap image = getBitmapFromView(relativeLayout);

                try {
                    File cachePath = new File(getApplicationContext().getCacheDir(), "qrcode");
                    cachePath.mkdirs();
                    FileOutputStream stream = new FileOutputStream(cachePath + "/qrcode.png");
                    image.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    stream.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }

                File imagePath = new File(getApplicationContext().getCacheDir(), "qrcode");
                File newFile = new File(imagePath, "/qrcode.png");
                Uri contentUri = FileProvider.getUriForFile(getApplicationContext(), "com.dephub.android.fileprovider", newFile);

                if (contentUri != null) {
                    String text = "Dependency Name : " + qrCodeTitle + "\nDependency Link : " + qrCodeLink + "\n\nIn-App link : https://dephub.co/app/" + qrCodeId + "\n\nInformation Delivered by : DepHub\nInformation Provided by : GitHub\n\nDownload our Android App : https://bit.ly/installdephubapp\n\nThank You\nLet's code for a better tomorrow";
                    String subject = "QR Code of " + qrCodeTitle;
                    Intent shareIntent = new Intent();
                    shareIntent.setAction(Intent.ACTION_SEND);
                    shareIntent.setType("image/png");
                    shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
                    shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, text);
                    shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    shareIntent.setDataAndType(contentUri, getContentResolver().getType(contentUri));
                    shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
                    startActivity(Intent.createChooser(shareIntent, "Share using"));
                }
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        if (qrCodeInterstitialAd != null) {
            goBack = true;
            qrCodeInterstitialAd.show(QRCode.this);
        } else {
            super.onBackPressed();
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (qrCodeInterstitialAd != null) {
            goBack = true;
            qrCodeInterstitialAd.show(QRCode.this);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        if (qrCodeInterstitialAd != null) {
            goBack = false;
            qrCodeInterstitialAd.show(QRCode.this);
        } else {
        }
        super.onResume();
    }

    @SuppressLint("ResourceAsColor")
    private Bitmap getBitmapFromView(View view) {
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null) {
            bgDrawable.draw(canvas);
        } else {
            int nightModeFlags = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
            if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
                int black = Color.parseColor("#000000");

                canvas.drawColor(black);
            } else {
                int white = Color.parseColor("#FFFFFF");

                canvas.drawColor(white);
            }
        }
        view.draw(canvas);
        return returnedBitmap;
    }
}