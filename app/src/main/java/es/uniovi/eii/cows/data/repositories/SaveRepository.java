package es.uniovi.eii.cows.data.repositories;

import android.util.Log;

import com.google.firebase.firestore.DocumentReference;

import java.util.Collections;
import java.util.Map;
import java.util.function.Function;

import es.uniovi.eii.cows.data.BaseRepository;
import es.uniovi.eii.cows.model.NewsItem;

public class SaveRepository extends BaseRepository<String, NewsItem> {

    @Override
    public void add(String id) {
        Map<String, DocumentReference> save = Collections.singletonMap(getReferenceProperty(), getDatabase().document(NEWS_ITEMS + "/" + id));

        getDatabase().collection(NEWS_SAVED)
                .whereEqualTo(getReferenceProperty(), save.get(getReferenceProperty()))
                .get().addOnCompleteListener(c -> { if(c.getResult().size() == 0) addSave(save); }); //Added if does not exist
    }

    /**
     * Gets all saved newsItems passing them to callback function one by one
     * @param callback function that will be called when one newsItem id is retrieved
     */
    @Override
    public void getAll(Function<NewsItem, Void> callback) {
        getDatabase().collection(NEWS_SAVED)
                .get()
                .addOnCompleteListener(t -> t.getResult().forEach(d -> {
                    DocumentReference document = (DocumentReference) d.getData().get(getReferenceProperty());
                    document.get().
                            addOnCompleteListener(f -> { NewsItem n = f.getResult().toObject(NewsItem.class); callback.apply(n); } ); }));
    }

    @Override
    public void get(String id, Function<NewsItem, Void> callback) {

    }

    /**
     * Private method that adds the previously-checked unique save
     * @param save
     */
    private void addSave(Map<String, DocumentReference> save){

        getDatabase().collection(NEWS_SAVED)
                .add(save)
                .addOnSuccessListener(e -> Log.d("Database", "Success"))
                .addOnFailureListener(e -> Log.w("Database", "Error adding newsItem"));
    }

}
