package es.uniovi.eii.cows.data.repositories;

import android.util.Log;
import android.util.Pair;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Collections;
import java.util.Map;
import java.util.function.Function;

import es.uniovi.eii.cows.data.BaseRepository;
import es.uniovi.eii.cows.model.NewsItem;

public class LikeRepository extends BaseRepository<String, NewsItem> {

    @Override
    protected String getCollection() { return NEWS_LIKED; }

    @Override
    public void doGet(QueryDocumentSnapshot d, Function<NewsItem, Void> callback) {
        DocumentReference document = (DocumentReference) d.getData().get(getReferenceProperty());
        document.get()
                .addOnCompleteListener(f -> { NewsItem n = f.getResult().toObject(NewsItem.class); callback.apply(n); } );
    }

    @Override
    protected void doAdd(Task<QuerySnapshot> c, String id, Function<String, Void> callback) {
        if(c.getResult().size() == 0)
            addLike(id, callback);
    }

    @Override
    protected Pair<String, Object> getAddingCondition(String id) {
        return Pair.create(getReferenceProperty(), createDocumentReference(NEWS_ITEMS, id));
    }

    @Override
    protected Pair<String, Object> getDeletingCondition(String id) {
        return Pair.create(getReferenceProperty(), createDocumentReference(NEWS_ITEMS, id));
    }
    /**
     * Private method that adds the previously-checked unique like
     * @param id identifier of the liked newsItem
     */
    private void addLike(String id, Function<String, Void> callback){
        Map<String, DocumentReference> like = Collections.singletonMap(getReferenceProperty(), createDocumentReference(NEWS_ITEMS, id));

        getDatabase().collection(NEWS_LIKED)
                .add(like)
                .addOnSuccessListener(e -> callback.apply(id))
                .addOnFailureListener(e -> callback.apply(null));
    }
}
