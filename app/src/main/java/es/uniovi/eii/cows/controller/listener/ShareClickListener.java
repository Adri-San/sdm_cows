package es.uniovi.eii.cows.controller.listener;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import es.uniovi.eii.cows.R;
import es.uniovi.eii.cows.model.NewsItem;
import es.uniovi.eii.cows.util.Connection;

public class ShareClickListener implements View.OnClickListener {
    // Application context
    private final Context context;

    // News item to share
    private final NewsItem newsItem;

    public ShareClickListener(Context context, NewsItem newsItem) {
        this.context = context;
        this.newsItem = newsItem;
    }

    @Override
    public void onClick(View v) {
        Connection connection = new Connection(context);
        if (connection.checkConnection()) {
            shareNewsItem();
        } else {
            Toast.makeText(context, R.string.error_connection, Toast.LENGTH_LONG).show();
        }
    }

    private void shareNewsItem() {
        // New intent
        Intent itSend = new Intent(Intent.ACTION_SEND);
        itSend.setType("text/plain");

        // Only share title and link to the news item
        itSend.putExtra(Intent.EXTRA_SUBJECT, newsItem.getTitle());
        itSend.putExtra(Intent.EXTRA_TEXT, newsItem.getLink());

        // Start activity
        Intent shareIntent = Intent.createChooser(itSend, null);
        context.startActivity(shareIntent);
    }
}
