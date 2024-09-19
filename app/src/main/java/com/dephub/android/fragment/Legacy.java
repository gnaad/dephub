package com.dephub.android.fragment;

import android.annotation.SuppressLint;
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
import com.dephub.android.utility.Snippet;

import java.util.ArrayList;

public class Legacy extends Fragment {
    public static final String url = "https://gnaad.github.io/dephub/json/dependency.json";
    private RecyclerView cardRecyclerViewLegacy;
    private ArrayList<DependencyModel> cardLegacy;
    private SwipeRefreshLayout swipeRefreshLayout;
    LinearLayoutManager linearLayoutManager;
    private DependencyAdapter cardViewAdapterLegacy;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Snippet.followNightModeInSystem();
        View view = inflater.inflate(R.layout.activity_fragment_legacy, container, false);

        cardRecyclerViewLegacy = view.findViewById(R.id.legacyFragment);
        setRetainInstance(true);

        cardLegacy = new ArrayList<>();

        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLegacyFragment);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void run() {
                        cardLegacy.clear();
                        cardViewAdapterLegacy.notifyDataSetChanged();
                        swipeRefreshLayout.setRefreshing(false);
                        getDependency();
                    }
                }, 3000);
            }
        });
        getDependency();
        buildCardView();
        return view;
    }

    private void getDependency() {
        Snippet.dependencyArray(getContext(),
                url,
                response -> {
                    Snippet.fetchDependency("Legacy", getContext(), response, cardLegacy, null);
                    buildCardView();
                }, error -> {
                }
        );
    }

    private void buildCardView() {
        cardViewAdapterLegacy = new DependencyAdapter(cardLegacy, getActivity());
        linearLayoutManager = new LinearLayoutManager(getContext());
        cardRecyclerViewLegacy.setHasFixedSize(true);
        cardRecyclerViewLegacy.setLayoutManager(linearLayoutManager);
        cardRecyclerViewLegacy.setAdapter(cardViewAdapterLegacy);
    }
}