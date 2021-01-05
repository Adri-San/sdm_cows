package es.uniovi.eii.cows.data.repositories;

import android.util.Pair;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.function.Function;

import es.uniovi.eii.cows.data.BaseRepository;
import es.uniovi.eii.cows.model.NewsItem;
import es.uniovi.eii.cows.model.Save;

public class SaveRepository extends BaseRepository<Save, NewsItem> {

    @Override
    protected String getCollection() { return NEWS_SAVED; }

    @Override
    protected void doGet(QueryDocumentSnapshot d, Function<Save, Void> callback) {
        Save n = d.toObject(Save.class);
        callback.apply(n);
    }

    @Override
    protected void doAdd(Task<QuerySnapshot> c, Save save, Function<Save, Void> callback) {
        if(c.getResult().size() == 0)
            addSave(save, callback);

    }

    @Override
    protected Pair<String, Object> getAddingCondition(Save save) {
        return Pair.create(getReferenceProperty(), save.getNewsItemId());
    }

    @Override
    protected Pair<String, Object> getDeletingCondition(Save save) {
        return Pair.create(getReferenceProperty(), save.getNewsItemId());
    }

    /**
     * Private method that adds the previously-checked unique save
     * @param save
     */
    private void addSave(Save save, Function<Save, Void> callback){
         getDatabase().collection(NEWS_SAVED)
                .add(save)
                .addOnSuccessListener(e -> callback.apply(save))
                .addOnFailureListener(e -> callback.apply(null));
    }
}
