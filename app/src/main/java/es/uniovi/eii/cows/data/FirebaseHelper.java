package es.uniovi.eii.cows.data;


import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import es.uniovi.eii.cows.model.NewsItem;


public class FirebaseHelper {

    private static final String NEWS_ITEMS = "news_items";
    private static final String NEWS_LIKED = "news_liked";
    private static final String NEWS_SAVED = "news_saved";

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
    public void addNewsItem(NewsItem newsItem){
        database.collection(NEWS_ITEMS)
                .add(newsItem)
                .addOnSuccessListener(documentReference -> Log.d("Database", "NewsItem added with ID: " + documentReference.getId()))
                .addOnFailureListener(e -> Log.w("Database", "Error adding newsItem", e));
    }

    public void getNewsItems(){
        List<NewsItem> newsItems = new ArrayList<>();
        database.collection(NEWS_ITEMS)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d("Database", document.getId() + " => " + document.getData());
                            newsItems.add((NewsItem) document.getData());
                        }
                    } else {
                        Log.w("Database", "Error getting documents.", task.getException());
                    }
                });
    }

}
