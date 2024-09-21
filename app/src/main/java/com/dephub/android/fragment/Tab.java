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
import com.dephub.android.cardview.Dependency;
import com.dephub.android.constant.ApplicationConstant;
import com.dephub.android.utility.Snippet;
import com.dephub.android.utility.Widget;

import java.util.ArrayList;

public class Tab extends Fragment {
    private ArrayList<Dependency> tabArrayList;
    private DependencyAdapter tabAdapter;
    private RecyclerView tabRecyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;

    LinearLayoutManager linearLayoutManager;
    ProgressDialog progressDialog;
    String type;

    public Tab(String type) {
        this.type = type;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Snippet.followNightModeInSystem();
        View view = inflater.inflate(R.layout.activity_tab, container, false);

        tabRecyclerView = view.findViewById(R.id.fragment);
        setRetainInstance(true);

        tabArrayList = new ArrayList<>();
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_text_fragment);

        swipeRefreshLayout.setOnRefreshListener(() -> new Handler().postDelayed(new Runnable() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void run() {
                tabArrayList.clear();
                tabAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
                getDependency();
            }
        }, 3000));
        showLoadingBar();
        getDependency();
        buildCardView();
        return view;
    }

    private void showLoadingBar() {
        progressDialog = new ProgressDialog(getContext(), R.style.customAlertDialog);
        progressDialog.setMessage(ApplicationConstant.LOADING);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    private void getDependency() {
        Snippet.dependencyArray(getContext(),
                ApplicationConstant.DEPENDENCY,
                response -> {
                    progressDialog.dismiss();
                    Snippet.fetchDependency(type, response, tabArrayList);
                    buildCardView();
                },
                error -> {
                    Widget.alertDialog(getContext(),
                            false,
                            "Failed to load Dependencies. Please try again\n\nAlso Please check your Internet Connectivity or WiFi Connection",
                            ApplicationConstant.RETRY,
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
        tabAdapter = new DependencyAdapter(tabArrayList, getActivity());
        linearLayoutManager = new LinearLayoutManager(getContext());
        tabRecyclerView.setHasFixedSize(true);
        tabRecyclerView.setLayoutManager(linearLayoutManager);
        tabRecyclerView.setAdapter(tabAdapter);
    }
}