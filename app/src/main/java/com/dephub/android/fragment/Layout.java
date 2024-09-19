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

public class Layout extends Fragment {
    public static final String url = "https://gnaad.github.io/dephub/json/dependency.json";
    private RecyclerView cardRecyclerViewLayout;
    private ArrayList<DependencyModel> cardLayout;
    private SwipeRefreshLayout swipeRefreshLayout;
    LinearLayoutManager linearLayoutManager;
    private DependencyAdapter cardViewAdapterLayout;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Snippet.followNightModeInSystem();
        View view = inflater.inflate(R.layout.activity_fragment_layout, container, false);

        cardRecyclerViewLayout = view.findViewById(R.id.layoutFragment);
        setRetainInstance(true);

        cardLayout = new ArrayList<>();

        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayoutFragment);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void run() {
                        cardLayout.clear();
                        cardViewAdapterLayout.notifyDataSetChanged();
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
                    Snippet.fetchDependency("Layout", getContext(), response, cardLayout, null);
                    buildCardView();
                }, error -> {
                }
        );
    }

    private void buildCardView() {
        cardViewAdapterLayout = new DependencyAdapter(cardLayout, getActivity());
        linearLayoutManager = new LinearLayoutManager(getContext());
        cardRecyclerViewLayout.setHasFixedSize(true);
        cardRecyclerViewLayout.setLayoutManager(linearLayoutManager);
        cardRecyclerViewLayout.setAdapter(cardViewAdapterLayout);
    }
}