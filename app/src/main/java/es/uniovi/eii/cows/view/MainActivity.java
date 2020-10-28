package es.uniovi.eii.cows.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import es.uniovi.eii.cows.R;
import es.uniovi.eii.cows.model.NewsItem;
import es.uniovi.eii.cows.view.adapter.NewsAdapter;
import es.uniovi.eii.cows.controller.reader.ReadersManager;

public class MainActivity extends AppCompatActivity {

    // Model
    private List<NewsItem> news;
    private List<NewsItem> newsToShow;

    //Clicked news item
    public static final String SELECTED_NEWS_ITEM = "selected_news_item";

    // Class managing all the NewsReading actions
    private final ReadersManager readersManager = ReadersManager.getInstance();

    // RecyclerView that contains the news
    RecyclerView newsView;
    // Adapter for RecyclerView
    NewsAdapter newsAdapter;

    // Check if news are loading
    boolean isLoading = false;
    // Limit of news to load
    public static final int LIMIT = 3;
    // Index of last item loaded
    private int indexNewsLoaded = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar(findViewById(R.id.app_bar));
        // We start the pull and parse of news
        readersManager.run();
        // When finished we retrieve those parsed news
        news = new ArrayList<>(readersManager.getNews());

        newsToShow = new ArrayList<>();
        // Load only LIMIT news
        loadSomeNews();
        // We set up the news list
        setUpRecyclerView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Close the connections when the app is stopped
        readersManager.shutdown();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_profile) {
            Toast.makeText(MainActivity.this, "Action clicked", Toast.LENGTH_LONG).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Load LIMIT news
     */
    private void loadSomeNews() {
        int numberLoaded = 0;
        for (int i = indexNewsLoaded; i < indexNewsLoaded + LIMIT; i++) {
            newsToShow.add(news.get(i));
            numberLoaded++;
        }
        indexNewsLoaded += numberLoaded;
    }

    private void setUpRecyclerView() {
        // Show the news on the RecyclerView
        newsView = findViewById(R.id.idRecycler_main);
        newsView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        newsView.setLayoutManager(layoutManager);

        newsAdapter = new NewsAdapter(newsToShow, this::clickOnNewsItem);
        newsView.setAdapter(newsAdapter);

        // Check scrolled state of the RecyclerView
        initScrollListener();
    }

    /**
     * Sets scroll listener for ProgressBar
     */
    private void initScrollListener() {
        newsView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                // Load more news
                if (!isLoading) {
                    Log.i("Number of news loaded", String.valueOf(newsToShow.size()));
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == newsToShow.size() - 1) {
                        //bottom of list
                        updateNewsAdapter();
                        isLoading = true;
                    }
                }
            }
        });
    }

    /**
     * Notifies NewsAdapter of changes and calls loadSomeNews()
     */
    private void updateNewsAdapter() {
        newsToShow.add(null);
        // Notify in next frame
        newsView.post(() -> newsAdapter.notifyItemInserted(newsToShow.size() - 1));

        Handler handler = new Handler();
        handler.postDelayed(() -> {
            // Remove last item of the list
            newsToShow.remove(newsToShow.get(newsToShow.size() - 1));
            int scrollPosition = newsToShow.size();
            newsAdapter.notifyItemRemoved(scrollPosition);

            loadSomeNews();

            newsAdapter.notifyDataSetChanged();
            isLoading = false;
        }, 2000);
    }

    private void clickOnNewsItem(NewsItem item) {

        //Intent to start NewsActivity
        Intent intent = new Intent(MainActivity.this, NewsActivity.class);
        intent.putExtra(SELECTED_NEWS_ITEM, item);

        //Animation
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());

    }
}