package es.uniovi.eii.cows.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.threeten.bp.format.DateTimeFormatter;
import org.threeten.bp.format.FormatStyle;

import es.uniovi.eii.cows.R;
import es.uniovi.eii.cows.model.NewsItem;

public class NewsActivity extends AppCompatActivity {

    //news item showing
    private NewsItem newsItem;

    private TextView title;
    private TextView source;
    private TextView date;
    private WebView description;
    private FloatingActionButton fabLinkCompleteNews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        //setSupportActionBar(findViewById(R.id.app_bar));

        //Receiving intent
        Intent newsIntent = getIntent();
        newsItem = newsIntent.getParcelableExtra(MainActivity.SELECTED_NEWS_ITEM);

        //initialize and show to the user
        initializeNewsItemProperties();
        if(newsItem != null)
            showNewsItem(newsItem);
    }

    private void initializeNewsItemProperties(){

        title = (TextView) findViewById(R.id.idTitle_news);
        source = (TextView) findViewById(R.id.idSource_news);
        date = (TextView) findViewById(R.id.idDate_news);
        description = (WebView) findViewById(R.id.idDescription_news);

        fabLinkCompleteNews = (FloatingActionButton) findViewById(R.id.floating_action_button_news);
        fabLinkCompleteNews.setOnClickListener(v -> linkToCompleteNewsItem());
    }

    private void showNewsItem(NewsItem newsItem){
        String title = newsItem.getTitle();
        String source = newsItem.getSource();
        String description = newsItem.getDescription();
        String date = newsItem.getDate().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT));

        this.title.setText(title);
        this.source.setText(source);
        this.date.setText(date);
        
        this.description.loadData(description, "text/html; charset=utf-8", "utf-8");

    }

    private void linkToCompleteNewsItem(){
        if(newsItem != null)
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(newsItem.getLink())));
    }
}
