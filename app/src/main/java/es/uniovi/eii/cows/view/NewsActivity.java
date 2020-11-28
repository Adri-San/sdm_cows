package es.uniovi.eii.cows.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.apache.commons.text.StringEscapeUtils;
import org.threeten.bp.format.DateTimeFormatter;
import org.threeten.bp.format.FormatStyle;

import es.uniovi.eii.cows.R;
import es.uniovi.eii.cows.controller.listener.LikeClickListener;
import es.uniovi.eii.cows.controller.listener.SaveClickListener;
import es.uniovi.eii.cows.controller.listener.ShareClickListener;
import es.uniovi.eii.cows.model.NewsItem;

public class NewsActivity extends AppCompatActivity {

    //news item showing
    private NewsItem newsItem;

    private TextView title;
    private TextView source;
    private TextView date;
    private WebView description;
    private ImageView image;
    private FloatingActionButton fabLinkCompleteNews;

    // Buttons
    private Button like;
    private MaterialButton save;
    private Button share;

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
        initializeButtons();
        if(newsItem != null)
            showNewsItem(newsItem);
    }

    private void initializeButtons() {
        like = findViewById(R.id.idLike_news);
        save = findViewById(R.id.idSave_news);
        share = findViewById(R.id.idShare_news);

        share.setOnClickListener(new ShareClickListener(this, newsItem, share));
        save.setOnClickListener(new SaveClickListener(this, newsItem, save));
        like.setOnClickListener(new LikeClickListener(this, newsItem, like));
    }

    private void initializeNewsItemProperties(){
        title = (TextView) findViewById(R.id.idTitle_news);
        source = (TextView) findViewById(R.id.idSource_news);
        date = (TextView) findViewById(R.id.idDate_news);
        description = (WebView) findViewById(R.id.idDescription_news);
        image = (ImageView) findViewById(R.id.idImage_news);

        Glide.with(this).load(newsItem.getImageUrl())
                .thumbnail(Glide.with(this).load(R.drawable.loading))
                .error(R.drawable.no_image_available)
                .centerInside()
                .into(image);

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

        loadNewsContent(description);
    }

    private void linkToCompleteNewsItem(){
        if(newsItem != null)
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(newsItem.getLink())));
    }

    @SuppressLint("SetJavaScriptEnabled")           // Safe sources
    private void loadNewsContent(String content){
        // Adapting WebView to dark mode
        TypedValue outValue = new TypedValue();
        this.getTheme().resolveAttribute(R.attr.colorOnBackground, outValue, true);
        Log.d("color", String.valueOf(R.attr.colorOnBackground));
        Log.d("color", String.format("#%06X", (0xFFFFFF & outValue.data)));
        this.description.setBackgroundColor(Color.TRANSPARENT);
        this.description.getSettings().setJavaScriptEnabled(true);
        this.description.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView view, String url) {
                view.loadUrl(
                        "javascript:document.body.style.setProperty(\"color\", \"" +
                                String.format("#%06X", (0xFFFFFF & outValue.data)) +
                                "\");"
                );
            }
        });
        //Format
        content = StringEscapeUtils.unescapeHtml4(content);
        //Loading
        this.description.loadData(content, "text/html; charset=utf-8", "UTF-8");
    }
}
