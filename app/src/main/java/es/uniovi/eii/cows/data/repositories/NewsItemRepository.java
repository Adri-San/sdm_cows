package es.uniovi.eii.cows.data.repositories;

import es.uniovi.eii.cows.data.Repository;
import es.uniovi.eii.cows.model.NewsItem;

public class NewsItemRepository extends Repository<NewsItem> {

    /**
     * Adds the specified newsItem to the collection
     * if it does not already exists
     * @param newsItem
     */
    @Override
    public void add(NewsItem newsItem) {

        getDatabase().collection(NEWS_ITEMS)
                .whereEqualTo("title", newsItem.getTitle())
                .whereEqualTo("date", newsItem.getDate())
                .whereEqualTo("source", newsItem.getSource())
                .get()
                .addOnCompleteListener(c -> { if(c.getResult().size() == 0) addNewsItem(newsItem);
                else updateNewsItemId(newsItem, c.getResult().getDocuments().get(0).getId());}); //Added if does not exist
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
