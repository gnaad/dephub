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

public class Google extends Fragment {
    public static final String url = "https://gnanendraprasadp.github.io/dephub/json/dependency.json";
    private RecyclerView cardRecyclerViewGoogle;
    private ArrayList<DependencyModel> cardGoogle;
    private SwipeRefreshLayout swipeRefreshLayout;
    LinearLayoutManager linearLayoutManager;
    private DependencyAdapter cardViewAdapterGoogle;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Snippet.followNightModeInSystem();
        View view = inflater.inflate(R.layout.activity_fragment_google, container, false);

        cardRecyclerViewGoogle = view.findViewById(R.id.googleFragment);
        setRetainInstance(true);

        cardGoogle = new ArrayList<>();

        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshGoogleFragment);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void run() {
                        cardGoogle.clear();
                        cardViewAdapterGoogle.notifyDataSetChanged();
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
                    Snippet.fetchDependency("Google", getContext(), response, cardGoogle, null);
                    buildCardView();
                }, error -> {
                }
        );
    }

    private void buildCardView() {
        cardViewAdapterGoogle = new DependencyAdapter(cardGoogle, getActivity());
        linearLayoutManager = new LinearLayoutManager(getContext());
        cardRecyclerViewGoogle.setHasFixedSize(true);
        cardRecyclerViewGoogle.setLayoutManager(linearLayoutManager);
        cardRecyclerViewGoogle.setAdapter(cardViewAdapterGoogle);
    }
}