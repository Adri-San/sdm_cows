package es.uniovi.eii.cows;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import org.xmlpull.v1.XmlPullParserException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import es.uniovi.eii.cows.model.NewsItem;
import es.uniovi.eii.cows.model.reader.ReadersFactory;
import es.uniovi.eii.cows.model.reader.NewsReader;
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
        readersManager.run();
        // When finished we retrieve those parsed news
        news = readersManager.getNews();
        // TODO we show the news (substitute for RecyclerView)
        news.forEach(NewsItem::toString);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Close the connections when the app is stopped
        readersManager.shutdown();
    }
}