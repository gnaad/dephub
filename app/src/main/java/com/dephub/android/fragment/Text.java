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
import java.util.Collections;
import java.util.Comparator;

public class Text extends Fragment {
    public static final String url = "https://android-dephub.web.app/json/fragmentfollowedbytext.json";
    ProgressDialog progressDialog;
    LinearLayoutManager linearLayoutManager;
    private RecyclerView cardrecyclerviewtext;
    private Cardadapter cardviewadaptertext;
    private ArrayList<Cardmodel> cardtext;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);

        View view = inflater.inflate(R.layout.activity_fragment_text,container,false);

        cardrecyclerviewtext = view.findViewById(R.id.textfragment);
        setRetainInstance(true);

        cardtext = new ArrayList<>( );

        progressDialog = new ProgressDialog(getContext( ),R.style.CustomAlertDialog);
        progressDialog.setMessage("Loading");
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.show( );

        getdependency( );

        buildcardview( );

        return view;
    }

    private void getdependency() {
        @SuppressWarnings("ConstantConditions")
        RequestQueue requestQueue = Volley.newRequestQueue(getContext( ));
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
                        cardtext.add(new Cardmodel(depname,devname,gitlink,bg,youtube,id,license,licenselink,fullname));

                        Collections.sort(cardtext,new Comparator<Cardmodel>( ) {
                            @Override
                            public int compare(Cardmodel o1,Cardmodel o2) {
                                return o1.getDependencyname( ).compareTo(o2.getDependencyname( ));
                            }
                        });

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

        cardviewadaptertext = new Cardadapter(cardtext,getActivity( ));

        linearLayoutManager = new LinearLayoutManager(getContext( ));
        cardrecyclerviewtext.setHasFixedSize(true);
        cardrecyclerviewtext.setLayoutManager(linearLayoutManager);
        cardrecyclerviewtext.setAdapter(cardviewadaptertext);
    }
}