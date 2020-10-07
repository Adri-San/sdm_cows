package es.uniovi.eii.cows;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import org.xmlpull.v1.XmlPullParserException;

import java.util.ArrayList;
import java.util.List;

import es.uniovi.eii.cows.model.NewsItem;
import es.uniovi.eii.cows.model.rss.ReadersFactory;
import es.uniovi.eii.cows.model.rss.NewsReader;

public class MainActivity extends AppCompatActivity {

    private List<NewsReader> readers;
    private List<NewsItem> news;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            news = new ArrayList<>();
            readers = ReadersFactory.getInstance().getReaders();

            for (NewsReader loader : readers) {
                Thread thread = new Thread(() -> {
                    try {
                        loader.run();
                        while (loader.getNews() == null) {
                            // this is horrible but it is just a try
                        }
                        loader.getNews().forEach(x -> Log.i("TITLE", x.getTitle()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                // TODO add thread to list to treat it later
                thread.start();
            }
        } catch (XmlPullParserException xppe)  {
            // TODO catch RSSReaderException and TwitterReaderException
        }
    }
}