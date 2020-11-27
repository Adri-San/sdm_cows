package es.uniovi.eii.cows.view;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import es.uniovi.eii.cows.R;
import es.uniovi.eii.cows.controller.reader.ReadersManager;
import es.uniovi.eii.cows.data.helper.FirebaseHelper;
import es.uniovi.eii.cows.model.NewsItem;
import es.uniovi.eii.cows.view.adapter.NewsAdapter;

public class MainActivity extends AppCompatActivity {

    // Model
    private List<NewsItem> news;

    //Clicked news item
    public static final String SELECTED_NEWS_ITEM = "selected_news_item";

    // Class managing all the NewsReading actions
    private final ReadersManager readersManager = ReadersManager.getInstance();


    private SwipeRefreshLayout swipeRefreshLayout;  //Pull to refresh item
    private ProgressBar loadingNewsSpinner;         //Loading spinner until news are ready

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
        //Stores on database
        storeNewsItems(news);
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

        configurePullToRefresh();   //Pull to refresh news initialization
        configureLoadingSpinner();  //Loading spinner until newsItems are ready

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
        swipeRefreshLayout.setOnRefreshListener(this::onPullToRefresh);
    }

    private void configureLoadingSpinner(){
        loadingNewsSpinner = (ProgressBar) findViewById(R.id.loadingNewsSpinner);
        loadingNewsSpinner.setVisibility(View.VISIBLE);

    }

    private void onPullToRefresh() {
        //Start of the refreshing
        swipeRefreshLayout.setRefreshing(true);

        //Reloading news
        reloadNews();

        //Updating database
        storeNewsItems(news);
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

    /**
     * Stores the specified list of news items into the database.
     * It also assigns an ID to new newsItems without one
     *
     * @param newsItems list of newsItems to be stored
     */
    public void storeNewsItems(List<NewsItem> newsItems){
        newsItems.forEach(newsItem -> FirebaseHelper.getInstance().addNewsItem(newsItem,
                n-> {checkIfNewsItemsAreReady(); return null; }));
    }

    /**
     * If all news items have an ID assigned, then they will be presented to the user.
     * Note that ID assignation is an asynchronous process
     */
    private void checkIfNewsItemsAreReady(){
        int numberOfNewsWithID = (int) news.stream().filter(n -> n.getId() != null).count();

        if(numberOfNewsWithID >= news.size()){
            newsAdapter.setNewsItems(news);
            //Stop spinners
            loadingNewsSpinner.setVisibility(View.GONE);
            swipeRefreshLayout.setRefreshing(false);
        }
    }
}