package es.uniovi.eii.cows;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import java.util.List;

import es.uniovi.eii.cows.model.NewsItem;
import es.uniovi.eii.cows.model.reader.ReadersManager;

public class MainActivity extends AppCompatActivity {

    private List<NewsItem> news;

    // Class managing all the NewsReading actions
    private final ReadersManager readersManager = ReadersManager.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // We start the pull and parse of news
        Log.d("holi", "holi");
        readersManager.run();
        // When finished we retrieve those parsed news
        news = readersManager.getNews();
        // TODO we show the news (substitute for RecyclerView)
        for (NewsItem n: news) {
            Log.d("News", n.toString());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Close the connections when the app is stopped
        readersManager.shutdown();
    }
}