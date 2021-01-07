package es.uniovi.eii.cows.data;

import android.util.Pair;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.function.Function;

public abstract class BaseRepository<T, P> implements Repository<T, P> {

    //Database collections
    public static final String NEWS_ITEMS = "news_items";
    public static final String NEWS_LIKED = "news_liked";
    public static final String NEWS_SAVED = "news_saved";

    //Property that references news item
    private String referenceProperty = "newsItemId";

    private FirebaseFirestore database;

    protected BaseRepository(){ database = FirebaseFirestore.getInstance(); }

    public FirebaseFirestore getDatabase() { return database; }

    public String getReferenceProperty() { return referenceProperty;}


    @Override
    public void add(T item, Function<T, Void> callback) {

        getAddingCondition(item, getDatabase().collection(getCollection()))
                .get()
                .addOnCompleteListener(c -> doAdd(c, item, callback));
    }

    @Override
    public void get(Pair<String, Object> condition, Function<T, Void> callback) {

        getDatabase().collection(getCollection())
                .whereEqualTo(condition.first, condition.second)
                .get()
                .addOnCompleteListener(t -> t.getResult().forEach(d -> doGet(d, callback)));
    }

    @Override
    public void getAll(Function<T, Void> callback) {

        getDatabase().collection(getCollection())
                .get()
                .addOnCompleteListener(t -> {
                    if(t.getResult().getDocuments().size() == 0)
                        callback.apply(null);
                    else
                        t.getResult().forEach(d -> doGet(d, callback));
                });
    }

    @Override
    public void delete(T item, Function<T, Void> callback) {
        getDeletingCondition(item, getDatabase().collection(getCollection()))
                .get()
                .addOnCompleteListener(t -> t.getResult().forEach(d -> {d.getReference().delete(); callback.apply(item);}));
    }

    //Methods to be redefined

    protected abstract String getCollection();                                                                                          //name of the database collection

    protected abstract void doGet(QueryDocumentSnapshot d, Function<T, Void> callback);                                                 //on get action

    protected abstract void doAdd(Task<QuerySnapshot> c, T item, Function<T, Void> callback);                                           //on add action

    protected abstract Query getAddingCondition(T item, CollectionReference collectionReference);                                       //condition to be added

    protected abstract Query getDeletingCondition(T item, CollectionReference collectionReference);                                     //condition to be deleted

}
