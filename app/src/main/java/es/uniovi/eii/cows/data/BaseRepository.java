package es.uniovi.eii.cows.data;

import android.util.Pair;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.function.Function;

public abstract class BaseRepository<T, P> implements Repository<T, P> {

    //Database collections
    protected static final String NEWS_ITEMS = "news_items";
    protected static final String NEWS_LIKED = "news_liked";
    protected static final String NEWS_SAVED = "news_saved";

    //Property that references news item
    private String referenceProperty = "news_item_ID";

    private FirebaseFirestore database;

    protected BaseRepository(){ database = FirebaseFirestore.getInstance(); }

    public FirebaseFirestore getDatabase() { return database; }

    protected String getReferenceProperty() { return referenceProperty;}


    @Override
    public void add(T item) {

        getDatabase().collection(getCollection())
                .whereEqualTo(getAddingCondition(item).first, getAddingCondition(item).second)
                .get()
                .addOnCompleteListener(c -> doAdd(c, item));
    }

    @Override
    public void get(Pair<String, Object> condition, Function<P, Void> callback) {

        getDatabase().collection(getCollection())
                .whereEqualTo(condition.first, condition.second)
                .get()
                .addOnCompleteListener(t -> t.getResult().forEach(d -> doGet(d, callback)));
    }

    @Override
    public void getAll(Function<P, Void> callback) {

        getDatabase().collection(getCollection())
                .get()
                .addOnCompleteListener(t -> t.getResult().forEach(d -> doGet(d, callback)));
    }

    @Override
    public void delete(T item) {

        getDatabase().collection(getCollection())
                .whereEqualTo(getDeletingCondition(item).first, getDeletingCondition(item).second)
                .get()
                .addOnCompleteListener(t -> t.getResult().forEach(d -> d.getReference().delete()));
    }

    /**
     * Creates a document reference to be stored in firebase DB
     * @param collection    collection where the referenced document is in
     * @param idToBeReferenced  identifier of the referenced document
     * @return  reference to the document
     */
    protected DocumentReference createDocumentReference(String collection, String idToBeReferenced){
        return getDatabase().document(collection + "/" + idToBeReferenced);
    }

    //Methods to be redefined

    protected abstract String getCollection();                                          //name of the database collection

    protected abstract void doGet(QueryDocumentSnapshot d, Function<P, Void> callback); //on get action

    protected abstract void doAdd(Task<QuerySnapshot> c, T item);                       //on add action

    protected abstract Pair<String, Object> getAddingCondition(T item);                 //condition to be added

    protected abstract Pair<String, Object> getDeletingCondition(T item);               //condition to be deleted

}
