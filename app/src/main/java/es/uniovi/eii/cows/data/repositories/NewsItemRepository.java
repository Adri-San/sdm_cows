package es.uniovi.eii.cows.data.repositories;

import java.util.function.Function;

import es.uniovi.eii.cows.data.BaseRepository;
import es.uniovi.eii.cows.model.NewsItem;

public class NewsItemRepository extends BaseRepository<NewsItem, NewsItem> {

    /**
     * Adds the specified newsItem to the collection
     * if it does not already exists
     * @param newsItem
     */
    @Override
    public void add(NewsItem newsItem) {

        getDatabase().collection(NEWS_ITEMS)
                .whereEqualTo("title", newsItem.getTitle())
                .whereEqualTo("source", newsItem.getSource())
                .get()
                .addOnCompleteListener(c -> { if(c.getResult().size() == 0) addNewsItem(newsItem);
                else updateNewsItemId(newsItem, c.getResult().getDocuments().get(0).getId());}); //Added if does not exist
    }

    /**
     * Gets all newsItem passing them to callback function one by one
     * @param callback function that will be called when one newsItem is retrieved
     */
    @Override
    public void getAll(Function<NewsItem, Void> callback) {

        getDatabase().collection(NEWS_ITEMS)
                .get()
                .addOnCompleteListener(t -> t.getResult().forEach(d -> {NewsItem n = d.toObject(NewsItem.class); callback.apply(n); }));
    }

    /**
     * Gets the newsItem whose id is equal to the specified identifier
     * passing it to callback function
     *
     * @param callback function that will be called when the specified newsItem is retrieved
     * @param id newItem's identifier
     */
    @Override
    public void get(String id, Function<NewsItem, Void> callback) {
        getDatabase().collection(NEWS_ITEMS)
                .get()
                .addOnCompleteListener(t -> t.getResult().forEach(d -> {NewsItem n = d.toObject(NewsItem.class); if(id.equals(n.getId())) callback.apply(n); }));
    }

    /**
     * Private method that re-assigns the stored id
     * to the new object that represents the newsItem model
     * @param newsItem
     * @param id
     */
    private void updateNewsItemId(NewsItem newsItem, String id){ newsItem.setId(id); }

    /**
     * Private method that adds the previously-checked unique newsItem
     * @param newsItem
     */
    private void addNewsItem(NewsItem newsItem){

        getDatabase().collection(NEWS_ITEMS)
                .add(newsItem)
                .addOnCompleteListener(t -> newsItem.setId(t.getResult().getId()));
    }
}
