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
import es.uniovi.eii.cows.model.NewsItem;
import es.uniovi.eii.cows.model.interactions.Like;
import es.uniovi.eii.cows.model.interactions.Save;


public class FirebaseHelper {

    private static FirebaseHelper instance = new FirebaseHelper();  //Singleton
    private String clientId; //identifier of application user in session

    //Repositories
    private Repository<NewsItem> newsItemRepository;
    private Repository<Like> likeRepository;
    private Repository<Save> saveRepository;

    private FirebaseHelper(){
        newsItemRepository = new NewsItemRepository();
        likeRepository = new LikeRepository();
        saveRepository = new SaveRepository();
    }

    /**
     * Singleton implementation.
     * Returns the FirebaseHelper instance.
     *
     * @return  Instance of the database helper
     */
    public static FirebaseHelper getInstance() {
        return instance;
    }

    /**
     * Setter of clientId
     * @param clientId identifier of the current application user in session
     */
    public void setClientId(@NonNull String clientId){ this.clientId = clientId; }


    //Add Methods

    /**
     * Method that communicates with the newsItems repository to add the received {newsItem}.
     * When finished, it will call {callback} function with the added {newsItem}.
     *
     * @param newsItem      model object to be added
     * @param callback      function called at the end of the addition
     */
    public void addNewsItem(NewsItem newsItem, Function<NewsItem, Void> callback) { newsItemRepository.add(newsItem, n-> callback.apply(n)); }

    /**
     * Method that communicates with the likes repository to add a new like. It receives the
     * identifier of the liked newsItem ({idNewsItem}) and stores a Like taking into account
     * the user that likes it (the user in session) as well as the mentioned identifier.
     *
     * When finished, it will call {callback} function with the added like.
     *
     * @param idNewsItem    identifier of the liked newsItem
     * @param callback      function called at the end of the addition
     */
    public void addLike(String idNewsItem, Function<Like, Void> callback){
        DocumentReference likedNewsItem = createDocumentReference(LikeRepository.NEWS_ITEMS, idNewsItem);
        likeRepository.add(new Like(likedNewsItem, clientId), n-> callback.apply(n));
    }

    /**
     * Method that communicates with the saves repository to add a new save. It receives the
     * identifier of the saved newsItem ({idNewsItem}) and stores a Save taking into account
     * the user that saves it (the user in session) as well as the mentioned identifier.
     *
     * When finished, it will call {callback} function with the added save.
     *
     * @param idNewsItem    identifier of the saved newsItem
     * @param callback      function called at the end of the addition
     */
    public void addSave(String idNewsItem, Function<Save, Void> callback){
        DocumentReference savedNewsItem = createDocumentReference(SaveRepository.NEWS_ITEMS, idNewsItem);
        saveRepository.add(new Save(savedNewsItem, clientId), n-> callback.apply(n));
    }

    //Delete Methods

    /**
     * Method that communicates with the newsItems repository to remove the received {newsItem}.
     * When finished, it will call {callback} function with the removed {newsItem}.
     *
     * @param newsItem  model object to be deleted
     * @param callback  function called at the end of the removal
     */
    public void deleteNewsItem(NewsItem newsItem, Function<NewsItem, Void> callback){ newsItemRepository.delete(newsItem, n-> callback.apply(n)); }

    /**
     * Method that communicates with the likes repository to remove a like. It receives the
     * identifier of the liked newsItem ({idNewsItem}) and removes the Like taking into account
     * the user that liked it (the user in session) as well as the mentioned identifier.
     *
     * When finished, it will call {callback} function with the removed like.
     *
     * @param idNewsItem    identifier of the liked newsItem
     * @param callback      function called at the end of the removal
     */
    public void deleteLike(String idNewsItem, Function<Like, Void> callback) {
        DocumentReference likedNewsItem = createDocumentReference(LikeRepository.NEWS_ITEMS, idNewsItem);
        likeRepository.delete(new Like(likedNewsItem, clientId), n-> callback.apply(n));
    }

    /**
     * Method that communicates with the saves repository to remove a save. It receives the
     * identifier of the saved newsItem ({idNewsItem}) and removes the Save taking into account
     * the user that saved it (the user in session) as well as the mentioned identifier.
     *
     * When finished, it will call {callback} function with the removed save.
     *
     * @param idNewsItem    identifier of the saved newsItem
     * @param callback      function called at the end of the removal
     */
    public void deleteSave(String idNewsItem, Function<Save, Void> callback) {
        DocumentReference savedNewsItem = createDocumentReference(SaveRepository.NEWS_ITEMS, idNewsItem);
        saveRepository.delete(new Save(savedNewsItem, clientId), n-> callback.apply(n));
    }

    //Get All Methods

    /**
     * Method that communicates with the newsItems repository to get all newsItems.
     * When finished, it will call {callback} with the retrieved objects, one by one.
     *
     * @param callback      function called at the end of the process
     */
    public void getNewsItems(Function<NewsItem, Void> callback) { newsItemRepository.getAll(n -> callback.apply(n)); }

    /**
     * Method that communicates with the likes repository to get all likes associated
     * to the user in session. To provide greater information, instead of returning Like
     * objects, newsItems objects will be returned.
     *
     * When finished, it will call {callback} with the retrieved
     * objects (liked NewsItems), one by one, or with null if the like can't be retrieved.
     *
     * @param callback      function called at the end of the process
     */
    public void getLikedNewsItems(Function<NewsItem, Void> callback) {

        likeRepository.getAll(l -> {
            if(l == null || !clientId.equals(l.getUserId()))
                callback.apply(null);
            else {
                DocumentReference likedNewsItem = l.getNewsItemId(); //getting reference to NewsItem from Like object
                likedNewsItem.get()
                        .addOnCompleteListener(t -> callback.apply(t.getResult().toObject(NewsItem.class))); //mapping to NewsItem model object
            }
            return null;
    }); }

