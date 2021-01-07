package es.uniovi.eii.cows.controller.listener;

import android.content.Context;
import android.widget.Button;

import es.uniovi.eii.cows.R;
import es.uniovi.eii.cows.controller.listener.state.LikeState;
import es.uniovi.eii.cows.controller.listener.state.State;
import es.uniovi.eii.cows.data.helper.FirebaseHelper;
import es.uniovi.eii.cows.model.NewsItem;

public class LikeClickListener extends OnButtonClickListener {
    private final State state;

    public LikeClickListener(Context context, NewsItem newsItem, Button button) {
        super(context, newsItem, button);

        this.state = new LikeState(this, false, R.drawable.ic_like, R.drawable.ic_liked);
        this.state.setStoredState(false);

        FirebaseHelper.getInstance().getNewsItemIfLiked(newsItem.getId(), n -> {
            if(n != null)
                this.state.setStoredState(true);  //it was liked
            else
                this.state.setStoredState(false);  //it was not liked
            return null;
        });


    }

    @Override
    protected void doOnClick() {
        state.changeState();
    }
}
