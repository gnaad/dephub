package com.dephub.android.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dephub.android.R;
import com.dephub.android.cardview.DependencyModel;
import com.dephub.android.cardview.SearchAdapter;
import com.dephub.android.utility.Snippet;
import com.dephub.android.utility.Widget;
import com.github.aakira.compoundicontextview.CompoundIconTextView;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Search extends AppCompatActivity {
    SearchView searchview;
    RecyclerView recyclerView;
    String dependencyName, devName, githubLink, cardBackground, youtube, id, license, licenseLink, fullName;
    TextView centerText, notFound, count;
    Button submitYourDependency;
    LinearLayoutManager linearLayoutManager;
    CompoundIconTextView compoundIconTextView;
    public static final String url = "https://gnanendraprasadp.github.io/dephub/json/dependency.json";
    SearchAdapter cardViewSearch;
    private ArrayList<DependencyModel> cardSearch;
    RelativeLayout relativeLayout;
    private InterstitialAd searchInterstitialAd;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Snippet.followNightModeInSystem();
        setContentView(R.layout.activity_searchview);

        getWindow().setNavigationBarColor(getResources().getColor(R.color.black));

        searchview = findViewById(R.id.search);
        recyclerView = findViewById(R.id.searchRecyclerView);
        centerText = findViewById(R.id.description);
        compoundIconTextView = findViewById(R.id.noDependencyImage);
        notFound = findViewById(R.id.notFoundText);
        submitYourDependency = findViewById(R.id.submitYourDependencyButton);
        count = findViewById(R.id.dependencyNumber);
        relativeLayout = findViewById(R.id.searchView);

        Toolbar toolbar = findViewById(R.id.toolbarSearch);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        Snippet.toolbar(Search.this, toolbar);
        setSupportActionBar(toolbar);

        Drawable drawable = toolbar.getOverflowIcon();
        DrawableCompat.setTint(drawable.mutate(), getResources().getColor(R.color.toolbar_icon));
        toolbar.setOverflowIcon(drawable);

        submitYourDependency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Search.this, SubmitDependency.class);
                startActivity(intent);
            }
        });

        cardSearch = new ArrayList<>();

        searchview.onActionViewExpanded();

        centerText.setText("Type Dependency name or Developer Name or Id\nTap 🔍\n\nHint : Long Hold on Dependency Name to know Id");

        Snippet.dependencyArray(
                Search.this,
                url,
                response -> {
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject jsonObject = response.getJSONObject(i);
                            dependencyName = jsonObject.getString("dependency_name");
                            devName = jsonObject.getString("developer_name");
                            fullName = jsonObject.getString("full_name");
                            githubLink = jsonObject.getString("github_link");
                            @SuppressLint("ResourceType")
                            String bg = getString(R.color.white_to_black);
                            cardBackground = bg;
                            youtube = jsonObject.getString("youtube_link");
                            id = jsonObject.getString("id");
                            license = jsonObject.getString("license");
                            licenseLink = jsonObject.getString("license_link");
                            cardSearch.add(new DependencyModel(dependencyName, devName, githubLink, cardBackground, youtube, id, license, licenseLink, fullName));
                            buildSearchCardView();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                error -> {

                });

        searchview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                View view = getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    searchview.clearFocus();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                centerText.setVisibility(View.INVISIBLE);
                count.setVisibility(View.VISIBLE);

                if (TextUtils.isDigitsOnly(newText)) {
                    if (newText.length() > 4) {
                        Widget.Toast(Search.this, "Dependency Id is only 4 digits. Check once");
                    }
                }

                if (newText.length() > 0) {
                    count.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.VISIBLE);
                }

                if (newText.length() == 0 || newText.equals(" ")) {
                    count.setVisibility(View.INVISIBLE);
                    recyclerView.setVisibility(View.GONE);
                    centerText.setVisibility(View.VISIBLE);
                }

                filter(newText);
                return false;
            }
        });

        Snippet.initializeInterstitialAd(Search.this);
        AdRequest searchAdRequest = new AdRequest.Builder().build();

        Widget.showInterstitialAd(this, "ca-app-pub-3037529522611130/4061112510", searchAdRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                Search.super.onBackPressed();
            }

            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                searchInterstitialAd = interstitialAd;

                interstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        Search.super.onBackPressed();
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                        Search.super.onBackPressed();
                    }
                });
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (searchInterstitialAd != null) {
            searchInterstitialAd.show(Search.this);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        if (searchInterstitialAd != null) {
            searchInterstitialAd.show(Search.this);
        } else {
            super.onResume();
        }
    }

    @SuppressLint("SetTextI18n")
    private void filter(String text) {
        ArrayList<DependencyModel> filteredList = new ArrayList<>();

        for (DependencyModel item : cardSearch) {

            if (item.getDependencyName().toLowerCase().contains(text.toLowerCase()) || item.getDeveloperName().toLowerCase().contains(text.toLowerCase()) ||
                    item.getId().toLowerCase().contains(text.toLowerCase())) {
                count.setText(filteredList.size() + 1 + " results found");
                filteredList.add(item);
            }
        }
        if (filteredList.isEmpty()) {
            count.setVisibility(View.INVISIBLE);
            relativeLayout.setVisibility(View.VISIBLE);
            compoundIconTextView.setText(filteredList.size() + " results found for \"" + text + "\"");
            recyclerView.setVisibility(View.GONE);
        } else {
            relativeLayout.setVisibility(View.GONE);
            cardViewSearch.filterList(filteredList);
        }
    }

    private void buildSearchCardView() {
        cardViewSearch = new SearchAdapter(cardSearch, Search.this);
        linearLayoutManager = new LinearLayoutManager(Search.this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(cardViewSearch);
    }
}