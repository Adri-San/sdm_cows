package es.uniovi.eii.cows.controller.listener;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import es.uniovi.eii.cows.R;
import es.uniovi.eii.cows.controller.listener.abstractListener.OnButtonClickListener;
import es.uniovi.eii.cows.model.NewsItem;
import es.uniovi.eii.cows.util.Connection;

public class ShareClickListener extends OnButtonClickListener {

    public ShareClickListener(Context context, NewsItem newsItem) {
        super(context, newsItem);
    }

    @Override
    protected void doOnClick() {
        Connection connection = new Connection(getContext());
        if (connection.checkConnection()) {
            shareNewsItem();
        } else {
            Toast.makeText(getContext(), R.string.error_connection, Toast.LENGTH_LONG).show();
        }
    }

    private void shareNewsItem() {
        // New intent
        Intent itSend = new Intent(Intent.ACTION_SEND);
        itSend.setType("text/plain");

        // Only share title and link to the news item
        itSend.putExtra(Intent.EXTRA_SUBJECT, getNewsItem().getTitle());
        itSend.putExtra(Intent.EXTRA_TEXT, getNewsItem().getLink());

        // Start activity
        Intent shareIntent = Intent.createChooser(itSend, null);
        getContext().startActivity(shareIntent);
    }
}
