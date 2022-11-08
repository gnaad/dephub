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

public class Button extends Fragment {
    public static final String url = "https://gnanendraprasadp.github.io/DepHub-Web/json/dependency.json";
    private RecyclerView cardRecyclerViewButton;
    private ArrayList<CardModel> cardButton;
    private SwipeRefreshLayout swipeRefreshLayout;
    LinearLayoutManager linearLayoutManager;
    private CardAdapter cardViewAdapterButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Snippet.followNightModeInSystem();
        View view = inflater.inflate(R.layout.activity_fragment_button, container, false);

        cardRecyclerViewButton = view.findViewById(R.id.buttonFragment);
        setRetainInstance(true);

        cardButton = new ArrayList<>();

        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshButtonFragment);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void run() {
                        cardButton.clear();
                        cardViewAdapterButton.notifyDataSetChanged();
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
                    Snippet.fetchDependency("Button", getContext(), response, cardButton, null);
                    buildCardView();
                },
                error -> {
                }
        );
    }

    private void buildCardView() {
        cardViewAdapterButton = new CardAdapter(cardButton, getActivity());
        linearLayoutManager = new LinearLayoutManager(getContext());
        cardRecyclerViewButton.setHasFixedSize(true);
        cardRecyclerViewButton.setLayoutManager(linearLayoutManager);
        cardRecyclerViewButton.setAdapter(cardViewAdapterButton);
    }
}