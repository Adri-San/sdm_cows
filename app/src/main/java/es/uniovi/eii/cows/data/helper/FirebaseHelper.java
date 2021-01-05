package es.uniovi.eii.cows.data.helper;


import android.util.Pair;

import androidx.annotation.NonNull;

import java.util.function.Function;

import es.uniovi.eii.cows.data.BaseRepository;
import es.uniovi.eii.cows.data.repositories.LikeRepository;
import es.uniovi.eii.cows.data.repositories.NewsItemRepository;
import es.uniovi.eii.cows.data.repositories.SaveRepository;
import es.uniovi.eii.cows.model.Like;
import es.uniovi.eii.cows.model.NewsItem;
import es.uniovi.eii.cows.model.Save;


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
    public void addNewsItem(NewsItem newsItem, Function<NewsItem, Void> callback) { newsItemRepository.add(newsItem, n-> callback.apply((NewsItem) n)); }
    public void addLike(String idNewsItem, Function<Like, Void> callback){ likeRepository.add(new Like(likeRepository.createDocumentReference(LikeRepository.NEWS_ITEMS, idNewsItem), clientId), n-> callback.apply(n)); }
    public void addSave(String idNewsItem, Function<Save, Void> callback){ saveRepository.add(new Save(saveRepository.createDocumentReference(SaveRepository.NEWS_ITEMS, idNewsItem), clientId), n-> callback.apply(n)); }

    //Delete Methods
    public void deleteNewsItem(NewsItem newsItem, Function<NewsItem, Void> callback){ newsItemRepository.delete(newsItem, n-> callback.apply((NewsItem) n)); }
    public void deleteLike(String idNewsItem, Function<Like, Void> callback) { likeRepository.delete(new Like(likeRepository.createDocumentReference(LikeRepository.NEWS_ITEMS, idNewsItem), clientId), n-> callback.apply(n)); }
    public void deleteSave(String idNewsItem, Function<Save, Void> callback) { saveRepository.delete(new Save(saveRepository.createDocumentReference(SaveRepository.NEWS_ITEMS, idNewsItem), clientId), n-> callback.apply(n));}

    //Get All Methods
    public void getNewsItems(Function<NewsItem, Void> callback) { newsItemRepository.getAll(n -> callback.apply((NewsItem) n)); }
    public void getLikedNewsItems(Function<NewsItem, Void> callback) { likeRepository.getAll(l -> callback.apply(l.getNewsItemId().get().getResult().toObject(NewsItem.class))); }
    public void getSavedNewsItems(Function<NewsItem, Void> callback) { saveRepository.getAll(s -> callback.apply(s.getNewsItemId().get().getResult().toObject(NewsItem.class))); }

    //Get by id Methods
    public void getLikeById(String idLike, Function<Like, Void> callback) {likeRepository.get( Pair.create("__name__", idLike), t -> callback.apply(t));}
    public void getSaveById(String idSave, Function<Save, Void> callback) {saveRepository.get( Pair.create("__name__", idSave), t -> callback.apply(t));}
    public void getNewsItemById(String idNewsItem, Function<NewsItem, Void> callback) {newsItemRepository.get( Pair.create("__name__", idNewsItem), t -> callback.apply((NewsItem) t));}

    public void setClientId(@NonNull String clientId){
        this.clientId = clientId;
    }


}
