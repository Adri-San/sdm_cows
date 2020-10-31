package es.uniovi.eii.cows.view;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

public class ProgressBarListener implements RequestListener<Drawable> {
    private ProgressBar progressBar;

    public ProgressBarListener(ProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    @Override
    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
        progressBar.setVisibility(View.GONE);
        return false;
    }

    @Override
    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
        progressBar.setVisibility(View.GONE);
        return false;
    }
}
