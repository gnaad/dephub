package com.dephub.android.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dephub.android.R;
import com.dephub.android.cardview.Dependency;
import com.dephub.android.cardview.SearchAdapter;
import com.dephub.android.constant.ApplicationConstant;
import com.dephub.android.utility.Snippet;
import com.dephub.android.utility.Widget;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Search extends AppCompatActivity {
    Button submitYourDependency;
    Drawable drawable;
    ImageView noDependency;
    LinearLayoutManager linearLayoutManager;
    RelativeLayout relativeLayout;
    RecyclerView recyclerView;
    SearchView searchview;
    String dependencyName, developerName, githubLink, id, license, licenseLink, fullName;
    SearchAdapter cardViewSearch;
    TextView centerText, notFound, count;
    Toolbar toolbar;

    private ArrayList<Dependency> cardSearch;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Snippet.followNightModeInSystem();
        Snippet.layoutInDisplayCutoutMode(Search.this);
        Snippet.darkTheme(Search.this, getApplicationContext());
        setContentView(R.layout.activity_search_view);

        getWindow().setNavigationBarColor(getResources().getColor(R.color.black));

        searchview = findViewById(R.id.search);
        recyclerView = findViewById(R.id.search_recycler_view);
        centerText = findViewById(R.id.description);
        noDependency = findViewById(R.id.no_dependency_image);
        notFound = findViewById(R.id.not_found_text);
        submitYourDependency = findViewById(R.id.submit_your_dependency_button);
        count = findViewById(R.id.dependency_number);
        relativeLayout = findViewById(R.id.search_view);

        int searchColor = searchview.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        TextView textView = searchview.findViewById(searchColor);
        textView.setTextColor(Color.BLUE);

        toolbar = findViewById(R.id.toolbar_search);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        Snippet.toolbar(Search.this, toolbar);
        setSupportActionBar(toolbar);

        drawable = toolbar.getOverflowIcon();
        DrawableCompat.setTint(drawable.mutate(), getResources().getColor(R.color.toolbar_icon));
        toolbar.setOverflowIcon(drawable);

        submitYourDependency.setOnClickListener(v -> {
            Intent intent = new Intent(Search.this, SubmitDependency.class);
            startActivity(intent);
        });

        cardSearch = new ArrayList<>();
        searchview.onActionViewExpanded();
        centerText.setText(ApplicationConstant.SEARCH);

        Snippet.dependencyArray(
                Search.this,
                ApplicationConstant.DEPENDENCY,
                response -> {
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject jsonObject = response.getJSONObject(i);
                            id = jsonObject.getString(ApplicationConstant.ID);
                            dependencyName = jsonObject.getString(ApplicationConstant.DEPENDENCY_NAME);
                            developerName = jsonObject.getString(ApplicationConstant.DEVELOPER_NAME);
                            fullName = jsonObject.getString(ApplicationConstant.FULL_NAME);
                            githubLink = jsonObject.getString(ApplicationConstant.GITHUB_LINK);
                            cardSearch.add(new Dependency(id, dependencyName, developerName, fullName, githubLink));
                            buildSearchCardView();
                        } catch (JSONException e) {
                            cardSearch.add(new Dependency());
                        }
                    }
                },
                error -> {
                    cardSearch.add(new Dependency());
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
                        Widget.Toast(Search.this, ApplicationConstant.SEARCH_DEPENDENCY_ID);
                    }
                }

                if (!newText.isEmpty()) {
                    count.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.VISIBLE);
                }

                if (newText.isEmpty() || newText.equals(" ")) {
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

    @Override
    protected void onResume() {
        super.onResume();
    }

    @SuppressLint("SetTextI18n")
    private void filter(String text) {
        ArrayList<Dependency> filteredList = new ArrayList<>();

        for (Dependency item : cardSearch) {
            if (item.getDependencyName().toLowerCase().contains(text.toLowerCase()) || item.getDeveloperName().toLowerCase().contains(text.toLowerCase()) ||
                    item.getId().toLowerCase().contains(text.toLowerCase())) {
                count.setText(filteredList.size() + 1 + " results found");
                filteredList.add(item);
            }
        }
        if (filteredList.isEmpty()) {
            count.setVisibility(View.INVISIBLE);
            relativeLayout.setVisibility(View.VISIBLE);
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