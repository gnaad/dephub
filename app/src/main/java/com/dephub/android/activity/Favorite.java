package com.dephub.android.activity;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dephub.android.R;
import com.dephub.android.cardview.CardAdapter;
import com.dephub.android.cardview.CardModel;
import com.dephub.android.common.Snippet;
import com.dephub.android.favorite.DatabaseHelper;
import com.github.aakira.compoundicontextview.CompoundIconTextView;

import java.util.ArrayList;

public class Favorite extends AppCompatActivity {
    DatabaseHelper databaseHelper;
    LinearLayoutManager linearLayoutManager;
    CompoundIconTextView compoundIconTextView;
    private RecyclerView recyclerView;
    private ArrayList<CardModel> cardFavorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Snippet.followNightModeInSystem();
        setContentView(R.layout.activity_favorite);

        getWindow().setNavigationBarColor(getResources().getColor(R.color.black));

        recyclerView = findViewById(R.id.favoriteRecyclerView);
        compoundIconTextView = findViewById(R.id.noDependencyFavorites);
        databaseHelper = new DatabaseHelper(Favorite.this);
        cardFavorite = new ArrayList<>();

        buildCardView();

        Toolbar toolbar = findViewById(R.id.toolbarFavorites);
        toolbar.setTitle("My Favorites");
        toolbar.setNavigationIcon(R.drawable.ic_back);
        Snippet.toolbar(Favorite.this, toolbar);
        setSupportActionBar(toolbar);

        TextView textView = findViewById(R.id.favoritesText);

        Cursor cursor = databaseHelper.getFavorite();
        if (cursor != null && cursor.getCount() > 0) {

            while (cursor.moveToNext()) {
                @SuppressLint("ResourceType") String backgroundColor = getString(R.color.whitetoblack);
                cardFavorite.add(new CardModel(
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        backgroundColor,
                        cursor.getString(9),
                        cursor.getString(1),
                        cursor.getString(6),
                        cursor.getString(7),
                        cursor.getString(5)
                ));
            }
        } else {
            compoundIconTextView.setVisibility(View.VISIBLE);
            textView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        databaseHelper.close();
        super.onDestroy();
    }

    private void buildCardView() {
        CardAdapter cardAdapter = new CardAdapter(cardFavorite, Favorite.this);
        linearLayoutManager = new LinearLayoutManager(Favorite.this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(cardAdapter);
    }
}