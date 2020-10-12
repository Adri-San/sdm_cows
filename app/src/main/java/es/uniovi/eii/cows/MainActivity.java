package es.uniovi.eii.cows;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActionBar;
import android.os.Bundle;
import android.util.Log;

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
        // TODO we show the news (substitute for RecyclerView)

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Close the connections when the app is stopped
        readersManager.shutdown();
    }
}