    /**
     * Method that communicates with the saves repository to get all saves associated
     * to the user in session. To provide greater information, instead of returning Save
     * objects, newsItems objects will be returned.
     *
     * When finished, it will call {callback} with the retrieved
     * objects (saved NewsItems), one by one, or with null if the save can't be retrieved.
     *
     * @param callback      function called at the end of the process
     */
    public void getSavedNewsItems(Function<NewsItem, Void> callback) {

        saveRepository.getAll(s -> {
            if(s == null || !clientId.equals(s.getUserId()))
                callback.apply(null);
            else{
                DocumentReference savedNewsItem = s.getNewsItemId(); //getting reference to NewsItem from Save object
                savedNewsItem.get()
                        .addOnCompleteListener(t -> callback.apply(t.getResult().toObject(NewsItem.class))); //mapping to NewsItem model object
            }

            return null;
    }); }

    //Get by id Methods

    /**
     * Method that gets the like with same identifier as the one received by
     * parameter ({idLike}) or null if the object does not exist on database.
     *
     * @param idLike    identifier of the Like to be returned
     * @param callback  function called at the end of the process
     */
    public void getLikeById(String idLike, Function<Like, Void> callback) {
        Pair checkEqualIdentifier = Pair.create("__name__", idLike); //condition
        likeRepository.get(checkEqualIdentifier, t -> callback.apply(t));
    }

    /**
     * Method that gets the save with same identifier as the one received by
     * parameter ({idSave}) or null if the object does not exist on database.
     *
     * @param idSave    identifier of the Save to be returned
     * @param callback  function called at the end of the process
     */
    public void getSaveById(String idSave, Function<Save, Void> callback) {
        Pair checkEqualIdentifier = Pair.create("__name__", idSave); //condition
        saveRepository.get(checkEqualIdentifier, t -> callback.apply(t));
    }

    /**
     * Method that gets the save with same identifier as the one received by
     * parameter ({idNewsItem}) or null if the object does not exist on database.
     *
     * @param idNewsItem    identifier of the NewsItem to be returned
     * @param callback      function called at the end of the process
     */
    public void getNewsItemById(String idNewsItem, Function<NewsItem, Void> callback) {
        Pair checkEqualIdentifier = Pair.create("__name__", idNewsItem); //condition
        newsItemRepository.get(checkEqualIdentifier, t -> callback.apply((NewsItem) t));
    }

    //Utility Methods

    /**
     * Method that gets the newsItem with identifier equal to {idNewsItem} if it was liked.
     *
     * It calls {callback} function with the newsItem object if it was liked, otherwise, it will
     * be called with null as parameter.
     *
     * @param idNewsItem    identifier of the newsItem model object
     * @param callback      function called at the end of the process
     */
    public void getNewsItemIfLiked(String idNewsItem, Function<NewsItem, Void> callback){
        getNewsItemIfInteraction(likeRepository, idNewsItem, callback);
    }

    /**
     * Method that gets the newsItem with identifier equal to {idNewsItem} if it was saved.
     *
     * It calls {callback} function with the newsItem object if it was saved, otherwise, it will
     * be called with null as parameter.
     *
     * @param idNewsItem    identifier of the newsItem model object
     * @param callback      function called at the end of the process
     */
    public void getNewsItemIfSaved(String idNewsItem, Function<NewsItem, Void> callback){
        getNewsItemIfInteraction(saveRepository, idNewsItem, callback);
    }

    /**
     * Auxiliary method that implements 'getNewsItemIfLiked' and 'getNewsItemIfSaved'.
     *
     * @param repository    repository of the interaction (likes or saves repository)
     * @param idNewsItem    identifier of the interacted newsItem (liked or saved)
     * @param callback      function called at the end of the process
     */
    private void getNewsItemIfInteraction(Repository repository, String idNewsItem, Function<NewsItem, Void> callback){

        DocumentReference referencedNewsItem = createDocumentReference(LikeRepository.NEWS_ITEMS, idNewsItem);  //liked or saved newsItem
        Pair newsItemIsReferenced = Pair.create(BaseRepository.getReferenceProperty(), referencedNewsItem);     //condition

        repository.get(newsItemIsReferenced, i -> {
            Interaction interaction = (Interaction) i;  //Like or Save class

            if(interaction == null || !interaction.getUserId().equals(clientId)) //it was not interacted
                callback.apply(null);
            else {
                DocumentReference newsItem = interaction.getNewsItemId();
                newsItem.get()
                        .addOnCompleteListener(t -> callback.apply(t.getResult().toObject(NewsItem.class))); //mapping to NewsItem model object
            }
            return null;
        });
    }

    /**
     * Auxiliary method that creates a document reference like 'news_items/ABC1234',
     * being 'ABC1234' the identifier of a document from 'news_items' collection.
     *
     * @param collection        collection where the referenced document is in
     * @param idToBeReferenced  identifier of the referenced document
     * @return                  reference to the document
     */
    private DocumentReference createDocumentReference(String collection, String idToBeReferenced){
        return FirebaseFirestore.getInstance().document(collection + "/" + idToBeReferenced);
    }

}
