package es.uniovi.eii.cows;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import es.uniovi.eii.cows.model.NewsItem;
import es.uniovi.eii.cows.model.adapter.NewsAdapter;
import es.uniovi.eii.cows.model.reader.ReadersManager;

public class MainActivity extends AppCompatActivity {
    // View
    private RecyclerView newsView;

    // Model
    private List<NewsItem> news;

    // Class managing all the NewsReading actions
    private final ReadersManager readersManager = ReadersManager.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //getActionBar().hide();

        // We start the pull and parse of news
        readersManager.run();
        // When finished we retrieve those parsed news
        news = readersManager.getNews();

        setSupportActionBar(findViewById(R.id.app_bar));
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
        // Show the news on the RecyclerView
        newsView = (RecyclerView) findViewById(R.id.idRecycler_main);
        newsView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        newsView.setLayoutManager(layoutManager);

        NewsAdapter newsAdapter = new NewsAdapter(news, item -> {
            //TODO
        });

        newsView.setAdapter(newsAdapter);
    }
}