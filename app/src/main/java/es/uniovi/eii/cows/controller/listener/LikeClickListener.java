package es.uniovi.eii.cows.controller.listener;

import android.content.Context;

import es.uniovi.eii.cows.controller.listener.abstractListener.OnButtonClickListener;
import es.uniovi.eii.cows.data.FirebaseHelper;
import es.uniovi.eii.cows.model.NewsItem;

public class LikeClickListener extends OnButtonClickListener {

    public LikeClickListener(Context context, NewsItem newsItem) { super(context, newsItem); }

    @Override
    protected void doOnClick() {
        FirebaseHelper.getInstance().addLike(getNewsItem().getId());
    }

}
