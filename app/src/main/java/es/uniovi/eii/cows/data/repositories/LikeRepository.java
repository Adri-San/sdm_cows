package es.uniovi.eii.cows.data.repositories;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.function.Function;

import es.uniovi.eii.cows.data.BaseRepository;
import es.uniovi.eii.cows.model.interactions.Like;
import es.uniovi.eii.cows.model.NewsItem;

/**
 * Repository with CRUD operations for Likes. It has NewsItem as secondary
 * type so that news item can be returned when there is the need to get more
 * information than a simple like
 *
 * - <T> Like, main type
 * - <P> NewsItem, secondary type
 */
public class LikeRepository extends BaseRepository<Like, NewsItem> {

    //Name of the collection that is managed by the repository ("news_liked")
    @Override
    protected String getCollection() { return NEWS_LIKED; }

    /**
     * Method that implements the concrete "get" operation. It uses firebase
     * data mapper to build a Like object automatically given the stored
     * information that is provided by {d}.
     * @param d         document snapshot that contains the stored information
     * @param callback  function that will be called when finished with the retrieved Like object
     */
    @Override
    protected void doGet(QueryDocumentSnapshot d, Function<Like, Void> callback) {
        Like n = d.toObject(Like.class);
        callback.apply(n);
    }

    /**
     * Method that implements the concrete "add" operation. It uses {c} to check if all previous
     * tasks are finished and function {callback} to pass the {like} object added.
     *
     * @param c             task with the database collection
     * @param like          Like that will be added
     * @param callback      function that will be called when finished with the added {like}
     */
    @Override
    protected void doAdd(Task<QuerySnapshot> c, Like like, Function<Like, Void> callback) {
        if(c.getResult().size() == 0)
            addLike(like, callback);
    }

    /**
     * Method that adds the required conditions to perform the addition of the Like {item}.
     * To add a Like to the repository, these conditions will be checked:
     *  - It does not exist another like with the same newsItem identifier as {item}
     *                              AND
     *  - It does not exist another like with the same user identifier as {item}
     *
     * @param item                  Like that will later be added
     * @param collectionReference   collection that will suffer the conditions
     * @return the collection reference with the conditions added
     */
    @Override
    protected Query putAddingConditions(Like item, CollectionReference collectionReference) {
        return collectionReference
                .whereEqualTo(getReferenceProperty(), item.getNewsItemId())     //check newsItemId
                .whereEqualTo("userId", item.getUserId());                //check userId
    }

    /**
     * Method that adds the required conditions to perform the removal of the Like {item}.
     * To remove a Like from the repository, these conditions will be checked:
     *  - The like to be removed will need to have the same newsItem identifier as {item}
     *                                      AND
     *  - The like to be removed will need to have the same user identifier as {item}
     *
     * @param item                  Like that will later be removed
     * @param collectionReference   collection that will suffer the conditions
     * @return the collection reference with the conditions added
     */
    @Override
    protected Query putDeletingConditions(Like item, CollectionReference collectionReference) {
        return collectionReference
                .whereEqualTo(getReferenceProperty(), item.getNewsItemId())     //check newsItemId
                .whereEqualTo("userId", item.getUserId());                //check userId
    }

    /**
     * Private method that adds the previously-checked unique like
     * @param like Like to be added
     */
    private void addLike(Like like, Function<Like, Void> callback){
        getDatabase().collection(NEWS_LIKED)
                .add(like)
                .addOnSuccessListener(e -> callback.apply(like))
                .addOnFailureListener(e -> callback.apply(null));
    }
}
