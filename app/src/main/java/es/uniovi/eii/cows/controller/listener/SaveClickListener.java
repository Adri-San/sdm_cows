package es.uniovi.eii.cows.controller.listener;

import android.content.Context;
import android.util.Log;
import android.widget.Button;

import com.google.android.material.button.MaterialButton;

import es.uniovi.eii.cows.R;
import es.uniovi.eii.cows.controller.listener.state.SaveState;
import es.uniovi.eii.cows.controller.listener.state.State;
import es.uniovi.eii.cows.data.helper.FirebaseHelper;
import es.uniovi.eii.cows.model.NewsItem;

public class SaveClickListener extends OnButtonClickListener {
    private final State state;

    public SaveClickListener(Context context, NewsItem newsItem, Button button) {
        super(context, newsItem, button);

        // TODO: state may not be false, get state from BD
        this.state = new SaveState(this, false, R.drawable.ic_save, R.drawable.ic_saved);
    }

    @Override
    protected void doOnClick() {
        state.changeState();
    }
}
