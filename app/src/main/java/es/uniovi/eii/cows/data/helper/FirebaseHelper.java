package es.uniovi.eii.cows.data.helper;


import android.util.Pair;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.function.Function;

import es.uniovi.eii.cows.data.BaseRepository;
import es.uniovi.eii.cows.data.Repository;
import es.uniovi.eii.cows.data.repositories.LikeRepository;
import es.uniovi.eii.cows.data.repositories.NewsItemRepository;
import es.uniovi.eii.cows.data.repositories.SaveRepository;
import es.uniovi.eii.cows.model.Interaction;
import es.uniovi.eii.cows.model.interactions.Like;
import es.uniovi.eii.cows.model.NewsItem;
import es.uniovi.eii.cows.model.interactions.Save;


public class FirebaseHelper {

    private static FirebaseHelper instance = new FirebaseHelper();
    private String clientId; //identifier of application user in session

    //Repositories
    private BaseRepository<NewsItem, NewsItem> newsItemRepository;
    private BaseRepository<Like, NewsItem> likeRepository;
    private BaseRepository<Save, NewsItem> saveRepository;

    private FirebaseHelper(){
        newsItemRepository = new NewsItemRepository();
        likeRepository = new LikeRepository();
        saveRepository = new SaveRepository();
    }

    /**
     * @return  Instance of the database helper
     */
    public static FirebaseHelper getInstance() {
        return instance;
    }

    //Add Methods
    public void addNewsItem(NewsItem newsItem, Function<NewsItem, Void> callback) { newsItemRepository.add(newsItem, n-> callback.apply(n)); }
    public void addLike(String idNewsItem, Function<Like, Void> callback){ likeRepository.add(new Like(createDocumentReference(LikeRepository.NEWS_ITEMS, idNewsItem), clientId), n-> callback.apply(n)); }
    public void addSave(String idNewsItem, Function<Save, Void> callback){ saveRepository.add(new Save(createDocumentReference(SaveRepository.NEWS_ITEMS, idNewsItem), clientId), n-> callback.apply(n)); }

    //Delete Methods
    public void deleteNewsItem(NewsItem newsItem, Function<NewsItem, Void> callback){ newsItemRepository.delete(newsItem, n-> callback.apply(n)); }
    public void deleteLike(String idNewsItem, Function<Like, Void> callback) { likeRepository.delete(new Like(createDocumentReference(LikeRepository.NEWS_ITEMS, idNewsItem), clientId), n-> callback.apply(n)); }
    public void deleteSave(String idNewsItem, Function<Save, Void> callback) { saveRepository.delete(new Save(createDocumentReference(SaveRepository.NEWS_ITEMS, idNewsItem), clientId), n-> callback.apply(n));}

    //Get All Methods
    public void getNewsItems(Function<NewsItem, Void> callback) { newsItemRepository.getAll(n -> callback.apply(n)); }

    public void getLikedNewsItems(Function<NewsItem, Void> callback) { likeRepository.getAll(l -> {
        l.getNewsItemId().get()
                .addOnCompleteListener(t -> callback.apply(t.getResult().toObject(NewsItem.class)));
        return null;
    }); }

    public void getSavedNewsItems(Function<NewsItem, Void> callback) { saveRepository.getAll(s -> {
        if(s == null || !s.getUserId().equals(clientId))
            callback.apply(null);
        else
            s.getNewsItemId().get()
                    .addOnCompleteListener(t -> callback.apply(t.getResult().toObject(NewsItem.class)));
        return null;
    }); }

    //Get by id Methods
    public void getLikeById(String idLike, Function<Like, Void> callback) {likeRepository.get( Pair.create("__name__", idLike), t -> callback.apply(t));}
    public void getSaveById(String idSave, Function<Save, Void> callback) {saveRepository.get( Pair.create("__name__", idSave), t -> callback.apply(t));}
    public void getNewsItemById(String idNewsItem, Function<NewsItem, Void> callback) {newsItemRepository.get( Pair.create("__name__", idNewsItem), t -> callback.apply((NewsItem) t));}

    //Utility Methods
    public void getNewsItemIfLiked(String idNewsItem, Function<NewsItem, Void> callback){
        getNewsItemIfInteraction(likeRepository, idNewsItem, callback);
    }

    public void getNewsItemIfSaved(String idNewsItem, Function<NewsItem, Void> callback){
        getNewsItemIfInteraction(saveRepository, idNewsItem, callback);
    }

    /**
     *
     * @param repository
     * @param idNewsItem
     * @param callback
     */
    private void getNewsItemIfInteraction(Repository repository, String idNewsItem, Function<NewsItem, Void> callback){
        repository.get( Pair.create(likeRepository.getReferenceProperty(), createDocumentReference(LikeRepository.NEWS_ITEMS, idNewsItem)), i -> {
            Interaction interaction = (Interaction) i;

            if(interaction == null || !interaction.getUserId().equals(clientId))
                callback.apply(null);
            else
                interaction.getNewsItemId().get()
                        .addOnCompleteListener(t -> callback.apply(t.getResult().toObject(NewsItem.class)));
            return null;
        }); }

    public void setClientId(@NonNull String clientId){
        this.clientId = clientId;
    }

    /**
     * Creates a document reference to be stored in firebase DB
     * @param collection    collection where the referenced document is in
     * @param idToBeReferenced  identifier of the referenced document
     * @return  reference to the document
     */
    private DocumentReference createDocumentReference(String collection, String idToBeReferenced){
        return FirebaseFirestore.getInstance().document(collection + "/" + idToBeReferenced);
    }

}
