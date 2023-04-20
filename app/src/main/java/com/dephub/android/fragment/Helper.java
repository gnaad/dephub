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

public class Helper extends Fragment {
    public static final String url = "https://gnanendraprasadp.github.io/dephub/json/dependency.json";
    private RecyclerView cardRecyclerViewHelper;
    private ArrayList<DependencyModel> cardHelper;
    private SwipeRefreshLayout swipeRefreshLayout;
    LinearLayoutManager linearLayoutManager;
    private DependencyAdapter cardViewAdapterHelper;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Snippet.followNightModeInSystem();
        View view = inflater.inflate(R.layout.activity_fragment_helper, container, false);

        cardRecyclerViewHelper = view.findViewById(R.id.helperFragment);
        setRetainInstance(true);

        cardHelper = new ArrayList<>();

        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshHelperFragment);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void run() {
                        cardHelper.clear();
                        cardViewAdapterHelper.notifyDataSetChanged();
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
                    Snippet.fetchDependency("Helper", getContext(), response, cardHelper, null);
                    buildCardView();
                }, error -> {
                }
        );
    }

    private void buildCardView() {
        cardViewAdapterHelper = new DependencyAdapter(cardHelper, getActivity());
        linearLayoutManager = new LinearLayoutManager(getContext());
        cardRecyclerViewHelper.setHasFixedSize(true);
        cardRecyclerViewHelper.setLayoutManager(linearLayoutManager);
        cardRecyclerViewHelper.setAdapter(cardViewAdapterHelper);
    }
}