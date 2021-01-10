package es.uniovi.eii.cows.data.repositories;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.function.Function;

import es.uniovi.eii.cows.data.BaseRepository;
import es.uniovi.eii.cows.model.NewsItem;

/**
 * Repository with CRUD operations for NewsItem. It has NewsItem as
 * type because it is the object model that this repository manages.
 *
 * - <T> NewsItem, main type
 */
public class NewsItemRepository extends BaseRepository<NewsItem> {

    //Name of the collection that is managed by the repository ("news_items")
    @Override
    protected String getCollection() { return NEWS_ITEMS; }

    /**
     * Method that implements the concrete "get" operation. It uses firebase
     * data mapper to build a NewsItem object automatically given the stored
     * information that is provided by {d}.
     * @param d         document snapshot that contains the stored information
     * @param callback  function that will be called when finished with the retrieved NewsItem object
     */
    @Override
    protected void doGet(QueryDocumentSnapshot d, Function<NewsItem, Void> callback) {
        NewsItem n = d.toObject(NewsItem.class);
        callback.apply(n);
    }

    /**
     * Method that implements the concrete "add" operation. It uses {c} to check if all previous
     * tasks are finished and function {callback} to pass the {newsItem} object added.
     *
     * It also uses {c} to verify if newsItem already exists on the database. That could happen when
     * the user uses the 'pull-down to refresh' component. In that situation, new model objects will
     * be created in the application (NewsItems), and that models need to be linked with persistence.
     * So their respective identifiers will be mapped from database to the model using 'updateNewsItemId'
     * method.
     *
     * @param c                 task with the database collection
     * @param newsItem          NewsItem that will be added
     * @param callback          function that will be called when finished with the added {newsItem}
     */
    @Override
    protected void doAdd(Task<QuerySnapshot> c, NewsItem newsItem, Function<NewsItem, Void> callback) {
        if(c.getResult().size() == 0)
            addNewsItem(newsItem, callback);
        else updateNewsItemId(newsItem, c.getResult().getDocuments().get(0).getId(), callback);
    }

    /**
     * Method that adds the required conditions to perform the addition of the NewsItem {item}.
     * To add a NewsItem to the repository, these conditions will be checked:
     *  - It does not exist another newsItem with the same title as {item}
     *                              AND
     *  - It does not exist another newsItem with the same source as {item}
     *
     * @param item                  NewsItem that will later be added
     * @param collectionReference   collection that will suffer the conditions
     * @return the collection reference with the conditions added
     */
    @Override
    protected Query putAddingConditions(NewsItem item, CollectionReference collectionReference) {
        return collectionReference
                .whereEqualTo("title", item.getTitle())     //check title
                .whereEqualTo("source", item.getSource());  //check source
    }

    /**
     * Method that adds the required conditions to perform the removal of the NewsItem {item}.
     * To remove a NewsItem from the repository, these conditions will be checked:
     *  - The newsItem to be removed will need to have the same identifier as {item}
     *
     * @param item                  NewsItem that will later be removed
     * @param collectionReference   collection that will suffer the conditions
     * @return the collection reference with the conditions added
     */
    @Override
    protected Query putDeletingConditions(NewsItem item, CollectionReference collectionReference) {
        return collectionReference.whereEqualTo("__name__", item.getId());
    }

    /**
     * Private method that re-assigns the stored id to the new object that represents the
     * newsItem model.
     *
     * @param newsItem  new application object that needs an identifier
     * @param id        identifier that is going to be assigned
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
