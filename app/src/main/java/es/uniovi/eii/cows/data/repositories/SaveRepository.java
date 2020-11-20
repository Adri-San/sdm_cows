package es.uniovi.eii.cows.data.repositories;

import android.util.Log;

import com.google.firebase.firestore.DocumentReference;

import java.util.Collections;
import java.util.Map;
import java.util.function.Function;

import es.uniovi.eii.cows.data.BaseRepository;

public class SaveRepository extends BaseRepository<String> {

    @Override
    public void add(String id) {
        Map<String, DocumentReference> save = Collections.singletonMap(getReferenceProperty(), getDatabase().document(NEWS_ITEMS + "/" + id));

        getDatabase().collection(NEWS_SAVED)
                .whereEqualTo(getReferenceProperty(), save.get(getReferenceProperty()))
                .get().addOnCompleteListener(c -> { if(c.getResult().size() == 0) addSave(save); }); //Added if does not exist
    }

    /**
     * Gets all newsItem's ids passing them to callback function one by one
     * @param callback function that will be called when one newsItem id is retrieved
     */
    @Override
    public void getAll(Function<String, Void> callback) {
        getDatabase().collection(NEWS_SAVED)
                .get()
                .addOnCompleteListener(t -> t.getResult().forEach(d -> {
                    String id = d.toObject(String.class); callback.apply(id); }));
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
