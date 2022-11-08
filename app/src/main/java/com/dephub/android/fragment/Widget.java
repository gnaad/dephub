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
import com.dephub.android.cardview.CardAdapter;
import com.dephub.android.cardview.CardModel;
import com.dephub.android.common.Snippet;

import java.util.ArrayList;

public class Widget extends Fragment {
    public static final String url = "https://gnanendraprasadp.github.io/DepHub-Web/json/dependency.json";
    private RecyclerView cardRecyclerViewWidget;
    private ArrayList<CardModel> cardWidget;
    private SwipeRefreshLayout swipeRefreshLayout;
    LinearLayoutManager linearLayoutManager;
    private CardAdapter cardViewAdapterWidget;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Snippet.followNightModeInSystem();
        View view = inflater.inflate(R.layout.activity_fragment_widget, container, false);

        cardRecyclerViewWidget = view.findViewById(R.id.widgetFragment);
        setRetainInstance(true);

        cardWidget = new ArrayList<>();

        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshWidgetFragment);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void run() {
                        cardWidget.clear();
                        cardViewAdapterWidget.notifyDataSetChanged();
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
                    Snippet.fetchDependency("Widget", getContext(), response, cardWidget, null);
                    buildCardView();
                },
                error -> {
                }
        );
    }

    private void buildCardView() {
        cardViewAdapterWidget = new CardAdapter(cardWidget, getActivity());
        linearLayoutManager = new LinearLayoutManager(getContext());
        cardRecyclerViewWidget.setHasFixedSize(true);
        cardRecyclerViewWidget.setLayoutManager(linearLayoutManager);
        cardRecyclerViewWidget.setAdapter(cardViewAdapterWidget);
    }
}