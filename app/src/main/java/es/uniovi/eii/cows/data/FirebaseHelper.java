package es.uniovi.eii.cows.data;


import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import es.uniovi.eii.cows.model.NewsItem;


public class FirebaseHelper {

    //Database collections
    private static final String NEWS_ITEMS = "news_items";
    private static final String NEWS_LIKED = "news_liked";
    private static final String NEWS_SAVED = "news_saved";

    //Property that references news item
    private String referenceProperty = "news_item_ID";

    private static FirebaseHelper instance = new FirebaseHelper();

    private FirebaseFirestore database;

    private FirebaseHelper(){ database = FirebaseFirestore.getInstance(); }

    /**
     * @return  Instance of the database helper
     */
    public static FirebaseHelper getInstance() {
        return instance;
    }

    /**
     * Adds the specified newsItem to the collection
     * @param newsItem
     */
    public void addNewsItem(NewsItem newsItem) {

        //News item is not stored on the database
        database.collection(NEWS_ITEMS)
                .add(newsItem)
                .addOnCompleteListener(t -> newsItem.setId(t.getResult().getId()));

    }

    public void addLike(String id){
        Map<String, DocumentReference> like = new HashMap<>();
        like.put(referenceProperty, database.document(NEWS_ITEMS + "/" + id));

        database.collection(NEWS_LIKED)
                .add(like)
                .addOnSuccessListener(e -> Log.d("Database", "Success"))
                .addOnFailureListener(e -> Log.w("Database", "Error adding newsItem"));
    }

    public void addSave(String id){
        Map<String, DocumentReference> like = new HashMap<>();
        like.put(referenceProperty, database.document(NEWS_ITEMS + "/" + id));

        database.collection(NEWS_SAVED)
                .add(like)
                .addOnSuccessListener(e -> Log.d("Database", "Success"))
                .addOnFailureListener(e -> Log.w("Database", "Error adding newsItem"));
    }
}
