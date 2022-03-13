package com.dephub.android.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.dephub.android.R;
import com.dephub.android.cardview.Cardadapter;
import com.dephub.android.cardview.Cardmodel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Widget extends Fragment {
    public static final String url = "https://gnanendraprasadp.github.io/DepHub-Web/json/fragmentfollowedbywidget.json";
    private RecyclerView cardrecyclerviewwidget;
    private Cardadapter cardviewadapterwidget;
    private ArrayList<Cardmodel> cardwidget;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);

        View view = inflater.inflate(R.layout.activity_fragment_widget,container,false);

        cardrecyclerviewwidget = view.findViewById(R.id.widgetfragment);
        setRetainInstance(true);

        cardwidget = new ArrayList<>( );

        getdependency( );

        buildcardview( );

        return view;
    }

    private void getdependency() {
        @SuppressWarnings("ConstantConditions") RequestQueue requestQueue = Volley.newRequestQueue(getContext( ));
        requestQueue.getCache( ).clear( );

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET,url,null,new Response.Listener<JSONArray>( ) {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length( ); i++) {

                    try {
                        JSONObject jsonObject = response.getJSONObject(i);

                        String depname = jsonObject.getString("Dependency Name");
                        String devname = jsonObject.getString("Developer Name");
                        String fullname = jsonObject.getString("Full Name");
                        String gitlink = jsonObject.getString("Github Link");
                        @SuppressLint("ResourceType")
                        String bg = getString(R.color.whitetoblack);
                        String youtube = jsonObject.getString("YouTube Link");
                        String id = jsonObject.getString("Id");
                        String license = jsonObject.getString("License");
                        String licenselink = jsonObject.getString("License Link");
                        cardwidget.add(new Cardmodel(depname,devname,gitlink,bg,youtube,id,license,licenselink,fullname));

                        Collections.sort(cardwidget,new Comparator<Cardmodel>( ) {
                            @Override
                            public int compare(Cardmodel o1,Cardmodel o2) {
                                return o1.getDependencyname( ).compareTo(o2.getDependencyname( ));
                            }
                        });

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
    }

    private void buildcardview() {
        cardviewadapterwidget = new Cardadapter(cardwidget,getActivity( ));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext( ));
        cardrecyclerviewwidget.setHasFixedSize(true);
        cardrecyclerviewwidget.setLayoutManager(linearLayoutManager);
        cardrecyclerviewwidget.setAdapter(cardviewadapterwidget);
    }
}