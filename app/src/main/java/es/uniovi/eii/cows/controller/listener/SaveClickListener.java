package es.uniovi.eii.cows.controller.listener;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import es.uniovi.eii.cows.data.FirebaseHelper;
import es.uniovi.eii.cows.model.NewsItem;

public class SaveClickListener implements View.OnClickListener {

    // Application context
    private final Context context;

    // News item to share
    private final NewsItem newsItem;

    public SaveClickListener(Context context, NewsItem newsItem) {
        this.context = context;
        this.newsItem = newsItem;
    }

    @Override
    public void onClick(View v) {
        saveNewsItem();
    }

    private void saveNewsItem() {
        Toast.makeText(context, "Exito", Toast.LENGTH_LONG).show();
    }
}
