package es.uniovi.eii.cows.view;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import es.uniovi.eii.cows.R;
import es.uniovi.eii.cows.data.helper.FirebaseHelper;
import es.uniovi.eii.cows.model.NewsItem;
import es.uniovi.eii.cows.view.adapter.NewsAdapter;

public class SavedActivity extends AppCompatActivity {
    // Actions
    public static final String SELECTED_NEWS_ITEM = "selected_news_item";
    // UI
    private NewsAdapter savedAdapter;
    private ProgressBar loadingNewsSpinner;         // Loading spinner until news are ready
    private RecyclerView savedView;
    // List of news to display
    private final List<NewsItem> newsSaved = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved);

        // Toolbar
        Toolbar toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(view -> finish());
    }

    @Override
    protected void onResume() {
        super.onResume();
        newsSaved.clear();
        // Pre-configuration
        preconfigure();
        // Get news saved
        getSavedNews();
        // SetUp RecyclerView
        setUpRecyclerView();
    }

    private void setUpRecyclerView() {
        // Show the news on the RecyclerView
        savedView = findViewById(R.id.idRecycler_saved);
        savedView.setHasFixedSize(true);
        // Change layout view by orientation
        configureLayoutByOrientation();
        // Sets adapter
        savedAdapter = new NewsAdapter(new ArrayList<>(newsSaved), this::clickOnNewsItem, R.layout.line_news_saved);
        savedView.setAdapter(savedAdapter);
        loadingNewsSpinner.setVisibility(View.GONE);
    }

    private void configureLayoutByOrientation() {
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            savedView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        } else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            savedView.setLayoutManager(new GridLayoutManager(getApplicationContext(),2));
        }
    }

    private void clickOnNewsItem(NewsItem item) {
        //Intent to start NewsActivity
        Intent intent = new Intent(SavedActivity.this, NewsActivity.class);
        intent.putExtra(SELECTED_NEWS_ITEM, item);
        //Animation
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }

    private void preconfigure() {
        loadingNewsSpinner = findViewById(R.id.loadingNewsSpinner);
        loadingNewsSpinner.setVisibility(View.VISIBLE);
        findViewById(R.id.txtNoNews).setVisibility(View.VISIBLE);
    }

    private void getSavedNews() {
        FirebaseHelper.getInstance().getSavedNewsItems(n -> {
            addNewsItem(n);
            //Stop spinner
            findViewById(R.id.txtNoNews).setVisibility(View.GONE);
            loadingNewsSpinner.setVisibility(View.GONE);
            return null;
        });
    }

    private void addNewsItem(NewsItem n) {
        Log.d("Saved item: ", n.getId());
        newsSaved.add(n);
        savedAdapter.setNewsItems(newsSaved);
    }

}
