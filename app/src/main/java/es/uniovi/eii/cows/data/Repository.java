package es.uniovi.eii.cows.data;

import com.google.firebase.firestore.FirebaseFirestore;

public abstract class Repository<T> {

    //Database collections
    protected static final String NEWS_ITEMS = "news_items";
    protected static final String NEWS_LIKED = "news_liked";
    protected static final String NEWS_SAVED = "news_saved";

    //Property that references news item
    private String referenceProperty = "news_item_ID";

    private FirebaseFirestore database;

    protected Repository(){ database = FirebaseFirestore.getInstance(); }

    protected FirebaseFirestore getDatabase() { return database; }

    protected String getReferenceProperty() { return referenceProperty;}

    public abstract void add(T t);
}
