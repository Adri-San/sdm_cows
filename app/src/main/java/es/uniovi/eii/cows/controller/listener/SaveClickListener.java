package es.uniovi.eii.cows.controller.listener;

import android.content.Context;
import android.util.Log;

import es.uniovi.eii.cows.controller.listener.abstractListener.OnButtonClickListener;
import es.uniovi.eii.cows.data.helper.FirebaseHelper;
import es.uniovi.eii.cows.model.NewsItem;

public class SaveClickListener extends OnButtonClickListener {

    public SaveClickListener(Context context, NewsItem newsItem) {
        super(context, newsItem);
    }

    @Override
    protected void doOnClick() {
        FirebaseHelper.getInstance().addSave(getNewsItem().getId(), s -> {createMessage(s); return null;});
    }

    private void createMessage(String save){
        if(save != null)
            Log.d("Save added", save);
        else
            Log.d("Save not added: ", "Failure in Save Repository");
    }
}
