package com.dephub.android.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.dephub.android.R;
import com.dephub.android.cardview.CardModel;
import com.dephub.android.cardview.SearchAdapter;
import com.github.aakira.compoundicontextview.CompoundIconTextView;

import org.json.JSONArray;
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
    String url = "https://gnanendraprasadp.github.io/DepHub-Web/json/data.json";
    SearchAdapter cardViewSearch;
    private ArrayList<CardModel> cardSearch;
    RelativeLayout relativeLayout;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchview);

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
        int nightModeFlags = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
            int white = Color.parseColor("#ffffff");
            toolbar.setTitleTextColor(white);
        } else {
            int black = Color.parseColor("#000000");
            toolbar.setTitleTextColor(black);
        }
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

        RequestQueue requestQueue = Volley.newRequestQueue(Search.this);
        requestQueue.getCache().clear();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);

                        dependencyName = jsonObject.getString("Dependency Name");
                        devName = jsonObject.getString("Developer Name");
                        fullName = jsonObject.getString("Full Name");
                        githubLink = jsonObject.getString("Github Link");
                        @SuppressLint("ResourceType")
                        String bg = getString(R.color.whitetoblack);
                        cardBackground = bg;
                        youtube = jsonObject.getString("YouTube Link");
                        id = jsonObject.getString("Id");
                        license = jsonObject.getString("License");
                        licenseLink = jsonObject.getString("License Link");
                        cardSearch.add(new CardModel(dependencyName, devName, githubLink, cardBackground, youtube, id, license, licenseLink, fullName));
                        buildCardView();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        jsonArrayRequest.setShouldCache(false);
        requestQueue.add(jsonArrayRequest);

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
                        Toast.makeText(Search.this, "Dependency Id is only 4 digits. Check once", Toast.LENGTH_LONG).show();
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

    private void buildCardView() {
        cardViewSearch = new SearchAdapter(cardSearch, Search.this);
        linearLayoutManager = new LinearLayoutManager(Search.this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(cardViewSearch);
    }
}