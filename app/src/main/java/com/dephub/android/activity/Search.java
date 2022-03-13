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
import com.dephub.android.cardview.Cardmodel;
import com.dephub.android.cardview.Searchadapter;
import com.github.aakira.compoundicontextview.CompoundIconTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Search extends AppCompatActivity {

    SearchView searchview;
    RecyclerView recyclerView;

    String depname, devname, gitlink, cardbg, youtube, id, license, licenselink, fullname;

    TextView centretext, notfound, count;
    Button submityourdependency;
    LinearLayoutManager linearLayoutManager;
    CompoundIconTextView compoundIconTextView;
    String url = "https://gnanendraprasadp.github.io/DepHub-Web/json/search.json";

    Searchadapter cardviewsearch;
    String[] background = {"#3F51B5","#FF5001","#F44336","#FFC107","#3BEC1B","#00BCD4","#FF8000"};
    private ArrayList<Cardmodel> cardsearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchview);

        searchview = findViewById(R.id.search);
        recyclerView = findViewById(R.id.searchrecyclerview);
        centretext = findViewById(R.id.nodep);
        compoundIconTextView = findViewById(R.id.nodependency);
        notfound = findViewById(R.id.notfound);
        submityourdependency = findViewById(R.id.syd);
        count = findViewById(R.id.depnum);


        Toolbar toolbar = findViewById(R.id.toolbarsearch);
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

        Drawable drawable = toolbar.getOverflowIcon( );

        DrawableCompat.setTint(drawable.mutate( ),getResources( ).getColor(R.color.toolbaricon));
        toolbar.setOverflowIcon(drawable);

        submityourdependency.setOnClickListener(new View.OnClickListener( ) {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Search.this,SubmitDependency.class);
                startActivity(intent);
            }
        });

        cardsearch = new ArrayList<>( );

        searchview.onActionViewExpanded( );

        centretext.setVisibility(View.VISIBLE);
        centretext.setText("Type Dependency name or Developer Name or Id\nTap 🔍\n\nHint : Long Hold on Dependency Name to know Id");

        RequestQueue requestQueue = Volley.newRequestQueue(Search.this);
        requestQueue.getCache( ).clear( );
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET,url,null,new Response.Listener<JSONArray>( ) {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length( ); i++) {
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);

                        depname = jsonObject.getString("Dependency Name");
                        devname = jsonObject.getString("Developer Name");
                        fullname = jsonObject.getString("Full Name");
                        gitlink = jsonObject.getString("Github Link");
                        @SuppressLint("ResourceType")
                        String bg = getString(R.color.whitetoblack);
                        cardbg = bg;
                        youtube = jsonObject.getString("YouTube Link");
                        id = jsonObject.getString("Id");
                        license = jsonObject.getString("License");
                        licenselink = jsonObject.getString("License Link");
                        cardsearch.add(new Cardmodel(depname,devname,gitlink,cardbg,youtube,id,license,licenselink,fullname));
                        buildcardview( );

                    } catch (JSONException e) {
                        e.printStackTrace( );
                    }
                }
            }
        },new Response.ErrorListener( ) {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        jsonArrayRequest.setShouldCache(false);
        requestQueue.add(jsonArrayRequest);

        searchview.setOnQueryTextListener(new SearchView.OnQueryTextListener( ) {
            @Override
            public boolean onQueryTextSubmit(String query) {
                View view = getCurrentFocus( );
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken( ),0);
                    searchview.clearFocus( );
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                centretext.setVisibility(View.INVISIBLE);
                count.setVisibility(View.VISIBLE);

                if (TextUtils.isDigitsOnly(newText)) {
                    if (newText.length( ) > 4) {
                        Toast.makeText(Search.this,"Dependency Id is only 4 digits. Check once",Toast.LENGTH_LONG).show( );
                    }
                }

                if (newText.length( ) > 0) {
                    count.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.VISIBLE);
                }

                if (newText.length( ) == 0 || newText.equals(" ")) {
                    count.setVisibility(View.INVISIBLE);
                    recyclerView.setVisibility(View.GONE);
                    centretext.setVisibility(View.VISIBLE);
                }

                filter(newText);
                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        View view = getCurrentFocus( );
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (searchview.hasFocus( )) {
            searchview.clearFocus( );
            imm.hideSoftInputFromWindow(view.getWindowToken( ),0);
        } else {
            super.onBackPressed( );
        }
    }

    @SuppressLint("SetTextI18n")
    private void filter(String text) {
        ArrayList<Cardmodel> filteredlist = new ArrayList<>( );

        for (Cardmodel item : cardsearch) {

            if (item.getDependencyname( ).toLowerCase( ).contains(text.toLowerCase( )) || item.getDevelopername( ).toLowerCase( ).contains(text.toLowerCase( )) ||
                    item.getId( ).toLowerCase( ).contains(text.toLowerCase( ))) {
                count.setText(filteredlist.size( ) + 1 + " results found");
                filteredlist.add(item);
            }
        }
        if (filteredlist.isEmpty( )) {
            compoundIconTextView.setVisibility(View.VISIBLE);
            count.setVisibility(View.INVISIBLE);
            notfound.setVisibility(View.VISIBLE);
            submityourdependency.setVisibility(View.VISIBLE);
            compoundIconTextView.setText(filteredlist.size( ) + " results found for \"" + text + "\"");
            recyclerView.setVisibility(View.GONE);
        } else {
            compoundIconTextView.setVisibility(View.GONE);
            notfound.setVisibility(View.GONE);
            submityourdependency.setVisibility(View.GONE);
            cardviewsearch.filterList(filteredlist);
        }
    }

    private void buildcardview() {
        cardviewsearch = new Searchadapter(cardsearch,Search.this);
        linearLayoutManager = new LinearLayoutManager(Search.this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(cardviewsearch);
    }
}