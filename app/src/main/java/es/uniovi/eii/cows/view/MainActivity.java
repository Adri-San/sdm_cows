package es.uniovi.eii.cows.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
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

    //Clicked news item
    public static final String SELECTED_NEWS_ITEM = "selected_news_item";

    // Class managing all the NewsReading actions
    private final ReadersManager readersManager = ReadersManager.getInstance();

    //Pull to refresh item
    private SwipeRefreshLayout swipeRefreshLayout;

    //News Adapter
    private NewsAdapter newsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar(findViewById(R.id.app_bar));
        // We start the pull and parse of news
        readersManager.run();
        // When finished we retrieve those parsed news
        news = readersManager.getNews();
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

    private void setUpRecyclerView() {
        //Pull to refresh news initialization
        configurePullToRefresh();

        // Show the news on the RecyclerView
        RecyclerView newsView = (RecyclerView) findViewById(R.id.idRecycler_main);
        newsView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        newsView.setLayoutManager(layoutManager);

        newsAdapter = new NewsAdapter(new ArrayList<>(news), this::clickOnNewsItem);
        newsView.setAdapter(newsAdapter);
    }

    private void configurePullToRefresh(){

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.pullToRefresh_main);
        swipeRefreshLayout.setColorSchemeResources(R.color.primaryColor);
        swipeRefreshLayout.setProgressBackgroundColorSchemeResource(R.color.design_default_color_background);

        //onRefresh listener
        swipeRefreshLayout.setOnRefreshListener(() -> onPullToRefresh());
    }

    private void onPullToRefresh() {
        //Start of the refreshing
        swipeRefreshLayout.setRefreshing(true);

        //Reloading news
        reloadNews();

        //Setting the refreshed news items list
        newsAdapter.setNewsItems(new ArrayList<>(news));

        //end of the refreshing
        swipeRefreshLayout.setRefreshing(false);
    }

    private void clickOnNewsItem(NewsItem item) {

        //Intent to start NewsActivity
        Intent intent = new Intent(MainActivity.this, NewsActivity.class);
        intent.putExtra(SELECTED_NEWS_ITEM, item);

        //Animation
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());

    }

    private void reloadNews(){
        readersManager.rerun();
        news = readersManager.getNews();
    }
}