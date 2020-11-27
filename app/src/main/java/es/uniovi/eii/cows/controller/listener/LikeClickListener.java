package es.uniovi.eii.cows.controller.listener;

import android.content.Context;
import android.util.Log;

import es.uniovi.eii.cows.data.helper.FirebaseHelper;
import es.uniovi.eii.cows.model.NewsItem;

public class LikeClickListener extends OnButtonClickListener {

    public LikeClickListener(Context context, NewsItem newsItem) { super(context, newsItem); }

    @Override
    protected void doOnClick() { FirebaseHelper.getInstance().addLike(getNewsItem().getId(), l -> {createMessage(l); return null;} ); }

    private void createMessage(String like){
        if(like != null)
            Log.d("Like added", like);
        else
            Log.d("Like not added: ", "Failure in Like Repository");
    }
}
