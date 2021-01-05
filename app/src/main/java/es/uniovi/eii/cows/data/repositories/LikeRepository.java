package es.uniovi.eii.cows.data.repositories;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.function.Function;

import es.uniovi.eii.cows.data.BaseRepository;
import es.uniovi.eii.cows.model.Like;
import es.uniovi.eii.cows.model.NewsItem;

public class LikeRepository extends BaseRepository<Like, NewsItem> {

    @Override
    protected String getCollection() { return NEWS_LIKED; }

    @Override
    protected void doGet(QueryDocumentSnapshot d, Function<Like, Void> callback) {
        Like n = d.toObject(Like.class);
        callback.apply(n);
    }

    @Override
    protected void doAdd(Task<QuerySnapshot> c, Like like, Function<Like, Void> callback) {
        if(c.getResult().size() == 0)
            addLike(like, callback);
    }

    @Override
    protected Query getAddingCondition(Like item, CollectionReference collectionReference) {
        return collectionReference
                .whereEqualTo(getReferenceProperty(), item.getNewsItemId())     //check newsItemId
                .whereEqualTo("userId", item.getUserId());                //check userId
    }

    @Override
    protected Query getDeletingCondition(Like item, CollectionReference collectionReference) {
        return collectionReference
                .whereEqualTo(getReferenceProperty(), item.getNewsItemId())     //check newsItemId
                .whereEqualTo("userId", item.getUserId());                //check userId
    }

    /**
     * Private method that adds the previously-checked unique like
     * @param like
     */
    private void addLike(Like like, Function<Like, Void> callback){
        getDatabase().collection(NEWS_LIKED)
                .add(like)
                .addOnSuccessListener(e -> callback.apply(like))
                .addOnFailureListener(e -> callback.apply(null));
    }
}
