package es.uniovi.eii.cows.data.repositories;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.function.Function;

import es.uniovi.eii.cows.data.BaseRepository;
import es.uniovi.eii.cows.model.interactions.Save;

/**
 * Repository with CRUD operations for Saves. It has Save as
 * type because it is the object model that this repository manages.
 *
 * - <T> Like, main type
 */
public class SaveRepository extends BaseRepository<Save> {

    //Name of the collection that is managed by the repository ("news_saved")
    @Override
    protected String getCollection() { return NEWS_SAVED; }

    /**
     * Method that implements the concrete "get" operation. It uses firebase
     * data mapper to build a Save object automatically given the stored
     * information that is provided by {d}.
     * @param d         document snapshot that contains the stored information
     * @param callback  function that will be called when finished with the retrieved Save object
     */
    @Override
    protected void doGet(QueryDocumentSnapshot d, Function<Save, Void> callback) {
        Save n = d.toObject(Save.class);
        callback.apply(n);
    }

    /**
     * Method that implements the concrete "add" operation. It uses {c} to check if all previous
     * tasks are finished and function {callback} to pass the {save} object added.
     *
     * @param c             task with the database collection
     * @param save          Save that will be added
     * @param callback      function that will be called when finished with the added {saved}
     */
    @Override
    protected void doAdd(Task<QuerySnapshot> c, Save save, Function<Save, Void> callback) {
        if(c.getResult().size() == 0)
            addSave(save, callback);

    }

    /**
     * Method that adds the required conditions to perform the addition of the Save {item}.
     * To add a Save to the repository, these conditions will be checked:
     *  - It does not exist another save with the same newsItem identifier as {item}
     *                                  AND
     *  - It does not exist another save with the same user identifier as {item}
     *
     * @param item                  Save that will later be added
     * @param collectionReference   collection that will suffer the conditions
     * @return the collection reference with the conditions added
     */
    @Override
    protected Query putAddingConditions(Save item, CollectionReference collectionReference) {
        return collectionReference
                .whereEqualTo(getReferenceProperty(), item.getNewsItemId())     //check newsItemId
                .whereEqualTo("userId", item.getUserId());                //check userId
    }

    /**
     * Method that adds the required conditions to perform the removal of the Save {item}.
     * To remove a Save from the repository, these conditions will be checked:
     *  - The save to be removed will need to have the same newsItem identifier as {item}
     *                                      AND
     *  - The save to be removed will need to have the same user identifier as {item}
     *
     * @param item                  Save that will later be removed
     * @param collectionReference   collection that will suffer the conditions
     * @return the collection reference with the conditions added
     */
    @Override
    protected Query putDeletingConditions(Save item, CollectionReference collectionReference) {
        return collectionReference
                .whereEqualTo(getReferenceProperty(), item.getNewsItemId())     //check newsItemId
                .whereEqualTo("userId", item.getUserId());                //check userId
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
