package es.uniovi.eii.cows.view;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import es.uniovi.eii.cows.R;
import es.uniovi.eii.cows.controller.reader.ReadersManager;
import es.uniovi.eii.cows.data.helper.FirebaseHelper;
import es.uniovi.eii.cows.model.NewsItem;
import es.uniovi.eii.cows.view.adapter.NewsAdapter;

public class MainActivity extends AppCompatActivity {
    // Actions
    public static final String SELECTED_NEWS_ITEM = "selected_news_item";
    // Logging
    private GoogleSignInClient client;
    // UI
    private DrawerLayout drawerLayout;
    private NewsAdapter newsAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;  // Pull to refresh item
    private ProgressBar loadingNewsSpinner;         // Loading spinner until news are ready
    // Class managing all the NewsReading actions
    private final ReadersManager readersManager = ReadersManager.getInstance();
    // List of news to display
    private List<NewsItem> news;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        // Configure the Google Auth Options            TODO extract to singleton helper
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .requestProfile()
                .build();
        client = GoogleSignIn.getClient(this, gso);
        // Set the toolbar
        Toolbar toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        // Set the navigation drawer
        setUpNavigation(toolbar);
        // Set the user info
        // TODO image of the user
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        // Start the pull and parse of news
        readersManager.run();
        // When finished retrieve those parsed news
        news = readersManager.getNews();
        // Store on database
        storeNewsItems(news);
        // Set up the news list
        setUpRecyclerView();
    }

    private void setUpNavigation(Toolbar toolbar) {
        // Set the drawer layout
        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        // Set the Navigation View
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.nav_saved:
                    redirectToSavedNews();
                    break;
                case R.id.nav_settings:
                    throw new IllegalArgumentException("menu option not implemented!!");
                case R.id.nav_logout:
                    FirebaseAuth.getInstance().signOut();
                    client.signOut().addOnCompleteListener(this,
                            task -> returnToAuthActivity());
                    break;
                default:
                    throw new IllegalArgumentException("menu option not implemented!!");
            }
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });
    }

    private void redirectToSavedNews() {
        //Intent to start SavedActivity
        Intent intent = new Intent(MainActivity.this, SavedActivity.class);
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }

    private void setUpRecyclerView() {
        configurePullToRefresh();   //Pull to refresh news initialization
        configureLoadingSpinner();  //Loading spinner until newsItems are ready
        // Show the news on the RecyclerView
        RecyclerView newsView = (RecyclerView) findViewById(R.id.idRecycler_main);
        newsView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        newsView.setLayoutManager(layoutManager);
        // Sets adapter
        newsAdapter = new NewsAdapter(new ArrayList<>(news), this::clickOnNewsItem, R.layout.line_news_view);
        newsView.setAdapter(newsAdapter);
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
            Toast.makeText(MainActivity.this, "Muuuu", Toast.LENGTH_LONG).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        // Closes drawer if Back is pressed when it is open
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    private void configurePullToRefresh(){

        swipeRefreshLayout = findViewById(R.id.pullToRefresh_main);
        swipeRefreshLayout.setColorSchemeResources(R.color.primaryColor);
        swipeRefreshLayout.setProgressBackgroundColorSchemeResource(R.color.design_default_color_background);

        //onRefresh listener
        swipeRefreshLayout.setOnRefreshListener(this::onPullToRefresh);
    }

    private void configureLoadingSpinner(){
        loadingNewsSpinner = findViewById(R.id.loadingNewsSpinner);
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

    /**
     * Gets the news again
     */
    private void reloadNews(){
        readersManager.run();
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

    /**
     * When logging out, the user is returned to the log in activity
     */
    private void returnToAuthActivity() {
        startActivity(new Intent(this, AuthActivity.class));
    }
}