package es.uniovi.eii.cows.view;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import es.uniovi.eii.cows.R;
import es.uniovi.eii.cows.model.NewsItem;

public class NewsActivity extends AppCompatActivity {

    private NewsItem newsItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        //setSupportActionBar(findViewById(R.id.app_bar));

        //Receiving intent
        Intent newsIntent = getIntent();
        newsItem = newsIntent.getParcelableExtra(MainActivity.SELECTED_NEWS_ITEM);

    }
}
