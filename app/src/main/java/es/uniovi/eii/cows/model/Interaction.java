package es.uniovi.eii.cows.model;

import com.google.firebase.firestore.DocumentReference;

public interface Interaction {

    DocumentReference getNewsItemId();
    String getUserId();
}
