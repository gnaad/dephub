package com.dephub.android.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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

public class Top10 extends Fragment {
    public static final String url = "https://gnanendraprasadp.github.io/DepHub-Web/json/leaderboardfollowedbytop10.json";
    private RecyclerView cardrecyclerviewtopten;
    private Cardadapter cardviewadaptertopten;
    private ArrayList<Cardmodel> cardtopten;
    private ProgressDialog progressDialog;

    @SuppressLint("SetJavaScriptEnabled")

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);

        View view = inflater.inflate(R.layout.activity_fragment_top10,container,false);

        cardrecyclerviewtopten = view.findViewById(R.id.top10);
        setRetainInstance(true);

        progressDialog = new ProgressDialog(getContext( ),R.style.CustomAlertDialog);
        progressDialog.setMessage("Loading");
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.show( );

        cardtopten = new ArrayList<>( );

        getdependency( );

        buildcardview( );

        return view;
    }

    private void getdependency() {
        @SuppressWarnings("ConstantConditions") RequestQueue requestQueue = Volley.newRequestQueue(getContext( ));
        requestQueue.getCache().clear();

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET,url,null,new Response.Listener<JSONArray>( ) {
            @Override
            public void onResponse(JSONArray response) {
                progressDialog.dismiss( );
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
                        cardtopten.add(new Cardmodel(depname,devname,gitlink,bg,youtube,id,license,licenselink,fullname));
                        buildcardview( );
                    } catch (JSONException e) {
                        progressDialog.dismiss( );
                        e.printStackTrace( );
                    }
                }
            }
        },new Response.ErrorListener( ) {
            @Override
            public void onErrorResponse(VolleyError error) {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext( ),R.style.CustomAlertDialog);

                alertDialogBuilder.setCancelable(false);
                alertDialogBuilder.setMessage("Failed to load Dependencies. Please try again\n\nAlso Please check your Internet Connectivity or WiFi Connection");
                alertDialogBuilder.setPositiveButton("Retry",new DialogInterface.OnClickListener( ) {
                    public void onClick(DialogInterface dialog,int id) {
                        //noinspection ConstantConditions
                        getActivity( ).finish( );
                        startActivity(getActivity( ).getIntent( ));
                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create( );
                alertDialog.show( );
                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources( ).getColor(R.color.colorAccent));
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources( ).getColor(R.color.colorAccent));
            }
        });
        jsonArrayRequest.setShouldCache(false);
        requestQueue.add(jsonArrayRequest);
    }

    private void buildcardview() {

        cardviewadaptertopten = new Cardadapter(cardtopten,getActivity( ));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext( ));
        cardrecyclerviewtopten.setHasFixedSize(true);
        cardrecyclerviewtopten.setLayoutManager(linearLayoutManager);
        cardrecyclerviewtopten.setAdapter(cardviewadaptertopten);
    }
}