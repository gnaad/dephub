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

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dephub.android.R;
import com.dephub.android.cardview.CardModel;
import com.dephub.android.cardview.SearchAdapter;
import com.dephub.android.common.Component;
import com.dephub.android.common.Snippet;
import com.github.aakira.compoundicontextview.CompoundIconTextView;

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
    public static final String url = "https://gnanendraprasadp.github.io/DepHub-Web/json/dependency.json";
    SearchAdapter cardViewSearch;
    private ArrayList<CardModel> cardSearch;
    RelativeLayout relativeLayout;

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
        DrawableCompat.setTint(drawable.mutate(), getResources().getColor(R.color.toolbaricon));
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
                            String bg = getString(R.color.whitetoblack);
                            cardBackground = bg;
                            youtube = jsonObject.getString("youtube_link");
                            id = jsonObject.getString("id");
                            license = jsonObject.getString("license");
                            licenseLink = jsonObject.getString("license_link");
                            cardSearch.add(new CardModel(dependencyName, devName, githubLink, cardBackground, youtube, id, license, licenseLink, fullName));
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
                        Component.Toast(Search.this, "Dependency Id is only 4 digits. Check once");
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
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @SuppressLint("SetTextI18n")
    private void filter(String text) {
        ArrayList<CardModel> filteredList = new ArrayList<>();

        for (CardModel item : cardSearch) {

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