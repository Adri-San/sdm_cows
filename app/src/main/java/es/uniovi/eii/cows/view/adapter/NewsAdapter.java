package es.uniovi.eii.cows.view.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;

import org.threeten.bp.format.DateTimeFormatter;
import org.threeten.bp.format.FormatStyle;

import java.util.List;

import es.uniovi.eii.cows.R;
import es.uniovi.eii.cows.model.NewsItem;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsItemViewHolder> {

    private List<NewsItem> news;
    private final OnItemClickListener listener;

    public NewsAdapter(List<NewsItem> news, OnItemClickListener listener) {
        this.news = news;
        this.listener = listener;
    }

    @NonNull
    @Override
    public NewsItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.line_news_view, parent, false);
        return new NewsItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsItemViewHolder holder, int position) {
        NewsItem newsItem = news.get(position);
        holder.bindUser(newsItem, listener);
    }

    @Override
    public int getItemCount() {
        return news.size();
    }

    /**************************************************************************/
    public static class NewsItemViewHolder extends RecyclerView.ViewHolder{
        private TextView title;
        private TextView source;
        private TextView date;
        private ImageView image;

        public NewsItemViewHolder(@NonNull View itemView) {
            super(itemView);
            this.title = itemView.findViewById(R.id.idTitle_main);
            this.source = itemView.findViewById(R.id.idSource_main);
            this.date = itemView.findViewById(R.id.idDate_main);
            this.image = itemView.findViewById(R.id.idImage_main);
        }

        public void bindUser(final NewsItem newsItem, final OnItemClickListener listener){
            title.setText(newsItem.getTitle());
            source.setText(newsItem.getSource());
            date.setText(newsItem.getDate().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)));

            Glide.with(itemView).load(newsItem.getImageUrl())
                    .thumbnail(Glide.with(itemView).load(R.drawable.loading))
                    .error(R.drawable.no_image_available)
                    .centerInside()
                    .into(image);

            itemView.setOnClickListener(v ->
                listener.onItemClick(newsItem));
        }
    }

    public interface OnItemClickListener{
        void onItemClick(NewsItem item);
    }
}
