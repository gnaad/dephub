package com.dephub.android.fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.dephub.android.R;
import com.dephub.android.cardview.DependencyAdapter;
import com.dephub.android.cardview.DependencyModel;
import com.dephub.android.utility.Widget;
import com.dephub.android.utility.Snippet;

import java.util.ArrayList;

public class Text extends Fragment {
    public static final String url = "https://gnaad.github.io/dephub/json/dependency.json";
    ProgressDialog progressDialog;
    LinearLayoutManager linearLayoutManager;
    private RecyclerView cardRecyclerViewText;
    private ArrayList<DependencyModel> cardText;
    private SwipeRefreshLayout swipeRefreshLayout;
    private DependencyAdapter cardViewAdapterText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Snippet.followNightModeInSystem();
        View view = inflater.inflate(R.layout.activity_fragment_text, container, false);

        cardRecyclerViewText = view.findViewById(R.id.textFragment);
        setRetainInstance(true);

        cardText = new ArrayList<>();

        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshTextFragment);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void run() {
                        cardText.clear();
                        cardViewAdapterText.notifyDataSetChanged();
                        swipeRefreshLayout.setRefreshing(false);
                        getDependency();
                    }
                }, 3000);
            }
        });
        showLoadingBar();
        getDependency();
        buildCardView();
        return view;
    }

    private void showLoadingBar() {
        progressDialog = new ProgressDialog(getContext(), R.style.CustomAlertDialog);
        progressDialog.setMessage("Loading...");
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    private void getDependency() {
        Snippet.dependencyArray(getContext(),
                url,
                response -> {
                    progressDialog.dismiss();
                    Snippet.fetchDependency("Text", getContext(), response, cardText, progressDialog);
                    buildCardView();
                },
                error -> {
                    Widget.alertDialog(getContext(),
                            false,
                            "Failed to load Dependencies. Please try again\n\nAlso Please check your Internet Connectivity or WiFi Connection",
                            "Retry",
                            null,
                            (dialog, which) -> {
                                getActivity().finish();
                                startActivity(getActivity().getIntent());
                            },
                            null);
                }
        );
    }

    private void buildCardView() {
        cardViewAdapterText = new DependencyAdapter(cardText, getActivity());
        linearLayoutManager = new LinearLayoutManager(getContext());
        cardRecyclerViewText.setHasFixedSize(true);
        cardRecyclerViewText.setLayoutManager(linearLayoutManager);
        cardRecyclerViewText.setAdapter(cardViewAdapterText);
    }
}