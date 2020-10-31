package es.uniovi.eii.cows.view.adapter;


import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;

import org.threeten.bp.format.DateTimeFormatter;
import org.threeten.bp.format.FormatStyle;

import java.util.List;

import es.uniovi.eii.cows.R;
import es.uniovi.eii.cows.model.NewsItem;
import es.uniovi.eii.cows.view.ProgressBarListener;

public class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int VIEW_TYPE_ITEM = 0;   // Progress bar
    private final int VIEW_TYPE_LOADING = 1;    // News item

    private List<NewsItem> news;
    private final OnItemClickListener listener;

    public NewsAdapter(List<NewsItem> news, OnItemClickListener listener) {
        this.news = news;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.line_news_view, parent, false);
            return new NewsItemViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof NewsItemViewHolder) {
            newsItemRows((NewsItemViewHolder) viewHolder, position);
        } else if (viewHolder instanceof LoadingViewHolder) {
            showLoadingView((LoadingViewHolder) viewHolder, position);
        }
    }

    @Override
    public int getItemCount() {
        return news.size();
    }

    /**
     * The following method decides the type of ViewHolder to display in the RecyclerView
     * @param position index element
     * @return view type
     */
    @Override
    public int getItemViewType(int position) {
        return news.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    private void showLoadingView(LoadingViewHolder viewHolder, int position) {
        //ProgressBar would be displayed
    }

    private void newsItemRows(NewsItemViewHolder viewHolder, int position) {
        NewsItem item = news.get(position);
        viewHolder.bindUser(item, listener);
    }

    /**************************************************************************/
    public static class NewsItemViewHolder extends RecyclerView.ViewHolder{
        private TextView title;
        private TextView source;
        private TextView date;
        private ImageView image;

        private ProgressBar progressBarImage;

        public NewsItemViewHolder(@NonNull View itemView) {
            super(itemView);
            this.title = itemView.findViewById(R.id.idTitle_main);
            this.source = itemView.findViewById(R.id.idSource_main);
            this.date = itemView.findViewById(R.id.idDate_main);
            this.image = itemView.findViewById(R.id.idImage_main);

            this.progressBarImage = itemView.findViewById(R.id.idProgressImage);
        }

        public void bindUser(final NewsItem newsItem, final OnItemClickListener listener){
            title.setText(newsItem.getTitle());
            source.setText(newsItem.getSource());
            date.setText(newsItem.getDate().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)));

            Glide.with(itemView).load(newsItem.getImageUrl())
                    .listener(new ProgressBarListener(progressBarImage))
                    .error(R.drawable.no_image_available)
                    .centerInside()
                    .apply(
                           RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.AUTOMATIC )
                    )
                    .into(image);


            itemView.setOnClickListener(v ->
                listener.onItemClick(newsItem));
        }
    }

    public static class LoadingViewHolder extends RecyclerView.ViewHolder{
        ProgressBar progressBar;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }

    public interface OnItemClickListener{
        void onItemClick(NewsItem item);
    }
}
