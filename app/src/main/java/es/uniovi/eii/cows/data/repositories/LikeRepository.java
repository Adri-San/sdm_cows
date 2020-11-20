package es.uniovi.eii.cows.data.repositories;

import android.util.Log;

import com.google.firebase.firestore.DocumentReference;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import es.uniovi.eii.cows.data.BaseRepository;
import es.uniovi.eii.cows.model.NewsItem;

public class LikeRepository extends BaseRepository<String> {

    @Override
    public void add(String id) {
        Map<String, DocumentReference> like = new HashMap<>();
        like.put(getReferenceProperty(), getDatabase().document(NEWS_ITEMS + "/" + id));

        getDatabase().collection(NEWS_LIKED)
                .whereEqualTo(getReferenceProperty(), like.get(getReferenceProperty()))
                .get().addOnCompleteListener(c -> { if(c.getResult().size() == 0) addLike(like); }); //Added if does not exist
    }

    /**
     * Gets all newsItem's ids passing them to callback function one by one
     * @param callback function that will be called when one newsItem id is retrieved
     */
    @Override
    public void getAll(Function<String, Void> callback) {
        getDatabase().collection(NEWS_LIKED)
                .get()
                .addOnCompleteListener(t -> t.getResult().forEach(d -> {
                    String id = d.toObject(String.class); callback.apply(id); }));
    }

    /**
     * Private method that adds the previously-checked unique like
     * @param like
     */
    private void addLike(Map<String, DocumentReference> like){

        getDatabase().collection(NEWS_LIKED)
                .add(like)
                .addOnSuccessListener(e -> Log.d("Database", "Success"))
                .addOnFailureListener(e -> Log.w("Database", "Error adding newsItem"));
    }
}
