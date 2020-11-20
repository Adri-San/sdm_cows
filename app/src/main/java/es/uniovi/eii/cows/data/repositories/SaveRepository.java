package es.uniovi.eii.cows.data.repositories;

import android.util.Log;

import com.google.firebase.firestore.DocumentReference;

import java.util.Collections;
import java.util.Map;

import es.uniovi.eii.cows.data.Repository;

public class SaveRepository extends Repository<String> {

    @Override
    public void add(String id) {
        Map<String, DocumentReference> save = Collections.singletonMap(getReferenceProperty(), getDatabase().document(NEWS_ITEMS + "/" + id));

        getDatabase().collection(NEWS_SAVED)
                .whereEqualTo(getReferenceProperty(), save.get(getReferenceProperty()))
                .get().addOnCompleteListener(c -> { if(c.getResult().size() == 0) addSave(save); }); //Added if does not exist
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
