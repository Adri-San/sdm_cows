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

public class SaveRepository extends BaseRepository<String, NewsItem> {

    @Override
    protected String getCollection() { return NEWS_SAVED; }

    @Override
    public void doGet(QueryDocumentSnapshot d, Function<NewsItem, Void> callback) {
        DocumentReference document = (DocumentReference) d.getData().get(getReferenceProperty());
        document.get()
                .addOnCompleteListener(f -> { NewsItem n = f.getResult().toObject(NewsItem.class); callback.apply(n); });
    }

    @Override
    protected void doAdd(Task<QuerySnapshot> c, String id) {
        if(c.getResult().size() == 0)
            addSave(Collections.singletonMap(getReferenceProperty(), createDocumentReference(NEWS_ITEMS, id)));

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
     * Private method that adds the previously-checked unique save
     * @param save
     */
    private void addSave(Map<String, DocumentReference> save){

        getDatabase().collection(NEWS_SAVED)
                .add(save)
                .addOnSuccessListener(e -> Log.d("Database", "Success adding save"))
                .addOnFailureListener(e -> Log.w("Database", "Error adding save"));
    }
}
