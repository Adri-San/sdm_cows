package es.uniovi.eii.cows.controller.listener;

import android.content.Context;
import android.view.View;

import es.uniovi.eii.cows.model.NewsItem;

public abstract class OnButtonClickListener implements View.OnClickListener {

    // Application context
    private final Context context;

    // News item to share
    private final NewsItem newsItem;

    public OnButtonClickListener(Context context, NewsItem newsItem) {
        this.context = context;
        this.newsItem = newsItem;
    }

    @Override
    public void onClick(View v) {
        doOnClick();
    }

    public Context getContext() {
        return context;
    }

    public NewsItem getNewsItem() {
        return newsItem;
    }

    //Method to be implemented by Listeners
    protected abstract void doOnClick();
}
