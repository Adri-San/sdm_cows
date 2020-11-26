package es.uniovi.eii.cows.data.repositories;

import android.util.Pair;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.function.Function;

import es.uniovi.eii.cows.data.BaseRepository;
import es.uniovi.eii.cows.model.NewsItem;

public class NewsItemRepository extends BaseRepository<NewsItem, NewsItem> {

    @Override
    protected String getCollection() { return NEWS_ITEMS; }

    @Override
    public void doGet(QueryDocumentSnapshot d, Function<NewsItem, Void> callback) {
        NewsItem n = d.toObject(NewsItem.class);
        callback.apply(n);
    }

    @Override
    protected void doAdd(Task<QuerySnapshot> c, NewsItem newsItem, Function<NewsItem, Void> callback) {
        if(c.getResult().size() == 0)
            addNewsItem(newsItem, callback);
        else updateNewsItemId(newsItem, c.getResult().getDocuments().get(0).getId(), callback);
    }

    @Override
    protected Pair<String, Object> getAddingCondition(NewsItem item) {
        return Pair.create("title", item.getTitle());
    }

    @Override
    protected Pair<String, Object> getDeletingCondition(NewsItem newsItem) {
        return Pair.create("__name__", newsItem.getId());
    }


    /**
     * Private method that re-assigns the stored id
     * to the new object that represents the newsItem model
     * @param newsItem
     * @param id
     */
    private void updateNewsItemId(NewsItem newsItem, String id, Function<NewsItem, Void> callback){
        newsItem.setId(id);
        callback.apply(newsItem);
    }

    /**
     * Private method that adds the previously-checked unique newsItem
     * @param newsItem
     */
    private void addNewsItem(NewsItem newsItem, Function<NewsItem, Void> callback){

        getDatabase().collection(NEWS_ITEMS)
                .add(newsItem)
                .addOnCompleteListener(t -> {newsItem.setId(t.getResult().getId()); callback.apply(newsItem);});
    }
}
