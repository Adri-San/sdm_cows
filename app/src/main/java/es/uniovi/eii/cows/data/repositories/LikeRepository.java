package es.uniovi.eii.cows.data.repositories;

import android.util.Log;

import com.google.firebase.firestore.DocumentReference;

import java.util.HashMap;
import java.util.Map;

import es.uniovi.eii.cows.data.Repository;

public class LikeRepository extends Repository<String> {

    @Override
    public void add(String id) {
        Map<String, DocumentReference> like = new HashMap<>();
        like.put(getReferenceProperty(), getDatabase().document(NEWS_ITEMS + "/" + id));

        getDatabase().collection(NEWS_LIKED)
                .whereEqualTo(getReferenceProperty(), like.get(getReferenceProperty()))
                .get().addOnCompleteListener(c -> { if(c.getResult().size() == 0) addLike(like); }); //Added if does not exist
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
