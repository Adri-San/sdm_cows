package es.uniovi.eii.cows.data;

import android.util.Pair;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.function.Function;

/**
 * Repository used to help concrete repositories with basic methods and operations.
 *
 * @param <T> main type of the repository (NewsItem, Like or Save)
 * @param <P> secondary type of the repository (that will be returned when needed)
 */
public abstract class BaseRepository<T, P> implements Repository<T, P> {

    //Database collections
    public static final String NEWS_ITEMS = "news_items";
    public static final String NEWS_LIKED = "news_liked";
    public static final String NEWS_SAVED = "news_saved";

    //Property used to reference newsItems throughout all repositories
    private String referenceProperty = "newsItemId";

    //Firestore Database
    private FirebaseFirestore database;

    protected BaseRepository(){ database = FirebaseFirestore.getInstance(); }

    public FirebaseFirestore getDatabase() { return database; }

    public String getReferenceProperty() { return referenceProperty;}


    /**
     * Method that adds a T object to the repository. {callback} function will be
     * called with:
     *  - null as parameter if it was not possible to store the {item},
     *  - otherwise, it will be called with the stored {item} as parameter.
     *
     * @param item      model object that will be added
     * @param callback  function that will be called when process is finished
     */
    @Override
    public void add(T item, Function<T, Void> callback) {

        putAddingConditions(item, getDatabase().collection(getCollection()))
                .get()
                .addOnCompleteListener(c -> doAdd(c, item, callback));
    }

    /**
     * Method that gets a T object from the repository if condition is true. {condition} will be
     * a tuple, for example, ("title", "COVID pandemic gets even worse"). If that is the condition,
     * returned object's 'title' field will have "COVID pandemic gets even worse" as value on
     * database.
     *
     * {callback} function will be called with:
     *  - null as parameter if no object was returned,
     *  - otherwise, it will be called with the T object as parameter.
     *
     * @param condition pair (name of database field, value of database field) that must be fulfilled
     * @param callback  function that will be called when process is finished
     */
    @Override
    public void get(Pair<String, Object> condition, Function<T, Void> callback) {

        getDatabase().collection(getCollection())
                .whereEqualTo(condition.first, condition.second)
                .get()
                .addOnCompleteListener(t -> t.getResult().forEach(d -> doGet(d, callback)));
    }

    /**
     * Method that gets all stored T objects from the repository. {callback} function will
     * be called with the returned objects, one by one. If there are two objects, it will be
     * called twice, each time with each object.
     *
     * {callback} function will be called with:
     *  - null as parameter if it was not possible to return the object,
     *  - otherwise, it will be called with the T object as parameter.
     *
     * @param callback function that will be called when process is finished
     */
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

    /**
     * Method that removes a stored T object ({item}) from the repository. {callback}
     * function will be called with the removed object.
     *
     * @param item      model object that will be removed
     * @param callback  function that will be called when process is finished
     */
    @Override
    public void delete(T item, Function<T, Void> callback) {
        putDeletingConditions(item, getDatabase().collection(getCollection()))
                .get()
                .addOnCompleteListener(t -> t.getResult().forEach(d -> {d.getReference().delete(); callback.apply(item);}));
    }

    //Methods to be redefined

    protected abstract String getCollection();                                                                                          //name of the database collection

    protected abstract void doGet(QueryDocumentSnapshot d, Function<T, Void> callback);                                                 //on get action

    protected abstract void doAdd(Task<QuerySnapshot> c, T item, Function<T, Void> callback);                                           //on add action

    protected abstract Query putAddingConditions(T item, CollectionReference collectionReference);                                       //condition to be added

    protected abstract Query putDeletingConditions(T item, CollectionReference collectionReference);                                     //condition to be deleted

}
