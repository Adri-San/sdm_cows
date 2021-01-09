package es.uniovi.eii.cows.model.interactions;

import com.google.firebase.firestore.DocumentReference;

import es.uniovi.eii.cows.model.Interaction;

public class Save implements Interaction {
    private DocumentReference newsItemId;
    private String userId;

    public Save(DocumentReference newsItemId, String userId) {
        this.newsItemId = newsItemId;
        this.userId = userId;
    }

    //Methods and constructor to be used by mapper
    public Save() {

    }

    public DocumentReference getNewsItemId() {
        return newsItemId;
    }

    public void setNewsItemId(DocumentReference newsItemId) {
        this.newsItemId = newsItemId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

}
