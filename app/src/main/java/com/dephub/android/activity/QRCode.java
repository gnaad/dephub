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
    private Resources mResources;
    private AdView mAdView;
    private InterstitialAd interstitialAdqrcode;

    @SuppressLint({"WrongThread","ResourceAsColor"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);

        setContentView(R.layout.activity_qrcode);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,WindowManager.LayoutParams.FLAG_SECURE);

        String qrlinkrec = getIntent( ).getExtras( ).getString("qrcodelink");
        String qrlinktitle = getIntent( ).getExtras( ).getString("qrcodetitle");
        String qrlinkid = getIntent( ).getExtras( ).getString("qrcodeid");
        String devname = getIntent( ).getExtras( ).getString("devname");

        ImageView compoundIconTextView = findViewById(R.id.dephublogo);

        TextView textView = findViewById(R.id.qrcodetitle);
        textView.setText("Dependency : " + qrlinktitle);

        textView.setOnLongClickListener(new View.OnLongClickListener( ) {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(QRCode.this,"Dependency Id : " + qrlinkid,Toast.LENGTH_SHORT).show( );
                return false;
            }
        });

        TextView textView1 = findViewById(R.id.qrcodedeveloper);
        textView1.setText("Developer : " + devname);

        //
        MobileAds.initialize(this,new OnInitializationCompleteListener( ) {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.adqrcode);
        AdRequest adRequest = new AdRequest.Builder( ).build( );
        mAdView.loadAd(adRequest);

        mAdView.setAdListener(new AdListener( ) {
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
            getWindow( ).setNavigationBarColor(getResources( ).getColor(R.color.black));
        }

        Toolbar toolbar = findViewById(R.id.toolbarqrcode);
        toolbar.setTitle("QR Code");
        toolbar.setNavigationIcon(R.drawable.ic_back);

        int nightModeFlags = getResources( ).getConfiguration( ).uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
            int white = Color.parseColor("#ffffff");
            toolbar.setTitleTextColor(white);
        } else {
            int black = Color.parseColor("#000000");
            toolbar.setTitleTextColor(black);
        }

        setSupportActionBar(toolbar);

        ImageView imageView = findViewById(R.id.qrcode);

        QRGEncoder qrEncoder = new QRGEncoder("https://dephub.co/app/" + qrlinkid,null,QRGContents.Type.TEXT,650);
        qrEncoder.setColorBlack(Color.BLACK);
        qrEncoder.setColorWhite(Color.WHITE);

        Bitmap bitmap = qrEncoder.getBitmap( );

        mResources = getResources( );

        float cornerRadius = 50.0f;
        RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(mResources,bitmap);
        roundedBitmapDrawable.setCornerRadius(cornerRadius);
        roundedBitmapDrawable.setAntiAlias(true);

        imageView.setImageDrawable(roundedBitmapDrawable);

        relativeLayout = findViewById(R.id.relativelayout);

        // Interstitial Ad start
        MobileAds.initialize(this,new OnInitializationCompleteListener( ) {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });


        AdRequest adRequest1 = new AdRequest.Builder( ).build( );

        InterstitialAd.load
                (this,"ca-app-pub-3037529522611130/4061112510",adRequest1,new InterstitialAdLoadCallback( ) {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        interstitialAdqrcode = interstitialAd;

                        interstitialAdqrcode.setFullScreenContentCallback(new FullScreenContentCallback( ) {
                            @Override
                            public void onAdShowedFullScreenContent() {
                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                                compoundIconTextView.setVisibility(View.VISIBLE);
                                Bitmap image = getBitmapFromView(relativeLayout);

                                try {

                                    File cachePath = new File(getApplicationContext( ).getCacheDir( ),"qrcode");
                                    //noinspection ResultOfMethodCallIgnored
                                    cachePath.mkdirs( );
                                    FileOutputStream stream = new FileOutputStream(cachePath + "/qrcode.png");
                                    image.compress(Bitmap.CompressFormat.PNG,100,stream);
                                    stream.close( );

                                } catch (IOException e) {
                                    e.printStackTrace( );
                                }

                                File imagePath = new File(getApplicationContext( ).getCacheDir( ),"qrcode");
                                File newFile = new File(imagePath,"/qrcode.png");
                                Uri contentUri = FileProvider.getUriForFile(getApplicationContext( ),"com.dephub.android.fileprovider",newFile);

                                if (contentUri != null) {
                                    String text = "Dependency Name : " + qrlinktitle + "\nDependency Link : " + qrlinkrec + "\n\nIn-App link : https://dephub.co/app/" + qrlinkid + "\n\nInformation Delivered by : DepHub\nInformation Provided by : GitHub\n\nDownload our Android App : https://bit.ly/installdephubapp\n\nThank You\nLet's code for a better tomorrow";
                                    String subject = "QR Code of " + qrlinktitle;
                                    Intent shareIntent = new Intent( );
                                    shareIntent.setAction(Intent.ACTION_SEND);
                                    shareIntent.setType("image/jpeg");
                                    shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,subject);
                                    shareIntent.putExtra(android.content.Intent.EXTRA_TEXT,text);
                                    shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                    shareIntent.setDataAndType(contentUri,getContentResolver( ).getType(contentUri));
                                    shareIntent.putExtra(Intent.EXTRA_STREAM,contentUri);
                                    startActivity(Intent.createChooser(shareIntent,"Share using"));
                                }
                            }

                            @Override
                            public void onAdDismissedFullScreenContent() {
                                compoundIconTextView.setVisibility(View.VISIBLE);
                                Bitmap image = getBitmapFromView(relativeLayout);

                                try {

                                    File cachePath = new File(getApplicationContext( ).getCacheDir( ),"qrcode");
                                    //noinspection ResultOfMethodCallIgnored
                                    cachePath.mkdirs( );
                                    FileOutputStream stream = new FileOutputStream(cachePath + "/qrcode.png");
                                    image.compress(Bitmap.CompressFormat.PNG,100,stream);
                                    stream.close( );

                                } catch (IOException e) {
                                    e.printStackTrace( );
                                }

                                File imagePath = new File(getApplicationContext( ).getCacheDir( ),"qrcode");
                                File newFile = new File(imagePath,"/qrcode.png");
                                Uri contentUri = FileProvider.getUriForFile(getApplicationContext( ),"com.dephub.android.fileprovider",newFile);

                                if (contentUri != null) {
                                    String text = "Dependency Name : " + qrlinktitle + "\nDependency Link : " + qrlinkrec + "\n\nIn-App link : https://dephub.co/app/" + qrlinkid + "\n\nInformation Delivered by : DepHub\nInformation Provided by : GitHub\n\nDownload our Android App : https://bit.ly/installdephubapp\n\nThank You\nLet's code for a better tomorrow";
                                    String subject = "QR Code of " + qrlinktitle;
                                    Intent shareIntent = new Intent( );
                                    shareIntent.setAction(Intent.ACTION_SEND);
                                    shareIntent.setType("image/jpeg");
                                    shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,subject);
                                    shareIntent.putExtra(android.content.Intent.EXTRA_TEXT,text);
                                    shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                    shareIntent.setDataAndType(contentUri,getContentResolver( ).getType(contentUri));
                                    shareIntent.putExtra(Intent.EXTRA_STREAM,contentUri);
                                    startActivity(Intent.createChooser(shareIntent,"Share using"));
                                }
                            }
                        });

                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        interstitialAdqrcode = null;
                    }
                });

        // Interstitial Ad End

        Button qrdownload = findViewById(R.id.download);

        qrdownload.setOnClickListener(new View.OnClickListener( ) {
            @Override
            public void onClick(View v) {
                if (interstitialAdqrcode != null) {
                    interstitialAdqrcode.show(QRCode.this);
                } else {
                    Toast.makeText(QRCode.this,"Oh No! Something went wrong while capturing QR Code. Tap Share button again to Recapture QR Code.",Toast.LENGTH_LONG).show( );
                }
            }
        });
    }

    @SuppressLint("ResourceAsColor")
    private Bitmap getBitmapFromView(View view) {
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth( ),view.getHeight( ),Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable = view.getBackground( );
        if (bgDrawable != null) {
            bgDrawable.draw(canvas);
        } else {
            int nightModeFlags = getResources( ).getConfiguration( ).uiMode & Configuration.UI_MODE_NIGHT_MASK;
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

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed( );
        return true;
    }
}