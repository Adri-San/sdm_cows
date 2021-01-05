package es.uniovi.eii.cows.model;

import com.google.firebase.firestore.DocumentReference;

public class Save {
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
